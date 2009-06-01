package org.semanticweb.owl.model;

import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.OWLOntologyOutputTarget;

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
 *
 * The <code>OWLOntologyManager</code> manages a set of ontologies.
 * It is the main point for creating, loading and accessing ontologies.
 *
 */
public interface OWLOntologyManager extends OWLOntologySetProvider {

    /**
     * Gets a data factory which can be used to create
     * OWL API objects such as classes, properties, individuals, axioms etc.
     */
    OWLDataFactory getOWLDataFactory();

    /**
     * Gets all of the ontologies that are managed by this
     * manager.
     */
    Set<OWLOntology> getOntologies();

    /**
     * Determines if there is an ontology with the specified URI that is
     * managed by this manager.
     * @param ontologyURI The URI of the ontology to test for
     * @return <code>true</code> if there is an ontology with the specified URI that is managed
     * by this manager, otherwise <code>false</code>.
     */
    boolean contains(URI ontologyURI);


    /**
     * Gets a previously loaded/created ontology that corresponds to the specified
     * ontology URI.
     * @param ontologyURI The URI of the ontology to be obtained (this is not the
     * physical URI of the ontology).
     * @return The ontology that has the specified URI.
     * @throws UnknownOWLOntologyException if there isn't an ontology in this manager
     * which has the specified URI.
     */
    OWLOntology getOntology(URI ontologyURI) throws UnknownOWLOntologyException;


    /**
     * Given an imports declaration, obtains the ontology that this imports has
     * been resolved to.
     * @param declaration The declaration that points to the imported ontology.
     * @return The ontology that the imports declaration resolves to, or <code>null</code>
     * if the imports declaration could not be resolved to an ontology, because
     * the ontology was not imported.
     */
    OWLOntology getImportedOntology(OWLImportsDeclaration declaration);

    /**
     * Gets the set of ontologies that are directly imported by the
     * specified ontology.
     * @param ontology The ontology whose direct imports are to be
     * retrieved.
     * @return A set of <code>OWLOntology</code> instances that represent
     * the direct imports of the specified ontology.  If, for what ever reason,
     * an imported ontology could not be loaded, then it will not be contained
     * in the returned set of ontologies.
     * @throws UnknownOWLOntologyException if there isn't an ontology in this manager
     * which has the specified URI.
     */
    Set<OWLOntology> getImports(OWLOntology ontology) throws UnknownOWLOntologyException;


    /**
     * Gets the imports closure for the specified ontology.
     * @param ontology The ontology whose imports closure is to be retrieved.
     * @return A <code>Set</code> of ontologies that contains the imports closure
     * for the specified ontology.  This set will also include the specified ontology.
     * Example: if A imports B and B imports C, then calling this method with A
     * will return the set consisting of A, B and C. If, for what ever reason,
     * an imported ontology could not be loaded, then it will not be contained
     * in the returned set of ontologies.
     * @throws UnknownOWLOntologyException if there isn't an ontology in this manager
     * which has the specified URI.
     */
    Set<OWLOntology> getImportsClosure(OWLOntology ontology) throws UnknownOWLOntologyException;



    /**
     * Applies a list of changes to some or all of the ontologies that
     * are managed by this manager.  The changes will be applied to the
     * appropraite ontologies.
     * @param changes The changes to be applied.
     * @return The changes that were actually applied.
     * @throws OWLOntologyChangeException If one or more of the changes could not be applied.  See subclasses
     * of ontology change exception for more specific details.
     */
    List<OWLOntologyChange> applyChanges(List<? extends OWLOntologyChange> changes) throws OWLOntologyChangeException;


    /**
     * A convenience method that adds a set of axioms to an ontology.  The appropriate
     * AddAxiom change objects are automatically generated.
     * @param ont The ontology to which the axioms should be added.
     * @param axioms The axioms to be added.
     * @return A list of ontology changes that represent the changes which took place in order
     * to add the axioms.
     * @throws OWLOntologyChangeException
     */
    List<OWLOntologyChange> addAxioms(OWLOntology ont, Set<? extends OWLAxiom> axioms) throws OWLOntologyChangeException;


    /**
     * A convenience method that adds a single axioms to an ontology. The appropriate
     * AddAxiom change object is automatically generated.
     * @param ont The ontology to add the axiom to.
     * @param axiom The axiom to be added
     * @return A list of ontology changes that represent the changes that actually took place.
     * @throws OWLOntologyChangeException
     */
    List<OWLOntologyChange> addAxiom(OWLOntology ont, OWLAxiom axiom) throws OWLOntologyChangeException;

    /**
     * A convenience method that applies just one change to an ontology that is managed by
     * this manager.
     * @param change The change to be applied
     * @return The changes that resulted of the applied ontology change.
     * @throws OWLOntologyChangeException If the change could not be applied.  See subclasses
     * of ontology change exception for more specific details.
     */
    List<OWLOntologyChange> applyChange(OWLOntologyChange change) throws OWLOntologyChangeException;

