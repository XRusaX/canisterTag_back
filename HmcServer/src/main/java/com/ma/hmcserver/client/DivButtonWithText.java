package com.ma.hmcserver.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class DivButtonWithText extends Widget implements HasClickHandlers {
	public DivButtonWithText(String text) {
		setElement(Document.get().createDivElement());

		Element textElement = Document.get().createSpanElement();
		textElement.setInnerText(text);

		getElement().appendChild(textElement);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
}