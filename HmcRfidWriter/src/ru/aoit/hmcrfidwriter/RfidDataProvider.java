package ru.aoit.hmcrfidwriter;

import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;

import ru.aoit.hmc.rfid.ruslandata.RfidData;

public class RfidDataProvider {
	private LinkedList<RfidData> records = new LinkedList<>();

	private CanisterSettings canisterSettings;

	private ConnectionToServer connectionToServer;

	public RfidDataProvider(ConnectionToServer connectionToServer) throws GeneralSecurityException {
		this.connectionToServer = connectionToServer;
	}

	// public RfidData getNextRecord() throws Exception {
	// if (records.isEmpty()) {
	// List<RfidData> sigs =
	// connectionToServer.proxy.getSigs(canisterSettings.CANISTER_NAME,
	// canisterSettings.CANISTER_VOLUME_ML);
	// records.addAll(sigs);
	// }
	//
	// RfidData record = records.removeFirst();
	// return record;
	// }

	public RfidData peekRecord() throws Exception {
		
		if (records.size() < 5) {
			// if (records.isEmpty()) {
			List<RfidData> sigs = connectionToServer.proxy.getSigs(canisterSettings.CANISTER_NAME,
					canisterSettings.CANISTER_VOLUME_ML);
			records.addAll(sigs);
		}
		return records.peekFirst();
		// RfidData record = records.removeFirst();
		// return record;
	}

	public int getRecordsQnt() {
		return records.size();
	}

	public void consumeRecord() {
		records.removeFirst();
	}

	// http://10.6.150.9:8899
	// http://pcserver425.npp-crts.ru:8080/desinfection
	public void setSettings(CanisterSettings canisterSettings) {
		this.canisterSettings = canisterSettings;
		records.clear();
	}

}
