package com.ma.hmcdb.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcdb.shared.synchronizer.CompanyField;

import lombok.Data;

@Data
@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Название", sortable = true)
	private String name;

//	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@CompanyField
	private Company company;

	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RoomLayer roomLayer;
	

	private Integer x;

	private Integer y;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RoomLayer layer;

	private Date lastModified;

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
