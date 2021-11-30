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
import com.ma.hmcdb.shared.synchronizer.ModifTime;
import com.ma.hmcdb.shared.synchronizer.Name;
import com.ma.hmcdb.shared.synchronizer.StoreTime;

@Entity
public class Operator {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "", sortable = false, isImage = true, widthEM = 10)
	public String avatar;
	
	@Name
	@UILabel(label = "Имя", sortable = true)
	public String name;
	
	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne
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