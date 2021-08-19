package ru.aoit.hmc.simulator;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import ru.nppcrts.common.cd.swing.SwingObjectEditorPanel;
import ru.nppcrts.common.ui.HorPanel;
import ru.nppcrts.common.ui.VertPanel;

@SuppressWarnings("serial")
public class HmcSimulatorFrame extends JFrame {

	public static Preferences userPrefs = Preferences.userNodeForPackage(HmcSimulatorFrame.class);

	private SwingObjectEditorPanel<ConnectionSettings> connectionPanel = new SwingObjectEditorPanel<>(
			ConnectionSettings.class);
	private SwingObjectEditorPanel<HmcReport> reportPanel = new SwingObjectEditorPanel<>(HmcReport.class);

	public HmcSimulatorFrame() {
		setLayout(new BorderLayout());
		// setSize(1000, 1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Симулятор МГЦ");

		add(new HorPanel(connectionPanel, reportPanel));

		JLabel status = new JLabel(" ");

		JToggleButton startButton = new JToggleButton("Run");
		startButton.addActionListener(arg0 -> {
			savePrefs();
			HmcSim hmcSim = new HmcSim(connectionPanel.get(null), reportPanel.get(null));
			if (startButton.isSelected())
				hmcSim.start();
			else
				hmcSim.stop();
		});

		JButton reportButton = new JButton("Report");
		reportButton.addActionListener(arg0 -> {
			savePrefs();
			HmcSim hmcSim = new HmcSim(connectionPanel.get(null), reportPanel.get(null));
			String report = hmcSim.report();
			status.setText(report);
		});

		add(new VertPanel(new HorPanel(reportButton, Box.createHorizontalGlue(), startButton), status),
				BorderLayout.SOUTH);

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

	private void loadPrefs() {
		ConnectionSettings conn = fromPrefs(userPrefs, "connection", ConnectionSettings.class,
				new ConnectionSettings());
		connectionPanel.set(conn);

		HmcReport hmcReport = fromPrefs(userPrefs, "hmc", HmcReport.class, new HmcReport());
		reportPanel.set(hmcReport);

	}

	private void savePrefs() {
		toPrefs(userPrefs, "connection", connectionPanel.get(null));
		toPrefs(userPrefs, "hmc", reportPanel.get(null));
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
}
