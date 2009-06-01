package org.semanticweb.owl.util;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;

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
 * Date: 16-Feb-2007<br><br>
 * <p/>
 * A utility class which checks if a class expression has a named conjunct or
 * a specific named conjunct.
 */
public class NamedConjunctChecker {

    private OWLClass conjunct;

    private boolean found;

    private boolean collect;

    private Set<OWLClass> conjuncts;

    private NamedConjunctCheckerVisitor visitor;


    public NamedConjunctChecker() {
        visitor = new NamedConjunctCheckerVisitor();
        conjuncts = new HashSet<OWLClass>();
    }


    /**
     * Checks whether a named class is a conjunct in a given class description.
     * For class expressions which aren't named classes or object intersections this
     * method will always return false.
     * @param conjunct    The conjunct to check for
     * @param description The description to be checked
     */
    public boolean isNamedConjunct(OWLClass conjunct, OWLDescription description) {
        reset();
        this.conjunct = conjunct;
        description.accept(visitor);
        return found;
    }


    /**
     * Checks whether the specified description has a named conjunct.  For
     * For class expressions which aren't named classes or object intersections this
     * method will always return false.
     * @param description The description to be checked.
     * @return <code>true</code> if the description is in fact a named class (<code>OWLClass</code>)
     *         or if the description is an intersection that has a named operand (included nested intersections),
     *         otherwise <code>false</code>
     */
    public boolean hasNamedConjunct(OWLDescription description) {
        reset();
        conjunct = null;
        description.accept(visitor);
        return found;
    }


    private void reset() {
        found = false;
        collect = false;
    }


    /**
     * Gets the named conjuncts for the specified description.
     * @param description The description whose conjuncts are to be retrieved.
     * @return A set containing the named conjuncts of the specified description.  If
     *         the description is not a named class or an intersection then the set will
     *         definitely be empty.
     */
    public Set<OWLClass> getNamedConjuncts(OWLDescription description) {
        conjuncts.clear();
        reset();
        collect = true;
        description.accept(visitor);
        return conjuncts;
    }


    private class NamedConjunctCheckerVisitor extends OWLDescriptionVisitorAdapter {

        public void visit(OWLClass desc) {
            if (conjunct == null) {
                found = true;
                if (collect) {
                    conjuncts.add(desc);
                }
            }
            else if (desc.equals(conjunct)) {
                found = true;
                if (collect) {
                    conjuncts.add(desc);
                }
            }
        }


        public void visit(OWLObjectIntersectionOf desc) {
            for (OWLDescription op : desc.getOperands()) {
                op.accept(this);
                // Early termination if we have found a named conjunct
                // and we don't need to collect
                if (found && !collect) {
                    break;
                }
            }
        }
    }
}
