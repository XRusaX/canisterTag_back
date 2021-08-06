package ru.aoit.hmcserver.shared;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ru.nppcrts.common.shared.cd.UILabel;

@Entity
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Name")
	public String name;

//	@OneToMany(cascade = CascadeType.ALL)
//	public List<User> users;
	
	public Company() {
	}

	public Company(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
