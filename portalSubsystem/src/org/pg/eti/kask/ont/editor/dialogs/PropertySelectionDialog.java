package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class PropertySelectionDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 9089612231444617766L;
	
	private ResourceBundle messages;
	
	private OntologyPropertiesTreeModel treeModel;
	
	private ArrayList<BasicTreeNode> selectedProperties;
	
	private JTree propertyTree;
	
	public PropertySelectionDialog(OntologyPropertiesTreeModel model) {
		this.messages = EditorUtil.getResourceBundle(PropertySelectionDialog.class);
		this.treeModel = model;
		this.selectedProperties = new ArrayList<BasicTreeNode>();
	}
	
	private void initialize() {
		JLabel selectClassLabel = new JLabel();
		selectClassLabel.setText(messages.getString("propertySelectionDialog.selectPropertyLabel.text"));
		
		propertyTree = new JTree(treeModel);
		
		JScrollPane scrollPane = new JScrollPane(propertyTree);
		
		//definicja przycisku ok
		final JButton okButton = new JButton();
		okButton.setText(messages.getString("propertySelectionDialog.okButton.text"));
		okButton.setActionCommand(CommandsConstans.OK_BUTTON);
		okButton.addActionListener(this);
		
		//definiowanie przycisku cancel
		final JButton cancelButton = new JButton();
		cancelButton.setText(messages.getString("propertySelectionDialog.cancelButton.text"));
		cancelButton.setActionCommand(CommandsConstans.CANCEL_BUTTON);
		cancelButton.addActionListener(this);
		
		//konetener na podstawowe elementy okienka dialogowego
		JPanel mainPanel = new JPanel();
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(selectClassLabel)
						.addComponent(scrollPane)
						.addGroup(layout.createSequentialGroup()
								.addGap(100)
								.addComponent(okButton)
								.addComponent(cancelButton))));
		
		//teraz pionowa
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(selectClassLabel)
				.addComponent(scrollPane)
				.addGroup(layout.createParallelGroup()						
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		//przyciski ok i cancel maja ten sam rozmiar
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(messages.getString("propertySelectionDialog.title"));
		this.setSize(new Dimension(300,350));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open(){
		initialize();		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.OK_BUTTON)) {
			TreePath[] selectedElements =  propertyTree.getSelectionPaths();
			if(selectedElements != null) {
				for(int i=0;i<selectedElements.length;i++) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)selectedElements[i].getLastPathComponent();
					if(treeNode != null) {
						BasicTreeNode node = (BasicTreeNode)treeNode.getUserObject();
						if(node != null){
							selectedProperties.add(node);
						}
					}	
				}
			}
			
		} else if(e.getActionCommand().equals(CommandsConstans.CANCEL_BUTTON)) {
			selectedProperties.clear();
		}
		
		this.setVisible(false);
	}

	public ArrayList<BasicTreeNode> getSelectedProperties() {
		return selectedProperties;
	}

}
