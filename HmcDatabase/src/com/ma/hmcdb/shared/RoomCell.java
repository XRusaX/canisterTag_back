package com.ma.hmcdb.shared;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class RoomCell {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RoomLayer layer;

	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Room room;

	public int x;
	public int y;

	public enum WallType {
		DOOR, WINDOW//
	}

	@Enumerated(EnumType.STRING)
	public WallType wallType;

	public RoomCell() {
	}

	public RoomCell(Company company, RoomLayer layer, Room room, int x, int y, WallType wallType) {
		this.company = company;
		this.layer = layer;
		this.room = room;
		this.x = x;
		this.y = y;
		this.wallType = wallType;
	}
}
