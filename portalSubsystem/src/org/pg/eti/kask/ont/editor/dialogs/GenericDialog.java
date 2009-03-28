package org.pg.eti.kask.ont.editor.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.util.EditorUtil;

/**
 * Podstawowe okienko dialogowe, ktore pozwala na tworzenie
 * bardziej zlozonych okienek edycyjnych.
 *
 * @author Andrzej Jakowski
 */
abstract class GenericDialog extends JDialog implements ActionListener {
	
	private ResourceBundle messages;
	
	private JList list;
	private DefaultListModel listModel;
	private JButton addButton;
	private JButton removeButton;
	private JButton okButton;
	private JButton cancelButton;
	
	public GenericDialog() {
		this.messages = EditorUtil.getResourceBundle(GenericDialog.class);
		this.listModel = new DefaultListModel();
	}
	
	public GenericDialog(DefaultListModel listModel) {
		this.messages = EditorUtil.getResourceBundle(GenericDialog.class);
		this.listModel = listModel;
	}
	
	private void initialize() {
		JLabel label = new JLabel();
		label.setText(getListLabel());
		
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		
		JScrollPane domainClassesListScrollPane = new JScrollPane(list);
		
		this.addButton = new JButton();
		addButton.setText(messages.getString("addButton.text"));
		addButton.addActionListener(this);
		
		this.removeButton = new JButton();
		removeButton.setText(messages.getString("removeButton.text"));
		removeButton.addActionListener(this);
		
		this.okButton = new JButton();
		okButton.setText(messages.getString("okButton.text"));
		okButton.addActionListener(this);
		
		this.cancelButton = new JButton();
		cancelButton.setText(messages.getString("cancelButton.text"));
		cancelButton.addActionListener(this);
		
		JPanel mainPanel = new JPanel();
		
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(label)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(domainClassesListScrollPane)
										.addGroup(layout.createSequentialGroup()
												.addComponent(okButton)
												.addComponent(cancelButton)))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(addButton)
										.addComponent(removeButton)))
						));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(label)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(domainClassesListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addComponent(addButton)
								.addComponent(removeButton)))
				.addGroup(layout.createParallelGroup()
						.addComponent(okButton)
						.addComponent(cancelButton)));
		
		layout.linkSize(SwingConstants.HORIZONTAL, okButton, cancelButton);
		layout.linkSize(SwingConstants.HORIZONTAL, removeButton, addButton);
		
		//zainicjalizowania okienka dialogowego
		this.add(mainPanel);		
		this.setTitle(getTitle());
		this.setSize(new Dimension(getWidth(),getHeight()));
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	public void open() {
		initialize();
	}

	
	public abstract String getListLabel();
	
	public abstract String getTitle();
	
	public abstract Object getResult();
	
	public abstract void doAdd();
	
	public abstract void doRemove();
	
	public abstract void doOk();
	
	public abstract void doCancel();
	
	public int getHeight() {
		return 400;
	}
	
	public int getWidth() {
		return 450;
	}
	
	public DefaultListModel getListModel() {
		return listModel;
	}

	public void setListModel(DefaultListModel listModel) {
		this.listModel = listModel;
	}
	
	public JList getList() {
		return list;
	}

	public void setList(JList list) {
		this.list = list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addButton) {
			doAdd();
		} else if(e.getSource() == removeButton) {
			doRemove();
		} else if(e.getSource() == okButton) {
			doOk();
		} else if(e.getSource() == cancelButton) {
			doCancel();
		}

	}

}
