package org.coode.owlapi.examples;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.apibinding.OWLManager;

import java.net.URI;
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
 * Date: 23-May-2007<br><br>
 *
 * This example shows how add an object property assertion
 * (triple) of the form prop(subject, object) for example
 * hasPart(a, b)
 */
public class Example4 {

    public static void main(String[] args) {
        try {
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();

            String base = "http://www.semanticweb.org/ontologies/individualsexample";

            OWLOntology ont = man.createOntology(URI.create(base));

            OWLDataFactory dataFactory = man.getOWLDataFactory();

            // In this case, we would like to state that matthew has a father
            // who is peter.
            // We need a subject and object - matthew is the subject and peter is the
            // object.  We use the data factory to obtain references to these individuals
            OWLIndividual matthew = dataFactory.getOWLIndividual(URI.create(base + "#matthew"));
            OWLIndividual peter = dataFactory.getOWLIndividual(URI.create(base + "#peter"));
            // We want to link the subject and object with the hasFather property, so use the data factory
            // to obtain a reference to this object property.
            OWLObjectProperty hasFather = dataFactory.getOWLObjectProperty(URI.create(base + "#hasFather"));
            // Now create the actual assertion (triple), as an object property assertion axiom
            // matthew --> hasFather --> peter
            OWLObjectPropertyAssertionAxiom assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(matthew, hasFather, peter);
            // Finally, add the axiom to our ontology and save
            AddAxiom addAxiomChange = new AddAxiom(ont, assertion);
            man.applyChange(addAxiomChange);

            man.saveOntology(ont, URI.create("file:/tmp/example.owl"));
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Could not create ontology: " + e.getMessage());
        }
        catch(OWLOntologyChangeException e) {
            System.out.println("Problem editing ontology: " + e.getMessage());
        }
        catch(OWLOntologyStorageException e) {
            System.out.println("Could not save ontology: " + e.getMessage());
        }
    }
}
