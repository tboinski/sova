package org.pg.eti.kask.ont.editor.wizard.ontologyCreator;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.OntologyFileFilter;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyCreator.model.OntologyInfoPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

import prefuse.util.FontLib;

public class OntologyInfoPage extends AbstractPage implements ActionListener{
	
	private ResourceBundle messages;
	
	private JPanel mainPanel;

	private JTextField projectNameTextField;
	private JTextField baseUriTextField;
	private JTextField physicalURI;
	private JTextField ontologyName;
	private JTextArea ontologyDescription;
	private JTextArea initialVersionDescription;
	private JButton browseButton;
	
	private OntologyInfoPageModel result;

	public OntologyInfoPage(AbstractWizard parentWizard) {
		super(parentWizard);
		
		this.messages = EditorUtil.getResourceBundle(OntologyInfoPage.class);
		this.mainPanel = new JPanel();
		
		initialize();
	}
	
	private void initialize() {
		
		JLabel projectNameLabel = new JLabel();
		projectNameLabel.setText(messages.getString("projectNameLabel.text"));
		
		projectNameTextField = new JTextField();
		
		
		JLabel baseUriLabel = new JLabel();
		baseUriLabel.setText(messages.getString("baseUriLabel.text"));
		
		baseUriTextField = new JTextField();
		
		JLabel physicalURILabel = new JLabel();
		physicalURILabel.setText(messages.getString("physicalURILabel.text"));
		
		physicalURI = new JTextField();
		
		browseButton = new JButton();
		browseButton.setText(messages.getString("browseButton.text"));
		browseButton.addActionListener(this);
		
		JLabel ontologyNameLabel = new JLabel();
		ontologyNameLabel.setText(messages.getString("ontologyNameLabel.text"));
		ontologyName = new JTextField();
		
		JLabel ontologyDescriptionLabel = new JLabel();
		ontologyDescriptionLabel.setText(messages.getString("ontologyDescriptionLabel.text"));
		
		ontologyDescription = new JTextArea();
		ontologyDescription.setWrapStyleWord(true);
		ontologyDescription.setLineWrap(true);
		ontologyDescription.setFont(FontLib.getFont("Arial", 12));
		
		JScrollPane ontologyDescriptionPane = new JScrollPane(ontologyDescription);
		
		JLabel initialVersionDescriptionLabel = new JLabel();
		initialVersionDescriptionLabel.setText(messages.getString("initialVersionDescriptionLabel.text"));
		
		initialVersionDescription = new JTextArea();
		initialVersionDescription.setWrapStyleWord(true);
		initialVersionDescription.setLineWrap(true);
		initialVersionDescription.setFont(FontLib.getFont("Arial", 12));
		
		JScrollPane initialVersionDescriptionPane = new JScrollPane(initialVersionDescription);
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(projectNameLabel)
						.addComponent(baseUriLabel)
						.addComponent(physicalURILabel)
						.addComponent(ontologyNameLabel)
						.addComponent(ontologyDescriptionLabel)
						.addComponent(initialVersionDescriptionLabel))
				.addGap(20)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(baseUriTextField)
						.addComponent(projectNameTextField)
						.addGroup(layout.createSequentialGroup()
								.addComponent(physicalURI)
								.addComponent(browseButton))
						.addComponent(ontologyName)
						.addComponent(ontologyDescriptionPane)
						.addComponent(initialVersionDescriptionPane)));		
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(projectNameLabel)
						.addComponent(projectNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(baseUriLabel)
						.addComponent(baseUriTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE))
				.addGap(10)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(physicalURILabel)
						.addComponent(physicalURI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						          GroupLayout.PREFERRED_SIZE)
						.addComponent(browseButton))
				.addGap(10)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(ontologyNameLabel)
						.addComponent(ontologyName))
				.addGap(10)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(ontologyDescriptionLabel)
						.addComponent(ontologyDescriptionPane))
				.addGap(10)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(initialVersionDescriptionLabel)
						.addComponent(initialVersionDescriptionPane)));
		
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		String baseUri = baseUriTextField.getText();
		
		String projectName = this.projectNameTextField.getText();
		String ontologyName = this.ontologyName.getText();
		String ontologyDescription = this.ontologyDescription.getText();
		
		result = new OntologyInfoPageModel();
		Logic logic = Logic.getInstance();
		
		if(projectName.trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("projectNameError.body"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			result.setProjectName(projectName);		
		}
		
		result.setCreatorUserName(logic.getLoggedInUser().getUsername());
		
		try {
			URI.create(baseUri);
			result.setBaseUri(baseUri);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("errorDialog.body1"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		result.setPhysicalURI(physicalURI.getText());
		
		if(ontologyName.trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("ontologyNameError.body"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			result.setOntologyName(ontologyName);
		}
		
		if(ontologyDescription.trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("ontologyDescriptionError.body"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			result.setOntologyDescription(ontologyDescription);
		}
		
		result.setInitialVersionDescription(initialVersionDescription.getText());
			
		return true;
	}

	public OntologyInfoPageModel getResult() {
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new OntologyFileFilter(false));
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int result = fileChooser.showOpenDialog(parentWizard);
		
		if(result == JFileChooser.CANCEL_OPTION) {
			return;			
		} else {
			String fileURI = fileChooser.getSelectedFile().toURI().toString();
			physicalURI.setText(fileURI);
		}
		
	}
}
