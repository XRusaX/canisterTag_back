package com.ma.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.commondata.CommonDataService;
import com.ma.common.gwtapp.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwtapp.client.commondata.ContextMenuHandlerX;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.ui.ContextMenu;
import com.ma.common.gwtapp.client.ui.canvas.CanvasAdapter;
import com.ma.common.gwtapp.client.ui.panel.HorPanel;
import com.ma.common.gwtapp.client.ui.panel.LayoutHeaderPanel;
import com.ma.common.shared.canvas.PaintTarget;
import com.ma.common.shared.canvas.XY;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Room;
import com.ma.hmcdb.shared.RoomCell;
import com.ma.hmcdb.shared.RoomCell.WallType;
import com.ma.hmcrfidserver.client.geditor.GEditor;
import com.ma.hmcrfidserver.client.geditor.P;
import com.ma.hmcrfidserver.client.geditor.Rect;
import com.ma.hmcrfidserver.client.geditor.Utils;

public class MapPanel extends ResizeComposite {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	private Set<Long> errRooms = new HashSet<>();

	public void setPos(CDObject t, P pos) {
		t.set("x", pos.x);
		t.set("y", pos.y);
	}

	public P getPos(CDObject t) {
		Integer x = t.getInt("x");
		Integer y = t.getInt("y");

		if (x == null || y == null)
			return null;

		return new P(x, y);
	}

	private Timer checkTimer = new Timer() {

		@Override
		public void run() {
			checkFill();
		}
	};

	private final EditableData2<CDObject> data = new EditableData2<CDObject>() {
		@Override
		public CDObject getCopy(CDObject cell) {
			return new CDObject(cell);
		}

		@Override
		protected void onChanged() {
			checkTimer.schedule(1000);

		}
	};
	private Map<Long, CDObject> rooms;
	private CDObject room;
	private Long companyId;

	private boolean editMode;

	private final int CELL_SIZE = 16;

