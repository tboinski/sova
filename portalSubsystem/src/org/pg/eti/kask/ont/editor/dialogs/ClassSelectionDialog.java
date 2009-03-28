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
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

/**
 * Klasa reprezentujaca okno dialogowe do wyboru klasy z dostepnej ontologii.
 * 
 * @author Andrzej Jakowski
 */
public class ClassSelectionDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 427233792986634233L;
	
	private ResourceBundle messages;
	
	private OntologyClassesTreeModel treeModel;
	
	private ArrayList<BasicTreeNode> selectedClassesInfo;
	
	private JTree classesTree;
	
	/**
	 * 
	 * @param model
	 */
	public ClassSelectionDialog(OntologyClassesTreeModel model) {
		this.messages = EditorUtil.getResourceBundle(ClassSelectionDialog.class);
		this.treeModel = model;
		this.selectedClassesInfo = new ArrayList<BasicTreeNode>();
	}
	
	
	private void initialize() {
		JLabel selectClassLabel = new JLabel();
		selectClassLabel.setText(messages.getString("classSelectionDialog.selectClassLabel.text"));
		
		classesTree = new JTree(treeModel);
		
		JScrollPane scrollPane = new JScrollPane(classesTree);
		
		//definicja przycisku ok
		final JButton okButton = new JButton();
		okButton.setText(messages.getString("classSelectionDialog.okButton.text"));
		okButton.setActionCommand(CommandsConstans.OK_BUTTON);
		okButton.addActionListener(this);
		
		//definiowanie przycisku cancel
		final JButton cancelButton = new JButton();
		cancelButton.setText(messages.getString("classSelectionDialog.cancelButton.text"));
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
		this.setTitle(messages.getString("classSelectionDialog.title"));
		this.setSize(new Dimension(300,350));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	/**
	 * Inicjalizuje komponenty skladowe i otwiera okno dialogowe.
	 */
	public void open(){
		initialize();		
	}


	
	/**
	 * Zwrocenie URI wszystkich zaznaczonych klas na drzewie klas. 
	 * 
	 * @return lista wszystkich URI zaznaczonych klas
	 */
	public ArrayList<BasicTreeNode> getSelectedClassesInfo() {
		return selectedClassesInfo;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.OK_BUTTON)) {
			//pobranie zaznaczonych klas i zapisanie do obiektu wyjsciowego
			TreePath[] selectedElements =  classesTree.getSelectionPaths();
			if(selectedElements != null) {
				for(int i=0;i<selectedElements.length;i++) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)selectedElements[i].getLastPathComponent();
					if(treeNode != null) {
						BasicTreeNode node = (BasicTreeNode)treeNode.getUserObject();
						if(node != null){
							selectedClassesInfo.add(node);
						}
					}	
				}
			}
			
		} else if(e.getActionCommand().equals(CommandsConstans.CANCEL_BUTTON)) {
			selectedClassesInfo.clear();
		}
		
		//na koniec zamkniecie okienka
		this.setVisible(false);
		
	}

}
