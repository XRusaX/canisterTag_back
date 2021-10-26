package com.ma.hmcapp.datasource;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Operator;

@Component
public class OperatorDataSource extends DataSourceImpl<Operator> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@PostConstruct
	private void init() {
		super.init(Operator.class, database);
		commonDataImpl.addDataSource(Operator.class, this);
	}

	public Operator loadByName(EM em, String name, Company company) {
		return Database2.select(em.em, Operator.class).whereEQ("name", name).whereEQ("company", company)
				.getResultStream().findFirst().orElse(null);
	}

//	@Override
//	protected synchronized void onBeforeStore(EM em, Operator obj) {
//		if (obj.lastModified == null)
//			obj.lastModified = new Date();
//	}

	public synchronized boolean updateOperators(EM em, List<com.ma.hmc.iface.ping.Operator> operators,
			Company company) {
		boolean changed = false;
		for (com.ma.hmc.iface.ping.Operator o : operators)
			changed |= updateOperator(em, o, company);
		return changed;
	}

	private boolean updateOperator(EM em, com.ma.hmc.iface.ping.Operator o, Company company) {

		Operator operator = null;
		if (o.id != null) {
			operator = load(em, o.id);
			if (operator == null) {
				return true;
			} else if (operator.company != company) {
				msgLogger.add(null, Severity.WARNING, "Компания оператора и владелец МГЦ не совпадают");
				return true;
			} else if (o.removed) {
//				operator.removed = true;
//				store(em, operator);
				delete(em, Arrays.asList(o.id));
				return true;
			}
		} else {
			operator = loadByName(em, o.name, company);
		}

		if (operator != null) {
//			if (operator.lastModified.getTime() < o.lastModified) {
//				operator.name = o.name;
//				store(em, operator);
//				return true;
//			}
		} else {
			operator = new Operator(o.name, company);
			store(em, operator);
		}
		return false;
	}

	public List<Operator> getModified(EM em, Company company, long lastSync) {
		List<Operator> list = em.em
				.createQuery("select * from Operator where company=:company and lastModified>:lastSync", Operator.class)
				.setParameter("company", company).setParameter("lastSync", lastSync).getResultList();
		// TODO Auto-generated method stub
		return null;
	}

}
