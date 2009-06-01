package com.clarkparsia.explanation.util;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntityCollector;

import java.util.*;

/*
* Copyright (C) 2007, Clark & Parsia
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
 * <p/>
 * Description: This class tracks the usage of named entities in the imports
 * closure of a designated ontology. It only tracks the logical axioms in the
 * ontologies so an entity that is only used in annotations will be treated as
 * if it does not exist. This class listens to the changes broadcasted by the
 * ontology manager to update the usage statistics for entities automatically
 * when a change occurs in one of the ontologies.
 * <p/>
 * <bold>WARNING:</bold> This class is not appropriate for general use because
 * it does not distinguish multiple occurrences of the same axiom in separate
 * ontologies. If the exact same axiom exists in two separate ontologies of the
 * imports closure, removing the axiom from one ontology would not remove the
 * axiom from the imports closure. However, when such a change occurs the
 * DefinitionTracker will treat as if the axiom is removed completely from the
 * imports closure. This is not important for generating explanation because
 * when we remove an axiom (during multiple explanation generation or blackbox
 * explanation) we remove it from all the ontologies in the imports closure.
 * </p>
 * <p/>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p/>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 * @author Evren Sirin
 */
public class DefinitionTracker implements OWLOntologyChangeListener {

    /**
     * Mapping from entities to the number of axioms
     */
    private Map<OWLEntity, Integer> referenceCounts = new HashMap<OWLEntity, Integer>();

    private OWLEntityCollector entityCollector = new OWLEntityCollector();

    private Set<OWLOntology> ontologies = new HashSet<OWLOntology>();

    private Set<OWLLogicalAxiom> axioms = new HashSet<OWLLogicalAxiom>();

    private OWLOntologyManager manager;

    private Integer ONE = Integer.valueOf(1);


    public DefinitionTracker(OWLOntologyManager manager) {
        this.manager = manager;

        manager.addOntologyChangeListener(this);
    }


    private void addAxiom(OWLLogicalAxiom axiom) {
        if (axioms.add(axiom)) {
            for (OWLEntity entity : getEntities(axiom)) {
                Integer count = referenceCounts.get(entity);
                if (count == null)
                    count = ONE;
                else
                    count = count + 1;
                referenceCounts.put(entity, count);
            }
        }
    }


    private Set<OWLEntity> getEntities(OWLObject obj) {
        entityCollector.reset();
        obj.accept(entityCollector);
        return entityCollector.getObjects();
    }


    private void removeAxiom(OWLLogicalAxiom axiom) {
        if (axioms.remove(axiom)) {
            for (OWLEntity entity : getEntities(axiom)) {
                Integer count = referenceCounts.get(entity);
                if (count == 1)
                    referenceCounts.remove(entity);
                else
                    referenceCounts.put(entity, count - 1);
            }
        }
    }


    public void setOntology(OWLOntology ontology) {
        axioms.clear();
        referenceCounts.clear();

        for (OWLOntology ont : manager.getImportsClosure(ontology)) {
            ontologies.add(ont);
            for (OWLLogicalAxiom axiom : ont.getLogicalAxioms())
                addAxiom(axiom);
        }
    }


    /**
     * Checks if this entity is referred by a logical axiom in the imports
     * closure of the designated ontology.
     * @param entity entity we are searching for
     * @return <code>true</code> if there is at least one logical axiom in the
     *         imports closure of the given ontology that refers the given
     *         entity
     */
    public boolean isDefined(OWLEntity entity) {
        if (entity instanceof OWLClass) {
            OWLClass cls = (OWLClass) entity;
            if (cls.isOWLThing() || cls.isOWLNothing())
                return true;
        }

        return referenceCounts.containsKey(entity);
    }


    /**
     * Checks if all the entities referred in the given concept are also
     * referred by a logical axiom in the imports closure of the designated
     * ontology.
     * @param description description that contains the entities we are searching for
     * @return <code>true</code> if all the entities in the given description
     *         are referred by at least one logical axiom in the imports closure
     *         of the given ontology
     */
    public boolean isDefined(OWLDescription description) {
        for (OWLEntity entity : getEntities(description)) {
            if (!isDefined(entity))
                return false;
        }
        return true;
    }


    public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
        for (OWLOntologyChange change : changes) {
            if (!change.isAxiomChange() || !ontologies.contains(change.getOntology()))
                continue;

            OWLAxiom axiom = change.getAxiom();

            if (!axiom.isLogicalAxiom())
                continue;

            if (change instanceof AddAxiom)
                addAxiom((OWLLogicalAxiom) axiom);
            else if (change instanceof RemoveAxiom)
                removeAxiom((OWLLogicalAxiom) axiom);
            else
                throw new UnsupportedOperationException("Unrecognized axiom change: " + change);
        }
    }
}
