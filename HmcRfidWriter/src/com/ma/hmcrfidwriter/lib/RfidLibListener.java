package com.ma.hmcrfidwriter.lib;

import com.ma.hmc.rfid.ruslandata.RfidData;

public interface RfidLibListener {
	void onTouch(int id);

	void onDetouch(int id);

	void onWriteOk(Integer id);

	void onError(String message);

	void onData(int id, RfidData data);
	
	void onDebugMessage(String message);
	
	void onClearTag(int id);
}