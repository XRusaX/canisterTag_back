package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.hmcapp.entity.Agent;

@Component
public class AgentDataSource extends DataSourceImpl<Agent> {

	@Autowired
	private CommonData commonDataImpl;

	public AgentDataSource() {
		super(Agent.class);
	}
	
	public void clear(EM em) {
		em.em.createQuery("delete from Agent").executeUpdate();
	}

	
	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(Agent.class, this);
	}

	public Agent getByName(EM conn, String agentName) {
		Agent agent = conn.em.createQuery("select a from Agent a where a.name=:name", Agent.class)
				.setParameter("name", agentName).getResultStream().findFirst().orElse(null);
		return agent;
	}

}
