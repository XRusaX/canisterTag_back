package ru.aoit.hmcserver.shared;

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
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@UILabel(label = "Name", sortable = true)
	public String name;

	@UILabel(label = "Company", nullable = true, sortable = true)
	@ManyToOne(cascade=CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Company company;

	public User() {
	}

	public User(String name) {
		this.name = name;
	}
}
