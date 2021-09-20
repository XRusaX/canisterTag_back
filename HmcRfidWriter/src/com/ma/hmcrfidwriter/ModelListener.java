package com.ma.hmcrfidwriter;

public interface ModelListener {
	
	public void onError(String str, String details);

	public void onWriteDoneOk(String writtenData);

	public void onReadOk(String readedData);

	public void onDetouch();
	
	public void onTouch();

	public void onDebugMessage(String message);

	public void onConnectionStatusUpdated();

	public void onProcessingStatusUpdated();
	
	public void onTouchedStatusUpdated();
	
	public void onClearTagOk();

}
