package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Room;

@Component
public class RoomDataSource extends DataSourceImpl<Room> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(Room.class, database);
		commonDataImpl.addDataSource(Room.class, this);
	}

	public Room loadByName(EM em, String name, Company company) {
		return Database2.select(em.em, Room.class).whereEQ("name", name).whereEQ("company", company).getResultStream()
				.findFirst().orElse(null);
	}

}
