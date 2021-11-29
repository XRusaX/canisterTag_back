package com.ma.hmcserver.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ma.appcommon.UserService;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.hmcapp.datasource.CompanyDataSource;
import com.ma.hmcapp.servlet.FirmwareController;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.shared.CompanyType;
import com.ma.hmcdb.shared.Permissions;
import com.ma.hmcserver.client.HmcService;

@Service("hmc")
public class HmcServiceImpl implements HmcService {

	@Autowired
	private UserService userService;

	@Autowired
	private Database2 database;

	@Autowired
	private FirmwareController firmwareController;

	@Autowired
	private CompanyDataSource companyDataSource;

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

	@Override
	public void removeFirmware(String type) {
		try {
			firmwareController.removeFirmware(type);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
