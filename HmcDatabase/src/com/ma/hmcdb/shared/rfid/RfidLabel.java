package com.ma.hmcdb.shared.rfid;

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
import com.ma.hmcdb.shared.Agent;
import com.ma.hmcdb.shared.Company;

@Entity
public class RfidLabel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Метка", sortable = true)
	public int name;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	public Date time;

	@UILabel(label = "Оператор", sortable = true, readOnly = true)
	public String userName;

	@UILabel(label = "Производство", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@UILabel(label = "Средство", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Agent agent;

	@UILabel(label = "Объем (ml)", sortable = true)
	public int canisterVolume;

	public RfidLabel() {
	}

	public RfidLabel(int name, Date time, String userName, Company company, Agent agent, int canisterVolume) {
		this.name = name;
		this.time = time;
		this.userName = userName;
		this.company = company;
		this.agent = agent;
		this.canisterVolume = canisterVolume;
	}
	
	
}
