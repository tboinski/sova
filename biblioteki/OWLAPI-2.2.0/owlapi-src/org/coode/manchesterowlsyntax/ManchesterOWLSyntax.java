package org.coode.manchesterowlsyntax;
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
 * Date: 25-Apr-2007<br><br>
 *
 * The vocabulary that the Manchester OWL Syntax uses
 */
public enum ManchesterOWLSyntax {

    ONTOLOGY("Ontology", false),

    IMPORTS("Imports", true),

    NAMESPACE("Namespace", false),

    CLASS("Class", true),

    OBJECT_PROPERTY("ObjectProperty", true),

    DATA_PROPERTY("DataProperty", true),

    INDIVIDUAL("Individual", true),

    SOME("some", false),

    ONLY("only", false),

    ONLYSOME("onlysome", false),

    MIN("min", false),

    MAX("max", false),

    EXACTLY("exactly", false),

    VALUE("value", false),

    AND("and", false),

    THAT("that", false),

    OR("or", false),

    NOT("not", false),

    INVERSE("inv", false),

    SELF("Self", false),

    FACET_RESTRICTION_SEPARATOR(",", false),

    SUBCLASS_OF("SubClassOf", true),

    EQUIVALENT_TO("EquivalentTo", true),

    DISJOINT_WITH("DisjointWith", true),

    DISJOINT_UNION_OF("DisjointUnionOf", true),

    FACTS("Facts", true),

    SAME_AS("SameAs", true),

    DIFFERENT_FROM("DifferentFrom", true),

    MIN_INCLUSIVE_FACET(">=", false),

    MAX_INCLUSIVE_FACET("<=", false),

    MIN_EXCLUSIVE_FACET(">", false),

    MAX_EXCLUSIVE_FACET("<", false),

    ONE_OF_DELIMETER(",", false),

    TYPES("Types", true),

    ANNOTATIONS("Annotations", true),

    COMMA(",", false),

    DOMAIN("Domain", true),

    RANGE("Range", true),

    CHARACTERISTICS("Characteristics", true),

    FUNCTIONAL("Functional", false),

    INVERSE_FUNCTIONAL("InverseFunctional", false),

    SYMMETRIC("Symmetric", false),

    TRANSITIVE("Transitive", false),

    REFLEXIVE("Reflexive", false),

    IRREFLEXIVE("Irreflexive", false),

    ANTI_SYMMETRIC("AntiSymmetric", false),

    INVERSE_OF("InverseOf", false),

    SUB_PROPERTY_OF("SubPropertyOf", false),


    ;

    private boolean sectionKeyword;

    private String rendering;

    private ManchesterOWLSyntax(String rendering, boolean sectionKeyword) {
        this.rendering = rendering;
        this.sectionKeyword = sectionKeyword;
    }


    public boolean isSectionKeyword() {
        return sectionKeyword;
    }


    public String toString() {
        return rendering;
    }
}
