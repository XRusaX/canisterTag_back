package ru.aoit.hmcrfidserver.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.RpcServlet;
import ru.aoit.appcommon.SpringUtils;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.hmc.rfid.rpcinterface.TestRpcInterface;
import ru.aoit.hmcdb.shared.Agent;
import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Company.CompanyType;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.Operator;
import ru.aoit.hmcdb.shared.Room;
import ru.aoit.hmcdb.shared.RoomCell;
import ru.aoit.hmcdb.shared.rfid.Quota;
import ru.nppcrts.common.shared.color.ColorX;

@SuppressWarnings("serial")
@WebServlet(TestRpcInterface.servletPath)
public class TestRpcServlet extends RpcServlet implements TestRpcInterface {

	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;

	private int nextColor;

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		SpringUtils.autowire(request, this);
		super.service(request, response);
	}

	@Override
	public void clear() throws IOException {
		database.execVoid(em -> {
			em.createQuery("delete from Company").executeUpdate();
		});
	}

	@Override
	public void createCustomerCompany(String name) throws IOException {
		database.execVoid(em -> {
			Company company = new Company(name, CompanyType.CUSTOMER, "addr", "contacts", 0);
			em.persist(company);
		});
	}

	@Override
	public void createHmc(String serialNum, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = Database2.select(em, Company.class).whereEQ("name", companyName).getResultStream()
					.findFirst().orElse(null);
			Hmc hmc = new Hmc(serialNum, company);
			em.persist(hmc);
		});
	}

	@Override
	public void createRoom(String roomName, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = Database2.select(em, Company.class).whereEQ("name", companyName).getResultStream()
					.findFirst().orElse(null);
			Room room = new Room(roomName, String.format("#%06X", ColorX.contrastColors[nextColor].value), company);
			em.persist(room);

			for (int x = 0; x < 3; x++)
				for (int y = 0; y < 3; y++) {
					RoomCell roomCell = new RoomCell(company, null, room, x + nextColor * 4, y);
					em.persist(roomCell);
				}

			nextColor = (nextColor + 1) % ColorX.contrastColors.length;
		});
	}

	@Override
	public void createOperator(String operatorName, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = Database2.select(em, Company.class).whereEQ("name", companyName).getResultStream()
					.findFirst().orElse(null);
			Operator operator = new Operator(operatorName, company);
			em.persist(operator);
		});
	}

	@Override
	public void createQuota() throws IOException {
		database.execVoid(em -> {
			{
				Company company = new Company("testcompany", CompanyType.TEST, "addr", "contacts", 0);
				em.persist(company);
				em.createQuery("update UserData set company=" + company.id + ", companyName='" + company.name
						+ "' where name='t1'").executeUpdate();
			}

			Company company = new Company("завод1", CompanyType.CANISTER, "addr", "contacts", 10);
			em.persist(company);
			em.createQuery("update UserData set company=" + company.id + ", companyName='" + company.name
					+ "' where name='u1'").executeUpdate();

			Agent agent = Database2.select(em, Agent.class).whereEQ("name", "Гриндез").getSingleResult();

			Quota q = new Quota("u1", company, agent, 3000, 10000);
			em.persist(q);
		});
	}

}
