/*
 *
 * Copyright (c) 2010 Kunowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.ont.pluginSova;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.pg.eti.kask.sova.visualization.annotation.AnnotationComponent;
import prefuse.util.FontLib;

public class AnnotationPanel extends JPanel implements AnnotationComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5829137853147447938L;
	private static int PANEL_WIDTH = 180;
	private JTextArea name = null;
	private JTextArea label = null;
	private JTextArea comment = null;
	public AnnotationPanel(){
		initPanel();
	}
	
	private void initPanel(){
		setSize(PANEL_WIDTH,300);
		// pole nazwy
		name = new JTextArea();
		name.setEditable(false);
		name.setText("");
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		name.setPreferredSize(new Dimension(PANEL_WIDTH-10, 20));
		name.setMaximumSize(new Dimension(PANEL_WIDTH-5, 20));
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		name.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		name.setFont(FontLib.getFont("Tahoma", 10));
		//pole etykiety 
		label = new JTextArea();
		label.setEditable(false);		
		label.setText("");
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setPreferredSize(new Dimension(PANEL_WIDTH-10, 20));
		label.setMaximumSize(new Dimension(PANEL_WIDTH-5, 20));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		label.setFont(FontLib.getFont("Tahoma", 10));
		// pole komentarza 
		comment = new JTextArea();		
		comment.setEditable(false);
		comment.setLineWrap(true);
		comment.setWrapStyleWord(true);
		comment.setText("");
		comment.setFont(FontLib.getFont("Tahoma", 10));
		JScrollPane pane = new JScrollPane(comment);
		pane.setAlignmentX(Component.LEFT_ALIGNMENT);
		pane.setPreferredSize(new Dimension(PANEL_WIDTH-10,200));
		pane.setMaximumSize(new Dimension(PANEL_WIDTH-5, 200));
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(nameLabel);
		this.add(name);
		
		JLabel labelLabel = new JLabel("Label:");
		labelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(labelLabel);		
		this.add(label);
		JLabel commentLabel = new JLabel("Comment:");
		commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(commentLabel);
		this.add(pane);
		this.setPreferredSize(new Dimension(PANEL_WIDTH,300));
		this.setBorder(BorderFactory.createTitledBorder("Properties"));
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	

	public void setCommentText(String coment) {
		this.comment.setText(coment);
	}


	public void setLabelText(String label) {
		this.label.setText(label);
	}


	public void setNameText(String name) {
		this.name.setText(name);
		repaint();
	}
	
	@Override
    public JComboBox getCommentLang() {
        return commentLang;
    }

    @Override
    public JComboBox getLabelLang() {
        return labelLang;
    }	
}
