package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.tree.model.OntologyIndividualsTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLIndividualAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLTypedConstant;
import org.semanticweb.owl.vocab.XSDVocabulary;

public class PropertyAssertionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -6891676032012997457L;
	
	private ResourceBundle messages;
	
	private String individualURI;
	private boolean isObjectPropertyIndicator;
	
	private Logic logic;
	
	private OWLIndividualAxiom axiom;
	
	private JButton okButton;
	private JButton cancelButton;
	private JButton choosePropertyButton;
	private JButton chooseIndividualButton;
	private JTextField selectedPropertyTextField;
	private JTextField selectedIndividualTextField;
	private JTextField expressionTextField;
	private JLabel selectedIndividualLabel;
	private JLabel expressionLabel;
	
	public PropertyAssertionDialog(String individualURI) {
		this.messages = EditorUtil.getResourceBundle(PropertyAssertionDialog.class);
		this.logic = Logic.getInstance();
		this.individualURI = individualURI;
	}
	
	private void initialize() {
		JLabel individualLabel = new JLabel();
		individualLabel.setText(messages.getString("individualLabel.text"));
		
		JTextField individualTextField = new JTextField();
		individualTextField.setText(individualURI);
		individualTextField.setEnabled(false);
				
		JLabel propertyLabel = new JLabel();
		propertyLabel.setText(messages.getString("propertyLabel.text"));
		
		selectedPropertyTextField = new JTextField();
		selectedPropertyTextField.setEditable(false);
		
		choosePropertyButton = new JButton();
		choosePropertyButton.setText(messages.getString("choosePropertyButton.text"));
		choosePropertyButton.addActionListener(this);
		
		selectedIndividualLabel = new JLabel();
		selectedIndividualLabel.setText(messages.getString("selectedIndividualLabel.text"));
		selectedIndividualLabel.setVisible(false);
		
		selectedIndividualTextField = new JTextField();
		selectedIndividualTextField.setEditable(false);
		selectedIndividualTextField.setVisible(false);
		
		expressionLabel = new JLabel();
		expressionLabel.setText(messages.getString("expressionLabel.text"));
		expressionLabel.setVisible(false);
		
		expressionTextField = new JTextField();
		expressionTextField.setVisible(false);
		
		chooseIndividualButton = new JButton();
		chooseIndividualButton.setText(messages.getString("chooseIndividualButton.text"));
		chooseIndividualButton.addActionListener(this);
		chooseIndividualButton.setVisible(false);
		
		okButton = new JButton();
		okButton.setText(messages.getString("okButton.text"));
		okButton.addActionListener(this);
		
		cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancelButton.text"));
		cancelButton.addActionListener(this);
		
		JPanel tempPanel = new JPanel();
		
		GroupLayout layout = new GroupLayout(tempPanel);
		tempPanel.setLayout(layout);
		
		//layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(individualLabel)
						.addComponent(propertyLabel)
						.addComponent(selectedIndividualLabel)
						.addComponent(expressionLabel))
				.addGroup(layout.createParallelGroup()
						.addComponent(individualTextField)
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectedPropertyTextField)
								.addComponent(choosePropertyButton))
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectedIndividualTextField)
								.addComponent(chooseIndividualButton))
						.addComponent(expressionTextField)								
						));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(individualLabel)
						.addComponent(individualTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE))
				.addGroup(layout.createParallelGroup()
						.addComponent(propertyLabel)
						.addComponent(selectedPropertyTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE)
						.addComponent(choosePropertyButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(selectedIndividualLabel)
						.addComponent(selectedIndividualTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE)
						.addComponent(chooseIndividualButton))
				.addGroup(layout.createParallelGroup()
						.addComponent(expressionLabel)
						.addComponent(expressionTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE))
						
				);
		
		layout.linkSize(SwingConstants.HORIZONTAL, choosePropertyButton, chooseIndividualButton);
		
		JPanel mainPanel = new JPanel();
		
		GroupLayout layout1 = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout1);
		
		layout1.setAutoCreateContainerGaps(true);
		layout1.setAutoCreateGaps(true);
		
		layout1.setHorizontalGroup(layout1.createSequentialGroup()
				.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(tempPanel)
						.addGroup(layout1.createSequentialGroup()
								.addComponent(okButton)
								.addGap(15)
								.addComponent(cancelButton))));
		
		layout1.setVerticalGroup(layout1.createSequentialGroup()
				.addComponent(tempPanel)
				.addGap(30)
				.addGroup(layout1.createParallelGroup()
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout1.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("propertyAssertionDialog.title"));
		this.setSize(new Dimension(500,200));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			String selectedProp = selectedPropertyTextField.getText();
			OWLIndividual individual = logic.getDataFactory().getOWLIndividual(URI.create(individualURI));
			if(isObjectPropertyIndicator) {
				OWLObjectProperty objectProperty = logic.getDataFactory().getOWLObjectProperty(URI.create(selectedProp));
				OWLIndividual individual1 = logic.getDataFactory().getOWLIndividual(URI.create(selectedIndividualTextField.getText()));
				this.axiom = logic.getDataFactory().getOWLObjectPropertyAssertionAxiom(individual, objectProperty, individual1);
			} else {
				OWLDataProperty dataProperty = logic.getDataFactory().getOWLDataProperty(URI.create(selectedProp));
				OWLTypedConstant constant = logic.getDataFactory().getOWLTypedConstant(expressionTextField.getText(), logic.getDataFactory().getOWLDataType(XSDVocabulary.INTEGER.getURI()));
				this.axiom = logic.getDataFactory().getOWLDataPropertyAssertionAxiom(individual, dataProperty, constant);
			}
			this.setVisible(false);
		} else if(e.getSource() == cancelButton) {
			this.setVisible(false);
		} else if(e.getSource() == choosePropertyButton) {
			OntologyPropertiesTreeModel model = new OntologyPropertiesTreeModel(logic.getOntology(logic.getLoadedOntologyURI()), OntologyPropertiesTreeModel.ALL_PROPERTY_TREE);
			PropertySelectionDialog propertySelection = new PropertySelectionDialog(model);
			propertySelection.open();
			selectedIndividualTextField.setText("");
			expressionTextField.setText("");
			List<BasicTreeNode> properties = propertySelection.getSelectedProperties();
			if(properties != null && properties.size() == 1) {
				BasicTreeNode node = properties.get(0);
				if(node != null && !node.getElementURI().equals("")) {
					String type = node.getType();
					selectedPropertyTextField.setText(node.getElementURI());
					if(type.equals(OntologyPropertiesTreeModel.DATA_PROPERTY)) {
						isObjectPropertyIndicator = false;
						expressionLabel.setVisible(true);
						expressionTextField.setVisible(true);
						
						selectedIndividualLabel.setVisible(false);
						selectedIndividualTextField.setVisible(false);
						chooseIndividualButton.setVisible(false);
					} else if(type.equals(OntologyPropertiesTreeModel.OBJECT_PROPERTY)) {
						isObjectPropertyIndicator = true;
						expressionLabel.setVisible(false);
						expressionTextField.setVisible(false);
						
						selectedIndividualLabel.setVisible(true);
						selectedIndividualTextField.setVisible(true);
						chooseIndividualButton.setVisible(true);
					}
				}
			}
		} else if(e.getSource() == chooseIndividualButton) {
			OntologyIndividualsTreeModel model = new OntologyIndividualsTreeModel(logic.getOntology(logic.getLoadedOntologyURI()));
			IndividualSelectionDialog individualSelection = new IndividualSelectionDialog(model);
			individualSelection.open();
			
			List<BasicTreeNode> individuals= individualSelection.getSelectedIndividualsInfo();
			
			if(individuals != null && individuals.size() == 1) {
				BasicTreeNode node = individuals.get(0);
				if(node != null && !node.getElementURI().equals("")) {
					selectedIndividualTextField.setText(node.getElementURI());
				}
			}
		}

	}
	
	public void open() {
		initialize();
	}	

	public OWLIndividualAxiom getAxiom() {
		return axiom;
	}

}
