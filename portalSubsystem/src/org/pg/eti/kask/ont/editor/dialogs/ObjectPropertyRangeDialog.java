package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.list.model.BasicListElement;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.RemoveAxiom;

public class ObjectPropertyRangeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 4707403728892989057L;
	
	private Logic logic;
	
	private ResourceBundle messages;
	
	private JList rangeClassesList;
	private DefaultListModel rangeClassesListModel;
	private JButton addButton;
	private JButton removeButton;
	private JButton okButton;
	private JButton cancelButton;
	
	private OWLObjectProperty selectedObjectProperty; 
	
	private List<OWLAxiomChange> changes;
	
	public ObjectPropertyRangeDialog(OWLObjectProperty selectedProperty) {
		this.messages = EditorUtil.getResourceBundle(ObjectPropertyRangeDialog.class);
		
		this.logic = Logic.getInstance();		
		this.selectedObjectProperty = selectedProperty;
		this.changes = new ArrayList<OWLAxiomChange>();
	}
	
	private void initialize() {
		JLabel rangeLabel = new JLabel();
		rangeLabel.setText(messages.getString("rangeLabel.text"));
		
		rangeClassesListModel = new DefaultListModel();
		
		OWLOntology ontology = logic.getOntology(logic.getLoadedOntologyURI());
		Set<OWLDescription> descriptions = selectedObjectProperty.getRanges(ontology);
		for(OWLDescription desc : descriptions) {
			if(!desc.isAnonymous()) {
				OWLClass domainClass = desc.asOWLClass();
				BasicListElement element = new BasicListElement(EditorUtil.getOwlEntityName(domainClass), domainClass.getURI().toString());
				rangeClassesListModel.addElement(element);
			} 
		}
		
		
		rangeClassesList = new JList(rangeClassesListModel);
		rangeClassesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rangeClassesList.setLayoutOrientation(JList.VERTICAL);
		rangeClassesList.setVisibleRowCount(-1);
		
		JScrollPane domainClassesListScrollPane = new JScrollPane(rangeClassesList);
		
		this.addButton = new JButton();
		addButton.setText(messages.getString("propertyRangeDialog.addButton.text"));
		addButton.addActionListener(this);
		
		this.removeButton = new JButton();
		removeButton.setText(messages.getString("propertyRangeDialog.removeButton.text"));
		removeButton.addActionListener(this);
		
		this.okButton = new JButton();
		okButton.setText(messages.getString("propertyRangeDialog.okButton.text"));
		okButton.addActionListener(this);
		
		this.cancelButton = new JButton();
		cancelButton.setText(messages.getString("propertyRangeDialog.cancelButton.text"));
		cancelButton.addActionListener(this);
		
		JPanel mainPanel = new JPanel();
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(rangeLabel)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(domainClassesListScrollPane)
										.addGroup(layout.createSequentialGroup()
												.addComponent(okButton)
												.addComponent(cancelButton)))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(addButton)
										.addComponent(removeButton)))
						));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(rangeLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(domainClassesListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addComponent(addButton)
								.addComponent(removeButton)))
				.addGroup(layout.createParallelGroup()
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		layout.linkSize(SwingConstants.HORIZONTAL, removeButton, addButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("propertyRangeDialog.title"));
		this.setSize(new Dimension(450,400));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open() {
		initialize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addButton) {			
			String ontologyURI = logic.getLoadedOntologyURI();
			OntologyClassesTreeModel model = new OntologyClassesTreeModel(logic.getOntology(ontologyURI));
			ClassSelectionDialog dialog = new ClassSelectionDialog(model);
			dialog.open();
			
			List<BasicTreeNode> selectedClasses =dialog.getSelectedClassesInfo();
			for(BasicTreeNode currentClass : selectedClasses) {
				if(currentClass.getElementURI() != null && !currentClass.getElementURI().equals("")) {
					BasicListElement elementToAdd = new BasicListElement();
					elementToAdd.setElementLabel(EditorUtil.getOwlEntityName(currentClass.getElementURI()));
					elementToAdd.setElementURI(currentClass.getElementURI());
					if(!rangeClassesListModel.contains(elementToAdd)) {
						rangeClassesListModel.addElement(elementToAdd);
						
						OWLAxiom axiom = null;
						axiom = logic.getObjectPropertyRangeAxiom(selectedObjectProperty.getURI().toString(), elementToAdd.getElementURI());
						changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
					}
				}
			}
		} else if(e.getSource() == removeButton) {
			int selectedInd =  rangeClassesList.getSelectedIndex();
			if(selectedInd != -1) {
				String ontologyURI = logic.getLoadedOntologyURI();
				OWLAxiom axiom = null;
				BasicListElement elementToRemove = (BasicListElement)rangeClassesListModel.get(selectedInd);
				axiom = logic.getObjectPropertyRangeAxiom(selectedObjectProperty.getURI().toString(), elementToRemove.getElementURI());
				changes.add(new RemoveAxiom(logic.getOntology(ontologyURI), axiom));
				rangeClassesListModel.remove(selectedInd);
			}
		} else if(e.getSource() == okButton) {
			try {
				if(changes != null &&changes.size() >0) {
					logic.applyChanges(changes);
				}
			} catch (OWLOntologyChangeException e1) {
				e1.printStackTrace();
			}
			this.setVisible(false);
		} else if(e.getSource() == cancelButton) {
			this.setVisible(false);
		}
	}

}
