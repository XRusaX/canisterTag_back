package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.hmcdb.entity.test.TestReport;

@Component
public class TestReportDataSource extends DataSourceImpl<TestReport> {

	@Autowired
	private CommonData commonDataImpl;

	public TestReportDataSource() {
		super(TestReport.class);
	}

	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(TestReport.class, this);
	}

}
