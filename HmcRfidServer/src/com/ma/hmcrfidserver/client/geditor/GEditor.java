package com.ma.hmcrfidserver.client.geditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.ma.common.shared.canvas.Drawable;
import com.ma.common.shared.canvas.PaintTarget;
import com.ma.common.shared.canvas.XY;
import com.ma.hmcrfidserver.client.EditableData;

public abstract class GEditor<T> extends Drawable {
	private final int CELL_SIZE;

	private EditableData<T> editableData;

	protected abstract String getColor(T t);

	protected abstract boolean canEdit();

	protected abstract boolean isSelected(T t);

	public GEditor(int CELL_SIZE, EditableData<T> cells) {
		this.CELL_SIZE = CELL_SIZE;
		this.editableData = cells;
	}

	// public P getCellPos(P p) {
	// return getCellPos(p.x, p.y);
	// }

	public P getCellPos(double x, double y) {
		return new P(getCellX(x), getCellY(y));
	}

	public int getCellX(double x) {
		return (int) x / CELL_SIZE;
	}

	public int getCellY(double y) {
		return (int) y / CELL_SIZE;
	}

	private Rect drag;

	private ClipboardData clipboardData;

	@Override
	public void draw(PaintTarget paintTarget) {
		paintTarget.setColor("lightgray");

		Rect bounds = editableData.getBounds();

		for (int x = bounds.x0; x <= bounds.x1; x++)
			paintTarget.drawLine(x * CELL_SIZE, bounds.y0 * CELL_SIZE, x * CELL_SIZE, bounds.y1 * CELL_SIZE);

		for (int y = bounds.y0; y <= bounds.y1; y++)
			paintTarget.drawLine(bounds.x0 * CELL_SIZE, y * CELL_SIZE, bounds.x1 * CELL_SIZE, y * CELL_SIZE);

		int dx = drag == null ? 0 : drag.getWidth();
		int dy = drag == null ? 0 : drag.getHeigth();

		editableData.stream().forEach(cell -> {
			P pos = editableData.getPos(cell);

			if (clipboardData != null && clipboardData.list != null
					&& clipboardData.rect.inside(pos.x - dx, pos.y - dy))
				return;

			paintTarget.setColor(getColor(cell));
			paintTarget.fillRect(pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

			if (isSelected(cell)) {
				paintTarget.setColor("black");
				paintTarget.drawRect(pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		});

		if (clipboardData != null) {
			Rect copy = clipboardData.rect.normCopy();
			copy.move(dx, dy);
			if (clipboardData.list != null)
				clipboardData.list.stream().forEach(cell -> {
					paintTarget.setColor(getColor(cell));
					P pos = editableData.getPos(cell);
					int x = pos.x + dx;
					int y = pos.y + dy;
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

	private void drawRect(PaintTarget paintTarget, Rect rect, String color) {
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
			list.forEach(cell -> {
				P pos = editableData.getPos(cell);
				editableData.setPos(cell, new P(pos.x + dx, pos.y + dy));
			});
		}
	}

	private void copy() {
		GWT.log("copy");

		clipboardData.list = editableData.stream().filter(cell -> clipboardData.rect.inside(editableData.getPos(cell)))
				.map(cell -> editableData.getCopy(cell)).collect(Collectors.toList());
	}

	private void cut() {
		GWT.log("cut");
		copy();
		convertRect(clipboardData.rect, (p, t) -> null);
	}

	private void paste() {
		GWT.log("paste");

		Map<P, T> map = new HashMap<>();
		clipboardData.list.stream().forEach(cell -> map.put(editableData.getPos(cell), cell));

		convertRect(clipboardData.rect, (p, t) -> {
			T t2 = map.get(p);
			return t2;
		});

		editableData.checkData();
	}

	@Override
	public boolean onMousePressed(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		if (clipboardData != null && !clipboardData.rect.inside(getCellPos(xy.x, xy.y))) {
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

		if (clipboardData != null && clipboardData.list == null && clipboardData.rect.inside(getCellPos(xy.x, xy.y))) {
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

	public void dropSel() {
		clipboardData = null;
		drag = null;
		invalidate();
	}

	public interface FF<T> {
		T apply(P pos, T old);
	}

	public void convertRect(Rect rect, FF<T> f) {
		Map<P, T> map = new HashMap<>();
		editableData.stream().filter(cell -> rect.inside(editableData.getPos(cell)))
				.forEach(cell -> map.put(editableData.getPos(cell), cell));

		rect.foreach(xy -> {
			T cell = map.get(xy);
			T t = f.apply(xy, cell);
			if (t != cell) {
				if (cell != null)
					editableData.remove(cell);
				if (t != null)
					editableData.add(t);
			}
		});

		invalidate();
		editableData.checkData();

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
		editableData.checkData();
	}

	public void redo() {
		dropSel();
		editableData.redo();
		invalidate();
		editableData.checkData();
	}
}
