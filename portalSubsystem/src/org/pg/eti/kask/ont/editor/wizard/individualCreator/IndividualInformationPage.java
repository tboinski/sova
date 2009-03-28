package org.pg.eti.kask.ont.editor.wizard.individualCreator;

import java.awt.Color;
import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.individualCreator.model.IndividualInformationPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

import prefuse.util.FontLib;

public class IndividualInformationPage extends AbstractPage {
	
	private ResourceBundle messages;
	
	private JPanel mainPanel;
	
	private Logic logic;
	
	private JTextField individualNameTextField;
	private JTextField finalIndividualUri;
	private JTextField individualLabelTextField;
	private JTextArea individualCommentTextArea;
	
	private IndividualInformationPageModel result;

	public IndividualInformationPage(AbstractWizard parentWizard) {
		super(parentWizard);
		
		this.messages = EditorUtil.getResourceBundle(IndividualInformationPage.class);
		this.logic = Logic.getInstance();
		
		this.mainPanel = new JPanel();
		
		this.result = new IndividualInformationPageModel();
		
		initialize();
	}
	
	private void initialize() {
		//Stworzenie wszystkich komponentow
		JLabel individualNameLabel = new JLabel(messages.getString("individualNameLabel.text"));
		individualNameTextField = new JTextField();
		individualNameTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) { 
				updateFinalIndividualUriTextField(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalIndividualUriTextField(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalIndividualUriTextField(e);
			}
			
			private void updateFinalIndividualUriTextField(DocumentEvent e) {
				String newClassName =  individualNameTextField.getText();
				String ontologyURI = logic.getLoadedOntologyURI();
				finalIndividualUri.setText(ontologyURI.concat("#").concat(newClassName));
			}
			
		});
		String ontologyURI = logic.getLoadedOntologyURI();
		
		JLabel individualUriLabel = new JLabel(messages.getString("individualUriLabel.text"));
		finalIndividualUri = new JTextField(ontologyURI.concat("#"));
		finalIndividualUri.setEditable(false);
				
		JLabel individualLabelLabel = new JLabel(messages.getString("individualLabelLabel.text"));
		individualLabelTextField = new JTextField();
		
		JLabel individualCommentLabel = new JLabel(messages.getString("individualCommentLabel.text"));
		individualCommentTextArea = new JTextArea();
		individualCommentTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		individualCommentTextArea.setWrapStyleWord(true);
		individualCommentTextArea.setLineWrap(true);
		individualCommentTextArea.setFont(FontLib.getFont("Arial", 12));
		
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy - zupelnie tak samo jak robi to netbeans :)
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(individualNameLabel)
						.addComponent(individualUriLabel)
						.addComponent(individualLabelLabel)
						.addComponent(individualCommentLabel))				
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(individualNameTextField)
						.addComponent(finalIndividualUri)
						.addComponent(individualLabelTextField)
						.addComponent(individualCommentTextArea))
				);
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(individualNameLabel)
						.addComponent(individualNameTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(individualUriLabel)
						.addComponent(finalIndividualUri))			
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(individualLabelLabel)
						.addComponent(individualLabelTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(individualCommentLabel)
						.addComponent(individualCommentTextArea)
						));
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		String indName = individualNameTextField.getText();
		if(indName.trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("errorDialog.body1"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			result.setIndividualURI(finalIndividualUri.getText());
			result.setComment(individualCommentTextArea.getText());
			result.setLabel(individualLabelTextField.getText());
			return true;	
		}		
	}

	public IndividualInformationPageModel getResult() {
		return result;
	}

}
