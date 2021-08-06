package ru.aoit.hmcapp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.Database2.DBSelect;
import ru.aoit.appcommon.SpringServlet;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.aoit.hmcdb.shared.rfid.Hmc;
import ru.aoit.hmcdb.shared.rfid.Report;
import ru.aoit.hmcdb.shared.rfid.RfidLabel;
import ru.aoit.hmcdb.shared.rfid.RfidLabelGroup;
import ru.nppcrts.common.shared.Severity;

//@WebServlet("/report")
@SuppressWarnings("serial")
public class HmcReportServlet extends SpringServlet {

	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HmcReport report = new Gson().fromJson(req.getReader(), HmcReport.class);

		database.execVoid(em -> {
			List<RfidLabel> labels = new DBSelect<>(em, RfidLabel.class).whereEQ("label", report.canisterId).getResultList();
			if (labels.size() != 1)
				msgLogger.add(null, Severity.ERROR, "меток " + report.canisterId + " найдено " + labels.size() + " штук");
			List<Hmc> hmcs = new DBSelect<>(em, Hmc.class).whereEQ("serialNumber", report.hmcSerialNumber)
					.getResultList();
			if (hmcs.size() != 1)
				msgLogger.add(null, Severity.ERROR,
						"МГЦ " + report.hmcSerialNumber + " найдено " + hmcs.size() + " штук");

			if (labels.size() != 1 || hmcs.size() != 1)
				return;

			RfidLabel label = labels.get(0);
			Hmc hmc = hmcs.get(0);

			Report report2 = new Report(hmc, label, report.consumtionML);
			em.persist(report2);
			database.incrementTableVersion(Report.class);

			RfidLabelGroup group = label.rfidLabelGroup;

			List<Report> reports = new DBSelect<>(em, Report.class).whereEQ("rfidLabel", label).getResultList();
			int sum_consumption = reports.stream().mapToInt(r -> r.consumtion_ml).sum();
			if (sum_consumption > group.canisterVolume)
				msgLogger.add(null, Severity.ERROR, "Перерасход из канистры " + label.name);
		});
	}

}
