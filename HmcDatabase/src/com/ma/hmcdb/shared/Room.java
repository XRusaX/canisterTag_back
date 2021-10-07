package com.ma.hmcdb.shared;

import javax.persistence.CascadeType;
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

	//@UILabel(label = "Color", sortable = true)
	public String color;

	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	public Room() {
	}

	public Room(String name, String color, Company company) {
		this.name = name;
		this.color = color;
		this.company = company;
	}

}
