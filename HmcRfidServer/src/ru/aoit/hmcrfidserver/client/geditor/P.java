package ru.aoit.hmcrfidserver.client.geditor;

public class P {
	public int x;
	public int y;

	public P(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		return x * 10000 + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		P other = (P) obj;
		return x == other.x && y == other.y;
	}
}