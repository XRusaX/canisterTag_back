package com.ma.hmcapp.datasource;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.rfid.RfidLabel;

@Component
public class RfidLabelDataSource extends DataSourceImpl<RfidLabel> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonData commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(RfidLabel.class, database);
		commonDataImpl.addDataSource(RfidLabel.class, this);
	}

	public List<RfidLabel> loadByName(EM em, Integer canisterId) {
		return Database2.select(em.em, RfidLabel.class).whereEQ("name", canisterId).getResultList();
	}

}
