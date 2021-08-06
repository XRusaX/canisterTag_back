package ru.aoit.hmcapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import ru.aoit.appcommon.auth.UserServiceDB;
import ru.aoit.appcommon.shared.auth.UserData;
import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Permissions;

@Component
public class HmcUserService extends UserServiceDB {
	{
		addPermission(Permissions.PERMISSION_TEST, "Тестирование плат");
		addPermission(Permissions.PERMISSION_WRITE_RFID, "Запись меток");
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
