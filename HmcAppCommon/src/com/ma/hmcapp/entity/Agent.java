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

	@UILabel(label = "Концентрация, %", nullable = false)
	private double concentration;

	@UILabel(label = "Срок годности, мес.")
	private Integer shelfLife_months;

	public Agent() {
	}

	public Agent(String name, int consumption_ml_m3, int consumption2_ml_m3, int aeration_min, Integer shelfLife_months,
			String ingridientName, double concentration) {
		this.name = name;
		this.shelfLife_months = shelfLife_months;
		this.ingridientName = ingridientName;
		this.concentration = concentration;
	}

	/**
	 * Возвращает значение концентрации целым чилом в долях процента Например, для
	 * концентрации 0.12% вернется значение 12
	 * 
	 * @return
	 */
	public int getConcentrationForRfid() {
		return (int) (concentration * 100);
	}

}
