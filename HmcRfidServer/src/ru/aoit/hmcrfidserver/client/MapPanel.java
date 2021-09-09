package ru.aoit.hmcrfidserver.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ToggleButton;

import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Room;
import ru.aoit.hmcdb.shared.RoomCell;
import ru.nppcrts.common.gwt.client.AlertAsyncCallback;
import ru.nppcrts.common.gwt.client.commondata.CommonDataService;
import ru.nppcrts.common.gwt.client.commondata.CommonDataServiceAsync;
import ru.nppcrts.common.gwt.client.commondata.PageEventBus;
import ru.nppcrts.common.gwt.client.commondata.SelChangeEvent;
import ru.nppcrts.common.gwt.client.ui.canvas.CanvasAdapter;
import ru.nppcrts.common.gwt.client.ui.panel.HorPanel;
import ru.nppcrts.common.gwt.client.ui.panel.LayoutHeaderPanel;
import ru.nppcrts.common.shared.cd.CDObject;
import ru.nppcrts.common.shared.commondata.Filter;

public class MapPanel extends ResizeComposite {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	private final EditableData<CDObject> cells = new EditableData<CDObject>() {
		@Override
		protected void setXY(CDObject t, int x, int y) {
			t.set("x", x);
			t.set("y", y);
		}

		@Override
		protected int getY(CDObject t) {
			return t.getInt("y");
		}

		@Override
		protected int getX(CDObject t) {
			return t.getInt("x");
		}
	};
	private Map<Long, CDObject> rooms;
	private CDObject room;
	private Long companyId;

	private boolean editMode;

	private GEditor<CDObject> drawable = new GEditor<CDObject>(cells) {

		@Override
		protected int getX(CDObject t) {
			return t.getInt("x");
		}

		@Override
		protected int getY(CDObject t) {
			return t.getInt("y");
		}

		@Override
		protected String getColor(CDObject t) {
			Long roomId = t.getLong("room");
			if (roomId == null)
				return "gray";
			return rooms.get(roomId).get("color");
		}

		@Override
		protected boolean canEdit() {
			return editMode;
		}

		@Override
		protected boolean isSelected(CDObject t) {
			Long roomId = t.getLong("room");
			return roomId != null && MapPanel.this.room != null && Objects.equals(MapPanel.this.room.getId(), roomId);
		}

	};

	private ToggleButton editButton = new ToggleButton("Редактировать", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			editMode = editButton.isDown();
			wallButton.setEnabled(editButton.isDown());
			clearButton.setEnabled(editButton.isDown());
			if (!editButton.isDown()) {
				drawable.dropSel();

				if (Window.confirm("Сохранить?")) {
					List<CDObject> list = cells.stream().collect(Collectors.toList());
					hmcService.saveAll(list, companyId, new AlertAsyncCallback<Void>(null));
				}

			}
		}
	});

	private Button wallButton = new Button("Стены", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			cells.markUndo();
			drawable.convertSel((x, y, t) -> {
				CDObject cell = new CDObject();
				cell.set("company", companyId);
				cell.set("room", (Long) null);
				cell.set("x", x);
				cell.set("y", y);
				return cell;
			});
		}
	});

	private Button clearButton = new Button("Очистить", (ClickHandler) event -> {
		cells.markUndo();
		drawable.convertSel((x, y, t) -> null);
	});

	private Button undoButton = new Button("<<", (ClickHandler) event -> drawable.undo());
	private Button redoButton = new Button(">>", (ClickHandler) event -> drawable.redo());

	public MapPanel(PageEventBus eventBus) {

		wallButton.setEnabled(false);
		clearButton.setEnabled(false);

		initWidget(new LayoutHeaderPanel(new HorPanel(editButton, wallButton, clearButton)
				.alignHor(HasAlignment.ALIGN_RIGHT).add(undoButton, redoButton), new CanvasAdapter(drawable)));

		eventBus.registerListener(SelChangeEvent.class, se -> {

			Long cId = companyId;

			if (se.clazz == Room.class) {
				room = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next() : null;
				if (room != null) {
					cId = room.getLong("company");
				}
				drawable.invalidate();
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
											cells.setData(list1);
											drawable.invalidate();
										}));
							}));

				} else {
					cells.clear();
					rooms = null;
					drawable.invalidate();
				}

			}
		});

	}
}
