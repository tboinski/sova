package org.semanticweb.owl.model;

import org.semanticweb.owl.vocab.OWLRestrictedDataRangeFacetVocabulary;

import java.net.URI;
import java.util.List;
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
 * Bio-Health Informatics Group
 * Date: 24-Oct-2006
 * <p/>
 * An interface for creating entities, descriptions and axioms.
 */
public interface OWLDataFactory extends SWRLDataFactory {

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Entities and data stuff
    //
    ////////////////////////////////////////////////////////////////////////////////////


    OWLClass getOWLThing();


    OWLClass getOWLNothing();


    OWLDataType getTopDataType();


    OWLClass getOWLClass(URI uri);


    OWLObjectProperty getOWLObjectProperty(URI uri);


    OWLDataProperty getOWLDataProperty(URI uri);


    OWLIndividual getOWLIndividual(URI uri);


    OWLIndividual getOWLAnonymousIndividual(URI anonId);


    OWLDataType getOWLDataType(URI uri);


    OWLTypedConstant getOWLTypedConstant(String literal, OWLDataType dataType);


    /**
     * Convenience method that obtains a constant typed as an integer.
     * @param value The value of the constant
     * @return An <code>OWLTypedConstant</code> whose literal is the lexical
     * value of the integer, and whose data type is xsd:integer.
     */
    OWLTypedConstant getOWLTypedConstant(int value);


    /**
     * Convenience method that obtains a constant typed as a double.
     * @param value The value of the constant
     * @return An <code>OWLTypedConstant</code> whose literal is the lexical
     * value of the double, and whose data type is xsd:double.
     */
    OWLTypedConstant getOWLTypedConstant(double value);


    /**
     * Convenience method that obtains a constant typed as a boolean.
     * @param value The value of the constant
     * @return An <code>OWLTypedConstant</code> whose literal is the lexical
     * value of the boolean, and whose data type is xsd:boolean.
     */
    OWLTypedConstant getOWLTypedConstant(boolean value);


    /**
     * Convenience method that obtains a constant typed as a float.
     * @param value The value of the constant
     * @return An <code>OWLTypedConstant</code> whose literal is the lexical
     * value of the float, and whose data type is xsd:float.
     */
    OWLTypedConstant getOWLTypedConstant(float value);

    /**
     * Convenience method that obtains a constant typed as a string.
     * @param value The value of the constant
     * @return An <code>OWLTypedConstant</code> whose literal is the lexical
     * value of the string, and whose data type is xsd:string.
     */
    OWLTypedConstant getOWLTypedConstant(String value);

    OWLUntypedConstant getOWLUntypedConstant(String literal);


    OWLUntypedConstant getOWLUntypedConstant(String literal, String lang);


    OWLDataOneOf getOWLDataOneOf(Set<? extends OWLConstant> values);


    OWLDataOneOf getOWLDataOneOf(OWLConstant... values);


    OWLDataComplementOf getOWLDataComplementOf(OWLDataRange dataRange);


    OWLDataRangeRestriction getOWLDataRangeRestriction(OWLDataRange dataRange,
                                                       Set<OWLDataRangeFacetRestriction> facetRestrictions);


    OWLDataRangeRestriction getOWLDataRangeRestriction(OWLDataRange dataRange,
                                                       OWLRestrictedDataRangeFacetVocabulary facet,
                                                       OWLTypedConstant typedConstant);

    OWLDataRangeRestriction getOWLDataRangeRestriction(OWLDataRange dataRange,
                                                       OWLDataRangeFacetRestriction ... facetRestrictions);


    OWLDataRangeFacetRestriction getOWLDataRangeFacetRestriction(OWLRestrictedDataRangeFacetVocabulary facet,
                                                                 OWLTypedConstant facetValue);

    OWLDataRangeFacetRestriction getOWLDataRangeFacetRestriction(OWLRestrictedDataRangeFacetVocabulary facet,
                                                                 int facetValue);

