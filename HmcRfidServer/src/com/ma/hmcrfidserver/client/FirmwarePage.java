package com.ma.hmcrfidserver.client;

import com.google.gwt.user.client.ui.Composite;
import com.ma.common.gwt.client.ui.UploadForm;

public class FirmwarePage extends Composite {

	public FirmwarePage() {
		UploadForm uploadForm = new UploadForm("api/firmware", true);
		initWidget(uploadForm);
	}

}
