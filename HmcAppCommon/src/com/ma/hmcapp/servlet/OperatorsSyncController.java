package com.ma.hmcapp.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ma.appcommon.db.Database2;
import com.ma.hmc.iface.opsync.OpSync;
import com.ma.hmcapp.datasource.HmcDataSource;
import com.ma.hmcapp.datasource.OperatorDataSource;
import com.ma.hmcapp.synchronizer.DatasourceSynchronizer;
import com.ma.hmcdb.entity.Hmc;
import com.ma.hmcdb.entity.Operator;

@RestController
public class OperatorsSyncController {

	@Autowired
	private Database2 database;

	@Autowired
	private HmcDataSource hmcDataSource;

	@Autowired
	private OperatorDataSource operatorDataSource;

	@PostMapping(value = "/api/operatorsync")
	private synchronized String sync(@RequestBody String opsync, @RequestParam("serNum") String hmcSerialNumber)
			throws IOException {

		OpSync opSync = new Gson().fromJson(opsync, OpSync.class);

		synchronized (operatorDataSource) {
			OpSync response = new OpSync();
			database.execVoid(em -> {
				Hmc hmc = hmcDataSource.getBySerNum(em, hmcSerialNumber);

				if (hmc.company == null)
					return;

				if (opSync.operators != null) {
					DatasourceSynchronizer<Operator> operatorsSynchronizer = new DatasourceSynchronizer<>(
							operatorDataSource, hmc.company);
					List<Operator> toClient = operatorsSynchronizer.sync(em, opSync.operators.stream()
							.map(o -> new Operator(o.id, o.name, hmc.company, o.removed, new Date(o.modifTime), null))
							.collect(Collectors.toList()), new Date(opSync.lastSync));

					response.operators = toClient.stream()
							.map(o -> new OpSync.Operator(o.id, o.name, o.removed, o.modifTime.getTime()))
							.collect(Collectors.toList());
					response.lastSync = System.currentTimeMillis();
				}
			});
			return new Gson().toJson(response);
		}
	}
}
