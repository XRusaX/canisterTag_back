package com.ma.hmcrfidserver.client;

import java.text.DecimalFormat;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProgressBar extends VerticalPanel {
	private final int HEIGHT = 150;
	private final int WIDTH = 20;
	private Canvas canvas;
	private Context2d context;
	final CssColor colorWhite = CssColor.make("white");
	final CssColor colorGreen = CssColor.make("#04AA6D");
	final CssColor colorYellow = CssColor.make("yellow");
	int percentage = 50;
	double remainLiters = 1.5;
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

		draw(canvas);
		disLevelContainer.add(percLabel);

		add(disLevelContainer);
	}

	private void draw(Canvas canvas) {
		context = canvas.getContext2d();

		context.beginPath();
		context.setFillStyle(colorWhite);
		context.fillRect(0, 0, WIDTH, HEIGHT);
		context.setFillStyle(PercentageToColor(percentage));
		context.fillRect(0, HEIGHT, WIDTH, -(HEIGHT * percentage / 100));
		context.closePath();
		percLabel.setText(NumberFormat.getFormat("#0.#").format(remainLiters) + "л");
	}

	public void setProgress(int value, int remainML) {
		percentage = value;
		remainLiters = remainML / 1000.0;
		draw(canvas);
	}

	static CssColor PercentageToColor(int percentage) {
		if (percentage > 100) {
			percentage = 100;
		} else if (percentage < 0) {
			percentage = 0;
		}
		int red = (255 * (100 - percentage)) / 100;
		int green = (255 * percentage) / 100;
		int blue = 85;
		return CssColor.make(red, green, blue);
	}

}