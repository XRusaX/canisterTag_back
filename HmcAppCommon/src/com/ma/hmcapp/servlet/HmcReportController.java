package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;
import com.ma.hmc.iface.report.HmcReport;
import com.ma.hmcapp.datasource.HmcDataSource;
import com.ma.hmcapp.datasource.OperatorDataSource;
import com.ma.hmcapp.datasource.ReportDataSource;
import com.ma.hmcapp.datasource.RfidLabelDataSource;
import com.ma.hmcapp.datasource.RoomDataSource;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Hmc;
import com.ma.hmcdb.shared.HmcReportStatus;
import com.ma.hmcdb.shared.Operator;
import com.ma.hmcdb.shared.Room;
import com.ma.hmcdb.shared.rfid.Report;
import com.ma.hmcdb.shared.rfid.RfidLabel;

@RestController
public class HmcReportController {

	@Autowired
	private Database2 database;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@Autowired
	private HmcDataSource hmcDataSource;

	@Autowired
	private ReportDataSource reportDataSource;

	@Autowired
	private OperatorDataSource operatorDataSource;

	@Autowired
	private RoomDataSource roomDataSource;

	@Autowired
	private RfidLabelDataSource rfidLabelDataSource;

	@GetMapping("/lastreport")
	private long getLastReport(String serialNumber) throws IOException {
		if (serialNumber == null)
			return 0;

		long result = database.exec(em -> {
			Report report = reportDataSource.getLastReport(em, serialNumber);
			if (report == null)
				return 0l;
			return report.time.getTime();
		});

		return result;
	}

	@PostMapping(value = "/report")
	private synchronized String report(@RequestBody String strreport) throws IOException {

		HmcReport report = new Gson().fromJson(strreport, HmcReport.class);

		String result = database.exec(em -> {

			RfidLabel label = getLabel(em, report.canisterId);
			if (label == null) {
				msgLogger.add(null, Severity.ERROR, "Метка канистры не найдена " + report.canisterId);
				return "error";
			}

			Hmc hmc = hmcDataSource.getBySerNum(em, report.hmcSerialNumber);
			Company company = hmc.company;

			Operator operator = null;
			Room room = null;

			if (company != null) {
				operator = getOperator(em, report.operatorId, report.operatorName, company);
				room = getRoom(em, report.roomId, report.roomName, company);
			}

			Report report2 = new Report(hmc, new Date(report.time), report.durationS, label, report.cleaningId,
					report.consumptionML, report.remainML, company, operator, room,
					HmcReportStatus.valueOf(report.status));
			reportDataSource.store(em, report2);

			List<Report> reports = reportDataSource.loadByRfidLabel(em, label);

			int sum_consumption = reports.stream().mapToInt(r -> r.consumtion_ml).sum();
			if (sum_consumption > label.canisterVolume)
				msgLogger.add(null, Severity.ERROR, "Перерасход из канистры " + label.name);

			return "success";
		});

		return result;
	}

	private Operator getOperator(EM em, Long id, String name, Company company) {
		assert (company != null);

		if (id != null) {
			Operator operator = operatorDataSource.load(em, id);
			if (operator.company != company)
				msgLogger.add(null, Severity.WARNING, "Компания оператора и владелец МГЦ не совпадают");
			return operator;
		}

		Operator operator = operatorDataSource.loadByName(em, name, company);

		if (operator != null)
			return operator;

		operator = new Operator(name, company);
		operatorDataSource.store(em, operator);

		return operator;
	}

	private Room getRoom(EM em, Long id, String name, Company company) {
		assert (company != null);

		if (id != null) {
			Room room = roomDataSource.load(em, id);
			if (room.company != company)
				msgLogger.add(null, Severity.WARNING, "Владелец комнаты и владелец МГЦ не совпадают");
			return room;
		}

		Room room = roomDataSource.loadByName(em, name, company);
		if (room != null)
			return room;

		room = new Room(name, company);
		roomDataSource.store(em, room);

		return room;
	}

	private RfidLabel getLabel(EM em, Integer canisterId) {
		List<RfidLabel> labels = rfidLabelDataSource.loadByName(em, canisterId);
		if (labels.size() != 1) {
			msgLogger.add(null, Severity.ERROR, "меток " + canisterId + " найдено " + labels.size() + " штук");
			return null;
		}
		RfidLabel label = labels.get(0);
		return label;
	}

}
