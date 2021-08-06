package ru.aoit.hmcrfidwriter.ui;

import javax.swing.ImageIcon;

public enum PingStatus {
	INIT(new ImageIcon(PingPanel.class.getResource("/resources/connection-status-init-16.png"))), //
	FAIL(new ImageIcon(PingPanel.class.getResource("/resources/connection-status-off-16.png"))), //
	OK(new ImageIcon(PingPanel.class.getResource("/resources/connection-status-on-16.png")));

	private ImageIcon imageIcon;

	PingStatus(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}
}