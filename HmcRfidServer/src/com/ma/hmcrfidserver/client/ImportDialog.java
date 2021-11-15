package com.ma.hmcrfidserver.client;

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
/*			if (!event.getResults().isEmpty())
				Window.alert(event.getResults());
			else
				Window.alert("Файл загружен");*/
		});
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("settings");
		formPanel.setWidget(fileUpload);

		setContent(formPanel);

		addButton("Ok", event -> {
			String fileName = fileUpload.getFilename();
			if (fileName.length() == 0)
				Window.alert("Файл не выбран");
			else {
				formPanel.submit();
			}
		}, "6em");

		addButton("Cancel", null, "6em");
	}

	protected void onComplete(SubmitCompleteEvent event) {};
}
