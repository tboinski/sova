/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of SOVA.  SOVA is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.pg.eti.kask.sova.graph;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import prefuse.data.Graph;
import prefuse.data.Node;

import org.pg.eti.kask.sova.utils.Debug;
import org.semanticweb.owlapi.model.*;

import prefuse.data.Table;

/**
 * Konwerter obiektu OWLOntology na obiekt Graph wykorzystywany przez Prefuse.
 * Klasa jest singletonem.
 * 
 * @author Anna Jaworska
 * @author Piotr Kunowski
 */
public class OWLtoGraphConverter {

//	private static final OWLtoGraphConverter INSTANCE = new OWLtoGraphConverter();
    Graph graph;
    public static final String COLUMN_IRI = "IRI";
    private static final String COLUMN_NAME_NODE = "node.name";
    private static String COLUMN_NODE = "node";
    private Table edges;
    Hashtable<String, Integer> classes;
    Hashtable<String, Integer> properties;
    Hashtable<String, Integer> individuals;
    Hashtable<String, Integer> dataProperties;
    Hashtable<String, Integer> dataTypes;
    Hashtable<String, Integer> anonyms;
    int thingNumber = 0;
    int defaultNumber;
    // Private constructor prevents instantiation from other classes

    public OWLtoGraphConverter() {
        Debug.sendMessage("OWLtoGraphConverter constructor call");
        graph = new Graph();
        edges = graph.getEdgeTable();
        classes = new Hashtable<String, Integer>();
        properties = new Hashtable<String, Integer>();
        dataProperties = new Hashtable<String, Integer>();
        dataTypes = new Hashtable<String, Integer>();
        individuals = new Hashtable<String, Integer>();
        anonyms = new Hashtable<String, Integer>();
        //anonyms.put("dummy", -100);
    }

//	public static OWLtoGraphConverter getInstance() {
//		return INSTANCE;
//	}
//	
    private void insertDataType(OWLOntology ontology, Graph graph) {
        for (OWLDataProperty prop : ontology.getDataPropertiesInSignature()) {

            System.out.println("DATATYPE :  " + prop.toString());
            Set<OWLDataRange> s = prop.getRanges(ontology);
            Iterator<OWLDataRange> it = s.iterator();
            int dataPropertyRowNr = 0;
            if (!dataProperties.containsKey(prop.toString())) {
                Node dataProperty = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.PropertyNode();
                node.setLabel(prop.getIRI().getFragment());
                dataProperty.set(COLUMN_NODE, node);
                dataProperty.set(COLUMN_IRI, prop.getIRI());
                dataPropertyRowNr = dataProperty.getRow();
                dataProperties.put(prop.toString(), dataPropertyRowNr);
            } else {
                dataPropertyRowNr = dataProperties.get(prop.toString());
            }

            while (it.hasNext()) {
                OWLDataRange r = it.next();
                if (r.isDatatype()) {
                    int dataTypeRowNr = 0;
                    if (!dataTypes.containsKey(r.toString())) {
                        Node dataType = graph.addNode();
                        org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.DataTypeNode();
                        node.setLabel(r.toString());
                        dataType.set(COLUMN_NODE, node);
                        dataTypeRowNr = dataType.getRow();
                        dataTypes.put(r.toString(), dataTypeRowNr);
                    } else {
                        dataTypeRowNr = dataTypes.get(r.toString());
                    }
                    int row = edges.addRow();
                    edges.set(row, "source", dataPropertyRowNr);
                    edges.set(row, "target", dataTypeRowNr);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.RangeEdge());


                }
            }
        }

    }

    /**
     * Umieszcza w grafie wszystkie klasy nie-anonimowe zawarte w ontologii oraz
     * krawędzie między węzłem thing i klasami będącymi jego subclasses.
     *
     * @param ontology
     * @param graph
     */
    private void insertBaseClasses(OWLOntology ontology, Graph graph) {

        for (OWLClass cls : ontology.getClassesInSignature()) {
            // dodajemy na sucho wszystkie klasy bez krawedzi
            // oprocz Thing
            if (!cls.isOWLThing()) {
                Node n = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.ClassNode();
                node.setLabel(cls.getIRI().getFragment());
                n.set(COLUMN_NODE, node);
                n.set(COLUMN_IRI, cls.getIRI());
                n.set(COLUMN_NAME_NODE, cls.getIRI().getFragment());
                //n.set(COLUMN_NAME_NODE, cls.toString());
                if (cls.getSuperClasses(ontology).isEmpty() == true) { // thing
                    // jest
                    // superklasa
                    int row = edges.addRow();
                    edges.set(row, "source", thingNumber);
                    edges.set(row, "target", n.getRow());
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.SubClassEdge());
                }
                classes.put(cls.getIRI().getFragment(), n.getRow());

            }
        }

    }

    /**
     * Umieszcza w grafie wszystkie zdefiniowane w ontologii property
     *
     * @param ontology
     * @param graph
     * @param properties
     */
    private void insertBaseProperties(OWLOntology ontology, Graph graph) {
        // dodajemy wszystkie definicje property
        for (OWLObjectProperty property : ontology.getObjectPropertiesInSignature()) {
            Node n = graph.addNode();
            org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.PropertyNode();
            node.setLabel(property.getIRI().getFragment());
            n.set(COLUMN_NODE, node);
            n.set(COLUMN_IRI, property.getIRI());
            n.set(COLUMN_NAME_NODE, property.getIRI().getFragment());
            properties.put(property.toString(), n.getRow());
        }
    }

    /**
     * umieszcze w grafie węzły typu individual w grafie
     *
     * @param ontology
     * @param graph
     * @param individuals
     */
    private void insertBaseIndividuals(OWLOntology ontology, Graph graph) {
        // dodajemy wszystkie definicje individuals
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
            Node n = graph.addNode();
            org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.IndividualNode();
            node.setLabel(individual.getIRI().getFragment());
            n.set(COLUMN_NODE, node);
            n.set(COLUMN_IRI, individual.getIRI());
            n.set(COLUMN_NAME_NODE, individual.getIRI().getFragment());
            individuals.put(individual.getIRI().getFragment(), n.getRow());
        }

        for (OWLAnonymousIndividual individual : ontology.getReferencedAnonymousIndividuals()) {
            Node n = graph.addNode();
            org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.IndividualNode();
            try {
                String[] split = individual.getID().getID().split("#");
                node.setLabel("Anonymous: " + split[1]);
            } catch (Exception e) {
                node.setLabel(individual.getID().getID());
            }

            n.set(COLUMN_NODE, node);
            n.set(COLUMN_IRI, IRI.create(individual.getID().getID()));
            n.set(COLUMN_NAME_NODE, individual.getID().getID());
            individuals.put(individual.getID().getID(), n.getRow());
        }
    }

    /**
     * Metoda umieszcza w grafie krawędzi i/lub wierzchołki odpowiadające danemu
     * obiektowi OWLClassExpression
     *
     * @param description
     * @return
     * @throws Exception
     */
    private int DescriptionHandler(OWLClassExpression description) throws Exception {
        // ObjectSomeValueFrom
        if (description instanceof OWLObjectSomeValuesFrom) {
            // dodaj anonimowe node - uchwyt do niego powinien byc zwrocony

            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                // anonyms.put(description.toString(), description.hashCode());
                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + "dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.AnonymousClassNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());
                // Tworzenie somenode
                Node someValNode = graph.addNode();
                if (((OWLObjectSomeValuesFrom) description).getProperty() instanceof OWLObjectProperty) {
                    // wstaw SomeNode i polacz

                    // someVal.put(((OWLObjectSomeValuesFrom)
                    // description).getProperty().toString(),
                    // ((OWLObjectSomeValuesFrom)
                    // description).getProperty().hashCode());
                    // System.out.println("SomeVal NODE DLA " +
                    // ((OWLObjectSomeValuesFrom) description).getProperty());
                    org.pg.eti.kask.sova.nodes.Node sNode = new org.pg.eti.kask.sova.nodes.SomeValuesFromPropertyNode();
                    sNode.setLabel(((OWLObjectSomeValuesFrom) description).getProperty().asOWLObjectProperty().getIRI().getFragment());
                    //tutaj zmieniłem
                    someValNode.set(COLUMN_NODE, sNode);

                    // dodanie krawedzi laczacej node SomeValuesFrom z definicja
                    // property
                    int definitionID = properties.get(((OWLObjectSomeValuesFrom) description).getProperty().toString());
                    int someRow = edges.addRow();
                    edges.set(someRow, "source", definitionID);
                    edges.set(someRow, "target", someValNode.getRow());
                    edges.set(someRow, "edge", new org.pg.eti.kask.sova.edges.InstancePropertyEdge());
                } else {
                    // wywolaj rekurencyjnie i poznaj czym jest property
                    // probably will never enter here;
                    throw new Exception("Some values from somethign wired "
                            + ((OWLObjectSomeValuesFrom) description).getProperty());

                }
                // System.out.println("EDGE MIEDZY ANONYM I SOMEVAL " +
                // someValNode.getRow() +" " +anonymNode.getRow() );
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", someValNode.getRow());
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.PropertyEdge());

                // tworzenie wynikowego node
                int targetId = 0;
                if (((OWLObjectSomeValuesFrom) description).getFiller() instanceof OWLClass) {
                    // pobierz drugie node z z klas
                    // System.out.println("Some val from klass "
                    // +((OWLObjectSomeValuesFrom) description).getFiller() /*
                    // + " " + classes.get(((OWLObjectSomeValuesFrom)
                    // description).getFiller().toString() ) */ );
                    targetId = classes.get(((OWLClass) (((OWLObjectSomeValuesFrom) description).getFiller())).getIRI().getFragment());
                } else {
                    // to cos bardziej skomplikowanego niz klasa lzu individual
                    // System.out.println("More complicated");
                    targetId = DescriptionHandler(((OWLObjectSomeValuesFrom) description).getFiller());
                }
                // polacz edgami i ogolnie upenij sie za bangla
                int row2 = edges.addRow();
                edges.set(row2, "source", someValNode.getRow());
                edges.set(row2, "target", targetId);
                edges.set(row2, "edge", new org.pg.eti.kask.sova.edges.PropertyEdge());

                return anonymNode.getRow();
            }

        } else if (description instanceof OWLObjectAllValuesFrom) {

            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                // anonyms.put(description.toString(), description.hashCode());
                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + "dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.AnonymousClassNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());
                // Tworzenie somenode
                Node allValNode = graph.addNode();

                // wstaw SomeNode i polacz

                // someVal.put(((OWLObjectSomeValuesFrom)
                // description).getProperty().toString(),
                // ((OWLObjectSomeValuesFrom)
                // description).getProperty().hashCode());
                // System.out.println("SomeVal NODE DLA " +
                // ((OWLObjectAllValuesFrom) description).getProperty());
                org.pg.eti.kask.sova.nodes.Node sNode = new org.pg.eti.kask.sova.nodes.AllValuesFromPropertyNode();
                OWLObjectPropertyExpression prop = ((OWLObjectAllValuesFrom) description).getProperty();
                if (prop.isAnonymous()) {
                    //TODO: write it in a better form!
                    try {
                        String[] split = prop.toString().split("[(#>]");
                        sNode.setLabel(split[0] + " ( " + split[2] + " " + split[3]);
                    } catch (Exception e) {
                        sNode.setLabel(prop.toString());
                    }
                } else {
                    sNode.setLabel(prop.asOWLObjectProperty().getIRI().getFragment());
                }
                //tu zmieniłem
                allValNode.set(COLUMN_NODE, sNode);

                int definitionID = 0;
                if (((OWLObjectAllValuesFrom) description).getProperty() instanceof OWLObjectProperty) {

                    definitionID = properties.get(((OWLObjectAllValuesFrom) description).getProperty().toString());
                } else {

                    //recusively chcek what it is
                    definitionID = DescriptionHandler(((OWLObjectAllValuesFrom) description).getFiller());
                }
                int someRow = edges.addRow();
                edges.set(someRow, "source", definitionID);
                edges.set(someRow, "target", allValNode.getRow());
                edges.set(someRow, "edge", new org.pg.eti.kask.sova.edges.InstancePropertyEdge());


                // System.out.println("EDGE MIEDZY ANONYM I ALLVAL " +
                // allValNode.getRow() +" " +anonymNode.getRow() );
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", allValNode.getRow());
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.PropertyEdge());

                // tworzenie wynikowego node
                int targetId = 0;
                if (((OWLObjectAllValuesFrom) description).getFiller() instanceof OWLClass) {
                    // pobierz drugie node z z klas
                    // System.out.println("ALl val from klass "
                    // +((OWLObjectAllValuesFrom) description).getFiller() /* +
                    // " " + classes.get(((OWLObjectSomeValuesFrom)
                    // description).getFiller().toString() ) */ );
                    targetId = classes.get(((OWLClass) (((OWLObjectAllValuesFrom) description).getFiller())).getIRI().getFragment());
                } else {
                    // to cos bardziej skomplikowanego niz klasa lzu individual
                    // System.out.println("More complicated");
//                    targetId = DescriptionHandler(((OWLObjectAllValuesFrom) description).getFiller());
                }
                // polacz edgami i ogolnie upenij sie za bangla
                if (targetId != -1) {
                    int row2 = edges.addRow();
                    edges.set(row2, "source", allValNode.getRow());
                    edges.set(row2, "target", targetId);
                    edges.set(row2, "edge", new org.pg.eti.kask.sova.edges.PropertyEdge());

                    return anonymNode.getRow();
                }
            }

        } else if (description instanceof OWLObjectHasValue) {

            // System.out.println("HAS VALUE" +description );
            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + "dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.AnonymousClassNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());
                // Tworzenie somenode
                Node allValNode = graph.addNode();

                if (((OWLObjectHasValue) description).getProperty() instanceof OWLObjectProperty) {
                    // wstaw has val Node i polacz

                    // someVal.put(((OWLObjectSomeValuesFrom)
                    // description).getProperty().toString(),
                    // ((OWLObjectSomeValuesFrom)
                    // description).getProperty().hashCode());
                    // System.out.println("has Val NODE DLA " +
                    // ((OWLObjectHasValue ) description).getProperty());
                    org.pg.eti.kask.sova.nodes.Node sNode = new org.pg.eti.kask.sova.nodes.AllValuesFromPropertyNode();
                    sNode.setLabel(((OWLObjectHasValue) description).getProperty().asOWLObjectProperty().getIRI().getFragment());
                    //tutaj zmieniłem
                    allValNode.set(COLUMN_NODE, sNode);

                    int definitionID = properties.get(((OWLObjectHasValue) description).getProperty().asOWLObjectProperty().getIRI().getFragment());
                    //tu zmieniłem
                    int someRow = edges.addRow();
                    edges.set(someRow, "source", definitionID);
                    edges.set(someRow, "target", allValNode.getRow());
                    edges.set(someRow, "edge", new org.pg.eti.kask.sova.edges.InstancePropertyEdge());

                } else {
                    // wywolaj rekurencyjnie i poznaj czym jest property
                    // probably will never enter here;
                    throw new Exception("HAS values  somethign wired "
                            + ((OWLObjectHasValue) description).getProperty());

                }
                // System.out.println("EDGE MIEDZY ANONYM I ALLVAL " +
                // allValNode.getRow() +" " +anonymNode.getRow() );
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", allValNode.getRow());
                edges.set(row, "edge",
                        new org.pg.eti.kask.sova.edges.PropertyEdge());

                int row2 = edges.addRow();
                edges.set(row2, "source", allValNode.getRow());
                edges.set(row2, "target", individuals.get(((OWLObjectHasValue) description).getValue().asOWLNamedIndividual().getIRI().getFragment()));
                edges.set(row2, "edge",
                        new org.pg.eti.kask.sova.edges.PropertyEdge());
                // System.out.println("has Val INDIVIDUAL " +
                // ((OWLObjectHasValue ) description).getValue());
                return anonymNode.getRow();

            }
        } else if (description instanceof OWLObjectIntersectionOf) {

            // System.out.println("INTERSECTION " + ((OWLObjectIntersectionOf)
            // description).toString());
            // wstaw anonym dla tej intersekcji
            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.IntersectionOfNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());
                // podczep do niego skladowe
                for (OWLClassExpression d : ((OWLObjectIntersectionOf) description).getOperands()) {
                    if (d instanceof OWLClass) {
                        // System.out.println("Skaldowa klasa intersekcji " +
                        // d.toString() );
                        int clsNodeID = classes.get(((OWLClass) d).getIRI().getFragment());
                        int row = edges.addRow();
                        edges.set(row, "source", clsNodeID);
                        edges.set(row, "target", anonymNode.getRow());
                        edges.set(row, "edge", new org.pg.eti.kask.sova.edges.OperationEdge());

                    } else {
                        // System.out.println("Skompikowa skladow INtersekcji" +
                        // d.toString());
                        int nodeID = DescriptionHandler(d);
                        int row = edges.addRow();
                        edges.set(row, "source", nodeID);
                        edges.set(row, "target", anonymNode.getRow());
                        edges.set(row, "edge", new org.pg.eti.kask.sova.edges.OperationEdge());
                    }

                }

                return anonymNode.getRow();
            }
        } else if (description instanceof OWLObjectUnionOf) {

            // System.out.println("UNIA " + ((OWLObjectUnionOf)
            // description).toString());

            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                // wstaw anonym dla tej unii
                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.UnionOfNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());
                // podczep do niego skladowe
                for (OWLClassExpression d : ((OWLObjectUnionOf) description).getOperands()) {
                    if (d instanceof OWLClass) {
                        // System.out.println("Klasa w UNII" + d.toString());
                        int clsNodeID = classes.get(((OWLClass) d).getIRI().getFragment());
                        int row = edges.addRow();
                        edges.set(row, "source", clsNodeID);
                        edges.set(row, "target", anonymNode.getRow());
                        edges.set(row, "edge",
                                new org.pg.eti.kask.sova.edges.OperationEdge());
                    } else {
                        // System.out.println("Skompikowa skladow UNII" +
                        // d.toString());
                        int nodeID = DescriptionHandler(d);
                        int row = edges.addRow();
                        edges.set(row, "source", nodeID);
                        edges.set(row, "target", anonymNode.getRow());
                        edges.set(row, "edge", new org.pg.eti.kask.sova.edges.OperationEdge());
                    }

                }
                return anonymNode.getRow();

            }
        } else if (description instanceof OWLObjectComplementOf) {
            // System.out.println("KOMPLEMENTARNE " + ((OWLObjectComplementOf)
            // description).toString());

            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {

                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.ComplementOfNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());

                // System.out.println("OPERAND " +
                // ((OWLObjectComplementOf)description ).getOperand());
                int complID = 0;

                if (((OWLObjectComplementOf) description).getOperand() instanceof OWLClass) {
                    complID = classes.get(((OWLObjectComplementOf) description).getOperand().asOWLClass().getIRI().getFragment());
                } else {
                    complID = DescriptionHandler(((OWLObjectComplementOf) description).getOperand());
                }

                int row = edges.addRow();
                edges.set(row, "source", complID);
                edges.set(row, "target", anonymNode.getRow());
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.OperationEdge());

                return anonymNode.getRow();

            }

        } else if (description instanceof OWLObjectOneOf) {
            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {

                // System.out.println("ANONYM NODE DLA " +
                // description.toString() + " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.OneOfNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());
                for (OWLIndividual ind : ((OWLObjectOneOf) description).getIndividuals()) {
                    int id = individuals.get(ind.asOWLNamedIndividual().getIRI().getFragment());
                    int row = edges.addRow();
                    edges.set(row, "source", id);
                    edges.set(row, "target", anonymNode.getRow());
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.OperationEdge());

                }

                return anonymNode.getRow();
            }
        } else if (description instanceof OWLObjectMinCardinality) {
            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.CardinalityNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());

                int propertyLink = 0;
                if (((OWLObjectMinCardinality) description).getProperty() instanceof OWLProperty) {
                    propertyLink = properties.get(((OWLObjectMinCardinality) description).getProperty().toString());
                } else {
                    throw new Exception("Cardinality went wrong");
                }

                int classLink = 0;
                if (((OWLObjectMinCardinality) description).getFiller() instanceof OWLClass) {
                    classLink = classes.get(((OWLObjectMinCardinality) description).getFiller().asOWLClass().getIRI().getFragment());
                } else {
                    classLink = DescriptionHandler(((OWLObjectMinCardinality) description).getFiller());
                }

                Node numberNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node2 = new org.pg.eti.kask.sova.nodes.MinCardinalityValueNode();
                node2.setLabel(Integer.toString(((OWLObjectMinCardinality) description).getCardinality()));

                numberNode.set(COLUMN_NODE, node2);
                anonyms.put(description.toString(), anonymNode.getRow());

                int row = edges.addRow();
                edges.set(row, "source", classLink);
                edges.set(row, "target", anonymNode.getRow());
                edges.set(row, "edge",
                        new org.pg.eti.kask.sova.edges.EquivalentEdge());

                int row2 = edges.addRow();
                edges.set(row2, "source", propertyLink);
                edges.set(row2, "target", anonymNode.getRow());
                edges.set(row2, "edge", new org.pg.eti.kask.sova.edges.Edge());

                int row3 = edges.addRow();
                edges.set(row3, "source", numberNode.getRow());
                edges.set(row3, "target", anonymNode.getRow());
                edges.set(row3, "edge", new org.pg.eti.kask.sova.edges.Edge());

                return anonymNode.getRow();
            }
        } else if (description instanceof OWLObjectMaxCardinality) {
            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.CardinalityNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());

                int propertyLink = 0;
                if (((OWLObjectMaxCardinality) description).getProperty() instanceof OWLProperty) {
                    propertyLink = properties.get(((OWLObjectMaxCardinality) description).getProperty().toString());
                } else {
                    throw new Exception("Cardinality went wrong");
                }

                int classLink = 0;
                if (((OWLObjectMaxCardinality) description).getFiller() instanceof OWLClass) {
                    classLink = classes.get(((OWLObjectMaxCardinality) description).getFiller().asOWLClass().getIRI().getFragment());
                } else {
                    classLink = DescriptionHandler(((OWLObjectMaxCardinality) description).getFiller());
                }

                Node numberNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node2 = new org.pg.eti.kask.sova.nodes.MaxCardinalityValueNode();
                node2.setLabel(Integer.toString(((OWLObjectMaxCardinality) description).getCardinality()));

                numberNode.set(COLUMN_NODE, node2);
                anonyms.put(description.toString(), anonymNode.getRow());

                int row = edges.addRow();
                edges.set(row, "source", classLink);
                edges.set(row, "target", anonymNode.getRow());
                edges.set(row, "edge",
                        new org.pg.eti.kask.sova.edges.EquivalentEdge());

                int row2 = edges.addRow();
                edges.set(row2, "source", propertyLink);
                edges.set(row2, "target", anonymNode.getRow());
                edges.set(row2, "edge", new org.pg.eti.kask.sova.edges.Edge());

                int row3 = edges.addRow();
                edges.set(row3, "source", numberNode.getRow());
                edges.set(row3, "target", anonymNode.getRow());
                edges.set(row3, "edge", new org.pg.eti.kask.sova.edges.Edge());

                return anonymNode.getRow();
            }
        } else if (description instanceof OWLObjectExactCardinality) {
            if (anonyms.containsKey(description.toString())) {
                // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
                // description.toString() );
                return anonyms.get(description.toString());

            } else {
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.CardinalityNode();
                anonymNode.set(COLUMN_NODE, node);
                anonyms.put(description.toString(), anonymNode.getRow());

                int propertyLink = 0;
                if (((OWLObjectExactCardinality) description).getProperty() instanceof OWLProperty) {
                    propertyLink = properties.get(((OWLObjectExactCardinality) description).getProperty().toString());
                } else {
                    throw new Exception("Cardinality went wrong");
                }

                int classLink = 0;
                if (((OWLObjectExactCardinality) description).getFiller() instanceof OWLClass) {
                    classLink = classes.get(((OWLObjectExactCardinality) description).getFiller().asOWLClass().getIRI().getFragment());
                } else {
                    classLink = DescriptionHandler(((OWLObjectExactCardinality) description).getFiller());
                }

                Node numberNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node2 = new org.pg.eti.kask.sova.nodes.CardinalityValueNode();
                node2.setLabel(Integer.toString(((OWLObjectExactCardinality) description).getCardinality()));

                numberNode.set(COLUMN_NODE, node2);
                anonyms.put(description.toString(), anonymNode.getRow());

                int row = edges.addRow();
                edges.set(row, "source", classLink);
                edges.set(row, "target", anonymNode.getRow());
                edges.set(row, "edge",
                        new org.pg.eti.kask.sova.edges.EquivalentEdge());

                int row2 = edges.addRow();
                edges.set(row2, "source", propertyLink);
                edges.set(row2, "target", anonymNode.getRow());
                edges.set(row2, "edge", new org.pg.eti.kask.sova.edges.Edge());

                int row3 = edges.addRow();
                edges.set(row3, "source", numberNode.getRow());
                edges.set(row3, "target", anonymNode.getRow());
                edges.set(row3, "edge", new org.pg.eti.kask.sova.edges.Edge());

                return anonymNode.getRow();
            }
        } else {
            System.out.println("DESCUNHANLED: " + description);
        }

        return defaultNumber;
    }

    private void initializeGraphColumns() {
        graph.addColumn(COLUMN_NODE, org.pg.eti.kask.sova.nodes.Node.class);
        graph.addColumn(COLUMN_IRI, IRI.class);
        graph.addColumn(COLUMN_NAME_NODE, String.class);
        edges.addColumn("edge", org.pg.eti.kask.sova.edges.Edge.class);
    }

    private int insertThingClass(OWLOntology ontology, Graph graph) {
        boolean isThing = false;
        for (OWLClass cls : ontology.getClassesInSignature()) {
            if (cls.isOWLThing()) {
                // Dodajemy węzeł Thing z ontologi
                Node thing = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node t = new org.pg.eti.kask.sova.nodes.ThingNode();
                thing.set(COLUMN_NODE, t);
                thing.set(COLUMN_IRI, cls.getIRI());
                thing.set(COLUMN_NAME_NODE, cls.toString());
                thingNumber = thing.getRow();
                classes.put(cls.getIRI().getFragment(), thingNumber);
                isThing = true;
            }
        }
        if (!isThing) {
            // Dodajemy węzeł Thing dla spójności
            Node thing = graph.addNode();
            org.pg.eti.kask.sova.nodes.Node t = new org.pg.eti.kask.sova.nodes.ThingNode();
            thing.set(COLUMN_NODE, t);
            thing.set(COLUMN_NAME_NODE, "thing");
            thing.set(COLUMN_IRI, IRI.create("http://www.w3.org/2002/07/owl#Thing"));
            thingNumber = thing.getRow();
            classes.put("http://www.w3.org/2002/07/owl#Thing", thingNumber);
        }
        return thingNumber;
    }

    /**
     * Metoda zamiany obiektu OWLOntology na obiekt graph biblioteki prefuse.
     * Metoda wywoluje podrzedne metody wpisujace do grafu wezly i krawedzie
     *
     * @param ontology
     * @return Graph - graf prefuse
     */
    public Graph OWLtoGraph(OWLOntology ontology) throws Exception {

        initializeGraphColumns();


        defaultNumber = this.insertThingClass(ontology, graph);
        this.insertBaseClasses(ontology, graph);
        this.insertBaseProperties(ontology, graph);
        this.insertBaseIndividuals(ontology, graph);
        this.insertDataType(ontology, graph);
        /*
         * Node n = graph.addNode(); org.eti.kask.sova.nodes.Node node = new
         * org.eti.kask.sova.nodes.ClassNode(); node.setLabel(cls.toString());
         * n.set(COLUMN_NODE, node);
         *
         * if( cls.getSuperClasses(ontology).isEmpty() == true ){ //thing jest
         * superklasa int row = edges.addRow(); edges.set(row, "source",
         * thing.getRow()); edges.set(row, "target", n.getRow()); edges.set(row,
         * "edge", new org.eti.kask.sova.edges.SubEdge() ); }
         */
        for (OWLAxiom axiom : ontology.getAxioms()) {

            if (axiom instanceof OWLSubClassOfAxiom) {
                // System.out.println("OWLCLASSAXIOM : " + axiom.toString());
                int subClassID = -1;
                int superClassID = -1;
                // budowanie edge
                // uchwyty miedzy edgem to ((OWLSubClassOfAxiom)
                // axiom).getSubClass() i(OWLSubClassOfAxiom)
                // axiom).getSuperClass()
                if (((OWLSubClassOfAxiom) axiom).getSubClass() instanceof OWLClass) {
                    // System.out.println("Subklasa:  " + ((OWLSubClassOfAxiom)
                    // axiom).getSubClass());
                    subClassID = classes.get(((OWLSubClassOfAxiom) axiom).getSubClass().asOWLClass().getIRI().getFragment());

                    // wez z hashtable uchwyt subklasy
                } else {

                    // pobierz z handlera uchwyt do wierzcholka
                    subClassID = DescriptionHandler(((OWLSubClassOfAxiom) axiom).getSubClass());
                }

                if (((OWLSubClassOfAxiom) axiom).getSuperClass() instanceof OWLClass /* &&!((OWLSubClassOfAxiom ) axiom).getSuperClass().isOWLThing()*/) {
                    // System.out.println("Superklasa: " + ((OWLSubClassOfAxiom)
                    // axiom).getSuperClass());
                    // wez z hashtable uchwyt taty
                    superClassID = classes.get(((OWLClass) ((OWLSubClassOfAxiom) axiom).getSuperClass()).getIRI().getFragment());
                } else {

                    superClassID = DescriptionHandler(((OWLSubClassOfAxiom) axiom).getSuperClass());
                }
                // dodaj edge
                if (superClassID >= 0 && subClassID >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", superClassID);
                    edges.set(row, "target", subClassID);
                    edges.set(row, "edge",
                            new org.pg.eti.kask.sova.edges.SubClassEdge());
                }

            } else if (axiom instanceof OWLDisjointClassesAxiom) {
                // wstaw krawedz disjoint

                // System.out.println("DISJOINT AXIOM: " +
                // ((OWLDisjointClassesAxiom) axiom).toString());
                int id1 = -1, id2 = -1, i = 0;
                for (OWLClassExpression d : ((OWLDisjointClassesAxiom) axiom).getClassExpressions()) {
                    if (d instanceof OWLClass) {
                        // normalnie
                        if (i == 0) {
                            id1 = classes.get(d.asOWLClass().getIRI().getFragment());
                        } else {
                            id2 = classes.get(d.asOWLClass().getIRI().getFragment());
                        }
                    } else {
                        if (i == 0) {
                            id1 = DescriptionHandler(d);
                        } else {
                            id2 = DescriptionHandler(d);
                        }
                    }
                    i++;
                }
                if (id1 >= 0 && id2 >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", id1);
                    edges.set(row, "target", id2);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.DisjointEdge());
                }

            } else if (axiom instanceof OWLEquivalentClassesAxiom) {

                // System.out.println("EQUIV AXIOM: " +
                // ((OWLEquivalentClassesAxiom) axiom).toString());
                int id1 = -1, id2 = -1, i = 0;
                for (OWLClassExpression d : ((OWLEquivalentClassesAxiom) axiom).getClassExpressions()) {
                    if (d instanceof OWLClass) {
                        // normalnie
                        if (i == 0) {
                            id1 = classes.get(((OWLClass) d).getIRI().getFragment());
                        } else {
                            id2 = classes.get(((OWLClass) d).getIRI().getFragment());
                        }
                        // System.out.println("EQUIV skladowa klasa : " +
                        // d.toString() );
                    } else {
                        // System.out.println("EQUIV skladowa skomplikowana : "
                        // + d.toString());
                        if (i == 0) {
                            id1 = DescriptionHandler(d);
                        } else {
                            id2 = DescriptionHandler(d);
                        }
                    }
                    i++;
                }
                if (id1 >= 0 && id2 >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", id1);
                    edges.set(row, "target", id2);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.EquivalentEdge());
                }

            } else if (axiom instanceof OWLObjectPropertyRangeAxiom) {

                int id1 = properties.get(((OWLObjectPropertyRangeAxiom) axiom).getProperty().toString());

                int id2 = -1;
                if (((OWLObjectPropertyRangeAxiom) axiom).getRange() instanceof OWLClass) {
                    id2 = classes.get(((OWLObjectPropertyRangeAxiom) axiom).getRange().asOWLClass().getIRI().getFragment());
                } else {
                    id2 = DescriptionHandler(((OWLObjectPropertyRangeAxiom) axiom).getRange());
                }

                if (id1 >= 0 && id2 >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", id1);
                    edges.set(row, "target", id2);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.RangeEdge());
                }
            } else if (axiom instanceof OWLObjectPropertyDomainAxiom) {
                int id1 = properties.get(((OWLObjectPropertyDomainAxiom) axiom).getProperty().toString());

                int id2 = -1;
                if (((OWLObjectPropertyDomainAxiom) axiom).getDomain() instanceof OWLClass) {
                    id2 = classes.get(((OWLObjectPropertyDomainAxiom) axiom).getDomain().asOWLClass().getIRI().getFragment());
                } else {
                    id2 = DescriptionHandler(((OWLObjectPropertyDomainAxiom) axiom).getDomain());
                }

                if (id1 >= 0 && id2 >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", id1);
                    edges.set(row, "target", id2);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.DomainEdge());
                }

            } else if (axiom instanceof OWLClassAssertionAxiom) {
                int id1 = -1;

                if (((OWLClassAssertionAxiom) axiom).getIndividual().isNamed()) {
                    id1 = individuals.get(((OWLClassAssertionAxiom) axiom).getIndividual().asOWLNamedIndividual().getIRI().getFragment());
                } else {
                    id1 = individuals.get(((OWLClassAssertionAxiom) axiom).getIndividual().asOWLAnonymousIndividual().getID().getID());
                }

                int id2 = -1;
                // if(!((OWLClassAssertionAxiom)axiom).getDescription().isOWLThing()){
                if (((OWLClassAssertionAxiom) axiom).getClassExpression() instanceof OWLClass) {
                    id2 = classes.get(((OWLClassAssertionAxiom) axiom).getClassExpression().asOWLClass().getIRI().getFragment());

                } else {
                    id2 = DescriptionHandler(((OWLClassAssertionAxiom) axiom).getClassExpression());
                }
                if (id1 >= 0 && id2 >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", id1);
                    edges.set(row, "target", id2);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.InstanceOfEdge());
                }
