package ru.aoit.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gwt.user.client.Window;

import ru.nppcrts.common.shared.Pair;
import ru.nppcrts.common.shared.canvas.Drawable;
import ru.nppcrts.common.shared.canvas.PaintTarget;
import ru.nppcrts.common.shared.canvas.XY;

public abstract class GEditor<T> extends Drawable {
	private static final int CELL_SIZE = 16;

	private EditableData<T> cells;

	protected abstract int getX(T t);

	protected abstract int getY(T t);

	protected abstract String getColor(T t);

	protected abstract boolean canEdit();

	protected abstract boolean isSelected(T t);

	public GEditor(EditableData<T> cells) {
		this.cells = cells;
	}

	private static int getCellX(double x) {
		return (int) x / CELL_SIZE;
	}

	private static int getCellY(double y) {
		return (int) y / CELL_SIZE;
	}

	static class P {
		public int x;
		public int y;

		public P(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			return x * 10000 + y;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			P other = (P) obj;
			return x == other.x && y == other.y;
		}
	}

	static class Rect {
		public int x0;
		public int y0;
		public int x1;
		public int y1;

		public Rect(P xy) {
			this(xy.x, xy.y);
		}

		public Rect(int x, int y) {
			this(x, y, x, y);
		}

		public Rect(int x0, int y0, int x1, int y1) {
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
		}

		public Rect(Rect sel) {
			this(sel.x0, sel.x1, sel.y0, sel.y1);
		}

		public boolean inside(int x, int y) {
			int xx0 = x0;
			int xx1 = x1;
			int yy0 = y0;
			int yy1 = y1;

			if (x0 > x1) {
				xx0 = x1 + 1;
				xx1 = x0 + 1;
			}

			if (y0 > y1) {
				yy0 = y1 + 1;
				yy1 = y0 + 1;
			}

			return x >= xx0 && x < xx1 && y >= yy0 && y < yy1;
		}

		public int getWidth() {
			return x1 - x0;
		}

		public int getHeigth() {
			return y1 - y0;
		}

		public List<P> getLine() {
			List<P> res = new ArrayList<>();
			if (Math.abs(getWidth()) > Math.abs(getHeigth())) {
				for (int x = x0; x != x1; x += x1 > x0 ? 1 : -1)
					res.add(new P(x, y0));
			} else {
				for (int y = y0; y != y1; y += y1 > y0 ? 1 : -1)
					res.add(new P(x0, y));
			}
			return res;
		}

		public void foreach(Consumer<P> consumer) {
			for (int x = x0; x != x1; x += x1 > x0 ? 1 : -1)
				for (int y = y0; y != y1; y += y1 > y0 ? 1 : -1)
					consumer.accept(new P(x, y));
		}
	}

	private Rect sel;

	private Rect drag;

	private ClipboardData<T> clipboardData;

	@Override
	public void draw(PaintTarget paintTarget) {
		paintTarget.setColor("lightgray");

		int maxx = cells.stream().mapToInt(c -> getX(c)).max().orElse(0) + 5;
		int maxy = cells.stream().mapToInt(c -> getY(c)).max().orElse(0) + 5;

		for (int x = 0; x <= maxx + 1; x++)
			paintTarget.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, (maxy + 1) * CELL_SIZE);

		for (int y = 0; y <= maxy + 1; y++)
			paintTarget.drawLine(0, y * CELL_SIZE, (maxx + 1) * CELL_SIZE, y * CELL_SIZE);

