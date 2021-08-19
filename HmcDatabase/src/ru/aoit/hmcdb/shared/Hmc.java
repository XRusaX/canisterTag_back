package ru.aoit.hmcdb.shared;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import ru.aoit.hmc.rfid.shared.HmcReportStatus;
import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Hmc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Серийный номер", isName = true, sortable = true)
	public String serialNumber;

	@UILabel(label = "Владелец", sortable = true, nullable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@UILabel(label = "Статус", sortable = true)
	public transient HmcReportStatus status;

}
