package org.pg.eti.kask.ont.editor.wizard.ontologyDownloader;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.model.ProjectPropertiesPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class ProjectPropertiesPage extends AbstractPage {
	
	private ResourceBundle messages;

	private JPanel mainPanel;
	private JTextField projectNameTextField;
	private JTextField fileNameTextField;
	private JButton browseButton;
	
	private ProjectPropertiesPageModel result;
	
	
	/**
	 * 
	 * @param parentWizard
	 */
	public ProjectPropertiesPage(AbstractWizard parentWizard) {
		super(parentWizard);

		this.messages = EditorUtil.getResourceBundle(ProjectPropertiesPage.class);
		this.mainPanel = new JPanel();
		
		initialize();
	}
	
	private void initialize() {
		JLabel projectNameLabel = new JLabel();
		projectNameLabel.setText(messages.getString("projectNameLabel.text"));
		
		projectNameTextField = new JTextField();
		
		
		JLabel fileNameLabel = new JLabel();
		fileNameLabel.setText(messages.getString("fileNameLabel.text"));
		
		fileNameTextField = new JTextField();
		
		browseButton = new JButton();
		browseButton.setText(messages.getString("browseButton.text"));
		
		
		browseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
			}
			
		});
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!
		//ustawienie komponentow zwiazanych z zapisem do pliku na niewidoczne
		browseButton.setVisible(false);
		fileNameLabel.setVisible(false);
		fileNameTextField.setVisible(false);
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//a teraz zlozenie layoutu 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(projectNameLabel)
						.addComponent(fileNameLabel))
				.addGroup(layout.createParallelGroup()
						.addComponent(projectNameTextField)
						.addGroup(layout.createSequentialGroup()
								.addComponent(fileNameTextField)
								.addComponent(browseButton))));
				
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(projectNameLabel)
						.addComponent(projectNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE))				
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(fileNameLabel)
						.addComponent(fileNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE)
						.addComponent(browseButton)));
		
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		String projectName = projectNameTextField.getText();
		
		if(projectName == null || projectName.trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("errorDialog2.body1"), messages.getString("errorDialog2.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			this.result = new ProjectPropertiesPageModel(projectName);
			return true;
		}
		
	}

	/**
	 * 
	 * @return
	 */
	public ProjectPropertiesPageModel getResult() {
		return result;
	}

}
