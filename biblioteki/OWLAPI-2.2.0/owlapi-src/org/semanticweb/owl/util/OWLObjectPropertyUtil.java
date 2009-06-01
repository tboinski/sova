package org.semanticweb.owl.util;

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
 * Date: 25-Feb-2008<br><br>
 */
public class OWLObjectPropertyUtil {

    private OWLDataFactory dataFactory;

    private OWLOntologySetProvider setProvider;


    public OWLObjectPropertyUtil(OWLDataFactory dataFactory, OWLOntologySetProvider ontologySetProvider) {
        this.dataFactory = dataFactory;
        this.setProvider = ontologySetProvider;
    }


    protected Set<OWLOntology> getOntologies() {
        return setProvider.getOntologies();
    }


    /**
     * The relation →* is the reflexive-transitive closure of →.
     * An object property expression PE is simple in Ax if,
     * for each object property expression PE' such that PE' →* PE holds, PE' is not composite.
     * @param expression The expression to be tested.
     * @return <code>true</code> if the object property expression is simple, otherwise false.
     */
    public boolean isSimple(OWLObjectPropertyExpression expression) {
        
        return false;
    }

    public Set<OWLObjectPropertyExpressionPair> getReflexiveTransitiveClosure(Set<OWLObjectPropertyExpressionPair> pairs) {
        return null;
    }
    


    /**
     * The object property hierarchy relation → is the smallest relation on object property expressions
     * for which the following conditions hold (A → B means that → holds for A and B):
     * if Ax contains an axiom SubObjectPropertyOf(PE1 PE2), then PE1 → PE2 holds; and
     * if Ax contains an axiom EquivalentObjectProperties(PE1 PE2), then PE1 → PE2 and PE2 → PE1 hold; and
     * if Ax contains an axiom InverseObjectProperties(PE1 PE2), then PE1 → INV(PE2) and INV(PE2) → PE1 hold; and
     * if Ax contains an axiom SymmetricObjectProperty(PE), then PE → INV(PE) holds; and
     * if PE1 → PE2 holds, then INV(PE1) → INV(PE2) holds as well.
     */
    public Set<OWLObjectPropertyExpressionPair> getObjectPropertyHierarchyRelations() {
        Set<OWLObjectPropertyExpressionPair> pairs = new HashSet<OWLObjectPropertyExpressionPair>();
        for (OWLOntology ont : getOntologies()) {
            for (OWLObjectSubPropertyAxiom ax : ont.getAxioms(AxiomType.SUB_OBJECT_PROPERTY)) {
                addPairs(pairs, ax.getSubProperty(), ax.getSuperProperty());
            }
            for (OWLEquivalentObjectPropertiesAxiom ax : ont.getAxioms(AxiomType.EQUIVALENT_OBJECT_PROPERTIES)) {
                List<OWLObjectPropertyExpression> props = new ArrayList<OWLObjectPropertyExpression>(ax.getProperties());
                for (int i = 0; i < props.size() - 1; i++) {
                    for (int j = i + 1; j < props.size(); j++) {
                        OWLObjectPropertyExpression propA = props.get(i);
                        OWLObjectPropertyExpression propB = props.get(j);
                        addPairs(pairs, propA, propB);
                        addPairs(pairs, propB, propA);
                    }
                }
            }
            for (OWLInverseObjectPropertiesAxiom ax : ont.getAxioms(AxiomType.INVERSE_OBJECT_PROPERTIES)) {
                addPairs(pairs, ax.getFirstProperty(), getInverse(ax.getSecondProperty()));
                addPairs(pairs, getInverse(ax.getSecondProperty()), ax.getFirstProperty());
            }
            for (OWLSymmetricObjectPropertyAxiom ax : ont.getAxioms(AxiomType.SYMMETRIC_OBJECT_PROPERTY)) {
                addPairs(pairs, ax.getProperty(), getInverse(ax.getProperty()));
            }
        }
        return pairs;
    }


    private void addPairs(Set<OWLObjectPropertyExpressionPair> pairs, OWLObjectPropertyExpression left,
                          OWLObjectPropertyExpression right) {
        OWLObjectPropertyExpression simpLeft = getSimplifiedPropertyExpression(left);
        OWLObjectPropertyExpression simpRight = getSimplifiedPropertyExpression(right);
        pairs.add(new OWLObjectPropertyExpressionPair(simpLeft, simpRight));
        pairs.add(new OWLObjectPropertyExpressionPair(getInverse(simpLeft), getInverse(simpRight)));
    }


