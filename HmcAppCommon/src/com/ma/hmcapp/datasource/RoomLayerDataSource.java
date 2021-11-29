package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.RoomLayer;

@Component
public class RoomLayerDataSource extends DataSourceImpl<RoomLayer> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonData commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(RoomLayer.class, database);
		commonDataImpl.addDataSource(RoomLayer.class, this);
	}

	public RoomLayer loadByCompany(EM em, Company company) {
		RoomLayer layer = Database2.select(em.em, RoomLayer.class).whereEQ("company", company).getResultStream()
				.findFirst().orElse(null);
		return layer;
	}

}
