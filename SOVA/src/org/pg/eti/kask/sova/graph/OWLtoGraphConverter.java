package org.pg.eti.kask.sova.graph;

import java.net.URI;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import prefuse.data.Graph;
import prefuse.data.Node;

import org.pg.eti.kask.sova.utils.Debug;
import org.semanticweb.owl.model.*;

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
	public static final String COLUMN_URI = "URI";
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
		anonyms.put("dummy", -100);
	}

//	public static OWLtoGraphConverter getInstance() {
//		return INSTANCE;
//	}
//	
	private void insertDataType(OWLOntology ontology, Graph graph) {
		for (OWLDataProperty prop: ontology.getReferencedDataProperties()){
			
			System.out.println("DATATYPE :  "+prop.toString());
			Set<OWLDataRange> s = prop.getRanges(ontology);
			Iterator<OWLDataRange> it = s.iterator();
			int dataPropertyRowNr = 0;
			if (!dataProperties.containsKey(prop.toString())){
				Node dataProperty = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.PropertyNode();
				node.setLabel(prop.toString());
				dataProperty.set("node", node);
				dataProperty.set(COLUMN_URI, prop.getURI());
				dataPropertyRowNr = dataProperty.getRow();
				dataProperties.put(prop.toString(),dataPropertyRowNr);
			}else{
				dataPropertyRowNr = dataProperties.get(prop.toString());
			}
			
			while(it.hasNext()){
				OWLDataRange r = it.next();
				if (r.isDataType()){
					int dataTypeRowNr = 0;
					if (!dataTypes.containsKey(r.toString())){
						Node dataType = graph.addNode();
						org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.DataTypeNode();
						node.setLabel(r.toString());
						dataType.set("node", node);
						dataTypeRowNr = dataType.getRow();
						dataTypes.put(r.toString(),dataTypeRowNr);
					}else{
						dataTypeRowNr = dataTypes.get(r.toString());
					}
					int row = edges.addRow();
					edges.set(row, "source", dataPropertyRowNr);
					edges.set(row, "target", dataTypeRowNr);
					edges.set(row, "edge",	new org.pg.eti.kask.sova.edges.RangeEdge());
					

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

		for (OWLClass cls : ontology.getReferencedClasses()) {
			// dodajemy na sucho wszystkie klasy bez krawedzi
			// oprocz Thing
			if (!cls.isOWLThing()) {
				Node n = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.ClassNode();
				node.setLabel(cls.toString());
				n.set(COLUMN_NODE, node);
				n.set(COLUMN_URI, cls.getURI());
				//n.set(COLUMN_NAME_NODE, cls.toString());
				if (cls.getSuperClasses(ontology).isEmpty() == true) { // thing
					// jest
					// superklasa
					int row = edges.addRow();
					edges.set(row, "source", thingNumber);
					edges.set(row, "target", n.getRow());
					edges.set(row, "edge",new org.pg.eti.kask.sova.edges.SubClassEdge());
				}
				classes.put(cls.getURI().toString(), n.getRow());
				
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
		for (OWLObjectProperty property : ontology
				.getReferencedObjectProperties()) {
			Node n = graph.addNode();
			org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.PropertyNode();
			node.setLabel(property.toString());
			n.set("node", node);
			n.set(COLUMN_URI, property.getURI());
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
		for (OWLIndividual individual : ontology.getReferencedIndividuals()) {
			Node n = graph.addNode();
			org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.IndividualNode();
			node.setLabel(individual.toString());
			n.set("node", node);
			n.set(COLUMN_URI, individual.getURI());
			individuals.put(individual.toString(), n.getRow());
		}
	}

	/**
	 * Metoda umieszcza w grafie krawędzi i/lub wierzchołki odpowiadające danemu
	 * obiektowi OWLDescription
	 * 
	 * @param description
	 * @return
	 * @throws Exception
	 */
	private int DescriptionHandler(OWLDescription description) throws Exception {
		// ObjectSomeValueFrom
		if (description instanceof OWLObjectSomeRestriction) {
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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());
				// Tworzenie somenode
				Node someValNode = graph.addNode();
				if (((OWLObjectSomeRestriction) description).getProperty() instanceof OWLObjectProperty) {
					// wstaw SomeNode i polacz

					// someVal.put(((OWLObjectSomeRestriction)
					// description).getProperty().toString(),
					// ((OWLObjectSomeRestriction)
					// description).getProperty().hashCode());
					// System.out.println("SomeVal NODE DLA " +
					// ((OWLObjectSomeRestriction) description).getProperty());
					org.pg.eti.kask.sova.nodes.Node sNode = new org.pg.eti.kask.sova.nodes.SomeValuesFromPropertyNode();
					sNode.setLabel(((OWLObjectSomeRestriction) description)
							.getProperty().toString());
					someValNode.set("node", sNode);

					// dodanie krawedzi laczacej node SomeValuesFrom z definicja
					// property
					int definitionID = properties.get(((OWLObjectSomeRestriction) description)
									.getProperty().toString());
					int someRow = edges.addRow();
					edges.set(someRow, "source", definitionID);
					edges.set(someRow, "target", someValNode.getRow());
					edges.set(someRow, "edge",new org.pg.eti.kask.sova.edges.InstancePropertyEdge());
				} else {
					// wywolaj rekurencyjnie i poznaj czym jest property
					// probably will never enter here;
					throw new Exception("Some values from somethign wired "
							+ ((OWLObjectSomeRestriction) description)
									.getProperty());

				}
				// System.out.println("EDGE MIEDZY ANONYM I SOMEVAL " +
				// someValNode.getRow() +" " +anonymNode.getRow() );
				int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", someValNode.getRow());
				edges.set(row, "edge",new org.pg.eti.kask.sova.edges.PropertyEdge());

				// tworzenie wynikowego node
				int targetId = 0;
				if (((OWLObjectSomeRestriction) description).getFiller() instanceof OWLClass) {
					// pobierz drugie node z z klas
					// System.out.println("Some val from klass "
					// +((OWLObjectSomeRestriction) description).getFiller() /*
					// + " " + classes.get(((OWLObjectSomeRestriction)
					// description).getFiller().toString() ) */ );
					targetId = classes.get(((OWLClass) (((OWLObjectSomeRestriction) description)
									.getFiller())).getURI().toString());
				} else {
					// to cos bardziej skomplikowanego niz klasa lzu individual
					// System.out.println("More complicated");
					targetId = DescriptionHandler(((OWLObjectSomeRestriction) description)
							.getFiller());
				}
				// polacz edgami i ogolnie upenij sie za bangla
				int row2 = edges.addRow();
				edges.set(row2, "source", someValNode.getRow());
				edges.set(row2, "target", targetId);
				edges.set(row2, "edge",new org.pg.eti.kask.sova.edges.PropertyEdge());

				return anonymNode.getRow();
			}

		} else if (description instanceof OWLObjectAllRestriction) {

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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());
				// Tworzenie somenode
				Node allValNode = graph.addNode();
				if (((OWLObjectAllRestriction) description).getProperty() instanceof OWLObjectProperty) {
					// wstaw SomeNode i polacz

					// someVal.put(((OWLObjectSomeRestriction)
					// description).getProperty().toString(),
					// ((OWLObjectSomeRestriction)
					// description).getProperty().hashCode());
					// System.out.println("SomeVal NODE DLA " +
					// ((OWLObjectAllRestriction) description).getProperty());
					org.pg.eti.kask.sova.nodes.Node sNode = new org.pg.eti.kask.sova.nodes.AllValuesFromPropertyNode();
					sNode.setLabel(((OWLObjectAllRestriction) description)
							.getProperty().toString());
					allValNode.set("node", sNode);

					int definitionID = properties
							.get(((OWLObjectAllRestriction) description)
									.getProperty().toString());
					int someRow = edges.addRow();
					edges.set(someRow, "source", definitionID);
					edges.set(someRow, "target", allValNode.getRow());
					edges.set(someRow, "edge",new org.pg.eti.kask.sova.edges.InstancePropertyEdge());

				} else {
					// wywolaj rekurencyjnie i poznaj czym jest property
					// probably will never enter here;
					throw new Exception("All values from somethign wired "
							+ ((OWLObjectAllRestriction) description).getProperty());

				}
				// System.out.println("EDGE MIEDZY ANONYM I ALLVAL " +
				// allValNode.getRow() +" " +anonymNode.getRow() );
				int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", allValNode.getRow());
				edges.set(row, "edge",new org.pg.eti.kask.sova.edges.PropertyEdge());

				// tworzenie wynikowego node
				int targetId = 0;
				if (((OWLObjectAllRestriction) description).getFiller() instanceof OWLClass) {
					// pobierz drugie node z z klas
					// System.out.println("ALl val from klass "
					// +((OWLObjectAllRestriction) description).getFiller() /* +
					// " " + classes.get(((OWLObjectSomeRestriction)
					// description).getFiller().toString() ) */ );
					targetId = classes.get(((OWLClass) (((OWLObjectAllRestriction) description)
									.getFiller())).getURI().toString());
				} else {
					// to cos bardziej skomplikowanego niz klasa lzu individual
					// System.out.println("More complicated");
					targetId = DescriptionHandler(((OWLObjectAllRestriction) description).getFiller());
				}
				// polacz edgami i ogolnie upenij sie za bangla
				if (targetId != -1) {
					int row2 = edges.addRow();
					edges.set(row2, "source", allValNode.getRow());
					edges.set(row2, "target", targetId);
					edges.set(row2, "edge",new org.pg.eti.kask.sova.edges.PropertyEdge());

					return anonymNode.getRow();
				}
			}

		} else if (description instanceof OWLObjectValueRestriction) {

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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());
				// Tworzenie somenode
				Node allValNode = graph.addNode();

				if (((OWLObjectValueRestriction) description).getProperty() instanceof OWLObjectProperty) {
					// wstaw has val Node i polacz

					// someVal.put(((OWLObjectSomeRestriction)
					// description).getProperty().toString(),
					// ((OWLObjectSomeRestriction)
					// description).getProperty().hashCode());
					// System.out.println("has Val NODE DLA " +
					// ((OWLObjectValueRestriction) description).getProperty());
					org.pg.eti.kask.sova.nodes.Node sNode = new org.pg.eti.kask.sova.nodes.AllValuesFromPropertyNode();
					sNode.setLabel(((OWLObjectValueRestriction) description).getProperty().toString());
					allValNode.set("node", sNode);

					int definitionID = properties.get(((OWLObjectValueRestriction) description)
									.getProperty().toString());
					int someRow = edges.addRow();
					edges.set(someRow, "source", definitionID);
					edges.set(someRow, "target", allValNode.getRow());
					edges.set(someRow, "edge",new org.pg.eti.kask.sova.edges.InstancePropertyEdge());

				} else {
					// wywolaj rekurencyjnie i poznaj czym jest property
					// probably will never enter here;
					throw new Exception("HAS values  somethign wired "
							+ ((OWLObjectValueRestriction) description).getProperty());

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
				edges.set(row2, "target", individuals
						.get(((OWLObjectValueRestriction) description)
								.getValue().toString()));
				edges.set(row2, "edge",
						new org.pg.eti.kask.sova.edges.PropertyEdge());
				// System.out.println("has Val INDIVIDUAL " +
				// ((OWLObjectValueRestriction) description).getValue());
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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());
				// podczep do niego skladowe
				for (OWLDescription d : ((OWLObjectIntersectionOf) description)
						.getOperands()) {
					if (d instanceof OWLClass) {
						// System.out.println("Skaldowa klasa intersekcji " +
						// d.toString() );
						int clsNodeID = classes.get(((OWLClass) d).getURI().toString());
						int row = edges.addRow();
						edges.set(row, "source", clsNodeID);
						edges.set(row, "target", anonymNode.getRow());
						edges.set(row, "edge",new org.pg.eti.kask.sova.edges.OperationEdge());

					} else {
						// System.out.println("Skompikowa skladow INtersekcji" +
						// d.toString());
						int nodeID = DescriptionHandler(d);
						int row = edges.addRow();
						edges.set(row, "source", nodeID);
						edges.set(row, "target", anonymNode.getRow());
						edges.set(row, "edge",new org.pg.eti.kask.sova.edges.OperationEdge());
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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());
				// podczep do niego skladowe
				for (OWLDescription d : ((OWLObjectUnionOf) description)
						.getOperands()) {
					if (d instanceof OWLClass) {
						// System.out.println("Klasa w UNII" + d.toString());
						int clsNodeID = classes.get(((OWLClass) d).getURI().toString());
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
						edges.set(row, "edge",new org.pg.eti.kask.sova.edges.OperationEdge());
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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());

				// System.out.println("OPERAND " +
				// ((OWLObjectComplementOf)description ).getOperand());
				int complID = 0;

				if (((OWLObjectComplementOf) description).getOperand() instanceof OWLClass) {
					complID = classes.get(((OWLObjectComplementOf) description)
							.getOperand().asOWLClass().getURI().toString());
				} else {
					complID = DescriptionHandler(((OWLObjectComplementOf) description)
							.getOperand());
				}

				int row = edges.addRow();
				edges.set(row, "source", complID);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge",new org.pg.eti.kask.sova.edges.OperationEdge());

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
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());
				for (OWLIndividual ind : ((OWLObjectOneOf) description)
						.getIndividuals()) {
					int id = individuals.get(ind.toString());
					int row = edges.addRow();
					edges.set(row, "source", id);
					edges.set(row, "target", anonymNode.getRow());
					edges.set(row, "edge",	new org.pg.eti.kask.sova.edges.OperationEdge());

				}

				return anonymNode.getRow();
			}
		} else if (description instanceof OWLObjectMinCardinalityRestriction) {
			if (anonyms.containsKey(description.toString())) {
				// System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
				// description.toString() );
				return anonyms.get(description.toString());

			} else {
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.CardinalityNode();
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());

				int propertyLink = 0;
				if (((OWLObjectMinCardinalityRestriction) description)
						.getProperty() instanceof OWLProperty) {
					propertyLink = properties.get(((OWLObjectMinCardinalityRestriction) description)
									.getProperty().toString());
				} else {
					throw new Exception("Cardinality went wrong");
				}

				int classLink = 0;
				if (((OWLObjectMinCardinalityRestriction) description)
						.getFiller() instanceof OWLClass) {
					classLink = classes.get(((OWLObjectMinCardinalityRestriction) description)
									.getFiller().asOWLClass().getURI().toString());
				} else {
					classLink = DescriptionHandler(((OWLObjectMinCardinalityRestriction) description)
							.getFiller());
				}

				Node numberNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node2 = new org.pg.eti.kask.sova.nodes.MinCardinalityValueNode();
				node2.setLabel(Integer.toString(((OWLObjectMinCardinalityRestriction) description)
										.getCardinality()));

				numberNode.set("node", node2);
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
		} else if (description instanceof OWLObjectMaxCardinalityRestriction) {
			if (anonyms.containsKey(description.toString())) {
				// System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
				// description.toString() );
				return anonyms.get(description.toString());

			} else {
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.CardinalityNode();
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());

				int propertyLink = 0;
				if (((OWLObjectMaxCardinalityRestriction) description)
						.getProperty() instanceof OWLProperty) {
					propertyLink = properties.get(((OWLObjectMaxCardinalityRestriction) description)
									.getProperty().toString());
				} else {
					throw new Exception("Cardinality went wrong");
				}

				int classLink = 0;
				if (((OWLObjectMaxCardinalityRestriction) description)
						.getFiller() instanceof OWLClass) {
					classLink = classes.get(((OWLObjectMaxCardinalityRestriction) description)
									.getFiller().asOWLClass().getURI().toString());
				} else {
					classLink = DescriptionHandler(((OWLObjectMaxCardinalityRestriction) description)
							.getFiller());
				}

				Node numberNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node2 = new org.pg.eti.kask.sova.nodes.MaxCardinalityValueNode();
				node2.setLabel(Integer.toString(((OWLObjectMaxCardinalityRestriction) description)
										.getCardinality()));

				numberNode.set("node", node2);
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
		} else if (description instanceof OWLObjectExactCardinalityRestriction) {
			if (anonyms.containsKey(description.toString())) {
				// System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" +
				// description.toString() );
				return anonyms.get(description.toString());

			} else {
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.CardinalityNode();
				anonymNode.set("node", node);
				anonyms.put(description.toString(), anonymNode.getRow());

				int propertyLink = 0;
				if (((OWLObjectExactCardinalityRestriction) description)
						.getProperty() instanceof OWLProperty) {
					propertyLink = properties.get(((OWLObjectExactCardinalityRestriction) description)
									.getProperty().toString());
				} else {
					throw new Exception("Cardinality went wrong");
				}

				int classLink = 0;
				if (((OWLObjectExactCardinalityRestriction) description)
						.getFiller() instanceof OWLClass) {
					classLink = classes.get(((OWLObjectExactCardinalityRestriction) description).getFiller().asOWLClass().getURI().toString());
				} else {
					classLink = DescriptionHandler(((OWLObjectExactCardinalityRestriction) description).getFiller());
				}

				Node numberNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node2 = new org.pg.eti.kask.sova.nodes.CardinalityValueNode();
				node2.setLabel(Integer.toString(((OWLObjectExactCardinalityRestriction) description).getCardinality()));

				numberNode.set("node", node2);
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
	private void initializeGraphColumns(){
		graph.addColumn("node", org.pg.eti.kask.sova.nodes.Node.class);
		graph.addColumn(COLUMN_URI, URI.class);
	}
	
	private int insertThingClass(OWLOntology ontology, Graph graph){
		boolean isThing = false;
		for(OWLClass cls : ontology.getReferencedClasses()){
			if(cls.isOWLThing()){
				// Dodajemy węzeł Thing z ontologi 
				Node thing = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node t = new org.pg.eti.kask.sova.nodes.ThingNode();
				thing.set(COLUMN_NODE, t);
				thing.set(COLUMN_URI, cls.getURI());
				thingNumber = thing.getRow();
				classes.put(cls.getURI().toString(), thingNumber);	
				isThing = true;
			}
		}
		if (!isThing){
			// Dodajemy węzeł Thing dla spójności 
			Node thing = graph.addNode();
			org.pg.eti.kask.sova.nodes.Node t = new org.pg.eti.kask.sova.nodes.ThingNode();
			thing.set(COLUMN_NODE, t);
			
			thing.set(COLUMN_URI, "http://www.w3.org/2002/07/owl#Thing");
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
		edges.addColumn("edge", org.pg.eti.kask.sova.edges.Edge.class);

		defaultNumber = this.insertThingClass(ontology, graph);
		this.insertBaseClasses(ontology, graph);
		this.insertBaseProperties(ontology, graph);
		this.insertBaseIndividuals(ontology, graph);
		this.insertDataType(ontology, graph);
		/*
		 * Node n = graph.addNode(); org.eti.kask.sova.nodes.Node node = new
		 * org.eti.kask.sova.nodes.ClassNode(); node.setLabel(cls.toString());
		 * n.set("node", node);
		 * 
		 * if( cls.getSuperClasses(ontology).isEmpty() == true ){ //thing jest
		 * superklasa int row = edges.addRow(); edges.set(row, "source",
		 * thing.getRow()); edges.set(row, "target", n.getRow()); edges.set(row,
		 * "edge", new org.eti.kask.sova.edges.SubEdge() ); }
		 */
		for (OWLAxiom axiom : ontology.getAxioms()) {
		
			if (axiom instanceof OWLSubClassAxiom) {
				// System.out.println("OWLCLASSAXIOM : " + axiom.toString());
				int subClassID = -1;
				int superClassID = -1;
				// budowanie edge
				// uchwyty miedzy edgem to ((OWLSubClassAxiom)
				// axiom).getSubClass() i(OWLSubClassAxiom)
				// axiom).getSuperClass()
				if (((OWLSubClassAxiom) axiom).getSubClass() instanceof OWLClass) {
					// System.out.println("Subklasa:  " + ((OWLSubClassAxiom)
					// axiom).getSubClass());
					subClassID = classes.get(((OWLSubClassAxiom) axiom).getSubClass().asOWLClass().getURI().toString());

					// wez z hashtable uchwyt subklasy
				} else {

					// pobierz z handlera uchwyt do wierzcholka
					subClassID = DescriptionHandler(((OWLSubClassAxiom) axiom).getSubClass());
				}

				if (((OWLSubClassAxiom) axiom).getSuperClass() instanceof OWLClass 
					/* &&!((OWLSubClassAxiom ) axiom).getSuperClass().isOWLThing()*/) {
					// System.out.println("Superklasa: " + ((OWLSubClassAxiom)
					// axiom).getSuperClass());
					// wez z hashtable uchwyt taty
					superClassID = classes.get(((OWLClass)((OWLSubClassAxiom) axiom)
							.getSuperClass()).getURI().toString());
				} else {

					superClassID = DescriptionHandler(((OWLSubClassAxiom) axiom)
							.getSuperClass());
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
				for (OWLDescription d : ((OWLDisjointClassesAxiom) axiom)
						.getDescriptions()) {
					if (d instanceof OWLClass) {
						// normalnie
						if (i == 0) {
							id1 = classes.get(d.asOWLClass().getURI().toString());
						} else {
							id2 = classes.get(d.asOWLClass().getURI().toString());
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
					edges.set(row, "edge",new org.pg.eti.kask.sova.edges.DisjointEdge());
				}

			} else if (axiom instanceof OWLEquivalentClassesAxiom) {

				// System.out.println("EQUIV AXIOM: " +
				// ((OWLEquivalentClassesAxiom) axiom).toString());
				int id1 = -1, id2 = -1, i = 0;
				for (OWLDescription d : ((OWLEquivalentClassesAxiom) axiom)
						.getDescriptions()) {
					if (d instanceof OWLClass) {
						// normalnie
						if (i == 0) {
							id1 = classes.get(((OWLClass)d).getURI().toString());
						} else {
							id2 = classes.get(((OWLClass)d).getURI().toString());
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
					edges.set(row, "edge",new org.pg.eti.kask.sova.edges.EquivalentEdge());
				}

			} else if (axiom instanceof OWLObjectPropertyRangeAxiom) {

				int id1 = properties.get(((OWLObjectPropertyRangeAxiom) axiom)
						.getProperty().toString());

				int id2 = -1;
				if (((OWLObjectPropertyRangeAxiom) axiom).getRange() instanceof OWLClass) {
					id2 = classes.get(((OWLObjectPropertyRangeAxiom) axiom)
							.getRange().asOWLClass().getURI().toString());
				} else {
					id2 = DescriptionHandler(((OWLObjectPropertyRangeAxiom) axiom).getRange());
				}

				if (id1 >= 0 && id2 >= 0) {
					int row = edges.addRow();
					edges.set(row, "source", id1);
					edges.set(row, "target", id2);
					edges.set(row, "edge",new org.pg.eti.kask.sova.edges.RangeEdge());
				}
			} else if (axiom instanceof OWLObjectPropertyDomainAxiom) {
				int id1 = properties.get(((OWLObjectPropertyDomainAxiom) axiom)
						.getProperty().toString());

				int id2 = -1;
				if (((OWLObjectPropertyDomainAxiom) axiom).getDomain() instanceof OWLClass) {
					id2 = classes.get(((OWLObjectPropertyDomainAxiom) axiom)
							.getDomain().asOWLClass().getURI().toString());
				} else {
					id2 = DescriptionHandler(((OWLObjectPropertyDomainAxiom) axiom)
							.getDomain());
				}

				if (id1 >= 0 && id2 >= 0) {
					int row = edges.addRow();
					edges.set(row, "source", id1);
					edges.set(row, "target", id2);
					edges.set(row, "edge",new org.pg.eti.kask.sova.edges.DomainEdge());
				}

			} else if (axiom instanceof OWLClassAssertionAxiom) {
				int id1 = individuals.get(((OWLClassAssertionAxiom) axiom)
						.getIndividual().toString());

				int id2 = -1;
				// if(!((OWLClassAssertionAxiom)axiom).getDescription().isOWLThing()){
				if (((OWLClassAssertionAxiom) axiom).getDescription() instanceof OWLClass) {
					id2 = classes.get(((OWLClassAssertionAxiom) axiom)
							.getDescription().asOWLClass().getURI().toString());

				} else {
					id2 = DescriptionHandler(((OWLClassAssertionAxiom) axiom)
							.getDescription());
				}
				if (id1 >= 0 && id2 >= 0) {
					int row = edges.addRow();
					edges.set(row, "source", id1);
					edges.set(row, "target", id2);
					edges.set(row, "edge", new org.pg.eti.kask.sova.edges.InstanceOfEdge());
				}
				// }

			} else if (axiom instanceof OWLDifferentIndividualsAxiom) {

				// System.out.println("DIFFRENT NODE " + axiom.toString() +
				// " dodany" );
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.DifferentNode();
				anonymNode.set("node", node);

				for (OWLIndividual ind : ((OWLDifferentIndividualsAxiom) axiom).getIndividuals()) {
					int id = individuals.get(ind.toString());
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
					superClassID = properties.get(((OWLSubPropertyAxiom) axiom)
							.getSuperProperty().toString());
				}
				// dodaj edge
				if (superClassID >= 0 && subClassID >= 0) {
					int row = edges.addRow();
					edges.set(row, "source", superClassID);
					edges.set(row, "target", subClassID);
					edges.set(row, "edge",new org.pg.eti.kask.sova.edges.SubPropertyEdge());
				}

			} else if (axiom instanceof OWLInverseObjectPropertiesAxiom) {
				int id1 = properties
						.get(((OWLInverseObjectPropertiesAxiom) axiom)
								.getFirstProperty().toString());
				int id2 = properties
						.get(((OWLInverseObjectPropertiesAxiom) axiom)
								.getSecondProperty().toString());
				int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.pg.eti.kask.sova.edges.InverseOfEdge());

			} else if (axiom instanceof OWLEquivalentObjectPropertiesAxiom) {
				int id1 = -1, id2 = -1, i = 0;
				for (OWLPropertyExpression d : ((OWLEquivalentObjectPropertiesAxiom) axiom)
						.getProperties()) {
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
					edges.set(row,"edge",new org.pg.eti.kask.sova.edges.EquivalentPropertyEdge());
				}

			} else if (axiom instanceof OWLFunctionalObjectPropertyAxiom) {
				// System.out.println("FUNC OBJ PRO " + axiom.toString() +
				// " dodany" );
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.FunctionalPropertyNode();
				anonymNode.set("node", node);
				int id = properties.get(((OWLFunctionalObjectPropertyAxiom) axiom)
								.getProperty().toString());
				int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", id);
				edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());

			} else if (axiom instanceof OWLInverseFunctionalObjectPropertyAxiom) {
				// System.out.println("FUNC INV OBJ PRO " + axiom.toString() +
				// " dodany" );
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.InverseFunctionalPropertyNode();
				anonymNode.set("node", node);
				int id = properties.get(((OWLInverseFunctionalObjectPropertyAxiom) axiom)
								.getProperty().toString());
				int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", id);
				edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());
			} else if (axiom instanceof OWLSymmetricObjectPropertyAxiom) {
				// System.out.println("FUNC sym OBJ PRO " + axiom.toString() +
				// " dodany" );
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.SymmetricPropertyNode();
				anonymNode.set("node", node);
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
				anonymNode.set("node", node);
				int id = properties.get(((OWLTransitiveObjectPropertyAxiom) axiom).getProperty().toString());
				int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", id);
				edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());
			} else if (axiom instanceof OWLSameIndividualsAxiom) {

				// System.out.println("SAME NODE " + axiom.toString() +
				// " dodany" );
				Node anonymNode = graph.addNode();
				org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.SameAsNode();
				anonymNode.set("node", node);
				for (OWLIndividual ind : ((OWLSameIndividualsAxiom) axiom).getIndividuals()) {
					int id = individuals.get(ind.toString());
					int row = edges.addRow();
					edges.set(row, "source", id);
					edges.set(row, "target", anonymNode.getRow());
					edges.set(row, "edge", new org.pg.eti.kask.sova.edges.Edge());

				}
			} else{
				if (axiom instanceof OWLDataPropertyAssertionAxiom) {
					System.out.println("DATA PROPERTY: "+axiom.toString());
//					OWLDataProperty dp = (OWLDataProperty)axiom;
					OWLDataPropertyAssertionAxiom a = (OWLDataPropertyAssertionAxiom)axiom;
				 Set<?> s = 	a.getProperty().getRanges(ontology);
				 
				//System.out.println(	a.getAxiomType().toString());
				}
				
				System.out.println("OMITTED: " + axiom);
			}

		}

		return graph;
	}

}
