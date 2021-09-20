package com.ma.hmcrfidwriter.lib.jni;

public class RFIDComponent {
	public static native byte[] receiveMessage();

	public static native void sendMessage(byte[] bytes);

	public static native void start();
}
