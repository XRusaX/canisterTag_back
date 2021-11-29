package com.ma.hmcserver.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;

public interface FontAwesomeBundle extends ClientBundle {
	public static final FontAwesomeBundle INSTANCE = GWT.create(FontAwesomeBundle.class);

	@Source("/resources/css/fontawesome-all.min.css")
	@CssResource.NotStrict
	CssResource fontAwesome();

	@DataResource.DoNotEmbed
	@DataResource.MimeType("application/font-woff")
	@Source("/resources/fonts/fa-brands-400.woff")
	DataResource faBrands400woff();

	@DataResource.DoNotEmbed
	@DataResource.MimeType("application/font-woff")
	@Source("/resources/fonts/fa-regular-400.woff")
	DataResource faRegular400woff();

	@DataResource.DoNotEmbed
	@DataResource.MimeType("application/font-woff")
	@Source("/resources/fonts/fa-solid-900.woff")
	DataResource faSolid900woff();

}
