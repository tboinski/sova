package org.coode.owl.rdf.rdfxml;

import org.coode.xml.OWLOntologyNamespaceManager;
import org.semanticweb.owl.model.*;

import java.util.HashSet;
import java.util.Set;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 25-Jul-2007<br><br>
 */
public class RDFXMLNamespaceManager extends OWLOntologyNamespaceManager {


    public RDFXMLNamespaceManager(OWLOntologyManager man, OWLOntology ontology) {
        super(man, ontology);
    }


    public RDFXMLNamespaceManager(OWLOntologyManager man, OWLOntology ontology, OWLOntologyFormat format) {
        super(man, ontology, format);
    }


    protected Set<OWLEntity> getEntitiesThatRequireNamespaces() {
        // We need namespaces to write QNames for properties in
        // property assertions.  It is also nicer if classes in class
        // assertions have namespaces for pretty printing purposes.
        // We only need to return:
        // 1) Properties that appear in property assertions
        // 3) Classes that appear in class assertions
        Set<OWLEntity> entities = new HashSet<OWLEntity>();
        for(OWLObjectPropertyAssertionAxiom ax : getOntology().getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
            if (!ax.getProperty().isAnonymous()) {
                entities.add(ax.getProperty().asOWLObjectProperty());
            }
        }

        for(OWLDataPropertyAssertionAxiom ax : getOntology().getAxioms(AxiomType.DATA_PROPERTY_ASSERTION)) {
            if (!ax.getProperty().isAnonymous()) {
                entities.add(ax.getProperty().asOWLDataProperty());
            }
        }
        for(OWLClassAssertionAxiom ax : getOntology().getAxioms(AxiomType.CLASS_ASSERTION)) {
            if (!ax.getDescription().isAnonymous()) {
                entities.add(ax.getDescription().asOWLClass());
            }
        }
        return entities;
    }
}
