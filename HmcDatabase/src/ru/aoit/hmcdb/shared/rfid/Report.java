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

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Дата/время", sortable = true, readOnly = true)
	public Date time = new Date();

	@UILabel(label = "МГЦ", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Hmc hmc;

	@UILabel(label = "Метка", sortable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public RfidLabel rfidLabel;

	
	
	
	
	
	@UILabel(label = "Расход")
	public int consumtion_ml;

	public Report() {
	}

	public Report(Hmc hmc, RfidLabel rfidLabel, int consumtion_ml) {
		this.hmc = hmc;
		this.rfidLabel = rfidLabel;
		this.consumtion_ml = consumtion_ml;
	}

}
