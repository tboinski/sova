package org.pg.eti.kask.ont.editor.graph.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

/**
 * Klasa przeprowadzajaca konwersje z modelu OWL API
 * na model wymagany przez komponent do wizualizacji grafow  - Prefuse.
 * 
 * @author Andrzej Jakowski
 */
public class ClassesGraphModel extends Graph {
	
	private OWLOntology ontology;
	
	//przechowywane wezly grafu (wezel_pocz, typ, wezel_konc)
	private ArrayList<String[]> edges;
	
	//przechowywanie wezlow utworzonego grafu - klucz: URI klasy, wartosc: obiekt reprezentujacy wezel
	private Hashtable<String, Node> nodes;

	//tutaj przechowywane sa URI wszystkich klas w ontologii
	private Set<String> referencedClasses;
	/**
	 * Konstruktor przeprowadzajacy konwersje modelu obiektowego ontologii 
	 * na model umozliwiajacy wizualizacje ontologii w postaci grafu 
	 * (prefuse).
	 * 
	 * @param ontology model ontologii zgodny z owl api
	 */
	public ClassesGraphModel(OWLOntology ontology) {
		//utworzenie grafu skierowanego
		super(true);
		
		this.ontology = ontology;
		this.edges = new ArrayList<String[]>();
		this.nodes = new Hashtable<String, Node>();
		this.referencedClasses = new HashSet<String>();
		
		
		initializeGraph();
		createGraph();
		//na koniec dodanie kolejnych wezlow do grafu
		createGraphEdges();
		
	}
	
	/**
	 * Inicjalizuje poszczegolne kolumny grafu
	 */
	private void initializeGraph() {
		//ustawienie modelu danych dla wezlow 
		this.getNodeTable().addColumn(Constants.GRAPH_NODE_URI_COLUMN_NAME, String.class);
		this.getNodeTable().addColumn(Constants.GRAPH_NODE_NAME_COLUMN_NAME, String.class);
		this.getNodeTable().addColumn(Constants.GRAPH_NODE_TYPE_COLUMN_NAME, String.class);
		
		//ustawienie modelu danych dla krawedzi
		this.getEdgeTable().addColumn(Constants.GRAPH_EDGE_LABEL_COLUMN, String.class);
		this.getEdgeTable().addColumn(Constants.GRAPH_EDGE_TYPE_COLUMN, String.class);
	}
	
	
	/**
	 * Metoda tworzaca graf.
	 */
	private void createGraph() {
		//zapisanie uri klas
		for(OWLClass currentClass: ontology.getReferencedClasses()) {
			referencedClasses.add(currentClass.getURI().toString());
		}
		
		//dodanie wezlow niedowiazanych do zadnych klas
		Set<OWLClass> referencedClasses = ontology.getReferencedClasses();
		for(OWLClass currentClass: referencedClasses) {
			if(currentClass.getSuperClasses(ontology) == null || currentClass.getSuperClasses(ontology).size() == 0) { 
				if(!nodes.containsKey(currentClass.getURI().toString())) {
					Node currentNode = this.addNode();
					currentNode.setString(Constants.GRAPH_NODE_URI_COLUMN_NAME, currentClass.getURI().toString());
					currentNode.setString(Constants.GRAPH_NODE_NAME_COLUMN_NAME, EditorUtil.getOwlEntityName(currentClass));
					currentNode.setString(Constants.GRAPH_NODE_TYPE_COLUMN_NAME, Constants.GRAPH_NODE_CLASS_TYPE);
					//dodanie kolejnego wezla 
					nodes.put(currentClass.getURI().toString(), currentNode);
					//rozbudowanie drzewa
					buildGraph(currentClass);
				}
			}
		}
		
	}
	
