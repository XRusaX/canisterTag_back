package com.ma.hmcrfidserver.client;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ma.common.gwtapp.client.ui.panel.VertPanel;
import com.ma.commonui.shared.cd.CDObject;
import com.ma.hmc.iface.report.shared.HmcReportStatus;

public class HmcTileWidget extends Composite implements ImageResources {
	public HmcTileWidget(CDObject t) {
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		VertPanel sp = new VertPanel();
		Label ph = new Label();
		ph.setSize("120px", "1px");
		sp.add(ph);

		panel.setStyleName("rounded-panel");

		String hmcType = t.get("hmcType");
		Image hmcImg;
		switch (hmcType) {
		case "HMC_1":
			hmcImg = new Image(IMAGE_RESOURCES.hmc1());
			break;
		case "HMC_2":
			hmcImg = new Image(IMAGE_RESOURCES.hmc2());
			break;
		case "HMC_3":
			hmcImg = new Image(IMAGE_RESOURCES.hmc3());
			break;
		case "HMC_4":
			hmcImg = new Image(IMAGE_RESOURCES.notFound());
			break;
		default:
			hmcImg = new Image(IMAGE_RESOURCES.noType());
			break;
		}

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

		hPanel.add(disLevel);

		panel.add(hPanel);
		InlineHTML label = new InlineHTML();
		label.setStyleName("status-label", true);

		HmcReportStatus status = t.getEnum(HmcReportStatus.class, "status");

		if (status != null) {
			label.setText(t.getDisplay("status"));
			if (label.getText() != null) {
				label.addStyleName(status == HmcReportStatus.SUCSESS ? "success" : "warning");
			}
		}
		panel.add(label);

		initWidget(panel);
		Integer canVol = t.getInt("canisterVolumeML");
		Integer remain = t.getInt("remainML");

		if (canVol != null && remain != null)
			disLevel.setProgress(remain * 100 / canVol);
		else
			disLevel.setProgress(0);
	}

	@Override
	public ImageResource hmc1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource hmc2() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource hmc3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource noType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource notFound() {
		// TODO Auto-generated method stub
		return null;
	}
}
