package org.pg.eti.kask.ont.editor.tree.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
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
* Klasa przeprowadzajaca konwersje z modelu OWL API
* na model wymagany przez komponent do wizualizacji grafow  - Prefuse.
* Klasa ta tworzy obiekt drzewa, ktory jest akceptowany przez instancje
* klasy <code>Visualization</code>.
* 
* @author Andrzej Jakowski
*/
public class ClassesTreeModel extends Tree {
	
	//nazwa klasy Reasonera.
	private static final String REASONER_CLASS_NAME = "org.mindswap.pellet.owlapi.Reasoner";
	
	//rerefencje do obiektow OWL API
	private OWLOntology ontology;
	private OWLOntologyManager ontologyManager;
	private OWLDataFactory dataFactory;
	private OWLReasoner reasoner;
	
	//wykorzystane klasy podczas budowania drzewa
	private Set<String> usedClasses; 
	
	/**
	 * Konstruktor tworzacy nowy obiekt drzewa z ontologii.
	 * Drzewo to powstaje w oparciu o dzialanie mechanizmu wsnioskujacego.
	 * 
	 * @param ontology model ontologii zgodny z OWL API
	 */
	public ClassesTreeModel(OWLOntology ontology) {
		this.ontology = ontology;
		this.ontologyManager = OWLManager.createOWLOntologyManager();
		this.dataFactory = ontologyManager.getOWLDataFactory();
		
		this.usedClasses = new HashSet<String>();
		
		//inicjalizacja reasonera
		initializeReasoner();
		
		initializeTree();
		
		buildTree();
	}

	/**
	 * Zainicjalizowanie drzewa.
	 */
	private void initializeTree() {
		this.addColumn(Constants.TREE_NODE_URI_COLUMN, String.class);
		this.addColumn(Constants.TREE_NODE_NAME_COLUMN, String.class);
		this.addColumn(Constants.TREE_NODE_TYPE_COLUMN, String.class);
	}
	
	/**
	 * Zbudowanie drzewa poczynajac od elementu najwyzej stojacego 
	 * w hierarchi - klasy OWLThing. 
	 */
	private void buildTree() {
		try {
			reasoner.loadOntologies(ontologyManager.getImportsClosure(ontology));
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
	 * @param currentEntity aktualnie przetwarzany obiekt OWL API (albo OWLClass, albo OWLIndividual)
	 * @throws OWLReasonerException wyjatek z reasonera
	 */
	private void buildTree(Node parentNode, OWLEntity currentEntity) throws OWLReasonerException {
		Node currentNode = null;
		
		if(currentEntity instanceof OWLClass) {
			OWLClass currentClass = (OWLClass)currentEntity;
			
			if (reasoner.isSatisfiable(currentClass)) {
				if (parentNode == null) {
					currentNode = this.addRoot();
				} else {
					currentNode = this.addChild(parentNode);
				}
				usedClasses.add(currentClass.getURI().toString());

				currentNode.setString(Constants.TREE_NODE_URI_COLUMN, currentClass.getURI().toString());
				currentNode.setString(Constants.TREE_NODE_NAME_COLUMN, EditorUtil.getOwlEntityName(currentClass));
				currentNode.setString(Constants.TREE_NODE_TYPE_COLUMN, Constants.TREE_NODE_CLASS_TYPE);

				Set<OWLClass> subClasses = OWLReasonerAdapter.flattenSetOfSets(reasoner.getSubClasses(currentClass));

				for(OWLClass child : subClasses) {
					buildTree(currentNode, child);
				}
				
				Set<OWLIndividual> individuals = reasoner.getIndividuals(currentClass, true);
				
				for(OWLIndividual ind : individuals) {
					buildTree(currentNode, ind);
				}
				
			}
		} else if(currentEntity instanceof OWLIndividual) {
			OWLIndividual currentIndividual = (OWLIndividual)currentEntity;
			
			if(!currentIndividual.isAnonymous()) {
				currentNode = this.addChild(parentNode);
				currentNode.setString(Constants.TREE_NODE_URI_COLUMN, currentIndividual.getURI().toString());
				currentNode.setString(Constants.TREE_NODE_NAME_COLUMN, currentIndividual.toString());
				currentNode.setString(Constants.TREE_NODE_TYPE_COLUMN, Constants.TREE_NODE_INDIVIDUAL_TYPE);
			}
		}
	}
	
	/**
	 * Metoda inicjalizuja mechanizm wnioskujacy.
	 */
	private void initializeReasoner() {
		try {
			Class<?> reasonerClass = Class.forName(REASONER_CLASS_NAME);
			Constructor<?> con = reasonerClass.getConstructor(OWLOntologyManager.class);
			this.reasoner = (OWLReasoner)con.newInstance(ontologyManager);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (UnknownOWLOntologyException e) {
			e.printStackTrace();
		} 
		
	}
}
