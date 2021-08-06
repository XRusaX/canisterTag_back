package ru.aoit.hmcrfidwriter.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import ru.aoit.hmcrfidwriter.Model;

public class LoginDialog {

	private static void addUserNameToPrefs(String userName, Preferences userPrefs) {
		String users = userPrefs.get("users", "");
		String currentUser = userName.trim();
		if (!users.isEmpty()) {
			if (Arrays.asList(users.split(";")).stream().noneMatch(u -> currentUser.equals(u)))
				users += ";" + userName.trim();
		} else
			users += userName.trim();

		userPrefs.put("users", users);
		userPrefs.put("lastUser", currentUser);
	}

	private static String[] getUsersArrayFromPrefs(Preferences userPrefs) {
		String users = userPrefs.get("users", "");
		return users.split(";");
	}

	public static void login(Model model, Component parent, Preferences userPrefs) {
		GridLayout grid = new GridLayout(2, 2);
		grid.setVgap(10);
		grid.setHgap(5);
		JPanel panel = new JPanel(grid);

		JComboBox<String> usersList = new JComboBox<String>();
		usersList.setEditable(true);
		usersList.setMaximumRowCount(4);
		JPasswordField passwordTF = new JPasswordField();

		panel.add(new JLabel("Имя пользователя :"));
		panel.add(usersList);
		panel.add(new JLabel("Пароль :"));
		panel.add(passwordTF);

		usersList.setModel(new DefaultComboBoxModel<String>(getUsersArrayFromPrefs(userPrefs)));
		usersList.setSelectedItem(userPrefs.get("lastUser", ""));

		for (;;) {
			int res = JOptionPane.showConfirmDialog(parent, panel, null, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);

			if (res != JOptionPane.OK_OPTION)
				break;

			try {
				String userName = usersList.getSelectedItem().toString().trim();
				model.connectionToServer.proxy.login(userName, new String(passwordTF.getPassword()).trim());
				userPrefs.put("logginedUser", userName);
				System.out.println("login");
				addUserNameToPrefs(userName, userPrefs);
				break;
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Не удалось войти", "Ошибка авторизации",
						JOptionPane.ERROR_MESSAGE);
			}

		}

	}

}
