package org.pg.eti.kask.ont.editor.tree.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;

/**
 * 
 * 
 * @author Andrzej Jakowski
 */
public class OntologyClassesTreeModel extends DefaultTreeModel {
		
	private static final long serialVersionUID = -2535019222308299251L;
	
	private ResourceBundle messages;
	
	private DefaultMutableTreeNode root;
	
	private OWLOntology ontology;
	
	private Set<String> referencedClasses;
	
	//wykorzystane klasy podczas budowy hierarchii klas 
	private Set<String> usedClasses;
	
	public OntologyClassesTreeModel(OWLOntology ontology) {
		//konstruktor podrzedny musi byc wywolany
		super(null);
		
		this.messages = EditorUtil.getResourceBundle(OntologyClassesTreeModel.class);		
		this.ontology = ontology;
		this.usedClasses = new HashSet<String>();		
		this.referencedClasses = new HashSet<String>();		
		this.root = new DefaultMutableTreeNode(new BasicTreeNode("", messages.getString("classesRootNode.text"), ""));
		
		this.setRoot(root);
		
		//spowodowane bledem w OWL API
		for(OWLClass c :ontology.getReferencedClasses()) {
			referencedClasses.add(c.getURI().toString());			
		}
		
		buildTree();
		
	}
	
	private void buildTree() {
		List<OWLClass> nodes = new ArrayList<OWLClass>();
		Logic logic = Logic.getInstance();
		OWLClass thing = logic.getDataFactory().getOWLThing();
		int temp = 0;
		for (OWLClass currentClass : ontology.getReferencedClasses()) {
			//sprawdzenie czy klasa jest najwyzej w hierarchii
			if(currentClass.getSuperClasses(ontology) == null || currentClass.getSuperClasses(ontology).size() == 0) {
				if (!usedClasses.contains(currentClass.getURI().toString())) {
					if(currentClass.getSubClasses(ontology) != null  && currentClass.getSubClasses(ontology).size() > 0) {
						if(!currentClass.getURI().toString().equals(thing.getURI().toString())) {
							nodes.add(0, currentClass);
						}
					} else {
						if(!currentClass.getURI().toString().equals(thing.getURI().toString())) {
							nodes.add(currentClass);
						}
					}
					usedClasses.add(currentClass.getURI().toString());
					
					
				}
				
			} else if(currentClass.getSuperClasses(ontology).size() > 0){
				Set<OWLDescription> superClasses = currentClass.getSuperClasses(ontology);
				boolean temp1 = true;
				for(OWLDescription desc: superClasses) {
					if(!desc.isAnonymous()) {
						temp1 = false;
						break;
					}
				}
				
				if(temp1) {
					if (!usedClasses.contains(currentClass.getURI().toString())) {
						if(currentClass.getSubClasses(ontology) != null  && currentClass.getSubClasses(ontology).size() > 0) {
							if(!currentClass.getURI().toString().equals(thing.getURI().toString())) {
								nodes.add(0, currentClass);
							}
						}else {
							if(!currentClass.getURI().toString().equals(thing.getURI().toString())) {
								nodes.add(currentClass);
							}
						}
					}
				}
			}
		}
		
		for(OWLClass currentClass : nodes) {
			BasicTreeNode nodeData = new BasicTreeNode(currentClass.getURI().toString(), currentClass.getURI().getFragment(), "class");
			DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(nodeData);
			root.insert(currentNode, temp);
			buildSubTree(currentNode, currentClass);
			temp++;
		}
		
		
		
		if(referencedClasses.contains(thing.getURI().toString())) {
			buildSubTree(root, thing);
		}
		
	}
	
	private void buildSubTree(DefaultMutableTreeNode parent, OWLClass currentClass) {
		List<OWLClass> nodes = new ArrayList<OWLClass>();
		Set<OWLDescription> subClasses = currentClass.getSubClasses(ontology);
		
		if(subClasses != null && subClasses.size() > 0) {
			int temp = 0;
			for(OWLDescription desc: subClasses) {
				if(!desc.isAnonymous()) {
					OWLClass c = desc.asOWLClass();
					if(referencedClasses.contains(c.getURI().toString()) && !usedClasses.contains(c.getURI().toString())) {
						if(c.getSubClasses(ontology) != null  && c.getSubClasses(ontology).size() > 0) {
							nodes.add(0, c);
						} else {
							nodes.add(c);
						}
						usedClasses.add(currentClass.getURI().toString());						
					}
				}
			}
			
			for(OWLClass c : nodes) {
				BasicTreeNode nodeData = new BasicTreeNode(c.getURI().toString(), EditorUtil.getOwlEntityName(c), "class");
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeData);
				parent.insert(newNode, temp);
				temp++;
				buildSubTree(newNode, c);
			}
 		}
	}
	
}
