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
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.list.model.BasicListElement;
import org.pg.eti.kask.ont.editor.tree.model.OntologyIndividualsTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;

public class SameIndividualsDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -495048685578634952L;
	
	private ResourceBundle messages;
	
	private Logic logic;
	
	private String individualURI;
	
	private JPanel mainPanel;
	
	private JList sameIndividualsList;
	private DefaultListModel sameIndividualsListModel;
	private JButton addButton;
	private JButton removeButton;
	private JButton okButton;
	private JButton cancelButton;
	
	public SameIndividualsDialog(String individualURI) {
		this.messages = EditorUtil.getResourceBundle(SameIndividualsDialog.class);
		
		this.logic = Logic.getInstance();
		
		this.mainPanel = new JPanel();
		
		this.individualURI = individualURI;
	}
	
	private void initialize() {
		JLabel sameIndividualsLabel = new JLabel(messages.getString("sameIndividualsLabel.text"));
		
		sameIndividualsListModel = new DefaultListModel();
		Set<OWLSameIndividualsAxiom> axioms = logic.getSameIndividualsAxioms(individualURI);
		
		for(OWLSameIndividualsAxiom axiom : axioms) {
			Set<OWLIndividual> individuals = axiom.getIndividuals();
			for(OWLIndividual individual : individuals) {
				BasicListElement element = new BasicListElement(EditorUtil.getOwlEntityName(individual.getURI().toString()), individual.getURI().toString());
				if(!sameIndividualsListModel.contains(element)) {
					sameIndividualsListModel.addElement(element);
				}
			}
		}
		
		sameIndividualsList = new JList(sameIndividualsListModel);
		
		JScrollPane sameIndividualsScrollPane = new JScrollPane(sameIndividualsList);
		
		this.addButton = new JButton();
		addButton.setText(messages.getString("addButton.text"));
		addButton.addActionListener(this);
		
		this.removeButton = new JButton();
		removeButton.setText(messages.getString("removeButton.text"));
		removeButton.addActionListener(this);
		
		this.okButton = new JButton();
		okButton.setText(messages.getString("okButton.text"));
		okButton.addActionListener(this);
		
		this.cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancelButton.text"));
		cancelButton.addActionListener(this);
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(sameIndividualsLabel)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(sameIndividualsScrollPane)
										.addGroup(layout.createSequentialGroup()
												.addComponent(okButton)
												.addComponent(cancelButton)))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(addButton)
										.addComponent(removeButton)))
						));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(sameIndividualsLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(sameIndividualsScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addComponent(addButton)
								.addComponent(removeButton)))
				.addGroup(layout.createParallelGroup()
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton);
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("sameIndividuals.title"));
		this.setSize(new Dimension(400,350));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open() {
		initialize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			List<String> individualsURIs = new ArrayList<String>();
			List<OWLAxiomChange> changes = new ArrayList<OWLAxiomChange>();
			String ontologyURI  = logic.getLoadedOntologyURI();
			
			for(int i=0; i<sameIndividualsListModel.size(); i++) {
				BasicListElement element = (BasicListElement)sameIndividualsListModel.get(i);
				individualsURIs.add(element.getElementURI());
			}
			
			individualsURIs.add(this.individualURI);
			OWLAxiomChange change =logic.createSameIndividualsAxiom(ontologyURI, individualsURIs);
			changes.add(change);
			try {
				logic.applyChanges(changes);
			} catch (OWLOntologyChangeException e1) {
				e1.printStackTrace();
			}
			this.setVisible(false);
		} else if(e.getSource() == cancelButton) {
			this.setVisible(false);
		} else if(e.getSource() == addButton) {
			OWLOntology ontology = logic.getOntology(logic.getLoadedOntologyURI());
			OntologyIndividualsTreeModel model = new OntologyIndividualsTreeModel(ontology);
			
			IndividualSelectionDialog dialog = new IndividualSelectionDialog(model);
			dialog.open();
			
			List<BasicTreeNode> selectedIndividuals = dialog.getSelectedIndividualsInfo();
			for(BasicTreeNode individual : selectedIndividuals) {
				if(individual.getElementURI() != null && !individual.getElementURI().equals("")) {
					BasicListElement element = new BasicListElement(EditorUtil.getOwlEntityName(individual.getElementURI()), individual.getElementURI());
					if(!sameIndividualsListModel.contains(element)) {
						sameIndividualsListModel.addElement(element);
					}
				}
			}
		} else if(e.getSource() == removeButton) {
			int selectedInd =  sameIndividualsList.getSelectedIndex();
			if(selectedInd != -1) {
				sameIndividualsListModel.remove(selectedInd);
			}
		}
	}

}
