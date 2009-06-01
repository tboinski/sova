package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.*;
import static org.semanticweb.owl.model.AxiomType.*;
import static org.semanticweb.owl.util.CollectionFactory.createMap;
import static org.semanticweb.owl.util.CollectionFactory.createSet;
import org.semanticweb.owl.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owl.util.OWLEntityCollector;

import java.net.URI;
import java.util.*;
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
public class OWLOntologyImpl extends OWLObjectImpl implements OWLMutableOntology {

    private URI ontologyURI;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Sets of different kinds of axioms
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Set<OWLAxiom> allAxioms;

    private Set<OWLLogicalAxiom> logicalAxioms;

    private Map<AxiomType, Set<OWLAxiom>> axiomsByType;

    private Set<SWRLRule> ruleAxioms;

    private Set<OWLAnnotationAxiom> annotationAxioms;

    private Set<OWLClassAxiom> owlClassAxioms;

    private Set<OWLClassAxiom> generalClassAxioms;

    private Set<OWLObjectPropertyChainSubPropertyAxiom> propertyChainSubPropertyAxioms;

    private Set<OWLPropertyAxiom> owlObjectPropertyAxioms;

    private Set<OWLPropertyAxiom> owlDataPropertyAxioms;

    private Set<OWLIndividualAxiom> owlIndividualAxioms;

    private Set<OWLDeclarationAxiom> owlDeclarationAxioms;

    private Set<OWLAxiomAnnotationAxiom> owlAxiomAnnotationAxioms;

    private Map<OWLAxiom, Set<OWLAxiomAnnotationAxiom>> owlAxiomAnnotationAxiomByAxiom;

    private Map<OWLOntology, Set<OWLOntologyAnnotationAxiom>> owlOntologyAnnotationAxiomsByOntology;

    private Set<OWLImportsDeclaration> owlImportsAnnotationAxioms;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Referenced entities counts
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<OWLClass, Set<OWLAxiom>> owlClassReferences;

    private Map<OWLObjectProperty, Set<OWLAxiom>> owlObjectPropertyReferences;

    private Map<OWLDataProperty, Set<OWLAxiom>> owlDataPropertyReferences;

    private Map<OWLIndividual, Set<OWLAxiom>> owlIndividualReferences;

    private Map<OWLDataType, Set<OWLAxiom>> owlDataTypeReferences;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // OWLClassAxioms by OWLClass
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<OWLClass, Set<OWLClassAxiom>> classAxiomsByClass;

    private Map<OWLClass, Set<OWLSubClassAxiom>> subClassAxiomsByLHS;

    private Map<OWLClass, Set<OWLSubClassAxiom>> subClassAxiomsByRHS;

    private Map<OWLClass, Set<OWLEquivalentClassesAxiom>> equivalentClassesAxiomsByClass;

    private Map<OWLClass, Set<OWLDisjointClassesAxiom>> disjointClassesAxiomsByClass;

    private Map<OWLClass, Set<OWLDisjointUnionAxiom>> disjointUnionAxiomsByClass;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // OWLObjectPropertyAxioms by OWLObjectProperty
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private Map<OWLObjectPropertyExpression, Set<OWLObjectSubPropertyAxiom>> objectSubPropertyAxiomsByLHS;

    private Map<OWLObjectPropertyExpression, Set<OWLObjectSubPropertyAxiom>> objectSubPropertyAxiomsByRHS;

    private Map<OWLObjectPropertyExpression, Set<OWLEquivalentObjectPropertiesAxiom>> equivalentObjectPropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, Set<OWLDisjointObjectPropertiesAxiom>> disjointObjectPropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, Set<OWLObjectPropertyDomainAxiom>> objectPropertyDomainAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, Set<OWLObjectPropertyRangeAxiom>> objectPropertyRangeAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLFunctionalObjectPropertyAxiom> functionalObjectPropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLInverseFunctionalObjectPropertyAxiom> inverseFunctionalPropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLSymmetricObjectPropertyAxiom> symmetricPropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLAntiSymmetricObjectPropertyAxiom> antiSymmetricPropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLReflexiveObjectPropertyAxiom> reflexivePropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLIrreflexiveObjectPropertyAxiom> irreflexivePropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, OWLTransitiveObjectPropertyAxiom> transitivePropertyAxiomsByProperty;

    private Map<OWLObjectPropertyExpression, Set<OWLInverseObjectPropertiesAxiom>> inversePropertyAxiomsByProperty;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // OWLDataPropertyAxioms by OWLDataProperty
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<OWLDataPropertyExpression, Set<OWLDataSubPropertyAxiom>> dataSubPropertyAxiomsByLHS;

    private Map<OWLDataPropertyExpression, Set<OWLDataSubPropertyAxiom>> dataSubPropertyAxiomsByRHS;

    private Map<OWLDataPropertyExpression, Set<OWLEquivalentDataPropertiesAxiom>> equivalentDataPropertyAxiomsByProperty;

    private Map<OWLDataPropertyExpression, Set<OWLDisjointDataPropertiesAxiom>> disjointDataPropertyAxiomsByProperty;

    private Map<OWLDataPropertyExpression, Set<OWLDataPropertyDomainAxiom>> dataPropertyDomainAxiomsByProperty;

    private Map<OWLDataPropertyExpression, Set<OWLDataPropertyRangeAxiom>> dataPropertyRangeAxiomsByProperty;

    private Map<OWLDataPropertyExpression, OWLFunctionalDataPropertyAxiom> functionalDataPropertyAxiomsByProperty;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // OWLIndividualAxioms by OWLIndividual
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<OWLIndividual, Set<OWLClassAssertionAxiom>> typeAxiomsByIndividual;

    private Map<OWLClass, Set<OWLClassAssertionAxiom>> typeAxiomsByClass;

    private Map<OWLIndividual, Set<OWLObjectPropertyAssertionAxiom>> objectPropertyAssertionsByIndividual;

    private Map<OWLIndividual, Set<OWLDataPropertyAssertionAxiom>> dataPropertyAssertionsByIndividual;

    private Map<OWLIndividual, Set<OWLNegativeObjectPropertyAssertionAxiom>> negativeObjectPropertyAssertionAxiomsByIndividual;

    private Map<OWLIndividual, Set<OWLNegativeDataPropertyAssertionAxiom>> negativeDataPropertyAssertionAxiomsByIndividual;

    private Map<OWLIndividual, Set<OWLDifferentIndividualsAxiom>> differentIndividualsAxiomsByIndividual;

    private Map<OWLIndividual, Set<OWLSameIndividualsAxiom>> sameIndividualsAxiomsByIndividual;

    private Map<OWLEntity, Set<OWLDeclarationAxiom>> owlDeclarationAxiomMap;

    private Map<OWLEntity, Set<OWLEntityAnnotationAxiom>> owlEntityAnnotationAxiomsByEntity;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Annotations
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<URI, Set<OWLAnnotationAxiom>> annotationAxiomsByAnnotationURI;


    private OWLEntityReferenceChecker entityReferenceChecker = new OWLEntityReferenceChecker();


    public OWLOntologyImpl(OWLDataFactory dataFactory, URI uri) {
        super(dataFactory);
        this.ontologyURI = uri;
        createMaps();
    }

    private void createMaps() {

        // Sets of different types of axioms
        allAxioms = createSet();
        logicalAxioms = createSet();
        ruleAxioms = createSet();
        owlImportsAnnotationAxioms = createSet();
        disjointClassesAxiomsByClass = createMap();
        disjointUnionAxiomsByClass = createMap();
        generalClassAxioms = createSet();
        propertyChainSubPropertyAxioms = createSet();
        owlClassReferences = createMap();
        owlObjectPropertyReferences = createMap();
        owlDataPropertyReferences = createMap();
        owlIndividualReferences = createMap();
        owlOntologyAnnotationAxiomsByOntology = createMap();
        annotationAxiomsByAnnotationURI = createMap();
        owlAxiomAnnotationAxiomByAxiom = createMap();

        axiomsByType = createMap();

        objectSubPropertyAxiomsByLHS = createMap();
        objectSubPropertyAxiomsByRHS = createMap();
        equivalentObjectPropertyAxiomsByProperty = createMap();
        disjointObjectPropertyAxiomsByProperty = createMap();
        objectPropertyDomainAxiomsByProperty = createMap();
        objectPropertyRangeAxiomsByProperty = createMap();
        functionalObjectPropertyAxiomsByProperty = createMap();
        inverseFunctionalPropertyAxiomsByProperty = createMap();
        symmetricPropertyAxiomsByProperty = createMap();
        antiSymmetricPropertyAxiomsByProperty = createMap();
        reflexivePropertyAxiomsByProperty = createMap();
        irreflexivePropertyAxiomsByProperty = createMap();
        transitivePropertyAxiomsByProperty = createMap();
        inversePropertyAxiomsByProperty = createMap();


        dataSubPropertyAxiomsByLHS = createMap();
        dataSubPropertyAxiomsByRHS = createMap();
        equivalentDataPropertyAxiomsByProperty = createMap();
        disjointDataPropertyAxiomsByProperty = createMap();
        dataPropertyDomainAxiomsByProperty = createMap();
        dataPropertyRangeAxiomsByProperty = createMap();
        functionalDataPropertyAxiomsByProperty = createMap();


        objectPropertyAssertionsByIndividual = createMap();
        dataPropertyAssertionsByIndividual = createMap();
        negativeObjectPropertyAssertionAxiomsByIndividual = createMap();
        negativeDataPropertyAssertionAxiomsByIndividual = createMap();
        differentIndividualsAxiomsByIndividual = createMap();
        sameIndividualsAxiomsByIndividual = createMap();

    }


    /**
     * Gets the URI of the ontology.
     */
    public URI getURI() {
        return ontologyURI;
    }


