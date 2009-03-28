package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.RemoveAxiom;
import org.semanticweb.owl.vocab.XSDVocabulary;

public class DataPropertyRangeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 3830071366100701805L;
	
	private Logic logic;
	
	private ResourceBundle messages;
	
	private JComboBox rangeDataTypeComboBox;
	private DefaultComboBoxModel rangeDataTypeComboBoxModel;
	private JButton okButton;
	private JButton cancelButton;
	
	private OWLDataProperty selectedDataProperty; 
	
	private String setDataRange;
	
	private List<OWLAxiomChange> changes;
	
	public DataPropertyRangeDialog(OWLDataProperty selectedProperty) {
		this.messages = EditorUtil.getResourceBundle(DataPropertyRangeDialog.class);
		
		this.logic = Logic.getInstance();		
		this.selectedDataProperty = selectedProperty;
		this.changes = new ArrayList<OWLAxiomChange>();
		this.setDataRange = new String();
	}
	
	private void initialize() {
		JLabel rangeLabel = new JLabel();
		rangeLabel.setText(messages.getString("rangeLabel.text"));
		
		rangeDataTypeComboBoxModel = new DefaultComboBoxModel();
		rangeDataTypeComboBoxModel.addElement("");
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_INTEGER);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_BOOLEAN);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_DATE);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_DATETIME);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_DOUBLE);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_FLOAT);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_STRING);
		rangeDataTypeComboBoxModel.addElement(Constants.XSD_TIME);
		
		rangeDataTypeComboBox = new JComboBox(rangeDataTypeComboBoxModel);
		
		OWLOntology ontology = logic.getOntology(logic.getLoadedOntologyURI());
		Set<OWLDataRange> ranges = selectedDataProperty.getRanges(ontology);
		for(OWLDataRange range : ranges) {
			if(range instanceof OWLDataType) {
				OWLDataType dataTypeRange = (OWLDataType)range;
				URI uri = dataTypeRange.getURI();			
				
				if(uri.equals(XSDVocabulary.INT.getURI()) || uri.equals(XSDVocabulary.INTEGER.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_INTEGER);
					setDataRange = Constants.XSD_INTEGER;
				} else if(uri.equals(XSDVocabulary.BOOLEAN.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_BOOLEAN);
					setDataRange = Constants.XSD_BOOLEAN;
				} else if(uri.equals(XSDVocabulary.DATE.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_DATE);
					setDataRange = Constants.XSD_DATE;
				} else if(uri.equals(XSDVocabulary.DATE_TIME.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_DATETIME);
					setDataRange = Constants.XSD_DATETIME;
				} else if(uri.equals(XSDVocabulary.DOUBLE.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_DOUBLE);
					setDataRange = Constants.XSD_DOUBLE;
				} else if(uri.equals(XSDVocabulary.FLOAT.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_FLOAT);
					setDataRange = Constants.XSD_FLOAT;
				} else if(uri.equals(XSDVocabulary.STRING.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_STRING);
					setDataRange = Constants.XSD_STRING;
				} else if(uri.equals(XSDVocabulary.TIME.getURI()) ) {
					rangeDataTypeComboBox.setSelectedItem(Constants.XSD_TIME);
					setDataRange = Constants.XSD_TIME;
				}
			}
		}
		
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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(rangeLabel)
								.addGap(10)
								.addComponent(rangeDataTypeComboBox))
						.addGroup(layout.createSequentialGroup()
								.addComponent(okButton)
								.addGap(15)
								.addComponent(cancelButton))));			
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()							
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(rangeLabel)
						.addComponent(rangeDataTypeComboBox))
				.addGap(30)
				.addGroup(layout.createParallelGroup()
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("propertyRangeDialog.title"));
		this.setSize(new Dimension(250, 120));
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
			String chosenDataRange = (String)rangeDataTypeComboBox.getSelectedItem();
			String ontologyURI = logic.getLoadedOntologyURI();
			
			if(setDataRange.equals("")) {
				if(!setDataRange.equals(chosenDataRange)) {
					OWLDataType dataType = null;
					if(chosenDataRange.equals(Constants.XSD_INTEGER)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.INTEGER.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_BOOLEAN)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.BOOLEAN.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_DATE)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_DATETIME)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE_TIME.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_DOUBLE)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DOUBLE.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_FLOAT)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.FLOAT.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_STRING)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.STRING.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_TIME)) {
						dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.TIME.getURI());
					}
					if(dataType != null) {
						OWLAxiom axiom = logic.getDataFactory().getOWLDataPropertyRangeAxiom(selectedDataProperty, dataType);
						changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
					}
				}
			} else {
				if(!setDataRange.equals(chosenDataRange)) {
					OWLDataType setDataType = null;
					if(setDataRange.equals(Constants.XSD_INTEGER)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.INTEGER.getURI());
					} else if(setDataRange.equals(Constants.XSD_BOOLEAN)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.BOOLEAN.getURI());
					} else if(setDataRange.equals(Constants.XSD_DATE)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE.getURI());
					} else if(setDataRange.equals(Constants.XSD_DATETIME)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE_TIME.getURI());
					} else if(setDataRange.equals(Constants.XSD_DOUBLE)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DOUBLE.getURI());
					} else if(setDataRange.equals(Constants.XSD_FLOAT)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.FLOAT.getURI());
					} else if(setDataRange.equals(Constants.XSD_STRING)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.STRING.getURI());
					} else if(setDataRange.equals(Constants.XSD_TIME)) {
						setDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.TIME.getURI());
					}
					if(setDataType != null) {
						OWLAxiom axiom = logic.getDataFactory().getOWLDataPropertyRangeAxiom(selectedDataProperty, setDataType);
						changes.add(new RemoveAxiom(logic.getOntology(ontologyURI), axiom));
					}
					
					OWLDataType chosenDataType = null;
					if(chosenDataRange.equals(Constants.XSD_INTEGER)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.INTEGER.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_BOOLEAN)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.BOOLEAN.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_DATE)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_DATETIME)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE_TIME.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_DOUBLE)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DOUBLE.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_FLOAT)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.FLOAT.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_STRING)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.STRING.getURI());
					} else if(chosenDataRange.equals(Constants.XSD_TIME)) {
						chosenDataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.TIME.getURI());
					}
					if(chosenDataType != null) {
						OWLAxiom axiom = logic.getDataFactory().getOWLDataPropertyRangeAxiom(selectedDataProperty, chosenDataType);
						changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
					}
				}
			}
			
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
