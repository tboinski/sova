package org.pg.eti.kask.ont.editor.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.pg.eti.kask.ont.editor.menu.ClassesPopupMenu;
import org.pg.eti.kask.ont.editor.panels.ComponentRepositoryPanel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;

public class ClassesTree extends JTree  {

	private static final long serialVersionUID = 7605349120405913438L;
	
	private OntologyClassesTreeModel model;
	
	private ComponentRepositoryPanel parentPanel;

	public ClassesTree(OntologyClassesTreeModel model, ComponentRepositoryPanel parentPanel) {
		super(model);
		
		this.model = model;
		this.parentPanel = parentPanel;
		
		initialize();
	}
	
	private void initialize() {
		this.addMouseListener(new ClassesPopupMenu(parentPanel));
	}
	
	/**
	 * Metoda usuwajaca aktualnie zaznaczony wezel w drzewie.
	 * 
	 */
	public void removeNode() {
		DefaultMutableTreeNode nodeToRemove = null;
		TreePath selectionPath = this.getSelectionPath();
				
		
		if(selectionPath != null) {
			nodeToRemove = ((DefaultMutableTreeNode)selectionPath.getLastPathComponent());
			model.removeNodeFromParent(nodeToRemove);
			
		}
	}
	
	/**
	 * Metoda dodajaca nowy wezel do drzewa. 
	 * 
	 * @param newNode wezel do dodania
	 * @param selectNewNode okresla czy dany wezel mam byc zaznaczony w drzewie
	 */
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
	
	/**
	 * Metoda dodajaca nowy wezel do drzewa.
	 * 
	 * @param parentNode wezel bedacy rodzicem danego wezla
	 * @param newNode wezel do dodania
	 * @param selectNewNode okresla czy dany wezel mam byc zaznaczony w drzewie
	 */
	public void addNode(DefaultMutableTreeNode parentNode, BasicTreeNode newNode, boolean selectNewNode) {
		DefaultMutableTreeNode addedNode = new DefaultMutableTreeNode(newNode);
		
		model.insertNodeInto(addedNode, parentNode, parentNode.getChildCount());
		
		if(selectNewNode) {
			this.scrollPathToVisible(new TreePath(addedNode.getPath()));
		}
	}
	
	/**
	 * Metoda zwracajaca zaznaczony obiekt w drzewie.
	 * 
	 * @return instancja klasy BasicTreeNode okreslajaca aktualnie zaznaczony wezel
	 */
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
