package com.ma.hmcdb.shared;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcapp.synchronizer.CompanyField;
import com.ma.hmcapp.synchronizer.ModifTime;
import com.ma.hmcapp.synchronizer.Name;
import com.ma.hmcapp.synchronizer.StoreTime;

@Entity
public class Operator {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Name
	@UILabel(label = "Имя", sortable = true)
	public String name;

	@CompanyField
	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	// @UILabel(label = "Removed")
	public Boolean removed;

	@ModifTime
	public Date modifTime;
	
	@StoreTime
	public Date storeTime;

	public Operator() {
	}

	public Operator(String name, Company company) {
		this.name = name;
		this.company = company;
	}

	public Operator(long id, String name, Company company, Boolean removed, Date modifTime, Date storeTime) {
		this.id = id;
		this.name = name;
		this.company = company;
		this.removed = removed;
		this.modifTime = modifTime;
		this.storeTime = storeTime;
	}
}
