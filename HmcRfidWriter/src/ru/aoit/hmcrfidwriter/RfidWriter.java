package ru.aoit.hmcrfidwriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import ru.aoit.hmcrfidwriter.ui.LoginDialog;
import ru.aoit.hmcrfidwriter.ui.PingPanel;
import ru.nppcrts.common.shared.Severity;
import ru.nppcrts.common.ui.HorPanel;

@SuppressWarnings("serial")
public class RfidWriter extends JFrame {

	public static Preferences userPrefs = Preferences.userNodeForPackage(RfidWriter.class);

	private JLabel messageLabel = new JLabel(" ");
	private JTextArea debugTextArea = new JTextArea();
	private JEditorPane tagProperties = new JEditorPane("text/html", "");
	private JLabel indicatorLabel = new JLabel("");

	// private JProgressBar progressBar = new JProgressBar();

	private Model model = new Model(this, new ModelListener() {
		@Override
		public void onError(String str, String details) {
			setMessage(Severity.ERROR, str, details);
			indicatorLabel.setIcon(new ImageIcon(RfidWriter.class.getResource("/resources/rfid-fail-96.png")));
		}

		@Override
		public void onWriteDoneOk(String writtenData) {
			tagProperties.setContentType("text/html");
			tagProperties.setText("<h2>Осталось меток: " + model.getTagsQnt()
					+ "<br><br>Данные успешно записаны</h2>\n\n" + writtenData);
			tagProperties.setCaretPosition(0);
			setMessage(Severity.INFO, "Метка записана", null);
			indicatorLabel.setIcon(new ImageIcon(RfidWriter.class.getResource("/resources/rfid-ok-96.png")));
		}

		@Override
		public void onReadOk(String readedData) {
			tagProperties.setContentType("text/html");
			tagProperties.setText("<h2>Данные на метке:</h2>\n\n" + readedData);
			tagProperties.setCaretPosition(0);
			setMessage(Severity.INFO, "Метка прочитана", null);
			indicatorLabel.setIcon(new ImageIcon(RfidWriter.class.getResource("/resources/rfid-ok-96.png")));
		}

		@Override
		public void onDebugMessage(String message) {
			debugTextArea.append(message + "\n");
		}

		@Override
		public void onConnectionStatusUpdated() {
			updateUI();
		}

		@Override
		public void onDetouch() {
			indicatorLabel.setIcon(null);
			if (writeButton.isSelected()) {
				tagProperties.setContentType("text/html");
				String toBeWrite = model.peekNextData();
				tagProperties.setText("<h2>Осталось меток: " + model.getTagsQnt()
						+ "<br><br>Будут записаны данные:</h2>\n\n" + toBeWrite);
			} else {
				tagProperties.setContentType("text/plain");
				tagProperties.setText("");
			}
		}

		@Override
		public void onTouch() {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProcessingStatusUpdated() {
			if (model.isProcessing())
				indicatorLabel.setIcon(new ImageIcon(RfidWriter.class.getResource("/resources/rfid-processing.gif")));
		}

		@Override
		public void onClearTagOk() {
			setMessage(Severity.INFO, "Данные стёрты с метки", null);
		}

		@Override
		public void onTouchedStatusUpdated() {
			updateUI();
		}

	});

	public PingPanel pingPanel = new PingPanel(model);
	private JButton loginButton = new JButton("LOGIN");
	private JToggleButton writeButton = new JToggleButton("Запись");
	private JButton readTagBtn = new JButton("Прочесть метку");
	private JButton clearTagBtn = new JButton("Стереть метку");

	private void setMessage(Severity severity, String str, String details) {
		messageLabel.setForeground(severity == Severity.ERROR ? Color.RED : Color.BLACK);
		messageLabel.setText(str);
		if (details != null)
			messageLabel.setToolTipText("<html><p width=\"400\">" + details + "</p></html>");
	}

	public RfidWriter() throws IOException, GeneralSecurityException {
		UIManager.put("OptionPane.okButtonText", "ОК");
		UIManager.put("OptionPane.cancelButtonText", "Отмена");

		UIManager.put("OptionPane.yesButtonText", "Да");
		UIManager.put("OptionPane.noButtonText", "Нет");

		setTitle("Запись RFID меток");
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				model.start();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				model.stop();
				e.getWindow().setVisible(false);
				System.exit(0);
			}
		});

		debugTextArea.setPreferredSize(new Dimension(800, 400));

		JScrollPane debugAreaContainer = new JScrollPane(debugTextArea);
		getContentPane().add(debugAreaContainer, BorderLayout.EAST);

		JPanel mainAreaContainer = new JPanel(new BorderLayout());

		// colorPanel.add(progressBar, BorderLayout.SOUTH);
		getContentPane().add(mainAreaContainer);

		JPanel panel = new JPanel();
		mainAreaContainer.add(panel, BorderLayout.WEST);

		JPanel containerPanel = new JPanel(new BorderLayout(0, 0));
		panel.add(containerPanel);

		JButton settingsButton = new JButton("Параметры канистры");
		settingsButton.addActionListener(e -> model.onCanisterSettings());

		JButton clientSettingsButton = new JButton("Настройки");
		clientSettingsButton.addActionListener(e -> model.onClientSettings());

		writeButton.addActionListener(e -> {
			model.setWriteMode(writeButton.isSelected());
			if (writeButton.isSelected()) {
				tagProperties.setContentType("text/html");
				String toBeWrite = model.peekNextData();
				tagProperties.setText("<h2>Осталось меток: " + model.getTagsQnt()
						+ "<br><br>Будут записаны данные:</h2>\n\n" + toBeWrite);
			}
//			try {
//				UIManager.setLookAndFeel(writeButton.isSelected() ? new FlatDarkLaf() : new FlatLightLaf());
//				FlatLaf.updateUI();
//			} catch (UnsupportedLookAndFeelException e1) {
//				e1.printStackTrace();
//			}
		});

		JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		containerPanel.add(mainPanel, BorderLayout.NORTH);
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (model.connectionToServer.userData != null) {

						int result = JOptionPane.showConfirmDialog(null, "Выйти?", null, JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);

						if (result == JOptionPane.YES_OPTION) {
							model.connectionToServer.proxy.logout();
							model.setWriteMode(false);
							System.out.println("logout");
						}
						return;
					}
					LoginDialog.login(model, RfidWriter.this, userPrefs);
				} catch (Exception e1) {
					// e1.printStackTrace();
				} finally {
					new Thread(() -> model.connectionToServer.updateConnectionStatus()).run();
				}
			}
		});

		HorPanel hp = new HorPanel(20, pingPanel, loginButton);
		mainPanel.add(hp);
		GridLayout gl_buttonsPanel = new GridLayout(0, 1);
		gl_buttonsPanel.setVgap(5);
		JPanel buttonsPanel = new JPanel(gl_buttonsPanel);
		buttonsPanel.add(clientSettingsButton);
		buttonsPanel.add(settingsButton);
		buttonsPanel.add(writeButton);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		buttonsPanel.add(readTagBtn);

		readTagBtn.addActionListener(e -> model.readTag(model.getTouched()));

		clearTagBtn.addActionListener(e -> model.clearTag(model.getTouched()));

		buttonsPanel.add(clearTagBtn);
		JPanel colorPanel = new JPanel();
		colorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		containerPanel.add(colorPanel);
		colorPanel.setLayout(new BorderLayout(0, 0));

		JPanel indicatorPanel = new JPanel();
		colorPanel.add(indicatorPanel, BorderLayout.CENTER);

		indicatorPanel.add(indicatorLabel);
		indicatorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		tagProperties.setEditable(false);
		tagProperties.setBackground(Color.WHITE);
		tagProperties.setMinimumSize(new Dimension(800, 400));
		tagProperties.setPreferredSize(new Dimension(800, 400));
		tagProperties.setMaximumSize(new Dimension(800, 400));

		JPanel scrollPaneContainer = new JPanel(new BorderLayout());
		mainAreaContainer.add(scrollPaneContainer, BorderLayout.CENTER);

		JToggleButton showLogBtn = new JToggleButton("Log");
		showLogBtn.setMargin(new Insets(0, 0, 0, 0));
		scrollPaneContainer.add(showLogBtn, BorderLayout.EAST);
		debugAreaContainer.setVisible(showLogBtn.isSelected());

		showLogBtn.addActionListener(e -> {
			debugAreaContainer.setVisible(showLogBtn.isSelected());
			pack();
		});

		JScrollPane scrollPane = new JScrollPane(tagProperties);
		scrollPaneContainer.add(scrollPane);
		messageLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

		getContentPane().add(messageLabel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);

		new Thread(() -> model.connectionToServer.updateConnectionStatus()).run();

	}

	public static void main(String[] args) throws Exception {
		if (System.getProperty("os.name").contains("Windows"))
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		else {
			FlatLightLaf.install();
			UIManager.put("Button.arc", 99);
			UIManager.put("Component.arc", 10);
			UIManager.put("TextComponent.arc", 10);
		}
		SwingUtilities.invokeLater(() -> {
			try {
				RfidWriter writer = new RfidWriter();
				writer.setVisible(true);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
				System.exit(1);
			}
		});
	}

	private void updateUI() {
		pingPanel.updatePingPanelUI(model.connectionToServer.connected);
		loginButton.setEnabled(model.connectionToServer.connected);
		loginButton
				.setText(model.connectionToServer.userData != null ? userPrefs.get("logginedUser", "Войти") : "Войти");

		boolean canWrite = model.connectionToServer.userData != null && model.connectionToServer.userData.canWrite;
		writeButton.setEnabled(canWrite);
		if (!canWrite)
			writeButton.setSelected(false);

		readTagBtn.setEnabled(model.getTouched() != null);
		clearTagBtn.setEnabled(model.getTouched() != null);
	}

	// @SuppressWarnings("unused")
	// private static TableModel toTableModel(Map<?, ?> map) {
	// DefaultTableModel model = new DefaultTableModel(new Object[] {
	// "Параметр", "Значение" }, 0) {
	// @Override
	// public boolean isCellEditable(int row, int column) {
	// return false;
	// }
	// };
	// for (Map.Entry<?, ?> entry : map.entrySet()) {
	// model.addRow(new Object[] { entry.getKey(), entry.getValue() });
	// }
	// return model;
	// }
}
