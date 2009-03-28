package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.editor.table.model.OntologyInfoModel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class ChooseOntologyDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = -7026185330663020649L;
	
	private ResourceBundle messages;
	
	private JPanel mainPanel;
	private JTextField chosenOntologyTextField;
	private JTable ontologiesTable;
	private OntologyInfoModel ontologyTableModel;
	private JButton okButton;
	private JButton cancelButton;
	
	private BaseURI result;
	
	public ChooseOntologyDialog(OntologyInfoModel ontologyTableModel) {
		this.messages = EditorUtil.getResourceBundle(ChooseOntologyDialog.class);
		
		this.mainPanel = new JPanel();
		this.ontologyTableModel = ontologyTableModel; 
	}
	
	private void initialize() {
		JLabel availableOntologiesLabel = new JLabel();
		availableOntologiesLabel.setText(messages.getString("availableOntologiesLabel.text"));
		
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
					
				} else {
					chosenOntologyTextField.setText("");
				}
			}
			
		});
		JScrollPane ontologiesInfoScrollPane = new JScrollPane(ontologiesTable);
		ontologiesInfoScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
		
			
		
		//uri wybranej ontologii
		JLabel chosenOntologyLabel = new JLabel();
		chosenOntologyLabel.setText(messages.getString("chosenOntologyLabel.text"));
		
		chosenOntologyTextField = new JTextField();
		chosenOntologyTextField.setEditable(false);	
		
		okButton = new JButton();
		okButton.setText(messages.getString("okButton.text"));
		okButton.addActionListener(this);
		
		cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancelButton.text"));
		cancelButton.addActionListener(this);
		
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
						.addComponent(chosenOntologyLabel)
						.addComponent(chosenOntologyTextField)
						.addGroup(layout.createSequentialGroup()
								.addGap(300)
								.addComponent(okButton)
								.addGap(10)
								.addComponent(cancelButton))));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(availableOntologiesLabel)
				.addComponent(ontologiesInfoScrollPane)
				.addComponent(chosenOntologyLabel)
				.addComponent(chosenOntologyTextField)
				.addGroup(layout.createParallelGroup()						
						.addComponent(okButton)						
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		this.add(mainPanel);		
		this.setTitle(messages.getString("chooseOntologyDialog.title"));
		this.setSize(new Dimension(500,450));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open(){
		initialize();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			int selectedOntologyRow = ontologiesTable.getSelectedRow();
			
			if(selectedOntologyRow == -1) {
				JOptionPane.showMessageDialog(this, messages.getString("errorDialog.body1"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			} else {
				String baseUri = (String)ontologyTableModel.getValueAt(selectedOntologyRow, 1);
				this.result = new BaseURI(baseUri);
				this.setVisible(false);
			}		
			
		} else if(e.getSource() == cancelButton) {
			this.setVisible(false);
		}
	}
	
	public BaseURI getResult() {
		return result;
	}

}
