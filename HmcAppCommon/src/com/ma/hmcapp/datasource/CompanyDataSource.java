package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.Company;

@Component
public class CompanyDataSource extends DataSourceImpl<Company> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(Company.class, database);
		commonDataImpl.addDataSource(Company.class, this);
	}

	public void clear(EM em) {
		em.em.createQuery("delete from Company").executeUpdate();
	}

	public Company getByName(EM em, String companyName) {
		return Database2.select(em.em, Company.class).whereEQ("name", companyName).getResultStream().findFirst()
				.orElse(null);
	}

}
