package ru.aoit.hmcdb.shared.rfid;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import ru.aoit.hmc.rfid.shared.HmcReportStatus;
import ru.aoit.hmcdb.shared.Hmc;
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

	@UILabel(label = "Пользователь", nullable = true, sortable = true)
	public String user;

	@UILabel(label = "Компания", nullable = true, sortable = true)
	public String company;

	@UILabel(label = "Помещ./объект", nullable = true, sortable = true)
	public String room;

	public Report() {
	}

	public Report(Hmc hmc, Date startTime, Integer durationS, RfidLabel rfidLabel, int consumtion_ml, String user,
			String company, String room, HmcReportStatus status) {
		this.hmc = hmc;
		this.startTime = startTime;
		this.durationS = durationS;
		this.rfidLabel = rfidLabel;
		this.consumtion_ml = consumtion_ml;
		this.user = user;
		this.company = company;
		this.room = room;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Report " + id + " hmc:" + hmc.id + " " + time;
	}
}
