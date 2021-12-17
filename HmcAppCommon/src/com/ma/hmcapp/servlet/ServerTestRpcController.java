package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.appcommon.UserService;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.rpc.RpcController;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.hmc.iface.servertest.rpcinterface.ServerTestRpcInterface;
import com.ma.hmc.iface.shared.HmcType;
import com.ma.hmcapp.datasource.AgentDataSource;
import com.ma.hmcapp.datasource.CompanyDataSource;
import com.ma.hmcapp.datasource.HmcDataSource;
import com.ma.hmcapp.datasource.OperatorDataSource;
import com.ma.hmcapp.datasource.QuotaDataSource;
import com.ma.hmcapp.datasource.RoomDataSource;
import com.ma.hmcapp.entity.Agent;
import com.ma.hmcapp.entity.Company;
import com.ma.hmcapp.entity.Hmc;
import com.ma.hmcapp.entity.Operator;
import com.ma.hmcapp.entity.Room;
import com.ma.hmcapp.entity.rfid.Quota;
import com.ma.hmcapp.shared.CompanyType;
import com.ma.hmcapp.shared.Permissions;

@RestController
@RequestMapping(ServerTestRpcInterface.servletPath)
public class ServerTestRpcController extends RpcController implements ServerTestRpcInterface {

	@Autowired
	private Database2 database;

	@Autowired
	private CompanyDataSource companyDataSource;

	@Autowired
	private QuotaDataSource quotaDataSource;

	@Autowired
	private RoomDataSource roomDataSource;

	@Autowired
	private OperatorDataSource operatorDataSource;

	@Autowired
	private HmcDataSource hmcDataSource;

	@Autowired
	private UserService userService;

	@Autowired
	private AgentDataSource agentDataSource;

	@Override
	public void clear() throws IOException {
		database.execVoid(em -> {
			companyDataSource.clear(em);
			agentDataSource.clear(em);
			try {
				userService.clear();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		AtomicLong id = new AtomicLong();

		database.execVoid(em -> {
			try {
				agentDataSource.store(em, new Agent("Гриндез", 1000, 100, 10, 10, "гипохлорид", 3));

				userService.addUser("admin", AuthUtils.getPwdHash("admin", "admin"),
						Arrays.asList(UserData.PERMISSIONS_ALL), null, null);

				Company testCompany = new Company("testcompany", CompanyType.TEST, "addr", "contacts", 0);
				companyDataSource.store(em, testCompany);
				userService.addUser("t", AuthUtils.getPwdHash("t", "t"), Arrays.asList(Permissions.PERMISSION_TEST),
						null, testCompany.getId());

				Company canisterCompany = new Company("завод1", CompanyType.CANISTER, "addr", "contacts", 10);
				companyDataSource.store(em, canisterCompany);
				// canisterCompany = companyDataSource.load(em,
				// canisterCompanyID);

				UserData user_u1 = userService.addUser("u1", AuthUtils.getPwdHash("u1", "p1"),
						Arrays.asList(Permissions.PERMISSION_WRITE_RFID), null, canisterCompany.getId());

				Agent agent = agentDataSource.getByName(em, "Гриндез");

				Quota q = new Quota(user_u1.name, canisterCompany, agent, 3000, 10000);
				quotaDataSource.store(em, q);
				id.set(q.getId());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public void createCustomerCompany(String name, String user, String password) throws IOException {
		database.execVoid(em -> {
			Company company = new Company(name, CompanyType.CUSTOMER, "addr", "contacts", 0);
			companyDataSource.store(em, company);
			if (user != null) {
				try {
					userService.addUser(user, AuthUtils.getPwdHash(user, password),
							Arrays.asList(UserData.PERMISSION_USERS, Permissions.PERMISSION_CUSTOMER), null,
							company.getId());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public void createHmc(HmcType hmcType, String serialNum, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = companyName == null ? null : companyDataSource.getByName(em, companyName);
			Hmc hmc = new Hmc(hmcType, serialNum, company);
			hmcDataSource.store(em, hmc);
		});
	}

	@Override
	public void createRoom(String roomName, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = companyDataSource.getByName(em, companyName);
			Room room = new Room(roomName, null, company);
			roomDataSource.store(em, room);
		});
	}

	@Override
	public void createOperator(String operatorName, String companyName) throws IOException {
		database.execVoid(em -> {
			Company company = companyDataSource.getByName(em, companyName);
			Operator operator = new Operator(operatorName, company);
			operatorDataSource.store(em, operator);
		});
	}

}
