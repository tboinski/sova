package org.pg.eti.kask.ont.editor.wizard.changesExporter;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.changesExporter.model.ChangeDescriptionPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

import prefuse.util.FontLib;

public class ChangeDescriptionPage extends AbstractPage {
	
	private ResourceBundle messages;
	
	private JPanel mainPanel;
	private JLabel baseURI;
	private JLabel version;
	private JLabel subversion;
	private JTextField changeTitleTextField;
	private JTextArea changeDescriptionTextArea;
			
	private Logic logic;
	
	private ChangeDescriptionPageModel result;

	public ChangeDescriptionPage(ExportChangesWizard parentWizard) {
		super(parentWizard);
		
		this.messages = EditorUtil.getResourceBundle(ChangeDescriptionPage.class);
		this.mainPanel = new JPanel();
		this.logic = Logic.getInstance();
		
		initialize();
	}
	
	private void initialize() {
		ProjectDescriptor desc = logic.getDescriptor();
		
		JLabel baseURILabel = new JLabel();
		baseURILabel.setText(messages.getString("baseURILabel.text"));
		
		baseURI = new JLabel();
		baseURI.setText(desc.getOntologyBaseURI());
		
		JLabel versionLabel = new JLabel();
		versionLabel.setText(messages.getString("versionLabel.text"));
		
		version = new JLabel();
		version.setText("" + desc.getVersion());
		
		JLabel subversionLabel = new JLabel();
		subversionLabel.setText(messages.getString("subversionLabel.text"));
		
		subversion = new JLabel();
		subversion.setText(""+desc.getSubVersion());
		
		JLabel changeTitleLabel = new JLabel();
		changeTitleLabel.setText(messages.getString("changeTitleLabel.text"));
		
		changeTitleTextField = new JTextField();		
		
		JLabel changeDescriptionLabel = new JLabel();
		changeDescriptionLabel.setText(messages.getString("changeDescriptionLabel.text"));
		
		changeDescriptionTextArea = new JTextArea();
		changeDescriptionTextArea.setWrapStyleWord(true);
		changeDescriptionTextArea.setLineWrap(true);
		changeDescriptionTextArea.setFont(FontLib.getFont("Arial", 12));
		
		JScrollPane changeDescriptionScrollPane = new JScrollPane(changeDescriptionTextArea);
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(baseURILabel)
						.addComponent(versionLabel)
						.addComponent(changeTitleLabel)
						.addComponent(changeDescriptionLabel))
				.addGap(20)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(baseURI)
						.addGroup(layout.createSequentialGroup()
								.addComponent(version)
								.addGap(50)
								.addComponent(subversionLabel)
								.addComponent(subversion))
						.addComponent(changeTitleTextField)
						.addComponent(changeDescriptionScrollPane)));
					
		
		//teraz pionowa
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(baseURILabel)
						.addComponent(baseURI))
				.addGap(15)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(versionLabel)
						.addComponent(version)
						.addComponent(subversionLabel)
						.addComponent(subversion))
				.addGap(15)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(changeTitleLabel)
						.addComponent(changeTitleTextField))
				.addGap(15)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(changeDescriptionLabel)
						.addComponent(changeDescriptionScrollPane)));
		
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		if(changeTitleTextField.getText().trim().equals("") || changeDescriptionTextArea.getText().trim().equals("") ) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("errorDialog.body1"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {		
			result = new ChangeDescriptionPageModel(changeTitleTextField.getText(), changeDescriptionTextArea.getText());
			return true;
		}
	}

	public ChangeDescriptionPageModel getResult() {
		return result;
	}

}
