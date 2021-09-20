package com.ma.hmcdb.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ma.common.shared.cd.UILabel;

@Entity
public class Agent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column(unique = true)
	@UILabel(label = "Название", sortable = true)
	public String name;

	@UILabel(label = "Расход (мл/м3)")
	public int consumption_ml_m3;
	@UILabel(label = "Расход профилакт. (мл/м3)")
	public int consumption2_ml_m3;
	@UILabel(label = "Время аэрации (мин)")
	public int aeration_min;

	@UILabel(label = "Срок годности (месяцев)")
	public Integer shelfLife_months;
}
