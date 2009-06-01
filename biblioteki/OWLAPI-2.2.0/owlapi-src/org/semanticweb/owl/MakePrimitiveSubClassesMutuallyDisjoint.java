package org.semanticweb.owl;

import org.semanticweb.owl.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
 * Date: 24-Jul-2007<br><br>
 * <p/>
 * For a given class, this composite change makes its told primitive subclasses mutually disjoint.
 * For example, if B, C, and D are primitive subclasses of A then this composite change will make
 * B, C, and D mutually disjoint.
 * <p/>
 * More formally, for a given class, A, and a set of ontologies, S, this method will obtain a
 * set of classes, G, where all classes in G are named and primitive.  Moreover, for any class, B in G,
 * some ontology O in S will contain an axiom, SubClassOf(B, A). All classes in G will be made
 * mutually disjoint by creating axiom(s) in a target ontology T.
 * <p/>
 * This composite change supports a common design pattern where primitive subclasses of a class
 * are made mutually disjoint.
 */
public class MakePrimitiveSubClassesMutuallyDisjoint extends AbstractCompositeOntologyChange {

    private OWLClass cls;

    private Set<OWLOntology> ontologies;

    private OWLOntology targetOntology;

    private boolean usePairwiseDisjointAxioms;

    private List<OWLOntologyChange> changes;


    public MakePrimitiveSubClassesMutuallyDisjoint(OWLDataFactory dataFactory, OWLClass cls,
                                                   Set<OWLOntology> ontologies, OWLOntology targetOntology) {
        this(dataFactory, cls, ontologies, targetOntology, false);
    }


    public MakePrimitiveSubClassesMutuallyDisjoint(OWLDataFactory dataFactory, OWLClass cls,
                                                   Set<OWLOntology> ontologies, OWLOntology targetOntology,
                                                   boolean usePairwiseDisjointAxioms) {
        super(dataFactory);
        this.cls = cls;
        this.ontologies = ontologies;
        this.targetOntology = targetOntology;
        this.usePairwiseDisjointAxioms = usePairwiseDisjointAxioms;
        generateChanges();
    }


    private void generateChanges() {
        changes = new ArrayList<OWLOntologyChange>();
        Set<OWLClass> subclasses = new HashSet<OWLClass>();
        for (OWLOntology ont : ontologies) {
            for (OWLSubClassAxiom ax : ont.getSubClassAxiomsForRHS(cls)) {
                if (!ax.getSubClass().isAnonymous()) {
                    if (!ax.getSubClass().asOWLClass().isDefined(ontologies)) {
                        subclasses.add(ax.getSuperClass().asOWLClass());
                    }
                }
            }
        }
        MakeClassesMutuallyDisjoint makeClassesMutuallyDisjoint = new MakeClassesMutuallyDisjoint(getDataFactory(),
                                                                                                  subclasses,
                                                                                                  usePairwiseDisjointAxioms,
                                                                                                  targetOntology);
        changes.addAll(makeClassesMutuallyDisjoint.getChanges());
    }


    public List<OWLOntologyChange> getChanges() {
        return changes;
    }
}
