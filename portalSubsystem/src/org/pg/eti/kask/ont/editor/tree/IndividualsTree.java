package org.pg.eti.kask.ont.editor.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.pg.eti.kask.ont.editor.menu.IndividualsPopupMenu;
import org.pg.eti.kask.ont.editor.panels.ComponentRepositoryPanel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyIndividualsTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;

public class IndividualsTree extends JTree {

	private static final long serialVersionUID = 1281500321031714026L;

	private OntologyIndividualsTreeModel model;
	
	private ComponentRepositoryPanel parentPanel;
	
	public IndividualsTree(OntologyIndividualsTreeModel model, ComponentRepositoryPanel parentPanel) {
		super(model);
		
		this.parentPanel = parentPanel;
		this.model = model;
		
		
		initialize();
	}

	private void initialize() {
		
		this.addMouseListener(new IndividualsPopupMenu(parentPanel));
	}
	
	public void removeNode() {
		DefaultMutableTreeNode nodeToRemove = null;
		TreePath selectionPath = this.getSelectionPath();
				
		
		if(selectionPath != null) {
			nodeToRemove = ((DefaultMutableTreeNode)selectionPath.getLastPathComponent());
			model.removeNodeFromParent(nodeToRemove);
			
		}
	}
	
	public void addNode(BasicTreeNode newNode, boolean selectNewNode) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = this.getSelectionPath();
		
		//jesli null to rodzicem jest korzen
		if(parentPath == null) {
			parentNode = (DefaultMutableTreeNode)model.getRoot();
		} else {
			parentNode = ((DefaultMutableTreeNode)parentPath.getLastPathComponent());
		}
		
		addNode(parentNode, newNode, selectNewNode);
	}
	
	
	public void addNode(DefaultMutableTreeNode parentNode, BasicTreeNode newNode, boolean selectNewNode) {
		DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(newNode);
		
		model.insertNodeInto(addedNode, parentNode, parentNode.getChildCount());
		
		if(selectNewNode) {
			this.scrollPathToVisible(new TreePath(addedNode.getPath()));
		}
	}
		
	public BasicTreeNode getSelectedTreeNode(){
		TreePath selectionPath = getSelectionPath();
		
		BasicTreeNode selectedNode = null;
		if(selectionPath != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();
			
			if(node != null) {
				selectedNode = (BasicTreeNode)node.getUserObject();
			}
		}
		
		return selectedNode;
	}
}
