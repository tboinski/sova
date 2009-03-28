package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;

public class ShowPropertyAxiomDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 7357143019372710316L;
	
	private ResourceBundle messages;
	
	private JList axiomsList;
	private DefaultListModel axiomsListModel;
	private JButton okButton;
	private JButton cancelButton;
	
	private Set<OWLAxiom> axioms;
	private String selectedPropertyURI;

	public ShowPropertyAxiomDialog(String selectedPropertyURI, Set<OWLAxiom> axioms) {
		this.messages = EditorUtil.getResourceBundle(ShowPropertyAxiomDialog.class);
		this.axioms = axioms; 
		this.selectedPropertyURI = selectedPropertyURI;
	}
	
	private void initialize() {
		JLabel referencedAxiomsLabel = new JLabel();
		referencedAxiomsLabel.setText(messages.getString("referencedAxiomsLabel.text"));
		
		axiomsListModel = new DefaultListModel();
		for(OWLAxiom axiom : axioms) {
			String element = new String();
			
			if(axiom instanceof OWLObjectSubPropertyAxiom) {
				OWLObjectSubPropertyAxiom subPropertyAxiom = (OWLObjectSubPropertyAxiom)axiom;
				OWLObjectPropertyExpression subPropExpression = subPropertyAxiom.getSubProperty();
				OWLObjectPropertyExpression superPropExpression = subPropertyAxiom.getSuperProperty();
				if(!subPropExpression.isAnonymous() && !superPropExpression.isAnonymous()) {
					OWLObjectProperty subProperty = subPropExpression.asOWLObjectProperty();
					OWLObjectProperty superProperty = superPropExpression.asOWLObjectProperty();
					element = EditorUtil.getOwlEntityName(subProperty);
					element += " " + messages.getString("isSubPropertyOf.text")+" ";
					element += EditorUtil.getOwlEntityName(superProperty);
				}
			} else if(axiom instanceof OWLDataSubPropertyAxiom) {
				OWLDataSubPropertyAxiom subPropertyAxiom = (OWLDataSubPropertyAxiom)axiom;
				OWLDataPropertyExpression subPropExpression = subPropertyAxiom.getSubProperty();
				OWLDataPropertyExpression superPropExpression = subPropertyAxiom.getSuperProperty();
				if(!subPropExpression.isAnonymous() && !superPropExpression.isAnonymous()) {
					OWLDataProperty subProperty = subPropExpression.asOWLDataProperty();
					OWLDataProperty superProperty = superPropExpression.asOWLDataProperty();
					element = EditorUtil.getOwlEntityName(subProperty);
					element += " " + messages.getString("isSubPropertyOf.text")+" ";
					element += EditorUtil.getOwlEntityName(superProperty);
				} else {
					element = subPropertyAxiom.toString();
				}
			} else if(axiom instanceof OWLTransitiveObjectPropertyAxiom) {
				OWLTransitiveObjectPropertyAxiom transitiveObjectPropertyAxiom = (OWLTransitiveObjectPropertyAxiom)axiom;
				if(!transitiveObjectPropertyAxiom.getProperty().isAnonymous()) {
					String propURI = transitiveObjectPropertyAxiom.getProperty().asOWLObjectProperty().getURI().toString();
					if(selectedPropertyURI.equals(propURI)) {
						element = EditorUtil.getOwlEntityName(selectedPropertyURI);
						element += " " + messages.getString("isTransitiveProperty.text")+" ";
					}
					
				}
			} else if(axiom instanceof OWLObjectPropertyDomainAxiom) {
				OWLObjectPropertyDomainAxiom domainAxiom = (OWLObjectPropertyDomainAxiom)axiom;
				OWLDescription desc = domainAxiom.getDomain();
				if(!desc.isAnonymous()) {
					element = EditorUtil.getOwlEntityName(selectedPropertyURI);
					element += " " + messages.getString("hasDomain.text")+" ";
					element += EditorUtil.getOwlEntityName(desc.asOWLClass());
				} else {
					element = domainAxiom.toString();
				}
			} else if(axiom instanceof OWLObjectPropertyRangeAxiom) {
				OWLObjectPropertyRangeAxiom rangeAxiom = (OWLObjectPropertyRangeAxiom)axiom;
				OWLDescription desc = rangeAxiom.getRange();
				if(!desc.isAnonymous()) {
					element = EditorUtil.getOwlEntityName(selectedPropertyURI);
					element += " " + messages.getString("hasRange.text")+" ";
					element += EditorUtil.getOwlEntityName(desc.asOWLClass());
				} else {
					element = rangeAxiom.toString();
				}
			} else if(axiom instanceof OWLFunctionalObjectPropertyAxiom) {
				element = EditorUtil.getOwlEntityName(selectedPropertyURI);
				element += " " + messages.getString("isFunctional.text");
			} else if(axiom instanceof OWLInverseObjectPropertiesAxiom ) {
				OWLInverseObjectPropertiesAxiom inversePropertiesAxiom = (OWLInverseObjectPropertiesAxiom)axiom;
				OWLObjectPropertyExpression firstPropExpression = inversePropertiesAxiom.getFirstProperty();
				OWLObjectPropertyExpression secondPropExpression = inversePropertiesAxiom.getSecondProperty();
				if(!firstPropExpression.isAnonymous() && !secondPropExpression.isAnonymous()) {
					OWLObjectProperty firstProperty = firstPropExpression.asOWLObjectProperty();
					OWLObjectProperty secondProperty = secondPropExpression.asOWLObjectProperty();
					element = EditorUtil.getOwlEntityName(firstProperty)+ ", "+ EditorUtil.getOwlEntityName(secondProperty);
					element += " " + messages.getString("isInverse.text");
				} else {
					element = inversePropertiesAxiom.toString();
				}
			}
			else {			
				element = axiom.toString();
			}
			axiomsListModel.addElement(element);
		}
		
		axiomsList = new JList(axiomsListModel);
		axiomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		axiomsList.setLayoutOrientation(JList.VERTICAL);
		axiomsList.setVisibleRowCount(-1);
		
		JScrollPane axiomsListScrollPane = new JScrollPane(axiomsList);
		
		this.okButton = new JButton();
		okButton.setText(messages.getString("showAxiomsDialog.okButton.text"));
		okButton.setActionCommand(CommandsConstans.OK_BUTTON);
		okButton.addActionListener(this);
		
		this.cancelButton = new JButton();
		cancelButton.setText(messages.getString("showAxiomsDialog.cancelButton.text"));
		cancelButton.setActionCommand(CommandsConstans.CANCEL_BUTTON);
		cancelButton.addActionListener(this);
		
		JPanel mainPanel = new JPanel();
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(referencedAxiomsLabel)
						.addComponent(axiomsListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addGap(420)
								.addComponent(okButton)
								.addComponent(cancelButton))));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(referencedAxiomsLabel)
				.addComponent(axiomsListScrollPane)
				.addGroup(layout.createParallelGroup()						
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("showAxiomsDialog.title"));
		this.setSize(new Dimension(600,400));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open(){
		initialize();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//na koniec zamkniecie okienka
		this.setVisible(false);
	}

}
