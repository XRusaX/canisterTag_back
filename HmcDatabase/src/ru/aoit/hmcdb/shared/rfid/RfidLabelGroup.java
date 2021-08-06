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

import ru.aoit.hmcdb.shared.Agent;
import ru.aoit.hmcdb.shared.Company;
import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class RfidLabelGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	public Date time;

	@UILabel(label = "Оператор", sortable = true, readOnly = true)
	public String userName;

	@UILabel(label = "Производство", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@UILabel(label = "Средство", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Agent agent;

	@UILabel(label = "Объем (ml)")
	public int canisterVolume;

}
