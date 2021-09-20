package com.ma.hmcrfidwriter;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.ma.common.rpc.HttpProxy;
import com.ma.hmc.rfid.rpcinterface.HmcRfidRpcInterface;

public class Test {
	static HmcRfidRpcInterface proxy = HttpProxy.makeProxy(HmcRfidRpcInterface.class,
			// "http://localhost:8899/hmcrfidserver/rpcserver"
			// "https://localhost:8443/hmcrfidserver/rpcserver"
			"https://localhost:8443/desinfection/hmcrfidserver/rpcserver", new File("/usr/share/ca-certificates/extra/server.csr"));

	public static void main(String[] args) throws Exception {

		while (JOptionPane.CANCEL_OPTION == JOptionPane.showConfirmDialog(null, new JButton("XXX"), "title",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE))
			;

		// RfidData data = new RfidData();
		// data.CANISTER_VOLUME_ML = 10;
		// data.CANISTER_MANUFACTURER_NAME = "man";
		// data.CANISTER_NAME = "nam";
		//
		// List<IdSig> sigs = proxy.getSigs(data, 10);
		//
		// System.out.println(new Gson().toJson(sigs));
	}

}
