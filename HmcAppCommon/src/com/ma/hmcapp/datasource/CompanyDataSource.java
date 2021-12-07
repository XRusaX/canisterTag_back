package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Company;

@Component
public class CompanyDataSource extends DataSourceImpl<Company> {

	@Autowired
	private CommonData commonDataImpl;

	public CompanyDataSource() {
		super(Company.class);
	}
	
	@PostConstruct
	private void init() {
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
