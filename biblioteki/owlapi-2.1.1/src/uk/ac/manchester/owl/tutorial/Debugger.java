package uk.ac.manchester.owl.tutorial;

import org.semanticweb.owl.debugging.OWLDebugger;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;
import org.semanticweb.owl.inference.OWLSatisfiabilityChecker;
import org.semanticweb.owl.debugging.BlackBoxOWLDebugger;

import uk.ac.manchester.owl.tutorial.io.OWLTutorialSyntaxObjectRenderer;

import java.io.PrintWriter;
import java.net.URI;
import java.util.Collections;
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
 * <p>This class demonstrates some aspects of the OWL API. It expects three
 * arguments:</p>
 * <ol>
 * <li> The URI of an ontology</li>
 * 
 * <li> The URI of a reasoner</li>
 * 
 * <li> A location to place the results.</li>
 * </ol>
 * <p>When executed, the class will find all inconsistent classes in the ontology.
 * For each inconsistent class, the debugger will be used to determine the set
 * of support for the inconsistency. A report will then be written to the outpur
 * file.</p>
 * 
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 24-April-2007<br>
 * <br>
 */
public class Debugger {

    private OWLOntology ontology;

    private OWLDebugger debugger;

    private OWLSatisfiabilityChecker checker;

    private OWLClass bottom;

    public Debugger(OWLOntologyManager manager, OWLOntology ontology,
            OWLSatisfiabilityChecker checker) throws OWLException {
        this.ontology = ontology;
        this.checker = checker;
        checker.loadOntologies(Collections.singleton(ontology));
        /* Create a new debugger */
        this.debugger = new BlackBoxOWLDebugger(manager, ontology, checker);
        /* Get bottom */
        URI bottomURI = OWLRDFVocabulary.OWL_THING.getURI();
        bottom = manager.getOWLDataFactory().getOWLClass(bottomURI);
    }

    public void report(PrintWriter writer) throws OWLException {
        OWLTutorialSyntaxObjectRenderer renderer = new OWLTutorialSyntaxObjectRenderer(
                ontology, writer);
        /* Write a header */
        renderer.header();

        Set<OWLClass> unsatisfiables = new HashSet<OWLClass>();
        for (OWLClass clazz : ontology.getReferencedClasses()) {
            /* Collect the unsatisfiable classes that aren't bottom. */
            if (!checker.isSatisfiable(clazz) && !clazz.equals(bottom)) {
                unsatisfiables.add(clazz);
            }
        }

        writer.println("<h1>Ontology Debugging Report</h1>");
        writer.println("<p>Ontology: " + ontology.getURI() + "</p>");

        if (unsatisfiables.isEmpty()) {
            writer.println("<p>No Unsatisfiable Classes found</p>");
        } else {
            for (OWLClass unsatisfiable : unsatisfiables) {
                writer.println("<div class='box'>\n");
                writer.println("<h2 class='cl'>");
                unsatisfiable.accept(renderer);
                writer.println("</h2>");
                writer.println("<p>Axioms causing inconsistency:</p>");
                writer.println("<ul>");

                /*
                 * Find the set of support for the inconsistency. This will
                 * return us a collection of axioms
                 */
                Set<OWLAxiom> sos = 
                    debugger.getSOSForIncosistentClass(unsatisfiable);

                /* Print the axioms. */
                for (OWLAxiom axiom : sos) {
                    writer.println("<li>");
                    axiom.accept(renderer);
                    writer.println("</li>");
                }
                writer.println("</ul>");
                writer.println("</div>\n");

            }
        }
        renderer.footer();

    }

}