package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.PhysicalURIInputSource;
import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;
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
 * Date: 27-Oct-2006<br><br>
 */
public class OWLOntologyManagerImpl implements OWLOntologyManager, OWLOntologyFactory.OWLOntologyCreationHandler {

    private static final Logger logger = Logger.getLogger(OWLOntologyManagerImpl.class.getName());

    private Set<OWLOntology> ontologies;

    private Map<URI, OWLOntology> ontologiesByURI;

    private Map<URI, URI> physicalURIsByOntologyURI;

    private Map<OWLOntology, OWLOntologyFormat> ontologyFormatsByOntology;

    private List<OWLOntologyURIMapper> uriMappers;

    private List<OWLOntologyFactory> ontologyFactories;

    private List<OWLOntologyStorer> ontologyStorers;

    private boolean broadcastChanges;

    private int loadCount = 0;

    private OWLDataFactory dataFactory;

    private Map<OWLOntology, Set<OWLOntology>> importsClosureCache;


    public OWLOntologyManagerImpl(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
        ontologies = new HashSet<OWLOntology>();
        ontologiesByURI = new HashMap<URI, OWLOntology>();
        physicalURIsByOntologyURI = new HashMap<URI, URI>();
        ontologyFormatsByOntology = new HashMap<OWLOntology, OWLOntologyFormat>();
        uriMappers = new ArrayList<OWLOntologyURIMapper>();
        ontologyFactories = new ArrayList<OWLOntologyFactory>();
        installDefaultURIMappers();
        installDefaultOntologyFactories();
        broadcastChanges = true;
        ontologyStorers = new ArrayList<OWLOntologyStorer>();
        importsClosureCache = new HashMap<OWLOntology, Set<OWLOntology>>();
    }


    public OWLDataFactory getOWLDataFactory() {
        return dataFactory;
    }


    public Set<OWLOntology> getOntologies() {
        return new HashSet<OWLOntology>(ontologies);
    }


    public boolean contains(OWLOntology ontology) {
        return ontologies.contains(ontology);
    }


    public boolean contains(URI ontologyURI) {
        return ontologiesByURI.containsKey(ontologyURI);
    }


    public OWLOntology getOntology(URI ontologyURI) {
        return ontologiesByURI.get(ontologyURI);
    }


    public Set<OWLOntology> getImports(OWLOntology ontology) {
        Set<OWLOntology> imports = new HashSet<OWLOntology>();
        for (OWLImportsDeclaration axiom : ontology.getImportsDeclarations()) {
            OWLOntology importedOntology = ontologiesByURI.get(axiom.getImportedOntologyURI());
            if (importedOntology != null) {
                imports.add(importedOntology);
            }
        }
        return imports;
    }


    public Set<OWLOntology> getImportsClosure(OWLOntology ontology) {
        Set<OWLOntology> ontologies = importsClosureCache.get(ontology);
        if(ontologies == null) {
            ontologies = new HashSet<OWLOntology>();
            getImportsClosure(ontology, ontologies);
            importsClosureCache.put(ontology, ontologies);
        }
        return Collections.unmodifiableSet(ontologies);
    }


    private void getImportsClosure(OWLOntology ontology, Set<OWLOntology> ontologies) {
        ontologies.add(ontology);
        for (OWLOntology ont : getImports(ontology)) {
            if (!ontologies.contains(ont)) {
                getImportsClosure(ont, ontologies);
            }
        }
    }


    public List<OWLOntologyChange> applyChanges(List<? extends OWLOntologyChange> changes) throws
                                                                                           OWLOntologyChangeException {
        List<OWLOntologyChange> appliedChanges = new ArrayList<OWLOntologyChange>();
        for (OWLOntologyChange change : changes) {
            OWLOntology ont = change.getOntology();
            if (ont instanceof OWLMutableOntology) {
                appliedChanges.addAll(((OWLMutableOntology) ont).applyChange(change));
                checkForOntologyURIChange(change);
                checkForImportsChange(change);
            }
        }
        broadcastChanges(changes);
        return appliedChanges;
        // Another option here is to sort the changes in to multiple lists, with one
        // list per ontology.  In this case we could apply a list of changes to each
        // ontology rather than applying changes one-by-one.
    }

    private void checkForImportsChange(OWLOntologyChange change) {
        if(change.isAxiomChange() && change.getAxiom() instanceof OWLImportsDeclaration) {
            resetImportsClosureCache();
        }
    }

    private void checkForOntologyURIChange(OWLOntologyChange change) {
        if (change instanceof SetOntologyURI) {
            SetOntologyURI setURI = (SetOntologyURI) change;
            renameOntology(setURI.getOriginalURI(), setURI.getNewURI());
            resetImportsClosureCache();
        }
    }


