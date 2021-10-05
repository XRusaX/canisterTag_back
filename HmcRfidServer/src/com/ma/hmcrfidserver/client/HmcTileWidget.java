package com.ma.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ma.common.gwt.client.ui.panel.VertPanel;
import com.ma.common.shared.cd.CDObject;

public class HmcTileWidget extends Composite {
	public HmcTileWidget(CDObject t) {
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		VertPanel sp = new VertPanel();
		Label ph = new Label("нет картинки");
		ph.setSize("120px", "1px");
		sp.add(ph);

		panel.setStyleName("rounded-panel");

		String hmcType = t.get("hmcType");
		// Image hmcImg = new
		// Image("http://geonode.ciifen.org/static/documents/png-placeholder.png");
		Image hmcImg = new Image();

		hmcImg.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				GWT.log("Нет изображения по адресу: " + hmcImg.getUrl());
			}
		});

		hmcImg.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				hmcImg.setPixelSize(120, hmcImg.getHeight() * 120 / hmcImg.getWidth());
				// hmcImg.setPixelSize(120, hmcImg.getHeight() * 120 /
				// hmcImg.getWidth());
				// sp.setWidth(hmcImg.getWidth() + "px");
				// sp.setHeight(hmcImg.getHeight() + "px");
				// hmcImg.setVisible(true);

				sp.remove(ph);
				hmcImg.setVisible(true);
				GWT.log("***");
			}
		});

		ProgressBar disLevel = new ProgressBar();
		disLevel.setSpacing(10);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// hmcImg.setStyleName("hmc-image");

		sp.add(hmcImg);
		hmcImg.setVisible(false);

		hPanel.add(sp);

		hmcImg.setUrl("images/" + (hmcType == null ? "no_type" : hmcType) + ".png");

		hPanel.add(disLevel);

		panel.add(hPanel);
		InlineHTML label = new InlineHTML();
		label.setStyleName("status-label", true);
		label.setText(t.getDisplay("status"));
		if (label.getText() != null) {
			label.addStyleName(label.getText().equalsIgnoreCase("успешно") ? "success" : "warning");
			// panel.addStyleName("other");
		}
		panel.add(label);

		initWidget(panel);
		disLevel.setProgress(10);
		disLevel.setProgress(20);
		disLevel.setProgress(90);
	}
}
