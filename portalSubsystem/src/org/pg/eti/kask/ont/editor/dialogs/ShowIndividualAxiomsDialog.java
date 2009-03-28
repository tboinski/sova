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
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;

public class ShowIndividualAxiomsDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = -2151522393024683320L;
	
	private ResourceBundle messages;
	
	private JList axiomsList;
	private DefaultListModel axiomsListModel;
	private JButton okButton;
	private JButton cancelButton;
	
	private Set<OWLAxiom> axioms;
	private String selectedIndividualURI;

	public ShowIndividualAxiomsDialog(String selectedIndividualURI, Set<OWLAxiom> axioms) {
		this.messages = EditorUtil.getResourceBundle(ShowIndividualAxiomsDialog.class);
		this.axioms = axioms; 
		this.selectedIndividualURI = selectedIndividualURI;
	}
	
	private void initialize() {
		JLabel referencedAxiomsLabel = new JLabel();
		referencedAxiomsLabel.setText(messages.getString("referencedAxiomsLabel.text"));
		
		axiomsListModel = new DefaultListModel();
		for(OWLAxiom axiom : axioms) {
			String element = new String();
			if(axiom instanceof OWLClassAssertionAxiom) {
				element = EditorUtil.getOwlEntityName(((OWLClassAssertionAxiom)axiom).getIndividual());
				element += " " + messages.getString("isInstanceOf.text")+ " ";
				element += EditorUtil.getOwlEntityName(((OWLClassAssertionAxiom)axiom).getDescription().asOWLClass());;
			} else if(axiom instanceof OWLDifferentIndividualsAxiom) {
				element = EditorUtil.getOwlEntityName(selectedIndividualURI);
				element += " " + messages.getString("differentFrom.text")+ " ";
				Set<OWLIndividual> differentIndividuals = ((OWLDifferentIndividualsAxiom)axiom).getIndividuals();
				for(OWLIndividual ind : differentIndividuals) {
					element += EditorUtil.getOwlEntityName(ind.getURI().toString()) + " ";
				}
			} else if(axiom instanceof OWLSameIndividualsAxiom) {
				element = EditorUtil.getOwlEntityName(selectedIndividualURI);
				element += " " + messages.getString("sameAs.text")+ " ";
				Set<OWLIndividual> sameIndividuals = ((OWLSameIndividualsAxiom)axiom).getIndividuals();
				for(OWLIndividual ind : sameIndividuals) {
					element += EditorUtil.getOwlEntityName(ind.getURI().toString()) + " ";
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
		//na koniec zamkniecie okienka
		this.setVisible(false);
	}
}
