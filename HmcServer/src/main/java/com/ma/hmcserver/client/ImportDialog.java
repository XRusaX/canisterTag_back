package com.ma.hmcserver.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.ma.common.gwtapp.client.ui.dialog.DialogBoxExt;

public class ImportDialog extends DialogBoxExt {

	public ImportDialog(String title, String action) {
		super(title);

		FormPanel formPanel = new FormPanel();
		formPanel.setAction(action);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.addSubmitCompleteHandler(event -> {
			hide();

			onComplete(event);
		});
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("fileChooser");
		fileUpload.getElement().setAttribute("accept", ".png");
		formPanel.setWidget(fileUpload);
		formPanel.getElement().getStyle().setMarginBottom(20, Unit.PX);

		setContent(formPanel);

		addButton("ОТМЕНА", null, "6em", "button-cancel");

		addButton("ОК", event -> {
			String fileName = fileUpload.getFilename();
			if (fileName.length() == 0)
				Window.alert("Файл не выбран");
			else {
				formPanel.submit();
			}
		}, "6em", "button-ok");

		setStyleName("uploadDialog");
	}

	protected void onComplete(SubmitCompleteEvent event) {
	};
}
