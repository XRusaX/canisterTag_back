package ru.aoit.hmcapp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.SpringServlet;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.rfid.Report;
import ru.aoit.hmcdb.shared.rfid.RfidLabel;
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

		String result = database.exec(em -> {
			List<RfidLabel> labels = Database2.select(em, RfidLabel.class).whereEQ("name", report.canisterId)
					.getResultList();
			if (labels.size() != 1)
				msgLogger.add(null, Severity.ERROR,
						"меток " + report.canisterId + " найдено " + labels.size() + " штук");
			List<Hmc> hmcs = Database2.select(em, Hmc.class).whereEQ("serialNumber", report.hmcSerialNumber)
					.getResultList();
			if (hmcs.size() != 1)
				msgLogger.add(null, Severity.ERROR,
						"МГЦ " + report.hmcSerialNumber + " найдено " + hmcs.size() + " штук");

			if (labels.size() != 1 || hmcs.size() != 1)
				return "error";

			RfidLabel label = labels.get(0);
			Hmc hmc = hmcs.get(0);

			Report report2 = new Report(hmc, report.startTime, report.durationS, label, report.consumtionML,
					report.user, report.company, report.room, report.status);
			em.persist(report2);
			database.incrementTableVersion(Report.class);

			List<Report> reports = Database2.select(em, Report.class).whereEQ("rfidLabel", label).getResultList();
			int sum_consumption = reports.stream().mapToInt(r -> r.consumtion_ml).sum();
			if (sum_consumption > label.canisterVolume)
				msgLogger.add(null, Severity.ERROR, "Перерасход из канистры " + label.name);

			return "success";
		});

		resp.getWriter().write(result);

	}

}
