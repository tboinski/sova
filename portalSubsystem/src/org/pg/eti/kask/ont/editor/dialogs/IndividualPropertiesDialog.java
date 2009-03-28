package org.pg.eti.kask.ont.editor.dialogs;

import java.net.URI;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultListModel;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLIndividualAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.RemoveAxiom;

public class IndividualPropertiesDialog extends GenericDialog {

	private static final long serialVersionUID = 8568926290322072269L;
	
	private ResourceBundle messages;
	
	private String selectedIndividualURI;
	
	private Logic logic;
	
	private DefaultListModel listModel;
	
	private OWLOntology loadedOntology;
	private OWLIndividual individual;
	private Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropsAssertionAxioms;
	private Map<OWLDataPropertyExpression, Set<OWLConstant>> dataPropsAssertionAxioms;
	
	public IndividualPropertiesDialog(String selectedIndividualURI) {
		this.messages = EditorUtil.getResourceBundle(IndividualPropertiesDialog.class);
		this.logic = Logic.getInstance();
		this.selectedIndividualURI = selectedIndividualURI;
		this.listModel = new DefaultListModel();
		super.setListModel(listModel);
		
		loadedOntology = logic.getOntology(logic.getLoadedOntologyURI());
		individual = logic.getDataFactory().getOWLIndividual(URI.create(selectedIndividualURI));
		objectPropsAssertionAxioms = individual.getObjectPropertyValues(loadedOntology);
		
		for(OWLObjectPropertyExpression key : objectPropsAssertionAxioms.keySet()) {
			if(!key.isAnonymous()) {
				OWLObjectProperty prop = key.asOWLObjectProperty();
				Set<OWLIndividual> inds = objectPropsAssertionAxioms.get(key);
				OWLIndividual rsIndividual = null;
				for(OWLIndividual ind: inds) {
					rsIndividual = ind;
				}
				OWLObjectPropertyAssertionAxiom axiom = logic.getDataFactory().getOWLObjectPropertyAssertionAxiom(individual, prop, rsIndividual);
				listModel.addElement(axiom);
			}
		}
		
		dataPropsAssertionAxioms = individual.getDataPropertyValues(loadedOntology);
		
		for(OWLDataPropertyExpression key : dataPropsAssertionAxioms.keySet()) {
			if(!key.isAnonymous()) {
				OWLDataProperty prop = key.asOWLDataProperty();
				Set<OWLConstant> inds = dataPropsAssertionAxioms.get(key);
				OWLConstant rsConstant = null;
				for(OWLConstant ind: inds) {
					rsConstant = ind;
				}
				OWLDataPropertyAssertionAxiom axiom = logic.getDataFactory().getOWLDataPropertyAssertionAxiom(individual, prop, rsConstant);
				listModel.addElement(axiom);
			}
		}
	}

	@Override
	public void doAdd() {
		PropertyAssertionDialog dialog = new PropertyAssertionDialog(selectedIndividualURI);
		dialog.open();		
		OWLIndividualAxiom axiom = dialog.getAxiom();
		if(axiom != null) {
			listModel.addElement(axiom);
			AddAxiom addAxiom = new AddAxiom(logic.getOntology(logic.getLoadedOntologyURI()), axiom);
			try {
				logic.applyChange(addAxiom);
			} catch (OWLOntologyChangeException e) {
				e.printStackTrace();
			}			
		}
	}

	@Override
	public void doCancel() {
		this.setVisible(false);
	}

	@Override
	public void doOk() {
		this.setVisible(false);
	}

	@Override
	public void doRemove() {
		int selectedIndex = super.getList().getSelectedIndex();
		if(selectedIndex != -1) {
			OWLIndividualAxiom axiom = (OWLIndividualAxiom)listModel.get(selectedIndex);
			RemoveAxiom removeAxiom = new RemoveAxiom(logic.getOntology(logic.getLoadedOntologyURI()), axiom);
			try {
				logic.applyChange(removeAxiom);
				listModel.remove(selectedIndex);
			} catch (OWLOntologyChangeException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public String getListLabel() {
		return messages.getString("individualPropertiesDialog.listLabel.text");
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public String getTitle() {
		return messages.getString("individualPropertiesDialog.title");
	}

}
