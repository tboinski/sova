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
import org.pg.eti.kask.ont.editor.dialogs.DifferentIndividualsDialog;
import org.pg.eti.kask.ont.editor.dialogs.IndividualPropertiesDialog;
import org.pg.eti.kask.ont.editor.dialogs.SameIndividualsDialog;
import org.pg.eti.kask.ont.editor.dialogs.ShowIndividualAxiomsDialog;
import org.pg.eti.kask.ont.editor.panels.ComponentRepositoryPanel;
import org.pg.eti.kask.ont.editor.tree.IndividualsTree;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.individualCreator.CreateNewIndividualWizard;
import org.pg.eti.kask.ont.editor.wizard.showTypes.ShowTypesWizard;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * 
 * @author Andrzej Jakowski
 */
public class IndividualsPopupMenu extends MouseAdapter implements ActionListener {
	
	private ResourceBundle messages;

	//
	private ComponentRepositoryPanel parent;

	//
	private JPopupMenu menu;
	
	private JMenu addMenu;
	
	private JMenu editMenu;
	
	private JMenuItem showTypesItem;
	private JMenuItem newIndividualItem;
	private JMenuItem deleteIndividualItem;
	private JMenuItem showAxiomsItem;
	private JMenuItem differentIndividualsItem;
	private JMenuItem sameIndividualsItem;
	private JMenuItem individualPropertiesItem;

	/**
	 * 
	 * @param parent
	 */
	public IndividualsPopupMenu(ComponentRepositoryPanel parent) {
		this.messages = EditorUtil.getResourceBundle(IndividualsPopupMenu.class);
		this.parent = parent;
		this.menu = new JPopupMenu();
		
		this.showTypesItem = new JMenuItem();
		this.newIndividualItem = new JMenuItem();
		this.deleteIndividualItem = new JMenuItem();
		this.showAxiomsItem = new JMenuItem();
		this.differentIndividualsItem = new JMenuItem();
		this.sameIndividualsItem = new JMenuItem();
		this.individualPropertiesItem = new JMenuItem();
		
		this.addMenu = new JMenu();
		this.editMenu = new JMenu();
		
		initialize();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
		if (selectedNode != null) {
			displayMenu(selectedNode, e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
		if (selectedNode != null) {
			displayMenu(selectedNode, e);
		}
	}

	private void displayMenu(BasicTreeNode selectedNode, MouseEvent e) {
		if (e.isPopupTrigger()) {
			if(selectedNode.getElementURI() == null || selectedNode.getElementURI().equals("")) {
				newIndividualItem.setEnabled(true);
				deleteIndividualItem.setEnabled(false);
				showAxiomsItem.setEnabled(false);
				showTypesItem.setEnabled(false);
				editMenu.setEnabled(false);				
			} else {
				newIndividualItem.setEnabled(false);
				deleteIndividualItem.setEnabled(true);
				showAxiomsItem.setEnabled(true);
				showTypesItem.setEnabled(true);
				editMenu.setEnabled(true);
			}
				
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == showTypesItem) {
			BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
			
			ShowTypesWizard wizard = new ShowTypesWizard(selectedNode.getElementURI());
			wizard.open();
			
		} else if(e.getSource() == newIndividualItem) {
			
			CreateNewIndividualWizard wizard = new CreateNewIndividualWizard(parent.getParentFrame());
			wizard.open();
			
		} else if(e.getSource() == deleteIndividualItem) {
			BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
			String selectedIndividualUri = selectedNode.getElementURI();
			
			int result = JOptionPane.showConfirmDialog(parent.getParentFrame(), messages.getString("deleteIndividualDialog.text"), messages.getString("deleteIndividualDialog.title"), JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				deleteIndividual(selectedIndividualUri);
			} 
		} else if(e.getSource() == showAxiomsItem) {
			BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
			String selectedIndividualUri = selectedNode.getElementURI();
			Logic logic = Logic.getInstance();
			
			Set<OWLAxiom> axioms = logic.getReferencedAxioms(logic.getDataFactory().getOWLIndividual(URI.create(selectedIndividualUri)));
			
			ShowIndividualAxiomsDialog dialog = new ShowIndividualAxiomsDialog(selectedIndividualUri, axioms);
			dialog.open();
		} else if(e.getSource() == sameIndividualsItem) {
			BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
			String selectedIndividualUri = selectedNode.getElementURI();
			SameIndividualsDialog dialog = new SameIndividualsDialog(selectedIndividualUri);
			dialog.open();
		} else if(e.getSource() == differentIndividualsItem) {
			BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
			String selectedIndividualUri = selectedNode.getElementURI();
			DifferentIndividualsDialog dialog = new DifferentIndividualsDialog(selectedIndividualUri);
			dialog.open();
		} else if(e.getSource() == individualPropertiesItem) {
			BasicTreeNode selectedNode = parent.getIndividualsTree().getSelectedTreeNode();
			String selectedIndividualUri = selectedNode.getElementURI();
			IndividualPropertiesDialog dialog = new IndividualPropertiesDialog(selectedIndividualUri);
			dialog.open();
		}
			
	}
	
	/**
	 * 
	 */
	private void initialize() {
		showTypesItem.setText(messages.getString("showTypesItem.text"));		
		showTypesItem.addActionListener(this);
		
		differentIndividualsItem.setText(messages.getString("differentIndividualsItem.text"));		
		differentIndividualsItem.addActionListener(this);
		
		sameIndividualsItem.setText(messages.getString("sameIndividualsItem.text"));		
		sameIndividualsItem.addActionListener(this);
		
		newIndividualItem.setText(messages.getString("newIndividualItem.text"));
		newIndividualItem.addActionListener(this);
		
		deleteIndividualItem.setText(messages.getString("deleteIndividualItem.text"));
		deleteIndividualItem.addActionListener(this);	
		
		individualPropertiesItem.setText(messages.getString("individualPropertiesItem.text"));		
		individualPropertiesItem.addActionListener(this);
		
		showAxiomsItem.setText(messages.getString("showAxiomsItem.text"));
		showAxiomsItem.addActionListener(this);
		
		addMenu.add(newIndividualItem);
		addMenu.setText(messages.getString("newMenu.text"));
				
		editMenu.add(showTypesItem);
		editMenu.add(sameIndividualsItem);
		editMenu.add(differentIndividualsItem);
		editMenu.add(individualPropertiesItem);
		editMenu.setText(messages.getString("editMenu.text"));
		
		menu.add(addMenu);
		menu.add(editMenu);
		menu.add(deleteIndividualItem);
		menu.addSeparator();
		menu.add(showAxiomsItem);
	}
	
	
	private void deleteIndividual(String individualURI) {
		Logic logic = Logic.getInstance();
		
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		
		changes = logic.createDeleteIndividualAxiom(logic.getLoadedOntologyURI(), individualURI);
		
		try {
			logic.applyChanges(changes);
			
			IndividualsTree individualsTree = parent.getParentFrame().getComponentsPanel().getIndividualsTree();
			
			individualsTree.removeNode();
		} catch (OWLOntologyChangeException e) {
			e.printStackTrace();
		}
	}

}
