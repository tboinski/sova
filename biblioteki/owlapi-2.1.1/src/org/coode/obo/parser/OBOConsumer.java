package org.coode.obo.parser;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.CollectionFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
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
 * Date: 10-Jan-2007<br><br>
 */
public class OBOConsumer implements OBOParserHandler {

    private static final Logger logger = Logger.getLogger(OBOConsumer.class.getName());

    private OWLOntologyManager owlOntologyManager;

    private OWLOntology ontology;

    private boolean inHeader;

    private String currentId;

    private Map<String, TagValueHandler> handlerMap;

    private String defaultNamespace;

    private String currentNamespace;

    private String stanzaType;

    private boolean termType;

    private boolean typedefType;

    private boolean instanceType;

    private Set<OWLDescription> intersectionOfOperands;

    private Set<OWLDescription> unionOfOperands;

    private Map<String, URI> uriCache;


    public OBOConsumer(OWLOntologyManager owlOntologyManager, OWLOntology ontology) {
        this.owlOntologyManager = owlOntologyManager;
        this.ontology = ontology;
        defaultNamespace = OBOVocabulary.BASE;
        intersectionOfOperands = new HashSet<OWLDescription>();
        unionOfOperands = new HashSet<OWLDescription>();
        uriCache = new HashMap<String, URI>();
        setupTagHandlers();
    }


    public OWLOntologyManager getOWLOntologyManager() {
        return owlOntologyManager;
    }


    public OWLOntology getOntology() {
        return ontology;
    }


    public String getCurrentId() {
        return currentId;
    }


