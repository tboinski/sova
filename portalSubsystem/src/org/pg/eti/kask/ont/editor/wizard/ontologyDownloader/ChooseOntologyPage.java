package org.pg.eti.kask.ont.editor.wizard.ontologyDownloader;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.table.model.OntologyInfoModel;
import org.pg.eti.kask.ont.editor.table.model.OntologyVersionsModel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.model.ChooseOntologyPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class ChooseOntologyPage extends AbstractPage {
	
	private ResourceBundle messages;
	
	//poszczegolne komponenty strony wizarda
	private JPanel mainPanel;
	private JTextField chosenOntologyTextField;
	private JTable ontologiesTable;
	private JTable ontologyVersionsTable;
	private Logic logic;
	private OntologyInfoModel ontologyTableModel;
	private OntologyVersionsModel ontologyVersionsTableModel;
	
	//obiekt zawierajacy wszystkie dane wproadzone przez uzytkownika na tej stronie wizarda
	private ChooseOntologyPageModel result;

	public ChooseOntologyPage(DownloadOntologyWizard parentWizard) {
		super(parentWizard);
	
		this.messages = EditorUtil.getResourceBundle(ChooseOntologyPage.class);
		this.mainPanel = new JPanel();
		this.result = new ChooseOntologyPageModel();
		this.logic = Logic.getInstance();
		
		initialize();
	}
	
	private void initialize() {
		JLabel availableOntologiesLabel = new JLabel();
		availableOntologiesLabel.setText(messages.getString("availableOntologiesLabel.text"));
		
		ontologyTableModel = new OntologyInfoModel(logic.getOntologiesInfo());
		
		//tabela z ontologiami
		ontologiesTable = new JTable(ontologyTableModel);
		ontologiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ontologiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if(!lsm.isSelectionEmpty()) {
					int selectedOntology = ontologiesTable.getSelectedRow();
					
					//pobranie URI ontologii, ktore znajduje sie w drugiej kolumnie!!!					
					String ontologyBaseURI = (String)ontologyTableModel.getValueAt(selectedOntology, 1);
					chosenOntologyTextField.setText(ontologyBaseURI);
					
					List<VersionedURI> listOfVersions = logic.getOntologyVersions(new BaseURI(ontologyBaseURI));
					
					ontologyVersionsTableModel.setData(listOfVersions);
					ontologyVersionsTableModel.fireTableDataChanged();
					ontologyVersionsTable.changeSelection(0, 0, false, false);
				} else {
					chosenOntologyTextField.setText("");
				}
			}
			
		});
		JScrollPane ontologiesInfoScrollPane = new JScrollPane(ontologiesTable);
		ontologiesInfoScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
		
		//tabela z informacjami o wersjach i podwersjach
		JLabel ontologyVersionLabel = new JLabel();
		ontologyVersionLabel.setText(messages.getString("ontologyVersionLabel.text"));
		ontologyVersionsTableModel = new OntologyVersionsModel();
		ontologyVersionsTable = new JTable(ontologyVersionsTableModel);
		ontologyVersionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ontologyVersionsTable.getColumnModel().getColumn(0).setMaxWidth(70);
		ontologyVersionsTable.getColumnModel().getColumn(1).setMaxWidth(70);
		ontologyVersionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ontologyVersionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if(!lsm.isSelectionEmpty()) {					
					int selectedVersion = ontologyVersionsTable.getSelectedRow();
					int selectedOntology = ontologiesTable.getSelectedRow();
					
					Integer version = (Integer)ontologyVersionsTableModel.getValueAt(selectedVersion, 0);
					Integer subVersion = (Integer)ontologyVersionsTableModel.getValueAt(selectedVersion, 1);
					
					String chosenOntology = new String();
					//zlozenie Uri z baseUri z wersji i podwersji
					chosenOntology = chosenOntology
						.concat((String)ontologyTableModel.getValueAt(selectedOntology, 1))
						.concat(";")
						.concat(version.toString())
						.concat(";")
						.concat(subVersion.toString());
					
					chosenOntologyTextField.setText(chosenOntology);
					
				} else {
					chosenOntologyTextField.setText("");
				}
			}
			
		}); 
		
		JScrollPane ontologyVersionsScrollPane = new JScrollPane(ontologyVersionsTable);
		ontologyVersionsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
		
		
		//uri wybranej ontologii
		JLabel chosenOntologyLabel = new JLabel();
		chosenOntologyLabel.setText(messages.getString("chosenOntologyLabel.text"));
		
		chosenOntologyTextField = new JTextField();
		chosenOntologyTextField.setEditable(false);		
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//a teraz zlozenie layoutu 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(availableOntologiesLabel)
						.addComponent(ontologiesInfoScrollPane)
						.addComponent(ontologyVersionLabel)
						.addComponent(ontologyVersionsScrollPane)
						.addComponent(chosenOntologyLabel)
						.addComponent(chosenOntologyTextField)));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(availableOntologiesLabel)
				.addComponent(ontologiesInfoScrollPane)
				.addComponent(ontologyVersionLabel)
				.addComponent(ontologyVersionsScrollPane)
				.addComponent(chosenOntologyLabel)
				.addComponent(chosenOntologyTextField));
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		int selectedVersionRow = ontologyVersionsTable.getSelectedRow();
		int selectedOntologyRow = ontologiesTable.getSelectedRow();
		
		if(selectedOntologyRow == -1 || selectedVersionRow == -1 ) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("errorDialog.body1"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			String baseUri = (String)ontologyTableModel.getValueAt(selectedOntologyRow, 1);
			Integer version = (Integer)ontologyVersionsTableModel.getValueAt(selectedVersionRow, 0);
			Integer subversion = (Integer)ontologyVersionsTableModel.getValueAt(selectedVersionRow, 1);
			VersionedURI uri = new VersionedURI(new BaseURI(baseUri), version, subversion);
			result = new ChooseOntologyPageModel(uri);
			return true;
		}		
	}

	/**
	 * 
	 * @return
	 */
	public ChooseOntologyPageModel getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 */
	public void setResult(ChooseOntologyPageModel result) {
		this.result = result;
	}

}
