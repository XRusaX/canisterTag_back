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
		listener.onDebugMessage("RFIDComponent.start()");
		RFIDComponent.start();
	}

	@Override
	public void stop() {
		stopped = true;
		RfidData data = new RfidData();
		data.REQ = Constants.STOP_REQ.value;
		listener.onDebugMessage("stop()");
		sendMessage(data);
	}

	@Override
	public void writeData(int id, RfidData data) {
		listener.onDebugMessage("writeData(int id, RfidData data)");
		data.REQ = Constants.WRITE_DATA_REQ.value;
		sendMessage(data);
	}

	@Override
	public void clearTag(int id) {
		listener.onDebugMessage("clearTag(int id)");
		RfidData data = new RfidData();
		data.REQ = Constants.CLEAN_REQ.value;
		sendMessage(data);
	}

	@Override
	public void readData(int id) {
		listener.onDebugMessage("readData(int id)");
		RfidData data = new RfidData();
		data.REQ = Constants.READ_DATA_REQ.value;
		sendMessage(data);
	}

	private void sendMessage(Object obj) {
		byte[] bytes = RuslanStructIO.write(obj);
		String s = new String();
		for (byte b : bytes)
			s += String.format("%02X ", b);
		listener.onDebugMessage("-> " + s);
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
			listener.onDebugMessage("<- " + s);

			RfidData data = RuslanStructIO.read(messageBytes, RfidData.class);
			processMessage(data);
		}

	}

	private void processMessage(RfidData data) {
		if (data.RSP != null) {
			Constants rsp = Constants.byValue(data.RSP);
			listener.onDebugMessage("RSP " + rsp.name());

			switch (rsp) {
			case READ_DATA_REQ:
				listener.onData(data.UID, data);
				break;
			case WRITE_DATA_REQ:
				listener.onWriteOk(data.ID);
				break;
			default:
				listener.onError("RSP " + rsp.name());
				break;
			}
		}

		if (data.MSG != null) {
			Constants msg = Constants.byValue(data.MSG);
			listener.onDebugMessage("MSG " + msg.name());
			switch (msg) {

			case RFID_DETECTED_MSG:
				listener.onTouch(data.UID);
				break;

			case RFID_REMOVED_MSG:
				listener.onDetouch(data.UID);
				break;

			case DATA_MSG:
				listener.onData(data.UID, data);
				break;

			default:
				listener.onError("MSG " + msg.name());
				break;
			}
		}

		if (data.NAK != null) {
			listener.onDebugMessage("NAK");
			listener.onError("NAK");
		}
	}

}