	/**
	 * Metoda przeprowadzajaca rekurencyjne tworzenie grafu.
	 * Dla kazdego wezla wyszukiwani sa jego potomkowie (podklasy,
	 * klasy rozlaczne, klasy rownorzedne) i dla kazdego z nich 
	 * wywolywana jest ta metoda.
	 * 
	 * @param currentClass aktualnie przetwarzana klasa
	 */
	private void buildGraph(OWLClass currentClass) {

		//sprawdzenie czy klasa ta istnieje w ontologii - bug w owl api 
		//podczas usuwania klasy aksjom subclass nie jest ususwany!!!!!
		if (referencedClasses.contains(currentClass.getURI().toString()) && !nodes.containsKey(currentClass.getURI().toString())) {
			Node currentNode = this.addNode();
			currentNode.setString(Constants.GRAPH_NODE_URI_COLUMN_NAME, currentClass.getURI().toString());
			currentNode.setString(Constants.GRAPH_NODE_NAME_COLUMN_NAME, EditorUtil.getOwlEntityName(currentClass));
			currentNode.setString(Constants.GRAPH_NODE_TYPE_COLUMN_NAME, Constants.GRAPH_NODE_CLASS_TYPE);
			//dodanie kolejnego wezla  
			nodes.put(currentClass.getURI().toString(), currentNode);
		}
			
		//wyszukaj wszystkie podklasy do danej klasy
		Set<OWLDescription> subClasses = currentClass.getSubClasses(ontology);

		for (OWLDescription desc : subClasses) {
			if (!desc.isAnonymous()) {
				OWLClass current = desc.asOWLClass();
				if(referencedClasses.contains(current.getURI().toString())) {
					if (!nodes.containsKey(current.getURI().toString())) {
						buildGraph(current);
					}
					addEdge(new String[] { currentClass.getURI().toString(), Constants.GRAPH_EDGE_SUBCLASS_TYPE, current.getURI().toString() });
				}
				
			}
		}

		//pobierz klasy rozlaczne do danej klasy
		Set<OWLDescription> disjointClasses = currentClass.getDisjointClasses(ontology);

		for (OWLDescription desc : disjointClasses) {
			if (!desc.isAnonymous()) {
				OWLClass current = desc.asOWLClass();
				if(referencedClasses.contains(current.getURI().toString())) {
					if (!nodes.containsKey(current.getURI().toString())) {
						buildGraph(current);
					}
					addEdge(new String[] { currentClass.getURI().toString(), Constants.GRAPH_EDGE_DISJOINT_TYPE, current.getURI().toString() });
				}
			}
		}

		//wyszukaj wszystkie klasy rownorzedne do danej klasy
		Set<OWLDescription> equivalentClasses = currentClass.getEquivalentClasses(ontology);

		for (OWLDescription desc : equivalentClasses) {
			if (!desc.isAnonymous()) {
				OWLClass current = desc.asOWLClass();
				if(referencedClasses.contains(current.getURI().toString())) {
					if (!nodes.containsKey(current.getURI().toString())) {
						buildGraph(current);
					}
					addEdge(new String[] { currentClass.getURI().toString(), Constants.GRAPH_EDGE_EQUIVALENCE_TYPE, current.getURI().toString() });
				}
			}
		}

		
	}

	
	/**
	 * Metoda umozliwiajaca utworzenie wszystkich krawedzi 
	 * grafu w oparciu o dane przechowywane w tymczasowym 
	 * obiekcie.
	 */
	private void createGraphEdges() {

		for (String[] edgeDescription : edges) {
			Node source = nodes.get(edgeDescription[2]);
			Node target = nodes.get(edgeDescription[0]);

			Edge edge = this.addEdge(source, target);
			edge.setString(Constants.GRAPH_EDGE_LABEL_COLUMN, edgeDescription[1]);
			edge.setString(Constants.GRAPH_EDGE_TYPE_COLUMN, edgeDescription[1]);
		}
	}
	
	/**
	 * Metoda dodajaca dany wezel do tymczasowej tablicy wezlow.
	 * 
	 * @param edgeDescription tablica zawierajaca dane niezbedne do utworzenia grafu (zrodlo, nazwa/typ, cel)
	 */
	private void addEdge(String[] edgeDescription) {
		edges.add(edgeDescription);
	}


}