	private GEditor<CDObject> gEditor = new GEditor<CDObject>(CELL_SIZE, data) {

		protected String getColor(CDObject t) {
			// Long roomId = t.getLong("room");
			// if (roomId != null) {
			// if (editMode && rooms != null && rooms.get(roomId) != null)
			// return rooms.get(roomId).get("color");
			// else
			// return "yellow";
			// }

			String wt = t.get("wallType");
			if (wt != null) {
				switch (WallType.valueOf(wt)) {
				case DOOR:
					return "lightgray";
				case WINDOW:
					return "lightcyan";
				default:
					return "black";
				}
			}
			return "gray";
		}

		@Override
		protected boolean canEdit() {
			return editMode;
		}

		@Override
		protected void drawCell(PaintTarget paintTarget, P pos, CDObject cell) {
			paintTarget.setColor(getColor(cell));
			paintTarget.fillRect(pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
		}

		@Override
		public void draw(PaintTarget paintTarget) {
			if (rooms != null) {
				Set<P> textPositions = new HashSet<>();
				
				rooms.values().forEach(r -> {
					P textPos = getTextPos(r);
					if (textPos == null)
						return;

					while(textPositions.contains(textPos))
						textPos = new P(textPos.x, textPos.y+1);
					
					textPositions.add(textPos);
					
					
					paintTarget.setColor(errRooms.contains(r.getId()) ? "red" : "black");

					if (room != null && Objects.equals(room.getId(), r.getId()))
						paintTarget.setFontSize(CELL_SIZE * 3 / 2);
					else
						paintTarget.setFontSize(CELL_SIZE);

					String roomName = r.get("name");
					XY stringBounds = paintTarget.getStringBounds(roomName);
					paintTarget.drawString(roomName, textPos.x * CELL_SIZE - stringBounds.x / 2,
							textPos.y * CELL_SIZE - stringBounds.y / 2);
				});
			}
			super.draw(paintTarget);
		}

	};

	private P getTextPos(CDObject room) {
		Integer x = room.getInt("x");
		Integer y = room.getInt("y");

		if (x == null || y == null)
			return null;

		P cellPos = new P(x, y);

		P massCenter2 = getMassCenter(cellPos);
		if (massCenter2 != null)
			cellPos = new P(massCenter2.x, massCenter2.y + 1);

		return cellPos;
	}

	private Set<P> getArea(P p) {
		Map<P, CDObject> map = new HashMap<>();
		data.forEach((p1, cell) -> map.put(p1, cell));
		Rect bounds = data.getBounds();
		Rect bounds1 = new Rect(bounds.x0 + 1, bounds.y0 + 1, bounds.x1 - 1, bounds.y1 - 1);

		Set<P> setToFill = Utils.findFilledArea(p, bounds, p1 -> {
			CDObject cell = map.get(p1);
			return cell == null;
		});

		if (!setToFill.isEmpty() && setToFill.stream().allMatch(p1 -> bounds1.inside(p1)))
			return setToFill;
		return null;
	}

	private P getMassCenter(P p) {
		Set<P> area = getArea(p);
		if (area == null)
			return null;
		return P.massCenter(area);
	}

	private ToggleButton editButton = new ToggleButton("Редактировать", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			editMode = editButton.isDown();
			wallButton.setEnabled(editButton.isDown());
			clearButton.setEnabled(editButton.isDown());
			doorButton.setEnabled(editButton.isDown());
			windowButton.setEnabled(editButton.isDown());
			if (!editButton.isDown()) {
				gEditor.dropSel();

				if (Window.confirm("Сохранить?")) {
					List<CDObject> list = new ArrayList<>();
					data.forEach((p, t) -> {
						setPos(t, p);
						list.add(t);
					});
					hmcService.saveRoomCells(list, companyId, new AlertAsyncCallback<Void>(null));
				}
			}
			gEditor.invalidate();
		}
	});

	private Button wallButton = new Button("Стена", (ClickHandler) event -> {
		data.markUndo();
		gEditor.getSel().foreach(p -> data.replace(p, newCell(p, null)));
		gEditor.invalidate();
	});

	private Button doorButton = new Button("Дверь", (ClickHandler) event -> {
		data.markUndo();
		gEditor.getSel().foreach(p -> data.replace(p, newCell(p, WallType.DOOR)));
		gEditor.invalidate();
	});

	private Button windowButton = new Button("Окно", (ClickHandler) event -> {
		data.markUndo();
		gEditor.getSel().foreach(p -> data.replace(p, newCell(p, WallType.WINDOW)));
		gEditor.invalidate();
	});

	private Button clearButton = new Button("Очистить", (ClickHandler) event -> {
		data.markUndo();
		gEditor.getSel().foreach(p -> data.replace(p, null));
		gEditor.invalidate();
	});

	private Button undoButton = new Button("<<", (ClickHandler) event -> gEditor.undo());
	private Button redoButton = new Button(">>", (ClickHandler) event -> gEditor.redo());

	public MapPanel(PageEventBus eventBus) {

		wallButton.setEnabled(false);
		clearButton.setEnabled(false);

		CanvasAdapter canvasAdapter = new CanvasAdapter(gEditor);

		new ContextMenuHandlerX(canvasAdapter) {
			@Override
			protected void prepareContextMenu(ContextMenu menu, int x, int y, Runnable onPrepared) {
				menu.addItem("<<", () -> gEditor.undo());
				menu.addItem(">>", () -> gEditor.redo());
				P cellPos = gEditor.getCellPos(x, y);
				Set<P> area = getArea(cellPos);

				if (area != null && !area.isEmpty()) {
					menu.addSeparator();
					rooms.values().forEach(room -> {
						menu.addItem(room.get("name"), () -> {
							room.set("x", cellPos.x);
							room.set("y", cellPos.y);
							service.store(Room.class.getName(), room,
									new AlertAsyncCallback<>(v -> gEditor.invalidate()));
						});
					});
				}

				onPrepared.run();
			}
		};

		initWidget(new LayoutHeaderPanel(new HorPanel(editButton, wallButton, doorButton, windowButton, clearButton)
				.alignHor(HasAlignment.ALIGN_RIGHT).add(undoButton, redoButton), canvasAdapter));

		eventBus.registerListener(SelChangeEvent.class, se -> {

			Long cId = companyId;

			if (se.clazz == Room.class) {
				room = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next() : null;
				if (room != null) {
					cId = room.getLong("company");
				}
				gEditor.invalidate();
			}
			if (se.clazz == Company.class) {
				cId = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next().getId() : null;
			}

			if (!Objects.equals(cId, companyId)) {
				companyId = cId;

				if (companyId != null) {
					Filter filter = new Filter();
					filter.addEQ("company", "" + companyId);

					service.loadRange(Room.class.getName(), filter, null, new AlertAsyncCallback<>(list -> {
						rooms = new HashMap<>();
						list.range.stream().forEach(r -> rooms.put(r.getId(), r));
						service.loadRange(RoomCell.class.getName(), filter, null, new AlertAsyncCallback<>(list1 -> {
							data.clear();
							list1.range.forEach(o -> data.replace(getPos(o), o));
							data.clearUndoRedo();
							gEditor.invalidate();
						}));
					}));

				} else {
					data.clear();
					rooms = null;
					gEditor.invalidate();
				}

			}
		});

	}

	private void checkFill() {
		errRooms.clear();

		if (rooms != null) {

			Map<P, Long> map = new HashMap<>();

			rooms.values().forEach(r -> {
				P pos = getPos(r);
				if (pos == null)
					return;

				P massCenter = getMassCenter(pos);
				Long id = r.getId();
				if (massCenter == null)
					errRooms.add(id);
				else {
					if (map.containsKey(massCenter)) {
						errRooms.add(id);
						errRooms.add(map.get(massCenter));
					} else {
						map.put(massCenter, id);
					}
				}
			});
		}

		gEditor.invalidate();
	}

	private CDObject newCell(P pos, WallType wallType) {
		CDObject cell = new CDObject();
		cell.set("company", companyId);
		cell.set("x", pos.x);
		cell.set("y", pos.y);
		cell.set("wallType", wallType == null ? null : wallType.name());
		return cell;
	}
}
