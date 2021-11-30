package com.ma.hmcdb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;

@Entity
public class RoomLayer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Имя", sortable = true)
	public String name;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

//	@Column(length = 10000000)
	public String imageUrl;

	public RoomLayer() {
	}

	public RoomLayer(String name, Company company, String imageUrl) {
		this.name = name;
		this.company = company;
		this.imageUrl = imageUrl;
	}

}
