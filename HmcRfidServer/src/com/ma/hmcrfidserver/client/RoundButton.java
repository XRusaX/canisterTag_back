package com.ma.hmcrfidserver.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class RoundButton extends Composite implements HasClickHandlers,ImageResources {
	private final Label textHolder = new Label("helasdfasdf");
	private final Image leftSide = new Image(IMAGE_RESOURCES.hmc1());
	private final Image rightSide = new Image(IMAGE_RESOURCES.hmc1());
	private final HorizontalPanel contentTable;

	public RoundButton() {
		contentTable = new HorizontalPanel();
		initWidget(contentTable);
		contentTable.add(leftSide);
		contentTable.add(textHolder);
		contentTable.add(rightSide);
	}

	public void setText(String text) {
		textHolder.setText(text);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
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