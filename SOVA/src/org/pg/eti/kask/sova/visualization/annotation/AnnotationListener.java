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
package org.pg.eti.kask.sova.visualization.annotation;

import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Set;
import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;

/**
 * Klasa listenera ustawiająca opisy zaznaczonego elementu w zadanym komponencie
 *
 * @author Piotr Kunowski
 *
 */
public class AnnotationListener extends ControlAdapter {

    private AnnotationComponent descriptComponent = null;
    private OWLOntologyManager manager = null;
    private OWLOntology ontology = null;
    private String lang = "";
    
    
    public AnnotationListener(AnnotationComponent component, OWLOntology ontology) {
        this.descriptComponent = component;
        this.ontology = ontology;
        this.manager = OWLManager.createOWLOntologyManager();

    }

    /**
     * Obsługa akcji kliknięcia na obiekt
     *
     * @see prefuse.controls.Control#itemClicked(prefuse.visual.VisualItem,
     * java.awt.event.MouseEvent)
     */
    
    private void returnProperty(OWLAnnotation elem){
        OWLAnnotationProperty prop = elem.getProperty();
        String label = prop.getIRI().getFragment();
        if (label.equals("label")) {
            OWLAnnotationValue val = elem.getValue();
            String l = val.toString();
            descriptComponent.setLabelText(l);
        } else if (label.equals("comment")) {
            OWLAnnotationValue val = elem.getValue();
            String l = val.toString();
            descriptComponent.setCommentText(l);
        }           
    }
    
    
    @Override
    public void itemClicked(VisualItem item, MouseEvent e) {
        Object o = item.get(OWLtoGraphConverter.COLUMN_IRI);
        descriptComponent.setNameText("");
        descriptComponent.setLabelText("");
        descriptComponent.setCommentText("");
        if (o != null) {
            OWLClass currentClass = manager.getOWLDataFactory().getOWLClass(IRI.create(((IRI) o).toURI()));

            descriptComponent.setNameText(currentClass.getIRI().getFragment());

            Set<OWLAnnotation> set = currentClass.getAnnotations(ontology);

            for (OWLAnnotation elem : set) {  
                returnProperty(elem);
            }
        }
    }
}