    /**
     * Creates a new (empty) ontology that has the specified ontology URI.
     * @param ontologyURI The URI of the ontology to be created.  The ontology
     * URI will be mapped to a physical URI in order to determine the type of
     * ontology factory that will be used to create the ontology.  If this mapping
     * is <code>null</code> then a default (in memory) implementation of the
     * ontology will most likely be created.
     * @return The newly created ontology, or if an ontology with the specified URI
     * already exists then this existing ontology will be returned.
     * @throws OWLOntologyCreationException If the ontology could not be created.
     */
    OWLOntology createOntology(URI ontologyURI) throws OWLOntologyCreationException;

    /**
     * Loads the ontology specified by the <code>ontologyURI</code>
     * parameter.  Note that this is <b>NOT</b> the physical URI that
     * points to a concrete representation (e.g. an RDF/XML OWL file)
     * of an ontology.  The mapping to a physical URI will be determined
     * by using one of the loaded <code>OWLOntologyURIMapper</code>s.
     * @param ontologyURI The ontology URI (sometimes called logical URI
     * of the ontology to be loaded)
     * @return The <code>OWLOntology</code> representation of the ontology
     * that was loaded.  If an ontology with the specified URI is already loaded
     * then that ontology will be returned.
     * @throws OWLOntologyCreationException If there was a problem in creating and
     * loading the ontology.
     */
    OWLOntology loadOntology(URI ontologyURI) throws OWLOntologyCreationException;

    /**
     * A convenience method that loads an ontology from a physical URI.  If
     * the ontology contains imports, then the appropriate mappers should
     * be set up before calling this method.
     * @param uri The physical URI which points to a concrete representation
     * of an ontology.
     * @return The ontology that was loaded.
     * @throws OWLOntologyCreationException If the ontology could not be created and loaded.
     */
    OWLOntology loadOntologyFromPhysicalURI(URI uri) throws OWLOntologyCreationException;


    /**
     * A convenience method that load an ontology from an input source.  If the ontology
     * contains imports then the appropriate mappers should be set up before calling this
     * method.
     * @param inputSource
     * @return
     * @throws OWLOntologyCreationException
     */
    OWLOntology loadOntology(OWLOntologyInputSource inputSource) throws OWLOntologyCreationException;

    /**
     * Reloads an ontology that is already managed by this manager.
     * If the manager does not already manage an ontology that has
     * the specified URI, it will attempt to load the ontology.
     * @param ontologyURI The URI of the ontology to be reloaded.
     * @return The ontology which was reloaded
     * @throws OWLOntologyCreationException If the ontology could not be reloaded.
     */
    OWLOntology reloadOntology(URI ontologyURI) throws OWLOntologyCreationException;

    /**
     * Attempts to remove an ontology.  The ontology which is identified by the specified
     * URI is removed regardless of whether it is referenced by other ontologies via
     * imports statements.
     * @param ontologyURI The URI of the ontology to be removed.  If this manager does
     * not contain an ontology identified by the specified URI then nothing happens.
     */
    void removeOntology(URI ontologyURI);


    /**
     * Gets the physical URI for a given ontology.  This will either be the physical
     * URI from where the ontology was obtained from during loading, or the
     * physcial URI which was specified (via a mapper) when the (empty) ontology was
     * created.  Note that this may not correspond to the first physical URI found in the
     * list of mappings from ontology URI to physcial URI. The reason for this is that
     * it might not have been possible to load the ontology from the first physical URI
     * found in the mapping table.
     * @param ontology The ontology whose physical URI is to be obtained.
     * @return The physical URI of the ontology.
     * @throws UnknownOWLOntologyException If the specified ontology is not managed by this manager.
     */
    URI getPhysicalURIForOntology(OWLOntology ontology) throws UnknownOWLOntologyException;


    /**
     * Overrides the current physical URI for a given ontology.  This method does
     * not alter the URI mappers which are installed, but alters the physical URI
     * of an ontology that has already been loaded.
     * @param ontology The ontology that has already been loaded.
     * @param physicalURI The new physical URI of the ontology.
     * @throws UnknownOWLOntologyException If the specified ontology is not managed by this manager.
     */
    void setPhysicalURIForOntology(OWLOntology ontology, URI physicalURI) throws UnknownOWLOntologyException;

    /**
     * Gets the ontology format for the specified ontology.
     * @param ontology The ontology whose format it to be obtained.
     * @return The format of the ontology.
     * @throws UnknownOWLOntologyException If the specified ontology is not managed by this manager.
     */
    OWLOntologyFormat getOntologyFormat(OWLOntology ontology) throws UnknownOWLOntologyException;

    /**
     * Sets the format for the specified ontology.
     * @param ontology The ontology whose format is to be set.
     * @param ontologyFormat The format for the specified ontology.
     */
    void setOntologyFormat(OWLOntology ontology, OWLOntologyFormat ontologyFormat) throws UnknownOWLOntologyException;

