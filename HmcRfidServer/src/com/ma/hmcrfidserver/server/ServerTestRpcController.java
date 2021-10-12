package com.ma.hmcrfidserver.server;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.Database2;
import com.ma.appcommon.rpc.RpcController;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.shared.color.ColorX;
import com.ma.hmc.iface.servertest.rpcinterface.ServerTestRpcInterface;
import com.ma.hmc.iface.shared.HmcType;
import com.ma.hmcdb.shared.Agent;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Company.CompanyType;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Operator;
import com.ma.hmcdb.shared.Permissions;
import com.ma.hmcdb.shared.Room;
import com.ma.hmcdb.shared.rfid.Quota;

@RestController
@RequestMapping(ServerTestRpcInterface.servletPath)
public class ServerTestRpcController extends RpcController implements ServerTestRpcInterface {

	@Autowired
	private Database2 database;

	// @Autowired
	// private MsgLoggerImpl msgLogger;

	private int nextColor;

	@Override
	public void clear() throws IOException {
		try {
			database.execVoid(em -> {
				em.createQuery("delete from Company").executeUpdate();
				em.createQuery("delete from UserData").executeUpdate();

				em.persist(new UserData("admin", AuthUtils.getPwdHash("admin", "admin"),
						Arrays.asList(UserData.PERMISSIONS_ALL), null, null));

				Company testCompany = new Company("testcompany", CompanyType.TEST, "addr", "contacts", 0);
				em.persist(testCompany);
				em.persist(new UserData("t", AuthUtils.getPwdHash("t", "t"), Arrays.asList(Permissions.PERMISSION_TEST),
						null, testCompany.id));

				Company canisterCompany = new Company("завод1", CompanyType.CANISTER, "addr", "contacts", 10);
				em.persist(canisterCompany);
				UserData user_u1 = new UserData("u1", AuthUtils.getPwdHash("u1", "p1"),
						Arrays.asList(Permissions.PERMISSION_WRITE_RFID), null, canisterCompany.id);
				em.persist(user_u1);
				Agent agent = Database2.select(em, Agent.class).whereEQ("name", "Гриндез").getSingleResult();
				Quota q = new Quota(user_u1.name, canisterCompany, agent, 3000, 10000);
				em.persist(q);
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void createCustomerCompany(String name, String user, String password) throws IOException {
		database.execVoid(em -> {
			Company company = new Company(name, CompanyType.CUSTOMER, "addr", "contacts", 0);
			em.persist(company);
			if (user != null) {
				UserData userData = new UserData(user, AuthUtils.getPwdHash(user, password),
						Arrays.asList(UserData.PERMISSION_USERS, Permissions.PERMISSION_CUSTOMER), null, company.id);
				em.persist(userData);
			}
		});
	}

	@Override
	public void createHmc(HmcType hmcType, String serialNum, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = Database2.select(em, Company.class).whereEQ("name", companyName).getResultStream()
					.findFirst().orElse(null);
			Hmc hmc = new Hmc(hmcType, serialNum, company);
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

			// for (int x = 0; x < 3; x++)
			// for (int y = 0; y < 3; y++) {
			// RoomCell roomCell = new RoomCell(company, null, room, x +
			// nextColor * 4, y, null);
			// em.persist(roomCell);
			// }

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

}
