package com.ma.hmcrfidserver.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.ma.appcommon.CommonDataHelpers;
import com.ma.appcommon.Database2;
import com.ma.appcommon.UserService;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.cd.CDUtils;
import com.ma.common.gwt.app.server.SpringRemoteServiceServlet;
import com.ma.common.shared.cd.CDObject;
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
	private CommonDataHelpers helpers;

	@Autowired
	private FirmwareController firmwareController;

	@Override
	public void saveRoomCells(List<CDObject> list, long companyId) throws Exception {
		try {
			database.execVoid(em -> {
				Company company = em.find(Company.class, companyId);
				em.createQuery("delete from RoomCell rc where company=:company").setParameter("company", company)
						.executeUpdate();
				CDUtils2 utils = new CDUtils2(em);
				for (CDObject cdObject : list) {
					Object obj = new RoomCell();
					helpers.onBeforeStore(em, obj);
					utils.setCDObject(obj, cdObject);
					em.merge(obj);
					helpers.onAfterStore(em, obj);
					database.incrementTableVersion(em, obj);
				}
			});


		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}

	public static class CDUtils2 extends CDUtils {

		private EntityManager entityManager;

		public CDUtils2(EntityManager entityManager) {
			this.entityManager = entityManager;
		}

		@Override
		protected <T> T loadObject(Class<T> type, long id) {
			return entityManager.find(type, id);
		}
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
					em.persist(company);
				} catch (Exception e) {
					if (e.getCause() instanceof ConstraintViolationException) {
						throw new IOException("Такая компания уже существует");
					}
					throw e;
				}

				try {
					userService.addUser(adminName, adminPasswordHash, Arrays.asList(Permissions.PERMISSION_USER,
							UserData.PERMISSION_USERS, UserData.PERMISSION_SETTINGS), adminEmail, company.id);
				} catch (Exception e) {
					throw new IOException("Пользователь с таким именем уже существует");
				}
			});
	}
}
