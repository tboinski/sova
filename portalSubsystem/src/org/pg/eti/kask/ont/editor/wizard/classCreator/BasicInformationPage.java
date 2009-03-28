package org.pg.eti.kask.ont.editor.wizard.classCreator;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.dialogs.ClassSelectionDialog;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.classCreator.model.BasicInformationPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

import prefuse.util.FontLib;

/**
 * Klasa bedaca odzwierciedleniem pierwszej strony wizarda,
 * sluzacego do tworzenia nowej klasy ontologii. Po wypelnieniu 
 * formularza mozna pobrac obiekt bedacy obiektowa reprezentacja
 * danych wprowadzonych w formularzu.
 * 
 * @see BasicInformationPage#getResult()
 * 
 * @author Andrzej Jakowski
 */
public class BasicInformationPage extends AbstractPage  {
	
	private ResourceBundle messages;
		
	private String superClassUri; 
	
	private JPanel mainPanel;
	
	private Logic logic;
	
	private JTextField classNameTextField;
	private JTextField finalClassUri;
	private JTextField superClassTextField;
	private JTextField classLabelTextField;
	private JTextArea classCommentTextArea;
	
	private BasicInformationPageModel result;

	public BasicInformationPage(AbstractWizard parentWizard, Logic logic, String superClassUri) {
		super(parentWizard);
		this.messages = EditorUtil.getResourceBundle(BasicInformationPage.class);
		this.logic = logic;
		this.superClassUri = superClassUri;
		this.mainPanel = new JPanel();
		this.result = new BasicInformationPageModel();
		
		initialize();
	}
	
	private void initialize() {
		//Stworzenie wszystkich komponentow
		JLabel classNameLabel = new JLabel(messages.getString("classNameLabel.text"));
		classNameTextField = new JTextField();
		classNameTextField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) { 
				updateFinalClassUriTextField(e);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateFinalClassUriTextField(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateFinalClassUriTextField(e);
			}
			
			private void updateFinalClassUriTextField(DocumentEvent e) {
				String newClassName =  classNameTextField.getText();
				String ontologyURI = logic.getLoadedOntologyURI();
				finalClassUri.setText(ontologyURI.concat("#").concat(newClassName));
			}
			
		});
		String ontologyURI = logic.getLoadedOntologyURI();
		
		JLabel classUriLabel = new JLabel(messages.getString("classUriLabel.text"));
		finalClassUri = new JTextField(ontologyURI.concat("#"));
		finalClassUri.setEditable(false);
		
		JLabel superClassLabel = new JLabel(messages.getString("superClassLabel.text"));
		superClassTextField = new JTextField();
		superClassTextField.setText(superClassUri);
		
		JLabel classLabelLabel = new JLabel(messages.getString("classLabelLabel.text"));
		classLabelTextField = new JTextField();
		
		JLabel classCommentLabel = new JLabel(messages.getString("classCommentLabel.text"));
		classCommentTextArea = new JTextArea();
		classCommentTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		classCommentTextArea.setWrapStyleWord(true);
		classCommentTextArea.setLineWrap(true);
		classCommentTextArea.setFont(FontLib.getFont("Arial", 12));
		
		final JButton browseButton = new JButton(messages.getString("browseButton.text"));
		browseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String ontologyURI = logic.getLoadedOntologyURI();
				OntologyClassesTreeModel classesTree = new OntologyClassesTreeModel(logic.getOntology(ontologyURI));
				//otwarcie okna dialogowego w celu wybrania klasy
				ClassSelectionDialog dialog = new ClassSelectionDialog(classesTree);
				dialog.open();
				
				//wpisanie odpowiedniej nazwy klasy
				ArrayList<BasicTreeNode> classesInfo = dialog.getSelectedClassesInfo();
				if(classesInfo != null && classesInfo.size() > 0) {
					superClassTextField.setText(classesInfo.get(0).getElementURI());
				}
				
			}
			
		});
		
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy - zupelnie tak samo jak robi to netbeans :)
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(classNameLabel)
						.addComponent(classUriLabel)
						.addComponent(superClassLabel)
						.addComponent(classLabelLabel)
						.addComponent(classCommentLabel))				
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(classNameTextField)
						.addComponent(finalClassUri)
						.addGroup(layout.createSequentialGroup()
							.addComponent(superClassTextField)
							.addComponent(browseButton))
						.addComponent(classLabelTextField)
						.addComponent(classCommentTextArea))
				);
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(classNameLabel)
						.addComponent(classNameTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(classUriLabel)
						.addComponent(finalClassUri))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(superClassLabel)
						.addComponent(superClassTextField)
						.addComponent(browseButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(classLabelLabel)
						.addComponent(classLabelTextField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(classCommentLabel)
						.addComponent(classCommentTextArea)
						));
		
	}

	@Override
	public boolean validate() {
		if(classNameTextField.getText() == null || classNameTextField.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(parentWizard, messages.getString("errorDialog.body1"), messages.getString("errorDialog.title"),JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			result.setClassUri(finalClassUri.getText());
			result.setSuperClassUri(superClassTextField.getText());
			result.setLabel(classLabelTextField.getText());
			result.setComment(classCommentTextArea.getText());
			return true;
		}
	}
	
	@Override
	public Component getContainer() {
		return mainPanel ;
	}

	/**
	 * Zwraca dane formularza w postaci modelu obiektowego.
	 * 
	 * @return instancja klasy <code>BasicInformationPageModel</code> odzwierciedlajaca dane formularza
	 */
	public BasicInformationPageModel getResult() {
		return result;
	}

}
