package org.pg.eti.kask.ont.editor.tree.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;

public class OntologyPropertiesTreeModel extends DefaultTreeModel {
	
	private static final long serialVersionUID = 6679476227271466751L;
	
	public static final String DATA_PROPERTY = "dataProperty";
	public static final String OBJECT_PROPERTY = "objectProperty";
	
	public static final String OBJECT_PROPERTY_TREE = "objectPropertyTree";
	public static final String DATA_PROPERTY_TREE = "dataPropertyTree";
	public static final String ALL_PROPERTY_TREE = "tree";

	private ResourceBundle messages;
	
	private DefaultMutableTreeNode root;
	
	private OWLOntology ontology;
	
	//wykorzystane wlasciwosci podczas budowy hierarchii klas 
	private Set<String> usedDataProperties;
	
	//wykorzystane wlasciwosci podczas budowy hierarchii klas 
	private Set<String> usedObjectProperties;
	
	public OntologyPropertiesTreeModel(OWLOntology ontology, String typeOfTree) {
		super(null);
		
		this.messages = EditorUtil.getResourceBundle(OntologyPropertiesTreeModel.class);
		this.ontology = ontology;
		this.usedDataProperties = new HashSet<String>();
		this.usedObjectProperties = new HashSet<String>();
		BasicTreeNode nodeData = new BasicTreeNode("", messages.getString("propertiesRootNode.text"), "");
		this.root = new DefaultMutableTreeNode(nodeData);
		this.setRoot(root);
		
		if(typeOfTree.equals(ALL_PROPERTY_TREE)) {
			buildTree();
		} else if(typeOfTree.equals(OBJECT_PROPERTY_TREE)) {
			buildObjectPropertyTree();
		} else if(typeOfTree.equals(DATA_PROPERTY_TREE)) {
			buildDataPropertyTree();
		}
	}
	
	private void buildObjectPropertyTree() {
		BasicTreeNode objectPropertyNodeData = new BasicTreeNode("", messages.getString("objectPropertiesNode.text"), OBJECT_PROPERTY);
		DefaultMutableTreeNode objectPropertyRootNode = new DefaultMutableTreeNode(objectPropertyNodeData);
		
		List<OWLObjectProperty> objectProperties = new ArrayList<OWLObjectProperty>();
		
		root.insert(objectPropertyRootNode, 0);
		
		Set<OWLObjectProperty> referencedObjectProperties = ontology.getReferencedObjectProperties();
		
		for(OWLObjectProperty currentProperty : referencedObjectProperties) {		
			
			//sprawdzenie czy dana wlasciwosc nie ma zadnych wlasciwosci nadrzednych
			if(currentProperty.getSuperProperties(ontology) == null || currentProperty.getSuperProperties(ontology).size() == 0) {
				if(!usedObjectProperties.contains(currentProperty.getURI().toString())) {
					if(currentProperty.getSubProperties(ontology) != null  && currentProperty.getSubProperties(ontology).size() > 0) {
						objectProperties.add(0, currentProperty);
					} else {
						objectProperties.add(currentProperty);
					}
					usedObjectProperties.add(currentProperty.getURI().toString());
				}
			} 
		}
		
		int temp = 0;
		for(OWLObjectProperty currentProperty : objectProperties) {
			DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(new BasicTreeNode(currentProperty.getURI().toString(), currentProperty.getURI().getFragment(), OBJECT_PROPERTY));
			objectPropertyRootNode.insert(currentNode, temp);
			
			buildObjectPropertiesSubTree(currentNode, currentProperty);
			temp++;
		}
	}
	
	private void buildDataPropertyTree() {
		BasicTreeNode dataPropertyNodeData = new BasicTreeNode("", messages.getString("dataPropertiesNode.text"), DATA_PROPERTY);
		DefaultMutableTreeNode dataPropertyRootNode = new DefaultMutableTreeNode(dataPropertyNodeData);
		
		List<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>();
		
		root.insert(dataPropertyRootNode, 0);
		
		Set<OWLDataProperty> referencedDataProperties = ontology.getReferencedDataProperties();
		
		int temp = 0;
		for(OWLDataProperty currentProperty : referencedDataProperties) {		
			//sprawdzenie czy dana wlasciwosc nie ma zadnych wlasciwosci nadrzednych
			if(currentProperty.getSuperProperties(ontology) == null || currentProperty.getSuperProperties(ontology).size() == 0) {
				if(!usedDataProperties.contains(currentProperty.getURI().toString())) {
					if(currentProperty.getSubProperties(ontology) != null  && currentProperty.getSubProperties(ontology).size() > 0) {
						dataProperties.add(0, currentProperty);
					} else {
						dataProperties.add(currentProperty);
					}
					usedDataProperties.add(currentProperty.getURI().toString());
					
				}
			} 
		}
		
		for(OWLDataProperty currentProperty : dataProperties) {
			DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(new BasicTreeNode(currentProperty.getURI().toString(), currentProperty.getURI().getFragment(), DATA_PROPERTY));
			dataPropertyRootNode.insert(currentNode, temp);
			buildDataPropertiesSubTree(currentNode, currentProperty);
			temp++;
		}
	}
	
