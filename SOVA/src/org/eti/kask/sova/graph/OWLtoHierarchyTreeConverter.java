package org.eti.kask.sova.graph;

import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.UnknownOWLOntologyException;

import prefuse.data.Node;
import prefuse.data.Tree;

/**
 * Konwerter obiektu OWLOntology na obiekt Tree wykorzystywany przez Prefuse.
 * Klasa jest singletonem.
 * 
 * @author Piotr Kunowski
 * 
 */
public class OWLtoHierarchyTreeConverter {
	private static OWLtoHierarchyTreeConverter instance =null;
	
	//rerefencje do obiektow OWL API
	private OWLOntology ontology;
	private OWLOntologyManager ontologyManager;
	private OWLDataFactory dataFactory;
	private OWLReasoner reasoner;
	private Tree tree;
	//wykorzystane klasy podczas budowania drzewa
	private Set<String> usedClasses; 
	
	private OWLtoHierarchyTreeConverter(){
		
	}
	
	/**
	 * 
	 * @return statyczną instancję klasy OWLtoHierarchyTree
	 */
	public static OWLtoHierarchyTreeConverter getInstance() {
		if (instance == null ){
			instance = new OWLtoHierarchyTreeConverter();
		}
		return instance;
	}
	
	public Tree OWLtoTree(OWLOntology ontology){
		
		this.ontology = ontology;
		this.ontologyManager = OWLManager.createOWLOntologyManager();
		this.dataFactory = ontologyManager.getOWLDataFactory();
		
		this.usedClasses = new HashSet<String>();
		tree=new Tree();
		tree.addColumn(Constants.TREE_NODES, org.eti.kask.sova.nodes.Node.class);
		
		//inicjalizacja reasonera
		initializeReasoner();
		
		buildTree();
		return tree;
	}

	
	/**
	 * Zbudowanie drzewa poczynajac od elementu najwyzej stojacego w hierarchi -
	 * klasy OWLThing.
	 */
	private void buildTree() {
		try {
			reasoner
					.loadOntologies(ontologyManager.getImportsClosure(ontology));
			OWLClass thingClass = dataFactory.getOWLThing();
			buildTree(null, thingClass);
			reasoner.clearOntologies();
		} catch (UnknownOWLOntologyException e) {
			e.printStackTrace();
		} catch (OWLReasonerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda rekurencyjnie rozbudowujaca drzewo.
	 * 
	 * @param parentNode aktualnie przetwarzany wezel
	 * @param currentEntity  aktualnie przetwarzany obiekt OWL API (albo OWLClass, albo  OWLIndividual)
	 * @throws OWLReasonerException wyjatek z reasonera
	 */
	private void buildTree(Node parentNode, OWLEntity currentEntity)
			throws OWLReasonerException {
		Node currentNode = null;

		if (currentEntity instanceof OWLClass) {
			OWLClass currentClass = (OWLClass) currentEntity;

			if (reasoner.isSatisfiable(currentClass)) {
				if (parentNode == null) {
					currentNode = tree.addRoot();
				} else {
					currentNode = tree.addChild(parentNode);
				}
				usedClasses.add(currentClass.getURI().toString());
				org.eti.kask.sova.nodes.Node node =null;
				
				if (currentClass.isOWLThing()){
					node = new org.eti.kask.sova.nodes.ThingNode();
					node.setLabel("T");

				}else{
					node = new org.eti.kask.sova.nodes.ClassNode();
					node.setLabel(currentClass.toString());
				}
				currentNode.set(Constants.TREE_NODES, node);

				Set<OWLClass> subClasses = OWLReasonerAdapter
						.flattenSetOfSets(reasoner.getSubClasses(currentClass));

				for (OWLClass child : subClasses) {
					buildTree(currentNode, child);
				}

				Set<OWLIndividual> individuals = reasoner.getIndividuals(
						currentClass, true);

				for (OWLIndividual ind : individuals) {
					buildTree(currentNode, ind);
				}

			}
		} else if (currentEntity instanceof OWLIndividual) {
			OWLIndividual currentIndividual = (OWLIndividual) currentEntity;

			if (!currentIndividual.isAnonymous()) {
				currentNode = tree.addChild(parentNode);
				org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.IndividualNode();
				node.setLabel(currentIndividual.toString());
				currentNode.set(Constants.TREE_NODES, node);
			}
		}
	}

	/**
	 * Metoda inicjalizuja mechanizm wnioskujacy.
	 */
	private void initializeReasoner() {
		this.reasoner = new org.mindswap.pellet.owlapi.Reasoner(ontologyManager);

	}
	
}
