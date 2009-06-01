package org.coode.owl.owlxmlparser;

import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import static org.semanticweb.owl.vocab.OWLXMLVocabulary.*;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Date: 13-Dec-2006<br><br>
 * <p/>
 * A handler which knows about OWLXML
 */
public class OWLXMLParserHandler extends DefaultHandler {

    private OWLOntologyManager owlOntologyManager;

    private OWLOntology ontology;

    private List<OWLElementHandler> handlerStack;

    private Map<String, OWLElementHandlerFactory> handlerMap;

    private Map<String, String> prefix2NamespaceMap;

    private Locator locator;

    /**
     * Creates an OWLXML handler.
     * @param owlOntologyManager The manager that should be used to obtain a data factory,
     *                           imported ontologies etc.
     * @param ontology           The ontology that the XML representation will be parsed into.
     */
    public OWLXMLParserHandler(OWLOntologyManager owlOntologyManager, OWLOntology ontology) {
        this(owlOntologyManager, ontology, null);
    }


    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }


    /**
     * Creates an OWLXML handler with the specified top level handler.  This allows OWL/XML
     * representations of axioms to be embedded in abitrary XML documents e.g. DIG 2.0 documents.
     * (The default handler behaviour expects the top level element to be an Ontology
     * element).
     * @param owlOntologyManager The manager that should be used to obtain a data factory,
     *                           imported ontologies etc.
     * @param ontology           The ontology object that the XML representation should be parsed into.
     * @param topHandler         The handler for top level elements - may be <code>null</code>, in which
     *                           case the parser will expect an Ontology element to be the root element.
     */
    public OWLXMLParserHandler(OWLOntologyManager owlOntologyManager, OWLOntology ontology,
                               OWLElementHandler topHandler) {
        this.owlOntologyManager = owlOntologyManager;
        this.ontology = ontology;
        handlerStack = new ArrayList<OWLElementHandler>();
        prefix2NamespaceMap = new HashMap<String, String>();
        if (topHandler != null) {
            handlerStack.add(0, topHandler);
        }
        handlerMap = new HashMap<String, OWLElementHandlerFactory>();

        addFactory(new AbstractElementHandlerFactory(ONTOLOGY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLOntologyHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(ANNOTATION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLAnnotationElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(CONSTANT) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLConstantElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(IMPORTS) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLImportsHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(CLASS) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLClassElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectPropertyElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(INVERSE_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLInverseObjectPropertyElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataPropertyElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(INDIVIDUAL) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLIndividualElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(DATA_COMPLEMENT_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataComplementOfElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_ONE_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataOneOfElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATATYPE) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataTypeElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATATYPE_RESTRICTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDatatypeRestrictionElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(DATATYPE_FACET_RESTRICTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDatatypeFacetRestrictionElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_INTERSECTION_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectIntersectionOfElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_UNION_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectUnionOfElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_COMPLEMENT_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectComplementOfElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_ONE_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectOneOfElementHandler(handler);
            }
        });

        // Object Restrictions

        addFactory(new AbstractElementHandlerFactory(OBJECT_SOME_VALUES_FROM) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectSomeValuesFromElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_ALL_VALUES_FROM) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectAllValuesFromElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_EXISTS_SELF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectExistsSelfElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_HAS_VALUE) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectHasValueElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_MIN_CARDINALITY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectMinCardinalityElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_EXACT_CARDINALITY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectExactCardinalityElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_MAX_CARDINALITY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectMaxCardinalityElementHandler(handler);
            }
        });

        // Data Restrictions

        addFactory(new AbstractElementHandlerFactory(DATA_SOME_VALUES_FROM) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataSomeValuesFromElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_ALL_VALUES_FROM) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataAllValuesFromElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_HAS_VALUE) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataHasValueElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_MIN_CARDINALITY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataMinCardinalityElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_EXACT_CARDINALITY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataExactCardinalityElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_MAX_CARDINALITY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataMaxCardinalityElementHandler(handler);
            }
        });

        // Axioms

        addFactory(new AbstractElementHandlerFactory(SUB_CLASS_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLSubClassAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(EQUIVALENT_CLASSES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLEquivalentClassesAxiomElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(DISJOINT_CLASSES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDisjointClassesAxiomElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(DISJOINT_UNION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDisjointUnionElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(UNION_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLUnionOfElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(SUB_OBJECT_PROPERTY_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLSubObjectPropertyOfAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(SUB_OBJECT_PROPERTY_CHAIN) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLSubObjectPropertyChainElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(EQUIVALENT_OBJECT_PROPERTIES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLEquivalentObjectPropertiesAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DISJOINT_OBJECT_PROPERTIES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDisjointObjectPropertiesAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_PROPERTY_DOMAIN) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectPropertyDomainElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_PROPERTY_RANGE) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectPropertyRangeAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(INVERSE_OBJECT_PROPERTIES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLInverseObjectPropertiesAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(FUNCTIONAL_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLFunctionalObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(INVERSE_FUNCTIONAL_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLInverseFunctionalObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(SYMMETRIC_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLSymmetricObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(ANTI_SYMMETRIC_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLAntisymmetricObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(REFLEXIVE_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLReflexiveObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(IRREFLEXIVE_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLIrreflexiveObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(TRANSITIVE_OBJECT_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLTransitiveObjectPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(SUB_DATA_PROPERTY_OF) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLSubDataPropertyOfAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(EQUIVALENT_DATA_PROPERTIES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLEquivalentDataPropertiesAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DISJOINT_DATA_PROPERTIES) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDisjointDataPropertiesAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_PROPERTY_DOMAIN) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataPropertyDomainAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DATA_PROPERTY_RANGE) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataPropertyRangeAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(FUNCTIONAL_DATA_PROPERTY) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLFunctionalDataPropertyAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(SAME_INDIVIDUALS) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLSameIndividualsAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DIFFERENT_INDIVIDUALS) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDifferentIndividualsAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(CLASS_ASSERTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLClassAssertionAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(OBJECT_PROPERTY_ASSERTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLObjectPropertyAssertionAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(NEGATIVE_OBJECT_PROPERTY_ASSERTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLNegativeObjectPropertyAssertionAxiomElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(NEGATIVE_DATA_PROPERTY_ASSERTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLNegativeDataPropertyAssertionAxiomElementHandler(handler);
            }
        });


        addFactory(new AbstractElementHandlerFactory(DATA_PROPERTY_ASSERTION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDataPropertyAssertionAxiomElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(ENTITY_ANNOTATION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLEntityAnnotationElementHandler(handler);
            }
        });

        addFactory(new AbstractElementHandlerFactory(DECLARATION) {
            public OWLElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLDeclarationAxiomElementHandler(handler);
            }
        });
    }


    /**
     * Gets the line number that the parser is at.
     * @return A positive integer that represents the line number or
     * -1 if the line number is not known.
     */
    public int getLineNumber() {
        if(locator != null) {
            return locator.getLineNumber();
        }
        else {
            return -1;
        }
    }

    private Map<String, URI> uriMap = new HashMap<String, URI>();

    public URI getURI(String string) throws OWLXMLParserException {
        try {
            URI uri = uriMap.get(string);
            if (uri == null) {
                uri = new URI(string);
                uriMap.put(string, uri);
            }
            return uri;
        }
        catch (URISyntaxException e) {
            throw new OWLXMLParserException(getLineNumber(), e);
        }
    }


    public Map<String, String> getPrefix2NamespaceMap() {
        return prefix2NamespaceMap;
    }


    private void addFactory(OWLElementHandlerFactory factory) {
        handlerMap.put(factory.getElementName().getShortName(), factory);
    }


    public OWLOntology getOntology() {
        return ontology;
    }


    public OWLDataFactory getDataFactory() {
        return getOWLOntologyManager().getOWLDataFactory();
    }


    public void startDocument() throws SAXException {

    }


    public void endDocument() throws SAXException {

    }


    public void characters(char ch[], int start, int length) throws SAXException {
        if (!handlerStack.isEmpty()) {
            try {
                OWLElementHandler handler = handlerStack.get(0);
                if (handler.isTextContentPossible()) {
                    handler.handleChars(ch, start, length);
                }
            }
            catch (OWLException e) {
                throw new SAXException(e);
            }
        }
        else {
            throw new SAXException("Unexpected text content: " + ch);
        }
    }


    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            OWLElementHandlerFactory handlerFactory = handlerMap.get(localName);
            if (handlerFactory == null) {
                throw new OWLXMLParserUnknownElementType(getLineNumber(), uri + localName);
            }
            OWLElementHandler handler = handlerFactory.createHandler(this);
            if (!handlerStack.isEmpty()) {
                OWLElementHandler topElement = handlerStack.get(0);
                handler.setParentHandler(topElement);
            }
            handlerStack.add(0, handler);
            for (int i = 0; i < attributes.getLength(); i++) {
                handler.attribute(attributes.getLocalName(i), attributes.getValue(i));
            }
            handler.startElement(uri);
        }
        catch (OWLException e) {
            throw new SAXException("(Current element " + localName, e);
        }
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            OWLElementHandler handler = handlerStack.remove(0);
            handler.endElement();
        }
        catch (OWLException e) {
            throw new SAXException(e);
        }
    }


    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        prefix2NamespaceMap.put(prefix, uri);
    }


    public OWLOntologyManager getOWLOntologyManager() {
        return owlOntologyManager;
    }
}
