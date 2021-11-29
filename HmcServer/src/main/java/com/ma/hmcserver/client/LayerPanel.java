package com.ma.hmcserver.client;

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
import com.ma.hmcdb.entity.Room;
import com.ma.hmcdb.entity.RoomLayer;

public class LayerPanel extends ResizeComposite {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);
	//private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	// private Map<Long, CDObject> rooms;
	private CDObject room;
	private CDObject layer;
	private Image im = new Image();
	private LayoutPanel lp = new LayoutPanel();

	public LayerPanel(PageEventBus eventBus) {

		eventBus.registerListener(SelChangeEvent.class, se -> {

			if (se.clazz == Room.class) {
				room = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next() : null;
			}
			if (se.clazz == RoomLayer.class) {
				layer = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next() : null;
				updateImage();
			}
		});

		GwtUtils.addContextMenu(this, (menu, x, y, onPrepared) -> {
			if (layer != null) {
				menu.addItem("Загрузить план", () -> {
					new UploadDialog("Загрузить план", "api/images", (e) -> {

						if (!e.getResults().isEmpty()) {
							layer.set("imageUrl", e.getResults());
							service.store(RoomLayer.class.getName(), layer,
									new AlertAsyncCallback<>(v -> updateImage()));
						}

					}).center();
				});

				menu.addSeparator();

				service.loadRange(Room.class.getName(), new Filter().addEQ("layer", "" + layer.getId()), null,
						new AlertAsyncCallback<>(list -> {
							list.range.stream().forEach(room -> {
								menu.addItem(room.get("name"), () -> {
									room.set("x", x);
									room.set("y", y);
									service.store(Room.class.getName(), room, new AlertAsyncCallback<>(v -> {
										updateImage();
									}));
								});
							});
							onPrepared.run();
						}));
			}

		});

		updateImage();

		initWidget(lp);
	}

	private void updateImage() {

		lp.clear();
		lp.add(im);

		if (layer != null)
			im.setUrl(layer.get("imageUrl"));
		else
			im.setUrl("");

		im.setSize("1000px", "500px");

		if (layer != null)
			service.loadRange(Room.class.getName(), new Filter().addEQ("layer", "" + layer.getId()), null,
					new AlertAsyncCallback<>(list -> list.range.forEach(room -> addRoom(room))));
	}

	private void addRoom(CDObject room) {
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
