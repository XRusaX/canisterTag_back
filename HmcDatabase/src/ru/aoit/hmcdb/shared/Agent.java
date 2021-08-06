package ru.aoit.hmcdb.shared;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Agent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Название", sortable = true)
	public String name;

	@UILabel(label = "Расход (мл/м3)")
	public int consumption_ml_m3;
	@UILabel(label = "Расход профилакт. (мл/м3)")
	public int consumption2_ml_m3;
	@UILabel(label = "Время аэрации (мин)")
	public int aeration_min;

	@Override
	public String toString() {
		return name;
	}
}
