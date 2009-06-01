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
 * Date: 26-Oct-2006<br><br>
 */
public enum OWLRDFVocabulary {



    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // OWL Vocab
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    OWL_THING(Namespaces.OWL, "Thing"),

    OWL_NOTHING(Namespaces.OWL, "Nothing"),

    OWL_CLASS(Namespaces.OWL, "Class"),

    OWL_ONTOLOGY(Namespaces.OWL, "Ontology"),

    OWL_IMPORTS(Namespaces.OWL, "imports"),

    OWL_VERSION_INFO(Namespaces.OWL, "versionInfo"),

    OWL_EQUIVALENT_CLASS(Namespaces.OWL, "equivalentClass"),

    OWL_OBJECT_PROPERTY(Namespaces.OWL, "ObjectProperty"),

    OWL_DATA_PROPERTY(Namespaces.OWL, "DatatypeProperty"),

    OWL_FUNCTIONAL_PROPERTY(Namespaces.OWL, "FunctionalProperty"),

    OWL_INVERSE_FUNCTIONAL_PROPERTY(Namespaces.OWL, "InverseFunctionalProperty"),

    OWL_ANTI_SYMMETRIC_PROPERTY(Namespaces.OWL2, "AntisymmetricProperty"),

    OWL_SYMMETRIC_PROPERTY(Namespaces.OWL, "SymmetricProperty"),

    OWL_RESTRICTION(Namespaces.OWL, "Restriction"),

    OWL_DATA_RESTRICTION(Namespaces.OWL2, "DataRestriction"),

    OWL_OBJECT_RESTRICTION(Namespaces.OWL2, "ObjectRestriction"),

    OWL_ON_PROPERTY(Namespaces.OWL, "onProperty"),

    OWL_INTERSECTION_OF(Namespaces.OWL, "intersectionOf"),

    OWL_UNION_OF(Namespaces.OWL, "unionOf"),

    OWL_ALL_VALUES_FROM(Namespaces.OWL, "allValuesFrom"),

    OWL_SOME_VALUES_FROM(Namespaces.OWL, "someValuesFrom"),

    OWL_HAS_VALUE(Namespaces.OWL, "hasValue"),

    OWL_DISJOINT_WITH(Namespaces.OWL, "disjointWith"),

    OWL_ONE_OF(Namespaces.OWL, "oneOf"),

    OWL_SELF_RESTRICTION(Namespaces.OWL2, "SelfRestriction"),

    OWL_DISJOINT_UNION_OF(Namespaces.OWL2, "disjointUnionOf"),

    OWL_MIN_CARDINALITY(Namespaces.OWL, "minCardinality"),

    OWL_CARDINALITY(Namespaces.OWL, "cardinality"),

    OWL_ANNOTATION_PROPERTY(Namespaces.OWL, "AnnotationProperty"),

    OWL_DECLARED_AS(Namespaces.OWL2, "declaredAs"),

    OWL_INDIVIDUAL(Namespaces.OWL2, "Individual"),

    OWL_DATATYPE(Namespaces.OWL, "Datatype"),

    RDFS_SUBCLASS_OF(Namespaces.RDFS, "subClassOf"),

    RDFS_SUB_PROPERTY_OF(Namespaces.RDFS, "subPropertyOf"),

    RDF_TYPE(Namespaces.RDF, "type"),

    RDF_NIL(Namespaces.RDF, "nil"),

    RDF_REST(Namespaces.RDF, "rest"),

    RDF_FIRST(Namespaces.RDF, "first"),

    RDF_LIST(Namespaces.RDF, "List"),

    OWL_MAX_CARDINALITY(Namespaces.OWL, "maxCardinality"),

    OWL_NEGATIVE_OBJECT_PROPERTY_ASSERTION(
            Namespaces.OWL2, "NegativeObjectPropertyAssertion"),

    OWL_NEGATIVE_DATA_PROPERTY_ASSERTION(
            Namespaces.OWL2, "NegativeDataPropertyAssertion"),

    RDFS_LABEL(Namespaces.RDFS, "label"),

    RDFS_COMMENT(Namespaces.RDFS, "comment"),

    RDFS_SEE_ALSO(Namespaces.RDFS, "seeAlso"),

    RDFS_IS_DEFINED_BY(Namespaces.RDFS, "isDefinedBy"),

    RDFS_RESOURCE(Namespaces.RDFS, "Resource"),

    RDFS_LITERAL(Namespaces.RDFS, "Literal"),

    RDFS_DATATYPE(Namespaces.RDFS, "Datatype"),

    OWL_TRANSITIVE_PROPERTY(Namespaces.OWL, "TransitiveProperty"),

    OWL_REFLEXIVE_PROPERTY(Namespaces.OWL2, "ReflexiveProperty"),

    OWL_IRREFLEXIVE_PROPERTY(Namespaces.OWL2, "IrreflexiveProperty"),

    OWL_INVERSE_OF(Namespaces.OWL, "inverseOf"),

    OWL_COMPLEMENT_OF(Namespaces.OWL, "complementOf"),

    OWL_ALL_DIFFERENT(Namespaces.OWL, "AllDifferent"),

    OWL_DISTINCT_MEMBERS(Namespaces.OWL, "distinctMembers"),

    OWL_SAME_AS(Namespaces.OWL, "sameAs"),

