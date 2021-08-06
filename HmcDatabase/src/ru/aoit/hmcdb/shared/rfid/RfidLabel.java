package ru.aoit.hmcdb.shared.rfid;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class RfidLabel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Метка")
	public int name;

	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RfidLabelGroup rfidLabelGroup;

}
