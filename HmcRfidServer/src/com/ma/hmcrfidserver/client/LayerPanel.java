package com.ma.hmcrfidserver.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.GwtUtils;
import com.ma.common.gwtapp.client.commondata.CommonDataService;
import com.ma.common.gwtapp.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.ui.dialog.UploadDialog;
import com.ma.common.gwtapp.client.ui.panel.VertPanel;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Room;

public class LayerPanel extends ResizeComposite {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	private Map<Long, CDObject> rooms;
	private CDObject room;
	private Long companyId;
	private Image im = new Image();
	private LayoutPanel lp = new LayoutPanel();

	public LayerPanel(PageEventBus eventBus) {

		eventBus.registerListener(SelChangeEvent.class, se -> {

			Long cId = companyId;

			if (se.clazz == Room.class) {
				room = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next() : null;
				if (room != null) {
					cId = room.getLong("company");
				}
				// gEditor.invalidate();
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
						updateImage();
					}));

				} else {
					// data.clear();
					// rooms = null;
					// gEditor.invalidate();
				}

			}
		});

		GwtUtils.addContextMenu(this, (menu, x, y, onPrepared) -> {
			if (companyId != null) {
				menu.addItem("Загрузить план", () -> {
					new UploadDialog("Загрузить план", "api/uploadlayer?id=" + companyId, (e) -> updateImage())
							.center();
				});

				menu.addSeparator();

				rooms.values().forEach(room -> {
					menu.addItem(room.get("name"), () -> {
						room.set("x", x);
						room.set("y", y);
						service.store(Room.class.getName(), room, new AlertAsyncCallback<>(v -> {
							updateImage();
						}));
					});
				});

			}

			onPrepared.run();
		});

		updateImage();

		initWidget(lp);
	}

	private int version;

	private void updateImage() {
		lp.clear();
		lp.add(im);

		if (rooms != null) {
			rooms.values().forEach(room -> {
				Integer x = room.getInt("x");
				Integer y = room.getInt("y");
				if (x != null && y != null) {
					int w = 100;
					int h = 22 * 2;

					Widget roomWidget = createRoomWidget(room);
					roomWidget.setWidth(w + "px");
					roomWidget.setHeight(h + "px");

					lp.add(roomWidget);
					lp.setWidgetTopHeight(roomWidget, y - h / 2, Unit.PX, h, Unit.PX);
					lp.setWidgetLeftWidth(roomWidget, x - w / 2, Unit.PX, w, Unit.PX);

				}
			});
		}

		if (companyId != null)
			im.setUrl("api/uploadlayer?id=" + companyId + "&v" + version++);
		else
			im.setUrl("");

		im.setSize("1000px", "500px");

	}

	private Widget createRoomWidget(CDObject room) {
		Label label = new Label(room.get("name"));
		VertPanel vp = new VertPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.add1(label);
		vp.add1(new Label("10.02.2021"));
		vp.setBorderWidth(1);
		vp.getElement().getStyle().setBackgroundColor("cyan");
		return vp;
	}

}
