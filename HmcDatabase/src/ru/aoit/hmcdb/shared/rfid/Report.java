package ru.aoit.hmcdb.shared.rfid;

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

import ru.aoit.hmc.rfid.shared.HmcReportStatus;
import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.Operator;
import ru.aoit.hmcdb.shared.Room;
import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Получено", sortable = true, readOnly = true)
	public Date time = new Date();

	@UILabel(label = "МГЦ", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Hmc hmc;

	@UILabel(label = "Статус", sortable = true)
	@Enumerated(EnumType.STRING)
	public HmcReportStatus status;

	@UILabel(label = "Метка", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RfidLabel rfidLabel;

	@UILabel(label = "Расход (ml)")
	public int consumtion_ml;

	@UILabel(label = "Начало работы", sortable = true)
	public Date startTime;

	@UILabel(label = "Продолж. работы (с)")
	public Integer durationS;

	@UILabel(label = "Организация", nullable = true, sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@UILabel(label = "Оператор", nullable = true, sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Operator operator;

	@UILabel(label = "Помещ./объект", nullable = true, sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Room room;

	public Report() {
	}

	public Report(Hmc hmc, Date startTime, Integer durationS, RfidLabel rfidLabel, int consumtion_ml, Company company,
			Operator operator, Room room, HmcReportStatus status) {
		this.hmc = hmc;
		this.startTime = startTime;
		this.durationS = durationS;
		this.rfidLabel = rfidLabel;
		this.consumtion_ml = consumtion_ml;
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