		cells.stream().forEach(cell -> {
			int x = getX(cell);
			int y = getY(cell);

			if (drag != null && sel.inside(x - drag.getWidth(), y - drag.getHeigth()))
				return;

			if (drag != null && sel.inside(x, y)) {
				return;
			}

			paintTarget.setColor(getColor(cell));
			paintTarget.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

			if (isSelected(cell)) {
				paintTarget.setColor("black");
				paintTarget.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		});

		cells.stream().forEach(cell -> {
			int x = getX(cell);
			int y = getY(cell);

			if (drag != null && sel.inside(x, y)) {
				x += drag.getWidth();
				y += drag.getHeigth();
				paintTarget.setColor(getColor(cell));
				paintTarget.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		});

		if (sel != null) {
			paintTarget.setColor("magenta");

			int x = sel.getWidth() > 0 ? sel.x0 : sel.x1 + 1;
			int y = sel.getHeigth() > 0 ? sel.y0 : sel.y1 + 1;

			if (drag != null) {
				x += drag.getWidth();
				y += drag.getHeigth();
			}

			paintTarget.drawRect(x * CELL_SIZE, y * CELL_SIZE, Math.abs(sel.getWidth()) * CELL_SIZE,
					Math.abs(sel.getHeigth()) * CELL_SIZE);
		}

	}

	static class ClipboardData<T> {
		public Rect rect;
		public List<T> list;
	}

	private void copy() {
		if (sel == null)
			return;

		ClipboardData<T> data = new ClipboardData<>();
		data.list = cells.stream().filter(cell -> {
			int x = getX(cell);
			int y = getY(cell);
			return sel.inside(x, y);
		}).collect(Collectors.toList());
		data.rect = new Rect(sel);

		clipboardData = data;
	}

	private void cut() {
		copy();
		convertSel((x, y, t) -> null);
	}

	private void paste(int x, int y) {
		
	}
	
	@Override
	public boolean onMousePressed(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		if (sel != null && sel.inside(getCellX(xy.x), getCellY(xy.y))) {
			drag = new Rect(getCellX(xy.x), getCellY(xy.y));
		} else {
			sel = new Rect(getCellX(xy.x), getCellY(xy.y));
		}
		invalidate();
		return true;
	}

	@Override
	public boolean onMouseDragged(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		if (drag != null) {
			drag.x1 = getCellX(xy.x);
			drag.y1 = getCellY(xy.y);
		} else if (sel != null) {
			sel.x1 = getCellX(xy.x);
			sel.y1 = getCellY(xy.y);
		}
		invalidate();
		return true;
	}

	@Override
	public boolean onMouseReleased(XY xy, boolean shift, boolean control) {
		if (!canEdit())
			return false;

		if (drag != null) {
			cells.markUndo();
			List<T> cellsToMove = cells.stream().filter(cell -> {
				int x = getX(cell);
				int y = getY(cell);
				return sel.inside(x, y);
			}).collect(Collectors.toList());

			// cells.removeAll(cellsToMove);

			removeIf(cell -> {
				if (cellsToMove.contains(cell))
					return false;
				int x = getX(cell) - drag.getWidth();
				int y = getY(cell) - drag.getHeigth();
				return sel.inside(x, y);
			});

			cellsToMove.forEach(cell -> {
				int x = getX(cell);
				int y = getY(cell);

				cells.moveTo(cell, x + drag.getWidth(), y + drag.getHeigth());
			});

			// cells.addAll(cellsToMove);

			checkData();

			sel = null;
			drag = null;
		} else if (sel != null) {

		}

		invalidate();
		return true;
	}

	// static class P {
	// public P(int x, int y) {
	//
	// }
	// }

	private void checkData() {
		Set<Pair<Integer, Integer>> set = new HashSet<>();
		cells.stream().forEach(t -> {
			Pair<Integer, Integer> pair = Pair.create(getX(t), getY(t));
			if (set.contains(pair))
				Window.alert("Data integrity error!");
			set.add(pair);
		});
	}

	public void dropSel() {
		sel = null;
		drag = null;
		invalidate();
	}

	public interface FF<T> {
		T apply(int x, int y, T old);
	}

	public void convertSel(FF<T> f) {
		if (sel == null)
			return;

		Map<P, T> map = new HashMap<>();
		cells.stream().filter(cell -> sel.inside(getX(cell), getY(cell)))
				.forEach(cell -> map.put(new P(getX(cell), getY(cell)), cell));

		sel.foreach(xy -> {
			T cell = map.get(xy);
			T t = f.apply(xy.x, xy.y, cell);

			// GWT.log(cell + " " + t);

			if (t != cell) {
				if (cell != null)
					cells.remove(cell);
				if (t != null)
					cells.add(t);
			}
		});

		invalidate();
		checkData();
	}

	private void removeIf(Predicate<T> p) {
		cells.stream().filter(cell -> p.test(cell)).collect(Collectors.toList()).forEach(t -> cells.remove(t));
	}

	public void undo() {
		cells.undo();
		invalidate();
		checkData();
	}

	public void redo() {
		cells.redo();
		invalidate();
		checkData();
	}
}