package com.ma.hmcdb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Название", sortable = true)
	public String name;

//	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RoomLayer roomLayer;
	

	public Integer x;

	public Integer y;
	
	@ManyToOne
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
