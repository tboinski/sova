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

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.pg.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owlapi.model.OWLOntology;
import prefuse.Display;

public class HierarchyPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 3124793583527560797L;
    private OWLOntology ontology = null;
    private JPanel leftPanel = null, rightPanel = null;
    private OVDisplay display;
    private boolean showFullTree = true;
    private JButton but3 = null;

    public HierarchyPanel(OWLOntology ontology) {
        this.ontology = ontology;
        initialiseOWLView();
    }

    private void initialiseOWLView() {
        display = new OVDisplay();
        display.setSize(1000, 900);
        display.generateTreeFromOWl(ontology);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        display.setSize(1000, 900);
        initLeftPanel();
        this.add(leftPanel);
        initRightPanel();
        this.add(rightPanel);

    }

    private void initRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource(
                "SOVA.png")).getImage());
        rightPanel.add(icon);
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1));
        JButton but2 = new JButton("Reset");
        but2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                display.removeDisplayVis();
                display.generateTreeFromOWl(ontology);

            }
        });
        but2.setSize(100, 80);
        but2.setToolTipText("Reload ontology");
        buttonPanel.add(but2);

        but3 = new JButton("Show Full Tree");
        but3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (showFullTree) {
                    display.showFullTree();
                    but3.setText("Hide Full Tree");
                    showFullTree = false;
                } else {
                    display.hideFullTree();
                    but3.setText("Show Full Tree");
                    showFullTree = true;
                }
            }
        });
        buttonPanel.add(but3);

        JButton saveImage = new JButton("Save Image");
        saveImage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                display.getVisualization().stopLayout();
                File f = new File("");
                FileDialog fd = new FileDialog(new Frame(), "Save", FileDialog.SAVE);
                fd.setFilenameFilter(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        if (name.toUpperCase().endsWith(".PNG")
                                || name.toUpperCase().endsWith(".JPG")) {
                            return true;
                        }
                        return false;
                    }
                });
                fd.setLocation(50, 50);
                fd.setVisible(true);
                if (fd.getDirectory() == null || fd.getFile() == null) {
                    return;
                }
                String sFile = fd.getDirectory() + fd.getFile();

                String format = "png";

                if (sFile.toUpperCase().endsWith(".PNG")
                        || sFile.toUpperCase().endsWith(".JPG")) {
                    format = sFile.substring(sFile.length() - 3, sFile.length());
                } else {
                    sFile += '.' + format;
                }

                File file = new File(sFile);

                FileOutputStream os;
                try {

                    os = new FileOutputStream(file);
                    display.saveImage(os, format.toUpperCase(), 5);
                    os.close();
                    //zapis do pliku
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        buttonPanel.add(saveImage);

        JButton saveFullImage = new JButton("Save Full Image");
        saveFullImage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                display.getVisualization().stopLayout();
                File f = new File("");
                FileDialog fd = new FileDialog(new Frame(), "Save Full Image", FileDialog.SAVE);
                fd.setFilenameFilter(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        if (name.toUpperCase().endsWith(".PNG")
                                || name.toUpperCase().endsWith(".JPG")) {
                            return true;
                        }
                        return false;
                    }
                });
                fd.setLocation(50, 50);
                fd.setVisible(true);
                if (fd.getDirectory() == null || fd.getFile() == null) {
                    return;
                }
                String sFile = fd.getDirectory() + fd.getFile();

                String format = "png";

                if (sFile.toUpperCase().endsWith(".PNG")
                        || sFile.toUpperCase().endsWith(".JPG")) {
                    format = sFile.substring(sFile.length() - 3, sFile.length());
                } else {
                    sFile += '.' + format;
                }

                File file = new File(sFile);

                FileOutputStream os;
                try {

                    os = new FileOutputStream(file);
                    Display display2 = new Display(display.getVisualization());
                    display2.saveImage(os, format.toUpperCase(), 5);
//                    display.saveFullImage(os, 5.0);
                    os.close();
                    //zapis do pliku
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttonPanel.add(saveFullImage);

        rightPanel.add(buttonPanel);
        rightPanel.setPreferredSize(new Dimension(120, Integer.MAX_VALUE));
        rightPanel.setMaximumSize(new Dimension(140, Integer.MAX_VALUE));
        rightPanel.setMinimumSize(new Dimension(100, Integer.MAX_VALUE));
    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.add(display);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    }
}
