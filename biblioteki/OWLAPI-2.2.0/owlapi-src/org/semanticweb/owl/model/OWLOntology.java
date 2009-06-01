package org.semanticweb.owl.model;


import java.net.URI;
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
 * <p/>
 * An <code>OWLOntology</code> contains a set of axioms.  Ontologies do not specifically
 * contain entities such as classes, properties or individuals - these entities are merely
 * referenced by the axioms that the ontology contains.  Unlike some APIs it is therefore not
 * possible to directly add or remove entities to or from an ontology.
 */
public interface OWLOntology extends OWLNamedObject {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Methods to retrive class, property and individual axioms
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Retrieves all of the axioms in this ontology.
     * @return The set of all axioms in this ontology, including logical
     *         axioms and annotation axioms.
     */
    Set<OWLAxiom> getAxioms();


    /**
     * Gets all of the axioms in the ontology that affect the
     * logical meaning of the ontology.  In other words, this
     * method returns all axioms that are not annotation axioms,
     * declaration axioms or imports declarations.
     * @return A set of axioms which are of the type <code>OWLLogicalAxiom</code>
     */
    Set<OWLLogicalAxiom> getLogicalAxioms();


    Set<SWRLRule> getRules();


    /**
     * Gets the axioms which are of the specified type
     * @param axiomType The type of axioms to be retrived
     * @return A set containing the axioms which are of the specified type
     */
    <T extends OWLAxiom> Set<T> getAxioms(AxiomType<T> axiomType);


    /**
     * Gets all of the class axioms in this ontology
     */
    Set<OWLClassAxiom> getClassAxioms();


    /**
     * Gets all of the object property axioms in this ontology
     */
    Set<OWLPropertyAxiom> getObjectPropertyAxioms();


    /**
     * Gets all of the data property axioms in this ontology
     */
    Set<OWLPropertyAxiom> getDataPropertyAxioms();


    /**
     * Gets all of the individual axioms in this ontology
     */
    Set<OWLIndividualAxiom> getIndividualAxioms();


    /**
     * Gets the set of general axioms in this ontology.  This includes:
     * <p/>
     * <ul>
     * <li>Subclass axioms that have a complex class as the subclass</li>
     * <li>Equivalent class axioms that don't contain any named classes (<code>OWLClass</code>es)</li>
     * <li>Disjoint class axioms that don't contain any named classes (<code>OWLClass</code>es)</li>
     * </ul>
     */
    Set<OWLClassAxiom> getGeneralClassAxioms();


    /**
     * Gets the property chain sub property axioms in the ontology.
     */
    Set<OWLObjectPropertyChainSubPropertyAxiom> getPropertyChainSubPropertyAxioms();

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // References/usage
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the entities that are referenced by axioms in this ontology.
     * @return A set of <code>OWLEntity</code> objects.
     */
    Set<OWLEntity> getReferencedEntities();

    /**
     * Gets the classes that are referenced by axioms (including annotation axioms) in
     * this ontology.
     * @return A set of named classes, which are referenced by any axiom in this
     *         ontology.  The set may be unmodifiable.
     */
    Set<OWLClass> getReferencedClasses();


    /**
     * Gets the object properties that are referenced by axioms (including annotation axioms) in
     * this ontology.
     * @return A set of object properties, which are referenced by any axiom in this
     *         ontology.
     */
    Set<OWLObjectProperty> getReferencedObjectProperties();


    /**
     * Gets the data properties that are referenced by axioms (including annotation axioms) in
     * this ontology.
     * @return A set of data properties, which are referenced by any axiom in this
     *         ontology.
     */
    Set<OWLDataProperty> getReferencedDataProperties();


    /**
     * Gets the individuals that are referenced by axioms (including annotation axioms) in
     * this ontology.
     * @return A set of individuals, which are referenced by any axiom in this
     *         ontology.
     */
    Set<OWLIndividual> getReferencedIndividuals();


    /**
     * Gets the set of URIs that are used in annotations (e.g. rdfs:label)
     */
    Set<URI> getAnnotationURIs();


    /**
     * Gets the axioms where the specified entity appears <i>anywhere</i> in the axiom.
     * The set that is returned, contains all axioms that directly reference the specified
     * entity, including annotation axioms.
     * @param owlEntity The entity that should be directly referred to by an axiom
     *                  that appears in the results set.
     */
    Set<OWLAxiom> getReferencingAxioms(OWLEntity owlEntity);


