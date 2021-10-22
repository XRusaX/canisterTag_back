package com.ma.hmcrfidserver.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.UserService;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.gwtapp.server.SpringRemoteServiceServlet;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcapp.datasource.CompanyDataSource;
import com.ma.hmcapp.datasource.RoomCellDataSource;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Company.CompanyType;
import com.ma.hmcdb.shared.Permissions;
import com.ma.hmcdb.shared.RoomCell;
import com.ma.hmcrfidserver.client.HmcService;

@SuppressWarnings("serial")
@WebServlet("/hmcrfidserver/hmc")
public class HmcServiceImpl extends SpringRemoteServiceServlet implements HmcService {

	@Autowired
	private UserService userService;

	@Autowired
	private Database2 database;

	@Autowired
	private FirmwareController firmwareController;

	@Autowired
	private RoomCellDataSource roomCellDataSource;

	@Autowired
	private CompanyDataSource companyDataSource;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@Override
	public void saveRoomCells(List<CDObject> list, long companyId) throws Exception {
		database.execVoid(em -> {
			Company company = companyDataSource.load(em, companyId);
			roomCellDataSource.updateCells(em, company, list.stream().map(cdo -> {
				RoomCell obj = new RoomCell();
				commonDataImpl.setFields(em, obj, cdo);
				return obj;
			}).collect(Collectors.toList()));
		});
	}

	@Override
	public Map<String, String> getFirmwareList() {
		return firmwareController.getFirmwareList();
	}

	@Override
	public void addUserCompany(String newCompanyName, String adminName, String adminPasswordHash, String adminEmail)
			throws IOException {

		database.execVoid(em -> {
			Company company;
			try {
				company = new Company(newCompanyName, CompanyType.CUSTOMER, null, null, null);
				companyDataSource.store(em, company);
			} catch (Exception e) {
				if (e.getCause() instanceof ConstraintViolationException) {
					throw new RuntimeException("Такая компания уже существует");
				}
				throw e;
			}

			try {
				userService.addUser(adminName, adminPasswordHash, Arrays.asList(Permissions.PERMISSION_CUSTOMER,
						UserData.PERMISSION_USERS, UserData.PERMISSION_SETTINGS), adminEmail, company.id);
			} catch (Exception e) {
				throw new RuntimeException("Пользователь с таким именем уже существует");
			}
		});
	}
}
