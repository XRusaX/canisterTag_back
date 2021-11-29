package com.ma.hmcdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcdb.shared.CompanyType;

@Entity
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column(unique = true)
	@UILabel(label = "Название", sortable = true)
	public String name;

	@UILabel(label = "Тип", sortable = true)
	@Enumerated(EnumType.STRING)
	public CompanyType companyType;

	@UILabel(label = "Адрес", sortable = true)
	public String addr;

	@UILabel(label = "Контакты", sortable = true)
	public String contacts;

	@UILabel(label = "Размер блока меток", sortable = true)
	public Integer rfidBlockSize;

//	public byte[] plan;
	
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
