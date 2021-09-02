package ru.aoit.hmc.simulator;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;

import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.aoit.hmc.rfid.rpcinterface.TestRpcInterface;
import ru.aoit.hmc.simulator.sim.HmcSim;
import ru.aoit.hmc.simulator.sim.WorldSim;
import ru.nppcrts.common.cd.swing.SwingObjectEditorPanel;
import ru.nppcrts.common.rpc.HttpProxy;
import ru.nppcrts.common.shared.cd.UILabel;
import ru.nppcrts.common.ui.HorPanel;
import ru.nppcrts.common.ui.VertPanel;

@SuppressWarnings("serial")
public class HmcSimulatorFrame extends JFrame {

	public static Preferences userPrefs = Preferences.userNodeForPackage(HmcSimulatorFrame.class);

	private SwingObjectEditorPanel<ConnectionSettings> connectionPanel = new SwingObjectEditorPanel<>(
			ConnectionSettings.class);
	private SwingObjectEditorPanel<HmcReport> reportPanel = new SwingObjectEditorPanel<>(HmcReport.class);
	private SwingObjectEditorPanel<FillParams> fillParamsPanel = new SwingObjectEditorPanel<>(FillParams.class);

	private TestRpcInterface proxy;

	private JLabel status = new JLabel(" ");

	public static class FillParams {
		@UILabel(label = "companies")
		public int companies = 10;
		@UILabel(label = "hmcs")
		public int hmcs = 10;
		@UILabel(label = "rooms")
		public int rooms = 10;
		@UILabel(label = "operators")
		public int operators = 10;
	}

	private WorldSim worldSim;

	public HmcSimulatorFrame() {
		setLayout(new BorderLayout());
		// setSize(1000, 1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Симулятор МГЦ");

		JJButton reportButton = new JJButton("Report", arg0 -> {
			savePrefs();
			String report;
			try {
				report = HmcSim.report(connectionPanel.getData().serverURL, reportPanel.getData());
				status.setText(report);
			} catch (IOException e) {
				status.setText(e.toString());
			}
		});

		JToggleButton startButton = new JToggleButton("Run");
		startButton.addActionListener(arg0 -> {
			savePrefs();
			if (startButton.isSelected()) {
				worldSim = new WorldSim(proxy, connectionPanel.getData().serverURL, fillParamsPanel.getData());
				worldSim.start();
			} else {
				if (worldSim != null)
					worldSim.stop();
				worldSim = null;
			}
		});

		add(new HorPanel(connectionPanel, //
				new VertPanel(reportPanel, new HorPanel(Box.createHorizontalGlue(), reportButton)), //
				new VertPanel(fillParamsPanel, new HorPanel(//
						Box.createHorizontalGlue(), //
						new JJButton("clear", a -> clear()), //
						new JJButton("fill", a -> fill()), //
						startButton//
				))//
		));

		add(status, BorderLayout.SOUTH);

		setResizable(false);
		pack();

		loadPrefs();

		proxy = HttpProxy.makeProxy(TestRpcInterface.class, connectionPanel.getData().serverURL + "/testapp", null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				savePrefs();
			}
		});
	}

	private void loadPrefs() {
		ConnectionSettings conn = fromPrefs(userPrefs, "connection", ConnectionSettings.class,
				new ConnectionSettings());
		connectionPanel.setData(conn);

		HmcReport hmcReport = fromPrefs(userPrefs, "hmc", HmcReport.class, new HmcReport());
		reportPanel.setData(hmcReport);

		FillParams fillParams = fromPrefs(userPrefs, "fillParams", FillParams.class, new FillParams());
		fillParamsPanel.setData(fillParams);
	}

	private void savePrefs() {
		toPrefs(userPrefs, "connection", connectionPanel.getData());
		toPrefs(userPrefs, "hmc", reportPanel.getData());
		toPrefs(userPrefs, "fillParams", fillParamsPanel.getData());
	}

	public static void toPrefs(Preferences userPrefs, String key, Object o) {
		userPrefs.put(key, new Gson().toJson(o));
	}

	public static <T> T fromPrefs(Preferences userPrefs, String key, Class<T> clazz, T defaultValue) {
		String str = userPrefs.get(key, null);
		if (str == null)
			return defaultValue;
		try {
			T t = new Gson().fromJson(str, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				HmcSimulatorFrame frame = new HmcSimulatorFrame();
				frame.setVisible(true);
			}
		});

	}

	public static class JJButton extends JButton {
		public JJButton(String text, ActionListener l) {
			super(text);
			addActionListener(l);
		}
	}

	private void clear() {
		try {
			proxy.clear();
			status.setText("success");
		} catch (Exception e) {
			status.setText(e.toString());
		}
	}

	private void fill() {
		try {
			new WorldSim(proxy, connectionPanel.getData().serverURL, fillParamsPanel.getData()).createDB();
			status.setText("success");
		} catch (Exception e) {
			status.setText(e.toString());
		}
	}
}
