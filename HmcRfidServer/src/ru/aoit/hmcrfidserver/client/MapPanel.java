package ru.aoit.hmcrfidserver.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.ResizeComposite;

import ru.aoit.hmcdb.shared.Company;
import ru.aoit.hmcdb.shared.Room;
import ru.aoit.hmcdb.shared.RoomCell;
import ru.nppcrts.common.gwt.client.AlertAsyncCallback;
import ru.nppcrts.common.gwt.client.commondata.CommonDataService;
import ru.nppcrts.common.gwt.client.commondata.CommonDataServiceAsync;
import ru.nppcrts.common.gwt.client.commondata.PageEventBus;
import ru.nppcrts.common.gwt.client.commondata.SelChangeEvent;
import ru.nppcrts.common.gwt.client.ui.canvas.CanvasAdapter;
import ru.nppcrts.common.shared.canvas.Drawable;
import ru.nppcrts.common.shared.canvas.PaintTarget;
import ru.nppcrts.common.shared.cd.CDObject;
import ru.nppcrts.common.shared.commondata.Filter;

public class MapPanel extends ResizeComposite {
	private static final int CELL_SIZE = 8;

	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);

	private List<CDObject> cells;
	private Map<Long, CDObject> rooms;
	private CDObject room;
	private Long companyId;

	private Drawable drawable = new Drawable() {
		@Override
		public void draw(PaintTarget paintTarget) {

			if (cells != null && rooms != null) {
				paintTarget.setColor("lightgray");

				int maxx = cells.stream().mapToInt(c -> c.getInt("x")).max().orElse(0) + 5;
				int maxy = cells.stream().mapToInt(c -> c.getInt("y")).max().orElse(0) + 5;

				for (int x = 0; x <= maxx + 1; x++)
					paintTarget.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, (maxy + 1) * CELL_SIZE);

				for (int y = 0; y <= maxy + 1; y++)
					paintTarget.drawLine(0, y * CELL_SIZE, (maxx + 1) * CELL_SIZE, y * CELL_SIZE);

				cells.forEach(cell -> {
					int x = cell.getInt("x");
					int y = cell.getInt("y");

					Long roomId = cell.getLong("room");
					if (roomId == null) {
						paintTarget.setColor("green");
					} else {
						paintTarget.setColor(rooms.get(roomId).get("color"));
					}
					paintTarget.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

					if (roomId != null && Objects.equals(MapPanel.this.room.getId(), roomId)) {
						paintTarget.setColor("black");
						paintTarget.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					}
				});
			}

		}
	};

	public MapPanel(PageEventBus eventBus) {
		initWidget(new CanvasAdapter(drawable));

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
											cells = list1;
											drawable.invalidate();
										}));
							}));

				} else {
					cells = null;
					rooms = null;
					drawable.invalidate();
				}

			}
		});

	}
}
