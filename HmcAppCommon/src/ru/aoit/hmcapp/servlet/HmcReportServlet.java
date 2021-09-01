package ru.aoit.hmcapp.servlet;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import ru.aoit.appcommon.Database2;
import ru.aoit.appcommon.SpringServlet;
import ru.aoit.appcommon.logger.MsgLoggerImpl;
import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Hmc;
import ru.aoit.hmcdb.shared.Operator;
import ru.aoit.hmcdb.shared.Room;
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
	protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HmcReport report = new Gson().fromJson(req.getReader(), HmcReport.class);

		String result = database.exec(em -> {

			RfidLabel label = getLabel(em, report.canisterId);
			if (label == null) {
				msgLogger.add(null, Severity.ERROR, "Метка канистры не найдена " + report.canisterId);
				return "error";
			}

			Hmc hmc = getHmc(em, report.hmcSerialNumber);

			Operator operator = getOperator(em, report.userId, report.userName, hmc.company);
			Room room = getRoom(em, report.roomId, report.roomName, hmc.company);

			Report report2 = new Report(hmc, report.startTime, report.durationS, label, report.consumtionML,
					hmc.company, operator, room, report.status);
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

	private Operator getOperator(EntityManager em, Long id, String name, Company company) {
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

	private Hmc getHmc(EntityManager em, String hmcSerialNumber) {
		Hmc hmc = Database2.select(em, Hmc.class).whereEQ("serialNumber", hmcSerialNumber).getResultStream().findFirst()
				.orElse(null);
		if (hmc != null)
			return hmc;

		hmc = new Hmc(hmcSerialNumber, null);
		em.persist(hmc);

		msgLogger.add(null, Severity.INFO, "МГЦ " + hmcSerialNumber + " автоматически добавлен");
		database.incrementTableVersion(Hmc.class);

		return hmc;
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
