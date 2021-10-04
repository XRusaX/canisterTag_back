package com.ma.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.ma.common.gwt.client.AlertAsyncCallback;
import com.ma.common.gwt.client.commondata.CommonDataService;
import com.ma.common.gwt.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwt.client.commondata.ContextMenuHandlerX;
import com.ma.common.gwt.client.commondata.PageEventBus;
import com.ma.common.gwt.client.commondata.SelChangeEvent;
import com.ma.common.gwt.client.ui.ContextMenu;
import com.ma.common.gwt.client.ui.canvas.CanvasAdapter;
import com.ma.common.gwt.client.ui.panel.HorPanel;
import com.ma.common.gwt.client.ui.panel.LayoutHeaderPanel;
import com.ma.common.shared.canvas.PaintTarget;
import com.ma.common.shared.cd.CDObject;
import com.ma.common.shared.commondata.Filter;
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
		return new P(t.getInt("x"), t.getInt("y"));
	}

	public boolean isWall(CDObject t) {
		return t.getLong("room") == null;
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
			Long roomId = t.getLong("room");
			if (roomId != null) {
				if (editMode && rooms != null && rooms.get(roomId) != null)
					return rooms.get(roomId).get("color");
				else
					return "yellow";
			}

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

		protected boolean isSelected(CDObject t) {
			Long roomId = t.getLong("room");
			return roomId != null && MapPanel.this.room != null && Objects.equals(MapPanel.this.room.getId(), roomId);
		}

		@Override
		protected void drawCell(PaintTarget paintTarget, P pos, CDObject cell) {
			paintTarget.setColor(getColor(cell));
			paintTarget.fillRect(pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

			if (isSelected(cell)) {
				paintTarget.setColor("black");
				paintTarget.drawRect(pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}

			if (errRooms.contains(cell.getLong("room"))) {
				paintTarget.setColor("black");
				paintTarget.drawRect(pos.x * CELL_SIZE + 4, pos.y * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
			}
		}

	};

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
		gEditor.getSel().foreach(p -> data.replace(p, newCell(p, null, null)));
		gEditor.invalidate();
	});

	private Button doorButton = new Button("Дверь", (ClickHandler) event -> {
		data.markUndo();
		gEditor.getSel().foreach(p -> data.replace(p, newCell(p, null, WallType.DOOR)));
		gEditor.invalidate();
	});

	private Button windowButton = new Button("Окно", (ClickHandler) event -> {
		data.markUndo();
		gEditor.getSel().foreach(p -> data.replace(p, newCell(p, null, WallType.WINDOW)));
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

				Map<P, CDObject> map = new HashMap<>();
				data.forEach((p, t) -> map.put(p, t));

				Rect bounds = data.getBounds();
				Set<P> setToFill = Utils.findFilledArea(gEditor.getCellPos(x, y), bounds, p -> {
					CDObject object = map.get(p);
					return object == null || object.getLong("room") != null;
				});

				Rect bounds1 = new Rect(bounds.x0 + 1, bounds.y0 + 1, bounds.x1 - 1, bounds.y1 - 1);

				if (!setToFill.isEmpty() && setToFill.stream().allMatch(p -> bounds1.inside(p))) {
					menu.addSeparator();
					rooms.values().forEach(room -> {
						menu.addItem(room.get("name"), () -> {
							data.markUndo();
							setToFill.forEach(pos -> {
								data.replace(pos, newCell(pos, room.getId(), null));
							});
							gEditor.invalidate();
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
					filter.stringMap.put("company", "" + companyId);

					service.loadRange(Room.class.getName(), filter, null,
							new AlertAsyncCallback<List<CDObject>>(list -> {
								rooms = new HashMap<>();
								list.stream().forEach(r -> rooms.put(r.getId(), r));
								service.loadRange(RoomCell.class.getName(), filter, null,
										new AlertAsyncCallback<List<CDObject>>(list1 -> {
											data.clear();
											list1.forEach(o -> data.replace(getPos(o), o));
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

		Set<Long> rooms = new HashSet<>();
		Set<P> outerArea = new HashSet<>();
		Set<P> allCells = new HashSet<>();

		Rect bounds = data.getBounds();
		Rect bounds1 = new Rect(bounds.x0 + 1, bounds.y0 + 1, bounds.x1 - 1, bounds.y1 - 1);
		bounds.foreach(p -> {
			if (allCells.contains(p))
				return;

			Set<P> set = Utils.findFilledArea(p, bounds, p1 -> {
				CDObject object = data._get(p1);
				return object == null || object.getLong("room") != null;
			});

			if (set.stream().anyMatch(p1 -> !bounds1.inside(p1))) {
				set.forEach(p1 -> {
					CDObject object = data._get(p1);
					if (object != null && object.getLong("room") != null)
						errRooms.add(object.getLong("room"));
				});
				outerArea.addAll(set);
			} else if (!set.isEmpty()) {
				Set<Long> roomsInArea = set.stream().map(p1 -> data._get(p1))
						.map(o -> o == null ? null : o.getLong("room")).collect(Collectors.toSet());
				if (roomsInArea.size() != 1)
					roomsInArea.stream().filter(r -> r != null).forEach(r -> errRooms.add(r));
				roomsInArea.stream().filter(r -> r != null).forEach(r -> {
					if (rooms.contains(r))
						errRooms.add(r);
					rooms.add(r);
				});
			}

			allCells.addAll(set);
		});

		gEditor.invalidate();
	}

	private CDObject newCell(P pos, Long room, WallType wallType) {
		CDObject cell = new CDObject();
		cell.set("company", companyId);
		cell.set("room", room);
		cell.set("x", pos.x);
		cell.set("y", pos.y);
		cell.set("wallType", wallType == null ? null : wallType.name());
		return cell;
	}
}
