package com.ma.hmcapp.datasource;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;
import com.ma.hmc.iface.shared.HmcType;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.rfid.Report;
import com.ma.hmcdb.shared.rfid.RfidLabel;

@Component
public class HmcDataSource extends DataSourceImpl<Hmc> {

	@Autowired
	private Database2 database;

	@Autowired
	private ReportDataSource reportDataSource;

	@Autowired
	private CommonData commonDataImpl;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@PostConstruct
	private void init() {
		super.init(Hmc.class, database);
		commonDataImpl.addDataSource(Hmc.class, this);
	}

	@Override
	protected void onLoad(EM em, Hmc hmc) {
		Report report = reportDataSource.getLastReport(em, hmc);
		if (report != null) {
			hmc.status = report.status;
			hmc.remainML = report.remain_ml;

			RfidLabel rfidLabel = em.em.find(RfidLabel.class, report.rfidLabel.id);
			if (rfidLabel != null) {
				hmc.canisterVolumeML = rfidLabel.canisterVolume;
			}
		}
	}

	public Hmc getCreateHmc(EM em, HmcType hmcType, String hmcSerialNumber) {
		Hmc hmc = getBySerNum(em, hmcSerialNumber);
		if (hmc != null)
			return hmc;

		hmc = new Hmc(hmcType, hmcSerialNumber, null);
		em.em.persist(hmc);
		database.incrementTableVersion(em.em, hmc);

		msgLogger.add(null, Severity.INFO, "МГЦ " + hmcSerialNumber + " автоматически добавлен");

		return hmc;
	}

	public Hmc getBySerNum(EM em, String hmcSerialNumber) {
		Hmc hmc = Database2.select(em.em, Hmc.class).whereEQ("serialNumber", hmcSerialNumber).getResultStream().findFirst()
				.orElse(null);
		return hmc;
	}
}
