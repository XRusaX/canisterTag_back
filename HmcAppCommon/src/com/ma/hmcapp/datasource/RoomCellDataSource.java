package com.ma.hmcapp.datasource;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.RoomCell;

@Component
public class RoomCellDataSource extends DataSourceImpl<RoomCell> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonData commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(RoomCell.class, database);
		commonDataImpl.addDataSource(RoomCell.class, this);
	}

	public void updateCells(EM em, Company company, List<RoomCell> list) {
		em.em.createQuery("delete from RoomCell rc where company=:company").setParameter("company", company)
				.executeUpdate();
		for (RoomCell roomCell : list)
			store(em, roomCell);
	}

}
