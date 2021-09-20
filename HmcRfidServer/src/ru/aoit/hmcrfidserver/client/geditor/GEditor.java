package ru.aoit.hmcrfidserver.client.geditor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import ru.aoit.hmcrfidserver.client.EditableData;
import ru.nppcrts.common.shared.Pair;
import ru.nppcrts.common.shared.canvas.Drawable;
import ru.nppcrts.common.shared.canvas.PaintTarget;
import ru.nppcrts.common.shared.canvas.XY;

public abstract class GEditor<T> extends Drawable {
	private static final int CELL_SIZE = 16;

	private EditableData<T> editableData;

	protected abstract int getX(T t);

	protected abstract int getY(T t);

	protected abstract String getColor(T t);

	protected abstract boolean canEdit();

	protected abstract boolean isSelected(T t);

	public GEditor(EditableData<T> cells) {
		this.editableData = cells;
	}

	private static int getCellX(double x) {
		return (int) x / CELL_SIZE;
	}

	private static int getCellY(double y) {
		return (int) y / CELL_SIZE;
	}

	private Rect drag;

	private ClipboardData clipboardData;

	@Override
	public void draw(PaintTarget paintTarget) {
		paintTarget.setColor("lightgray");

		int maxx = editableData.stream().mapToInt(c -> getX(c)).max().orElse(0) + 5;
		int maxy = editableData.stream().mapToInt(c -> getY(c)).max().orElse(0) + 5;

		for (int x = 0; x <= maxx + 1; x++)
			paintTarget.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, (maxy + 1) * CELL_SIZE);

		for (int y = 0; y <= maxy + 1; y++)
			paintTarget.drawLine(0, y * CELL_SIZE, (maxx + 1) * CELL_SIZE, y * CELL_SIZE);

		int dx = drag == null ? 0 : drag.getWidth();
		int dy = drag == null ? 0 : drag.getHeigth();

		editableData.stream().forEach(cell -> {
			int x = getX(cell);
			int y = getY(cell);

			if (clipboardData != null && clipboardData.list != null && clipboardData.rect.inside(x - dx, y - dy))
				return;

			paintTarget.setColor(getColor(cell));
			paintTarget.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

			if (isSelected(cell)) {
				paintTarget.setColor("black");
				paintTarget.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		});

		if (clipboardData != null) {
			Rect copy = clipboardData.rect.normCopy();
			copy.move(dx, dy);
			if (clipboardData.list != null)
				clipboardData.list.stream().forEach(cell -> {
					paintTarget.setColor(getColor(cell));
					int x = getX(cell) + dx;
					int y = getY(cell) + dy;
					paintTarget.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				});
			paintTarget.setLineWidth(4);
			drawRect(paintTarget, copy, "magenta");
			paintTarget.setLineWidth(1);

		}

		if (drag != null && clipboardData == null) {
			drawRect(paintTarget, drag, "black");
		}

	}

	private static void drawRect(PaintTarget paintTarget, Rect rect, String color) {
		paintTarget.setColor(color);
		rect = rect.normCopy();
		paintTarget.drawRect(rect.x0 * CELL_SIZE, rect.y0 * CELL_SIZE, rect.getWidth() * CELL_SIZE,
				rect.getHeigth() * CELL_SIZE);
	}

	class ClipboardData {
		private Rect rect;
		public List<T> list;

		public ClipboardData(Rect drag) {
			rect = drag.normCopy();
		}

		public void move(int dx, int dy) {
			rect.move(dx, dy);
			list.forEach(cell -> editableData.setXY(cell, editableData.getX(cell) + dx, editableData.getY(cell) + dy));
		}
	}

	private void copy() {
		GWT.log("copy");

		clipboardData.list = editableData.stream().filter(cell -> {
			int x = getX(cell);
			int y = getY(cell);
			return clipboardData.rect.inside(x, y);
		}).map(cell -> editableData.getCopy(cell)).collect(Collectors.toList());
	}

	private void cut() {
		GWT.log("cut");
		copy();
		convertRect(clipboardData.rect, (x, y, t) -> null);
	}

	private void paste() {
		GWT.log("paste");

		Map<P, T> map = new HashMap<>();
		clipboardData.list.stream().forEach(cell -> map.put(new P(getX(cell), getY(cell)), cell));

		convertRect(clipboardData.rect, (x, y, t) -> {
			T t2 = map.get(new P(x, y));
			return t2;
		});

		checkData();
	}

	@Override
	public boolean onMousePressed(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		if (clipboardData != null && !clipboardData.rect.inside(getCellX(xy.x), getCellY(xy.y))) {
			if (clipboardData.list != null)
				paste();
			clipboardData = null;
		}

		invalidate();
		return true;
	}

	@Override
	public boolean onMouseDragged(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		if (clipboardData != null && clipboardData.list == null
				&& clipboardData.rect.inside(getCellX(xy.x), getCellY(xy.y))) {
			editableData.markUndo();
			if (control)
				copy();
			else
				cut();
		}

		if (drag == null)
			drag = new Rect(getCellX(xy.x), getCellY(xy.y));

		drag.x1 = getCellX(xy.x);
		drag.y1 = getCellY(xy.y);

		invalidate();
		return true;
	}

	@Override
	public boolean onMouseReleased(XY xy, boolean shift, boolean control) {
		GWT.log("onMouseReleased");

		if (!canEdit())
			return false;

		if (drag == null)
			return false;

		if (clipboardData == null)
			clipboardData = new ClipboardData(drag);
		else if (clipboardData.list != null)
			clipboardData.move(drag.getWidth(), drag.getHeigth());

		drag = null;

		invalidate();
		return true;
	}

	private void checkData() {
		Set<Pair<Integer, Integer>> set = new HashSet<>();
		editableData.stream().forEach(t -> {
			Pair<Integer, Integer> pair = Pair.create(getX(t), getY(t));
			if (set.contains(pair))
				Window.alert("Data integrity error!");
			set.add(pair);
		});
	}

	public void dropSel() {
		clipboardData = null;
		drag = null;
		invalidate();
	}

	public interface FF<T> {
		T apply(int x, int y, T old);
	}

	public void convertRect(Rect rect, FF<T> f) {
		Map<P, T> map = new HashMap<>();
		editableData.stream().filter(cell -> rect.inside(getX(cell), getY(cell)))
				.forEach(cell -> map.put(new P(getX(cell), getY(cell)), cell));

		rect.foreach(xy -> {
			T cell = map.get(xy);
			T t = f.apply(xy.x, xy.y, cell);
			if (t != cell) {
				if (cell != null)
					editableData.remove(cell);
				if (t != null)
					editableData.add(t);
			}
		});

		invalidate();
		checkData();

	}

	public void convertSel(FF<T> f) {
		if (clipboardData == null)
			return;
		convertRect(clipboardData.rect, f);
		clipboardData.list = null;
	}

	public void undo() {
		dropSel();
		editableData.undo();
		invalidate();
		checkData();
	}

	public void redo() {
		dropSel();
		editableData.redo();
		invalidate();
		checkData();
	}
}
