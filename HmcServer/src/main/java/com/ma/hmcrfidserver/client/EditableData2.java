package com.ma.hmcrfidserver.client;

import java.util.Set;

import com.ma.hmcrfidserver.client.geditor.P;
import com.ma.hmcrfidserver.client.geditor.Rect;

public abstract class EditableData2<T> extends EditableDataMap<P, T> {
	public Rect getBounds() {
		Set<P> coords = keySet();
		int minx = coords.stream().mapToInt(c -> c.x).min().orElse(0);
		int miny = coords.stream().mapToInt(c -> c.y).min().orElse(0);
		int maxx = coords.stream().mapToInt(c -> c.x + 1).max().orElse(0);
		int maxy = coords.stream().mapToInt(c -> c.y + 1).max().orElse(0);
		return new Rect(minx, miny, maxx, maxy);
	}

	public abstract T getCopy(T cell);
}
