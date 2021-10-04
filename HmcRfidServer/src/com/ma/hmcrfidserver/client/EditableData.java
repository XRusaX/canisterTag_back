package com.ma.hmcrfidserver.client;

import java.util.LinkedList;

public abstract class EditableData<POS, T> {
	private final LinkedList<Runnable> undoList = new LinkedList<>();
	private final LinkedList<Runnable> redoList = new LinkedList<>();

	protected void clearUndoRedo() {
		undoList.clear();
		redoList.clear();
	}

	public void replace(POS pos, T t) {
		redoList.clear();
		replace(pos, t, undoList, redoList);
	}

	protected abstract T _get(POS pos);

	protected abstract POS _set(POS pos, T t);

	private void replace(POS pos, T t, LinkedList<Runnable> undoList, LinkedList<Runnable> redoList) {
		T oldT = _get(pos);
		if(t == oldT)
			return;
		POS pos2 = _set(pos, t);
		undoList.add(() -> replace(pos2, oldT, redoList, undoList));
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

	public void markUndo() {
		if (undoList.isEmpty() || undoList.getLast() == null)
			return;
		undoList.add(null);
	}

}
