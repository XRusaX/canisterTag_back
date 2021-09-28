package com.ma.hmcrfidserver.client.geditor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class Utils {
	public static Set<P> findFilledArea(P p, Rect bounds, Predicate<P> toFill) {
		Set<P> result = new HashSet<>();
		Queue<P> queue = new LinkedList<>();
		queue.add(p);

		while (!queue.isEmpty()) {
			P coord = queue.poll();

			if (!bounds.inside(coord) || result.contains(coord))
				continue;

			if (toFill.test(coord)) {
				result.add(new P(coord.x, coord.y));
				queue.add(new P(coord.x + 1, coord.y));
				queue.add(new P(coord.x - 1, coord.y));
				queue.add(new P(coord.x, coord.y + 1));
				queue.add(new P(coord.x, coord.y - 1));
			}
		}
		return result;
	}
}
