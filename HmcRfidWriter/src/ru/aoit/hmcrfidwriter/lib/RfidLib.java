package ru.aoit.hmcrfidwriter.lib;

import ru.aoit.hmc.rfid.ruslandata.RfidData;

public interface RfidLib {
	void start();

	void stop();

	void writeData(int id, RfidData data);

	void readData(int id);
	
	void clearTag(int id);

	// этот метод запускает нативный цикл, из которого вызывает "onXXX()" методы
	// завершается при вызове stop()
	void loop();

}