    /**
     * Determines if the ontology contains a reference to the specified entity.
     * @param owlEntity The entity
     * @return <code>true</code> if the ontology contains a reference to the specified
     *         entity, otherwise <code>false</code>
     */
    boolean containsEntityReference(OWLEntity owlEntity);


    /**
     * Determines if this ontology contains a declaration axiom
     * for the specified entity.
     * @param owlEntity The entity
     * @return <code>true</code> if the ontology contains a declaration
     *         for the specified entity, otherwise <code>false</code>
     */
    boolean containsEntityDeclaration(OWLEntity owlEntity);

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Access by URI
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Determines if the ontology contains a reference to a class that
     * has a specific URI.
     * @param owlClassURI The URI to test for.
     * @return <code>true</code> if the ontology refers to a class with the
     *         specified URI, otherwise <code>false</code>
     */
    boolean containsClassReference(URI owlClassURI);


    /**
     * Determines if the ontology contains a reference to an object property
     * that a specific URI.
     */
    boolean containsObjectPropertyReference(URI propURI);


    boolean containsDataPropertyReference(URI propURI);


    boolean containsIndividualReference(URI individualURI);


    boolean containsDataTypeReference(URI datatypeURI);


    /**
     * Determines if the specified URI refers to more than one type of
     * entity (i.e. class, object property, data property, individual or datatype)
     * @param uri The URI to test.
     * @return <code>true</code> if the URI refers to more than one entity,
     *         for example because the ontology references a class with the URI and also
     *         an individual with the same URI.
     */
    boolean isPunned(URI uri);

//    Set<OWLEntity> getObjects(URI entityURI);

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Axioms that form part of a description of a named entity
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the axioms that form the definition/description of a class.
     * @param cls The class whose describing axioms are to be retrieved.
     * @return A set of class axioms that describe the class.  This set includes
     *         <ul>
     *         <li>Subclass axioms where the subclass is equal to the specified class</li>
     *         <li>Equivalent class axioms where the specified class is an operand in the equivalent class axiom</li>
     *         <li>Disjoint class axioms where the specified class is an operand in the disjoint class axiom</li>
     *         <li>Disjoint union axioms, where the specified class is the named class that is equivalent to the disjoint union</li>
     *         </ul>
     */
    Set<OWLClassAxiom> getAxioms(OWLClass cls);


    /**
     * Gets the axioms that form the definition/description of an object property.
     * @param prop The property whose defining axioms are to be retrieved.
     * @return A set of object property axioms that includes
     *         <ul>
     *         <li>Sub-property axioms where the sub property is equal to the specified property</li>
     *         <li>Equivalent property axioms where the axiom contains the specified property</li>
     *         <li>Equivalent property axioms that contain the inverse of the specified property</li>
     *         <li>Disjoint property axioms that contain the specified property</li>
     *         <li>Domain axioms that specify a domain of the specified property</li>
     *         <li>Range axioms that specify a range of the specified property</li>
     *         <li>Any property characteristic axiom (i.e. Functional, Symmetric, Reflexive etc.) whose
     *         subject is the specified property</li>
     *         <li>Inverse properties axioms that contain the specified property</li>
     *         </ul>
     */
    Set<OWLObjectPropertyAxiom> getAxioms(OWLObjectPropertyExpression prop);


    /**
     * Gets the axioms that form the definition/description of a data property.
     * @param prop The property whose defining axioms are to be retrieved.
     * @return A set of data property axioms that includes
     *         <ul>
     *         <li>Sub-property axioms where the sub property is equal to the specified property</li>
     *         <li>Equivalent property axioms where the axiom contains the specified property</li>
     *         <li>Disjoint property axioms that contain the specified property</li>
     *         <li>Domain axioms that specify a domain of the specified property</li>
     *         <li>Range axioms that specify a range of the specified property</li>
     *         <li>Any property characteristic axiom (i.e. Functional, Symmetric, Reflexive etc.) whose
     *         subject is the specified property</li>
     *         </ul>
     */
    Set<OWLDataPropertyAxiom> getAxioms(OWLDataProperty prop);


