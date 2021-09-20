package com.ma.hmcrfidwriter.lib.jni;

import com.ma.hmc.rfid.ruslandata.RfidData;
import com.ma.hmcrfidwriter.lib.RfidLibListener;

public class TestJni {

	public static void main(String[] args) {
		RfidLibJni lib = new RfidLibJni(new RfidLibListener() {

			@Override
			public void onWriteOk(Integer id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTouch(int id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(String message) {
				System.out.println(message);
			}

			@Override
			public void onDetouch(int id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onData(int id, RfidData data) {
			}

			@Override
			public void onDebugMessage(String message) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onClearTag(int id) {
				// TODO Auto-generated method stub
				
			}
		});

		lib.writeData(10, new RfidData());

		lib.stop();
	}
}
