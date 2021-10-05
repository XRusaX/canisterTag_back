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
import com.ma.hmc.iface.report.shared.HmcReportStatus;

public class HmcTileWidget extends Composite {
	public HmcTileWidget(CDObject t) {
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		VertPanel sp = new VertPanel();
		Label ph = new Label();
		ph.setSize("120px", "1px");
		sp.add(ph);

		panel.setStyleName("rounded-panel");

		String hmcType = t.get("hmcType");
		Image hmcImg = new Image();

		hmcImg.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				hmcImg.setUrl("images/not_found.png");
			}
		});

		hmcImg.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				hmcImg.setPixelSize(120, hmcImg.getHeight() * 120 / hmcImg.getWidth());
				sp.remove(ph);
				hmcImg.setVisible(true);
			}
		});

		ProgressBar disLevel = new ProgressBar();
		disLevel.setSpacing(10);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		sp.add(hmcImg);
		hmcImg.setVisible(false);

		hPanel.add(sp);

		hmcImg.setUrl("images/" + (hmcType == null ? "no_type" : hmcType) + ".png");

		hPanel.add(disLevel);

		panel.add(hPanel);
		InlineHTML label = new InlineHTML();
		label.setStyleName("status-label", true);

		HmcReportStatus status = t.getEnum(HmcReportStatus.class, "status");

		label.setText(t.getDisplay("status"));
		if (label.getText() != null) {
			label.addStyleName(status == HmcReportStatus.SUCSESS ? "success" : "warning");
		}
		panel.add(label);

		initWidget(panel);
		disLevel.setProgress(10);
		disLevel.setProgress(20);
		disLevel.setProgress(90);
	}
}
