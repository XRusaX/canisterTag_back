package ru.aoit.hmcrfidwriter.lib.jni;

import ru.aoit.hmc.rfid.ruslandata.Constants;
import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmc.rfid.ruslandata.RuslanStructIO;
import ru.aoit.hmcrfidwriter.lib.RfidLib;
import ru.aoit.hmcrfidwriter.lib.RfidLibListener;

public class RfidLibJni implements RfidLib {

	static {
		System.loadLibrary("rfid_component");
	}

	private volatile boolean stopped;
	private RfidLibListener listener;

	public RfidLibJni(RfidLibListener listener) {
		this.listener = listener;
	}

	@Override
	public void start() {
		listener.onDebudMessage("RFIDComponent.start()");
		RFIDComponent.start();
	}

	@Override
	public void stop() {
		stopped = true;
		RfidData data = new RfidData();
		data.REQ = Constants.STOP_REQ;
		listener.onDebudMessage("stop()");
		sendMessage(data);
	}

	@Override
	public void writeData(int id, RfidData data) {
		listener.onDebudMessage("writeData(int id, RfidData data)");
		data.REQ = Constants.WRITE_DATA_REQ;
		sendMessage(data);
	}
	
	@Override
	public void clearTag(int id) {
		listener.onDebudMessage("clearTag(int id)");
		RfidData data = new RfidData();
		data.REQ = Constants.CLEAN_REQ;
		sendMessage(data);
	}

	@Override
	public void readData(int id) {
		listener.onDebudMessage("readData(int id)");
		RfidData data = new RfidData();
		data.REQ = Constants.READ_DATA_REQ;
		sendMessage(data);
	}

	private void sendMessage(Object obj) {
		byte[] bytes = RuslanStructIO.write(obj);
		String s = new String();
		for (byte b : bytes)
			s += String.format("%02X ", b);
		listener.onDebudMessage("-> " + s);
		RFIDComponent.sendMessage(bytes);
	}

	@Override
	public void loop() {
		while (!stopped) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}

			byte[] messageBytes = RFIDComponent.receiveMessage();
			if (messageBytes == null || messageBytes.length == 0)
				continue;

			String s = new String();
			for (byte b : messageBytes)
				s += String.format("%02X ", b);
			listener.onDebudMessage("<- " + s);

			RfidData data = RuslanStructIO.read(messageBytes, RfidData.class);
			processMessage(data);
		}

	}

	private void processMessage(RfidData data) {
		if (data.RSP != null) {
			switch (data.RSP) {
			case Constants.READ_DATA_REQ:
				listener.onDebudMessage("RSP + READ_DATA_REQ");
				listener.onData(data.UID, data);
				break;
			}
		}

		if (data.MSG != null) {
			switch (data.MSG) {
			case Constants.RFID_DETECTED_MSG:
				listener.onDebudMessage("RFID_DETECTED_MSG");
				listener.onTouch(data.UID);
				break;

			case Constants.RFID_REMOVED_MSG:
				listener.onDebudMessage("RFID_REMOVED_MSG");
				listener.onDetouch(data.UID);
				break;

			case Constants.DATA_MSG:
				listener.onDebudMessage("DATA_MSG");
				listener.onData(data.UID, data);
				break;

			case Constants.READ_ERROR_MSG:
				listener.onDebudMessage("READ_ERROR_MSG");
				listener.onError("READ_ERROR_MSG");
				break;

			case Constants.WRITE_ERROR_MSG:
				listener.onDebudMessage("WRITE_ERROR_MSG");
				listener.onError("WRITE_ERROR_MSG");
				break;

			case Constants.AUTH_ERROR_MSG:
				listener.onDebudMessage("AUTH_ERROR_MSG");
				listener.onError("AUTH_ERROR_MSG");
				break;

			case Constants.TEXT_MSG:
				listener.onDebudMessage("TEXT_MSG");
				listener.onError(data.TEXT);
				break;
				
			case Constants.UART_FAILED_MSG:
				listener.onDebudMessage("UART_FAILED_MSG");
				listener.onError("UART_FAILED_MSG");
				break;

			default:
				break;
			}
		}

		if (data.NAK != null) {
			listener.onDebudMessage("NAK");
			listener.onError("NAK");
		}
	}

}
