/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eti.kask.sova.demo1;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.eti.kask.sova.utils.Debug;
import org.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.eti.kask.sova.demo1.Constants;
import org.eti.kask.sova.utils.VisualizationProperties;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;

/**
 *
 * @author infinity
 */
public class Demo
{
    public static boolean doLayout = true;
    public static OVDisplay display ;
	public static void main(String[] args)
	{


		Debug.setStream(System.out);

		display = new OVDisplay();
		display.setSize(720, 500); // set display size
        
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


        VisualizationProperties.instanceOf().loadProperties("/home/piotr29/STUDIA/OCS/visualization.properties");
        System.out.println( VisualizationProperties.instanceOf().getProperty("node.name", "default string"));
        display.setBackground(VisualizationProperties.instanceOf().getPropertyColor("node.color.cardinalityNodeColor", Color.WHITE));
                        display.addControlListener(new NeighborHighlightControl("repaint"));
               display.addControlListener(new HoverActionControl("repaint"));
// create a new window to hold the visualization
		JFrame frame = new JFrame("SOVA prefuse usage demo");
// ensure application exits when window is closed
        display.addKeyListener(new java.awt.event.KeyListener() {
                   
            public void keyPressed(KeyEvent arg0) {
                 if (arg0.getKeyChar()=='z'){
                        if (doLayout){
                              System.err.println("1 display stop!!!");
                            display.stopLayout();

                            doLayout=false;
                        }else{
                              System.err.println("1 display start");
                            display.startLayout();

                            doLayout = true;
                        }
                }
            }

            public void keyReleased(KeyEvent arg0) {

            }

            public void keyTyped(KeyEvent arg0) {
    
            }
            });
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(display);
		frame.pack();           // layout components in window
		frame.setVisible(true); // show the window

		

	}
}
