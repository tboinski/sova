package org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.dialogs.ClassSelectionDialog;
import org.pg.eti.kask.ont.editor.dialogs.DataRangeSelectionDialog;
import org.pg.eti.kask.ont.editor.list.model.BasicListElement;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator.model.DataPropertyRelationsPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLOntology;

public class DataPropertyRelationsPage extends AbstractPage implements ActionListener{
	
	private ResourceBundle messages;
	
	private JPanel mainPanel;
	
	private Logic logic;
	
	private JCheckBox functionalCheckbox;
	private JList domainList;
	private DefaultListModel domainListModel;
	private JList rangeList;
	private DefaultListModel rangeListModel;
	private JButton domainAddButton;
	private JButton domainRemoveButton;
	private JButton rangeAddButton;
	private JButton rangeRemoveButton;
	
	private DataPropertyRelationsPageModel result;
	
	public DataPropertyRelationsPage(AbstractWizard parentWizard) {
		super(parentWizard);
		
		this.messages = EditorUtil.getResourceBundle(DataPropertyRelationsPage.class);
		
		this.result = new DataPropertyRelationsPageModel();
		
		this.logic = Logic.getInstance();
		
		this.mainPanel = new JPanel();
		
		initialize();
	}
	
	private void initialize() {
		this.functionalCheckbox = new JCheckBox();
		this.functionalCheckbox.setText(messages.getString("functionalCheckbox.text"));
		
		JLabel domainLabel = new JLabel();
		domainLabel.setText(messages.getString("domainLabel.text"));
		
		this.domainListModel = new DefaultListModel();
		
		this.domainList = new JList(domainListModel);
		
		JScrollPane domainListScrollPane = new JScrollPane(domainList);
		
		this.domainAddButton = new JButton();
		this.domainAddButton.setText(messages.getString("domainAddButton.text"));
		this.domainAddButton.addActionListener(this);
		
		this.domainRemoveButton = new JButton();
		this.domainRemoveButton.setText(messages.getString("domainRemoveButton.text"));
		this.domainRemoveButton.addActionListener(this);
		
		JLabel rangeLabel = new JLabel();
		rangeLabel.setText(messages.getString("rangeLabel.text"));
		
		this.rangeListModel = new DefaultListModel();
		
		this.rangeList = new JList(rangeListModel);
		
		JScrollPane rangeListScrollPane = new JScrollPane(rangeList);
		
		this.rangeAddButton = new JButton();
		this.rangeAddButton.setText(messages.getString("rangeAddButton.text"));
		this.rangeAddButton.addActionListener(this);
		
		this.rangeRemoveButton = new JButton();
		this.rangeRemoveButton.setText(messages.getString("rangeRemoveButton.text"));
		this.rangeRemoveButton.addActionListener(this);
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()										
										.addComponent(functionalCheckbox)))
						.addComponent(domainLabel)
						.addGroup(layout.createSequentialGroup()
								.addComponent(domainListScrollPane)
								.addGroup(layout.createParallelGroup()
										.addComponent(domainAddButton)
										.addComponent(domainRemoveButton)))
						.addComponent(rangeLabel)
						.addGroup(layout.createSequentialGroup()
								.addComponent(rangeListScrollPane)
								.addGroup(layout.createParallelGroup()
										.addComponent(rangeAddButton)
										.addComponent(rangeRemoveButton)))));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				
				.addGroup(layout.createParallelGroup()
						.addComponent(functionalCheckbox))
				.addComponent(domainLabel)
				.addGroup(layout.createParallelGroup()
						.addComponent(domainListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addComponent(domainAddButton)
								.addComponent(domainRemoveButton)))
				.addComponent(rangeLabel)
				.addGroup(layout.createParallelGroup()
						.addComponent(rangeListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addComponent(rangeAddButton)
								.addComponent(rangeRemoveButton))));
		
		layout.linkSize(SwingConstants.HORIZONTAL, domainAddButton, domainRemoveButton, rangeAddButton, rangeRemoveButton);
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		result.setFunctionalProperty(functionalCheckbox.isSelected());
		
		List<String> domainUris = new ArrayList<String>();
		List<String> rangeUris = new ArrayList<String>();
		
		if(domainListModel.size() > 0) {
			for(int i=0; i < domainListModel.size(); i++) {
				BasicListElement element = (BasicListElement)domainListModel.get(i);
				domainUris.add(element.getElementURI());
			}
		}
		
		if(rangeListModel.size() > 0) {
			for(int i=0; i < rangeListModel.size(); i++) {
				BasicListElement element = (BasicListElement)rangeListModel.get(i);
				rangeUris.add(element.getElementURI());
			}
			
		}
		
		result.setDomainClassesUris(domainUris);
		result.setRangeUris(rangeUris);
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ontologyURI = logic.getLoadedOntologyURI();
		if(e.getSource() == domainAddButton) {
			OWLOntology ontology = logic.getOntology(ontologyURI);
			OntologyClassesTreeModel treeModel = new OntologyClassesTreeModel(ontology);
			ClassSelectionDialog dialog = new ClassSelectionDialog(treeModel);
			dialog.open();
			
			List<BasicTreeNode> selectedClasses = dialog.getSelectedClassesInfo();
			
			for(BasicTreeNode currentClass : selectedClasses) {
				if(currentClass.getElementURI() != null && !currentClass.getElementURI().equals("")) {
					BasicListElement elementToAdd = new BasicListElement();
					elementToAdd.setElementLabel(EditorUtil.getOwlEntityName(currentClass.getElementURI()));
					elementToAdd.setElementURI(currentClass.getElementURI());
					if(!domainListModel.contains(elementToAdd)) {
						domainListModel.addElement(elementToAdd);
					}
				}
			}
		} else if(e.getSource() == domainRemoveButton) {
			int selectedInd =  domainList.getSelectedIndex();
			domainListModel.remove(selectedInd);
		} else if(e.getSource() == rangeAddButton) {
			
			DataRangeSelectionDialog dialog = new DataRangeSelectionDialog();
			dialog.open();
			
			OWLDataType selectedDataType = dialog.getDataType();
			
			String dataTypeToAdd = EditorUtil.convertFromOWLDataType(selectedDataType);
			
			BasicListElement elementToAdd = new BasicListElement();
			elementToAdd.setElementLabel(dataTypeToAdd);
			elementToAdd.setElementURI(selectedDataType.getURI().toString());
			
			if(!rangeListModel.contains(elementToAdd)) {
				rangeListModel.addElement(elementToAdd);
			}
			
		} else if(e.getSource() == rangeRemoveButton) {
			int selectedInd =  rangeList.getSelectedIndex();
			rangeListModel.remove(selectedInd);
		}
	}

	public DataPropertyRelationsPageModel getResult() {
		return result;
	}

}
