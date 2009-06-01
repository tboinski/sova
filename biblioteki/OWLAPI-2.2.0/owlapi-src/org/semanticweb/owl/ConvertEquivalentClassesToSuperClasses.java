package org.semanticweb.owl;/*
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


import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLDescriptionVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jul-2007<br><br>
 * <p/>
 * This composite change will convert a defined class to a primitive class by replacing equivalent classes
 * axioms where the class in question is a class in the equivalent classes axioms to a set of subclass axioms
 * whose superclasses are the set of classes which were originally equivalent to the class in question.
 * <p/>
 * More formally, for a given class A, a set of ontologies S, and a target ontology T, this composite change
 * will remove all equivalent axioms from each ontology O in S where the equivalent class axiom contains A
 * as a 'top level' class (e.g.  EquivalentClasses(A, C, D)). For each class, D, that was made equivalent to A via
 * an equivalent classes axiom, a subclass axiom SubClassOf(A, D) will be added to the target ontology T.
 * <p/>
 * This change supports a common pattern of working, where a class is converted from a
 * defined class to a primitive class.
 */
public class ConvertEquivalentClassesToSuperClasses extends AbstractCompositeOntologyChange {

    private OWLOntology targetOntology;

    private OWLClass cls;

    private Set<OWLOntology> ontologies;

    private boolean splitIntersections;

    private List<OWLOntologyChange> changes;


    public ConvertEquivalentClassesToSuperClasses(OWLDataFactory dataFactory, OWLClass cls, Set<OWLOntology> ontologies,
                                                  OWLOntology targetOntology, boolean splitIntersections) {
        super(dataFactory);
        this.targetOntology = targetOntology;
        this.cls = cls;
        this.ontologies = ontologies;
        this.splitIntersections = splitIntersections;
        generateChanges();
    }


    private void generateChanges() {
        changes = new ArrayList<OWLOntologyChange>();
        Set<OWLDescription> supers = new HashSet<OWLDescription>();
        for (OWLOntology o : ontologies) {
            for (OWLEquivalentClassesAxiom ax : o.getEquivalentClassesAxioms(cls)) {
                changes.add(new RemoveAxiom(o, ax));
                for (OWLDescription equivCls : ax.getDescriptions()) {
                    supers.addAll(getDescriptions(equivCls));
                }
            }
        }
        supers.remove(cls);
        for (OWLDescription sup : supers) {
            changes.add(new AddAxiom(targetOntology, getDataFactory().getOWLSubClassAxiom(cls, sup)));
        }
    }


    private Set<OWLDescription> getDescriptions(OWLDescription desc) {
        final Set<OWLDescription> result = new HashSet<OWLDescription>();
        if (splitIntersections) {
            desc.accept(new OWLDescriptionVisitorAdapter() {

                public void visit(OWLObjectIntersectionOf desc) {
                    for (OWLDescription op : desc.getOperands()) {
                        result.add(op);
                    }
                }
            });
        }
        if (result.isEmpty()) {
            result.add(desc);
        }
        return result;
    }


    public List<OWLOntologyChange> getChanges() {
        return changes;
    }
}