    OWLDataRangeFacetRestriction getOWLDataRangeFacetRestriction(OWLRestrictedDataRangeFacetVocabulary facet,
                                                                 double facetValue);

    OWLDataRangeFacetRestriction getOWLDataRangeFacetRestriction(OWLRestrictedDataRangeFacetVocabulary facet,
                                                                 float facetValue);


    OWLObjectPropertyInverse getOWLObjectPropertyInverse(OWLObjectPropertyExpression property);

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Descriptions
    //
    ////////////////////////////////////////////////////////////////////////////////////


    OWLObjectIntersectionOf getOWLObjectIntersectionOf(Set<? extends OWLDescription> operands);


    OWLObjectIntersectionOf getOWLObjectIntersectionOf(OWLDescription... operands);


    OWLDataAllRestriction getOWLDataAllRestriction(OWLDataPropertyExpression property, OWLDataRange dataRange);


    OWLDataExactCardinalityRestriction getOWLDataExactCardinalityRestriction(OWLDataPropertyExpression property,
                                                                             int cardinality);


    OWLDataExactCardinalityRestriction getOWLDataExactCardinalityRestriction(OWLDataPropertyExpression property,
                                                                             int cardinality, OWLDataRange dataRange);


    OWLDataMaxCardinalityRestriction getOWLDataMaxCardinalityRestriction(OWLDataPropertyExpression property,
                                                                         int cardinality);


    OWLDataMaxCardinalityRestriction getOWLDataMaxCardinalityRestriction(OWLDataPropertyExpression property,
                                                                         int cardinality, OWLDataRange dataRange);


    OWLDataMinCardinalityRestriction getOWLDataMinCardinalityRestriction(OWLDataPropertyExpression property,
                                                                         int cardinality);


    OWLDataMinCardinalityRestriction getOWLDataMinCardinalityRestriction(OWLDataPropertyExpression property,
                                                                         int cardinality, OWLDataRange dataRange);


    OWLDataSomeRestriction getOWLDataSomeRestriction(OWLDataPropertyExpression property, OWLDataRange dataRange);


    OWLDataValueRestriction getOWLDataValueRestriction(OWLDataPropertyExpression property, OWLConstant value);


    OWLObjectComplementOf getOWLObjectComplementOf(OWLDescription operand);


    OWLObjectAllRestriction getOWLObjectAllRestriction(OWLObjectPropertyExpression property,
                                                       OWLDescription description);


    OWLObjectOneOf getOWLObjectOneOf(Set<OWLIndividual> values);


    OWLObjectOneOf getOWLObjectOneOf(OWLIndividual... individuals);


    OWLObjectExactCardinalityRestriction getOWLObjectExactCardinalityRestriction(OWLObjectPropertyExpression property,
                                                                                 int cardinality);


    OWLObjectExactCardinalityRestriction getOWLObjectExactCardinalityRestriction(OWLObjectPropertyExpression property,
                                                                                 int cardinality,
                                                                                 OWLDescription description);


    OWLObjectMinCardinalityRestriction getOWLObjectMinCardinalityRestriction(OWLObjectPropertyExpression property,
                                                                             int cardinality);


    OWLObjectMinCardinalityRestriction getOWLObjectMinCardinalityRestriction(OWLObjectPropertyExpression property,
                                                                             int cardinality,
                                                                             OWLDescription description);


    OWLObjectMaxCardinalityRestriction getOWLObjectMaxCardinalityRestriction(OWLObjectPropertyExpression property,
                                                                             int cardinality);


    OWLObjectMaxCardinalityRestriction getOWLObjectMaxCardinalityRestriction(OWLObjectPropertyExpression property,
                                                                             int cardinality,
                                                                             OWLDescription description);


    OWLObjectSelfRestriction getOWLObjectSelfRestriction(OWLObjectPropertyExpression property);


