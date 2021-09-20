package com.ma.hmcrfidserver.client.geditor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Rect {
	public int x0;
	public int y0;
	public int x1;
	public int y1;

	public Rect(P xy) {
		this(xy.x, xy.y);
	}

	public Rect(int x, int y) {
		this(x, y, x, y);
	}

	public Rect(int x0, int y0, int x1, int y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public Rect normCopy() {
		int x = getWidth() > 0 ? x0 : x1 + 1;
		int y = getHeigth() > 0 ? y0 : y1 + 1;
		return new Rect(x, y, x + Math.abs(getWidth()), y + Math.abs(getHeigth()));
	}

	public void move(int dx, int dy) {
		x0 += dx;
		x1 += dx;
		y0 += dy;
		y1 += dy;
	}

	public boolean inside(int x, int y) {
		int xx0 = x0;
		int xx1 = x1;
		int yy0 = y0;
		int yy1 = y1;

		if (x0 > x1) {
			xx0 = x1 + 1;
			xx1 = x0 + 1;
		}

		if (y0 > y1) {
			yy0 = y1 + 1;
			yy1 = y0 + 1;
		}

		return x >= xx0 && x < xx1 && y >= yy0 && y < yy1;
	}

	public int getWidth() {
		return x1 - x0;
	}

	public int getHeigth() {
		return y1 - y0;
	}

	public List<P> getLine() {
		List<P> res = new ArrayList<>();
		if (Math.abs(getWidth()) > Math.abs(getHeigth())) {
			for (int x = x0; x != x1; x += x1 > x0 ? 1 : -1)
				res.add(new P(x, y0));
		} else {
			for (int y = y0; y != y1; y += y1 > y0 ? 1 : -1)
				res.add(new P(x0, y));
		}
		return res;
	}

	public void foreach(Consumer<P> consumer) {
		for (int x = x0; x != x1; x += x1 > x0 ? 1 : -1)
			for (int y = y0; y != y1; y += y1 > y0 ? 1 : -1)
				consumer.accept(new P(x, y));
	}

	public boolean isEmpty() {
		return x0 == x1 && y0 == y1;
	}
}