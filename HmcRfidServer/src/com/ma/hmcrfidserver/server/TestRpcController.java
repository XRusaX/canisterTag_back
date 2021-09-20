package com.ma.hmcrfidserver.server;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.appcommon.rpc.RpcController;
import com.ma.common.shared.color.ColorX;
import com.ma.hmc.rfid.rpcinterface.TestRpcInterface;
import com.ma.hmcdb.shared.Agent;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Operator;
import com.ma.hmcdb.shared.Room;
import com.ma.hmcdb.shared.RoomCell;
import com.ma.hmcdb.shared.Company.CompanyType;
import com.ma.hmcdb.shared.rfid.Quota;

@RestController
@RequestMapping(TestRpcInterface.servletPath)
public class TestRpcController extends RpcController implements TestRpcInterface {

	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;

	private int nextColor;

	@Override
	public void clear() throws IOException {
		try {
			database.execVoid(em -> {
				em.createQuery("delete from Company").executeUpdate();
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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