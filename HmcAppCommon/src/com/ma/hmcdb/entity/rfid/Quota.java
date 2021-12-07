package com.ma.hmcdb.entity.rfid;

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
import com.ma.hmcdb.entity.Agent;
import com.ma.hmcdb.entity.Company;

import lombok.Data;

@Data
@Entity
public class Quota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	private Date time = new Date();

	@UILabel(label = "Пользователь", sortable = true, readOnly = true)
	private String userName;

	@UILabel(label = "Производство", sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;

	@UILabel(label = "Средство", sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Agent agent;

	@UILabel(label = "Объем (ml)")
	private int volume;

	@UILabel(label = "Остаток")
	private int remain;

	public Quota() {
	}
	
	public Quota(String userName, Company company, Agent agent, int volume, int remain) {
		this.userName = userName;
		this.company = company;
		this.agent = agent;
		this.volume = volume;
		this.remain = remain;
	}
}
