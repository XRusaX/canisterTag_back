package ru.aoit.hmcrfidwriter.lib.sim;

import java.util.Objects;

import javax.swing.JFrame;

import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmcrfidwriter.ClientSettings;
import ru.aoit.hmcrfidwriter.lib.RfidLib;
import ru.aoit.hmcrfidwriter.lib.RfidLibListener;

public class RfidLibSim implements RfidLib {
	private static final int TICK = 200;

	private boolean stopped;

	private final LabelPad labelPad;
	private final HmcSimulator hmcSim;

	private final RfidLibListener listener;

	private volatile int writeTimer;
	private volatile RfidData dataBeingWritten;

	private volatile int readTimer;

	public RfidLibSim(JFrame mainFrame, RfidLibListener listener, ClientSettings clientSettings) {
		labelPad = new LabelPad(mainFrame);

		this.listener = listener;
		hmcSim = new HmcSimulator(mainFrame, clientSettings);
	}

	@Override
	public void start() {
		labelPad.setVisible(true);
		labelPad.setBounds(600, 0, 100, 100);

		hmcSim.setVisible(true);
		hmcSim.setBounds(800, 0, 100, 100);
	}

	@Override
	public void stop() {
		stopped = true;
	}

	@Override
	public void readData(int id) {
		if (writeTimer != 0 || readTimer != 0) {
			listener.onError("Занято");
			return;
		}

		Integer touchedId = labelPad.touchedId;
		if (touchedId == null || id != touchedId) {
			listener.onError("Метку успели убрать");
			return;
		}

		readTimer = 2000 / TICK;
	}

	@Override
	public void writeData(int id, RfidData data) {
		if (writeTimer != 0 || readTimer != 0) {
			listener.onError("Занято");
			return;
		}

		Integer touchedId = labelPad.touchedId;
		if (touchedId == null || id != touchedId) {
			listener.onError("Метку успели убрать");
			return;
		}

		dataBeingWritten = data;
		writeTimer = 2000 / TICK;
	}

	@Override
	public void clearTag(int id) {
		listener.onDebugMessage("Метка " + id + " стирается");
		listener.onWriteOk(id);
	}

	@Override
	public void loop() {
		Integer lastId = null;
		for (;;) {
			Integer touchedId = labelPad.touchedId;

			if (stopped) {
				System.out.println("exit");
				return;
			}

			if (lastId != null && readTimer > 0 && --readTimer == 0) {
				RfidData data = new RfidData();
				data.UID = lastId;
				data.UNIQUE_ID = 10;
				data.CANISTER_NAME = "Cansiter Name";
				listener.onData(lastId, data);
			}
			if (lastId != null && writeTimer > 0 && --writeTimer == 0) {
				hmcSim.addCanister(dataBeingWritten);
				listener.onWriteOk(lastId);
			}

			if (!Objects.equals(lastId, touchedId)) {
				if (lastId != null) {
					listener.onDetouch(lastId);
					readTimer = writeTimer = 0;
				}

				if (touchedId != null)
					listener.onTouch(touchedId);
			}

			lastId = touchedId;

			try {
				Thread.sleep(TICK);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
