package ru.aoit.hmcrfidwriter.ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.aoit.hmcrfidwriter.Model;
import ru.nppcrts.common.ui.HorPanel;

@SuppressWarnings("serial")
public class PingPanel extends JPanel {
	public final JLabel pingStatus = new JLabel(
			new ImageIcon(getClass().getResource("/resources/connection-status-init-16.png")));
	private Model model;

	public PingPanel(Model model) {
		this.model = model;
		add(new HorPanel(5, pingStatus, new JLabel("Соединение")));
	}

	// Метод публичный, потому что вызывался в RfidWriter
	public void updatePingPanelUI(boolean connected) {
		String addr = model.getServerAddr();
		if (addr != null && !addr.isEmpty()) {
			pingStatus.setIcon(connected ? PingStatus.OK.getImageIcon() : PingStatus.FAIL.getImageIcon());
			setToolTipText("Адрес сервера: " + model.getServerAddr());
		} else {
			setToolTipText("Адрес сервера: не задан");
			pingStatus.setIcon(PingStatus.INIT.getImageIcon());
		}
	}

}
