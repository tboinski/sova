package org.coode.owlapi.examples;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.util.DLExpressivityChecker;

import java.net.URI;
import java.util.Set;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 * Date: 20-Jun-2007<br><br>
 *
 * An example which shows how to interact with a reasoner.  In this example
 * Pellet is used as the reasoner.  You must get hold of the pellet libraries
 * from pellet.owldl.com.
 */
public class Example8 {

    public static final String PHYSICAL_URI = "http://www.co-ode.org/ontologies/pizza/2007/02/12/pizza.owl";

    public static void main(String[] args) {

        try {
            // Create our ontology manager in the usual way.
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            // Load a copy of the pizza ontology.  We'll load the ontology from the web.
            OWLOntology ont = manager.loadOntologyFromPhysicalURI(URI.create(PHYSICAL_URI));
            System.out.println("Loaded " + ont.getURI());


            // We need to create an instance of OWLReasoner.  OWLReasoner provides the basic
            // query functionality that we need, for example the ability obtain the subclasses
            // of a class etc.  See the createReasoner method implementation for more details
            // on how to instantiate the reasoner
            OWLReasoner reasoner = createReasoner(manager);

            // We now need to load some ontologies into the reasoner.  This is typically the
            // imports closure of an ontology that we're interested in.  In this case, we want
            // the imports closure of the pizza ontology.  Note that no assumptions are made
            // about the dependency of one ontology on another ontology.  This means that if
            // we loaded just the pizza ontology (using a singleton set) then any imported ontologies
            // would not automatically be loaded.
            // Obtain and load the imports closure of the pizza ontology
            Set<OWLOntology> importsClosure = manager.getImportsClosure(ont);
            reasoner.loadOntologies(importsClosure);

            // We can examine the expressivity of our ontology (some reasoners do not support
            // the full expressivity of OWL)
            DLExpressivityChecker checker = new DLExpressivityChecker(importsClosure);
            System.out.println("Expressivity: " + checker.getDescriptionLogicName());

            // We can determine if the pizza ontology is actually consistent.  (If an ontology is
            // inconsistent then owl:Thing is equivalent to owl:Nothing - i.e. there can't be any
            // models of the ontology)
            boolean consistent = reasoner.isConsistent(ont);
            System.out.println("Consistent: " + consistent);
            System.out.println("\n");

            // We can easily get a list of inconsistent classes.  (A class is inconsistent if it
            // can't possibly have any instances).  Note that the getInconsistentClasses method
            // is really just a convenience method for obtaining the classes that are equivalent
            // to owl:Nothing.
            Set<OWLClass> inconsistentClasses = reasoner.getInconsistentClasses();
            if (!inconsistentClasses.isEmpty()) {
                System.out.println("The following classes are inconsistent: ");
                for(OWLClass cls : inconsistentClasses) {
                    System.out.println("    " + cls);
                }
            }
            else {
                System.out.println("There are no inconsistent classes");
            }
            System.out.println("\n");

            // Now we want to query the reasoner for all descendants of VegetarianPizza - i.e. all
            // vegetarian pizzas.
            // Get a reference to the vegetarian pizza class
            OWLClass vegPizza = manager.getOWLDataFactory().getOWLClass(URI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#VegetarianPizza"));

            // Now use the reasoner to obtain the subclasses of vegetarian pizza.  Note the reasoner
            // returns a set of sets.  Each set represents a subclass of vegetarian pizza where the
            // classes in the set represent equivalence classes.  For example, if we asked for the
            // subclasses of A and got back {{B, C}, {D}} then A would have essentially to subclasses.
            // One of these subclasses would be equivalent to the class D, and the other would be the class that
            // was equivalent to class B and class C.
            Set<Set<OWLClass>> subClsSets = reasoner.getDescendantClasses(vegPizza);
            // In this case, we don't particularly care about the equivalences, so we will flatten this
            // set of sets and print the result
            System.out.println("Vegetarian pizzas: ");
            Set<OWLClass> subClses = OWLReasonerAdapter.flattenSetOfSets(subClsSets);
            for(OWLClass cls : subClses) {
                System.out.println("    " + cls);
            }

            System.out.println("\n");

            // We can easily retrieve the instances of a class.  In this example we'll obtain the instances of
            // country.  First we need to get a reference to the country class
            OWLClass country = manager.getOWLDataFactory().getOWLClass(URI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#Country"));
            System.out.println("Instances of country: ");
            for(OWLIndividual ind : reasoner.getIndividuals(country, true)) {
                System.out.println("    " + ind);
            }

        }
        catch(UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        }
        catch(OWLReasonerException ex) {
            System.out.println("Reasoner error: " + ex.getMessage());
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("Could not load the pizza ontology: " + e.getMessage());
        }
    }

    private static OWLReasoner createReasoner(OWLOntologyManager man) {
        try {
            // The following code is a little overly complicated.  The reason for using
            // reflection to create an instance of pellet is so that there is no compile time
            // dependency (since the pellet libraries aren't contained in the OWL API repository).
            // Normally, one would simply create an instance using the following incantation:
            //
            //     OWLReasoner reasoner = new Reasoner()
            //
            // Where the full class name for Reasoner is org.mindswap.pellet.owlapi.Reasoner
            //
            // Pellet requires the Pellet libraries  (pellet.jar, aterm-java-x.x.jar) and the
            // XSD libraries that are bundled with pellet: xsdlib.jar and relaxngDatatype.jar
            String reasonerClassName = "org.mindswap.pellet.owlapi.Reasoner";
            Class reasonerClass = Class.forName(reasonerClassName);
            Constructor<OWLReasoner> con = reasonerClass.getConstructor(OWLOntologyManager.class);
            return con.newInstance(man);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
