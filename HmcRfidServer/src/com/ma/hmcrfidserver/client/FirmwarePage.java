package com.ma.hmcrfidserver.client;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.ma.common.gwt.client.AlertAsyncCallback;
import com.ma.common.gwt.client.ui.Gap;
import com.ma.common.gwt.client.ui.UploadForm;
import com.ma.common.gwt.client.ui.panel.HorPanel;
import com.ma.common.gwt.client.ui.panel.PropertiesPanel;
import com.ma.common.gwt.client.ui.panel.VertPanel;
import com.ma.hmcdb.shared.Hmc.HmcType;

public class FirmwarePage extends Composite {
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	private UploadForm uploadForm = new UploadForm("api/firmware", true) {
		@Override
		public void onComplete(SubmitCompleteEvent event) {
			updateList();
		}
	};
	private ListBox listBox = new ListBox();
	private PropertiesPanel propertiesPanel = new PropertiesPanel("---");

	public FirmwarePage() {
		Arrays.stream(HmcType.values()).forEach(l -> listBox.addItem(l.name(), l.name()));
		listBox.addChangeHandler(event -> setAction());
		setAction();
		updateList();
		initWidget(new VertPanel().setSp(8).add(propertiesPanel, new HTML("<b>Загрузка прошивки</b>"),
				new HorPanel(new Label("Тип устройства"), new Gap(10, 1)).add1(listBox), uploadForm));
	}

	private void setAction() {
		String line = listBox.getSelectedValue();
		uploadForm.setAction(getUrl(line));
	}

	private void updateList() {
		propertiesPanel.removeAllRows();

		hmcService.getFirmwareList(new AlertAsyncCallback<>(map -> {
			map.forEach((line, value) -> {
				propertiesPanel.add(line, new Anchor(value, getUrl(line)));
			});
		}));

	}

	private static String getUrl(String line) {
		return "api/firmware?line=" + line;
	}

}
