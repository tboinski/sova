package org.coode.owl.rdf.rdfxml;

import org.apache.commons.lang.StringEscapeUtils;
import org.coode.owl.rdf.model.*;
import org.coode.xml.OWLOntologyNamespaceManager;
import org.coode.xml.XMLWriterFactory;
import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.SWRLVariableExtractor;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;
import static org.semanticweb.owl.vocab.OWLRDFVocabulary.*;

import java.io.Writer;
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
 * Date: 06-Dec-2006<br><br>
 */
public class RDFXMLRenderer {

    private OWLOntologyManager manager;

    private OWLOntology ontology;

    private RDFGraph graph;

    private RDFXMLWriter writer;

    private Set<RDFResourceNode> pending;

    private Set<URI> annotationURIs;

    private Set<URI> prettyPrintedTypes;


    public RDFXMLRenderer(OWLOntologyManager manager, OWLOntology ontology, Writer w) {
        this.ontology = ontology;
        this.manager = manager;
        pending = new HashSet<RDFResourceNode>();
        writer = new RDFXMLWriter(XMLWriterFactory.getInstance().createXMLWriter(w,
                                                                                 new OWLOntologyNamespaceManager(manager,
                                                                                                                 ontology),
                                                                                 ontology.getURI().toString()));
        annotationURIs = ontology.getAnnotationURIs();
        prettyPrintedTypes = new HashSet<URI>();
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_CLASS.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_OBJECT_PROPERTY.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_DATA_PROPERTY.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_ANNOTATION_PROPERTY.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_RESTRICTION.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_THING.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_NOTHING.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_ONTOLOGY.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_NEGATIVE_DATA_PROPERTY_ASSERTION.getURI());
        prettyPrintedTypes.add(OWLRDFVocabulary.OWL_NEGATIVE_OBJECT_PROPERTY_ASSERTION.getURI());
    }


    public void render() {
        writer.startDocument();

        // Put imports at the top of the rendering
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        axioms.addAll(ontology.getImportsDeclarations());
        axioms.addAll(ontology.getOntologyAnnotationAxioms());
        createGraph(axioms);
        graph.addTriple(new RDFTriple(new RDFResourceNode(ontology.getURI()),
                                      new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                      new RDFResourceNode(OWLRDFVocabulary.OWL_ONTOLOGY.getURI())));
        render(new RDFResourceNode(ontology.getURI()));
        for (OWLOntologyAnnotationAxiom ax : ontology.getOntologyAnnotationAxioms()) {
            if (!ax.getSubject().equals(ontology)) {
                render(new RDFResourceNode(ax.getSubject().getURI()));
            }
        }

        // Annotation properties

        Set<URI> annotationURIs = new HashSet<URI>(ontology.getAnnotationURIs());
        annotationURIs.removeAll(OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTIES);
        Map<URI, Set<OWLAnnotation>> annoURIAnnotations = Collections.emptyMap();
        OWLOntologyFormat format = manager.getOntologyFormat(ontology);

        if (format instanceof RDFXMLOntologyFormat) {
            RDFXMLOntologyFormat rdfXMLFormat = (RDFXMLOntologyFormat) format;
            annotationURIs.addAll(rdfXMLFormat.getAnnotationURIs());
            annoURIAnnotations = rdfXMLFormat.getAnnotationURIAnnotations();
        }
        if (!annotationURIs.isEmpty()) {
            writeBanner("Annotation properties");
            graph = new RDFGraph();
            for (URI uri : annotationURIs) {
                graph.addTriple(new RDFTriple(new RDFResourceNode(uri),
                                              new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.OWL_ANNOTATION_PROPERTY.getURI())));
                //////////////////////////////////////////////////////////////////////////////////////////
                // The following is a HACK which will be left in place until the OWL 1.1 spec is
                // fixed w.r.t. annotations on annotation URIs
                for(OWLAnnotation anno : getAnnotationsForURIViaHack(annoURIAnnotations, uri)) {
                    if(anno.isAnnotationByConstant()) {
                        OWLConstant con = anno.getAnnotationValueAsConstant();
                        RDFLiteralNode obj;
                        if(con.isTyped()) {
                            obj = new RDFLiteralNode(con.getLiteral(), con.asOWLTypedConstant().getDataType().getURI());
                        }
                        else {
                            if(con.asOWLUntypedConstant().hasLang()) {
                                obj = new RDFLiteralNode(con.getLiteral(), con.asOWLUntypedConstant().getLang());
                            }
                            else {
                                obj = new RDFLiteralNode(con.getLiteral());
                            }
                        }
                        graph.addTriple(new RDFTriple(new RDFResourceNode(uri),
                                                      new RDFResourceNode(anno.getAnnotationURI()),
                                                      obj));
                    }
                    else {
                        if(anno.getAnnotationValue() instanceof OWLNamedObject) {
                            OWLNamedObject obj = (OWLNamedObject) anno.getAnnotationValue();
                            graph.addTriple(new RDFTriple(new RDFResourceNode(uri),
                                                      new RDFResourceNode(anno.getAnnotationURI()),
                                                      new RDFResourceNode(obj.getURI())));
                        }
                    }
                }
                // End of HACK
                /////////////////////////////////////////////////////////////////////////////////////////
                render(new RDFResourceNode(uri));
            }
        }


        Set<OWLObjectProperty> objectProperties = ontology.getReferencedObjectProperties();
        if (!objectProperties.isEmpty()) {
            writeBanner("Object Properties");
            for (OWLObjectProperty prop : toSortedSet(objectProperties)) {
                writer.writeComment(StringEscapeUtils.escapeXml(prop.getURI().toString()));
                createGraph(prop);
                render(new RDFResourceNode(prop.getURI()));
                renderAnonRoots();
            }
        }

        Set<OWLDataProperty> dataProperties = ontology.getReferencedDataProperties();
        if (!dataProperties.isEmpty()) {
            writeBanner("Data properties");
            for (OWLDataProperty prop : toSortedSet(ontology.getReferencedDataProperties())) {
                writer.writeComment(StringEscapeUtils.escapeXml(prop.getURI().toString()));
                createGraph(prop);
                render(new RDFResourceNode(prop.getURI()));
                renderAnonRoots();
            }
        }


        Set<OWLClass> clses = ontology.getReferencedClasses();
        if (!clses.isEmpty()) {
            writeBanner("Classes");
            for (OWLClass cls : toSortedSet(clses)) {
                writer.writeComment(StringEscapeUtils.escapeXml(cls.getURI().toString()));
                createGraph(cls);
                render(new RDFResourceNode(cls.getURI()));
                renderAnonRoots();
            }
        }


        Set<? extends OWLIndividual> individuals = ontology.getReferencedIndividuals();
        if (!individuals.isEmpty()) {
            writeBanner("Individuals");
            for (OWLIndividual ind : toSortedSet(ontology.getReferencedIndividuals())) {
                writer.writeComment(StringEscapeUtils.escapeXml(ind.getURI().toString()));
                createGraph(ind);
                render(new RDFResourceNode(ind.getURI()));
                renderAnonRoots();
            }
        }


        Set<OWLAxiom> generalAxioms = new HashSet<OWLAxiom>();
        generalAxioms.addAll(ontology.getGeneralClassAxioms());
        generalAxioms.addAll(ontology.getPropertyChainSubPropertyAxioms());
        createGraph(generalAxioms);

        Set<RDFResourceNode> rootNodes = graph.getRootAnonymousNodes();
        if (!rootNodes.isEmpty()) {
            writeBanner("General axioms");
            renderAnonRoots();
        }

        Set<SWRLRule> ruleAxioms = ontology.getRules();
        createGraph(ruleAxioms);
        if (!ruleAxioms.isEmpty()) {
            writeBanner("Rules");
            SWRLVariableExtractor variableExtractor = new SWRLVariableExtractor();
            for (SWRLRule rule : ruleAxioms) {
                if (!rule.isAnonymous()) {
                    render(new RDFResourceNode(rule.getURI()));
                }
                rule.accept(variableExtractor);
            }
            for(SWRLAtomVariable var : variableExtractor.getIVariables()) {
                render(new RDFResourceNode(var.getURI()));
            }

            for(SWRLAtomVariable var : variableExtractor.getDVariables()) {
                render(new RDFResourceNode(var.getURI()));
            }
            renderAnonRoots();
        }

        writer.endDocument();
    }


    private Set<OWLAnnotation> getAnnotationsForURIViaHack(Map<URI, Set<OWLAnnotation>> annoURIAnnotations, URI uri) {
        Set<OWLAnnotation> annos =  annoURIAnnotations.get(uri);
        if(annos != null) {
            return annos;
        }
        else {
            return Collections.emptySet();
        }
    }


    private void createGraph(OWLEntity entity) {
        final Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        axioms.addAll(entity.getAnnotationAxioms(ontology));
        entity.accept(new OWLEntityVisitor() {
            public void visit(OWLClass cls) {
                axioms.addAll(ontology.getAxioms(cls));
                createGraph(axioms);
            }


            public void visit(OWLDataType dataType) {
            }


            public void visit(OWLIndividual individual) {
                axioms.addAll(ontology.getAxioms(individual));
                createGraph(axioms);
            }


            public void visit(OWLDataProperty property) {
                axioms.addAll(ontology.getAxioms(property));
                createGraph(axioms);
            }


            public void visit(OWLObjectProperty property) {
                axioms.addAll(ontology.getAxioms(property));
                axioms.addAll(ontology.getAxioms(manager.getOWLDataFactory().getOWLObjectPropertyInverse(property)));
                createGraph(axioms);
            }
        });
        addTypeTriple(entity);
    }


    private void createGraph(Set<? extends OWLAxiom> axioms) {
        RDFTranslator translator = new RDFTranslator(manager, ontology);
        for (OWLAxiom ax : axioms) {
            ax.accept(translator);
        }
        graph = translator.getGraph();
    }


    private void addTypeTriple(OWLEntity entity) {
        entity.accept(new OWLEntityVisitor() {
            public void visit(OWLClass cls) {
                graph.addTriple(new RDFTriple(new RDFResourceNode(cls.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.OWL_CLASS.getURI())));
            }


            public void visit(OWLDataType dataType) {
                graph.addTriple(new RDFTriple(new RDFResourceNode(dataType.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.RDFS_DATATYPE.getURI())));
            }


            public void visit(OWLIndividual individual) {
            }


            public void visit(OWLDataProperty property) {
                graph.addTriple(new RDFTriple(new RDFResourceNode(property.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.OWL_DATA_PROPERTY.getURI())));
                if (annotationURIs.contains(property.getURI())) {
                    graph.addTriple(new RDFTriple(new RDFResourceNode(property.getURI()),
                                                  new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                                  new RDFResourceNode(OWLRDFVocabulary.OWL_ANNOTATION_PROPERTY.getURI())));
                }
            }


            public void visit(OWLObjectProperty property) {
                graph.addTriple(new RDFTriple(new RDFResourceNode(property.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                              new RDFResourceNode(OWLRDFVocabulary.OWL_OBJECT_PROPERTY.getURI())));
                if (annotationURIs.contains(property.getURI())) {
                    graph.addTriple(new RDFTriple(new RDFResourceNode(property.getURI()),
                                                  new RDFResourceNode(OWLRDFVocabulary.RDF_TYPE.getURI()),
                                                  new RDFResourceNode(OWLRDFVocabulary.OWL_ANNOTATION_PROPERTY.getURI())));
                }
            }
        });
    }


    private void writeBanner(String name) {
        writer.writeComment(
                "\n///////////////////////////////////////////////////////////////////////////////////////\n" + "//\n" + "// " + name + "\n" + "//\n" + "///////////////////////////////////////////////////////////////////////////////////////\n");
    }


    private static <N extends OWLEntity> Set<N> toSortedSet(Set<N> entities) {
        Set<N> results = new TreeSet<N>(new Comparator<OWLEntity>() {
            public int compare(OWLEntity o1, OWLEntity o2) {
                return o1.getURI().compareTo(o2.getURI());
            }
        });
        results.addAll(entities);
        return results;
    }


    public void renderAnonRoots() {
        for (RDFResourceNode node : graph.getRootAnonymousNodes()) {
            render(node);
        }
    }


    public void render(RDFResourceNode node) {
        if (pending.contains(node)) {
            // We essentially remove all structure sharing during parsing - any cycles therefore indicate a bug!
            throw new IllegalStateException("Rendering cycle!  This indicates structure sharing and should not happen!");
        }
        pending.add(node);
        Set<RDFTriple> triples = new TreeSet<RDFTriple>(new TripleComparator());
        triples.addAll(graph.getTriplesForSubject(node));
        RDFTriple candidatePrettyPrintTypeTriple = null;
        for (RDFTriple triple : graph.getTriplesForSubject(node)) {
            URI propertyURI = triple.getProperty().getURI();
            if (propertyURI.equals(OWLRDFVocabulary.RDF_TYPE.getURI()) && !triple.getObject().isAnonymous()) {
                if (OWLRDFVocabulary.BUILT_IN_VOCABULARY.contains(triple.getObject().getURI())) {
                    if (prettyPrintedTypes.contains(triple.getObject().getURI())) {
                        candidatePrettyPrintTypeTriple = triple;
                    }
                }
                else {
                    candidatePrettyPrintTypeTriple = triple;
                }
            }
        }
        if (candidatePrettyPrintTypeTriple == null) {
            writer.writeStartElement(RDF_DESCRIPTION.getURI());
        }
        else {
            writer.writeStartElement(candidatePrettyPrintTypeTriple.getObject().getURI());
        }
        if (!node.isAnonymous()) {
            writer.writeAboutAttribute(node.getURI());
        }
        for (RDFTriple triple : triples) {
            if (candidatePrettyPrintTypeTriple != null && candidatePrettyPrintTypeTriple.equals(triple)) {
                continue;
            }
            writer.writeStartElement(triple.getProperty().getURI());
            RDFNode objectNode = triple.getObject();
            if (!objectNode.isLiteral()) {
                RDFResourceNode objectRes = (RDFResourceNode) objectNode;
                if (objectRes.isAnonymous()) {
                    // Special rendering for lists
                    if (isObjectList(objectRes)) {
                        writer.writeParseTypeAttribute();
                        List<RDFNode> list = new ArrayList<RDFNode>();
                        toJavaList(objectRes, list);
                        for (RDFNode n : list) {
                            if (n.isAnonymous()) {
                                render((RDFResourceNode) n);
                            }
                            else {
                                if (n.isLiteral()) {
                                    RDFLiteralNode litNode = (RDFLiteralNode) n;
                                    writer.writeStartElement(OWLRDFVocabulary.RDFS_LITERAL.getURI());
                                    if (litNode.getDatatype() != null) {
                                        writer.writeDatatypeAttribute(litNode.getDatatype());
                                    }
                                    else if (litNode.getLang() != null) {
                                        writer.writeLangAttribute(litNode.getLang());
                                    }
                                    writer.writeTextContent((litNode.getLiteral()));
                                    writer.writeEndElement();
                                }
                                else {
                                    writer.writeStartElement(RDF_DESCRIPTION.getURI());
                                    writer.writeAboutAttribute(n.getURI());
                                    writer.writeEndElement();
                                }
                            }
                        }
                    }
                    else {
                        render(objectRes);
                    }
                }
                else {
                    writer.writeResourceAttribute(objectRes.getURI());
                }
            }
            else {
                RDFLiteralNode rdfLiteralNode = ((RDFLiteralNode) objectNode);
                if (rdfLiteralNode.getDatatype() != null) {
                    writer.writeDatatypeAttribute(rdfLiteralNode.getDatatype());
                }
                else if (rdfLiteralNode.getLang() != null) {
                    writer.writeLangAttribute(rdfLiteralNode.getLang());
                }
                writer.writeTextContent(rdfLiteralNode.getLiteral());
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
        pending.remove(node);
    }


    private boolean isObjectList(RDFResourceNode node) {
        for (RDFTriple triple : graph.getTriplesForSubject(node)) {
            if (triple.getProperty().getURI().equals(RDF_TYPE.getURI())) {
                if (!triple.getObject().isAnonymous()) {
                    if (triple.getObject().getURI().equals(RDF_LIST.getURI())) {
                        List<RDFNode> items = new ArrayList<RDFNode>();
                        toJavaList(node, items);
                        for (RDFNode n : items) {
                            if (n.isLiteral()) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void toJavaList(RDFNode node, List<RDFNode> list) {
        for (RDFTriple triple : graph.getTriplesForSubject(node)) {
            if (triple.getProperty().getURI().equals(RDF_FIRST.getURI())) {
                list.add(triple.getObject());
            }
            else {
                if (triple.getProperty().getURI().equals(RDF_REST.getURI())) {
                    if (!triple.getObject().isAnonymous()) {
                        if (triple.getObject().getURI().equals(RDF_NIL.getURI())) {
                            // End of list
                        }
                    }
                    else {
                        // Should be another list
                        toJavaList(triple.getObject(), list);
                    }
                }
            }
        }
    }


    private class TripleComparator implements Comparator<RDFTriple> {

        private List<URI> orderedURIs;


        public TripleComparator() {
            orderedURIs = new ArrayList<URI>();
            orderedURIs.add(RDF_TYPE.getURI());
            orderedURIs.add(RDFS_LABEL.getURI());
            orderedURIs.add(OWL_EQUIVALENT_CLASS.getURI());
            orderedURIs.add(RDFS_SUBCLASS_OF.getURI());
            orderedURIs.add(OWL_DISJOINT_WITH.getURI());

            orderedURIs.add(OWL_ON_PROPERTY.getURI());
            orderedURIs.add(OWL_DATA_RANGE.getURI());
            orderedURIs.add(OWL_ON_CLASS.getURI());

            orderedURIs.add(RDF_SUBJECT.getURI());
            orderedURIs.add(RDF_PREDICATE.getURI());
            orderedURIs.add(RDF_OBJECT.getURI());
        }


        private int getIndex(URI uri) {
            int index = orderedURIs.indexOf(uri);
            if (index == -1) {
                index = orderedURIs.size();
            }
            return index;
        }


        public int compare(RDFTriple o1, RDFTriple o2) {
            int diff = getIndex(o1.getProperty().getURI()) - getIndex(o2.getProperty().getURI());
            if (diff == 0) {
                diff = 1;
            }
            return diff;
        }
    }
}
