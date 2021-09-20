package ru.aoit.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EditableData<T> {
	public abstract T getCopy(T cell);

	public abstract void setXY(T t, int x, int y);

	public abstract int getY(T t);

	public abstract int getX(T t);

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
		cells.remove(t);
	}

	private void add(T t, LinkedList<Runnable> undoList, LinkedList<Runnable> redoList) {
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

	private void moveTo(T t, int x, int y, LinkedList<Runnable> undoList, LinkedList<Runnable> redoList) {

		int x1 = getX(t);
		int y1 = getY(t);

		undoList.add(new Runnable() {
			@Override
			public void run() {
				moveTo(t, x1, y1, redoList, undoList);
			}
		});

		setXY(t, x, y);
	}

	public void moveTo(T t, int x, int y) {
		redoList.clear();
		moveTo(t, x, y, undoList, redoList);
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

}
