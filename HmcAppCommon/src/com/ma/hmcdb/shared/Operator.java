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

@Entity
public class Operator {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "", sortable = false, isImage = true, widthEM = 10)
	public String avatar;
	
	@UILabel(label = "Имя", sortable = true)
	public String name;
	
	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	//@UILabel(label = "Removed")
	public Boolean removed;

	public Date modifTime;
	public Date storeTime;

	public Operator() {
	}

	public Operator(String name, Company company) {
		this.name = name;
		this.company = company;
	}

}
