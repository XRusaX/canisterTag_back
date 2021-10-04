package com.ma.hmc.simulator;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.ma.common.cd.swing.SwingObjectEditorPanel;
import com.ma.common.gson.GsonUtils;
import com.ma.common.prefs.PreferencesUtils;
import com.ma.common.rpc.HttpProxy;
import com.ma.common.shared.cd.UILabel;
import com.ma.common.ui.HorPanel;
import com.ma.common.ui.VertPanel;
import com.ma.hmc.rfid.rpcdata.HmcReport;
import com.ma.hmc.rfid.rpcinterface.HmcRfidRpcInterface;
import com.ma.hmc.rfid.rpcinterface.TestRpcInterface;
import com.ma.hmc.simulator.sim.WorldSim;

@SuppressWarnings("serial")
public class HmcSimulatorFrame extends JFrame {

	public static Preferences userPrefs = Preferences.userNodeForPackage(HmcSimulatorFrame.class);

	private SwingObjectEditorPanel<ConnectionSettings> connectionPanel = new SwingObjectEditorPanel<>(
			ConnectionSettings.class);
	private SwingObjectEditorPanel<HmcReport> reportPanel = new SwingObjectEditorPanel<>(HmcReport.class);
	private SwingObjectEditorPanel<FillParams> fillParamsPanel = new SwingObjectEditorPanel<>(FillParams.class);

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
				report = report(connectionPanel.getData().serverURL, reportPanel.getData());
				status.setText(report);
			} catch (IOException e) {
				status.setText(e.toString());
			}
		});

		JToggleButton startButton = new JToggleButton("Run");
		startButton.addActionListener(arg0 -> {
			savePrefs();
			if (startButton.isSelected()) {
				worldSim = new WorldSim(getProxy(), connectionPanel.getData(), fillParamsPanel.getData());
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

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				savePrefs();
			}
		});
	}

	private TestRpcInterface getProxy() {
		return HttpProxy.makeProxy(TestRpcInterface.class,
				connectionPanel.getData().serverURL + HmcRfidRpcInterface.api + TestRpcInterface.servletPath, null);
	}

	private void loadPrefs() {
		ConnectionSettings conn = PreferencesUtils.fromPrefs(userPrefs, "connection", ConnectionSettings.class,
				new ConnectionSettings());
		connectionPanel.setData(conn);

		HmcReport hmcReport = PreferencesUtils.fromPrefs(userPrefs, "hmc", HmcReport.class, new HmcReport(null));
		reportPanel.setData(hmcReport);

		FillParams fillParams = PreferencesUtils.fromPrefs(userPrefs, "fillParams", FillParams.class, new FillParams());
		fillParamsPanel.setData(fillParams);
	}

	private void savePrefs() {
		PreferencesUtils.toPrefs(userPrefs, "connection", connectionPanel.getData());
		PreferencesUtils.toPrefs(userPrefs, "hmc", reportPanel.getData());
		PreferencesUtils.toPrefs(userPrefs, "fillParams", fillParamsPanel.getData());
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
			getProxy().clear();
			status.setText("success");
		} catch (Exception e) {
			status.setText(e.toString());
		}
	}

	private void fill() {
		try {
			new WorldSim(getProxy(), connectionPanel.getData(), fillParamsPanel.getData()).createDB();
			status.setText("success");
		} catch (Exception e) {
			status.setText(e.toString());
		}
	}

	public static String report(String serverURL, HmcReport report) throws IOException {
		report.startTime = new Date().getTime();
		String requestJson = GsonUtils.requestJson(new URL(serverURL + HmcRfidRpcInterface.api + "/report"), report,
				String.class, null);
		return requestJson;
	}

}
