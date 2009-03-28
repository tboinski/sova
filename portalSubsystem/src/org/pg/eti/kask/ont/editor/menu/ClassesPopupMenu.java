package org.pg.eti.kask.ont.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.dialogs.ClassesIntersectionDialog;
import org.pg.eti.kask.ont.editor.dialogs.ClassesUnionDialog;
import org.pg.eti.kask.ont.editor.dialogs.IndividualsEnumerationDialog;
import org.pg.eti.kask.ont.editor.dialogs.ShowClassAxiomsDialog;
import org.pg.eti.kask.ont.editor.graph.model.ClassesGraphModel;
import org.pg.eti.kask.ont.editor.panels.ComponentRepositoryPanel;
import org.pg.eti.kask.ont.editor.panels.MainPanel;
import org.pg.eti.kask.ont.editor.tree.ClassesTree;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.classCreator.CreateNewClassWizard;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class ClassesPopupMenu extends MouseAdapter implements ActionListener {

	private ResourceBundle messages;
	
	private ComponentRepositoryPanel parent;

	private JPopupMenu menu;
	
	private JMenu newMenu;
	private JMenu editMenu;
	
	private JMenuItem createClassItem; 
	private JMenuItem deleteClassItem;
	private JMenuItem individualsEnumerationItem;
	private JMenuItem classPropertiesItem; 
	private JMenuItem classesUnionItem;
	private JMenuItem classesIntersectionItem;
	private JMenuItem classesComplementItem;
	private JMenuItem focusOnGraphItem;
	private JMenuItem showAxiomsItem;
	

	/**
	 * 
	 * @param parent
	 */
	public ClassesPopupMenu(ComponentRepositoryPanel parent) {
		this.messages = EditorUtil.getResourceBundle(ClassesPopupMenu.class);
		this.parent = parent;
		this.menu = new JPopupMenu();
		
		this.newMenu = new JMenu();
		this.editMenu = new JMenu();
		
		this.createClassItem = new JMenuItem();
		this.deleteClassItem = new JMenuItem();
		this.focusOnGraphItem = new JMenuItem();
		this.individualsEnumerationItem = new JMenuItem();
		this.classPropertiesItem = new JMenuItem();
		this.classesUnionItem = new JMenuItem();
		this.classesIntersectionItem = new JMenuItem();
		this.classesComplementItem = new JMenuItem();
		this.showAxiomsItem = new JMenuItem();
		
		initialize();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == focusOnGraphItem) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				parent.getParentFrame().getMainPanel().focusNodeOnGraph(selectedNode.getElementURI());
			}
		} else if(e.getSource() == createClassItem ) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			String selectedClassUri = new String();
			if(selectedNode != null) {
				selectedClassUri = selectedNode.getElementURI();			
			}
			CreateNewClassWizard wizard = new CreateNewClassWizard(parent.getParentFrame(), selectedClassUri);
			wizard.open();
		} else if(e.getSource() == deleteClassItem) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				String selectedClassUri = selectedNode.getElementURI();
				int result = JOptionPane.showConfirmDialog(parent.getParentFrame(), messages.getString("deleteClassDialog.text"), messages.getString("deleteClassDialog.title"), JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					deleteClass(selectedClassUri);
				} 
			}
		} else if (e.getSource() == showAxiomsItem) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				String selectedClassUri = selectedNode.getElementURI();
				Logic logic = Logic.getInstance();
				OWLClass currentClass =  logic.getDataFactory().getOWLClass(URI.create(selectedClassUri));
				
				Set<OWLAxiom> axioms = logic.getReferencedAxioms(currentClass);
				
				
				ShowClassAxiomsDialog dialog = new ShowClassAxiomsDialog(selectedClassUri, axioms);
				dialog.open();
			}
		} else if (e.getSource() == classPropertiesItem) {
			
		} else if (e.getSource() == classesUnionItem) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				String selectedClassUri = selectedNode.getElementURI();
				ClassesUnionDialog dialog = new ClassesUnionDialog(selectedClassUri);
				dialog.open();
			}
		} else if (e.getSource() == classesIntersectionItem) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				String selectedClassUri = selectedNode.getElementURI();
				ClassesIntersectionDialog dialog = new ClassesIntersectionDialog(selectedClassUri);
				dialog.open();
			}
		} else if (e.getSource() == classesComplementItem) {
			
		} else if (e.getSource() == individualsEnumerationItem) {
			BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				String selectedClassUri = selectedNode.getElementURI();
				IndividualsEnumerationDialog dialog = new IndividualsEnumerationDialog(selectedClassUri);
				dialog.open();
			}
		}
		
	}
	
	/**
	 * 
	 */
	private void initialize() {
		
		createClassItem.setText(messages.getString("createClassItem.text"));
		createClassItem.setActionCommand(CommandsConstans.CREATE_CLASS);
		createClassItem.addActionListener(this);
		
		deleteClassItem.setText(messages.getString("deleteClassItem.text"));
		deleteClassItem.setActionCommand(CommandsConstans.DELETE_CLASS);
		deleteClassItem.addActionListener(this);
		
		focusOnGraphItem.setText(messages.getString("focusOnGraphItem.text"));
		focusOnGraphItem.setActionCommand(CommandsConstans.FOCUS_ON_GRAPH);
		focusOnGraphItem.addActionListener(this);
		
		showAxiomsItem.setText(messages.getString("showAxiomsItem.text"));
		showAxiomsItem.setActionCommand(CommandsConstans.SHOW_AXIOMS);
		showAxiomsItem.addActionListener(this);
		
		classPropertiesItem.setText(messages.getString("classPropertiesItem.text"));
		classPropertiesItem.addActionListener(this);
		
		classesUnionItem.setText(messages.getString("classesUnionItem.text"));
		classesUnionItem.addActionListener(this);
		
		classesIntersectionItem.setText(messages.getString("classesIntersectionItem.text"));
		classesIntersectionItem.addActionListener(this);
		
		classesComplementItem.setText(messages.getString("classesComplementItem.text"));
		classesComplementItem.addActionListener(this);
		
		individualsEnumerationItem.setText(messages.getString("individualsEnumerationItem.text"));
		individualsEnumerationItem.addActionListener(this);
		
		newMenu.setText(messages.getString("newMenu.text"));
		newMenu.add(createClassItem);
		
		editMenu.setText(messages.getString("editMenu.text"));
		editMenu.add(classesUnionItem);
		editMenu.add(classesIntersectionItem);
		//editMenu.add(classesComplementItem);
		editMenu.add(individualsEnumerationItem);
		//editMenu.add(classPropertiesItem);
		
		menu.add(newMenu);
		menu.add(editMenu);
		menu.add(deleteClassItem);
		menu.addSeparator();
		menu.add(focusOnGraphItem);
		menu.add(showAxiomsItem);
		
	}
	
	private void deleteClass(String classUri) {
		Logic logic = Logic.getInstance();
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

		//stworzenie propozycji zmian
		changes = logic.createDeleteClassAxiom(logic.getLoadedOntologyURI(), classUri);
		
		try {
			logic.applyChanges(changes);
			
			MainPanel mainPanel = parent.getParentFrame().getMainPanel();
			String ontologyURI = logic.getLoadedOntologyURI();
			ClassesGraphModel graph = new ClassesGraphModel(logic.getOntology(ontologyURI));
			mainPanel.displayGraph(graph, logic.getOntology(ontologyURI));
			
			ClassesTree classesTree = parent.getParentFrame().getComponentsPanel().getClassesTree();
			classesTree.removeNode();
			
		} catch (OWLOntologyChangeException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
		if (selectedNode != null) {
			displayMenu(selectedNode, e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		BasicTreeNode selectedNode = parent.getClassesTree().getSelectedTreeNode();
		if (selectedNode != null) {
			displayMenu(selectedNode, e);
		}
	}

	private void displayMenu(BasicTreeNode selectedNode, MouseEvent e) {
		if (e.isPopupTrigger()) {
			if(selectedNode.getElementURI() == null || selectedNode.getElementURI().equals("")) {
				deleteClassItem.setEnabled(false);
				focusOnGraphItem.setEnabled(false);
				showAxiomsItem.setEnabled(false);
				editMenu.setEnabled(false);
			} else {
				deleteClassItem.setEnabled(true);
				focusOnGraphItem.setEnabled(true);
				showAxiomsItem.setEnabled(true);
				editMenu.setEnabled(true);
			}
				
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}


}
