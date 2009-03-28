package org.pg.eti.kask.ont.editor.wizard.showTypes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.dialogs.ClassSelectionDialog;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.pg.eti.kask.ont.editor.wizard.showTypes.model.IndividualTypePageModel;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;

public class IndividualTypePage extends AbstractPage implements ActionListener {
	
	private ResourceBundle messages;
	
	private Logic logic;
	private DefaultListModel listModel; 
	private JList individualTypesList;
	private JPanel mainPanel;
	
	private OWLIndividual individual;
	
	private IndividualTypePageModel result;

	public IndividualTypePage(AbstractWizard parentWizard, Logic logic, String individualURI) {
		super(parentWizard);
		
		this.messages = EditorUtil.getResourceBundle(IndividualTypePage.class);
		this.logic = logic;
		this.mainPanel = new JPanel();
		
		this.individual = logic.getDataFactory().getOWLIndividual(URI.create(individualURI));
		this.result = new IndividualTypePageModel();
		
		initialize();
	}
	
	private void initialize() {
		JLabel individualTypesLabel = new JLabel();
		individualTypesLabel.setText(messages.getString("individualTypesLabel.text"));
		
		listModel = new DefaultListModel();
		String ontologyURI = logic.getLoadedOntologyURI();
		Set<OWLDescription> types = individual.getTypes(logic.getOntology(ontologyURI));
		
		//dodanie kolejnych klas, ktorych wystapieniem jest dany byt
		for(OWLDescription desc : types) {
			if(!desc.isAnonymous()) {
				OWLClass type = desc.asOWLClass();
				listModel.addElement(type.getURI());
			}
		}
		
		//lista z klasami rozlacznymi
		individualTypesList = new JList(listModel);
		individualTypesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		individualTypesList.setLayoutOrientation(JList.VERTICAL);
		individualTypesList.setVisibleRowCount(-1);
		
		JScrollPane listScrollPane = new JScrollPane(individualTypesList);
		listScrollPane.setPreferredSize(new Dimension(250, 180));
		
		JButton addClassesButton = new JButton();
		addClassesButton.setText(messages.getString("addClassesButton.text"));
		addClassesButton.setActionCommand(CommandsConstans.BROWSE_CLASSES_BUTTON);
		addClassesButton.addActionListener(this);
		
		JButton removeClassesButton = new JButton();
		removeClassesButton.setText(messages.getString("removeClassesButton.text"));
		removeClassesButton.setActionCommand(CommandsConstans.REMOVE_CLASS_BUTTON);
		removeClassesButton.addActionListener(this);
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(individualTypesLabel)
					.addGroup(layout.createSequentialGroup()
							.addComponent(listScrollPane)
							.addGroup(layout.createParallelGroup()
									.addComponent(addClassesButton)
									.addComponent(removeClassesButton)))));
		
		//teraz pionowa
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(individualTypesLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(listScrollPane, 0, 150, 150)
						.addGroup(layout.createSequentialGroup()
								.addComponent(addClassesButton)
								.addComponent(removeClassesButton)))
				.addComponent(addClassesButton));
		
		//przyciski removeClassesButton i addClassesButton maja ten sam rozmiar
		layout.linkSize(SwingConstants.HORIZONTAL, addClassesButton, removeClassesButton);
		
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		ArrayList<String> types= new ArrayList<String>();
		
		for(int i=0; i<listModel.getSize(); i++) {
			types.add(listModel.get(i).toString());
		}
		
		result.setIndividualTypes(types);
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.BROWSE_CLASSES_BUTTON)) {
			String ontologyURI = logic.getLoadedOntologyURI();
			OntologyClassesTreeModel classesTree = new OntologyClassesTreeModel(logic.getOntology(ontologyURI)); 
			ClassSelectionDialog dialog = new ClassSelectionDialog(classesTree);
			dialog.open();
			ArrayList<BasicTreeNode> result = dialog.getSelectedClassesInfo();
			if(result != null) {
				for(BasicTreeNode treeNode : result) {
					if(!listModel.contains(treeNode.getElementURI())) {
						listModel.addElement(treeNode.getElementURI());
					}
				}
			}
			
		} else if(e.getActionCommand().equals(CommandsConstans.REMOVE_CLASS_BUTTON)) {
			if(!individualTypesList.isSelectionEmpty()) {
				listModel.remove(individualTypesList.getSelectedIndex());
			}
		}		
	}

	public IndividualTypePageModel getResult() {
		return result;
	}

}