    protected int compareObjectOfSameType(OWLObject object) {
        return ontologyURI.compareTo(((OWLOntology) object).getURI());
    }


    public Set<OWLAxiom> getAxioms() {
        return Collections.unmodifiableSet(allAxioms);
    }


    public <T extends OWLAxiom> Set<T> getAxioms(AxiomType<T> axiomType) {
//        if(axiomsByType == null) {
//            buildAxiomsByTypeIndex();
//        }
        return (Set<T>) getAxioms(axiomType, axiomsByType, false);
    }


    public Set<OWLLogicalAxiom> getLogicalAxioms() {
        return Collections.unmodifiableSet(logicalAxioms);
    }


    public Set<SWRLRule> getRules() {
        return Collections.unmodifiableSet(ruleAxioms);
    }


    public Set<OWLClassAxiom> getClassAxioms() {
        if (owlClassAxioms == null) {
            buildClassAxiomsIndex();
        }
        return Collections.unmodifiableSet(owlClassAxioms);
    }


    public Set<OWLPropertyAxiom> getObjectPropertyAxioms() {
        if (owlObjectPropertyAxioms == null) {
            buildObjectPropertyAxiomsIndex();
        }
        return Collections.unmodifiableSet(owlObjectPropertyAxioms);
    }


    public Set<OWLPropertyAxiom> getDataPropertyAxioms() {
        if (owlDataPropertyAxioms == null) {
            buildDataPropertyAxiomsIndex();
        }
        return Collections.unmodifiableSet(owlDataPropertyAxioms);
    }


    public Set<OWLIndividualAxiom> getIndividualAxioms() {
        if (owlIndividualAxioms == null) {
            buildIndividualAxiomsIndex();
        }
        return Collections.unmodifiableSet(owlIndividualAxioms);
    }


    public Set<OWLAnnotationAxiom> getAnnotationAxioms() {
        if (annotationAxioms == null) {
            buildAnnotationAxiomsIndex();
        }
        return Collections.unmodifiableSet(annotationAxioms);
    }


    public Set<OWLAxiomAnnotationAxiom> getAxiomAnnotationAxioms() {
        if (owlAxiomAnnotationAxioms == null) {
            buildAxiomAnnotationAxiomsIndex();
        }
        return Collections.unmodifiableSet(owlAxiomAnnotationAxioms);
    }


    public Set<OWLDeclarationAxiom> getDeclarationAxioms() {
        if (owlDeclarationAxioms == null) {
            buildOWLDeclarationAxiomsIndex();
        }
        return Collections.unmodifiableSet(owlDeclarationAxioms);
    }


    public Set<OWLDeclarationAxiom> getDeclarationAxioms(OWLEntity entity) {
        if (owlDeclarationAxiomMap == null) {
            buildOWLDeclarationAxiomsIndex();
        }
        return getAxioms(entity, owlDeclarationAxiomMap, false);
    }


    public Set<OWLAxiomAnnotationAxiom> getAnnotations(OWLAxiom axiom) {
        return Collections.unmodifiableSet(getAxioms(axiom, owlAxiomAnnotationAxiomByAxiom));
    }


    public Set<OWLEntityAnnotationAxiom> getEntityAnnotationAxioms(OWLEntity entity) {
        if (owlEntityAnnotationAxiomsByEntity == null) {
            buildEntityAnnotationAxiomsByEntityIndex();
        }
        return getAxioms(entity, owlEntityAnnotationAxiomsByEntity, false);
    }


    /**
     * Gets the annotations whose subject is this ontology - i.e. annotations on
     * this ontology.
     */
    public Set<OWLOntologyAnnotationAxiom> getAnnotations(OWLOntology ontology) {
        return getAxioms(ontology, owlOntologyAnnotationAxiomsByOntology, false);
    }


    public Set<OWLOntologyAnnotationAxiom> getOntologyAnnotationAxioms() {
        Set<OWLOntologyAnnotationAxiom> result = new HashSet<OWLOntologyAnnotationAxiom>();
        for(Set<OWLOntologyAnnotationAxiom> annos : owlOntologyAnnotationAxiomsByOntology.values()) {
            result.addAll(annos);
        }
        return result;
    }


    public Set<OWLClassAxiom> getGeneralClassAxioms() {
        return Collections.unmodifiableSet(generalClassAxioms);
    }


    public Set<OWLObjectPropertyChainSubPropertyAxiom> getPropertyChainSubPropertyAxioms() {
        return Collections.unmodifiableSet(propertyChainSubPropertyAxioms);
    }


    public boolean containsAxiom(OWLAxiom axiom) {
        return allAxioms.contains(axiom);
    }


    private Map<OWLClass, Set<OWLAxiom>> getOWLClassReferences() {
        if (owlClassReferences == null) {
            buildOWLClassReferencesIndex();
        }
        return owlClassReferences;
    }


    private Map<OWLObjectProperty, Set<OWLAxiom>> getOWLObjectPropertyReferences() {
        if (owlObjectPropertyReferences == null) {
            buildOWLObjectPropertyReferencesIndex();
        }
        return owlObjectPropertyReferences;
    }


    private Map<OWLDataProperty, Set<OWLAxiom>> getOWLDataPropertyReferences() {
        if (owlDataPropertyReferences == null) {
            buildOWLDataPropertyReferencesIndex();
        }
        return owlDataPropertyReferences;
    }


    private Map<OWLIndividual, Set<OWLAxiom>> getOWLIndividualReferences() {
        if (owlIndividualReferences == null) {
            buildOWLIndividualReferencesIndex();
        }
        return owlIndividualReferences;
    }


    private Map<OWLDataType, Set<OWLAxiom>> getOWLDataTypeReferences() {
        if (owlDataTypeReferences == null) {
            buildOWLDataTypeReferencesIndex();
        }
        return owlDataTypeReferences;
    }


    public Set<URI> getAnnotationURIs() {
        return Collections.unmodifiableSet(annotationAxiomsByAnnotationURI.keySet());
    }


    public boolean containsClassReference(URI owlClassURI) {
        return getReferencedClasses().contains(getOWLDataFactory().getOWLClass(owlClassURI));
    }


    public boolean containsObjectPropertyReference(URI propURI) {
        return getReferencedObjectProperties().contains(getOWLDataFactory().getOWLObjectProperty(propURI));
    }


    public boolean containsDataPropertyReference(URI propURI) {
        return getReferencedDataProperties().contains(getOWLDataFactory().getOWLDataProperty(propURI));
    }


    public boolean containsIndividualReference(URI individualURI) {
        return getReferencedIndividuals().contains(getOWLDataFactory().getOWLIndividual(individualURI));
    }


    public boolean containsDataTypeReference(URI datatypeURI) {
        return getReferencedDatatypes().contains(getOWLDataFactory().getOWLDataType(datatypeURI));
    }


    public boolean isPunned(URI uri) {
        int count = 0;
        if (containsClassReference(uri)) {
            count++;
        }
        if (containsObjectPropertyReference(uri)) {
            count++;
            if (count > 1) {
                return true;
            }
        }
        if (containsDataPropertyReference(uri)) {
            count++;
            if (count > 1) {
                return true;
            }
        }
        if (containsIndividualReference(uri)) {
            count++;
            if (count > 1) {
                return true;
            }
        }
        if (containsDataTypeReference(uri)) {
            count++;
            if (count > 1) {
                return true;
            }
        }
        return false;
    }


    public boolean containsReference(OWLClass owlClass) {
        return getOWLClassReferences().keySet().contains(owlClass);
    }


    public boolean containsReference(OWLObjectProperty prop) {
        return getOWLObjectPropertyReferences().keySet().contains(prop);
    }


    public boolean containsReference(OWLDataProperty prop) {
        return getOWLDataPropertyReferences().keySet().contains(prop);
    }


    public boolean containsReference(OWLIndividual ind) {
        return getOWLIndividualReferences().keySet().contains(ind);
    }


    public boolean containsReference(OWLDataType dt) {
        return getOWLDataTypeReferences().keySet().contains(dt);
    }


    public boolean containsEntityDeclaration(OWLEntity owlEntity) {
        if (owlDeclarationAxiomMap == null) {
            throw new RuntimeException("NOT IMPLEMENTED");
        }
        return owlDeclarationAxiomMap.containsKey(owlEntity);
    }


    public boolean containsEntityReference(OWLEntity owlEntity) {
        return entityReferenceChecker.containsReference(owlEntity);
    }


    public Set<OWLAxiom> getReferencingAxioms(OWLEntity owlEntity) {
        if (owlEntity instanceof OWLClass) {
            return getAxioms(((OWLClass) owlEntity), getOWLClassReferences(), false);
        }
        if (owlEntity instanceof OWLObjectProperty) {
            return getAxioms(((OWLObjectProperty) owlEntity), getOWLObjectPropertyReferences(), false);
        }
        if (owlEntity instanceof OWLDataProperty) {
            return getAxioms(((OWLDataProperty) owlEntity), getOWLDataPropertyReferences(), false);
        }
        if (owlEntity instanceof OWLIndividual) {
            return getAxioms(((OWLIndividual) owlEntity), getOWLIndividualReferences(), false);
        }
        if (owlEntity instanceof OWLDataType) {
            return getAxioms(((OWLDataType) owlEntity), getOWLDataTypeReferences(), false);
        }
        return Collections.emptySet();
    }


    public Set<OWLClassAxiom> getAxioms(final OWLClass cls) {
        if (classAxiomsByClass == null) {
            buildClassAxiomsByClassIndex();
        }
        return getAxioms(cls, classAxiomsByClass);
    }


