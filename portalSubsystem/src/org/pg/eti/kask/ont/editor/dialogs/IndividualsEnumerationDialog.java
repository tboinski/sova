package org.pg.eti.kask.ont.editor.dialogs;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultListModel;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.list.model.BasicListElement;
import org.pg.eti.kask.ont.editor.tree.model.OntologyIndividualsTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectOneOf;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * 
 * @author Andrzej Jakowski
 */
public class IndividualsEnumerationDialog extends GenericDialog {
	
	private static final long serialVersionUID = 2442021789846674597L;

	private Logic logic;
	
	private ResourceBundle messages;
	
	private DefaultListModel listModel;
	
	private String selectedClassURI;
	
	public IndividualsEnumerationDialog(String selectedClassURI) {
		this.messages = EditorUtil.getResourceBundle(IndividualsEnumerationDialog.class);
		this.logic = Logic.getInstance();
		this.listModel = new DefaultListModel();
		this.selectedClassURI = selectedClassURI;
		super.setListModel(listModel);
		initialize();
	}

	private void initialize() {
		OWLClass currentClass = logic.getDataFactory().getOWLClass(URI.create(selectedClassURI));
		OWLOntology ontology = logic.getOntology(logic.getLoadedOntologyURI());
		Set<OWLClassAxiom> classAxioms = ontology.getAxioms(currentClass);
		
		for(OWLClassAxiom axiom : classAxioms) {
			if(axiom instanceof OWLEquivalentClassesAxiom) {
				OWLEquivalentClassesAxiom equivClassesAxiom = (OWLEquivalentClassesAxiom) axiom;
				Set<OWLDescription> descriptions = equivClassesAxiom.getDescriptions();
				for(OWLDescription desc : descriptions) {
					if(desc instanceof OWLObjectOneOf) {
						OWLObjectOneOf oneOfAxiom = (OWLObjectOneOf)desc;
						Set<OWLIndividual> individuals = oneOfAxiom.getIndividuals();
						for(OWLIndividual individual : individuals) {
							if(!individual.isAnonymous()) {
								BasicListElement elementToAdd = new BasicListElement();
								elementToAdd.setElementLabel(EditorUtil.getOwlEntityName(individual.getURI().toString()));
								elementToAdd.setElementURI(individual.getURI().toString());
								listModel.addElement(elementToAdd);
							}
						}
					}
				}
				
			}
		}
	}
	
	@Override
	public void doAdd() {
		OntologyIndividualsTreeModel model = new OntologyIndividualsTreeModel(logic.getOntology(logic.getLoadedOntologyURI()));
		
		IndividualSelectionDialog dialog = new IndividualSelectionDialog(model);
		dialog.open();
		List<BasicTreeNode> selectedClasses = dialog.getSelectedIndividualsInfo();
		for(BasicTreeNode currentClass : selectedClasses) {
			BasicListElement elementToAdd = new BasicListElement();
			elementToAdd.setElementLabel(currentClass.getElementLabel());
			elementToAdd.setElementURI(currentClass.getElementURI());
			if(!listModel.contains(elementToAdd)) {
				listModel.addElement(elementToAdd);
			}
		}
	}

	@Override
	public void doCancel() {
		this.setVisible(false);
	}

	@Override
	public void doOk() {
		List<OWLAxiomChange> changes = new ArrayList<OWLAxiomChange>();
		Set<OWLIndividual> indivudalsEnumeration = new HashSet<OWLIndividual>();
		for(int i=0; i < listModel.size(); i++) {
			String individualURI = ((BasicListElement)listModel.get(i)).getElementURI();
			indivudalsEnumeration.add(logic.getDataFactory().getOWLIndividual(URI.create(individualURI)));
		}
		
		OWLObjectOneOf intersectionAxiom = logic.getDataFactory().getOWLObjectOneOf(indivudalsEnumeration);
		OWLClass selectedClass = logic.getDataFactory().getOWLClass(URI.create(selectedClassURI));
		
		OWLAxiomChange change = logic.createEquivalentClassesAxiom(selectedClass, intersectionAxiom);
		changes.add(change);
		
		try {
			logic.applyChanges(changes);
		} catch (OWLOntologyChangeException e) {
			e.printStackTrace();
		}
		
		this.setVisible(false);
	}

	@Override
	public void doRemove() {
		int selectedIndex = super.getList().getSelectedIndex();
		if(selectedIndex != -1) {
			listModel.remove(selectedIndex);
		}
	}

	@Override
	public String getListLabel() {
		return messages.getString("individualsEnumerationDialog.listLabel.text");
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public String getTitle() {
		return messages.getString("individualsEnumerationDialog.title");
	}
	
	

}
