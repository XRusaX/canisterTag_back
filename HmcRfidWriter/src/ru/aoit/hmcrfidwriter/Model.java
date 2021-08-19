package ru.aoit.hmcrfidwriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ru.aoit.hmc.rfid.rpcinterface.HmcRfidRpcInterface;
import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmc.rfid.ruslandata.RfidDataReflection;
import ru.aoit.hmc.rfid.ruslandata.RfidDataUtils;
import ru.aoit.hmcrfidwriter.lib.RfidLib;
import ru.aoit.hmcrfidwriter.lib.RfidLibListener;
import ru.aoit.hmcrfidwriter.lib.jni.RfidLibJni;
import ru.aoit.hmcrfidwriter.lib.sim.RfidLibSim;
import ru.nppcrts.common.cd.CDUtils;
import ru.nppcrts.common.gson.GsonUtils;
import ru.nppcrts.common.rsa.SigSHA256WithRSA;

public class Model {
	private static String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJTCwq+GoGILQIq2IDw0QTtmFMoPfnG5dOmDBDk5uKPKkJB/5vW42SAa+QCp0Lvm/uWT49vBSlihcIPnYQDXLWkCAwEAAQ==";

	private SigSHA256WithRSA sig = new SigSHA256WithRSA(null, publicKeyStr);

	private File canisterSettingsFile = new File("canistersettings.json");
	private File clientSettingsFile = new File("clientsettings.json");

	private CanisterSettings canisterSettings = GsonUtils.read(canisterSettingsFile, CanisterSettings.class, true);
	private ClientSettings clientSettings = GsonUtils.read(clientSettingsFile, ClientSettings.class, true);
	private ModelListener modelListener;
	public final ConnectionToServer connectionToServer = new ConnectionToServer(
			() -> modelListener.onConnectionStatusUpdated());
	private final RfidDataProvider rfidDataProvider = new RfidDataProvider(connectionToServer);

	private boolean writeMode;
	private boolean processing;
	private Integer touched;
	// "https://localhost:8443/desinfection/hmcrfidserver/rpcserver"

	private CDUtils cdUtils = new CDUtils() {
		@Override
		protected <T> T loadObject(Class<T> type, long id) {
			throw new UnsupportedOperationException();
		}
	};
	
	private RfidLib lib;

	private RfidLibListener libListener = new RfidLibListener() {

		@Override
		public void onTouch(int id) {
			SwingUtilities.invokeLater(() -> {
				setTouched(id);
				try {
					if (writeMode) {
						RfidData dataBuffer = rfidDataProvider.peekRecord();
						checkRecord(dataBuffer);
						lib.writeData(id, dataBuffer);
						setProcessingState(true);
					}
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					modelListener.onError(e.getMessage(), errors.toString());
					System.out.println(errors);
				}
			});
		}

		@Override
		public void onDetouch(int id) {
			SwingUtilities.invokeLater(() -> {
				modelListener.onDetouch();
				setTouched(null);
				setProcessingState(false);
			});
		}

		@Override
		public void onWriteOk(Integer id) {
			SwingUtilities.invokeLater(() -> {
				setProcessingState(false);
				try {
					RfidDataReflection rfidData = new RfidDataReflection(rfidDataProvider.peekRecord());
					String htmlTable = rfidData.getHTMLTable("#3D9970");
					rfidDataProvider.consumeRecord();
					modelListener.onWriteDoneOk(htmlTable);
					connectionToServer.proxy.tagWriteDone(rfidData.getTagID());
					System.out.println("write ok " + id);
				} catch (Exception e) {
					e.printStackTrace();
					modelListener.onError("Ошибка при записи метки", e.getMessage());
				}
			});
		}

		@Override
		public void onError(String message) {
			SwingUtilities.invokeLater(() -> {
				setProcessingState(false);
				System.out.println("Error " + (message == null ? "" : message));
				modelListener.onError((message == null ? "" : message), "");
			});
		}

		@Override
		public void onData(int id, RfidData data) {
			SwingUtilities.invokeLater(() -> {

				System.out.println("onData " + id);
				System.out.println("=====================");
				new RfidDataReflection(data).printFields();
				System.out.println("=====================");
				try {
					modelListener.onReadOk(new RfidDataReflection(data).getHTMLTable("#3D9970"));
					setProcessingState(false);
					checkRecord(data);
				} catch (Exception e) {
					e.printStackTrace();
					modelListener.onError("Ошибка при чтении метки", e.getMessage());
					setProcessingState(false);
				}
			});
		}

		@Override
		public void onDebugMessage(String message) {
			SwingUtilities.invokeLater(() -> {
				Model.this.modelListener.onDebugMessage(message);
			});
		}

		@Override
		public void onClearTag(int id) {

			SwingUtilities.invokeLater(() -> {
				modelListener.onClearTagOk();
			});
		}

	};

