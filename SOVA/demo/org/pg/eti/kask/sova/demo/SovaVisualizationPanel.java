/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
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
package org.pg.eti.kask.sova.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
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

public class SovaVisualizationPanel extends JPanel {

    private static final long serialVersionUID = -4515710047558710080L;
    private OVDisplay display;
    private Options optionFrame = null;
    public boolean doLayout = true;
    private JButton options = null;
    private JPanel leftPanel = null, rightPanel = null;
    private AnnotationPanel annotation;
    private OWLOntology ontology = null;
    private IRITextField iriInfo = null;

    protected void disposeOWLView() {
        display.removeDisplayVis();
    }

    public SovaVisualizationPanel(OWLOntology onto) {
        this.ontology = onto;

        annotation = new AnnotationPanel();
        iriInfo = new IRITextField();
        display = new OVDisplay(ontology);
        display.setSize(800, 600);
        display.addAnnotationComponent(annotation);
        display.addIRIInfoComponent(iriInfo);
        display.generateGraphFromOWl();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        initLeftPanel();
        this.add(leftPanel);
        initRightPanel();
        this.add(rightPanel);
    }

    /**
     * inicjalizacja panelu nawigacyjnego
     */
    private void initRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource(
                "SOVA.png")).getImage());
        rightPanel.add(icon);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
        JButton but = new JButton("Play/Stop");
        but.setToolTipText("Play or stop animation");
        but.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (doLayout) {
                    display.getVisualization().stopLayout();
                    doLayout = false;
                } else {
                    display.getVisualization().startLayout();
                    doLayout = true;
                }
            }
        });
        but.setSize(100, 80);
        buttonPanel.add(but);
        JButton but2 = new JButton("Reset");
        but2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                display.removeDisplayVis();
                display.generateGraphFromOWl(ontology);
                doLayout = true;
            }
        });
        but2.setSize(100, 80);
        but2.setToolTipText("Reload ontology");
        buttonPanel.add(but2);

        options = new JButton("Options");
        options.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (optionFrame == null) {
                    initOptionFrame();
                }
                if (!optionFrame.isOptionFrameShow()) {
                    optionFrame.setVisible(true);
                    optionFrame.setOptionFrameShow(true);
                } else {
                    optionFrame.setVisible(false);
                    optionFrame.setOptionFrameShow(false);
                }
            }
        });
        options.setSize(100, 80);
        buttonPanel.add(options);
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
                    if (doLayout) {
                        display.getVisualization().startLayout();
                    }
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
                } finally {
                    if (doLayout) {
                        display.getVisualization().startLayout();
                    }
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
                    if (doLayout) {
                        display.getVisualization().startLayout();
                    }
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
                    display.saveFullImage(os, 5.0);
                    os.close();
                    //zapis do pliku
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (doLayout) {
                        display.getVisualization().startLayout();
                    }
                }

            }
        });
        buttonPanel.add(saveFullImage);

        rightPanel.add(buttonPanel);
        rightPanel.add(annotation);
        rightPanel.setPreferredSize(new Dimension(180, Integer.MAX_VALUE));
        rightPanel.setMaximumSize(new Dimension(190, Integer.MAX_VALUE));
        rightPanel.setMinimumSize(new Dimension(170, Integer.MAX_VALUE));
    }

    /**
     * inicjalizacja panelu wizualizacji
     */
    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(display);
        JPanel stopka = new JPanel();
        stopka.setLayout(new BoxLayout(stopka, BoxLayout.X_AXIS));
        stopka.setSize(Integer.MAX_VALUE, 20);
        stopka.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        stopka.setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
        stopka.setBackground(Color.WHITE);


//		JPanel searchPanel = new JPanel(new GridLayout(1, 10));
//		searchPanel.setSize(stopka.getSize().width/2, stopka.getSize().height);
//		JTextField searchText = new JTextField(10);
//		searchPanel.add(searchText);
//		JButton bSearch = new JButton("Search");
//		
//		searchPanel.add(bSearch);
//		stopka.add(searchPanel);
//		iriInfo.setSize(stopka.getSize().width/2, stopka.getSize().height);


        stopka.add(iriInfo);
        stopka.add(display.getSearchPanel());
        leftPanel.add(stopka);
    }

    private void initOptionFrame() {
        optionFrame = new Options(display);
        Point location = options.getLocationOnScreen();
        location.x -= optionFrame.getSize().width;
        location.y -= 200;
        optionFrame.setLocation(location);
    }
}
