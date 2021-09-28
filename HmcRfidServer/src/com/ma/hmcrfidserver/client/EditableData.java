package com.ma.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gwt.user.client.Window;
import com.ma.hmcrfidserver.client.geditor.P;
import com.ma.hmcrfidserver.client.geditor.Rect;

public abstract class EditableData<T> {
	public abstract T getCopy(T cell);

	public abstract void setPos(T t, P pos);

	public abstract P getPos(T t);

	private final List<T> cells = new ArrayList<>();

	private final LinkedList<Runnable> undoList = new LinkedList<>();
	private final LinkedList<Runnable> redoList = new LinkedList<>();

	public Stream<T> stream() {
		return cells.stream();
	}

	public void setData(List<T> list1) {
		cells.clear();
		cells.addAll(list1);
		undoList.clear();
		redoList.clear();
	}

	public void clear() {
		cells.clear();
		undoList.clear();
		redoList.clear();
	}

	private void remove(T t, LinkedList<Runnable> undoList, LinkedList<Runnable> redoList) {
		undoList.add(new Runnable() {
			@Override
			public void run() {
				add(t, redoList, undoList);
			}
		});

		boolean removed = cells.remove(t);
		if(!removed)
			throw new IllegalArgumentException();
	}

	private void add(T t, LinkedList<Runnable> undoList, LinkedList<Runnable> redoList) {
		if (t == null)
			throw new IllegalArgumentException();

		undoList.add(new Runnable() {
			@Override
			public void run() {
				remove(t, redoList, undoList);
			}
		});

		cells.add(t);
	}

	public void add(T t) {
		redoList.clear();
		add(t, undoList, redoList);
	}

	public void remove(T t) {
		redoList.clear();
		remove(t, undoList, redoList);
	}

	private void moveTo(T t, P pos, LinkedList<Runnable> undoList, LinkedList<Runnable> redoList) {

		P pos1 = getPos(t);

		undoList.add(new Runnable() {
			@Override
			public void run() {
				moveTo(t, pos1, redoList, undoList);
			}
		});

		setPos(t, pos);
	}

	public void moveTo(T t, P pos) {
		redoList.clear();
		moveTo(t, pos, undoList, redoList);
	}

	public void markUndo() {
		if (undoList.isEmpty() || undoList.getLast() == null)
			return;
		undoList.add(null);
	}

	public void undo() {
		if (!undoList.isEmpty())
			redoList.add(null);
		while (!undoList.isEmpty()) {
			Runnable last = undoList.removeLast();
			if (last == null)
				break;
			last.run();
		}
	}

	public void redo() {
		if (!redoList.isEmpty())
			undoList.add(null);
		while (!redoList.isEmpty()) {
			Runnable last = redoList.removeLast();
			if (last == null)
				break;
			last.run();
		}
	}

	public Rect getBounds() {
		List<P> coords = stream().map(c -> getPos(c)).collect(Collectors.toList());
		int minx = coords.stream().mapToInt(c -> c.x).min().orElse(0);
		int miny = coords.stream().mapToInt(c -> c.y).min().orElse(0);
		int maxx = coords.stream().mapToInt(c -> c.x + 1).max().orElse(0);
		int maxy = coords.stream().mapToInt(c -> c.y + 1).max().orElse(0);
		return new Rect(minx, miny, maxx, maxy);
	}

	public Map<P, T> getMap() {
		Map<P, T> map = new HashMap<>();
		stream().forEach(cell -> map.put(getPos(cell), cell));
		return map;
	}

	public void checkData() {
		Set<P> set = new HashSet<>();
		stream().forEach(t -> {
			P p = getPos(t);
			if (set.contains(p))
				Window.alert("Data integrity error!");
			set.add(p);
		});
	}

}
