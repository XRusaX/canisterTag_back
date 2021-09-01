package ru.aoit.hmcdb.shared.test;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class TestReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	public Date time = new Date();

	public String serialNumber;

	public TestReport() {
	}

	public TestReport(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