	private JFrame frame;

	public Model(JFrame frame, ModelListener modelListener) throws IOException, GeneralSecurityException {
		this.frame = frame;
		this.modelListener = modelListener;
		applyClientSettings();
		applySettings();
	}

	public void start() {
		modelListener.onDebugMessage("start");
		lib.start();
		new Thread("Rfid loop") {
			@Override
			public void run() {
				lib.loop();
			}
		}.start();
	}

	public void stop() {
		if (connectionToServer.userData != null)
			try {
				connectionToServer.proxy.logout();
			} catch (Exception e) {
				e.printStackTrace();
			}
		lib.stop();
	}

	public void onCanisterSettings() {
		try {
			boolean ok = cdUtils.editObject(canisterSettings, "Настройки", false);
			if (ok) {
				applySettings();
				GsonUtils.write(canisterSettings, canisterSettingsFile);
			}
		} catch (IOException e) {
			modelListener.onError(e.toString(), null);
		}
	}

	public void onClientSettings() {
		try {
			boolean ok = cdUtils.editObject(clientSettings, "Настройки клиента", false);
			if (ok) {
				applyClientSettings();
				GsonUtils.write(clientSettings, clientSettingsFile);
			}
		} catch (IOException e) {
			modelListener.onError(e.toString(), null);
		}
	}

	private void applyClientSettings() {
		if (lib != null)
			lib.stop();
		if (clientSettings.simLib)
			lib = new RfidLibSim(frame, libListener, clientSettings);
		else {
			try {
				lib = new RfidLibJni(libListener);
			} catch (UnsatisfiedLinkError e) {
				JOptionPane.showMessageDialog(null, e.toString() + "\nИспользуем симуляцию");
				lib = new RfidLibSim(frame, libListener, clientSettings);
			}
		}
		connectionToServer.setServerUrl(clientSettings.serverUrl + HmcRfidRpcInterface.servletPath);
	}

	public String getServerAddr() {
		return clientSettings.serverUrl;
	}

	// public HmcRfidRpcInterface getRfidDataProviderProxy() {
	// return rfidDataProvider.getProxy();
	// }
	//
	private void applySettings() {
		rfidDataProvider.setSettings(canisterSettings);
	}

	public void setWriteMode(boolean mode) {
		writeMode = mode;
	}

	public boolean isWriteMode() {
		return writeMode;
	}

	public boolean isProcessing() {
		return processing;
	}

	public Integer getTouched() {
		return touched;
	}

	public String peekNextData() {
		try {
			return new RfidDataReflection(rfidDataProvider.peekRecord()).getHTMLTable(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getTagsQnt() {
		return "" + rfidDataProvider.getRecordsQnt();
	}

	private void checkRecord(RfidData data) throws UnsupportedEncodingException, GeneralSecurityException {
		byte[] digitalSignature = data.DIGIT_SIG;
		if (digitalSignature == null)
			return;
		// throw new RuntimeException("отсутствует подпись");
		if (!sig.check(RfidDataUtils.getKey(data), digitalSignature))
			throw new RuntimeException("неправильная подпись");
	}

	public void clearTag(int id) {
		if (touched != null && !writeMode) {
			lib.clearTag(id);
			setProcessingState(true);
		}
	}

	public void readTag(int id) {
		if (touched != null && !writeMode) {
			lib.readData(id);
			setProcessingState(true);
		}
	}

	private void setProcessingState(boolean state) {
		processing = state;
		modelListener.onProcessingStatusUpdated();
	}

	private void setTouched(Integer id) {
		touched = id;
		modelListener.onTouchedStatusUpdated();
	}

}