    /**
     * Gets the axioms that form the definition/description of an individual
     * @param individual The individual whose defining axioms are to be retrieved.
     * @return A set of individual axioms that includes
     *         <ul>
     *         <li>Individual type assertions that assert the type of the specified individual</li>
     *         <li>Same individuals axioms that contain the specified individual</li>
     *         <li>Different individuals axioms that contain the specified individual</li>
     *         <li>Object property assertion axioms whose subject is the specified individual</li>
     *         <li>Data property assertion axioms whose subject is the specified individual</li>
     *         <li>Negative object property assertion axioms whose subject is the specified individual</li>
     *         <li>Negative data property assertion axioms whose subject is the specified individual</li>
     *         </ul>
     */
    Set<OWLIndividualAxiom> getAxioms(OWLIndividual individual);

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Annotation axioms
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets all of the annotation axioms in this ontology
     */
    Set<OWLAnnotationAxiom> getAnnotationAxioms();


    /**
     * Gets all of the annotation axioms in this ontology.
     */
    Set<OWLDeclarationAxiom> getDeclarationAxioms();

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Imported ontologies
    //
    // Imports annotations are presereved in their original form, as OWLImportsAnnotationAxioms.
    // In order to obtain concrete implementations of imported ontologies, an OWLOntologyManager
    // must be used.
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the set of imports annotations for this ontology.
     */
    Set<OWLImportsDeclaration> getImportsDeclarations();


    /**
     * Gets the <code>OWLOntology</code> instances that this ontology
     * imports.
     * @param ontologyManager The <code>OWLOntologyManager</code> that will map the
     *                        imports annotations to actual <code>OWLOntology</code> instances.
     */
    Set<OWLOntology> getImports(OWLOntologyManager ontologyManager) throws UnknownOWLOntologyException;

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Various methods that provide axioms relating to specific entities that allow
    // frame style views to be composed for a particular entity.  Such functionality is
    // useful for ontology editors and browsers.
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Determines if this ontology contains the specified
     * axiom.  This method does not take into consideration
     * annotations.  For example, if the ontology contained
     * SubClassOf("Added by M Horridge" A B) then then this method
     * would return true for SubClassOf(A B)
     * @return <code>true</code> if the ontology contains the
     *         specified axioms, or <code>false</code> if the ontology
     *         doesn't contain the specified axiom.
     */
    boolean containsAxiom(OWLAxiom axiom);


    /**
     * Gets the declaration axioms for specified entity.
     * @param subject The entity that is the subject of the set of returned axioms.
     */
    Set<OWLDeclarationAxiom> getDeclarationAxioms(OWLEntity subject);


    /**
     * Gets the annotation axioms that annotate the specified axiom in this ontology.
     * @param axiom The axiom that the returned set of axiom annotation axioms annotate.
     */
    Set<OWLAxiomAnnotationAxiom> getAnnotations(OWLAxiom axiom);


    /**
     * Gets the axioms that annotate the specified entity.
     */
    Set<OWLEntityAnnotationAxiom> getEntityAnnotationAxioms(OWLEntity entity);


    /**
     * Gets the ontology annotations whose subject is the specified ontology.
     * @param subject The ontology that is the subject of the annotations.
     * @return The set of annotation axioms which have been asserted in this ontology,
     *         which have the specified ontology as their subject.
     */
    Set<OWLOntologyAnnotationAxiom> getAnnotations(OWLOntology subject);


    /**
     * Gets all of the ontology annotation axioms which have been asserted in this
     * ontology.
     */
    Set<OWLOntologyAnnotationAxiom> getOntologyAnnotationAxioms();

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Classes
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets all of the subclass axioms where the left hand side (the subclass) is equal to the
     * specified class.
     * @param cls The class that is equal to the left hand side of the axiom (subclass)
     */
    Set<OWLSubClassAxiom> getSubClassAxiomsForLHS(OWLClass cls);


    /**
     * Gets all of the subclass axioms where the right hand side (the superclass) is
     * equal to the specified class.
     */
    Set<OWLSubClassAxiom> getSubClassAxiomsForRHS(OWLClass cls);


    /**
     * Gets all of the equivalent axioms in this ontology that contain the specified
     * class as an operand.
     * @return A set of equivalent class axioms that contain the specified class as
     *         an operand.
     */
    Set<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(OWLClass cls);


    /**
     * Gets the set of disjoint class axioms that contain the specified class as
     * an operand.
     * @param cls The class that should be contained in the set of disjoint class
     *            axioms that will be returned.
     * @return The set of disjoint axioms that contain the specified class.
     */
    Set<OWLDisjointClassesAxiom> getDisjointClassesAxioms(OWLClass cls);


