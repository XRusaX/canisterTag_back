package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.shared.test.TestReport;

@Component
public class TestReportDataSource extends DataSourceImpl<TestReport> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonData commonDataImpl;

	@PostConstruct
	private void init() {
		super.init(TestReport.class, database);
		commonDataImpl.addDataSource(TestReport.class, this);
	}

}
