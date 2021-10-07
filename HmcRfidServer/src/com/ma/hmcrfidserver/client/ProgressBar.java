package com.ma.hmcrfidserver.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProgressBar extends VerticalPanel {
	private final int HEIGHT = 150;
	private final int WIDTH = 20;
	private Canvas canvas;
	private Context2d context;
	final CssColor colorWhite = CssColor.make("white");
	final CssColor colorGreen = CssColor.make("#04AA6D");
	final CssColor colorBlue = CssColor.make("blue");
	int percentage = 47;
	private Label percLabel = new Label();
	// private NumberFormat percentFormat = NumberFormat.getFormat("#00");

	public ProgressBar() {

		canvas = Canvas.createIfSupported();

		VerticalPanel disLevelContainer = new VerticalPanel();
		disLevelContainer.add(canvas);
		canvas.setWidth("100");
		canvas.setHeight("100%");
		canvas.setCoordinateSpaceWidth(WIDTH);
		canvas.setCoordinateSpaceHeight(HEIGHT);

		draw(canvas, percentage);
		disLevelContainer.add(percLabel);

		add(disLevelContainer);
	}

	private void draw(Canvas canvas, int percentage) {
		context = canvas.getContext2d();

		context.beginPath();
		context.setFillStyle(colorWhite);
		context.fillRect(0, 0, WIDTH, HEIGHT);
		context.setFillStyle(colorGreen);
		context.fillRect(0, HEIGHT, WIDTH, -(HEIGHT * 100 / percentage));
		context.closePath();
		percLabel.setText(percentage + "%");
	}

	public void setProgress(int value) {
		percentage = value;
		draw(canvas, percentage);
	}

}