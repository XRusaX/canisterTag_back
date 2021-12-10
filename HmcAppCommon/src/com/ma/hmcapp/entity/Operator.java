package com.ma.hmcdb.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcdb.shared.synchronizer.CompanyField;
import com.ma.hmcdb.shared.synchronizer.ModifTime;
import com.ma.hmcdb.shared.synchronizer.Name;
import com.ma.hmcdb.shared.synchronizer.StoreTime;

import lombok.Data;

@Data
@Entity
public class Operator {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "", sortable = false, isImage = true, widthEM = 10)
	private String avatar;
	
	@Name
	@UILabel(label = "Имя", sortable = true)
	private String name;

	@UILabel(label = "Организация", sortable = true, nullable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@CompanyField
	private Company company;

	// @UILabel(label = "Removed")
	private Boolean removed;

	@ModifTime
	private Date modifTime;
	
	@StoreTime
	private Date storeTime;

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
