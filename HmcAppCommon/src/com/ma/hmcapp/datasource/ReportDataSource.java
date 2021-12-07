package com.ma.hmcapp.datasource;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.db.Database2.DBSelect.Dir;
import com.ma.appcommon.db.TableVersion;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.Hmc;
import com.ma.hmcdb.entity.rfid.Report;
import com.ma.hmcdb.entity.rfid.RfidLabel;

@Component
public class ReportDataSource extends DataSourceImpl<Report> {

	@Autowired
	private CommonData commonDataImpl;

	@Autowired
	private HmcDataSource hmcDataSource;

	@Autowired
	private TableVersion tableVersion;

	public ReportDataSource() {
		super(Report.class);
	}

	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(Report.class, this);
	}

	@Override
	public long store(EM em, Report obj) {
		long id = super.store(em, obj);

		Company company = obj.getCompany();
		tableVersion._incrementTableVersion(em.em, Hmc.class, company == null ? null : company.getId());

		return id;
	}

	@Override
	public void delete(EM em, Report obj) {
		Company company = obj.getCompany();
		tableVersion._incrementTableVersion(em.em, Hmc.class, company == null ? null : company.getId());

		super.delete(em, obj);
	}

	public Report getLastReport(EM em, String serNum) {
		Hmc hmc = hmcDataSource.getBySerNum(em, serNum);
		if (hmc == null)
			return null;
		Report report = getLastReport(em, hmc);
		return report;
	}

	public Report getLastReport(EM em, Hmc hmc) {
		Report report = Database2.select(em.em, Report.class).whereEQ("hmc", hmc).orderBy("time", Dir.DESC)
				.getResultStream().findFirst().orElse(null);
		return report;
	}

	public List<Report> loadByRfidLabel(EM em, RfidLabel label) {
		return Database2.select(em.em, Report.class).whereEQ("rfidLabel", label).getResultList();
	}

}
