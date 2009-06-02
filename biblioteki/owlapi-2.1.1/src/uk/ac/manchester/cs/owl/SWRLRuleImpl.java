package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.*;

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
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 15-Jan-2007<br><br>
 */
public class SWRLRuleImpl extends OWLAxiomImpl implements SWRLRule {

    private URI uri;

    private Set<SWRLAtom> consequent;

    private Set<SWRLAtom> antecedent;

    private boolean anon;


    public SWRLRuleImpl(OWLDataFactory dataFactory, URI uri, Set<? extends SWRLAtom> antecedent, Set<? extends SWRLAtom> consequent) {
        super(dataFactory);
        this.uri = uri;
        anon = false;
        this.consequent = new HashSet<SWRLAtom>(consequent);
        this.antecedent = new HashSet<SWRLAtom>(antecedent);
    }

    public SWRLRuleImpl(OWLDataFactory dataFactory, Set<? extends SWRLAtom> antecedent, Set<? extends SWRLAtom> consequent) {
        super(dataFactory);
        this.uri = URI.create("http://www.semanticweb.org/swrl#" + System.nanoTime());
        anon = true;
        this.consequent = new HashSet<SWRLAtom>(consequent);
        this.antecedent = new HashSet<SWRLAtom>(antecedent);
    }

    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(SWRLObjectVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * Determines if this rule is anonymous.  Rules may be named
     * using URIs.
     * @return <code>true</code> if this rule is anonymous and therefore
     *         doesn't have a URI.
     */
    public boolean isAnonymous() {
        return anon;
    }


    /**
     * Gets the atoms in the antecedent
     * @return A set of <code>SWRLAtom</code>s, which represent the atoms
     *         in the antecedent of the rule.
     */
    public Set<SWRLAtom> getBody() {
        return Collections.unmodifiableSet(antecedent);
    }


    /**
     * Gets the atoms in the consequent.
     * @return A set of <code>SWRLAtom</code>s, which represent the atoms
     *         in the consequent of the rule
     */
    public Set<SWRLAtom> getHead() {
        return Collections.unmodifiableSet(consequent);
    }


    public void accept(OWLAxiomVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * Determines if this axiom is a logical axiom. Logical axioms are defined to be
     * axioms other than declaration axioms (including imports declarations) and annotation
     * axioms.
     * @return <code>true</code> if the axiom is a logical axiom, <code>false</code>
     *         if the axiom is not a logical axiom.
     */
    public boolean isLogicalAxiom() {
        return true;
    }


    /**
     * Gets the name of this object.
     * @return A <code>URI</code> that represents the name
     *         of the object
     */
    public URI getURI() {
        return uri;
    }


    public boolean equals(Object obj) {
            if(!(obj instanceof SWRLRule)) {
                return false;
            }
            SWRLRule other = (SWRLRule) obj;
            return other.getURI().equals(uri) &&
                    other.getBody().equals(antecedent) &&
                    other.getHead().equals(consequent);
    }
}
