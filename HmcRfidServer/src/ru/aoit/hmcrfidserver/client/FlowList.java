package ru.aoit.hmcrfidserver.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import ru.nppcrts.common.gwt.client.ui.ContextMenu;
import ru.nppcrts.common.gwt.client.ui.panel.VertPanel;

public abstract class FlowList<T> extends Composite {
	private FlowPanel panel = new FlowPanel();

	private T sel;

	private Map<T, FlowListItem> map = new HashMap<>();

	public FlowList() {
		FocusPanel fp = new FocusPanel(new ScrollPanel(panel));

		new ContextMenuHandlerX<T>(fp) {
			@Override
			protected void prepareContextMenu(ContextMenu menu, Runnable onPrepared) {
				FlowList.this.prepareContextMenu(menu, null, onPrepared);
			}
		};

		initWidget(fp);
	}

	public void add(T t) {
		FlowListItem item = new FlowListItem(t, createWidget(t));
		map.put(t, item);
		panel.add(item);

		if (sel != null && t.equals(t))
			setSel(t);
	}

	public void setData(Collection<T> data) {
		panel.clear();
		map.clear();
		data.forEach(t -> add(t));
	}

	protected abstract Widget createWidget(T t);

	protected void onSelChanged(@SuppressWarnings("unused") T t) {
	}

	protected void onDoubleClick(@SuppressWarnings("unused") T t) {
	}

	@SuppressWarnings("unused")
	protected void prepareContextMenu(ContextMenu menu, T t, Runnable onPrepared) {
	}

	public void setSel(T t) {
		if (sel != null) {
			FlowListItem i = map.get(sel);
			if (i != null)
				i.setSelected(false);
		}

		sel = t;
		FlowListItem i = map.get(t);
		if (i != null)
			i.setSelected(true);
		onSelChanged(t);
	}

	private class FlowListItem extends Composite {

		private VertPanel vertPanel2;
		private T t;

		public FlowListItem(T t, Widget w) {
			this.t = t;
			w.getElement().getStyle().setMargin(0.5, Unit.EM);

			vertPanel2 = new VertPanel(w);
			vertPanel2.getElement().getStyle().setFloat(Style.Float.LEFT);
			vertPanel2.getElement().getStyle().setMargin(1, Unit.EM);

			FocusPanel focusPanel = new FocusPanel(vertPanel2);

			focusPanel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					event.preventDefault();
					event.stopPropagation();
					setSel(t);
				}
			});

			focusPanel.addDoubleClickHandler(new DoubleClickHandler() {

				@Override
				public void onDoubleClick(DoubleClickEvent event) {
					event.preventDefault();
					event.stopPropagation();
					FlowList.this.onDoubleClick(t);
				}
			});

			new ContextMenuHandlerX<T>(focusPanel) {
				@Override
				protected void prepareContextMenu(ContextMenu menu, Runnable onPrepared) {
					setSel(t);
					FlowList.this.prepareContextMenu(menu, t, onPrepared);
				}
			};

			initWidget(focusPanel);
		}

		private void setSelected(boolean selected) {
			vertPanel2.getElement().getStyle().setBackgroundColor(selected ? "LightBlue" : "White");
		}

	}

}
