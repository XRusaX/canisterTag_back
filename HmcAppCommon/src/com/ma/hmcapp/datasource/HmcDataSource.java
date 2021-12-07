package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.hmcdb.entity.Hmc;
import com.ma.hmcdb.entity.rfid.Report;
import com.ma.hmcdb.entity.rfid.RfidLabel;

@Component
public class HmcDataSource extends DataSourceImpl<Hmc> {

	@Autowired
	private ReportDataSource reportDataSource;

	@Autowired
	private CommonData commonDataImpl;

	public HmcDataSource() {
		super(Hmc.class);
	}
	
	@PostConstruct
	private void init() {
		commonDataImpl.addDataSource(Hmc.class, this);
	}

	@Override
	protected void onLoad(EM em, Hmc hmc) {
		Report report = reportDataSource.getLastReport(em, hmc);
		if (report != null) {
			hmc.status = report.getStatus();
			hmc.remainML = report.getRemain_ml();

			RfidLabel rfidLabel = em.em.find(RfidLabel.class, report.getRfidLabel().getId());
			if (rfidLabel != null) {
				hmc.canisterVolumeML = rfidLabel.getCanisterVolume();
			}
		}
	}

	public Hmc getBySerNum(EM em, String hmcSerialNumber) {
		Hmc hmc = Database2.select(em.em, Hmc.class).whereEQ("serialNumber", hmcSerialNumber).getResultStream().findFirst()
				.orElse(null);
		return hmc;
	}
}
