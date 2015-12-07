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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JComboBox;
import org.pg.eti.kask.sova.graph.Constants;
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
    private Map<String, String> commentsLangs = new HashMap<String, String>();
    private Map<String, String> labelsLangs = new HashMap<String, String>();
    private enum Selection { ALL, COMMENT, LABEL }
    private Object currentObj = null;
    private Object comment;
    private Object label;
    private JComboBox commentBox;
    private JComboBox labelBox;
    private String currentLangKey;
    
    public AnnotationListener(AnnotationComponent component, OWLOntology ontology) {
        this.descriptComponent = component;
        this.ontology = ontology;
        this.manager = OWLManager.createOWLOntologyManager();
        this.commentBox = descriptComponent.getCommentLang();
        this.labelBox = descriptComponent.getLabelLang();
    }
    
    /**
     * Obsługa akcji kliknięcia na obiekt
     *
     * @see prefuse.controls.Control#itemClicked(prefuse.visual.VisualItem,
     * java.awt.event.MouseEvent)
     */
    private void createDictionaries(OWLAnnotation elem) {

        OWLAnnotationProperty prop = elem.getProperty();
        String label = prop.getIRI().getFragment();
        OWLAnnotationValue val = elem.getValue();
        String l = val.toString();
        String[] parts = l.split("@");

        if (label.equals("label")) {

            if (parts.length == 1 || parts[1].isEmpty()) {
                descriptComponent.setLabelText(parts[0]);
                return;
            }

            labelsLangs.put(parts[1], parts[0]);

        } else if (label.equals("comment")) {

            if (parts.length == 1 || parts[1].isEmpty()) {
                descriptComponent.setCommentText(parts[0]);
                return;
            }

            commentsLangs.put(parts[1], parts[0]);
        }
    }

    //Załaduj wybrany element z Combo box'a
    private void changeContent(Selection selected) {
        
        comment = commentBox.getSelectedItem();
        label = labelBox.getSelectedItem();
        
        switch(selected){
        
            case ALL:
                descriptComponent.setLabelText(labelsLangs.get(label));
                descriptComponent.setCommentText(commentsLangs.get(comment));
                break;
            case COMMENT:     
                descriptComponent.setCommentText(commentsLangs.get(comment));
                break;
            case LABEL:        
                descriptComponent.setLabelText(labelsLangs.get(label));
                break;
                
            default:
                break;
        
        }
    }

    //Uzupełnij Combo box'a o dostępne języki 
    private void fullFillComboBox(JComboBox box, Map<String, String> map) {

        box.removeAllItems();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            box.addItem(pair.getKey());
        }
    }

    @Override
    public void itemClicked(VisualItem item, MouseEvent e) {

        Object o = item.get(OWLtoGraphConverter.COLUMN_IRI);     
        currentObj = ((VisualItem) item).get(Constants.GRAPH_NODES);
        
        descriptComponent.setNameText("");
        descriptComponent.setLabelText("");
        descriptComponent.setCommentText("");
        
        if (o != null) {
            OWLClass currentClass = manager.getOWLDataFactory().getOWLClass(IRI.create(((IRI) o).toURI()));

            descriptComponent.setNameText(currentClass.getIRI().getFragment());

            Set<OWLAnnotation> set = currentClass.getAnnotations(ontology);

            commentsLangs.clear();
            labelsLangs.clear();

            for (OWLAnnotation elem : set) {
                createDictionaries(elem);
            }
           
            //Sprawdź czy została ustawiona wizualizacja po labelach czy po ID
            String currentNodeLabel = ((org.pg.eti.kask.sova.nodes.ClassNode) currentObj).getLabel();     
            currentLangKey = "";
            if(labelsLangs.containsValue(currentNodeLabel)){
                Iterator it = labelsLangs.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    if(pair.getValue().equals(currentNodeLabel)){
                        currentLangKey = pair.getKey().toString();
                        break;
                    }
                }   
            }
            
            fullFillComboBox(commentBox, commentsLangs);
            fullFillComboBox(labelBox, labelsLangs);
            changeContent(Selection.ALL);
            
            //Ustawia wybrany wcześniej język w combo box
            if(!currentLangKey.isEmpty())
                labelBox.setSelectedItem(currentLangKey);
        }

        //Listener dla combo box'a z językami dostępnymi dla komentarzy
        descriptComponent.getCommentLang().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContent(Selection.COMMENT);
            }
        });

        //Listener dla combo box'a z językami dostępnymi dla labelów
        descriptComponent.getLabelLang().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeContent(Selection.LABEL);
                Object label = labelBox.getSelectedItem();
                if(labelsLangs.containsKey(currentLangKey)){
                    ((org.pg.eti.kask.sova.nodes.ClassNode) currentObj).setLabel(labelsLangs.get(label));
                }
            }
        });
    }
}