    public Set<OWLObjectPropertyAxiom> getAxioms(final OWLObjectPropertyExpression prop) {
        final Set<OWLObjectPropertyAxiom> result = new HashSet<OWLObjectPropertyAxiom>(50);

        addAxiomToSet(getAntiSymmetricObjectPropertyAxiom(prop), result);
        addAxiomToSet(getReflexiveObjectPropertyAxiom(prop), result);
        addAxiomToSet(getSymmetricObjectPropertyAxiom(prop), result);
        addAxiomToSet(getIrreflexiveObjectPropertyAxiom(prop), result);
        addAxiomToSet(getTransitiveObjectPropertyAxiom(prop), result);
        addAxiomToSet(getInverseFunctionalObjectPropertyAxiom(prop), result);
        addAxiomToSet(getFunctionalObjectPropertyAxiom(prop), result);
        result.addAll(getInverseObjectPropertyAxioms(prop));
        result.addAll(getObjectPropertyDomainAxioms(prop));
        result.addAll(getEquivalentObjectPropertiesAxioms(prop));
        result.addAll(getDisjointObjectPropertiesAxiom(prop));
        result.addAll(getObjectPropertyRangeAxioms(prop));
        result.addAll(getObjectSubPropertyAxiomsForLHS(prop));
        return result;
    }


