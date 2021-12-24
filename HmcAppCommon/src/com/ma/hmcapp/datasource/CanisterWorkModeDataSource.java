package com.ma.hmcapp.datasource;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.shared.Filter;
import com.ma.hmcapp.entity.Agent;
import com.ma.hmcapp.entity.CanisterWorkMode;

@Component
public class CanisterWorkModeDataSource extends DataSourceImpl<CanisterWorkMode> {

	@Autowired
	private CommonData commonDataImpl;

	public CanisterWorkModeDataSource() {
		super(CanisterWorkMode.class);
	}

	public void clear(EM em) {
		em.em.createQuery("delete from CanisterWorkMode").executeUpdate();
	}

	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(CanisterWorkMode.class, this);
	}

	public List<CanisterWorkMode> getByAgent(EM conn, Agent agent) {

		return loadRange(conn, new Filter().addEQ("agent", "" + agent.getId()), null).right;

//		CanisterWorkMode workMode = conn.em
//				.createQuery("select a from CanisterWorkMode a where a.name=:name", CanisterWorkMode.class)
//				.setParameter("name", agent.getName()).getResultStream().findFirst().orElse(null);
//		return workMode;
	}

}
