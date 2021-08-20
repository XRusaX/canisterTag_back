package ru.aoit.hmcrfidwriter.lib.jni;

import ru.aoit.hmc.rfid.ruslandata.RfidData;
import ru.aoit.hmcrfidwriter.lib.RfidLibListener;

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
