package ru.aoit.hmcdb.shared.rfid;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Hmc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Серийный номер")
	public String serialNumber;
}
