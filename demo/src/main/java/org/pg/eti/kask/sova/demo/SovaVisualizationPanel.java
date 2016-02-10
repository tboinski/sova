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
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
    private JPanel stopka = null;
    public JButton but2 = null;
    private final boolean enterFlag = false;
    
    protected void disposeOWLView() {
        display.removeDisplayVis();
    }

    public SovaVisualizationPanel(OWLOntology onto) {
        this.ontology = onto;        
        annotation = new AnnotationPanel();
        iriInfo = new IRITextField();
        display = new OVDisplay(ontology);
        display.setSovaPanel(this);
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
        ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource("SOVA.png")).getImage());
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
        but2 = new JButton("Reset");
        but2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                resetSearchBox();
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

    public void resetSearchBox(){
        display.removeDisplayVis();
        display.generateGraphFromOWl(ontology);
        leftPanel.removeAll();
        initLeftPanel();
        leftPanel.revalidate();
        if(optionFrame != null)
            optionFrame.getIDRadioButton().setSelected(true);
    }
    
    /**
     * inicjalizacja panelu wizualizacji
     */
    private void initLeftPanel() {
        if (leftPanel == null) {
            leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        }
        leftPanel.add(display);
        stopka = new JPanel();
        stopka.setLayout(new BoxLayout(stopka, BoxLayout.X_AXIS));
        stopka.setSize(Integer.MAX_VALUE, 20);
        stopka.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        stopka.setMinimumSize(new Dimension(Integer.MAX_VALUE, 20));
        stopka.setBackground(Color.WHITE);
        stopka.add(iriInfo);
        stopka.add(display.getSearchPanel());
        JLabel info = new JLabel("      Use keys ↑↓ to navigate");
        info.setFont(new Font("Tahoma", Font.PLAIN, 11));
        stopka.add(info);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
            new KeyEventDispatcher()  { 
                public boolean dispatchKeyEvent(KeyEvent e){
                    if(e.getID() == KeyEvent.KEY_PRESSED && e.isActionKey()){
                        display.getGraphLayoutVis().handleKeyPress(e.getKeyCode());
                        return true;                        
                    }
                    return false;
                }  
            });
        
        this.setFocusable(true);
        this.requestFocusInWindow();
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
