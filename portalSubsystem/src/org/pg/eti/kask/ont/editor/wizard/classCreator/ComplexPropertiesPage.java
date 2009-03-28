package org.pg.eti.kask.ont.editor.wizard.classCreator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.dialogs.ClassSelectionDialog;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.classCreator.model.ComplexPropertiesPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;

import prefuse.util.FontLib;

/**
 * 
 * @author Andrzej Jakowski
 */
public class ComplexPropertiesPage extends AbstractPage implements ActionListener {
	
	private ResourceBundle messages;
	
	private Logic logic;
	
	private JPanel mainPanel;
	private JList disJointClassesList;
	private DefaultListModel listModel; 
	private JTextArea expressionTextArea;
	
	private ComplexPropertiesPageModel result;
	
	public ComplexPropertiesPage(AbstractWizard parentWizard, Logic logic) {
		super(parentWizard);
		this.messages = EditorUtil.getResourceBundle(ComplexPropertiesPage.class);
		this.logic = logic;
		this.mainPanel = new JPanel();
		this.result = new ComplexPropertiesPageModel();
		
		initialize();
	}
	
	private void initialize() {
		JLabel disJointClassesLabel = new JLabel();
		disJointClassesLabel.setText(messages.getString("disJointClassesLabel.text"));
		
		listModel = new DefaultListModel();
		
		//lista z klasami rozlacznymi
		disJointClassesList = new JList(listModel);
		disJointClassesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		disJointClassesList.setLayoutOrientation(JList.VERTICAL);
		disJointClassesList.setVisibleRowCount(-1);
		
		JScrollPane listScrollPane = new JScrollPane(disJointClassesList);
		listScrollPane.setPreferredSize(new Dimension(250, 80));
		
		JButton addClassesButton = new JButton();
		addClassesButton.setText(messages.getString("addClassesButton.text"));
		addClassesButton.setActionCommand(CommandsConstans.BROWSE_CLASSES_BUTTON);
		addClassesButton.addActionListener(this);
		
		JButton removeClassesButton = new JButton();
		removeClassesButton.setText(messages.getString("removeClassesButton.text"));
		removeClassesButton.setActionCommand(CommandsConstans.REMOVE_CLASS_BUTTON);
		removeClassesButton.addActionListener(this);
		
		JLabel expressionEditorLabel = new JLabel();
		expressionEditorLabel.setText(messages.getString("expressionEditorLabel.text"));
		
		expressionTextArea = new JTextArea();
		expressionTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		expressionTextArea.setWrapStyleWord(true);
		expressionTextArea.setLineWrap(true);
		expressionTextArea.setFont(FontLib.getFont("Arial", 12));
		expressionTextArea.setEnabled(false);
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(disJointClassesLabel)
					.addGroup(layout.createSequentialGroup()
							.addComponent(listScrollPane)
							.addGroup(layout.createParallelGroup()
									.addComponent(addClassesButton)
									.addComponent(removeClassesButton)))));
					/*.addComponent(expressionEditorLabel)
					.addComponent(expressionTextArea)));*/
		
		//teraz pionowa
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(disJointClassesLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(listScrollPane, 0, 100, 100)
						.addGroup(layout.createSequentialGroup()
								.addComponent(addClassesButton)
								.addComponent(removeClassesButton)))
				.addComponent(addClassesButton));
				/*.addComponent(expressionEditorLabel)
				.addComponent(expressionTextArea));*/
		
		//przyciski removeClassesButton i addClassesButton maja ten sam rozmiar
		layout.linkSize(SwingConstants.HORIZONTAL, addClassesButton, removeClassesButton);
		
		
	}

	@Override
	public Component getContainer() {
		return mainPanel;
	}

	@Override
	public boolean validate() {
		ArrayList<String> disjointClassesUris = new ArrayList<String>();
		
		for(int i=0; i<listModel.getSize(); i++) {
			disjointClassesUris.add(listModel.get(i).toString());
		}
		
		result.setDisJointClassesUris(disjointClassesUris);
		result.setExpression(expressionTextArea.getText());
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.BROWSE_CLASSES_BUTTON)) {
			String ontologyURI = logic.getLoadedOntologyURI();
			OntologyClassesTreeModel classesTree = new OntologyClassesTreeModel(logic.getOntology(ontologyURI)); 
			ClassSelectionDialog dialog = new ClassSelectionDialog(classesTree);
			dialog.open();
			ArrayList<BasicTreeNode> result = dialog.getSelectedClassesInfo();
			if(result != null) {
				for(BasicTreeNode treeNode : result) {
					if(!listModel.contains(treeNode.getElementURI())) {
						listModel.addElement(treeNode.getElementURI());
					}
				}
			}
			
		} else if(e.getActionCommand().equals(CommandsConstans.REMOVE_CLASS_BUTTON)) {
			if(!disJointClassesList.isSelectionEmpty()) {
				listModel.remove(disJointClassesList.getSelectedIndex());
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public ComplexPropertiesPageModel getResult() {
		return result;
	}

}
