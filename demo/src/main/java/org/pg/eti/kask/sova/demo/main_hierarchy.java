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

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.pg.eti.kask.sova.utils.Debug;
import org.pg.eti.kask.sova.utils.VisualizationProperties;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class main_hierarchy {

    public static void main(String[] args) {

        PrintStream p = new PrintStream(System.out);
        try {
            // przekazanie komunikatów klasy debugiera na system.out
            Debug.setStream(p);
            OWLOntology ontology = null;
            OWLOntologyManager manager = null;
            // wczytanie propertiesów -klasa singleton 
            VisualizationProperties.instanceOf().loadProperties(Constants.PROPERTIES);
            manager = OWLManager.createOWLOntologyManager();

            IRI physicalIRI = IRI.create(OntologyFileGetter.GetOntologyFilePath(Constants.ONTO_TEST_FILE));
            try {// wczytanie ontologi z pliku 
                ontology = manager.loadOntologyFromOntologyDocument(physicalIRI);

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
        } finally {
            p.close();
        }

    }
}