    OWLObjectSomeRestriction getOWLObjectSomeRestriction(OWLObjectPropertyExpression property,
                                                         OWLDescription description);


    OWLObjectValueRestriction getOWLObjectValueRestriction(OWLObjectPropertyExpression property,
                                                           OWLIndividual individual);


    OWLObjectUnionOf getOWLObjectUnionOf(Set<? extends OWLDescription> operands);


    OWLObjectUnionOf getOWLObjectUnionOf(OWLDescription... operands);

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Axioms
    //
    /////////////////////////////////////////////////////////////////////////////////////////////


    OWLAntiSymmetricObjectPropertyAxiom getOWLAntiSymmetricObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLDataPropertyDomainAxiom getOWLDataPropertyDomainAxiom(OWLDataPropertyExpression property, OWLDescription domain);


    OWLDataPropertyRangeAxiom getOWLDataPropertyRangeAxiom(OWLDataPropertyExpression propery,
                                                           OWLDataRange owlDataRange);


    OWLDataSubPropertyAxiom getOWLSubDataPropertyAxiom(OWLDataPropertyExpression subProperty,
                                                       OWLDataPropertyExpression superProperty);


    OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity owlEntity);


    OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(Set<OWLIndividual> individuals);

    OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(OWLIndividual ... individuals);


    OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(Set<? extends OWLDescription> descriptions);


    OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(OWLDescription clsA, OWLDescription clsB);

    OWLDisjointClassesAxiom getOWLDisjointClassesAxiom(OWLDescription clsA, OWLDescription ... descriptions);


    OWLDisjointDataPropertiesAxiom getOWLDisjointDataPropertiesAxiom(
            Set<? extends OWLDataPropertyExpression> properties);


    OWLDisjointObjectPropertiesAxiom getOWLDisjointObjectPropertiesAxiom(
            Set<? extends OWLObjectPropertyExpression> properties);


    OWLDisjointUnionAxiom getOWLDisjointUnionAxiom(OWLClass owlClass, Set<? extends OWLDescription> descriptions);


    OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(Set<? extends OWLDescription> descriptions);


    OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(OWLDescription clsA, OWLDescription clsB);


    OWLEquivalentDataPropertiesAxiom getOWLEquivalentDataPropertiesAxiom(
            Set<? extends OWLDataPropertyExpression> properties);


    OWLEquivalentObjectPropertiesAxiom getOWLEquivalentObjectPropertiesAxiom(
            Set<? extends OWLObjectPropertyExpression> properties);


    OWLFunctionalDataPropertyAxiom getOWLFunctionalDataPropertyAxiom(OWLDataPropertyExpression property);


    OWLFunctionalObjectPropertyAxiom getOWLFunctionalObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLImportsDeclaration getOWLImportsDeclarationAxiom(OWLOntology subject, URI importedOntologyURI);


    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                   OWLDataPropertyExpression property,
                                                                   OWLConstant object);

    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                   OWLDataPropertyExpression property,
                                                                   int value);

    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                   OWLDataPropertyExpression property,
                                                                   double value);


    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                   OWLDataPropertyExpression property,
                                                                   float value);


    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                   OWLDataPropertyExpression property,
                                                                   boolean value);


    OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                   OWLDataPropertyExpression property,
                                                                   String value);

    OWLNegativeDataPropertyAssertionAxiom getOWLNegativeDataPropertyAssertionAxiom(OWLIndividual subject,
                                                                                   OWLDataPropertyExpression property,
                                                                                   OWLConstant object);


    OWLNegativeObjectPropertyAssertionAxiom getOWLNegativeObjectPropertyAssertionAxiom(OWLIndividual subject,
                                                                                       OWLObjectPropertyExpression property,
                                                                                       OWLIndividual object);


    OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLIndividual individual,
                                                                       OWLObjectPropertyExpression property,
                                                                       OWLIndividual object);


    OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLIndividual individual, OWLDescription description);

    OWLInverseFunctionalObjectPropertyAxiom getOWLInverseFunctionalObjectPropertyAxiom(
            OWLObjectPropertyExpression property);


    OWLIrreflexiveObjectPropertyAxiom getOWLIrreflexiveObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLObjectPropertyDomainAxiom getOWLObjectPropertyDomainAxiom(OWLObjectPropertyExpression property,
                                                                 OWLDescription description);


    OWLObjectPropertyRangeAxiom getOWLObjectPropertyRangeAxiom(OWLObjectPropertyExpression property,
                                                               OWLDescription range);


    OWLObjectSubPropertyAxiom getOWLSubObjectPropertyAxiom(OWLObjectPropertyExpression subProperty,
                                                           OWLObjectPropertyExpression superProperty);


    OWLReflexiveObjectPropertyAxiom getOWLReflexiveObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLSameIndividualsAxiom getOWLSameIndividualsAxiom(Set<OWLIndividual> individuals);


    OWLSubClassAxiom getOWLSubClassAxiom(OWLDescription subClass, OWLDescription superClass);


    OWLSymmetricObjectPropertyAxiom getOWLSymmetricObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLTransitiveObjectPropertyAxiom getOWLTransitiveObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLDeprecatedClassAxiom getOWLDeprecatedClassAxiom(OWLClass owlClass);


    OWLDeprecatedObjectPropertyAxiom getOWLDeprecatedObjectPropertyAxiom(OWLObjectProperty property);


    OWLDeprecatedDataPropertyAxiom getOWLDeprecatedDataPropertyAxiom(OWLDataProperty property);


    OWLObjectPropertyChainSubPropertyAxiom getOWLObjectPropertyChainSubPropertyAxiom(
            List<? extends OWLObjectPropertyExpression> chain, OWLObjectPropertyExpression superProperty);


    OWLInverseObjectPropertiesAxiom getOWLInverseObjectPropertiesAxiom(OWLObjectPropertyExpression forwardProperty,
                                                                       OWLObjectPropertyExpression inverseProperty);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Annotations


    OWLEntityAnnotationAxiom getOWLEntityAnnotationAxiom(OWLEntity entity, OWLAnnotation annotation);

    OWLEntityAnnotationAxiom getOWLEntityAnnotationAxiom(OWLEntity entity, URI annotationURI, OWLConstant value);

    OWLEntityAnnotationAxiom getOWLEntityAnnotationAxiom(OWLEntity entity, URI annotationURI, OWLIndividual value);


    OWLAxiomAnnotationAxiom getOWLAxiomAnnotationAxiom(OWLAxiom axiom, OWLAnnotation annotation);


    OWLConstantAnnotation getOWLConstantAnnotation(URI annotationURI, OWLConstant constant);


    OWLObjectAnnotation getOWLObjectAnnotation(URI annotationURI, OWLIndividual individual);


    /**
     * Gets a label annotation. This is an annotation that has a URI
     * which corresponds to rdfs:label
     * @param label The label content
     */
    OWLLabelAnnotation getOWLLabelAnnotation(String label);


    /**
     * Gets a label annotation
     * @param label    The label content
     * @param language The language of the label
     */
    OWLLabelAnnotation getOWLLabelAnnotation(String label, String language);


    /**
     * Gets a comment annotation.  This is an annotation with a URI
     * that corresponds to rdfs:comment
     */
    OWLCommentAnnotation getCommentAnnotation(String comment);


    /**
     * Gets a comment annotation with an attached language tag.
     * @param comment  The comment content
     * @param langauge The langauge that the comment is in
     */
    OWLCommentAnnotation getCommentAnnotation(String comment, String langauge);


    OWLOntologyAnnotationAxiom getOWLOntologyAnnotationAxiom(OWLOntology subject, OWLAnnotation annotation);
}

