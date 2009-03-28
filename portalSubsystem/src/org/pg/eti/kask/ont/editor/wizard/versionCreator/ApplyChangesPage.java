package org.pg.eti.kask.ont.editor.wizard.versionCreator;

import java.awt.Component;
import java.awt.Dimension;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.OWLPropositionData;
import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.table.model.OntologyChangesTableModel;
import org.pg.eti.kask.ont.editor.table.model.OntologyProposition;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.pg.eti.kask.ont.editor.wizard.versionCreator.model.ApplyChangesPageModel;
import org.pg.eti.kask.ont.lib.AddOWLAxiom;
import org.pg.eti.kask.ont.lib.Change;
import org.pg.eti.kask.ont.lib.DeleteOWLAxiom;

import prefuse.util.FontLib;

public class ApplyChangesPage extends AbstractPage {
	
	private ResourceBundle messages;
	private Logic logic;
	
	private JPanel mainPanel;
	private JLabel ontologyURI;
	private JComboBox newVersionsComboBox;
	private JTable propositionsTable;
	private OntologyChangesTableModel propositionsTableModel;
	private JList changesList;
	private DefaultListModel changesModel;
	private JTextArea versionDescriptionTextArea;
	
	private List<OntologyProposition> propositions;
	private List<Integer> propsToAdd;
	private List<Integer> propsToRemove;
	private VersionedURI versionedURI;
	
	private ApplyChangesPageModel result;

	public ApplyChangesPage(AbstractWizard parentWizard) {
		super(parentWizard);
		
		this.messages = EditorUtil.getResourceBundle(ApplyChangesPage.class);
		this.logic = Logic.getInstance();
		this.mainPanel = new JPanel();
		
		this.result = new ApplyChangesPageModel();	
		
		
		this.propsToRemove = new ArrayList<Integer>();
		this.propsToAdd = new ArrayList<Integer>();
		this.versionedURI = new VersionedURI();
		this.versionedURI.setBaseURI(new BaseURI(""));
		this.versionedURI.setOntologyVersion(0);
		this.versionedURI.setOntologySubVersion(0);
		
		initialize();
	}
	
	public void setOntologyURI(VersionedURI ontologyVersionedURI) {
		if(!versionedURI.getBaseURI().getURIAsString().equals(ontologyVersionedURI.getBaseURI().getURIAsString())
				||versionedURI.getOntologySubversion() != ontologyVersionedURI.getOntologySubversion()
				||versionedURI.getOntologyVersion() != ontologyVersionedURI.getOntologyVersion() ) {
			versionedURI = ontologyVersionedURI;
			this.result.setOntologyURI(ontologyVersionedURI);
			ontologyURI.setText(ontologyVersionedURI.getBaseURI().getURIAsString()+";"+ontologyVersionedURI.getOntologyVersion()+";"+ontologyVersionedURI.getOntologySubversion());
			
			this.propositions = logic.getPropositions(ontologyVersionedURI);	
			this.propositionsTableModel = new OntologyChangesTableModel(propositions);		
			propositionsTable.setModel(propositionsTableModel);
			propositionsTable.getColumnModel().getColumn(0).setMaxWidth(50);
		}
	}
	
	private void initialize() {
		
		JLabel ontologyURILabel = new JLabel();
		ontologyURILabel.setText(messages.getString("ontologyURILabel.text"));
		
		ontologyURI = new JLabel();
		
		
		JLabel newVersionLabel = new JLabel();
		newVersionLabel.setText(messages.getString("newVersionLabel.text"));
		
		String[] versions = new String[]{messages.getString("subVersion.text"), messages.getString("version.text")};
		newVersionsComboBox = new JComboBox(versions);
		newVersionsComboBox.setSelectedIndex(0);
		newVersionsComboBox.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
		
		
		JLabel propositionsLabel = new JLabel();
		propositionsLabel.setText(messages.getString("propositionsLabel.text"));
		
		propositionsTable = new JTable();
		propositionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		propositionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if(!lsm.isSelectionEmpty()) {
					int propositionTableSelectedRow = propositionsTable.getSelectedRow();
					Integer propositionId = (Integer)propositionsTableModel.getValueAt(propositionTableSelectedRow, 0);
					OWLPropositionData propositionData = logic.getPropositionData(propositionId, URI.create(result.getOntologyURI().getBaseURI().getBaseURI().getURIAsString()));

					changesModel.removeAllElements();
					for(Change c: propositionData.getChanges()) {
						if(c instanceof AddOWLAxiom) {
							changesModel.addElement(messages.getString("addition.text")+" "+c.getOWLAxiom());
						} else if(c instanceof DeleteOWLAxiom) {
							changesModel.addElement(messages.getString("removal.text")+" "+c.getOWLAxiom());
						}
					}
				}
			}
			
		});
		
		JScrollPane propositionsScrollPane = new JScrollPane(propositionsTable);
		propositionsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
		
		
		JLabel changesLabel = new JLabel();
		changesLabel.setText(messages.getString("changesLabel.text"));
		
		changesModel = new DefaultListModel();
		changesList = new JList(changesModel);
		changesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		changesList.setLayoutOrientation(JList.VERTICAL);
		changesList.setVisibleRowCount(-1);
		
		JScrollPane changesListScrollPane = new JScrollPane(changesList);
		changesListScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
		
		JLabel versionDescriptionLabel = new JLabel();
		versionDescriptionLabel.setText(messages.getString("versionDescriptionLabel.text"));
		
		versionDescriptionTextArea = new JTextArea();
		versionDescriptionTextArea.setWrapStyleWord(true);
		versionDescriptionTextArea.setLineWrap(true);
		versionDescriptionTextArea.setFont(FontLib.getFont("Arial", 12));
		
		JScrollPane versionDescriptionScrollPane = new JScrollPane(versionDescriptionTextArea);

		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//a teraz zlozenie layoutu 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(ontologyURILabel)		
								.addGap(20)
								.addComponent(ontologyURI))
						.addGroup(layout.createSequentialGroup()
								.addComponent(newVersionLabel)		
								.addGap(20)
								.addComponent(newVersionsComboBox))
						.addComponent(propositionsLabel)
						.addComponent(propositionsScrollPane)
						.addComponent(changesLabel)
						.addComponent(changesListScrollPane)
						.addComponent(versionDescriptionLabel)
						.addComponent(versionDescriptionScrollPane)));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(ontologyURILabel)
						.addComponent(ontologyURI))
				.addGap(15)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(newVersionLabel)
						.addComponent(newVersionsComboBox))
				.addComponent(propositionsLabel)
				.addComponent(propositionsScrollPane)
				.addGap(15)
				.addComponent(changesLabel)
				.addComponent(changesListScrollPane)
				.addComponent(versionDescriptionLabel)
				.addComponent(versionDescriptionScrollPane));
		
		
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		
		for(OntologyProposition prop : propositions) {
			
			if(prop.isPropositionToAdd()) {
				propsToAdd.add(prop.getPropositionId());
			} else if(prop.isPropositionToRemove()) {
				propsToRemove.add(prop.getPropositionId());
			}
		}
		
		result.setPropsToAdd(propsToAdd);
		result.setPropsToRemove(propsToRemove);
		
		if(newVersionsComboBox.getSelectedIndex()==0) {
			result.setNewVersionIndicator(false);
		} else if(newVersionsComboBox.getSelectedIndex()==1) {
			result.setNewVersionIndicator(true);
		}
		
		result.getOntologyURI().setVersionDescription(versionDescriptionTextArea.getText());
		
		return true;
	}

	public ApplyChangesPageModel getResult() {
		return result;
	}	

}
