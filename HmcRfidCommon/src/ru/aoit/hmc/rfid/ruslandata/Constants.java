package ru.aoit.hmc.rfid.ruslandata;

import java.util.Arrays;

public enum Constants {
	RFID_DETECTED_MSG(0), //
	RFID_REMOVED_MSG(1), //
	DATA_MSG(2), //
	READ_ERROR_MSG(3), //
	WRITE_ERROR_MSG(4), //
	AUTH_ERROR_MSG(5), //
	TEXT_MSG(6), //
	STOP_REQ(7), //
	UID_REQ(8), //
	S0_REQ(9), //
	UID_S0_REQ(10), //
	READ_DATA_REQ(11), //
	WRITE_DATA_REQ(12), //
	CLEAN_REQ(13), //
	READ_MODE_REQ(14), //
	WRITE_MODE_REQ(15), //
	DEBUG_OFF_REQ(16), //
	DEBUG_ON_REQ(17), //
	CLRC663_FAILED_MSG(18), //
	UART_FAILED_MSG(19);

	public final int value;

	private Constants(int value) {
		this.value = value;

	}

	public static Constants byValue(int number) {
		return Arrays.stream(values()).filter(v -> v.value == number).findFirst().orElse(null);
	}
}
