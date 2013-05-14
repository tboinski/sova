package org.pg.eti.kask.sova.demo;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;

import org.pg.eti.kask.sova.visualization.annotation.URIInfoComponent;

public class URITextField extends JTextField implements URIInfoComponent {

	{	/* blok inicjujacy */
		this.setSize(new Dimension(500,20));
		this.setMaximumSize(new Dimension(500,20));
		this.setMinimumSize(new Dimension(500,20));
		this.setColumns(50);
		this.setEditable(false);
		this.setBackground(Color.WHITE);
	}
	@Override
	public void setURIInfo(String uri) {
		this.setText(uri);

	}

}
