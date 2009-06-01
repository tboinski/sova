package org.semanticweb.owl.vocab;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
/*
 * Copyright (C) 2006, University of Manchester
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
 * Date: 12-Dec-2006<br><br>
 */
public enum OWLXMLVocabulary {

    CLASS("OWLClass"),

    DATA_PROPERTY("DataProperty"),

    OBJECT_PROPERTY("ObjectProperty"),

    INDIVIDUAL("Individual"),

    DATATYPE("Datatype"),

    ANNOTATION("Annotation"),

    ANNOTATION_URI("annotationURI"),

    IMPORTS("Imports"),

    LABEL("Label"),

    COMMENT("Comment"),

    DOCUMENTATION("Documentation"),

    ONTOLOGY("Ontology"),

    CONSTANT("Constant"),

    INVERSE_OBJECT_PROPERTY("InverseObjectProperty"),

    DATA_COMPLEMENT_OF("DataComplementOf"),

    DATA_ONE_OF("DataOneOf"),

    DATATYPE_RESTRICTION("DatatypeRestriction"),

    DATATYPE_FACET_RESTRICTION("DatatypeFacetRestriction"),

    DATATYPE_FACET("facet"),

    DATATYPE_URI("datatypeURI"),

    DATA_RANGE("DataRange"),

    OBJECT_INTERSECTION_OF("ObjectIntersectionOf"),

    OBJECT_UNION_OF("ObjectUnionOf"),

    OBJECT_COMPLEMENT_OF("ObjectComplementOf"),

    OBJECT_ONE_OF("ObjectOneOf"),

    OBJECT_SOME_VALUES_FROM("ObjectSomeValuesFrom"),

    OBJECT_ALL_VALUES_FROM("ObjectAllValuesFrom"),

    OBJECT_EXISTS_SELF("ObjectExistsSelf"),

    OBJECT_HAS_VALUE("ObjectHasValue"),

    OBJECT_MIN_CARDINALITY("ObjectMinCardinality"),

    OBJECT_EXACT_CARDINALITY("ObjectExactCardinality"),

    OBJECT_MAX_CARDINALITY("ObjectMaxCardinality"),

    DATA_SOME_VALUES_FROM("DataSomeValuesFrom"),

    DATA_ALL_VALUES_FROM("DataAllValuesFrom"),

    DATA_HAS_VALUE("DataHasValue"),

    DATA_MIN_CARDINALITY("DataMinCardinality"),

    DATA_EXACT_CARDINALITY("DataExactCardinality"),

    DATA_MAX_CARDINALITY("DataMaxCardinality"),

    SUB_CLASS_OF("SubClassOf"),

    EQUIVALENT_CLASSES("EquivalentClasses"),

    DISJOINT_CLASSES("DisjointClasses"),

    DISJOINT_UNION("DisjointUnion"),

    UNION_OF("UnionOf"),

    SUB_OBJECT_PROPERTY_OF("SubObjectPropertyOf"),

    SUB_OBJECT_PROPERTY_CHAIN("SubObjectPropertyChain"),

    EQUIVALENT_OBJECT_PROPERTIES("EquivalentObjectProperties"),

    DISJOINT_OBJECT_PROPERTIES("DisjointObjectProperties"),

    OBJECT_PROPERTY_DOMAIN("ObjectPropertyDomain"),

    OBJECT_PROPERTY_RANGE("ObjectPropertyRange"),

    INVERSE_OBJECT_PROPERTIES("InverseObjectProperties"),

    FUNCTIONAL_OBJECT_PROPERTY("FunctionalObjectProperty"),

    INVERSE_FUNCTIONAL_OBJECT_PROPERTY("InverseFunctionalObjectProperty"),

    SYMMETRIC_OBJECT_PROPERTY("SymmetricObjectProperty"),

    ANTI_SYMMETRIC_OBJECT_PROPERTY("AntisymmetricObjectProperty"),

    REFLEXIVE_OBJECT_PROPERTY("ReflexiveObjectProperty"),

    IRREFLEXIVE_OBJECT_PROPERTY("IrreflexiveObjectProperty"),

    TRANSITIVE_OBJECT_PROPERTY("TransitiveObjectProperty"),

    SUB_DATA_PROPERTY_OF("SubDataPropertyOf"),

    EQUIVALENT_DATA_PROPERTIES("EquivalentDataProperties"),

    DISJOINT_DATA_PROPERTIES("DisjointDataProperties"),

    DATA_PROPERTY_DOMAIN("DataPropertyDomain"),

    DATA_PROPERTY_RANGE("DataPropertyRange"),

    FUNCTIONAL_DATA_PROPERTY("FunctionalDataProperty"),

    SAME_INDIVIDUALS("SameIndividuals"),

    DIFFERENT_INDIVIDUALS("DifferentIndividuals"),

    CLASS_ASSERTION("ClassAssertion"),

    OBJECT_PROPERTY_ASSERTION("ObjectPropertyAssertion"),

    DATA_PROPERTY_ASSERTION("DataPropertyAssertion"),

    NEGATIVE_OBJECT_PROPERTY_ASSERTION("NegativeObjectPropertyAssertion"),

    NEGATIVE_DATA_PROPERTY_ASSERTION("NegativeDataPropertyAssertion"),

    DECLARATION("Declaration"),

    ENTITY_ANNOTATION("EntityAnnotation");

    private URI uri;

    private String shortName;


    OWLXMLVocabulary(String name) {
        this.uri = URI.create(Namespaces.OWL2XML + name);
        shortName = name;
    }


    public URI getURI() {
        return uri;
    }


    public String getShortName() {
        return shortName;
    }


    public String toString() {
        return uri.toString();
    }


    static Set<URI> BUILT_IN_URIS;


    static {
        BUILT_IN_URIS = new HashSet<URI>();
        for (OWLRDFVocabulary v : OWLRDFVocabulary.values()) {
            BUILT_IN_URIS.add(v.getURI());
        }
    }
}
