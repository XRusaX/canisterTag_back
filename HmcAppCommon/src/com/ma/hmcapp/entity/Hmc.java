package com.ma.hmcdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ma.commonui.shared.annotations.UILabel;
import com.ma.commonui.shared.annotations.UINameField;
import com.ma.hmc.iface.shared.HmcType;
import com.ma.hmcdb.shared.HmcReportStatus;

import lombok.Data;


@Entity
@Data
@UINameField("serialNumber")
public class Hmc {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@UILabel(label = "Тип", sortable = true)
	@Enumerated(EnumType.STRING)
	private HmcType hmcType;

	@Column(unique = true)
	@UILabel(label = "Серийный номер", sortable = true)
	private String serialNumber;

	@UILabel(label = "Владелец", sortable = true, nullable = true)
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;

	@UILabel(label = "Статус", sortable = true, readOnly = true)
	public transient HmcReportStatus status;

	@UILabel(label = "Остаток", readOnly = true)
	public transient Integer remainML;

	@UILabel(label = "Объем канистры", readOnly = true)
	public transient Integer canisterVolumeML;

	public Hmc() {
	}

	public Hmc(HmcType hmcType, String serialNumber, Company company) {
		this.hmcType = hmcType;
		this.serialNumber = serialNumber;
		this.company = company;
	}
}
