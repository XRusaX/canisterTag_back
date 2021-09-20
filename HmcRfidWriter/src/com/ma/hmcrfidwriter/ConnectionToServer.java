package com.ma.hmcrfidwriter;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.ma.common.rpc.HttpProxy;
import com.ma.hmc.rfid.rpcdata.User;
import com.ma.hmc.rfid.rpcinterface.HmcRfidRpcInterface;

public class ConnectionToServer {
	public HmcRfidRpcInterface proxy;
	public boolean connected;
	public User userData;
	private final Runnable statusUpdated;

	public ConnectionToServer(Runnable statusUpdated) {
		this.statusUpdated = statusUpdated;
		new Timer("updUITimer").schedule(new TimerTask() {

			@Override
			public void run() {
				updateConnectionStatus();
			}

		}, 0, 5000);
	}

	public void setServerUrl(String serverUrl) {
		proxy = HttpProxy.makeProxy(HmcRfidRpcInterface.class, serverUrl, new File("/usr/share/ca-certificates/extra/server.cer"));
	}

//	public void updateConnectionStatus() {
//		boolean connected = false;
//		UserData userData = null;
//
//		try {
//			HmcRfidRpcInterface proxy = this.proxy;
//			if (proxy != null) {
//				userData = proxy.getUser();
//				connected = true;
//			}
//		} catch (Exception e) {
//		} finally {
//			boolean connected1 = connected;
//			UserData userData1 = userData;
//			SwingUtilities.invokeLater(() -> {
//				this.connected = connected1;
//				this.userData = userData1;
//				statusUpdated.run();
//			});
//		}
//	}

	public void updateConnectionStatus() {
		try {
			HmcRfidRpcInterface proxy = this.proxy;
			if (proxy != null) {
				userData = proxy.getUser();
				connected = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			connected = false;
		} finally {
			SwingUtilities.invokeLater(() -> {
				statusUpdated.run();
			});
		}
	}

}
