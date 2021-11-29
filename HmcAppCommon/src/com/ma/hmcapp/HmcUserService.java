package com.ma.hmcapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import com.ma.appcommon.auth.UserServiceDB;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.shared.Permissions;

@Component
public class HmcUserService extends UserServiceDB {
	{
		addPermission(Permissions.PERMISSION_TEST, "Тестирование плат");
		addPermission(Permissions.PERMISSION_WRITE_RFID, "Запись меток");
		addPermission(Permissions.PERMISSION_CUSTOMER, "Пользователь");
	}

	@Override
	protected void addCompanies(EntityManager em, List<UserData> users) {
		users.forEach(user -> {
			if (user.company != null) {
				Company company = em.find(Company.class, user.company);
				if (company != null)
					user.companyName = company.name;
				else
					user.companyName = "---";
			} else
				user.companyName = "---";
		});

	}
}
