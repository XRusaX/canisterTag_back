package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ma.appcommon.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.common.shared.Severity;
import com.ma.hmc.iface.report.HmcReport;
import com.ma.hmcapp.HmcAppHelper;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Hmc;
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
	private HmcAppHelper hmcAppHelper;

	@GetMapping("/lastreport")
	private long getLastReport(String serialNumber) throws IOException {
		if (serialNumber == null)
			return 0;

		long result = database.exec(em -> {
			Report report = hmcAppHelper.getLastReport(em, serialNumber);
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
			// http://127.0.0.1:8888
			Hmc hmc = hmcAppHelper.getCreateHmc(em, report.hmcType, report.hmcSerialNumber);

			Operator operator = null;
			Room room = null;

			if (hmc.company != null) {
				operator = getOperator(em, report.operatorId, report.operatorName, hmc.company);
				room = getRoom(em, report.roomId, report.roomName, hmc.company);
			}

			Report report2 = new Report(hmc, new Date(report.startTime), report.durationS, label, report.cleaningId,
					report.consumptionML, report.remainML, hmc.company, operator, room, report.status);
			em.persist(report2);
			database.incrementTableVersion(Report.class);
			database.incrementTableVersion(Hmc.class);
			database.incrementTableVersion(Room.class);

			List<Report> reports = Database2.select(em, Report.class).whereEQ("rfidLabel", label).getResultList();
			int sum_consumption = reports.stream().mapToInt(r -> r.consumtion_ml).sum();
			if (sum_consumption > label.canisterVolume)
				msgLogger.add(null, Severity.ERROR, "Перерасход из канистры " + label.name);

			return "success";
		});

		return result;
	}

	private Operator getOperator(EntityManager em, Long id, String name, Company company) {
		assert (company != null);

		if (id != null) {
			Operator operator = em.find(Operator.class, id);
			if (operator.company != company)
				msgLogger.add(null, Severity.WARNING, "Компания оператора и владелец МГЦ не совпадают");
			return operator;
		}

		Operator operator = Database2.select(em, Operator.class).whereEQ("name", name).whereEQ("company", company)
				.getResultStream().findFirst().orElse(null);
		if (operator != null)
			return operator;

		operator = new Operator(name, company);
		em.persist(operator);

		database.incrementTableVersion(Operator.class);

		return operator;
	}

	private Room getRoom(EntityManager em, Long id, String name, Company company) {
		if (id != null) {
			Room room = em.find(Room.class, id);
			if (room.company != company)
				msgLogger.add(null, Severity.WARNING, "Владелец комнаты и владелец МГЦ не совпадают");
			return room;
		}

		Room room = Database2.select(em, Room.class).whereEQ("name", name).whereEQ("company", company).getResultStream()
				.findFirst().orElse(null);
		if (room != null)
			return room;

		room = new Room(name, null, company);
		em.persist(room);

		database.incrementTableVersion(Room.class);

		return room;
	}

	private RfidLabel getLabel(EntityManager em, Integer canisterId) {
		List<RfidLabel> labels = Database2.select(em, RfidLabel.class).whereEQ("name", canisterId).getResultList();
		if (labels.size() != 1) {
			msgLogger.add(null, Severity.ERROR, "меток " + canisterId + " найдено " + labels.size() + " штук");
			return null;
		}
		RfidLabel label = labels.get(0);
		return label;
	}

}
