package com.ma.hmcrfidwriter.lib.sim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class LabelPad extends JDialog {

	public volatile Integer touchedId;

	public LabelPad(JFrame owner) {
		super(owner);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		JToggleButton button = new JToggleButton("");
		add(button);
		button.addActionListener(new ActionListener() {
			private Random random = new Random();

			@Override
			public void actionPerformed(ActionEvent e) {
				touchedId = button.isSelected() ? random.nextInt() : null;
			}
		});
	}

}
