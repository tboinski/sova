package org.pg.eti.kask.ont.editor.tree.model;

import java.util.ResourceBundle;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;

public class OntologyIndividualsTreeModel extends DefaultTreeModel {
	
	private static final long serialVersionUID = 8504510288767423541L;

	private ResourceBundle messages;
	
	private DefaultMutableTreeNode root;
	
	private OWLOntology ontology;
	
	public OntologyIndividualsTreeModel(OWLOntology ontology) {
		super(null);

		this.messages = EditorUtil.getResourceBundle(OntologyIndividualsTreeModel.class);
		this.ontology = ontology;
		BasicTreeNode nodeData = new BasicTreeNode("", messages.getString("individualsRootNode.text"), "");
		this.root = new DefaultMutableTreeNode(nodeData);
		this.setRoot(root);
		
		buildTree();
	}

	private void buildTree() {
		int temp = 0;
		for(OWLIndividual ind : ontology.getReferencedIndividuals()) {
			BasicTreeNode nodeObjectData = new BasicTreeNode(ind.getURI().toString(),ind.toString(), "individuals");
			root.insert(new DefaultMutableTreeNode(nodeObjectData), temp);
			temp++;
		}
	}
	
}
