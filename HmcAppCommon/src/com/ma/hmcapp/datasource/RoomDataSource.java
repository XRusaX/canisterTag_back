package com.ma.hmcapp.datasource;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.Room;
import com.ma.hmcdb.entity.RoomLayer;

@Component
public class RoomDataSource extends DataSourceImpl<Room> {

	@Autowired
	private CommonData commonDataImpl;

	public RoomDataSource() {
		super(Room.class);
	}

	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(Room.class, this);
	}

	public List<Room> loadByLayer(EM em, RoomLayer roomLayer) {
		return Database2.select(em.em, Room.class).whereEQ("roomLayer", roomLayer).getResultStream()
				.collect(Collectors.toList());
	}

	@Override
	protected void onBeforeStore(EM em, Room obj) {
		if (obj.getLastModified() == null)
			obj.setLastModified(new Date());
	}

	public Room loadByName(EM em, String name, Company company) {
		return Database2.select(em.em, Room.class).whereEQ("company", company).whereEQ("name", name).getResultStream()
				.findFirst().get();
	}
}