    public List<OWLOntologyChange> applyChange(OWLOntologyChange change) throws OWLOntologyChangeException {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>(1);
        changes.add(change);
        return applyChanges(changes);

//        List<OWLOntologyChange> changes;
//        changes = ((OWLMutableOntology) change.getOntology()).applyChange(change);
//        checkForOntologyURIChange(change);
//        broadcastChanges(changes);
//        return changes;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Methods to create, load and reload ontologies
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void ontologyCreated(OWLOntology ontology) {
        // This method is called when a factory that we have asked to create or
        // load an ontology has created the ontology.  We add the ontology to the
        // set of loaded ontologies.
        addOntology(ontology);
    }


    public void setOntologyFormat(OWLOntology ontology, OWLOntologyFormat format) {
        ontologyFormatsByOntology.put(ontology, format);
    }


    public OWLOntologyFormat getOntologyFormat(OWLOntology ontology) {
        return ontologyFormatsByOntology.get(ontology);
    }


    public OWLOntology createOntology(URI ontologyURI) throws OWLOntologyCreationException {
        OWLOntology ontology = ontologiesByURI.get(ontologyURI);
        if (ontology != null) {
            return ontology;
        }
        URI physicalURI = getPhysicalURIFromOntologyURI(ontologyURI, false);
        for (OWLOntologyFactory factory : ontologyFactories) {
            if (factory.canCreateFromPhysicalURI(physicalURI)) {
                physicalURIsByOntologyURI.put(ontologyURI, physicalURI);
                return factory.createOWLOntology(ontologyURI, physicalURI, this);
            }
        }
        throw new OWLOntologyFactoryNotFoundException(physicalURI);
    }


    public OWLOntology loadOntology(URI ontologyURI) throws OWLOntologyCreationException {
        OWLOntology ontology = ontologiesByURI.get(ontologyURI);
        if (ontology != null) {
            return ontology;
        }
        URI physicalURI = getPhysicalURIFromOntologyURI(ontologyURI, true);
        // The ontology might be being loaded, but its logical URI might
        // not have been set (as is probably the case with RDF/XML!)
        if (physicalURI != null) {
            ontology = getOntology(physicalURI);
            if(ontology != null) {
                return ontology;
            }
        }
        return loadOntologyFromPhysicalURI(ontologyURI, physicalURI);
    }


    public OWLOntology loadOntologyFromPhysicalURI(URI uri) throws OWLOntologyCreationException {
        return loadOntologyFromPhysicalURI(uri, uri);
    }


    public OWLOntology loadOntologyFromPhysicalURI(URI ontologyURI, URI physicalURI) throws
                                                                                     OWLOntologyCreationException {
        return loadOntology(new PhysicalURIInputSource(physicalURI));
    }


    public OWLOntology loadOntology(OWLOntologyInputSource inputSource) throws OWLOntologyCreationException {
        loadCount++;
        broadcastChanges = false;
        try {
            for (OWLOntologyFactory factory : ontologyFactories) {
                if (factory.canLoad(inputSource)) {
                    // Note - there is no need to add the ontology here, because it will be added
                    // when the ontology is created.
                    OWLOntology ontology = factory.loadOWLOntology(inputSource, this);
                    // Store the ontology to physical URI mapping
                    physicalURIsByOntologyURI.put(ontology.getURI(), inputSource.getPhysicalURI());
                    return ontology;
                }
            }
        }
        finally {
            loadCount--;
            if (loadCount == 0) {
                broadcastChanges = true;
            }
        }
        throw new OWLOntologyFactoryNotFoundException(inputSource.getPhysicalURI());
    }


    public OWLOntology reloadOntology(URI ontologyURI) throws OWLOntologyCreationException {
        removeOntology(ontologyURI);
        return loadOntology(ontologyURI);
    }


    public void removeOntology(URI ontologyURI) {
        OWLOntology ontology = ontologiesByURI.get(ontologyURI);
        ontologiesByURI.remove(ontologyURI);
        ontologyFormatsByOntology.remove(ontology);
        ontologies.remove(ontology);
        physicalURIsByOntologyURI.remove(ontologyURI);
        resetImportsClosureCache();
    }


    private void addOntology(OWLOntology ont) {
        ontologies.add(ont);
        ontologiesByURI.put(ont.getURI(), ont);
    }


    public URI getPhysicalURIForOntology(OWLOntology ontology) throws UnknownOWLOntologyException {
        URI physicalURI = physicalURIsByOntologyURI.get(ontology.getURI());
        if (physicalURI == null) {
            throw new UnknownOWLOntologyException(ontology.getURI());
        }
        return physicalURI;
    }


    public void setPhysicalURIForOntology(OWLOntology ontology, URI physicalURI) throws UnknownOWLOntologyException {
        if (!ontologies.contains(ontology)) {
            throw new UnknownOWLOntologyException(ontology.getURI());
        }
        physicalURIsByOntologyURI.put(ontology.getURI(), physicalURI);
    }


    private void renameOntology(URI oldURI, URI newURI) {
        OWLOntology ont = ontologiesByURI.get(oldURI);
        if (ont == null) {
            // Nothing to rename!
            return;
        }
        ontologiesByURI.remove(oldURI);
        ontologiesByURI.put(newURI, ont);
        URI physicalURI = physicalURIsByOntologyURI.remove(oldURI);
        physicalURIsByOntologyURI.put(newURI, physicalURI);
        resetImportsClosureCache();
    }


    private void resetImportsClosureCache() {
        importsClosureCache.clear();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Methods to save ontologies
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void saveOntology(OWLOntology ontology) throws OWLOntologyStorageException, UnknownOWLOntologyException {
        OWLOntologyFormat format = getOntologyFormat(ontology);
        saveOntology(ontology, format);
    }


    public void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat) throws OWLOntologyStorageException,
                                                                                            UnknownOWLOntologyException {
        URI physicalURI = getPhysicalURIForOntology(ontology);
        saveOntology(ontology, ontologyFormat, physicalURI);
    }


    public void saveOntology(OWLOntology ontology, URI physicalURI) throws OWLOntologyStorageException,
                                                                           UnknownOWLOntologyException {
        OWLOntologyFormat format = getOntologyFormat(ontology);
        saveOntology(ontology, format, physicalURI);
    }


    public void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat, URI physcialURI) throws
                                                                                                      OWLOntologyStorageException,
                                                                                                      UnknownOWLOntologyException {
        for (OWLOntologyStorer storer : ontologyStorers) {
            if (storer.canStoreOntology(ontologyFormat)) {
                storer.storeOntology(this, ontology, physcialURI, ontologyFormat);
                return;
            }
        }
        throw new OWLOntologyStorerNotFoundException(ontologyFormat);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Methods to add/remove ontology storers
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void addOntologyStorer(OWLOntologyStorer storer) {
        ontologyStorers.add(0, storer);
    }


    public void removeOntologyStorer(OWLOntologyStorer storer) {
        ontologyStorers.remove(storer);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Methods to add/remove mappers etc.
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void addURIMapper(OWLOntologyURIMapper mapper) {
        uriMappers.add(0, mapper);
    }


    public void removeURIMapper(OWLOntologyURIMapper mapper) {
        uriMappers.remove(mapper);
    }


    public void addOntologyFactory(OWLOntologyFactory factory) {
        ontologyFactories.add(0, factory);
        factory.setOWLOntologyManager(this);
    }


    public void removeOntologyFactory(OWLOntologyFactory factory) {
        ontologyFactories.remove(factory);
    }


    /**
     * Uses the mapper mechanism to obtain a physical URI for an ontology
     * URI.
     * @param ontologyURI The ontology for which a physical URI mapping is to
     *                    be retrieved.
     * @param quiet
     * @return The physical URI that corresponds to the ontology URI, or
     *         <code>null</code> if no physical URI can be found.
     */
    private URI getPhysicalURIFromOntologyURI(URI ontologyURI, boolean quiet) {
        for (OWLOntologyURIMapper mapper : uriMappers) {
            URI physicalURI = mapper.getPhysicalURI(ontologyURI);
            if (physicalURI != null) {
                return physicalURI;
            }
        }
        if (!quiet) {
            throw new OWLOntologyURIMappingNotFoundException(ontologyURI);
        }
        else {
            return null;
        }
    }


    private void installDefaultURIMappers() {
        // By defaut install the default mapper that simply maps
        // ontology URIs to themselves.
        addURIMapper(new OWLOntologyURIMapperImpl());
    }


    private void installDefaultOntologyFactories() {
        // The default factories are the ones that can load
        // ontologies from http:// and file:// URIs
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Listener stuff - methods to add/remove listeners
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private Map<OWLOntologyChangeListener, OWLOntologyChangeBroadcastStrategy> listenerMap = new LinkedHashMap<OWLOntologyChangeListener, OWLOntologyChangeBroadcastStrategy>();


    public void addOntologyChangeListener(OWLOntologyChangeListener listener) {
        listenerMap.put(listener, new DefaultChangeBroadcastStrategy());
    }


    /**
     * Broadcasts to attached listeners, using the various broadcasting
     * strategies that were specified for each listener.
     */
    private void broadcastChanges(List<? extends OWLOntologyChange> changes) {
        if (!broadcastChanges) {
            return;
        }
        for (OWLOntologyChangeListener listener : new ArrayList<OWLOntologyChangeListener>(listenerMap.keySet())) {
            OWLOntologyChangeBroadcastStrategy strategy = listenerMap.get(listener);
            if(strategy == null) {
                // This listener may have been removed during the broadcast of the changes,
                // so when we attempt to retrieve it from the map it isn't there (because
                // we iterate over a copy).
                continue;
            }
            try {
                // Handle exceptions on a per listener basis.  If we have
                // badly behaving listeners, we don't want one listener
                // to prevent the other listeners from receiving events.
                strategy.broadcastChanges(listener, changes);
            }
            catch (Exception e) {
                logger.warning("BADLY BEHAVING LISTENER: " + e);
                e.printStackTrace();
            }
        }
    }


    public void addOntologyChangeListener(OWLOntologyChangeListener listener,
                                          OWLOntologyChangeBroadcastStrategy strategy) {
        listenerMap.put(listener, strategy);
    }


    public void removeOntologyChangeListener(OWLOntologyChangeListener listener) {
        listenerMap.remove(listener);
    }
}

