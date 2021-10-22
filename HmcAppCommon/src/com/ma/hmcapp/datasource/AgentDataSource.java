package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.Agent;

@Component
public class AgentDataSource extends DataSourceImpl<Agent> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(Agent.class, database);
		commonDataImpl.addDataSource(Agent.class, this);
	}

	public Agent getByName(EM conn, String agentName) {
		Agent agent = conn.em.createQuery("select a from Agent a where a.name=:name", Agent.class)
				.setParameter("name", agentName).getResultStream().findFirst().orElse(null);
		return agent;
	}

}
