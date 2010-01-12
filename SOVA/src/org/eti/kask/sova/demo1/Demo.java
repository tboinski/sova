package org.eti.kask.sova.demo1;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import org.eti.kask.sova.utils.Debug;
import org.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.eti.kask.sova.utils.VisualizationProperties;
import org.eti.kask.sova.visualization.OVVisualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.util.force.ForceSimulator;
import prefuse.util.ui.JForcePanel;

/**
 *
 * @author infinity
 */
public class Demo {

    public static boolean doLayout = true;
    public static OVDisplay display;

    public static void main(String[] args) {

        try {
            FileOutputStream out; // declare a file output object
            PrintStream p; // declare a print stream object
            // Create a new file output stream
            // connected to "myfile.txt"
            out = new FileOutputStream(Constants.DEBUG_FILE);
            // Connect print stream to the output stream
            p = new PrintStream(out);


            Debug.setStream(p);
            display = new OVDisplay();
            display.setSize(720, 500); // set display size
	    VisualizationProperties.instanceOf().loadProperties(Constants.PROPERTIES);
            // zoom with vertical right-drag
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            // We load an ontology from a physical URI - in this case we'll load the pizza
            // ontology.
            URI physicalURI = URI.create(Constants.ONTO_TEST_DIRECTORY);
            try {
                // Now ask the manager to load the ontology
                display.generateGraphFromOWl(manager.loadOntologyFromPhysicalURI(physicalURI));
            } catch (OWLOntologyCreationException ex) {
                Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println(VisualizationProperties.instanceOf().getProperty("node.name", "default string"));
            display.setBackground(VisualizationProperties.instanceOf().getPropertyColor("node.color.cardinalityNodeColor", Color.WHITE));
            display.addControlListener(new NeighborHighlightControl("repaint"));
            display.addControlListener(new HoverActionControl("repaint"));
            // create a new window to hold the visualization
            JFrame frame = new JFrame("SOVA prefuse usage demo");
            // ensure application exits when window is closed
            display.addKeyListener(new java.awt.event.KeyListener() {

                public void keyPressed(KeyEvent arg0) {
                    if (arg0.getKeyChar() == 'z') {
                        if (doLayout) {
                            display.getVisualization().stopLayout();
                            doLayout = false;
                        } else {
                            display.getVisualization().startLayout();
                            doLayout = true;
                        }
                    }
                }

                public void keyReleased(KeyEvent arg0) {
                }

                public void keyTyped(KeyEvent arg0) {
                }
            });
            JPanel visValues = new JPanel();
            visValues.setLayout(new BoxLayout(visValues, BoxLayout.Y_AXIS));
            if (display.getGraphLayout() == OVDisplay.FORCE_DIRECTED_LAYOUT) {
                Action action = display.getVisualization().getAction(OVVisualization.LAYOUT_ACTION);
                ForceSimulator fsim = ((ForceDirectedLayout) ((ActionList) action).get(0)).getForceSimulator();
                JForcePanel fpanel = new JForcePanel(fsim);
                visValues.add(fpanel);
            }
            JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
            buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rodzaje wizualizacji"));


            JRadioButton forceDirectedRadial = new JRadioButton("ForceDirectedLayout", true);
            forceDirectedRadial.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    display.setGraphLayout(OVDisplay.FORCE_DIRECTED_LAYOUT);
                    display.refreshVisualization();
                    display.repaint();
                }
            });
            JRadioButton radialTreeRadial = new JRadioButton("RadialTreeLayout", false);
            radialTreeRadial.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    display.setGraphLayout(OVDisplay.RADIAL_TREE_LAYOUT);
                    display.refreshVisualization();
                    display.repaint();
                }
            });
            ButtonGroup radialGroup = new ButtonGroup();
            radialGroup.add(forceDirectedRadial);
            radialGroup.add(radialTreeRadial);
            buttonPanel.add(forceDirectedRadial);

            buttonPanel.add(radialTreeRadial);
            buttonPanel.setSize(200, 200);
            visValues.add(buttonPanel);

            JPanel checkboxPanel = new JPanel(new GridLayout(2, 1));
            checkboxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filtry"));
            Checkbox chSubClass = new Checkbox("Wyłącz krawędzie SubEdge");
            chSubClass.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent arg0) {
                    if (ItemEvent.SELECTED == arg0.getStateChange()) {
                        display.getVisualization().getFilterOVPredicate().setSubEdgeFilter(true);
                        display.getVisualization().refreshFilter();
                    } else {
                        display.getVisualization().getFilterOVPredicate().setSubEdgeFilter(false);
                        display.getVisualization().refreshFilter();
                    }
                }
            });
            checkboxPanel.add(chSubClass);


            Checkbox chDisjointEdge = new Checkbox("Wyłącz krawędzie DisjointEdge");
            chDisjointEdge.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent arg0) {
                    if (ItemEvent.SELECTED == arg0.getStateChange()) {
                        display.getVisualization().getFilterOVPredicate().setDisjointEdgeFilter(true);
                        display.getVisualization().refreshFilter();
                        display.repaint();
                    } else {
                        display.getVisualization().getFilterOVPredicate().setDisjointEdgeFilter(false);
                        display.getVisualization().refreshFilter();
                        display.repaint();
                    }
                }
            });
            checkboxPanel.add(chDisjointEdge);

            visValues.add(checkboxPanel);

            Box v1 = new Box(BoxLayout.Y_AXIS);
            JButton but = new JButton("Wł/Wy Animację");
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
            v1.add(but);
            v1.setBorder(BorderFactory.createTitledBorder("Opcje Animacji"));
            visValues.add(v1);
            // create a new JSplitPane to present the interface
            JSplitPane split = new JSplitPane();
            split.setLeftComponent(display);
            split.setRightComponent(visValues);
            split.setOneTouchExpandable(true);
            split.setContinuousLayout(false);
            split.setDividerLocation(700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(split);
            frame.pack(); // layout components in window
            frame.setVisible(true); // show the window

            p.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}