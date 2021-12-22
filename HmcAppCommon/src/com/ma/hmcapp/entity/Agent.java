package com.ma.hmcapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ma.commonui.shared.annotations.UILabel;

import lombok.Data;

@Data
@Entity
public class Agent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	@UILabel(label = "Название", sortable = true)
	private String name;
	
	@UILabel(label = "Действующее в-во")
	private String ingridientName;
	
	@UILabel(label = "Концентрация, %")
	private int concentration;

//	@UILabel(label = "Расход, мл/м3")
//	private int consumption_ml_m3;
//	@UILabel(label = "Расход профилакт., мл/м3")
//	private int consumption2_ml_m3;
//	@UILabel(label = "Время аэрации, мин.")
//	private int aeration_min;

	@UILabel(label = "Срок годности, мес.")
	private Integer shelfLife_months;
	

	
	public Agent() {
	}

	public Agent(String name, int consumption_ml_m3, int consumption2_ml_m3, int aeration_min,
			Integer shelfLife_months, String ingridientName, int concentration) {
		this.name = name;
//		this.consumption_ml_m3 = consumption_ml_m3;
//		this.consumption2_ml_m3 = consumption2_ml_m3;
//		this.aeration_min = aeration_min;
		this.shelfLife_months = shelfLife_months;
		this.ingridientName = ingridientName;
		this.concentration = concentration;
	}
	
}
