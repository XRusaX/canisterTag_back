package com.ma.hmcrfidserver.client.customwidgets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.ma.appcommon.shared.Filter;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.GwtUtils;
import com.ma.common.gwtapp.client.auth.Login;
import com.ma.common.gwtapp.client.commondata.CommonDataService;
import com.ma.common.gwtapp.client.commondata.CommonDataServiceAsync;
import com.ma.common.gwtapp.client.commondata.PageEventBus;
import com.ma.common.gwtapp.client.commondata.SelChangeEvent;
import com.ma.common.gwtapp.client.ui.canvas.ResizeFocusPanel;
import com.ma.common.gwtapp.client.ui.dialog.UploadDialog;
import com.ma.common.gwtapp.client.ui.panel.VertPanel;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmcdb.entity.Room;
import com.ma.hmcdb.entity.RoomLayer;

public class LayerMapPanel extends ResizeComposite {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);

	List<RoomWidget> rooms = new LinkedList<>();
	private final LayoutPanel workspace = new LayoutPanel();;
	private CDObject layer;
	private final Image img = new Image();
	private int height;
	private int width;
	private double multiplicator = 1;
	private Integer dragСursorX;
	private Integer dragСursorY;
	private PageEventBus eventBus;

	private ScrollPanel scrollPanel;

	public LayerMapPanel(PageEventBus eventBus, ScrollPanel scrollPanel) {
		this.eventBus = eventBus;
		this.scrollPanel = scrollPanel;

		eventBus.registerListener(SelChangeEvent.class, se -> {
			if (se.clazz == RoomLayer.class) {
				layer = se.selectedSet.size() == 1 ? se.selectedSet.iterator().next() : null;
				workspace.clear();
				multiplicator = 1;
				refreshMap();
			}
		});

		refreshMap();

		initWidget(workspace);
	}

	private MouseWheelHandler createZoomHandler() {
		return new MouseWheelHandler() {
			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				if (multiplicator >= 0.3) {
					multiplicator = multiplicator *(1+ (event.getDeltaY() / 75d));
					GWT.log("" + multiplicator);
					reArange();
				} else {
					multiplicator = 0.3;
				}
			}
		};
	}

	private void createMap() {

		img.addMouseWheelHandler(createZoomHandler());

		img.addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				event.preventDefault();
			}
		});

		img.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				setHeight(img.getHeight());
				setWidth(img.getWidth());
				workspace.setPixelSize(img.getWidth(), img.getHeight());
				reArange();
			}
		});

		img.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				dragСursorX = event.getScreenX();
				dragСursorY = event.getScreenY();
				
				Event.setCapture(scrollPanel.getElement());
//				GWT.log("Запомнил начальную позицию курсора х = " + event.getX() + " y = " + event.getY());
			}
		});

		scrollPanel.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				event.preventDefault();
				if (dragСursorX != null) {
					int dX = dragСursorX - event.getScreenX();
					int dY = dragСursorY - event.getScreenY();

					moveMap(dX, dY);

					dragСursorX = event.getScreenX();
					dragСursorY = event.getScreenY();

					GWT.log("Переместил изображение");
				}
			}
		}, MouseMoveEvent.getType());

		scrollPanel.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				dragСursorX = null;
				dragСursorY = null;
				
				Event.releaseCapture(scrollPanel.getElement());
			}
		}, MouseUpEvent.getType());

		img.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				dragСursorX = null;
				dragСursorY = null;