    public String getDefaultNamespace() {
        return defaultNamespace;
    }


    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }


    public String getCurrentNamespace() {
        return currentNamespace;
    }


    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }


    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }


    public void addUnionOfOperand(OWLDescription description) {
        unionOfOperands.add(description);
    }


    public void addIntersectionOfOperand(OWLDescription description) {
        intersectionOfOperands.add(description);
    }


    public String getStanzaType() {
        return stanzaType;
    }


    public boolean isTerm() {
        return termType;
    }


    public boolean isTypedef() {
        return typedefType;
    }


    public boolean isInstanceType() {
        return instanceType;
    }


    private void setupTagHandlers() {
        handlerMap = new HashMap<String, TagValueHandler>();
        addTagHandler(new IDTagValueHandler(this));
        addTagHandler(new NameTagValueHandler(this));
        addTagHandler(new IsATagValueHandler(this));
        addTagHandler(new PartOfTagValueHandler(this));
        addTagHandler(new TransitiveTagValueHandler(this));
        addTagHandler(new SymmetricTagValueHandler(this));
        addTagHandler(new RelationshipTagValueHandler(this));
        addTagHandler(new UnionOfHandler(this));
        addTagHandler(new IntersectionOfHandler(this));
        addTagHandler(new DisjointFromHandler(this));
        addTagHandler(new AntiSymmetricHandler(this));
        addTagHandler(new InverseHandler(this));
        addTagHandler(new ReflexiveHandler(this));
        addTagHandler(new TransitiveOverHandler(this));
    }


    private void addTagHandler(TagValueHandler handler) {
        handlerMap.put(handler.getTag(), handler);
    }


    public void startHeader() {
        inHeader = true;
    }


    public void endHeader() {
        inHeader = false;
    }


    public void startStanza(String name) {
        currentId = null;
        currentNamespace = null;
        stanzaType = name;
        termType = stanzaType.equals("Term");
        typedefType = false;
        instanceType = false;
        if (!termType) {
            typedefType = stanzaType.equals("Typedef");
            if (!typedefType) {
                instanceType = stanzaType.equals("Instance");
            }
        }
    }


    public void endStanza() {
        if (!unionOfOperands.isEmpty()) {
            createUnionEquivalentClass();
            unionOfOperands.clear();
        }

        if (!intersectionOfOperands.isEmpty()) {
            createIntersectionEquivalentClass();
            intersectionOfOperands.clear();
        }
    }


    private void createUnionEquivalentClass() {
        OWLDescription equivalentClass;
        if (unionOfOperands.size() == 1) {
            equivalentClass = unionOfOperands.iterator().next();
        }
        else {
            equivalentClass = getDataFactory().getOWLObjectUnionOf(unionOfOperands);
        }
        createEquivalentClass(equivalentClass);
    }


    private void createIntersectionEquivalentClass() {
        OWLDescription equivalentClass;
        if (intersectionOfOperands.size() == 1) {
            equivalentClass = intersectionOfOperands.iterator().next();
        }
        else {
            equivalentClass = getDataFactory().getOWLObjectIntersectionOf(intersectionOfOperands);
        }
        createEquivalentClass(equivalentClass);
    }


    private void createEquivalentClass(OWLDescription description) {
        OWLAxiom ax = getDataFactory().getOWLEquivalentClassesAxiom(CollectionFactory.createSet(getCurrentClass(),
                                                                                                description));
        try {
            getOWLOntologyManager().applyChange(new AddAxiom(ontology, ax));
        }
        catch (OWLOntologyChangeException e) {
            logger.severe(e.getMessage());
        }
    }


    public void handleTagValue(String tag, String value) {
        try {
            if (!inHeader) {
                TagValueHandler handler = handlerMap.get(tag);
                if (handler != null) {
                    handler.handle(currentId, value);
                }
                else if (currentId != null) {
                    // Add as annotation
                    OWLEntity entity = getCurrentEntity();
                    OWLConstant con = getDataFactory().getOWLUntypedConstant(value);
                    OWLAnnotation anno = getDataFactory().getOWLConstantAnnotation(getURI(tag), con);
                    OWLEntityAnnotationAxiom ax = getDataFactory().getOWLEntityAnnotationAxiom(entity, anno);
                    owlOntologyManager.applyChange(new AddAxiom(ontology, ax));
                }
            }
            else {
                // Everything as annotations except imports?
                if(tag.equals("import")) {
//                    logger.info("Importing: " + value);
                    try {
                        URI uri = URI.create(value.trim());
                        OWLOntology importedOnt = owlOntologyManager.loadOntology(uri);
                        owlOntologyManager.applyChange(new AddAxiom(ontology, owlOntologyManager.getOWLDataFactory().getOWLImportsDeclarationAxiom(ontology, importedOnt.getURI())));
                    }
                    catch (OWLOntologyCreationException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    OWLConstant con = getDataFactory().getOWLUntypedConstant(value);
                    OWLAnnotation anno = getDataFactory().getOWLConstantAnnotation(getURI(tag), con);
                    OWLOntologyAnnotationAxiom ax = getDataFactory().getOWLOntologyAnnotationAxiom(ontology, anno);
                    owlOntologyManager.applyChange(new AddAxiom(ontology, ax));
                }

            }
        }
        catch (OWLOntologyChangeException e) {
            logger.severe(e.getMessage());
        }
    }


    private OWLDataFactory getDataFactory() {
        return getOWLOntologyManager().getOWLDataFactory();
    }


    public OWLClass getCurrentClass() {
        return getDataFactory().getOWLClass(getURI(currentId));
    }

    public OWLEntity getCurrentEntity() {
        if(isTerm()) {
            return getCurrentClass();
        }
        else if(isTypedef()) {
            return getDataFactory().getOWLObjectProperty(getURI(currentId));
        }
        else {
            return getDataFactory().getOWLIndividual(getURI(currentId));
        }
    }


    public URI getURI(String s) {
        if (s == null) {
            for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                System.out.println(e);
            }
        }
        URI uri = uriCache.get(s);
        if (uri != null) {
            return uri;
        }
        String localName = s;
        String namespace = getDefaultNamespace();
//        int sepIndex = s.indexOf(':');
//        if (sepIndex != -1) {
        localName = s.replace(':', '_');
//            localName = s.substring(sepIndex + 1, localName.length());
//        }
        if (currentNamespace != null) {
            namespace = currentNamespace;
        }
        localName = localName.replace(' ', '-');
        try {
            uri = new URI(namespace + localName);
        }
        catch (URISyntaxException e) {
            System.out.println("URI Syntax Exception: " + namespace + localName);
        }
        uriCache.put(s, uri);

        return uri;
    }
}
