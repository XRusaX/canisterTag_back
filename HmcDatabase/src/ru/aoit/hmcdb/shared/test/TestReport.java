package ru.aoit.hmcdb.shared.test;

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

import ru.aoit.hmc.test.shared.HmcTestStatus;
import ru.aoit.hmc.test.shared.HmcTestType;
import ru.aoit.hmcdb.shared.Hmc;
import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class TestReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	public Date time = new Date();

	@UILabel(label = "МГЦ", sortable = true, readOnly = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Hmc hmc;

	@Enumerated(EnumType.STRING)
	@UILabel(label = "Тип", sortable = true, readOnly = true)
	public HmcTestType testType;

	@Enumerated(EnumType.STRING)
	@UILabel(label = "Статус", sortable = true, readOnly = true)
	public HmcTestStatus testStatus;

	@UILabel(label = "Подробности", sortable = true, readOnly = true)
	public String details;

	public TestReport() {
	}

	public TestReport(Hmc hmc, HmcTestType testType, HmcTestStatus testStatus, String details) {
		this.hmc = hmc;
		this.testType = testType;
		this.testStatus = testStatus;
		this.details = details;
	}

}
