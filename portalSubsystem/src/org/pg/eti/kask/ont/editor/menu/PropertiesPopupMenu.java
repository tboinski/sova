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
import org.pg.eti.kask.ont.editor.dialogs.DataPropertyRangeDialog;
import org.pg.eti.kask.ont.editor.dialogs.PropertyDomainDialog;
import org.pg.eti.kask.ont.editor.dialogs.ObjectPropertyRangeDialog;
import org.pg.eti.kask.ont.editor.dialogs.ShowPropertyAxiomDialog;
import org.pg.eti.kask.ont.editor.panels.ComponentRepositoryPanel;
import org.pg.eti.kask.ont.editor.tree.PropertiesTree;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator.CreateDataPropertyWizard;
import org.pg.eti.kask.ont.editor.wizard.objectPropertyCreator.CreateObjectPropertyWizard;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLProperty;
import org.semanticweb.owl.model.OWLPropertyExpression;

public class PropertiesPopupMenu extends MouseAdapter implements ActionListener {
	
	private ResourceBundle messages;
	
	//
	private ComponentRepositoryPanel parent;
	
	//
	private JPopupMenu menu;
	
	private JMenu newMenu;
	private JMenu editMenu;
	
	private JMenuItem createPropertyItem;
	private JMenuItem deletePropertyItem;
	private JMenuItem showAxiomsItem;
	
	private JMenuItem editDomainItem;
	private JMenuItem editRangeItem;
	
	/**
	 * 
	 * @param parent
	 */
	public PropertiesPopupMenu(ComponentRepositoryPanel parent) {
		this.messages = EditorUtil.getResourceBundle(PropertiesPopupMenu.class);
		this.parent = parent;
		this.menu = new JPopupMenu();
		
		this.newMenu = new JMenu();
		this.editMenu = new JMenu();
		
		this.createPropertyItem = new JMenuItem();
		this.deletePropertyItem = new JMenuItem();		
		this.showAxiomsItem = new JMenuItem();
		
		this.editDomainItem = new JMenuItem();
		this.editRangeItem = new JMenuItem();
		
		initialize();
	}
	

