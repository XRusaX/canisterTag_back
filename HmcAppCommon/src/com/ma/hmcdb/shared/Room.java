package com.ma.hmcdb.shared;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.commonui.shared.annotations.UILabel;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Название", sortable = true)
	public String name;

//	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RoomLayer roomLayer;
	

	public Integer x;

	public Integer y;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RoomLayer layer;

	public Date lastModified;

	public Room() {
	}
	
	public Room(String name, Company company, RoomLayer roomLayer) {
		this.name = name;
		this.company = company;
		this.roomLayer = roomLayer;
	}
	
	public Room(String name, RoomLayer roomLayer, Company company) {
		this.name = name;
		this.company = company;
		this.roomLayer = roomLayer;
	}

}
