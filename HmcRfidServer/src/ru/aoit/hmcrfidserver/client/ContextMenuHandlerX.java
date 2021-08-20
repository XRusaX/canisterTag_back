package ru.aoit.hmcrfidserver.client;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import ru.nppcrts.common.gwt.client.ui.ContextMenu;

public abstract class ContextMenuHandlerX<T> {
	public ContextMenuHandlerX(Widget w) {
		w.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();

				ContextMenu menu = new ContextMenu();

				int x = event.getNativeEvent().getClientX();
				int y = event.getNativeEvent().getClientY();

				prepareContextMenu(menu, new Runnable() {
					@Override
					public void run() {
						if (menu.getSize() == 0)
							return;

						menu.show(x, y);
						int offsetHeight = menu.getOffsetHeight();
						if (Window.getClientHeight() - y < offsetHeight)
							menu.show(x, y - offsetHeight);
					}
				});

			}
		}, ContextMenuEvent.getType());
	}

	@SuppressWarnings("unused")
	protected abstract void prepareContextMenu(ContextMenu menu, Runnable onPrepared);

}