	private void buildTree() {
		
		BasicTreeNode objectPropertyNodeData = new BasicTreeNode("", messages.getString("objectPropertiesNode.text"), OBJECT_PROPERTY);
		DefaultMutableTreeNode objectPropertyRootNode = new DefaultMutableTreeNode(objectPropertyNodeData);
		BasicTreeNode dataPropertyNodeData = new BasicTreeNode("", messages.getString("dataPropertiesNode.text"), DATA_PROPERTY);
		DefaultMutableTreeNode dataPropertyRootNode = new DefaultMutableTreeNode(dataPropertyNodeData);
		
		List<OWLObjectProperty> objectProperties = new ArrayList<OWLObjectProperty>();
		List<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>();
		
		root.insert(objectPropertyRootNode, 0);
		root.insert(dataPropertyRootNode, 1);
		
		//najpierw wszystkie object properties
		Set<OWLObjectProperty> referencedObjectProperties = ontology.getReferencedObjectProperties();
		
		for(OWLObjectProperty currentProperty : referencedObjectProperties) {		
			
			//sprawdzenie czy dana wlasciwosc nie ma zadnych wlasciwosci nadrzednych
			if(currentProperty.getSuperProperties(ontology) == null || currentProperty.getSuperProperties(ontology).size() == 0) {
				if(!usedObjectProperties.contains(currentProperty.getURI().toString())) {
					if(currentProperty.getSubProperties(ontology) != null  && currentProperty.getSubProperties(ontology).size() > 0) {
						objectProperties.add(0, currentProperty);
					} else {
						objectProperties.add(currentProperty);
					}
					usedObjectProperties.add(currentProperty.getURI().toString());
				}
			} 
		}
		
		int temp = 0;
		for(OWLObjectProperty currentProperty : objectProperties) {
			DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(new BasicTreeNode(currentProperty.getURI().toString(), currentProperty.getURI().getFragment(), OBJECT_PROPERTY));
			objectPropertyRootNode.insert(currentNode, temp);
			
			buildObjectPropertiesSubTree(currentNode, currentProperty);
			temp++;
		}
		
		
		//teraz dopiero data properties
		Set<OWLDataProperty> referencedDataProperties = ontology.getReferencedDataProperties();
		
		temp = 0;
		for(OWLDataProperty currentProperty : referencedDataProperties) {		
			//sprawdzenie czy dana wlasciwosc nie ma zadnych wlasciwosci nadrzednych
			if(currentProperty.getSuperProperties(ontology) == null || currentProperty.getSuperProperties(ontology).size() == 0) {
				if(!usedDataProperties.contains(currentProperty.getURI().toString())) {
					if(currentProperty.getSubProperties(ontology) != null  && currentProperty.getSubProperties(ontology).size() > 0) {
						dataProperties.add(0, currentProperty);
					} else {
						dataProperties.add(currentProperty);
					}
					usedDataProperties.add(currentProperty.getURI().toString());
					
				}
			} 
		}
		
		for(OWLDataProperty currentProperty : dataProperties) {
			DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(new BasicTreeNode(currentProperty.getURI().toString(), currentProperty.getURI().getFragment(), DATA_PROPERTY));
			dataPropertyRootNode.insert(currentNode, temp);
			buildDataPropertiesSubTree(currentNode, currentProperty);
			temp++;
		}
		
	}
	
	private void buildObjectPropertiesSubTree(DefaultMutableTreeNode parent, OWLObjectProperty currentProperty) {
		Set<OWLObjectPropertyExpression> subProperties = currentProperty.getSubProperties(ontology);
		List<OWLObjectProperty> objectProperties = new ArrayList<OWLObjectProperty>();
		
		if(subProperties != null && subProperties.size() > 0) {
			int temp = 0;
			for(OWLObjectPropertyExpression propExpression : subProperties) {
				if(!propExpression.isAnonymous()) {
					OWLObjectProperty currentProp = propExpression.asOWLObjectProperty();
					if(!usedObjectProperties.contains(currentProp.getURI().toString())) {
						if(currentProp.getSubProperties(ontology) != null  && currentProp.getSubProperties(ontology).size() > 0) {
							objectProperties.add(0, currentProp);
						} else {
							objectProperties.add(currentProp);
						}
						usedObjectProperties.add(currentProp.getURI().toString());
						
					}
				}
			}
			
			for(OWLObjectProperty currentProp : objectProperties) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new BasicTreeNode(currentProp.getURI().toString(), currentProp.getURI().getFragment(), OBJECT_PROPERTY));
				parent.insert(newNode, temp);
				temp++;
				buildObjectPropertiesSubTree(newNode, currentProp);	
			}
		}
	}
	
	private void buildDataPropertiesSubTree(DefaultMutableTreeNode parent, OWLDataProperty currentProperty) {
		Set<OWLDataPropertyExpression> subProperties = currentProperty.getSubProperties(ontology);
		List<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>();
		
		if(subProperties != null && subProperties.size() > 0) {
			int temp = 0;
			for(OWLDataPropertyExpression propExpression : subProperties) {
				if(!propExpression.isAnonymous()) {
					OWLDataProperty currentProp = propExpression.asOWLDataProperty();
					if(!usedDataProperties.contains(currentProp.getURI().toString())) {
						if(currentProp.getSubProperties(ontology) != null  && currentProp.getSubProperties(ontology).size() > 0) {
							dataProperties.add(0, currentProp);
						} else {
							dataProperties.add(currentProp);
						}
						usedDataProperties.add(currentProp.getURI().toString());
						
					}
				}
			}
			
			for(OWLDataProperty currentProp : dataProperties) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new BasicTreeNode(currentProp.getURI().toString(), currentProp.getURI().getFragment(), DATA_PROPERTY));
				parent.insert(newNode, temp);
				temp++;
				buildDataPropertiesSubTree(newNode, currentProp);
			}
		}
	}

}
