package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLDataType;

public class DataRangeSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 5988476786200647976L;

	private ResourceBundle messages;
	
	private JComboBox rangeDataTypeComboBox;
	private DefaultComboBoxModel rangeDataTypeComboBoxModel;
	private JButton okButton;
	private JButton cancelButton;
	
	private OWLDataType dataType;
	
	public DataRangeSelectionDialog() {
		this.messages = EditorUtil.getResourceBundle(DataRangeSelectionDialog.class);
		
		this.dataType = null;
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
			
			dataType = EditorUtil.convertToOWLDataType(chosenDataRange);
					
			this.setVisible(false);
		} else if(e.getSource() == cancelButton) {
			this.setVisible(false);
		}
	}

	public OWLDataType getDataType() {
		return dataType;
	}

}
