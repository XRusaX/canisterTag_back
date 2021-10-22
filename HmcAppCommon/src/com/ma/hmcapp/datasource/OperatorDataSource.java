package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Operator;

@Component
public class OperatorDataSource extends DataSourceImpl<Operator> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(Operator.class, database);
		commonDataImpl.addDataSource(Operator.class, this);
	}

	public Operator loadByName(EM em, String name, Company company) {
		return Database2.select(em.em, Operator.class).whereEQ("name", name).whereEQ("company", company)
				.getResultStream().findFirst().orElse(null);
	}

}
