package com.ma.hmcrfidserver.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class EditableDataMap<P, T> extends EditableData<P, T> {
	private final Map<P, T> cells = new HashMap<>();

	protected void onChanged() {
	}

	@Override
	protected T _get(P pos) {
		return cells.get(pos);
	}

	@Override
	protected P _set(P pos, T t) {
		cells.remove(pos);
		if (t != null)
			cells.put(pos, t);
		onChanged();
		return pos;
	}

	public void forEach(BiConsumer<? super P, ? super T> action) {
		cells.forEach(action);
	}

	public void clear() {
		cells.clear();
		clearUndoRedo();
		onChanged();
	}

	public Set<P> keySet() {
		return cells.keySet();
	}
}
