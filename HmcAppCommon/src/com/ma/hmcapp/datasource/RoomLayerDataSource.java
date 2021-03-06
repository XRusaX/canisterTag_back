package com.ma.hmcapp.datasource;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcapp.entity.Company;
import com.ma.hmcapp.entity.RoomLayer;

@Component
public class RoomLayerDataSource extends DataSourceImpl<RoomLayer> {

	@Autowired
	private CommonData commonDataImpl;

	public RoomLayerDataSource() {
		super(RoomLayer.class);
	}

	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(RoomLayer.class, this);
	}

	public List<RoomLayer> loadLayersByCompany(EM em, Company company) {
		return Database2.select(em.em, RoomLayer.class).whereEQ("company", company).getResultStream()
				.collect(Collectors.toList());
	}

	public RoomLayer loadByCompany(EM em, Company company) {
		RoomLayer layer = Database2.select(em.em, RoomLayer.class).whereEQ("company", company).getResultStream()
				.findFirst().orElse(null);
		return layer;
	}
}