    /**
     * An object property expression PE is composite in Ax if Ax contains an axiom of the form
     * SubObjectPropertyOf(SubObjectPropertyChain(PE1 ... PEn) PE) with n > 1, or
     * SubObjectPropertyOf(SubObjectPropertyChain(PE1 ... PEn) INV(PE)) with n > 1, or
     * TransitiveObjectProperty(PE), or
     * TransitiveObjectProperty(INV(PE)).
     * @param expression The object property expression to be tested
     * @return <code>true</code> if the object property is composite (according to the above definition)
     *         or <code>false</code> if the object property is not composite.
     */
    public boolean isComposite(OWLObjectPropertyExpression expression) {
        OWLObjectPropertyExpression simpProp = getSimplifiedPropertyExpression(expression);
        OWLObjectPropertyExpression simpPropInv;
        if (simpProp.isAnonymous()) {
            simpPropInv = ((OWLObjectPropertyInverse) simpProp).getInverse();
        }
        else {
            simpPropInv = getInverse(simpProp);
        }

        for (OWLOntology ont : getOntologies()) {
            for (OWLObjectPropertyChainSubPropertyAxiom ax : ont.getPropertyChainSubPropertyAxioms()) {
                if (ax.getSuperProperty().equals(simpProp) || ax.getSuperProperty().equals(simpPropInv)) {
                    return true;
                }
            }
            if (ont.getTransitiveObjectPropertyAxiom(simpProp) != null) {
                return true;
            }
            if (ont.getTransitiveObjectPropertyAxiom(simpPropInv) != null) {
                return true;
            }
        }


        return false;
    }


    /**
     * Obtains the simplified form of a property expression.
     * Let P be a named property
     * If:
     * <ul>
     * <li>prop = P then the returned value is P</li>
     * <li>prop = INV(P) then the returned value is INV(P)</li>
     * <li>prop = INV(INV(P)) then the returned value is P</li>
     * </ul>
     * @param prop The property to be simplified.
     * @return For a given property, P, and an expression involving P, then
     *         the return value is either P or INV(P).
     */
    public OWLObjectPropertyExpression getSimplifiedPropertyExpression(OWLObjectPropertyExpression prop) {
        if (!prop.isAnonymous()) {
            return prop;
        }
        OWLObjectPropertyExpression invProp = ((OWLObjectPropertyInverse) prop).getInverse();
        if (invProp.isAnonymous()) {
            OWLObjectPropertyExpression simplifiedProp = ((OWLObjectPropertyInverse) invProp).getInverse();
            return getSimplifiedPropertyExpression(simplifiedProp);
        }
        else {
            return prop;
        }
    }


    /**
     * Gets the inverse of the specified property
     * @param prop The property whose inverse is to be obtained.
     * @return The inverse of the specified property.  If prop is named then
     *         this will be INV(prop).  If prop is anonymous i.e. INV(prop) then prop
     *         will be returned.
     */
    public OWLObjectPropertyExpression getInverse(OWLObjectPropertyExpression prop) {
        if (prop.isAnonymous()) {
            return ((OWLObjectPropertyInverse) prop).getInverse();
        }
        else {
            return dataFactory.getOWLObjectPropertyInverse(prop);
        }
    }

    public static class OWLObjectPropertyExpressionPair {

        private OWLObjectPropertyExpression left;

        private OWLObjectPropertyExpression right;


        public OWLObjectPropertyExpressionPair(OWLObjectPropertyExpression left, OWLObjectPropertyExpression right) {
            this.left = left;
            this.right = right;
        }


        public OWLObjectPropertyExpression getLeft() {
            return left;
        }


        public OWLObjectPropertyExpression getRight() {
            return right;
        }


        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OWLObjectPropertyExpressionPair)) {
                return false;
            }
            OWLObjectPropertyExpressionPair other = (OWLObjectPropertyExpressionPair) obj;
            return this.left.equals(other.left) && this.right.equals(other.right);
        }


        public int hashCode() {
            int hashCode = 37;
            hashCode = hashCode * 37 + left.hashCode();
            hashCode = hashCode * 37 + right.hashCode();
            return hashCode;
        }
    }
}
