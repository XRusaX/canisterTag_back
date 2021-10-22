package com.ma.hmcrfidserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {

	ImageResources IMAGE_RESOURCES = (ImageResources) GWT.create(ImageResources.class);

	@Source("resources/HMC_1.png")
	ImageResource hmc1();

	@Source("resources/HMC_2.png")
	ImageResource hmc2();

	@Source("resources/HMC_3.png")
	ImageResource hmc3();

	@Source("resources/no_type.png")
	ImageResource noType();

	@Source("resources/not_found.png")
	ImageResource notFound();
}
