package org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.dialogs.PropertySelectionDialog;
import org.pg.eti.kask.ont.editor.table.AnnotationsTable;
import org.pg.eti.kask.ont.editor.table.model.AnnotationsTableModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator.model.BasicInformationsPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLAnnotation;

public class BasicInformationsPage extends AbstractPage implements ActionListener{
	
	private ResourceBundle messages;

	private Logic logic;
	
	private JPanel mainPanel;
	
	private JTextField propertyNameTextField;
	private JTextField finalPropertyUri;
	private JTextField superPropertyTextField;
	private AnnotationsTable annotationsTable;
	private AnnotationsTableModel annotationsTableModel;
	private JButton browseSuperPropertyButton;
	
	private String superPropertyURI;

	private BasicInformationsPageModel result;

	public BasicInformationsPage(AbstractWizard parentWizard, String superPropertyURI) {
		super(parentWizard);
		
		this.superPropertyURI = superPropertyURI;
		this.mainPanel = new JPanel();
		this.logic = Logic.getInstance();
		
		this.messages = EditorUtil.getResourceBundle(BasicInformationsPage.class);
		this.result = new BasicInformationsPageModel();
		
		initialize();
	}
	
	private void initialize() {
		//Stworzenie wszystkich komponentow
		JLabel propertyNameLabel = new JLabel(messages.getString("propertyNameLabel.text"));
		propertyNameTextField = new JTextField();
		propertyNameTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) { 
				updateFinalPropertyUriTextField(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalPropertyUriTextField(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalPropertyUriTextField(e);
			}
			
			private void updateFinalPropertyUriTextField(DocumentEvent e) {
				String newClassName =  propertyNameTextField.getText();
				String ontologyURI = logic.getLoadedOntologyURI();
				finalPropertyUri.setText(ontologyURI.concat("#").concat(newClassName));
			}
			
		});
		String ontologyURI = logic.getLoadedOntologyURI();
		
		JLabel propertyUriLabel = new JLabel(messages.getString("propertyUriLabel.text"));
		this.finalPropertyUri = new JTextField(ontologyURI.concat("#"));
		this.finalPropertyUri.setEditable(false);
		
		JLabel superPropertyLabel = new JLabel();
		superPropertyLabel.setText(messages.getString("superPropertyLabel.text"));
		
		this.superPropertyTextField = new JTextField();
		this.superPropertyTextField.setText(superPropertyURI);
		
		this.browseSuperPropertyButton = new JButton();
		this.browseSuperPropertyButton.setText(messages.getString("browseButton.text"));
		this.browseSuperPropertyButton.addActionListener(this);
		
		this.annotationsTableModel = new AnnotationsTableModel();
		
		this.annotationsTable = new AnnotationsTable(annotationsTableModel);
		this.annotationsTable.setPreferredSize(new Dimension(parentWizard.getSize().width, 200));
		this.annotationsTable.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(propertyNameLabel)
										.addComponent(propertyUriLabel)
										.addComponent(superPropertyLabel))
								.addGroup(layout.createParallelGroup()
										.addComponent(propertyNameTextField)
										.addComponent(finalPropertyUri)
										.addGroup(layout.createSequentialGroup()
												.addComponent(superPropertyTextField)
												.addComponent(browseSuperPropertyButton))))
						.addComponent(annotationsTable)
						));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(propertyNameLabel)
						.addComponent(propertyNameTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(propertyUriLabel)
						.addComponent(finalPropertyUri))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(superPropertyLabel)
						.addComponent(superPropertyTextField)
						.addComponent(browseSuperPropertyButton))				
				.addComponent(annotationsTable)
				);
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		String propertyName = propertyNameTextField.getText();
		if(propertyName == null || propertyName.trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("error1.body"), messages.getString("error1.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String propertyURI = finalPropertyUri.getText();
		String superPropertyURI = superPropertyTextField.getText();
		List<OWLAnnotation<?>> annotations =  annotationsTable.getAnnotationsToAdd();
		
		result.setPropertyURI(propertyURI);
		
		if(superPropertyURI != null && !superPropertyURI.trim().equals("")) {
			result.setSuperPropertyURI(superPropertyURI);
		}
		
		if(annotations != null && annotations.size() > 0) {
			result.setAnnotations(annotations);
		}
		
		
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ontologyURI = logic.getLoadedOntologyURI();		
		OntologyPropertiesTreeModel propertiesTree = new OntologyPropertiesTreeModel(logic.getOntology(ontologyURI), OntologyPropertiesTreeModel.OBJECT_PROPERTY_TREE);
		//otwarcie okna dialogowego w celu wybrania klasy
		PropertySelectionDialog dialog = new PropertySelectionDialog(propertiesTree);
		dialog.open();
		
		//wpisanie odpowiedniej nazwy klasy
		ArrayList<BasicTreeNode> propertiesInfo = dialog.getSelectedProperties();
		if(e.getSource() == browseSuperPropertyButton) {			
			if(propertiesInfo != null && propertiesInfo.size() > 0) {
				superPropertyTextField.setText(propertiesInfo.get(0).getElementURI());
			}
		} 
	}

	public BasicInformationsPageModel getResult() {
		return result;
	}

}