    public Set<OWLDataPropertyAxiom> getAxioms(final OWLDataProperty prop) {
        final Set<OWLDataPropertyAxiom> result = createSet();
        for (OWLAxiom ax : getAxioms(prop, getOWLDataPropertyReferences(), false)) {
            ax.accept(new OWLAxiomVisitorAdapter() {

                public void visit(OWLDataPropertyDomainAxiom axiom) {
                    if (axiom.getProperty().equals(prop)) {
                        result.add(axiom);
                    }
                }


                public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
                    if (axiom.getProperties().contains(prop)) {
                        result.add(axiom);
                    }
                }


                public void visit(OWLDisjointDataPropertiesAxiom axiom) {
                    if (axiom.getProperties().contains(prop)) {
                        result.add(axiom);
                    }
                }


                public void visit(OWLDataPropertyRangeAxiom axiom) {
                    if (axiom.getProperty().equals(axiom.getProperty())) {
                        result.add(axiom);
                    }
                }


                public void visit(OWLFunctionalDataPropertyAxiom axiom) {
                    if (axiom.getProperty().equals(prop)) {
                        result.add(axiom);
                    }
                }


                public void visit(OWLDataSubPropertyAxiom axiom) {
                    if (axiom.getSubProperty().equals(prop)) {
                        result.add(axiom);
                    }
                }
            });
        }
        return result;
    }


    public Set<OWLIndividualAxiom> getAxioms(final OWLIndividual individual) {
        final Set<OWLIndividualAxiom> result = new TreeSet<OWLIndividualAxiom>();
        result.addAll(getClassAssertionAxioms(individual));
        result.addAll(getObjectPropertyAssertionAxioms(individual));
        result.addAll(getDataPropertyAssertionAxioms(individual));
        result.addAll(getNegativeObjectPropertyAssertionAxioms(individual));
        result.addAll(getNegativeDataPropertyAssertionAxioms(individual));
        result.addAll(getSameIndividualAxioms(individual));
        result.addAll(getDifferentIndividualAxioms(individual));
        return result;
    }


    public Set<OWLNamedObject> getReferencedObjects() {
        Set<OWLNamedObject> result = new TreeSet<OWLNamedObject>();
        result.addAll(owlClassReferences.keySet());
        // Consider doing this in a more efficient way (although typically, the number of
        // properties in an ontology isn't large)
        for (OWLObjectPropertyExpression prop : owlObjectPropertyReferences.keySet()) {
            if (!prop.isAnonymous()) {
                result.add((OWLObjectProperty) prop);
            }
        }
        result.addAll(owlDataPropertyReferences.keySet());
        result.addAll(owlIndividualReferences.keySet());
        return result;
    }


    public Set<OWLEntity> getReferencedEntities() {
        // We might want to cache this for performance reasons,
        // but I'm not sure right now.
        Set<OWLEntity> entities = new TreeSet<OWLEntity>();
        entities.addAll(getReferencedClasses());
        entities.addAll(getReferencedObjectProperties());
        entities.addAll(getReferencedDataProperties());
        entities.addAll(getReferencedIndividuals());
        return entities;
    }


    public Set<OWLClass> getReferencedClasses() {
        return Collections.unmodifiableSet(getOWLClassReferences().keySet());
    }


    public Set<OWLObjectProperty> getReferencedObjectProperties() {
        return Collections.unmodifiableSet(getOWLObjectPropertyReferences().keySet());
    }


    public Set<OWLDataProperty> getReferencedDataProperties() {
        return Collections.unmodifiableSet(getOWLDataPropertyReferences().keySet());
    }


    public Set<OWLIndividual> getReferencedIndividuals() {
        return Collections.unmodifiableSet(getOWLIndividualReferences().keySet());
    }


    public Set<OWLDataType> getReferencedDatatypes() {
        return Collections.unmodifiableSet(getOWLDataTypeReferences().keySet());
    }


    public Set<OWLImportsDeclaration> getImportsDeclarations() {
        return Collections.unmodifiableSet(owlImportsAnnotationAxioms);
    }


    public Set<OWLOntology> getImports(OWLOntologyManager ontologyManager) throws UnknownOWLOntologyException {
        return ontologyManager.getImports(this);
    }


    public Set<OWLSubClassAxiom> getSubClassAxiomsForLHS(OWLClass cls) {
        if (subClassAxiomsByLHS == null) {
            buildSubClassAxiomsByLHSIndex();
        }
        return getAxioms(cls, subClassAxiomsByLHS);
    }


    public Set<OWLSubClassAxiom> getSubClassAxiomsForRHS(OWLClass cls) {
        if (subClassAxiomsByRHS == null) {
            buildSubClassAxiomsByRHSIndex();
        }
        return getAxioms(cls, subClassAxiomsByRHS);
    }


    public Set<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(OWLClass cls) {
        if (equivalentClassesAxiomsByClass == null) {
            buildEquivalentClassesAxiomIndex();
        }
        return getAxioms(cls, equivalentClassesAxiomsByClass);
    }


    public Set<OWLDisjointClassesAxiom> getDisjointClassesAxioms(OWLClass cls) {
        return getAxioms(cls, disjointClassesAxiomsByClass);
    }


    public Set<OWLDisjointUnionAxiom> getDisjointUnionAxioms(OWLClass owlClass) {
        return getAxioms(owlClass, disjointUnionAxiomsByClass);
    }


    // Object properties
    public Set<OWLObjectSubPropertyAxiom> getObjectSubPropertyAxiomsForLHS(OWLObjectPropertyExpression property) {
        if (objectSubPropertyAxiomsByLHS == null) {
            buildObjectSubPropertyAxiomsByLHSIndex();
        }
        return getAxioms(property, objectSubPropertyAxiomsByLHS);
    }


    public Set<OWLObjectSubPropertyAxiom> getObjectSubPropertyAxiomsForRHS(OWLObjectPropertyExpression property) {
        if (objectSubPropertyAxiomsByRHS == null) {
            buildObjectSubPropertyAxiomsByRHSIndex();
        }
        return getAxioms(property, objectSubPropertyAxiomsByRHS);
    }


    public Set<OWLObjectPropertyDomainAxiom> getObjectPropertyDomainAxioms(OWLObjectPropertyExpression property) {
        if (objectPropertyRangeAxiomsByProperty == null) {
            buildObjectPropertyDomainAxiomsByPropertyIndex();
        }
        return getAxioms(property, objectPropertyDomainAxiomsByProperty);
    }


    public Set<OWLObjectPropertyRangeAxiom> getObjectPropertyRangeAxioms(OWLObjectPropertyExpression property) {
        if (objectPropertyRangeAxiomsByProperty == null) {
            buildObjectPropertyRangeAxiomsByPropertyIndex();
        }
        return getAxioms(property, objectPropertyRangeAxiomsByProperty);
    }


    public Set<OWLInverseObjectPropertiesAxiom> getInverseObjectPropertyAxioms(
            OWLObjectPropertyExpression property) {
        return getAxioms(property, inversePropertyAxiomsByProperty);
    }


    public Set<OWLEquivalentObjectPropertiesAxiom> getEquivalentObjectPropertiesAxioms(
            OWLObjectPropertyExpression property) {
        if (equivalentObjectPropertyAxiomsByProperty == null) {
            buildEquivalentObjectPropertiesAxiomsByPropertyIndex();
        }
        return getAxioms(property, equivalentObjectPropertyAxiomsByProperty);
    }


    public Set<OWLDisjointObjectPropertiesAxiom> getDisjointObjectPropertiesAxiom(
            OWLObjectPropertyExpression property) {
        if (disjointObjectPropertyAxiomsByProperty == null) {
            buildDisjointObjectPropertiesAxiomsByPropertyIndex();
        }
        return getAxioms(property, disjointObjectPropertyAxiomsByProperty);
    }


    public OWLFunctionalObjectPropertyAxiom getFunctionalObjectPropertyAxiom(
            OWLObjectPropertyExpression property) {
        return functionalObjectPropertyAxiomsByProperty.get(property);
    }


    public OWLInverseFunctionalObjectPropertyAxiom getInverseFunctionalObjectPropertyAxiom(
            OWLObjectPropertyExpression property) {
        return inverseFunctionalPropertyAxiomsByProperty.get(property);
    }


    public OWLSymmetricObjectPropertyAxiom getSymmetricObjectPropertyAxiom(OWLObjectPropertyExpression property) {
        return symmetricPropertyAxiomsByProperty.get(property);
    }


    public OWLAntiSymmetricObjectPropertyAxiom getAntiSymmetricObjectPropertyAxiom(
            OWLObjectPropertyExpression property) {
        return antiSymmetricPropertyAxiomsByProperty.get(property);
    }


    public OWLReflexiveObjectPropertyAxiom getReflexiveObjectPropertyAxiom(OWLObjectPropertyExpression property) {
        return reflexivePropertyAxiomsByProperty.get(property);
    }


    public OWLIrreflexiveObjectPropertyAxiom getIrreflexiveObjectPropertyAxiom(
            OWLObjectPropertyExpression property) {
        return irreflexivePropertyAxiomsByProperty.get(property);
    }


    public OWLTransitiveObjectPropertyAxiom getTransitiveObjectPropertyAxiom(
            OWLObjectPropertyExpression property) {
        return transitivePropertyAxiomsByProperty.get(property);
    }


    public OWLFunctionalDataPropertyAxiom getFunctionalDataPropertyAxiom(OWLDataPropertyExpression property) {
        return functionalDataPropertyAxiomsByProperty.get(property);
    }


    public Set<OWLDataSubPropertyAxiom> getDataSubPropertyAxiomsForLHS(OWLDataProperty lhsProperty) {
        return getAxioms(lhsProperty, dataSubPropertyAxiomsByLHS);
    }


    public Set<OWLDataSubPropertyAxiom> getDataSubPropertyAxiomsForRHS(OWLDataPropertyExpression property) {
        return getAxioms(property, dataSubPropertyAxiomsByRHS);
    }


    public Set<OWLDataPropertyDomainAxiom> getDataPropertyDomainAxioms(OWLDataProperty property) {
        return getAxioms(property, dataPropertyDomainAxiomsByProperty);
    }


    public Set<OWLDataPropertyRangeAxiom> getDataPropertyRangeAxiom(OWLDataProperty property) {
        return getAxioms(property, dataPropertyRangeAxiomsByProperty);
    }


    public Set<OWLEquivalentDataPropertiesAxiom> getEquivalentDataPropertiesAxiom(OWLDataProperty property) {
        return getAxioms(property, equivalentDataPropertyAxiomsByProperty);
    }


    public Set<OWLDisjointDataPropertiesAxiom> getDisjointDataPropertiesAxiom(OWLDataProperty property) {
        return getAxioms(property, disjointDataPropertyAxiomsByProperty);
    }

    ////


    public Set<OWLClassAssertionAxiom> getClassAssertionAxioms(OWLIndividual individual) {
        if (typeAxiomsByIndividual == null) {
            buildTypeAxiomsByIndividualIndex();
        }
        return getAxioms(individual, typeAxiomsByIndividual);
    }


    public Set<OWLClassAssertionAxiom> getClassAssertionAxioms(OWLClass type) {
        if (typeAxiomsByClass == null) {
            buildTypeAxiomsByClassIndex();
        }
        return getAxioms(type, typeAxiomsByClass);
    }


    public Set<OWLDataPropertyAssertionAxiom> getDataPropertyAssertionAxioms(OWLIndividual individual) {
        if (dataPropertyAssertionsByIndividual == null) {
            buildDataRelationshipsByIndividualIndex();
        }
        return getAxioms(individual, dataPropertyAssertionsByIndividual);
    }


    public Set<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertionAxioms(OWLIndividual individual) {
        if (objectPropertyAssertionsByIndividual == null) {
            buildObjectRelationshipsByIndividualIndex();
        }
        return getAxioms(individual, objectPropertyAssertionsByIndividual);
    }


    public Set<OWLNegativeObjectPropertyAssertionAxiom> getNegativeObjectPropertyAssertionAxioms(
            OWLIndividual individual) {
        if (negativeObjectPropertyAssertionAxiomsByIndividual == null) {
            buildNegativeObjectPropertyAssertionAxiomsByIndvidualIndex();
        }
        return getAxioms(individual, negativeObjectPropertyAssertionAxiomsByIndividual);
    }


    public Set<OWLNegativeDataPropertyAssertionAxiom> getNegativeDataPropertyAssertionAxioms(
            OWLIndividual individual) {
        if (negativeDataPropertyAssertionAxiomsByIndividual == null) {
            buildNegativeDataPropertyAssertionAxiomsByIndvidualIndex();
        }
        return getAxioms(individual, negativeDataPropertyAssertionAxiomsByIndividual);
    }


    public Set<OWLSameIndividualsAxiom> getSameIndividualAxioms(OWLIndividual individual) {
        return getAxioms(individual, sameIndividualsAxiomsByIndividual);
    }


    public Set<OWLDifferentIndividualsAxiom> getDifferentIndividualAxioms(OWLIndividual individual) {
        return getAxioms(individual, differentIndividualsAxiomsByIndividual);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///
    /// Ontology Change handling mechanism
    ///
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ChangeAxiomVisitor changeVisitor = new ChangeAxiomVisitor();

    private OWLOntologyChangeFilter changeFilter = new OWLOntologyChangeFilter();


    public List<OWLOntologyChange> applyChange(OWLOntologyChange change) {
        List<OWLOntologyChange> appliedChanges = new ArrayList<OWLOntologyChange>(1);
        changeFilter.reset();
        change.accept(changeFilter);
        appliedChanges.addAll(changeFilter.getAppliedChanges());
        return appliedChanges;
    }

    public List<OWLOntologyChange> applyChanges(List<OWLOntologyChange> changes) {
        List<OWLOntologyChange> appliedChanges = new ArrayList<OWLOntologyChange>();
        for (OWLOntologyChange change : changes) {
            change.accept(changeFilter);
            appliedChanges.addAll(changeFilter.getAppliedChanges());
            changeFilter.reset();
        }
        return appliedChanges;
    }


    private class OWLOntologyChangeFilter implements OWLOntologyChangeVisitor {

        private List<OWLOntologyChange> appliedChanges;


        public OWLOntologyChangeFilter() {
            appliedChanges = new ArrayList<OWLOntologyChange>();
        }


        public List<OWLOntologyChange> getAppliedChanges() {
            return appliedChanges;
        }


        public void reset() {
            appliedChanges.clear();
        }


        public void visit(RemoveAxiom change) {
            OWLAxiom axiom = change.getAxiom();
            if (allAxioms.contains(axiom)) {
                changeVisitor.setAddAxiom(false);
                axiom.accept(changeVisitor);
                appliedChanges.add(change);
                if (axiom.isLogicalAxiom()) {
                    logicalAxioms.remove((OWLLogicalAxiom) axiom);
                }
                handleAxiomRemoved(axiom);
            }
        }


        public void visit(SetOntologyURI change) {
            appliedChanges.add(change);
            ontologyURI = change.getNewURI();
        }


        public void visit(AddAxiom change) {
            OWLAxiom axiom = change.getAxiom();
            if (!allAxioms.contains(axiom)) {
                changeVisitor.setAddAxiom(true);
                axiom.accept(changeVisitor);
                appliedChanges.add(change);
                if (axiom.isLogicalAxiom()) {
                    logicalAxioms.add((OWLLogicalAxiom) axiom);
                }
                handleAxiomAdded(axiom);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Handlers for when axioms are added/removed, which perform various global indexing
    // housekeeping tasks.
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////

    private OWLEntityCollector entityCollector = new OWLEntityCollector();

    private OWLNamedObjectReferenceAdder referenceAdder = new OWLNamedObjectReferenceAdder();


    private void handleAxiomAdded(OWLAxiom axiom) {
        allAxioms.add(axiom);
        entityCollector.reset();
        axiom.accept(entityCollector);
        for (OWLEntity object : entityCollector.getObjects()) {
            referenceAdder.setAxiom(axiom);
            object.accept(referenceAdder);
        }
    }


    private OWLNamedObjectReferenceRemover referenceRemover = new OWLNamedObjectReferenceRemover();


    private void handleAxiomRemoved(OWLAxiom axiom) {
        allAxioms.remove(axiom);
        entityCollector.reset();
        axiom.accept(entityCollector);
        for (OWLEntity object : entityCollector.getObjects()) {
            referenceRemover.setAxiom(axiom);
            object.accept(referenceRemover);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // An inner helper class that adds the appropriate references indexes for a given axiom
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private class OWLNamedObjectReferenceAdder implements OWLEntityVisitor {

        private OWLAxiom axiom;


        public void setAxiom(OWLAxiom axiom) {
            this.axiom = axiom;
        }


        public void visit(OWLClass owlClass) {
            addToIndexedSet(owlClass, owlClassReferences, axiom);
        }


        public void visit(OWLObjectProperty property) {
            addToIndexedSet(property, owlObjectPropertyReferences, axiom);
        }


        public void visit(OWLDataProperty property) {
            addToIndexedSet(property, owlDataPropertyReferences, axiom);
        }


        public void visit(OWLIndividual owlIndividual) {
            addToIndexedSet(owlIndividual, owlIndividualReferences, axiom);
        }


        public void visit(OWLOntology ontology) {

        }


        public void visit(OWLDataType dataType) {
            addToIndexedSet(dataType, owlDataTypeReferences, axiom);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // An inner helper class that removes the appropriate references indexes for a given axiom
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private class OWLNamedObjectReferenceRemover implements OWLEntityVisitor {

        private OWLAxiom axiom;


        public void setAxiom(OWLAxiom axiom) {
            this.axiom = axiom;
        }


        public void visit(OWLClass owlClass) {
            removeAxiomFromSet(owlClass, owlClassReferences, axiom, true);
        }


        public void visit(OWLObjectProperty property) {
            removeAxiomFromSet(property, owlObjectPropertyReferences, axiom, true);
        }


        public void visit(OWLDataProperty property) {
            removeAxiomFromSet(property, owlDataPropertyReferences, axiom, true);
        }


        public void visit(OWLIndividual owlIndividual) {
            removeAxiomFromSet(owlIndividual, owlIndividualReferences, axiom, true);
        }


        public void visit(OWLOntology ontology) {
        }


        public void visit(OWLDataType dataType) {
            removeAxiomFromSet(dataType, owlDataTypeReferences, axiom, true);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Add/Remove axiom mechanism.  Each axiom gets visited by a visitor, which adds the axiom
    // to the appropriate index.
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private class ChangeAxiomVisitor implements OWLAxiomVisitor {

        private boolean addAxiom = false;


        public void setAddAxiom(boolean addAxiom) {
            this.addAxiom = addAxiom;
        }


        public void visit(OWLSubClassAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlClassAxioms);
                addToIndexedSet(SUBCLASS, axiomsByType, axiom);
                if (!axiom.getSubClass().isAnonymous()) {
                    OWLClass subClass = (OWLClass) axiom.getSubClass();
                    addToIndexedSet(subClass, subClassAxiomsByLHS, axiom);
                    addToIndexedSet(subClass, classAxiomsByClass, axiom);
                }
                else {
                    generalClassAxioms.add(axiom);
                }
                if (!axiom.getSuperClass().isAnonymous()) {
                    addToIndexedSet((OWLClass) axiom.getSuperClass(), subClassAxiomsByRHS, axiom);
                }
            }
            else {
                removeAxiomFromSet(axiom, owlClassAxioms);
                removeAxiomFromSet(SUBCLASS, axiomsByType, axiom, true);
                if (!axiom.getSubClass().isAnonymous()) {
                    OWLClass subClass = (OWLClass) axiom.getSubClass();
                    removeAxiomFromSet(subClass, subClassAxiomsByLHS, axiom, true);
                    removeAxiomFromSet(subClass, classAxiomsByClass, axiom, true);
                }
                else {
                    removeAxiomFromSet(axiom, generalClassAxioms);
                }
                if(!axiom.getSuperClass().isAnonymous()) {
                    removeAxiomFromSet(axiom.getSuperClass().asOWLClass(), subClassAxiomsByRHS, axiom, true);
                }
            }
        }


        public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlIndividualAxioms);
                addToIndexedSet(axiom.getSubject(), negativeObjectPropertyAssertionAxiomsByIndividual, axiom);
                addToIndexedSet(NEGATIVE_OBJECT_PROPERTY_ASSERTION, axiomsByType, axiom);
            }
            else {
                removeAxiomFromSet(NEGATIVE_OBJECT_PROPERTY_ASSERTION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                removeAxiomFromSet(axiom.getSubject(), negativeObjectPropertyAssertionAxiomsByIndividual, axiom, true);
            }
        }


        public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), antiSymmetricPropertyAxiomsByProperty, axiom);
                addToIndexedSet(ANTI_SYMMETRIC_OBJECT_PROPERTY, axiomsByType, axiom);
            }
            else {
                removeAxiomFromSet(ANTI_SYMMETRIC_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), antiSymmetricPropertyAxiomsByProperty);
            }
        }


        public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), reflexivePropertyAxiomsByProperty, axiom);
                addToIndexedSet(REFLEXIVE_OBJECT_PROPERTY, axiomsByType, axiom);
            }
            else {
                removeAxiomFromSet(REFLEXIVE_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), reflexivePropertyAxiomsByProperty);
            }
        }


        public void visit(OWLDisjointClassesAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlClassAxioms);
                addToIndexedSet(DISJOINT_CLASSES, axiomsByType, axiom);
                boolean allAnon = true;
                // Index against each named class in the axiom
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        OWLClass cls = (OWLClass) desc;
                        addToIndexedSet(cls, disjointClassesAxiomsByClass, axiom);
                        addToIndexedSet(cls, classAxiomsByClass, axiom);
                        allAnon = false;
                    }
                }
                if (allAnon) {
                    generalClassAxioms.add(axiom);
                }
            }
            else {
                removeAxiomFromSet(DISJOINT_CLASSES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlClassAxioms);
                boolean allAnon = true;
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        OWLClass cls = (OWLClass) desc;
                        removeAxiomFromSet(cls, disjointClassesAxiomsByClass, axiom, true);
                        removeAxiomFromSet(cls, classAxiomsByClass, axiom, true);
                        allAnon = false;
                    }
                }
                if (allAnon) {
                    generalClassAxioms.remove(axiom);
                }
            }
        }


        public void visit(OWLDataPropertyDomainAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(DATA_PROPERTY_DOMAIN, axiomsByType, axiom);
                addAxiomToSet(axiom, owlDataPropertyAxioms);
                addToIndexedSet(axiom.getProperty(), dataPropertyDomainAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(DATA_PROPERTY_DOMAIN, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlDataPropertyAxioms);
                removeAxiomFromSet(axiom.getProperty(), dataPropertyDomainAxiomsByProperty, axiom, true);
            }
        }


        public void visit(OWLImportsDeclaration axiom) {
            if (addAxiom) {
                addToIndexedSet(IMPORTS_DECLARATION, axiomsByType, axiom);
                owlImportsAnnotationAxioms.add(axiom);
            }
            else {
                removeAxiomFromSet(IMPORTS_DECLARATION, axiomsByType, axiom, true);
                owlImportsAnnotationAxioms.remove(axiom);
            }
        }


        public void visit(OWLAxiomAnnotationAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(AXIOM_ANNOTATION, axiomsByType, axiom);
                addAxiomToSet(axiom, annotationAxioms);
                addAxiomToSet(axiom, owlAxiomAnnotationAxioms);
                addToIndexedSet(axiom.getAnnotation().getAnnotationURI(), annotationAxiomsByAnnotationURI, axiom);
                addToIndexedSet(axiom.getSubject(), owlAxiomAnnotationAxiomByAxiom, axiom);
            }
            else {
                removeAxiomFromSet(AXIOM_ANNOTATION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, annotationAxioms);
                removeAxiomFromSet(axiom, owlAxiomAnnotationAxioms);
                removeAxiomFromSet(axiom.getAnnotation().getAnnotationURI(),
                                   annotationAxiomsByAnnotationURI,
                                   axiom,
                                   true);
                removeAxiomFromSet(axiom.getSubject(), owlAxiomAnnotationAxiomByAxiom, axiom, true);
            }
        }


        public void visit(OWLObjectPropertyDomainAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(OBJECT_PROPERTY_DOMAIN, axiomsByType, axiom);
                if (axiom.getProperty() instanceof OWLObjectProperty) {
                    addToIndexedSet((OWLObjectProperty) axiom.getProperty(),
                                    objectPropertyDomainAxiomsByProperty,
                                    axiom);
                }
            }
            else {
                removeAxiomFromSet(OBJECT_PROPERTY_DOMAIN, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                if (axiom.getProperty() instanceof OWLObjectProperty) {
                    removeAxiomFromSet((OWLObjectProperty) axiom.getProperty(),
                                       objectPropertyDomainAxiomsByProperty,
                                       axiom,
                                       true);
                }
            }
        }


        public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(EQUIVALENT_OBJECT_PROPERTIES, axiomsByType, axiom);
                for (OWLObjectPropertyExpression prop : axiom.getProperties()) {
                    addToIndexedSet(prop, equivalentObjectPropertyAxiomsByProperty, axiom);
                }
            }
            else {
                removeAxiomFromSet(EQUIVALENT_OBJECT_PROPERTIES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                for (OWLObjectPropertyExpression prop : axiom.getProperties()) {
                    removeAxiomFromSet(prop, equivalentObjectPropertyAxiomsByProperty, axiom, true);
                }
            }
        }


        public void visit(OWLInverseObjectPropertiesAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(INVERSE_OBJECT_PROPERTIES, axiomsByType, axiom);
                addToIndexedSet(axiom.getFirstProperty(), inversePropertyAxiomsByProperty, axiom);
                addToIndexedSet(axiom.getSecondProperty(), inversePropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(INVERSE_OBJECT_PROPERTIES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromSet(axiom.getFirstProperty(), inversePropertyAxiomsByProperty, axiom, false);
                removeAxiomFromSet(axiom.getSecondProperty(), inversePropertyAxiomsByProperty, axiom, false);
            }
        }


        public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlIndividualAxioms);
                addToIndexedSet(axiom.getSubject(), negativeDataPropertyAssertionAxiomsByIndividual, axiom);
                addToIndexedSet(NEGATIVE_DATA_PROPERTY_ASSERTION, axiomsByType, axiom);
            }
            else {
                removeAxiomFromSet(NEGATIVE_DATA_PROPERTY_ASSERTION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                removeAxiomFromSet(axiom.getSubject(), negativeDataPropertyAssertionAxiomsByIndividual, axiom, true);
            }
        }


        public void visit(OWLDifferentIndividualsAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlIndividualAxioms);
                for (OWLIndividual ind : axiom.getIndividuals()) {
                    addToIndexedSet(ind, differentIndividualsAxiomsByIndividual, axiom);
                    addToIndexedSet(DIFFERENT_INDIVIDUALS, axiomsByType, axiom);
                }
            }
            else {
                removeAxiomFromSet(DIFFERENT_INDIVIDUALS, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                for (OWLIndividual ind : axiom.getIndividuals()) {
                    removeAxiomFromSet(ind, differentIndividualsAxiomsByIndividual, axiom, true);
                }
            }
        }


        public void visit(OWLDisjointDataPropertiesAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlDataPropertyAxioms);
                addToIndexedSet(DISJOINT_DATA_PROPERTIES, axiomsByType, axiom);
                for (OWLDataPropertyExpression prop : axiom.getProperties()) {
                    addToIndexedSet(prop, disjointDataPropertyAxiomsByProperty, axiom);
                }
            }
            else {
                removeAxiomFromSet(DISJOINT_DATA_PROPERTIES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlDataPropertyAxioms);
                for (OWLDataPropertyExpression prop : axiom.getProperties()) {
                    removeAxiomFromSet(prop, disjointDataPropertyAxiomsByProperty, axiom, true);
                }
            }
        }


        public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(DISJOINT_OBJECT_PROPERTIES, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                for (OWLObjectPropertyExpression prop : axiom.getProperties()) {
                    addToIndexedSet(prop, disjointObjectPropertyAxiomsByProperty, axiom);
                }
            }
            else {
                removeAxiomFromSet(DISJOINT_OBJECT_PROPERTIES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                for (OWLObjectPropertyExpression prop : axiom.getProperties()) {
                    removeAxiomFromSet(prop, disjointObjectPropertyAxiomsByProperty, axiom, true);
                }
            }
        }


        public void visit(OWLObjectPropertyRangeAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(OBJECT_PROPERTY_RANGE, axiomsByType, axiom);
                addToIndexedSet(axiom.getProperty(), objectPropertyRangeAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(OBJECT_PROPERTY_RANGE, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromSet(axiom.getProperty(), objectPropertyRangeAxiomsByProperty, axiom, true);
            }
        }


        public void visit(OWLObjectPropertyAssertionAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlIndividualAxioms);
                addToIndexedSet(OBJECT_PROPERTY_ASSERTION, axiomsByType, axiom);
                addToIndexedSet(axiom.getSubject(), objectPropertyAssertionsByIndividual, axiom);
            }
            else {
                removeAxiomFromSet(OBJECT_PROPERTY_ASSERTION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                removeAxiomFromSet(axiom.getSubject(), objectPropertyAssertionsByIndividual, axiom, true);
            }
        }


        public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(FUNCTIONAL_OBJECT_PROPERTY, axiomsByType, axiom);
                addAxiomToMap(axiom.getProperty(), functionalObjectPropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(FUNCTIONAL_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), functionalObjectPropertyAxiomsByProperty);
            }
        }


        public void visit(OWLObjectSubPropertyAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(SUB_OBJECT_PROPERTY, axiomsByType, axiom);
                addToIndexedSet(axiom.getSubProperty(), objectSubPropertyAxiomsByLHS, axiom);
                addToIndexedSet(axiom.getSuperProperty(), objectSubPropertyAxiomsByRHS, axiom);
            }
            else {
                removeAxiomFromSet(SUB_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromSet(axiom.getSubProperty(), objectSubPropertyAxiomsByLHS, axiom, true);
                removeAxiomFromSet(axiom.getSuperProperty(), objectSubPropertyAxiomsByRHS, axiom, true);
            }
        }


        public void visit(OWLDisjointUnionAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlClassAxioms);
                addToIndexedSet(DISJOINT_UNION, axiomsByType, axiom);
                addToIndexedSet(axiom.getOWLClass(), disjointUnionAxiomsByClass, axiom);
                addToIndexedSet(axiom.getOWLClass(), classAxiomsByClass, axiom);
            }
            else {
                removeAxiomFromSet(DISJOINT_UNION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlClassAxioms);
                removeAxiomFromSet(axiom.getOWLClass(), disjointUnionAxiomsByClass, axiom, true);
                removeAxiomFromSet(axiom.getOWLClass(), classAxiomsByClass, axiom, true);
            }
        }


        public void visit(OWLDeclarationAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(DECLARATION, axiomsByType, axiom);
                addAxiomToSet(axiom, owlDeclarationAxioms);
                addToIndexedSet(axiom.getEntity(), owlDeclarationAxiomMap, axiom);
            }
            else {
                removeAxiomFromSet(DECLARATION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlDeclarationAxioms);
                removeAxiomFromSet(axiom.getEntity(), owlDeclarationAxiomMap, axiom, true);
            }
        }


        public void visit(OWLEntityAnnotationAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(ENTITY_ANNOTATION, axiomsByType, axiom);
                addToIndexedSet(axiom.getSubject(), owlEntityAnnotationAxiomsByEntity, axiom);
                addAxiomToSet(axiom, annotationAxioms);
                addToIndexedSet(axiom.getAnnotation().getAnnotationURI(), annotationAxiomsByAnnotationURI, axiom);
            }
            else {
                removeAxiomFromSet(ENTITY_ANNOTATION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom.getSubject(), owlEntityAnnotationAxiomsByEntity, axiom, true);
                removeAxiomFromSet(axiom, annotationAxioms);
                removeAxiomFromSet(axiom.getAnnotation().getAnnotationURI(),
                                   annotationAxiomsByAnnotationURI,
                                   axiom,
                                   true);
            }
        }


        public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(SYMMETRIC_OBJECT_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), symmetricPropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(SYMMETRIC_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), symmetricPropertyAxiomsByProperty);
            }
        }


        public void visit(OWLDataPropertyRangeAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(DATA_PROPERTY_RANGE, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addToIndexedSet(axiom.getProperty(), dataPropertyRangeAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(DATA_PROPERTY_RANGE, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromSet(axiom.getProperty(), dataPropertyRangeAxiomsByProperty, axiom, true);
            }
        }


        public void visit(OWLFunctionalDataPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(FUNCTIONAL_DATA_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlDataPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), functionalDataPropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(FUNCTIONAL_DATA_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlDataPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), functionalDataPropertyAxiomsByProperty);
            }
        }


        public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(EQUIVALENT_DATA_PROPERTIES, axiomsByType, axiom);
                addAxiomToSet(axiom, owlDataPropertyAxioms);
                for (OWLDataPropertyExpression prop : axiom.getProperties()) {
                    addToIndexedSet(prop, equivalentDataPropertyAxiomsByProperty, axiom);
                }
            }
            else {
                removeAxiomFromSet(EQUIVALENT_DATA_PROPERTIES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlDataPropertyAxioms);
                for (OWLDataPropertyExpression prop : axiom.getProperties()) {
                    removeAxiomFromSet(prop, equivalentDataPropertyAxiomsByProperty, axiom, true);
                }
            }
        }


        public void visit(OWLClassAssertionAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlIndividualAxioms);
                addToIndexedSet(axiom.getIndividual(), typeAxiomsByIndividual, axiom);
                addToIndexedSet(CLASS_ASSERTION, axiomsByType, axiom);
                if (!axiom.getDescription().isAnonymous()) {
                    addToIndexedSet((OWLClass) axiom.getDescription(), typeAxiomsByClass, axiom);
                }
            }
            else {
                removeAxiomFromSet(CLASS_ASSERTION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                removeAxiomFromSet(axiom.getIndividual(), typeAxiomsByIndividual, axiom, true);
                if (!axiom.getDescription().isAnonymous()) {
                    removeAxiomFromSet((OWLClass) axiom.getDescription(), typeAxiomsByClass, axiom, true);
                }
            }
        }


        public void visit(OWLEquivalentClassesAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlClassAxioms);
                addToIndexedSet(EQUIVALENT_CLASSES, axiomsByType, axiom);
                boolean allAnon = true;
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        addToIndexedSet((OWLClass) desc, equivalentClassesAxiomsByClass, axiom);
                        addToIndexedSet((OWLClass) desc, classAxiomsByClass, axiom);
                        allAnon = false;
                    }
                }
                if (allAnon) {
                    generalClassAxioms.add(axiom);
                }
            }
            else {
                removeAxiomFromSet(EQUIVALENT_CLASSES, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlClassAxioms);
                boolean allAnon = true;
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        removeAxiomFromSet((OWLClass) desc, equivalentClassesAxiomsByClass, axiom, true);
                        removeAxiomFromSet((OWLClass) desc, classAxiomsByClass, axiom, true);
                        allAnon = false;
                    }
                }
                if (allAnon) {
                    generalClassAxioms.remove(axiom);
                }
            }
        }


        public void visit(OWLDataPropertyAssertionAxiom axiom) {
            if (addAxiom) {
                addAxiomToSet(axiom, owlIndividualAxioms);
                addToIndexedSet(DATA_PROPERTY_ASSERTION, axiomsByType, axiom);
                addToIndexedSet(axiom.getSubject(), dataPropertyAssertionsByIndividual, axiom);
            }
            else {
                removeAxiomFromSet(DATA_PROPERTY_ASSERTION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                removeAxiomFromSet(axiom.getSubject(), dataPropertyAssertionsByIndividual, axiom, true);
            }
        }


        public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(TRANSITIVE_OBJECT_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), transitivePropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(TRANSITIVE_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), transitivePropertyAxiomsByProperty);
            }
        }


        public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(IRREFLEXIVE_OBJECT_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), irreflexivePropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(IRREFLEXIVE_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), irreflexivePropertyAxiomsByProperty);
            }
        }


        public void visit(OWLDataSubPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(SUB_DATA_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlDataPropertyAxioms);
                addToIndexedSet(axiom.getSubProperty(), dataSubPropertyAxiomsByLHS, axiom);
                addToIndexedSet(axiom.getSuperProperty(), dataSubPropertyAxiomsByRHS, axiom);
            }
            else {
                removeAxiomFromSet(SUB_DATA_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlDataPropertyAxioms);
                removeAxiomFromSet(axiom.getSubProperty(), dataSubPropertyAxiomsByLHS, axiom, true);
                removeAxiomFromSet(axiom.getSuperProperty(), dataSubPropertyAxiomsByRHS, axiom, true);
            }
        }


        public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(INVERSE_FUNCTIONAL_OBJECT_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToMap(axiom.getProperty(), inverseFunctionalPropertyAxiomsByProperty, axiom);
            }
            else {
                removeAxiomFromSet(INVERSE_FUNCTIONAL_OBJECT_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromMap(axiom.getProperty(), inverseFunctionalPropertyAxiomsByProperty);
            }
        }


        public void visit(OWLSameIndividualsAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(SAME_INDIVIDUAL, axiomsByType, axiom);
                addAxiomToSet(axiom, owlIndividualAxioms);
                for (OWLIndividual ind : axiom.getIndividuals()) {
                    addToIndexedSet(ind, sameIndividualsAxiomsByIndividual, axiom);
                }
            }
            else {
                removeAxiomFromSet(SAME_INDIVIDUAL, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlIndividualAxioms);
                for (OWLIndividual ind : axiom.getIndividuals()) {
                    removeAxiomFromSet(ind, sameIndividualsAxiomsByIndividual, axiom, true);
                }
            }
        }


        public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(PROPERTY_CHAIN_SUB_PROPERTY, axiomsByType, axiom);
                addAxiomToSet(axiom, owlObjectPropertyAxioms);
                addAxiomToSet(axiom, propertyChainSubPropertyAxioms);
            }
            else {
                removeAxiomFromSet(PROPERTY_CHAIN_SUB_PROPERTY, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom, owlObjectPropertyAxioms);
                removeAxiomFromSet(axiom, propertyChainSubPropertyAxioms);
            }
        }


        public void visit(OWLOntologyAnnotationAxiom axiom) {
            if (addAxiom) {
                addToIndexedSet(ONTOLOGY_ANNOTATION, axiomsByType, axiom);
                addToIndexedSet(axiom.getSubject(), owlOntologyAnnotationAxiomsByOntology, axiom);
                addAxiomToSet(axiom, annotationAxioms);
                addToIndexedSet(axiom.getAnnotation().getAnnotationURI(), annotationAxiomsByAnnotationURI, axiom);
            }
            else {
                removeAxiomFromSet(ONTOLOGY_ANNOTATION, axiomsByType, axiom, true);
                removeAxiomFromSet(axiom.getSubject(), owlOntologyAnnotationAxiomsByOntology, axiom, true);
                removeAxiomFromSet(axiom, annotationAxioms);
                removeAxiomFromSet(axiom.getAnnotation().getAnnotationURI(),
                                   annotationAxiomsByAnnotationURI,
                                   axiom,
                                   true);
            }
        }


        public void visit(SWRLRule rule) {
            if (addAxiom) {
                addToIndexedSet(SWRL_RULE, axiomsByType, rule);
                addAxiomToSet(rule, ruleAxioms);
            }
            else {
                removeAxiomFromSet(SWRL_RULE, axiomsByType, rule, true);
                removeAxiomFromSet(rule, ruleAxioms);
            }
        }
    }


    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(OWLNamedObjectVisitor visitor) {
        visitor.visit(this);
    }

    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///
    /// Utility methods for getting/setting various values in maps and sets
    ///
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static <K extends OWLObject, V extends OWLAxiom> Set<V> getAxioms(K key, Map<K, Set<V>> map) {
        Set<V> axioms = map.get(key);
        if (axioms != null) {
            return Collections.unmodifiableSet(axioms);
        }
        else {
            return Collections.emptySet();
        }
    }


    private static <K, V extends OWLAxiom> Set<V> getAxioms(K key, Map<K, Set<V>> map,
                                                                              boolean create) {
        Set<V> axioms = map.get(key);
        if (axioms == null) {
            axioms = new FakeSet<V>();
            if (create) {
                map.put(key, axioms);
            }
        }
        return axioms;
    }


    /**
     * Adds an axiom to a set contained in a map, which maps some key (e.g. an entity such as and individual,
     * class etc.) to the set of axioms.
     * @param key   The key that indexes the set of axioms
     * @param map   The map, which maps the key to a set of axioms, to which the axiom will be added.
     * @param axiom The axiom to be added
     */
    private static <K, V extends OWLAxiom> void addToIndexedSet(K key, Map<K, Set<V>> map, V axiom) {
        if (map == null) {
            return;
        }
        Set<V> axioms = map.get(key);
        if (axioms == null) {
            axioms = new FakeSet<V>();
            map.put(key, axioms);
        }
        axioms.add(axiom);
    }


    /**
     * Removes an axiom from a map that maps some key to the axiom
     */
    private static <K extends OWLObject, V extends OWLAxiom> void addAxiomToMap(K key, Map<K, V> map, V axiom) {
        map.put(key, axiom);
    }


    /**
     * Removes an axiom from a set of axioms, which is the value for a specified key in a specified map.
     * @param key   The key that indexes the set of axioms.
     * @param map   The map, which maps keys to sets of axioms.
     * @param axiom The axiom to remove from the set of axioms.
     */
    private static <K, V extends OWLAxiom> void removeAxiomFromSet(K key, Map<K, Set<V>> map, V axiom,
                                                                   boolean removeSetIfEmpty) {
        if (map == null) {
            return;
        }
        Set<V> axioms = map.get(key);
        if (axioms != null) {
            axioms.remove(axiom);
            if (removeSetIfEmpty) {
                if (axioms.isEmpty()) {
                    map.remove(key);
                }
            }
        }
    }


    /**
     * A convenience method that adds an axiom to a set, but
     * checks that the set isn't null before the axiom is added.
     * This is needed because many of the indexing sets are built
     * lazily.
     * @param axiom  The axiom to be added.
     * @param axioms The set of axioms that the axiom should be added to.  May
     *               be <code>null</code>.
     */
    private static <K extends OWLAxiom> void addAxiomToSet(K axiom, Set<K> axioms) {
        if (axioms != null && axiom != null) {
            axioms.add(axiom);
        }
    }


    private static <K extends OWLAxiom> void removeAxiomFromSet(K axiom, Set<K> axioms) {
        if (axioms != null) {
            axioms.remove(axiom);
        }
    }


    private static <K extends OWLObject, V extends OWLAxiom> void removeAxiomFromMap(K key, Map<K, V> map) {
        map.remove(key);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    //// Methods to build indexes
    ////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Builds an index of subclass axioms which are indexed by
     * subclass.
     */
    private void buildSubClassAxiomsByLHSIndex() {
        subClassAxiomsByLHS = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLSubClassAxiom axiom) {
                if (!axiom.getSubClass().isAnonymous()) {
                    addToIndexedSet((OWLClass) axiom.getSubClass(), subClassAxiomsByLHS, axiom);
                }
            }
        });
    }


    private void buildSubClassAxiomsByRHSIndex() {
        subClassAxiomsByRHS = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLSubClassAxiom axiom) {
                if (!axiom.getSuperClass().isAnonymous()) {
                    addToIndexedSet((OWLClass) axiom.getSuperClass(), subClassAxiomsByRHS, axiom);
                }
            }
        });
    }


    private void buildEquivalentClassesAxiomIndex() {
        equivalentClassesAxiomsByClass = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLEquivalentClassesAxiom axiom) {
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        addToIndexedSet((OWLClass) desc, equivalentClassesAxiomsByClass, axiom);
                    }
                }
            }
        });
    }


    private void buildClassAxiomsIndex() {
        owlClassAxioms = createSet();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLSubClassAxiom axiom) {
                owlClassAxioms.add(axiom);
            }


            public void visit(OWLDisjointClassesAxiom axiom) {
                owlClassAxioms.add(axiom);
            }


            public void visit(OWLEquivalentClassesAxiom axiom) {
                owlClassAxioms.add(axiom);
            }


            public void visit(OWLDisjointUnionAxiom axiom) {
                owlClassAxioms.add(axiom);
            }
        });
    }


    private void buildObjectPropertyAxiomsIndex() {
        owlObjectPropertyAxioms = createSet();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLObjectPropertyDomainAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLObjectPropertyRangeAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLObjectSubPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }


            public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
                owlObjectPropertyAxioms.add(axiom);
            }
        });
    }


    private void buildDataPropertyAxiomsIndex() {
        owlDataPropertyAxioms = createSet();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLDataPropertyDomainAxiom axiom) {
                owlDataPropertyAxioms.add(axiom);
            }


            public void visit(OWLDisjointDataPropertiesAxiom axiom) {
                owlDataPropertyAxioms.add(axiom);
            }


            public void visit(OWLDataPropertyRangeAxiom axiom) {
                owlDataPropertyAxioms.add(axiom);
            }


            public void visit(OWLFunctionalDataPropertyAxiom axiom) {
                owlDataPropertyAxioms.add(axiom);
            }


            public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
                owlDataPropertyAxioms.add(axiom);
            }


            public void visit(OWLDataSubPropertyAxiom axiom) {
                owlDataPropertyAxioms.add(axiom);
            }
        });
    }


    private void buildIndividualAxiomsIndex() {
        owlIndividualAxioms = createSet();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }


            public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }


            public void visit(OWLDifferentIndividualsAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }


            public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }


            public void visit(OWLClassAssertionAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }


            public void visit(OWLDataPropertyAssertionAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }


            public void visit(OWLSameIndividualsAxiom axiom) {
                owlIndividualAxioms.add(axiom);
            }
        });
    }


    private void buildOWLDeclarationAxiomsIndex() {
        owlDeclarationAxioms = createSet();
        owlDeclarationAxiomMap = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLDeclarationAxiom axiom) {
                owlDeclarationAxioms.add(axiom);
                addToIndexedSet(axiom.getEntity(), owlDeclarationAxiomMap, axiom);
            }
        });
    }


    private void buildAxiomAnnotationAxiomsIndex() {
        owlAxiomAnnotationAxioms = new TreeSet<OWLAxiomAnnotationAxiom>();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLAxiomAnnotationAxiom axiom) {
                owlAxiomAnnotationAxioms.add(axiom);
            }
        });
    }


    private void buildOWLClassReferencesIndex() {
        owlClassReferences = createMap();
        OWLEntityCollector collector = new OWLEntityCollector();
        collector.setCollectClasses(true);
        collector.setCollectDataProperties(false);
        collector.setCollectDataTypes(false);
        collector.setCollectIndividuals(false);
        collector.setCollectObjectProperties(false);
        for (OWLAxiom axiom : allAxioms) {
            collector.reset();
            axiom.accept(collector);
            for (OWLEntity obj : collector.getObjects()) {
                addToIndexedSet((OWLClass) obj, owlClassReferences, axiom);
            }
        }
    }


    private void buildOWLObjectPropertyReferencesIndex() {
        owlObjectPropertyReferences = createMap();
        OWLEntityCollector collector = new OWLEntityCollector();
        collector.setCollectClasses(false);
        collector.setCollectDataProperties(false);
        collector.setCollectDataTypes(false);
        collector.setCollectIndividuals(false);
        collector.setCollectObjectProperties(true);
        for (OWLAxiom axiom : allAxioms) {
            collector.reset();
            axiom.accept(collector);
            for (OWLEntity obj : collector.getObjects()) {
                addToIndexedSet((OWLObjectProperty) obj, owlObjectPropertyReferences, axiom);
            }
        }
    }


    private void buildOWLDataPropertyReferencesIndex() {
        owlDataPropertyReferences = createMap();
        OWLEntityCollector collector = new OWLEntityCollector();
        collector.setCollectClasses(false);
        collector.setCollectDataProperties(true);
        collector.setCollectDataTypes(false);
        collector.setCollectIndividuals(false);
        collector.setCollectObjectProperties(false);
        for (OWLAxiom axiom : allAxioms) {
            collector.reset();
            axiom.accept(collector);
            for (OWLEntity obj : collector.getObjects()) {
                addToIndexedSet((OWLDataProperty) obj, owlDataPropertyReferences, axiom);
            }
        }
    }


    private void buildOWLIndividualReferencesIndex() {
        owlIndividualReferences = createMap();
        OWLEntityCollector collector = new OWLEntityCollector();
        collector.setCollectClasses(false);
        collector.setCollectDataProperties(false);
        collector.setCollectDataTypes(false);
        collector.setCollectIndividuals(true);
        collector.setCollectObjectProperties(false);
        for (OWLAxiom axiom : allAxioms) {
            collector.reset();
            axiom.accept(collector);
            for (OWLEntity obj : collector.getObjects()) {
                addToIndexedSet((OWLIndividual) obj, owlIndividualReferences, axiom);
            }
        }
    }


    private void buildOWLDataTypeReferencesIndex() {
        owlDataTypeReferences = createMap();
        OWLEntityCollector collector = new OWLEntityCollector();
        collector.setCollectClasses(false);
        collector.setCollectDataProperties(false);
        collector.setCollectDataTypes(true);
        collector.setCollectIndividuals(false);
        collector.setCollectObjectProperties(false);
        for (OWLAxiom axiom : allAxioms) {
            collector.reset();
            axiom.accept(collector);
            for (OWLEntity obj : collector.getObjects()) {
                addToIndexedSet((OWLDataType) obj, owlDataTypeReferences, axiom);
            }
        }
    }


    private void buildAxiomIndex(OWLAxiomVisitor visitor) {
        for (OWLAxiom axiom : allAxioms) {
            axiom.accept(visitor);
        }
    }


    private void buildEntityAnnotationAxiomsByEntityIndex() {
        owlEntityAnnotationAxiomsByEntity = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLEntityAnnotationAxiom axiom) {
                addToIndexedSet(axiom.getSubject(), owlEntityAnnotationAxiomsByEntity, axiom);
            }
        });
    }


    private void buildTypeAxiomsByIndividualIndex() {
        typeAxiomsByIndividual = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLClassAssertionAxiom axiom) {
                addToIndexedSet(axiom.getIndividual(), typeAxiomsByIndividual, axiom);
            }
        });
    }


    private void buildTypeAxiomsByClassIndex() {
        typeAxiomsByClass = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLClassAssertionAxiom axiom) {
                if (!axiom.getDescription().isAnonymous()) {
                    addToIndexedSet((OWLClass) axiom.getDescription(), typeAxiomsByClass, axiom);
                }
            }
        });
    }


    private void buildDataRelationshipsByIndividualIndex() {
        dataPropertyAssertionsByIndividual = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLDataPropertyAssertionAxiom axiom) {
                addToIndexedSet(axiom.getSubject(), dataPropertyAssertionsByIndividual, axiom);
            }
        });
    }


    private void buildObjectRelationshipsByIndividualIndex() {
        objectPropertyAssertionsByIndividual = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                addToIndexedSet(axiom.getSubject(), objectPropertyAssertionsByIndividual, axiom);
            }
        });
    }


    private void buildObjectSubPropertyAxiomsByLHSIndex() {
        objectSubPropertyAxiomsByLHS = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLObjectSubPropertyAxiom axiom) {
                addToIndexedSet(axiom.getSubProperty(), objectSubPropertyAxiomsByLHS, axiom);
            }
        });
    }


    private void buildObjectSubPropertyAxiomsByRHSIndex() {
        objectSubPropertyAxiomsByRHS = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLObjectSubPropertyAxiom axiom) {
                addToIndexedSet(axiom.getSuperProperty(), objectSubPropertyAxiomsByRHS, axiom);
            }
        });
    }


    private void buildObjectPropertyRangeAxiomsByPropertyIndex() {
        objectPropertyRangeAxiomsByProperty = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLObjectPropertyRangeAxiom axiom) {
                addToIndexedSet(axiom.getProperty(), objectPropertyRangeAxiomsByProperty, axiom);
            }
        });
    }


    private void buildObjectPropertyDomainAxiomsByPropertyIndex() {
        objectPropertyDomainAxiomsByProperty = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLObjectPropertyDomainAxiom axiom) {
                addToIndexedSet(axiom.getProperty(), objectPropertyDomainAxiomsByProperty, axiom);
            }
        });
    }


    private void buildEquivalentObjectPropertiesAxiomsByPropertyIndex() {
        equivalentObjectPropertyAxiomsByProperty = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
                for (OWLObjectPropertyExpression prop : axiom.getProperties()) {
                    addToIndexedSet(prop, equivalentObjectPropertyAxiomsByProperty, axiom);
                }
            }
        });
    }


    private void buildDisjointObjectPropertiesAxiomsByPropertyIndex() {
        disjointObjectPropertyAxiomsByProperty = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
                for (OWLObjectPropertyExpression prop : axiom.getProperties()) {
                    addToIndexedSet(prop, disjointObjectPropertyAxiomsByProperty, axiom);
                }
            }
        });
    }


    private void buildNegativeObjectPropertyAssertionAxiomsByIndvidualIndex() {
        negativeObjectPropertyAssertionAxiomsByIndividual = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
                addToIndexedSet(axiom.getSubject(), negativeObjectPropertyAssertionAxiomsByIndividual, axiom);
            }
        });
    }


    private void buildNegativeDataPropertyAssertionAxiomsByIndvidualIndex() {
        negativeDataPropertyAssertionAxiomsByIndividual = createMap();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
                addToIndexedSet(axiom.getSubject(), negativeDataPropertyAssertionAxiomsByIndividual, axiom);
            }
        });
    }


    private void buildAnnotationAxiomsIndex() {
        annotationAxioms = new TreeSet<OWLAnnotationAxiom>();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLAxiomAnnotationAxiom axiom) {
                annotationAxioms.add(axiom);
            }


            public void visit(OWLOntologyAnnotationAxiom axiom) {
                annotationAxioms.add(axiom);
            }


            public void visit(OWLEntityAnnotationAxiom axiom) {
                annotationAxioms.add(axiom);
            }
        });
    }


    private void buildClassAxiomsByClassIndex() {
        classAxiomsByClass = new HashMap<OWLClass, Set<OWLClassAxiom>>();
        buildAxiomIndex(new OWLAxiomVisitorAdapter() {
            public void visit(OWLEquivalentClassesAxiom axiom) {
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        addToIndexedSet((OWLClass) desc, classAxiomsByClass, axiom);
                    }
                }
            }


            public void visit(OWLSubClassAxiom axiom) {
                if (!axiom.getSubClass().isAnonymous()) {
                    addToIndexedSet((OWLClass) axiom.getSubClass(), classAxiomsByClass, axiom);
                }
            }


            public void visit(OWLDisjointClassesAxiom axiom) {
                for (OWLDescription desc : axiom.getDescriptions()) {
                    if (!desc.isAnonymous()) {
                        addToIndexedSet((OWLClass) desc, classAxiomsByClass, axiom);
                    }
                }
            }


            public void visit(OWLDisjointUnionAxiom axiom) {
                addToIndexedSet(axiom.getOWLClass(), classAxiomsByClass, axiom);
            }
        });
    }


    public boolean equals(Object obj) {
        if (obj instanceof OWLOntology) {
            return ((OWLOntology) obj).getURI().equals(ontologyURI);
        }
        return false;
    }


    private class OWLEntityReferenceChecker implements OWLEntityVisitor {

        private boolean ref;


        public boolean containsReference(OWLEntity entity) {
            ref = false;
            entity.accept(this);
            return ref;
        }


        public void visit(OWLClass cls) {
            ref = OWLOntologyImpl.this.containsReference(cls);
        }


        public void visit(OWLDataType dataType) {
            ref = OWLOntologyImpl.this.containsReference(dataType);
        }


        public void visit(OWLIndividual individual) {
            ref = OWLOntologyImpl.this.containsReference(individual);
        }


        public void visit(OWLDataProperty property) {
            ref = OWLOntologyImpl.this.containsReference(property);
        }


        public void visit(OWLObjectProperty property) {
            ref = OWLOntologyImpl.this.containsReference(property);
        }
    }
}
