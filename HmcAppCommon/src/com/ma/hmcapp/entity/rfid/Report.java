package com.ma.hmcapp.entity.rfid;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.hmcapp.entity.Company;
import com.ma.hmcapp.entity.Hmc;
import com.ma.hmcapp.entity.Operator;
import com.ma.hmcapp.entity.Room;
import com.ma.hmcapp.shared.HmcReportStatus;

import lombok.Data;

@Data
@Entity
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Организация", nullable = true, sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;

	@UILabel(label = "Начало работы", sortable = true)
	private Date startTime;

	@UILabel(label = "Продолж. работы (с)")
	private Integer durationS;

	@UILabel(label = "Статус", sortable = true)
	@Enumerated(EnumType.STRING)
	private HmcReportStatus status;

	@UILabel(label = "МГЦ", sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Hmc hmc;

	@UILabel(label = "Оператор", nullable = true, sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Operator operator;

	@UILabel(label = "Помещ./объект", nullable = true, sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Room room;

	@UILabel(label = "Расход (ml)")
	private Integer consumtion_ml;

	@UILabel(label = "Остаток")
	private Integer remain_ml;

	@UILabel(label = "cleaningId")
	private Integer cleaningId;

	@UILabel(label = "Метка", sortable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RfidLabel rfidLabel;

	// @UILabel(label = "Получено", sortable = true, readOnly = true)
	private Date time = new Date();

	public Report() {
	}

	public Report(Hmc hmc, Date startTime, Integer durationS, RfidLabel rfidLabel, Integer cleaningId, Integer consumtion_ml,
			Integer remain_ml, Company company, Operator operator, Room room, HmcReportStatus status) {
		this.hmc = hmc;
		this.startTime = startTime;
		this.durationS = durationS;
		this.rfidLabel = rfidLabel;
		this.cleaningId = cleaningId;
		this.consumtion_ml = consumtion_ml;
		this.remain_ml = remain_ml;
		this.company = company;
		this.operator = operator;
		this.room = room;
		this.status = status;
	}

//	@Override
//	public String toString() {
//		//return "Report " + id + " hmc:" + hmc.getId() + " " + time;
//	}
}
