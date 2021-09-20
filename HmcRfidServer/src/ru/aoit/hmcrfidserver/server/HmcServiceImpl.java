package ru.aoit.hmcrfidserver.server;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaUpdate;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import ru.aoit.appcommon.CommonDataHelpers;
import ru.aoit.appcommon.Database2;
import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.RoomCell;
import ru.aoit.hmcrfidserver.client.HmcService;
import ru.nppcrts.common.cd.CDUtils;
import ru.nppcrts.common.gwt.app.server.SpringRemoteServiceServlet;
import ru.nppcrts.common.shared.cd.CDObject;

@SuppressWarnings("serial")
@WebServlet("/hmcrfidserver/hmc")
public class HmcServiceImpl extends SpringRemoteServiceServlet implements HmcService {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataHelpers helpers;

	@Override
	public void saveRoomCells(List<CDObject> list, long companyId) throws Exception {
		try {
			Class<?> clazz = RoomCell.class;
			database.execVoid(em -> {
				Company company = em.find(Company.class, companyId);
				em.createQuery("delete from RoomCell rc where company=:company").setParameter("company", company).executeUpdate();
				CDUtils2 utils = new CDUtils2(em);
				for (CDObject cdObject : list) {
					Object obj = clazz.newInstance();
					helpers.onBeforeStore(em, obj);
					utils.setCDObject(obj, cdObject);
					em.merge(obj);
					helpers.onAfterStore(em, obj);
				}
			});

			database.incrementTableVersion(clazz);

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

}
