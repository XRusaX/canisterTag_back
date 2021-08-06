package ru.aoit.hmcrfidwriter.lib.sim;

import java.io.IOException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import ru.aoit.hmc.rfid.rpcdata.HmcReport;
import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmcrfidwriter.ClientSettings;
import ru.nppcrts.common.gson.GsonUtils;

@SuppressWarnings("serial")
public class HmcSimulator extends JDialog {

	private Box box = Box.createVerticalBox();
	private ClientSettings clientSettings;

	public HmcSimulator(JFrame owner, ClientSettings clientSettings) {
		super(owner);
		this.clientSettings = clientSettings;
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		add(box);
	}

	public void addCanister(RfidData data) {
		JButton button = new JButton(data.CANISTER_NAME);
		box.add(button);

		button.setText(data.CANISTER_NAME + " " + data.CANISTER_VOLUME_ML);

		button.addActionListener(e -> {
			int ml = 500;
			data.CANISTER_VOLUME_ML -= ml;
			button.setText(data.CANISTER_NAME + " " + data.CANISTER_VOLUME_ML);

			HmcReport r = new HmcReport();
			r.canisterId = data.UNIQUE_ID;
			r.consumtionML = ml;

			try {
				GsonUtils.requestJson(new URL(clientSettings.serverUrl + "/report"), r, null, null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		pack();
	}
}
