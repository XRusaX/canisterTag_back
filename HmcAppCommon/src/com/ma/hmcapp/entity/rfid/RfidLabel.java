package com.ma.hmcapp.entity.rfid;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcapp.entity.Agent;
import com.ma.hmcapp.entity.Company;

import lombok.Data;

@Data
@Entity
public class RfidLabel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Метка", sortable = true)
	private int name;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	private Date time;

	@UILabel(label = "Оператор", sortable = true, readOnly = true)
	private String userName;

	@UILabel(label = "Производство", sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;

	@UILabel(label = "Средство", sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Agent agent;

	@UILabel(label = "Объем (ml)", sortable = true)
	private int canisterVolume;

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