    /**
     * Gets the set of disjoint union axioms that have the specified class as the named
     * class that is equivalent to the disjoint union of operands.  For example, if
     * the ontology contained the axiom DisjointUnion(A, propP some C, D, E) this axiom
     * would be returned for class A (but not for D or E).
     * @param owlClass The class that indexes the axioms to be retrieved.
     */
    Set<OWLDisjointUnionAxiom> getDisjointUnionAxioms(OWLClass owlClass);

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Object properties
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the object property sub property axioms where the specified property is on the
     * left hand side of the axiom.
     * @param property The property which is on the left hand side of the axiom.
     */
    Set<OWLObjectSubPropertyAxiom> getObjectSubPropertyAxiomsForLHS(OWLObjectPropertyExpression property);


    Set<OWLObjectSubPropertyAxiom> getObjectSubPropertyAxiomsForRHS(OWLObjectPropertyExpression property);


    Set<OWLObjectPropertyDomainAxiom> getObjectPropertyDomainAxioms(OWLObjectPropertyExpression property);


    Set<OWLObjectPropertyRangeAxiom> getObjectPropertyRangeAxioms(OWLObjectPropertyExpression property);


    Set<OWLInverseObjectPropertiesAxiom> getInverseObjectPropertyAxioms(OWLObjectPropertyExpression property);


    Set<OWLEquivalentObjectPropertiesAxiom> getEquivalentObjectPropertiesAxioms(OWLObjectPropertyExpression property);


    Set<OWLDisjointObjectPropertiesAxiom> getDisjointObjectPropertiesAxiom(OWLObjectPropertyExpression property);


    OWLFunctionalObjectPropertyAxiom getFunctionalObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLInverseFunctionalObjectPropertyAxiom getInverseFunctionalObjectPropertyAxiom(
            OWLObjectPropertyExpression property);


    OWLSymmetricObjectPropertyAxiom getSymmetricObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLAntiSymmetricObjectPropertyAxiom getAntiSymmetricObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLReflexiveObjectPropertyAxiom getReflexiveObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLIrreflexiveObjectPropertyAxiom getIrreflexiveObjectPropertyAxiom(OWLObjectPropertyExpression property);


    OWLTransitiveObjectPropertyAxiom getTransitiveObjectPropertyAxiom(OWLObjectPropertyExpression property);

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Data properties
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    Set<OWLDataSubPropertyAxiom> getDataSubPropertyAxiomsForLHS(OWLDataProperty lhsProperty);


    Set<OWLDataSubPropertyAxiom> getDataSubPropertyAxiomsForRHS(OWLDataPropertyExpression property);


    Set<OWLDataPropertyDomainAxiom> getDataPropertyDomainAxioms(OWLDataProperty property);


    Set<OWLDataPropertyRangeAxiom> getDataPropertyRangeAxiom(OWLDataProperty property);


    Set<OWLEquivalentDataPropertiesAxiom> getEquivalentDataPropertiesAxiom(OWLDataProperty property);


    Set<OWLDisjointDataPropertiesAxiom> getDisjointDataPropertiesAxiom(OWLDataProperty property);


    OWLFunctionalDataPropertyAxiom getFunctionalDataPropertyAxiom(OWLDataPropertyExpression property);

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Individuals
    //
    //////////////////////////////////////////////////////////////////////////////////////////////


    Set<OWLClassAssertionAxiom> getClassAssertionAxioms(OWLIndividual individual);


    Set<OWLClassAssertionAxiom> getClassAssertionAxioms(OWLClass type);


    Set<OWLDataPropertyAssertionAxiom> getDataPropertyAssertionAxioms(OWLIndividual individual);


    Set<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertionAxioms(OWLIndividual individual);


    Set<OWLNegativeObjectPropertyAssertionAxiom> getNegativeObjectPropertyAssertionAxioms(OWLIndividual individual);


    Set<OWLNegativeDataPropertyAssertionAxiom> getNegativeDataPropertyAssertionAxioms(OWLIndividual individual);


    Set<OWLSameIndividualsAxiom> getSameIndividualAxioms(OWLIndividual individual);


    Set<OWLDifferentIndividualsAxiom> getDifferentIndividualAxioms(OWLIndividual individual);
}
