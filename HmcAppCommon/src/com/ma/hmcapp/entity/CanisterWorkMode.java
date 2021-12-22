package com.ma.hmcapp.entity;

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
import com.ma.hmcapp.shared.synchronizer.ModifTime;
import com.ma.hmcapp.shared.synchronizer.Name;
import com.ma.hmcapp.shared.synchronizer.StoreTime;

import lombok.Data;

@Data
@Entity
public class CanisterWorkMode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Уникальный номер", sortable = true)
	private int uid;

	@Name
	@UILabel(label = "Название", sortable = true)
	private String name;

	@UILabel(label = "Расход, мл/м3", sortable = true)
	private int can_consumption_ml_m3;

	@UILabel(label = "Экспозиция, сек", sortable = true)
	private int can_exposure_sec;
	@UILabel(label = "Проветривание, сек", sortable = true)
	private int can_airing_sec;
	@UILabel(label = "Период импульса, сек", sortable = true)
	private int can_impulse_period_sec;
	@UILabel(label = "Ширина импульса, сек", sortable = true)
	private int can_inpulse_width_sec;

//	@UILabel(label = "Средство", sortable = true, nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Agent agent;

	@ModifTime
	private Date modifTime;

	@StoreTime
	private Date storeTime;

	public CanisterWorkMode() {
	}

	public CanisterWorkMode(String name, Agent agent) {
		this.name = name;
		this.agent = agent;
	}

	public CanisterWorkMode(long id, String name, Agent agent, Date modifTime, Date storeTime) {
		this.id = id;
		this.name = name;
		this.agent = agent;
		this.modifTime = modifTime;
		this.storeTime = storeTime;
	}
}