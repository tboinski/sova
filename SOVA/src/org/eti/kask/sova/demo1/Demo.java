/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eti.kask.sova.demo1;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 *
 * @author infinity
 */
public class Demo
{

	public static void main(String[] args)
	{
		OVDisplay display = new OVDisplay();
		display.setSize(720, 500); // set display size
		// zoom with vertical right-drag

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		// We load an ontology from a physical URI - in this case we'll load the pizza
		// ontology.
		URI physicalURI = URI.create("file:/home/valanthe/onot-test.owl");
		try {
			// Now ask the manager to load the ontology
			display.generateGraphFromOWl(manager.loadOntologyFromPhysicalURI(physicalURI));
		} catch (OWLOntologyCreationException ex) {
			Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
		}



// create a new window to hold the visualization
		JFrame frame = new JFrame("prefuse example");
// ensure application exits when window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(display);
		frame.pack();           // layout components in window
		frame.setVisible(true); // show the window

		

	}
}
