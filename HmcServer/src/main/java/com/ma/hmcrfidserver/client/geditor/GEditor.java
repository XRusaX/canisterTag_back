package com.ma.hmcrfidserver.client.geditor;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.ma.common.shared.canvas.PaintTarget;
import com.ma.common.shared.canvas.XY;
import com.ma.hmcrfidserver.client.EditableData2;

public abstract class GEditor<T> extends GEditorBase{
	private final int cellSize;

	private EditableData2<T> editableData;

	protected abstract boolean canEdit();

	protected abstract void drawCell(PaintTarget paintTarget, P pos, T cell);

	public GEditor(int cellSize, EditableData2<T> cells) {
		this.cellSize = cellSize;
		this.editableData = cells;
	}

	// public P getCellPos(P p) {
	// return getCellPos(p.x, p.y);
	// }

	public P getCellPos(double x, double y) {
		return new P(getCellX(x), getCellY(y));
	}

	public int getCellX(double x) {
		return (int) x / cellSize;
	}

	public int getCellY(double y) {
		return (int) y / cellSize;
	}

	private Rect drag;

	private ClipboardData clipboardData;

	private P pressed;

	@Override
	public void draw(PaintTarget paintTarget) {
		paintTarget.setColor("lightgray");

		Rect bounds = editableData.getBounds();

		for (int x = bounds.x0; x <= bounds.x1; x++)
			paintTarget.drawLine(x * cellSize, bounds.y0 * cellSize, x * cellSize, bounds.y1 * cellSize);

		for (int y = bounds.y0; y <= bounds.y1; y++)
			paintTarget.drawLine(bounds.x0 * cellSize, y * cellSize, bounds.x1 * cellSize, y * cellSize);

		int dx = drag == null ? 0 : drag.getWidth();
		int dy = drag == null ? 0 : drag.getHeigth();

		editableData.forEach((pos, cell) -> {
			if (clipboardData != null && clipboardData.map != null && clipboardData.rect.inside(pos.x - dx, pos.y - dy))
				return;

			drawCell(paintTarget, pos, cell);

		});

		if (clipboardData != null) {
			Rect copy = clipboardData.rect.normCopy();
			copy.move(dx, dy);
			if (clipboardData.map != null)
				clipboardData.map.forEach((pos, cell) -> {
					int x = pos.x + dx;
					int y = pos.y + dy;
					drawCell(paintTarget, new P(x, y), cell);
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
		paintTarget.drawRect(rect.x0 * cellSize, rect.y0 * cellSize, rect.getWidth() * cellSize,
				rect.getHeigth() * cellSize);
	}

	class ClipboardData {
		private Rect rect;
		public Map<P, T> map;

		public ClipboardData(Rect drag) {
			rect = drag.normCopy();
		}

		public void moveBy(int dx, int dy) {
			rect.move(dx, dy);
			Map<P, T> map2 = new HashMap<>();
			map.forEach((p, t) -> {
				P p2 = new P(p.x + dx, p.y + dy);
				map2.put(p2, t);
			});
			map = map2;
		}
	}

	private void copy() {
		GWT.log("copy");
		clipboardData.map = new HashMap<>();
		editableData.forEach((p, cell) -> {
			if (clipboardData.rect.inside(p))
				clipboardData.map.put(p, editableData.getCopy(cell));
		});
	}

	private void cut() {
		GWT.log("cut");
		copy();
		clipboardData.rect.foreach(p -> editableData.replace(p, null));
	}

	private void paste() {
		GWT.log("paste");
		clipboardData.rect.foreach(p -> editableData.replace(p, clipboardData.map.get(p)));
	}

	@Override
	public boolean onMousePressed(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		pressed = new P(getCellX(xy.x), getCellY(xy.y));

		if (clipboardData != null && !clipboardData.rect.inside(pressed)) {
			if (clipboardData.map != null)
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

		if (clipboardData != null && clipboardData.map == null && clipboardData.rect.inside(pressed)) {
			editableData.markUndo();
			if (control)
				copy();
			else
				cut();
		}

		if (drag == null)
			drag = new Rect(pressed.x, pressed.y);

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
		else if (clipboardData.map != null)
			clipboardData.moveBy(drag.getWidth(), drag.getHeigth());

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

	public void undo() {
		dropSel();
		editableData.undo();
		invalidate();
	}

	public void redo() {
		dropSel();
		editableData.redo();
		invalidate();
	}

	public Rect getSel() {
		return clipboardData == null ? null : clipboardData.rect;
	}
}
