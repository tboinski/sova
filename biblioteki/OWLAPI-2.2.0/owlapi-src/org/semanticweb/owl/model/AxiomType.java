package org.semanticweb.owl.model;

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
 * Date: 27-Jul-2007<br><br>
 * <p/>
 * Represents the type of axioms which can belong to ontologies
 */
public class AxiomType<C extends OWLAxiom> {

    private String name;

    private boolean owl11Axiom;

    private boolean nonSyntacticOWL11Axiom;

    final public int index;

    public static Set<AxiomType> AXIOM_TYPES;

    private static int count = 0;


    private AxiomType(String name, boolean owl11Axiom, boolean nonSyntacticOWL11Axiom) {
        this.name = name;
        this.owl11Axiom = owl11Axiom;
        this.nonSyntacticOWL11Axiom = nonSyntacticOWL11Axiom;
        index = count;
        count++;
    }


    public String toString() {
        return name + " axiom";
    }


    public boolean isOWL11Axiom() {
        return owl11Axiom;
    }


    public boolean isNonSyntacticOWL11Axiom() {
        return nonSyntacticOWL11Axiom;
    }


    public int getIndex() {
        return index;
    }


    public String getName() {
        return name;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Class axioms
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final AxiomType<OWLEquivalentClassesAxiom> EQUIVALENT_CLASSES = new AxiomType<OWLEquivalentClassesAxiom>(
            "Equivalent classes",
            false,
            false);

    public static final AxiomType<OWLSubClassAxiom> SUBCLASS = new AxiomType<OWLSubClassAxiom>("SubClass",
                                                                                                   false,
                                                                                                   false);

    
    public static final AxiomType<OWLDisjointClassesAxiom> DISJOINT_CLASSES = new AxiomType<OWLDisjointClassesAxiom>(
            "Disjoint classes",
            false,
            false);


    public static final AxiomType<OWLDisjointUnionAxiom> DISJOINT_UNION = new AxiomType<OWLDisjointUnionAxiom>(
            "Disjoint union",
            true,
            false
    );


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Individual axioms
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    public static final AxiomType<OWLClassAssertionAxiom> CLASS_ASSERTION = new AxiomType<OWLClassAssertionAxiom>(
            "Class assertion",
            false,
            false);

    public static final AxiomType<OWLSameIndividualsAxiom> SAME_INDIVIDUAL = new AxiomType<OWLSameIndividualsAxiom>(
            "Same individual",
            false,
            false);

    public static final AxiomType<OWLDifferentIndividualsAxiom> DIFFERENT_INDIVIDUALS = new AxiomType<OWLDifferentIndividualsAxiom>(
            "Different individuals",
            false,
            false);

    public static final AxiomType<OWLObjectPropertyAssertionAxiom> OBJECT_PROPERTY_ASSERTION = new AxiomType<OWLObjectPropertyAssertionAxiom>(
            "Object property assertion",
            false,
            false);

    public static final AxiomType<OWLNegativeObjectPropertyAssertionAxiom> NEGATIVE_OBJECT_PROPERTY_ASSERTION = new AxiomType<OWLNegativeObjectPropertyAssertionAxiom>(
            "Negative object property assertion",
            true,
            false);

    public static final AxiomType<OWLDataPropertyAssertionAxiom> DATA_PROPERTY_ASSERTION = new AxiomType<OWLDataPropertyAssertionAxiom>(
            "Data property assertion",
            false,
            false);

    public static final AxiomType<OWLNegativeDataPropertyAssertionAxiom> NEGATIVE_DATA_PROPERTY_ASSERTION = new AxiomType<OWLNegativeDataPropertyAssertionAxiom>(
            "Negative data property assertion",
            true,
            false);

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Object property axioms
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final AxiomType<OWLEquivalentObjectPropertiesAxiom> EQUIVALENT_OBJECT_PROPERTIES = new AxiomType<OWLEquivalentObjectPropertiesAxiom>(
            "Equivalent object properties",
            false,
            false);

    public static final AxiomType<OWLObjectSubPropertyAxiom> SUB_OBJECT_PROPERTY = new AxiomType<OWLObjectSubPropertyAxiom>(
            "Sub object property",
            false,
            false);

    public static final AxiomType<OWLInverseObjectPropertiesAxiom> INVERSE_OBJECT_PROPERTIES = new AxiomType<OWLInverseObjectPropertiesAxiom>(
            "Inverse object properties",
            false,
            false);

    public static final AxiomType<OWLFunctionalObjectPropertyAxiom> FUNCTIONAL_OBJECT_PROPERTY = new AxiomType<OWLFunctionalObjectPropertyAxiom>(
            "Functional object property",
            false,
            false);

    public static final AxiomType<OWLInverseFunctionalObjectPropertyAxiom> INVERSE_FUNCTIONAL_OBJECT_PROPERTY = new AxiomType<OWLInverseFunctionalObjectPropertyAxiom>(
            "Inverse functional object property",
            false,
            false);

    public static final AxiomType<OWLSymmetricObjectPropertyAxiom> SYMMETRIC_OBJECT_PROPERTY = new AxiomType<OWLSymmetricObjectPropertyAxiom>(
            "Symmetric object property",
            false,
            false);

    public static final AxiomType<OWLAntiSymmetricObjectPropertyAxiom> ANTI_SYMMETRIC_OBJECT_PROPERTY = new AxiomType<OWLAntiSymmetricObjectPropertyAxiom>(
            "Anti-symmetric object property",
            true,
            true);

    public static final AxiomType<OWLTransitiveObjectPropertyAxiom> TRANSITIVE_OBJECT_PROPERTY = new AxiomType<OWLTransitiveObjectPropertyAxiom>(
            "Transitive object property",
            false,
            false);

    public static final AxiomType<OWLReflexiveObjectPropertyAxiom> REFLEXIVE_OBJECT_PROPERTY = new AxiomType<OWLReflexiveObjectPropertyAxiom>(
            "Reflecive object property",
            true,
            true);

    public static final AxiomType<OWLIrreflexiveObjectPropertyAxiom> IRREFLEXIVE_OBJECT_PROPERTY = new AxiomType<OWLIrreflexiveObjectPropertyAxiom>(
            "Irrefexive object property",
            true,
            true);

    public static final AxiomType<OWLObjectPropertyDomainAxiom> OBJECT_PROPERTY_DOMAIN = new AxiomType<OWLObjectPropertyDomainAxiom>(
            "Object property domain",
            false,
            false);

    public static final AxiomType<OWLObjectPropertyRangeAxiom> OBJECT_PROPERTY_RANGE = new AxiomType<OWLObjectPropertyRangeAxiom>(
            "Object property range",
            false,
            false);

    public static final AxiomType<OWLDisjointObjectPropertiesAxiom> DISJOINT_OBJECT_PROPERTIES = new AxiomType<OWLDisjointObjectPropertiesAxiom>(
            "Disjoint object properties",
            true,
            true);

    public static final AxiomType<OWLObjectPropertyChainSubPropertyAxiom> PROPERTY_CHAIN_SUB_PROPERTY = new AxiomType<OWLObjectPropertyChainSubPropertyAxiom>(
            "Object property chain subproperty",
            true,
            true);



    public static final AxiomType<OWLEquivalentDataPropertiesAxiom> EQUIVALENT_DATA_PROPERTIES = new AxiomType<OWLEquivalentDataPropertiesAxiom>(
            "Equivalent data properties",
            false,
            false);

    public static final AxiomType<OWLDataSubPropertyAxiom> SUB_DATA_PROPERTY = new AxiomType<OWLDataSubPropertyAxiom>(
            "Sub data property",
            false,
            false);

    public static final AxiomType<OWLFunctionalDataPropertyAxiom> FUNCTIONAL_DATA_PROPERTY = new AxiomType<OWLFunctionalDataPropertyAxiom>(
            "Functional data property",
            false,
            false);

    public static final AxiomType<OWLDataPropertyDomainAxiom> DATA_PROPERTY_DOMAIN = new AxiomType<OWLDataPropertyDomainAxiom>(
            "Data property domain",
            false,
            false);

    public static final AxiomType<OWLDataPropertyRangeAxiom> DATA_PROPERTY_RANGE = new AxiomType<OWLDataPropertyRangeAxiom>(
            "Data property range",
            false,
            false);

    public static final AxiomType<OWLDisjointDataPropertiesAxiom> DISJOINT_DATA_PROPERTIES = new AxiomType<OWLDisjointDataPropertiesAxiom>(
            "Disjoint data properties",
            true,
            true);



    public static final AxiomType<OWLEntityAnnotationAxiom> ENTITY_ANNOTATION = new AxiomType<OWLEntityAnnotationAxiom>(
            "Entity annotation",
            false,
            false);

    public static final AxiomType<OWLAxiomAnnotationAxiom> AXIOM_ANNOTATION = new AxiomType<OWLAxiomAnnotationAxiom>(
            "Axiom annotation",
            true,
            true);

    public static final AxiomType<OWLImportsDeclaration> IMPORTS_DECLARATION = new AxiomType<OWLImportsDeclaration>(
            "Imports declaration",
            false,
            false
    );


    public static final AxiomType<OWLDeclarationAxiom> DECLARATION = new AxiomType<OWLDeclarationAxiom>(
            "Declaration",
            true,
            true
    );

    public static final AxiomType<SWRLRule> SWRL_RULE = new AxiomType<SWRLRule>(
            "SWRL rule",
            false,
            false
    );

    public static final AxiomType<OWLOntologyAnnotationAxiom> ONTOLOGY_ANNOTATION = new AxiomType<OWLOntologyAnnotationAxiom>(
            "Ontology annotation",
            false,
            false
    );


    static {
        AXIOM_TYPES = new HashSet<AxiomType>();
        AXIOM_TYPES.add(SUBCLASS);
        AXIOM_TYPES.add(EQUIVALENT_CLASSES);
        AXIOM_TYPES.add(DISJOINT_CLASSES);
        AXIOM_TYPES.add(CLASS_ASSERTION);
        AXIOM_TYPES.add(SAME_INDIVIDUAL);
        AXIOM_TYPES.add(DIFFERENT_INDIVIDUALS);
        AXIOM_TYPES.add(OBJECT_PROPERTY_ASSERTION);
        AXIOM_TYPES.add(NEGATIVE_OBJECT_PROPERTY_ASSERTION);
        AXIOM_TYPES.add(DATA_PROPERTY_ASSERTION);
        AXIOM_TYPES.add(NEGATIVE_DATA_PROPERTY_ASSERTION);
        AXIOM_TYPES.add(OBJECT_PROPERTY_DOMAIN);
        AXIOM_TYPES.add(OBJECT_PROPERTY_RANGE);
        AXIOM_TYPES.add(DISJOINT_OBJECT_PROPERTIES);
        AXIOM_TYPES.add(SUB_OBJECT_PROPERTY);
        AXIOM_TYPES.add(EQUIVALENT_OBJECT_PROPERTIES);
        AXIOM_TYPES.add(INVERSE_OBJECT_PROPERTIES);
        AXIOM_TYPES.add(PROPERTY_CHAIN_SUB_PROPERTY);
        AXIOM_TYPES.add(FUNCTIONAL_OBJECT_PROPERTY);
        AXIOM_TYPES.add(INVERSE_FUNCTIONAL_OBJECT_PROPERTY);
        AXIOM_TYPES.add(SYMMETRIC_OBJECT_PROPERTY);
        AXIOM_TYPES.add(ANTI_SYMMETRIC_OBJECT_PROPERTY);
        AXIOM_TYPES.add(TRANSITIVE_OBJECT_PROPERTY);
        AXIOM_TYPES.add(REFLEXIVE_OBJECT_PROPERTY);
        AXIOM_TYPES.add(IRREFLEXIVE_OBJECT_PROPERTY);
        AXIOM_TYPES.add(DATA_PROPERTY_DOMAIN);
        AXIOM_TYPES.add(DATA_PROPERTY_RANGE);
        AXIOM_TYPES.add(DISJOINT_DATA_PROPERTIES);
        AXIOM_TYPES.add(SUB_DATA_PROPERTY);
        AXIOM_TYPES.add(EQUIVALENT_DATA_PROPERTIES);
        AXIOM_TYPES.add(FUNCTIONAL_DATA_PROPERTY);
        AXIOM_TYPES.add(ENTITY_ANNOTATION);
        AXIOM_TYPES.add(AXIOM_ANNOTATION);
        AXIOM_TYPES.add(IMPORTS_DECLARATION);
        AXIOM_TYPES.add(DISJOINT_UNION);
        AXIOM_TYPES.add(DECLARATION);
        AXIOM_TYPES.add(SWRL_RULE);
        AXIOM_TYPES.add(ONTOLOGY_ANNOTATION);

    }


}
