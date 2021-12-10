package com.ma.hmcapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface CSSBundle extends ClientBundle{
	public static final CSSBundle css = (CSSBundle) GWT.create(CSSBundle.class);
	
		@Source("/resources/css/example.css")
		@CssResource.NotStrict
		CssResource myCss();

}
