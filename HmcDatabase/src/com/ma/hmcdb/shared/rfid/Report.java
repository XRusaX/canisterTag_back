package com.ma.hmcdb.shared.rfid;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.common.shared.cd.UILabel;
import com.ma.hmc.rfid.shared.HmcReportStatus;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.Operator;
import com.ma.hmcdb.shared.Room;

@Entity
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Организация", nullable = true, sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@UILabel(label = "Начало работы", sortable = true)
	public Date startTime;

	@UILabel(label = "Продолж. работы (с)")
	public Integer durationS;

	@UILabel(label = "Статус", sortable = true)
	@Enumerated(EnumType.STRING)
	public HmcReportStatus status;

	@UILabel(label = "МГЦ", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Hmc hmc;

	@UILabel(label = "Оператор", nullable = true, sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Operator operator;

	@UILabel(label = "Помещ./объект", nullable = true, sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Room room;

	@UILabel(label = "Расход (ml)")
	public Integer consumtion_ml;

	@UILabel(label = "Остаток")
	public Integer remain_ml;

	@UILabel(label = "cleaningId")
	public Integer cleaningId;

	@UILabel(label = "Метка", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RfidLabel rfidLabel;

	// @UILabel(label = "Получено", sortable = true, readOnly = true)
	public Date time = new Date();

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

	@Override
	public String toString() {
		return "Report " + id + " hmc:" + hmc.id + " " + time;
	}
}
