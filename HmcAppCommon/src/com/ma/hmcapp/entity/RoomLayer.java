package com.ma.hmcapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;

import lombok.Data;

@Data
@Entity
public class RoomLayer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Имя", sortable = true)
	private String name;

	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;

//	@Column(length = 10000000)
	private String imageUrl;

	public RoomLayer() {
	}

	public RoomLayer(String name, Company company, String imageUrl) {
		this.name = name;
		this.company = company;
		this.imageUrl = imageUrl;
	}

}
