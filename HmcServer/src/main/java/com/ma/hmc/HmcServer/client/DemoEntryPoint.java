package com.ma.hmc.HmcServer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DemoEntryPoint implements EntryPoint {
	private final DemoServiceAsync greetingService = GWT.create(DemoService.class);

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new Button("666", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				greetingService.hello("xxx", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						Window.alert(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});
			}
		}));
	}
}
