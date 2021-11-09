package com.ma.hmcrfidserver.client;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
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

public class HmcTileWidget extends Composite{
	
	public HmcTileWidget(CDObject t) {
		VerticalPanel panel = new VerticalPanel();
		HorizontalPanel topHorPanel = new HorizontalPanel();
		Label tileName = new Label();
		tileName.setStyleName("my-title");
		topHorPanel.add(tileName);
		tileName.setTitle("с/н: " + t.get("serialNumber"));
		HorizontalPanel middleHorPanel = new HorizontalPanel();
		VertPanel sp = new VertPanel();
		Label placeHolder = new Label();
		placeHolder.setSize("120px", "1px");
		sp.add(placeHolder);
		panel.setStyleName("rounded-panel");
		String hmcType = t.get("hmcType");
		Image hmcImg;
		switch (hmcType) {
		case "HMC_1":
			hmcImg = new Image(ImageResources.IMAGE_RESOURCES.hmc1());
			tileName.setText("МГЦ 1");
			break;
		case "HMC_2":
			hmcImg = new Image(ImageResources.IMAGE_RESOURCES.hmc2());
			tileName.setText("МГЦ 2");
			break;
		case "HMC_3":
			hmcImg = new Image(ImageResources.IMAGE_RESOURCES.hmc3());
			tileName.setText("МГЦ 3");
			break;
		case "HMC_4":
			hmcImg = new Image(ImageResources.IMAGE_RESOURCES.notFound());
			tileName.setText("МГЦ 4");
			break;
		default:
			hmcImg = new Image(ImageResources.IMAGE_RESOURCES.noType());
			break;
		}

		hmcImg.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				hmcImg.setPixelSize(120, hmcImg.getHeight() * 120 / hmcImg.getWidth());
				sp.remove(placeHolder);
				hmcImg.setVisible(true);
			}
		});

		ProgressBar disLevel = new ProgressBar();
		disLevel.setSpacing(10);
		middleHorPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		sp.add(hmcImg);
		hmcImg.setVisible(false);

		middleHorPanel.add(sp);
		middleHorPanel.add(disLevel);

		panel.add(topHorPanel);
		panel.add(middleHorPanel);

		InlineHTML label = new InlineHTML();
		label.setStyleName("status-label", true);

		HmcReportStatus status = t.getEnum(HmcReportStatus.class, "status");

		if (status != null) {
			label.setText(t.getDisplay("status"));
			if (label.getText() != null) {
//				label.addStyleName(status == HmcReportStatus.SUCSESS ? "success" : "warning");

				switch (status) {
				case SUCSESS:
					label.addStyleName("success");
					break;
				case INTERRUPTED:
					label.addStyleName("warning");
					break;
				default:
					label.addStyleName("danger");
					break;
				}

			}
		}
		panel.add(label);

		initWidget(panel);
		Integer canVol = t.getInt("canisterVolumeML");
		Integer remain = t.getInt("remainML");

		if (canVol != null && remain != null)
			disLevel.setProgress(remain * 100 / canVol, remain);
		else
			disLevel.setProgress(0, 0);
	}

}
