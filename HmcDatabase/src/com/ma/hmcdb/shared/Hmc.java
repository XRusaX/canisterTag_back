package com.ma.hmcdb.shared;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.common.shared.cd.UILabel;
import com.ma.hmc.rfid.shared.HmcReportStatus;

@Entity
public class Hmc {
	public enum HmcType {
		HMC_1, HMC_2, HMC_3, HMC_4,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Тип", sortable = true)
	@Enumerated(EnumType.STRING)
	public HmcType hmcType;

	@Column(unique = true)
	@UILabel(label = "Серийный номер", isName = true, sortable = true)
	public String serialNumber;

	@UILabel(label = "Владелец", sortable = true, nullable = true)
	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	@UILabel(label = "Статус", sortable = true, readOnly = true)
	public transient HmcReportStatus status;

	@UILabel(label = "Остаток")
	public transient Integer remainML;

	@UILabel(label = "Объем канистры")
	public transient Integer canisterVolumeML;

	public Hmc() {
	}

	public Hmc(String serialNumber, Company company) {
		this.serialNumber = serialNumber;
		this.company = company;
	}
}
