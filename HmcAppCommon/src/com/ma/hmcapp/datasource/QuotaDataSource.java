package com.ma.hmcapp.datasource;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.AuthComponent;
import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Agent;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.rfid.Quota;

@Component
public class QuotaDataSource extends DataSourceImpl<Quota> {

	@Autowired
	private Database2 database;

	@Autowired
	private AuthComponent authComponent;

	@Autowired
	private CommonData commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(Quota.class, database);
		commonDataImpl.addDataSource(Quota.class, this);
	}

	@Override
	protected void onAfterCreate(Quota quota) {
		quota.userName = authComponent.getUser().name;
		quota.time = new Date();
	}

	public List<Quota> get(EM conn, Company company, Agent agent, int canisterVolume) {
		List<Quota> quotas = conn.em
				.createQuery(
						"select q from Quota q where company=:company and agent=:agent and volume=:volume and remain>0",
						Quota.class)//
				.setParameter("company", company)//
				.setParameter("agent", agent)//
				.setParameter("volume", canisterVolume)//
				.getResultList();
		return quotas;
	}

}
