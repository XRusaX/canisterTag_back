package com.ma.hmcrfidserver.client.geditor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class Utils {
	public Set<P> getPolygonForCoords(int x, int y, int maxx, int maxy, Predicate<P> isWall) {
		Set<P> result = new HashSet<>();
		Queue<P> queue = new LinkedList<>();
		queue.add(new P(x, y));

		while (!queue.isEmpty()) {
			P coord = queue.poll();

			if (coord.x < 0 || coord.y < 0 || coord.x >= maxx || coord.y >= maxy
					|| result.contains(coord))
				continue;
			
//			Cell point = getCell(coord.x, coord.y);
//			if (point == null || point.room != null) {
//				result.add(new P(coord.x, coord.y));
//				queue.add(new P(coord.x + 1, coord.y));
//				queue.add(new P(coord.x - 1, coord.y));
//				queue.add(new P(coord.x, coord.y + 1));
//				queue.add(new P(coord.x, coord.y - 1));
//
//			}
		}
		return result;
	}

}