    OWL_DIFFERENT_FROM(Namespaces.OWL, "differentFrom"),

    OWL_DEPRECATED_PROPERTY(Namespaces.OWL, "DeprecatedProperty"),

    OWL_EQUIVALENT_PROPERTY(Namespaces.OWL, "equivalentProperty"),

    OWL_DEPRECATED_CLASS(Namespaces.OWL, "DeprecatedClass"),

    OWL_DATA_RANGE(Namespaces.OWL, "DataRange"),

    RDFS_DOMAIN(Namespaces.RDFS, "domain"),

    RDFS_RANGE(Namespaces.RDFS, "range"),

    RDFS_CLASS(Namespaces.RDFS, "Class"),

    RDF_PROPERTY(Namespaces.RDF, "Property"),

    RDF_SUBJECT(Namespaces.RDF, "subject"),

    RDF_PREDICATE(Namespaces.RDF, "predicate"),

    RDF_OBJECT(Namespaces.RDF, "object"),

    RDF_DESCRIPTION(Namespaces.RDF, "Description"),

    OWL_PRIOR_VERSION(Namespaces.OWL, "priorVersion"),

    OWL_BACKWARD_COMPATIBLE_WITH(Namespaces.OWL, "backwardCompatibleWith"),

    OWL_INCOMPATIBLE_WITH(Namespaces.OWL, "incompatibleWith"),

    OWL_OBJECT_PROPERTY_DOMAIN(Namespaces.OWL2, "objectPropertyDomain"),

    OWL_DATA_PROPERTY_DOMAIN(Namespaces.OWL2, "dataPropertyDomain"),

    OWL_DATA_PROPERTY_RANGE(Namespaces.OWL2, "dataPropertyRange"),

    OWL_OBJECT_PROPERTY_RANGE(Namespaces.OWL2, "objectPropertyRange"),

    OWL_SUB_OBJECT_PROPERTY_OF(Namespaces.OWL2, "subObjectPropertyOf"),

    OWL_SUB_DATA_PROPERTY_OF(Namespaces.OWL2, "subDataPropertyOf"),

    OWL_DISJOINT_DATA_PROPERTIES(Namespaces.OWL2, "disjointDataProperties"),

    OWL_DISJOINT_OBJECT_PROPERTIES(Namespaces.OWL2, "disjointObjectProperties"),

    OWL_EQUIVALENT_DATA_PROPERTIES(Namespaces.OWL2, "equivalentDataProperty"),

    OWL_EQUIVALENT_OBJECT_PROPERTIES(Namespaces.OWL2, "equivalentObjectProperty"),

    OWL_FUNCTIONAL_DATA_PROPERTY(Namespaces.OWL2, "FunctionalDataProperty"),

    OWL_FUNCTIONAL_OBJECT_PROPERTY(Namespaces.OWL2, "FunctionalObjectProperty"),

    OWL_ON_CLASS(Namespaces.OWL2, "onClass"),

    OWL_ON_DATA_RANGE(Namespaces.OWL2, "onDataRange"),

    OWL_INVERSE_OBJECT_PROPERTY_EXPRESSION(Namespaces.OWL2, "inverseObjectPropertyExpression"),

    OWL_AXIOM(Namespaces.OWL2, "Axiom"),

    OWL_PROPERTY_CHAIN(Namespaces.OWL2, "propertyChain"),

    OWL_ALL_DISJOINT_CLASSES(Namespaces.OWL2, "AllDisjointClasses"),

    OWL_MEMBERS(Namespaces.OWL2, "members"),

    OWL_PROPERTY_DISJOINT_WITH(Namespaces.OWL2, "propertyDisjointWith"),

    OWL_ALL_DISJOINT_PROPERTIES(Namespaces.OWL2, "AllDisjointProperties");



    URI uri;

    Namespaces namespace;

    String shortName;


    OWLRDFVocabulary(Namespaces namespace, String shortName) {
        this.namespace = namespace;
        this.shortName = shortName;
        this.uri = URI.create(namespace.toString() + shortName);
    }

    public URI getURI() {
        return uri;
    }


    public Namespaces getNamespace() {
        return namespace;
    }


    public String getShortName() {
        return shortName;
    }


    public static Set<URI> BUILT_IN_VOCABULARY;

    static {
        BUILT_IN_VOCABULARY = new HashSet<URI>();
        for(OWLRDFVocabulary v : OWLRDFVocabulary.values()) {
            BUILT_IN_VOCABULARY.add(v.getURI());
        }
    }

    public static Set<URI> BUILT_IN_ANNOTATION_PROPERTIES;

    static {
        BUILT_IN_ANNOTATION_PROPERTIES = new HashSet<URI>();
        BUILT_IN_ANNOTATION_PROPERTIES.add(RDFS_LABEL.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(RDFS_COMMENT.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(OWL_VERSION_INFO.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(OWL_BACKWARD_COMPATIBLE_WITH.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(OWL_PRIOR_VERSION.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(RDFS_SEE_ALSO.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(RDFS_IS_DEFINED_BY.getURI());
        BUILT_IN_ANNOTATION_PROPERTIES.add(OWL_INCOMPATIBLE_WITH.getURI());
    }


    public String toString() {
        return uri.toString();
    }
}
