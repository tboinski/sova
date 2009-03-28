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
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;

public class ShowClassAxiomsDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -2340394431626619213L;

	private ResourceBundle messages;
	
	private JList axiomsList;
	private DefaultListModel axiomsListModel;
	private JButton okButton;
	private JButton cancelButton;
	
	private Set<OWLAxiom> axioms;
	private String selectedClassURI;

	public ShowClassAxiomsDialog(String selectedClassURI, Set<OWLAxiom> axioms) {
		this.messages = EditorUtil.getResourceBundle(ShowClassAxiomsDialog.class);
		this.axioms = axioms; 
		this.selectedClassURI = selectedClassURI;
	}
	
	private void initialize() {
		JLabel referencedAxiomsLabel = new JLabel();
		referencedAxiomsLabel.setText(messages.getString("referencedAxiomsLabel.text"));
		
		axiomsListModel = new DefaultListModel();
		for(OWLAxiom axiom : axioms) {
			String element = new String();
			if(axiom instanceof OWLDisjointClassesAxiom) {
				OWLDisjointClassesAxiom disjointClassesAxiom = (OWLDisjointClassesAxiom)axiom;
				
				Set<OWLDescription> disjointClasses = disjointClassesAxiom.getDescriptions();
				for(OWLDescription desc : disjointClasses) {
					if(!desc.isAnonymous()) {
						OWLClass currentClass = desc.asOWLClass();
						if(!selectedClassURI.equals(currentClass.getURI().toString())) {
							element = EditorUtil.getOwlEntityName(selectedClassURI) + " " + messages.getString("isDisjointWith.text") + " ";
							element += EditorUtil.getOwlEntityName(currentClass.getURI().toString()) + " ";
						}
					} else {
						element = desc.toString(); 
					}
				}
			} else if(axiom instanceof OWLDisjointUnionAxiom) {
				OWLDisjointUnionAxiom disjontUnionAxiom = (OWLDisjointUnionAxiom)axiom;
				element = disjontUnionAxiom.toString();
			} else if(axiom instanceof OWLEquivalentClassesAxiom) {
				OWLEquivalentClassesAxiom equivalentClassesAxiom = (OWLEquivalentClassesAxiom)axiom;
				Set<OWLDescription> equivalentClasses = equivalentClassesAxiom.getDescriptions();
				for(OWLDescription description : equivalentClasses) {
					if(!description.isAnonymous()) {
						OWLClass currentClass = description.asOWLClass();
						if(!selectedClassURI.equals(currentClass.getURI().toString())) {
							element = EditorUtil.getOwlEntityName(selectedClassURI) + " " + messages.getString("isEquivalentTo.text") + " ";
							element += EditorUtil.getOwlEntityName(currentClass.getURI().toString()) + " ";
						}
					} else {
						element = description.toString();
					}
				}
			} else if(axiom instanceof OWLSubClassAxiom) {
				OWLSubClassAxiom subclassAxiom = (OWLSubClassAxiom)axiom;
				if(!subclassAxiom.getSuperClass().isAnonymous()) {
					OWLClass superClass = subclassAxiom.getSuperClass().asOWLClass();
					OWLClass subClass = subclassAxiom.getSubClass().asOWLClass();
					element = EditorUtil.getOwlEntityName(subClass) + " " + messages.getString("isSubClassOf.text") + " ";
					element += EditorUtil.getOwlEntityName(superClass); 
				} else {
					element = subclassAxiom.getSuperClass().toString();
				}
				
			} else {
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
		this.setVisible(false);
	}
}
