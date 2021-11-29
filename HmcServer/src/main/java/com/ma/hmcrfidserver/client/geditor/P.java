package com.ma.hmcrfidserver.client.geditor;

import java.util.Collection;

public class P {
	public final int x;
	public final int y;

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

	public static P massCenter(Collection<P> list) {
		int x = (int) list.stream().mapToInt(p -> p.x).average().orElse(0);
		int y = (int) list.stream().mapToInt(p -> p.y).average().orElse(0);
		return new P(x, y);
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}