    /**
     * Saves the specified ontology.  The ontology will be saved to the location
     * that it was loaded from, or if it was created programmatically, it will be
     * saved to the location specified by an ontology URI mapper at creation time.
     * The ontology will be saved in the same format which it was loaded from, or the
     * default ontology format if the ontology was created programmatically.
     * @param ontology The ontology to be saved.
     * @exception OWLOntologyStorageException An exception will be thrown if there is a problem with
     * saving the ontology, or the ontology can't be saved in the format it was loaded
     * from.
     */
    void saveOntology(OWLOntology ontology) throws OWLOntologyStorageException, UnknownOWLOntologyException;


    /**
     * Saves the specified ontology, using the specified URI to determine where/how the ontology
     * should be saved.
     * @param ontology The ontology to be saved.
     * @param physicalURI The physical URI which will be used to determine how and where the
     * ontology will be saved.
     * @throws OWLOntologyStorageException If the ontology cannot be saved.
     */
    void saveOntology(OWLOntology ontology, URI physicalURI) throws OWLOntologyStorageException, UnknownOWLOntologyException;

    void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat) throws OWLOntologyStorageException, UnknownOWLOntologyException;

    void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat, URI physcialURI) throws OWLOntologyStorageException, UnknownOWLOntologyException;

    void saveOntology(OWLOntology ontology, OWLOntologyOutputTarget outputTarget) throws OWLOntologyStorageException, UnknownOWLOntologyException;

    void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat, OWLOntologyOutputTarget outputTarget) throws OWLOntologyStorageException, UnknownOWLOntologyException;

    /**
     * Adds a mapper to this manager.  The mapper is used to obtain
     * physical URIs for ontology URIs (logical URIs).  The mapper will be
     * added so that it is given the highest priority (i.e. it will be tried
     * first).
     * @param mapper The mapper to be added.
     */
    void addURIMapper(OWLOntologyURIMapper mapper);


    /**
     * Removes a mapper from this manager.
     * @param mapper The mapper to be removed.
     */
    void removeURIMapper(OWLOntologyURIMapper mapper);


    /**
     * Clears any installed URI mappers
     */
    void clearURIMappers();

    /**
     * Adds an ontology factory that is capable of creating an ontology
     * given a particular physical URI.
     * @param factory The factory to be added.
     */
    void addOntologyFactory(OWLOntologyFactory factory);


    /**
     * Removes a previously added factory.
     * @param factory The factory to be removed.
     */
    void removeOntologyFactory(OWLOntologyFactory factory);


    /**
     * Add an ontology storer.
     * @param storer The storer to be added
     */
    void addOntologyStorer(OWLOntologyStorer storer);


    /**
     * Removes a previously added storer
     * @param storer The storer to be removed
     */
    void removeOntologyStorer(OWLOntologyStorer storer);

    /**
     * Adds an ontology change listener, which listens to all changes for
     * all ontologies.  To customise the changes/ontologies that are listened
     * to, the <code>addOntologyChangeListener</code> method which takes a
     * broadcast strategy as an argument should be used. 
     * @param listener
     */
    void addOntologyChangeListener(OWLOntologyChangeListener listener);


    /**
     * Adds an ontology change listener, which listens to ontology changes. An
     * ontology change broadcast strategy must be specified, which determines the
     * changes that are broadcast to the listener.
     * @param listener The listener to be added.
     * @param strategy The strategy that should be used for broadcasting changes to
     * the listener.
     */
    void addOntologyChangeListener(OWLOntologyChangeListener listener, OWLOntologyChangeBroadcastStrategy strategy);


    /**
     * Removes a previously added listener.
     * @param listener The listener to be removed.
     */
    void removeOntologyChangeListener(OWLOntologyChangeListener listener);


    /**
     * Requests that the manager loads an imported ontology that is described
     * by an imports statement.  This method is generally used by parsers and
     * other kinds of loaders.  For simply loading an ontology, use the loadOntologyXXX methods.
     * @param declaration The declaration that describes the import to be loaded.
     */
    void makeLoadImportRequest(OWLImportsDeclaration declaration) throws OWLOntologyCreationException ;


    /**
     * The default behaviour when an import cannot be loaded is to throw an
     * exception.  This completely stops the loading process.  If it is desired
     * that loading continues then this option can be set with this method.
     * @param b <code>true</code> if loading should continue when an imported
     * ontology cannot be loaded, other wise <code>false</code>.  The default
     * value is <code>false</code>.
     */
    void setSilentMissingImportsHandling(boolean b);


    /**
     * Determines if silent missing imports handling is enabled.
     * @return <code>true</code> if silent missing imports handler is
     * enabled, otherwise <code>false</code>.
     */
    boolean isSilentMissingImportsHandling();


    /**
     * In the case where silent missing imports handling is enabled,
     * a listener can be attached via this method so that there is a
     * mechanism that allows clients to be informed of the reason when
     * an import cannot be loaded.
     * @param listener The listener to be added.
     */
    void addMissingImportListener(MissingImportListener listener);


    /**
     * Removes a previously added missing import listener.
     * @param listener The listener to be removed.
     */
    void removeMissingImportListener(MissingImportListener listener);

    void addOntologyLoaderListener(OWLOntologyLoaderListener listener);

    void removeOntologyLoaderListener(OWLOntologyLoaderListener listener);
    
}