//			 }
//                            else {
//                                System.out.println(">>>>>>>>>>>>>>>>>" + ((OWLClassAssertionAxiom) axiom).getIndividual());
//                         }

            } else if (axiom instanceof OWLDifferentIndividualsAxiom) {

                // System.out.println("DIFFRENT NODE " + axiom.toString() +
                // " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.DifferentNode();
                anonymNode.set(COLUMN_NODE, node);

                for (OWLIndividual ind : ((OWLDifferentIndividualsAxiom) axiom).getIndividuals()) {
                    int id = individuals.get(ind.asOWLNamedIndividual().getIRI().getFragment());
                    int row = edges.addRow();
                    edges.set(row, "source", id);
                    edges.set(row, "target", anonymNode.getRow());
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());
                }

            } else if (axiom instanceof OWLSubPropertyAxiom) {
                // System.out.println("OWLPROPERTYAXIOM : " + axiom.toString());
                int subClassID = -1;
                int superClassID = -1;
                // budowanie edge
                // uchwyty miedzy edgem to ((OWLSubPropertyAxiom)
                // axiom).getSubClass() i(OWLSubPropertyAxiom)
                // axiom).getSuperClass()
                if (((OWLSubPropertyAxiom) axiom).getSubProperty() instanceof OWLProperty) {
                    // System.out.println("Subprop:  " + ((OWLSubPropertyAxiom)
                    // axiom).getSubProperty());
                    subClassID = properties.get(((OWLSubPropertyAxiom) axiom).getSubProperty().toString());
                    // wez z hashtable uchwyt subklasy
                }

                if (((OWLSubPropertyAxiom) axiom).getSuperProperty() instanceof OWLProperty) {
                    // System.out.println("Superprop: " + ((OWLSubPropertyAxiom)
                    // axiom).getSuperProperty());
                    // wez z hashtable uchwyt taty
                    superClassID = properties.get(((OWLSubPropertyAxiom) axiom).getSuperProperty().toString());
                }
                // dodaj edge
                if (superClassID >= 0 && subClassID >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", superClassID);
                    edges.set(row, "target", subClassID);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.SubPropertyEdge());
                }

            } else if (axiom instanceof OWLInverseObjectPropertiesAxiom) {
                int id1 = properties.get(((OWLInverseObjectPropertiesAxiom) axiom).getFirstProperty().toString());
                int id2 = properties.get(((OWLInverseObjectPropertiesAxiom) axiom).getSecondProperty().toString());
                int row = edges.addRow();
                edges.set(row, "source", id1);
                edges.set(row, "target", id2);
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.InverseOfEdge());

            } else if (axiom instanceof OWLEquivalentObjectPropertiesAxiom) {
                int id1 = -1, id2 = -1, i = 0;
                for (OWLPropertyExpression d : ((OWLEquivalentObjectPropertiesAxiom) axiom).getProperties()) {
                    // normalnie

                    if (i == 0) {
                        id1 = properties.get(d.toString());
                    } else {
                        id2 = properties.get(d.toString());
                    }
                    i++;
                }
                if (id1 >= 0 && id2 >= 0) {
                    int row = edges.addRow();
                    edges.set(row, "source", id1);
                    edges.set(row, "target", id2);
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.EquivalentPropertyEdge());
                }

            } else if (axiom instanceof OWLFunctionalObjectPropertyAxiom) {
                // System.out.println("FUNC OBJ PRO " + axiom.toString() +
                // " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.FunctionalPropertyNode();
                anonymNode.set(COLUMN_NODE, node);
                int id = properties.get(((OWLFunctionalObjectPropertyAxiom) axiom).getProperty().toString());
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", id);
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());

            } else if (axiom instanceof OWLInverseFunctionalObjectPropertyAxiom) {
                // System.out.println("FUNC INV OBJ PRO " + axiom.toString() +
                // " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.InverseFunctionalPropertyNode();
                anonymNode.set(COLUMN_NODE, node);
                int id = properties.get(((OWLInverseFunctionalObjectPropertyAxiom) axiom).getProperty().toString());
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", id);
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());
            } else if (axiom instanceof OWLSymmetricObjectPropertyAxiom) {
                // System.out.println("FUNC sym OBJ PRO " + axiom.toString() +
                // " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.SymmetricPropertyNode();
                anonymNode.set(COLUMN_NODE, node);
                int id = properties.get(((OWLSymmetricObjectPropertyAxiom) axiom).getProperty().toString());
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", id);
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());
            } else if (axiom instanceof OWLTransitiveObjectPropertyAxiom) {
                // System.out.println("FUNC sym OBJ PRO " + axiom.toString() +
                // " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.TransitivePropertyNode();
                anonymNode.set(COLUMN_NODE, node);
                int id = properties.get(((OWLTransitiveObjectPropertyAxiom) axiom).getProperty().toString());
                int row = edges.addRow();
                edges.set(row, "source", anonymNode.getRow());
                edges.set(row, "target", id);
                edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());
            } else if (axiom instanceof OWLSameIndividualAxiom) {

                // System.out.println("SAME NODE " + axiom.toString() +
                // " dodany" );
                Node anonymNode = graph.addNode();
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.SameAsNode();
                anonymNode.set(COLUMN_NODE, node);
                for (OWLIndividual ind : ((OWLSameIndividualAxiom) axiom).getIndividuals()) {
                    int id = individuals.get(ind.asOWLNamedIndividual().getIRI().getFragment());
                    int row = edges.addRow();
                    edges.set(row, "source", id);
                    edges.set(row, "target", anonymNode.getRow());
                    edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());

                }
            } else {
                if (axiom instanceof OWLDataPropertyAssertionAxiom) {
                    System.out.println("DATA PROPERTY: " + axiom.toString());
//					OWLDataProperty dp = (OWLDataProperty)axiom;
                    OWLDataPropertyAssertionAxiom a = (OWLDataPropertyAssertionAxiom) axiom;
                    Set<?> s = a.getProperty().getRanges(ontology);

                    //System.out.println(	a.getAxiomType().toString());
                }

                System.out.println("OMITTED: " + axiom);
            }

        }

        return graph;
    }
}