	/**
	 * 
	 */
	private void initialize() {	
		
		createPropertyItem.setText(messages.getString("createPropertyItem.text"));
		createPropertyItem.addActionListener(this);
		
		deletePropertyItem.setText(messages.getString("deletePropertyItem.text"));
		deletePropertyItem.addActionListener(this);
		
		showAxiomsItem.setText(messages.getString("showAxiomsItem.text"));
		showAxiomsItem.addActionListener(this);
		
		editDomainItem.setText(messages.getString("editDomainItem.text"));
		editDomainItem.addActionListener(this);
	
		editRangeItem.setText(messages.getString("editRangeItem.text"));
		editRangeItem.addActionListener(this);
		
		editMenu.add(editDomainItem);
		editMenu.add(editRangeItem);
		editMenu.setText(messages.getString("editMenu.text"));
		
		newMenu.add(createPropertyItem);
		newMenu.setText(messages.getString("newMenu.text"));
		
		menu.add(newMenu);
		menu.add(editMenu);	
		menu.add(deletePropertyItem);
		menu.addSeparator();
		menu.add(showAxiomsItem);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == createPropertyItem) {
			BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				if(selectedNode.getType().equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
					CreateObjectPropertyWizard wizard = new CreateObjectPropertyWizard(parent.getParentFrame(), selectedNode.getElementURI());
					wizard.open();
				} else if(selectedNode.getType().equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
					CreateDataPropertyWizard wizard = new CreateDataPropertyWizard(parent.getParentFrame(), selectedNode.getElementURI());
					wizard.open();
				}
					
			}
		} else if(event.getSource() == deletePropertyItem) {
			BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
			String selectedPropertyUri = selectedNode.getElementURI();
			
			int result = JOptionPane.showConfirmDialog(parent.getParentFrame(), messages.getString("deletePropertyDialog.text"), messages.getString("deletePropertyDialog.title"), JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				if(selectedNode.getType().equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
					deleteObjectProperty(selectedPropertyUri);
				} else if(selectedNode.getType().equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
					deleteDataProperty(selectedPropertyUri);
				}
			} 
		} else if(event.getSource() == showAxiomsItem) {
			BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				String selectedPropertyUri = selectedNode.getElementURI();
				Logic logic = Logic.getInstance();
				
				OWLProperty<?, ?> property = null;
				
				if(selectedNode.getType().equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
					property = logic.getDataFactory().getOWLDataProperty(URI.create(selectedPropertyUri));
				} else if(selectedNode.getType().equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
					property = logic.getDataFactory().getOWLObjectProperty(URI.create(selectedPropertyUri));
				}
				
				Set<OWLAxiom> axioms = logic.getReferencedAxioms(property); 
				
				ShowPropertyAxiomDialog dialog = new ShowPropertyAxiomDialog(selectedPropertyUri, axioms);
				dialog.open();
			}
		} else if(event.getSource() == editDomainItem) {
			BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				OWLPropertyExpression<?, ?> property = null;
				Logic logic = Logic.getInstance();
				if(selectedNode.getType().equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
					property = logic.getDataFactory().getOWLDataProperty(URI.create(selectedNode.getElementURI()));
				} else if(selectedNode.getType().equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
					property = logic.getDataFactory().getOWLObjectProperty(URI.create(selectedNode.getElementURI()));
				}
				
				PropertyDomainDialog dialog = new PropertyDomainDialog(property);
				dialog.open();
			}
		} else if(event.getSource() == editRangeItem) {
			BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
			if(selectedNode != null) {
				Logic logic = Logic.getInstance();
				if(selectedNode.getType().equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
					OWLDataProperty property = logic.getDataFactory().getOWLDataProperty(URI.create(selectedNode.getElementURI()));
					DataPropertyRangeDialog dialog = new DataPropertyRangeDialog(property);
					dialog.open();
				} else if(selectedNode.getType().equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
					OWLObjectProperty property = logic.getDataFactory().getOWLObjectProperty(URI.create(selectedNode.getElementURI()));
					ObjectPropertyRangeDialog dialog = new ObjectPropertyRangeDialog(property);
					dialog.open();
				}
				
				
			}
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
		if(selectedNode != null) {
			displayMenu(selectedNode, e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		BasicTreeNode selectedNode = parent.getPropertiesTree().getSelectedTreeNode();
		if (selectedNode != null) {
			displayMenu(selectedNode, e);
		}
	}

	private void displayMenu(BasicTreeNode selectedNode, MouseEvent e) {
		if (e.isPopupTrigger()) {
			if(selectedNode.getType().equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
				newMenu.setEnabled(true);
				if(selectedNode.getElementURI() != null && !selectedNode.getElementURI().equals("")) {
					deletePropertyItem.setEnabled(true);
					editMenu.setEnabled(true);
					showAxiomsItem.setEnabled(true);
				} else {
					deletePropertyItem.setEnabled(false);
					editMenu.setEnabled(false);
					showAxiomsItem.setEnabled(false);
				}
			} else if(selectedNode.getType().equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
				newMenu.setEnabled(true);
				if(selectedNode.getElementURI() != null && !selectedNode.getElementURI().equals("")) {
					deletePropertyItem.setEnabled(true);
					editMenu.setEnabled(true);
					showAxiomsItem.setEnabled(true);
				} else {
					deletePropertyItem.setEnabled(false);
					editMenu.setEnabled(false);
					showAxiomsItem.setEnabled(false);
				}
			} else {
				newMenu.setEnabled(false);
				deletePropertyItem.setEnabled(false);
				editMenu.setEnabled(false);
				showAxiomsItem.setEnabled(false);
			}
			
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void deleteObjectProperty(String objectPropertyURI) {
		Logic logic = Logic.getInstance();
		
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		
		OWLObjectProperty property = logic.getDataFactory().getOWLObjectProperty(URI.create(objectPropertyURI));
		
		changes = logic.createDeleteObjectPropertyAxiom(logic.getLoadedOntologyURI(), property);
		
		try {
			logic.applyChanges(changes);
			
			PropertiesTree propertiesTree = parent.getParentFrame().getComponentsPanel().getPropertiesTree();
			propertiesTree.removeNode();
		} catch (OWLOntologyChangeException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteDataProperty(String dataPropertyURI) {
		Logic logic = Logic.getInstance();
		
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		
		OWLDataProperty property = logic.getDataFactory().getOWLDataProperty(URI.create(dataPropertyURI));
		
		changes = logic.createDeleteDataPropertyAxiom(logic.getLoadedOntologyURI(), property);
		
		try {
			logic.applyChanges(changes);
			
			PropertiesTree propertiesTree = parent.getParentFrame().getComponentsPanel().getPropertiesTree();
			propertiesTree.removeNode();
		} catch (OWLOntologyChangeException e) {
			e.printStackTrace();
		}
	}

}