//				GWT.log("Занулил дельты");
			}
		});

		addContextMenu();
		loadMap();
	}

	private void moveMap(Integer dX, Integer dY) {
		scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() + dX);
		GWT.log("" + scrollPanel.getHorizontalScrollPosition());
		scrollPanel.setVerticalScrollPosition(scrollPanel.getVerticalScrollPosition() + dY);
		GWT.log("" + scrollPanel.getVerticalScrollPosition());
	}

	// Центральный обработчик состояния
	private void refreshMap() {
		workspace.clear();
		if (layer == null) {
			addSelectFloorLabel();
		} else if (layer.get("imageUrl") == null) {
			createUploadButton();
		} else {
			createMap();
		}
	}

	// Добавление надписи если никакой этаж не выбран
	private void addSelectFloorLabel() {
		Label label = new Label("Нажмите на нужный этаж для отображения");
		workspace.setSize(500 + "px", 700 + "px");
		workspace.add(label);
		workspace.setWidgetTopHeight(label, 2, Unit.PCT, 100, Unit.PCT);
		workspace.setWidgetLeftWidth(label, 25, Unit.PCT, 100, Unit.PCT);
	}

	// Создание кнопки загрузки если не найдено изображение для данного этажа
	private void createUploadButton() {
		img.setUrl("");
		Label label = new Label("Не найден сохраненный план данного этажа");
		Button button = new Button("Загрузить план этажа");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new UploadDialog("Загрузить план", "api/images", (e) -> {
					if (!e.getResults().isEmpty()) {
						layer.set("imageUrl", e.getResults());
						service.store(RoomLayer.class.getName(), layer, new AlertAsyncCallback<>(v -> {
							refreshMap();
						}));
					}
				}).center();
			}
		});

		button.setSize(150 + "px", 22 + "px");
		workspace.setSize(500 + "px", 700 + "px");
		workspace.add(label);
		workspace.add(button);
		workspace.setWidgetTopHeight(label, 2, Unit.PCT, 100, Unit.PCT);
		workspace.setWidgetLeftWidth(label, 25, Unit.PCT, 100, Unit.PCT);
		workspace.setWidgetTopHeight(button, 5, Unit.PCT, 100, Unit.PCT);
		workspace.setWidgetLeftWidth(button, 33, Unit.PCT, 100, Unit.PCT);
	}

	// Наполнение рабочего пространства
	private void loadMap() {
		rooms.clear();
		refreshImage();

		service.loadRange(Room.class.getName(), new Filter().addEQ("layer", "" + layer.getId()), null,
				new AlertAsyncCallback<>(list -> {
					list.range.forEach(room -> this.addRoom(room));
					reArange();
				}));
	}

	// Наполнение рабочего пространства
	private void loadRooms() {
		rooms.clear();

		service.loadRange(Room.class.getName(), new Filter().addEQ("layer", "" + layer.getId()), null,
				new AlertAsyncCallback<>(list -> {
					list.range.forEach(room -> this.addRoom(room));
					setRoomsPosition();
				}));
	}

	// Обновление плана этажа
	public void refreshImage() {

		img.setUrl(layer.get("imageUrl"));
		workspace.add(img);
	}

	// Масштабирование карты и вложенных объектов
	public void reArange() {
		if (getHeight() != 0) {
			setImageSize();
			setRoomsPosition();
		}
	}

	private void setRoomsPosition() {
		for (RoomWidget room : rooms) {
			setRoomPosition(room, multiplicator);
		}
	}

	// Масштабирование изображения плана этажа
	private void setImageSize() {
		double weidth = getWidth() * multiplicator;
		double height = getWidth() * multiplicator;
		img.setSize(weidth + "px", height + "px");
		// focusPanel.setSize(1200 + "px", 1200 + "px");
		this.setSize(weidth + "px", height + "px");
	}

	// Добавление виджета Room c использованием zoom
	public void addRoom(CDObject room) {
		RoomWidget roomWidget = new RoomWidget(room, createZoomHandler());

		rooms.add(roomWidget);
		workspace.add(roomWidget.getPanel());
	}

	// Позиционарование виджета Room на карте
	private void setRoomPosition(RoomWidget roomWidget, double axesMultiplicator) {
		final int w = 100;
		final int h = 22 * 2;

		roomWidget.getPanel().setWidth(w + "px");
		roomWidget.getPanel().setHeight(h + "px");

		int centerX = (int) Math.round(roomWidget.getRoom().getInt("x") * multiplicator);
		int centerY = (int) Math.round(roomWidget.getRoom().getInt("y") * multiplicator);

		GWT.log("" + centerX + "  " + centerY);

		workspace.setWidgetTopHeight(roomWidget.getPanel(), centerY - h / 2, Unit.PX, h, Unit.PX);
		workspace.setWidgetLeftWidth(roomWidget.getPanel(), centerX - w / 2, Unit.PX, w, Unit.PX);
	}

	// Добавление контекстного меню для загрузки нового плана и установки виджетов
	// Room
	private void addContextMenu() {
		GwtUtils.addContextMenu(workspace, (menu, x, y, onPrepared) -> {
			if (layer != null) {
				menu.addItem("Заменить план", () -> {
					new UploadDialog("Загрузить план", "api/images", (e) -> {
						if (!e.getResults().isEmpty()) {
							layer.set("imageUrl", e.getResults());
							service.store(RoomLayer.class.getName(), layer, new AlertAsyncCallback<>(v -> {
								refreshMap();
							}));
						}
					}).center();
				});

				menu.addSeparator();

				service.loadRange(Room.class.getName(), new Filter().addEQ("layer", "" + layer.getId()), null,
						new AlertAsyncCallback<>(list -> {
							list.range.stream().forEach(room -> {
								menu.addItem(room.get("name"), () -> {
									room.set("x", Math.round(x / multiplicator));
									room.set("y", Math.round(y / multiplicator));
									service.store(Room.class.getName(), room, new AlertAsyncCallback<>(v -> {
										deleteRoomWidgets();
										loadRooms();
									}));
								});
							});
							onPrepared.run();
						}));
			}
		});
	}

	public void deleteRoomWidgets() {
		for (RoomWidget roomWidget : rooms) {
			workspace.remove(roomWidget.getPanel());
		}
	}

	// Геттеры и сеттеры
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
