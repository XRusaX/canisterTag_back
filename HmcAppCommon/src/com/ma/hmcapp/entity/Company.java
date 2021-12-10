package com.ma.hmcapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcapp.shared.CompanyType;

import lombok.Data;

@Entity
@Data
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	@UILabel(label = "Название", sortable = true)
	private String name;

	@UILabel(label = "Тип", sortable = true)
	@Enumerated(EnumType.STRING)
	private CompanyType companyType;

	@UILabel(label = "Адрес", sortable = true)
	private String addr;

	@UILabel(label = "Контакты", sortable = true)
	private String contacts;

	@UILabel(label = "Размер блока меток", sortable = true)
	private Integer rfidBlockSize;

	public Company() {
	}

	public Company(String name, CompanyType companyType, String addr, String contacts, Integer rfidBlockSize) {
		this.name = name;
		this.companyType = companyType;
		this.addr = addr;
		this.contacts = contacts;
		this.rfidBlockSize = rfidBlockSize;
	}

}
