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
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;

public class ClassesIntersectionDialog extends GenericDialog {
	
	private static final long serialVersionUID = 7694361502796434192L;
	
	private ResourceBundle messages;
	
	private Logic logic;
	
	private DefaultListModel listModel;
	
	private String selectedClassURI;

	public ClassesIntersectionDialog(String selectedClassURI) {
		this.messages = EditorUtil.getResourceBundle(ClassesIntersectionDialog.class);
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
					if(desc instanceof OWLObjectIntersectionOf) {
						OWLObjectIntersectionOf intersectionAxiom = (OWLObjectIntersectionOf)desc;
						Set<OWLDescription> intersectedClasses = intersectionAxiom.getOperands();
						for(OWLDescription intersectedClass : intersectedClasses) {
							if(!intersectedClass.isAnonymous()) {
								BasicListElement elementToAdd = new BasicListElement();
								elementToAdd.setElementLabel(EditorUtil.getOwlEntityName(intersectedClass.asOWLClass().getURI().toString()));
								elementToAdd.setElementURI(intersectedClass.asOWLClass().getURI().toString());
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
		OntologyClassesTreeModel model = new OntologyClassesTreeModel(logic.getOntology(logic.getLoadedOntologyURI()));
		
		ClassSelectionDialog dialog = new ClassSelectionDialog(model);
		dialog.open();
		List<BasicTreeNode> selectedClasses = dialog.getSelectedClassesInfo();
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
		Set<OWLClass> intersectedClasses = new HashSet<OWLClass>();
		for(int i=0; i < listModel.size(); i++) {
			String classURI = ((BasicListElement)listModel.get(i)).getElementURI();
			intersectedClasses.add(logic.getDataFactory().getOWLClass(URI.create(classURI)));
		}
		
		OWLObjectIntersectionOf intersectionAxiom = logic.getDataFactory().getOWLObjectIntersectionOf(intersectedClasses);
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
		return messages.getString("classesIntersectionDialog.listLabel.text");
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public String getTitle() {
		return messages.getString("classesIntersectionDialog.title");
	}
	
}
