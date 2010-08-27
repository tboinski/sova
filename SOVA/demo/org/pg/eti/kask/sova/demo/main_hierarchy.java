package org.pg.eti.kask.sova.demo;

import java.io.PrintStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.pg.eti.kask.sova.utils.Debug;
import org.pg.eti.kask.sova.utils.VisualizationProperties;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

public class main_hierarchy {

	public static void main(String[] args) {
		
		PrintStream p=new PrintStream(System.out); 
		try {
			// przekazanie komunikatów klasy debugiera na system.out
            Debug.setStream(p);
            OWLOntology ontology = null;
            OWLOntologyManager manager = null;
            // wczytanie propertiesów -klasa singleton 
            VisualizationProperties.instanceOf().loadProperties(Constants.PROPERTIES);
            manager = OWLManager.createOWLOntologyManager();
                
            URI physicalURI = URI.create(Constants.ONTO_TEST_DIRECTORY);
            try {// wczytanie ontologi z pliku 
            	ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
            	
            } catch (OWLOntologyCreationException ex) {
                Logger.getLogger(OWLOntologyManager.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
            
            JFrame frame = new JFrame("SOVA - Simple Ontology Visualization API 2010 ");
            JPanel panel = new HierarchyPanel(ontology);
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
