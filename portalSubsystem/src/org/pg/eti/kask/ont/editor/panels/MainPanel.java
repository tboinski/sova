package org.pg.eti.kask.ont.editor.panels;


import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLOntology;

import prefuse.data.Graph;
import prefuse.data.Tree;



public class MainPanel extends JTabbedPane implements ChangeListener {

	private static final long serialVersionUID = 4299017998350435609L;

	public static final int GRAPH_TAB_INDEX = 0;
	public static final int TREE_TAB_INDEX = 1;
	
	private ResourceBundle messages;
	
	private TreePanel treeViewPanel;
	
	private GraphPanel graphViewPanel;	
	
	public MainPanel() {
		this.messages = EditorUtil.getResourceBundle(MainPanel.class);
		
		initialize();
	}
	
	
	
	public void displayTree(Tree tree){
		try {
			this.treeViewPanel = new TreePanel(tree);
			
			if(this.getComponentAt(TREE_TAB_INDEX) !=null) {
				this.setComponentAt(TREE_TAB_INDEX, treeViewPanel);
			} 
			
		} catch(IndexOutOfBoundsException e) {
			this.addTab(messages.getString("mainPanel.treeTab.title"), treeViewPanel);
		}
	}
	
	public void displayGraph(Graph graph, OWLOntology ontology){
		this.graphViewPanel = new GraphPanel(graph, ontology);
		this.setComponentAt(GRAPH_TAB_INDEX, graphViewPanel);
	}
	
	private void initialize() {
	
		this.addTab(messages.getString("mainPanel.graphTab.title"), new JPanel());
		if(EditorUtil.isInferredHierarchyPanelVisisble()) {
			this.addTab(messages.getString("mainPanel.treeTab.title"), new JPanel());
		}
		
		this.addChangeListener(this);
	}

	
	/**
	 * Metoda przenoszaca focus w grafie na konkretny wezel.
	 * 
	 * @param nodeURI URI wezla do przeniesienia focusu 
	 */
	public void focusNodeOnGraph(String nodeURI) {
		graphViewPanel.focusNodeOnGraph(nodeURI);
	}



	@Override
	public void stateChanged(ChangeEvent e) {
		
	//	if(this.getSelectedIndex() == TREE_TAB_INDEX) {
			//odswiezenie hierarchii
	//	}
	}
	
	
}
