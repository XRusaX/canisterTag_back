package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.CommonDataImpl;
import com.ma.appcommon.DataSourceImpl;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.test.TestReport;

@Component
public class TestReportDataSource extends DataSourceImpl<TestReport> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonDataImpl commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(TestReport.class, database);
		commonDataImpl.addDataSource(TestReport.class, this);
	}

}
