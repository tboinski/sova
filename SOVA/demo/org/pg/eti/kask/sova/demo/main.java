package org.pg.eti.kask.sova.demo;

import java.io.PrintStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.pg.eti.kask.sova.utils.Debug;
import org.pg.eti.kask.sova.utils.VisualizationProperties;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
/**
 * Klasa main
 * @author Piotr Kunowski
 *
 */
public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PrintStream p=new PrintStream(System.out); 
		try {
			// przekazanie komunikatów klasy debugiera na system.out
            Debug.setStream(p);
            OWLOntology ontology = null;
            // wczytanie propertiesów -klasa singleton 
            VisualizationProperties.instanceOf().loadProperties(Constants.PROPERTIES);
                
            URI physicalURI = URI.create(Constants.ONTO_TEST_DIRECTORY);
            try {// wczytanie ontologi z pliku 
            	ontology = OWLManager.createOWLOntologyManager().loadOntologyFromPhysicalURI(physicalURI);
            	
            } catch (OWLOntologyCreationException ex) {
                Logger.getLogger(OWLOntologyManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
            
            JFrame frame = new JFrame("SOVA - Simple Ontology Visualization API 2010 ");
            SovaVisualizationPanel panel = new SovaVisualizationPanel(ontology);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.setSize(frame.getMaximumSize());
            frame.setVisible(true); // show the window

        } catch (Exception ex) {
            Logger.getLogger(SovaVisualizationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        	p.close(); 
        }

	}

}
