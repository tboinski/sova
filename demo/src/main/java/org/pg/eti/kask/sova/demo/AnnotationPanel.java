/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of SOVA.  SOVA is free software: you can
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
package org.pg.eti.kask.sova.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.pg.eti.kask.sova.visualization.annotation.AnnotationComponent;
import prefuse.util.FontLib;

public class AnnotationPanel extends JPanel implements AnnotationComponent {

    /**
     *
     */
    private static final long serialVersionUID = 5829137853147447938L;
    private static int PANEL_WIDTH = 180;
    private JTextArea name = null;
    private JTextArea label = null;
    private JTextArea comment = null;
    private JComboBox labelLang = null;
    private JComboBox commentLang = null;

    public AnnotationPanel() {
        initPanel();
    }

    private void initPanel() {
        setSize(PANEL_WIDTH, 300);
        // pole nazwy
        name = new JTextArea();
        name.setEditable(false);
        name.setText("");
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setPreferredSize(new Dimension(PANEL_WIDTH - 10, 20));
        name.setMaximumSize(new Dimension(PANEL_WIDTH - 5, 20));
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        name.setFont(FontLib.getFont("Tahoma", 10));
        //pole etykiety 
        label = new JTextArea();
        label.setEditable(false);
        label.setText("");
        label.setLineWrap(true);
        label.setWrapStyleWord(true);
        label.setPreferredSize(new Dimension(PANEL_WIDTH - 10, 20));
        label.setMaximumSize(new Dimension(PANEL_WIDTH - 5, 20));
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
        pane.setPreferredSize(new Dimension(PANEL_WIDTH - 10, 200));
        pane.setMaximumSize(new Dimension(PANEL_WIDTH - 5, 200));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(nameLabel);
        this.add(name);

        JPanel labelLangPanel = new JPanel(new GridLayout(0, 2));
        labelLangPanel.setPreferredSize(new Dimension(PANEL_WIDTH - 10, 20));
        labelLangPanel.setMaximumSize(new Dimension(PANEL_WIDTH - 5, 20));
        JLabel labelLabel = new JLabel("Label");
   
        //Combo box dla labelów
        labelLang = new JComboBox();
        
        labelLangPanel.add(labelLabel);
        labelLangPanel.add(labelLang);
        
        this.add(labelLangPanel);

        this.add(label);

        JPanel commentPanel = new JPanel(new GridLayout(0, 2));
        commentPanel.setPreferredSize(new Dimension(PANEL_WIDTH - 10, 20));
        commentPanel.setMaximumSize(new Dimension(PANEL_WIDTH - 5, 20));

        JLabel commentLabel = new JLabel("Comment");
        //Combo box dla komentarzy
        commentLang = new JComboBox();
 
        commentPanel.add(commentLabel);
        commentPanel.add(commentLang);
        this.add(commentPanel);

        this.add(pane);
        
        this.setPreferredSize(new Dimension(PANEL_WIDTH, 300));
        this.setBorder(BorderFactory.createTitledBorder("Properties"));
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public void setCommentText(String coment) {
        this.comment.setText(coment);
        repaint();
    }

    @Override
    public void setLabelText(String label) {
        this.label.setText(label);
        repaint();
    }

    @Override
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
