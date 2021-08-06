package ru.aoit.hmcdb.shared;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Название", sortable = true)
	public String name;

	public enum CompanyType {
		CANISTER, TEST,
	}

	@UILabel(label = "Тип", sortable = true)
	@Enumerated(EnumType.STRING)
	public CompanyType companyType;

	@UILabel(label = "Адрес", sortable = true)
	public String addr;

	@UILabel(label = "Контакты", sortable = true)
	public String contacts;

	@UILabel(label = "Размер блока меток", sortable = true)
	public int rfidBlockSize = 10;

	@Override
	public String toString() {
		return name;
	}
}
