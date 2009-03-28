package org.pg.eti.kask.ont.editor.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.dialogs.AnnotationsDialog;
import org.pg.eti.kask.ont.editor.table.model.Annotation;
import org.pg.eti.kask.ont.editor.table.model.AnnotationsTableModel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLCommentAnnotation;
import org.semanticweb.owl.model.OWLLabelAnnotation;

public class AnnotationsTable extends JComponent implements ActionListener {
	
	private static final long serialVersionUID = 8957408025232636517L;

	private ResourceBundle messages;

	private JLabel annotationsLabel;
	private JTable annotationsTable;
	private AnnotationsTableModel annotationsTableModel;
	private JButton addButton;
	private JButton removeButton;
	
	private Logic logic;
	
	private List<OWLAnnotation<?>> annotationsToAdd;	
	private List<OWLAnnotation<?>> annotationsToRemove;
	
	public AnnotationsTable(AnnotationsTableModel annotationsTableModel) {
		this.messages = EditorUtil.getResourceBundle(AnnotationsTable.class);
		this.annotationsTableModel = annotationsTableModel;
		
		this.annotationsLabel = new JLabel();
		this.annotationsTable = new JTable();
		this.annotationsTableModel = annotationsTableModel;
		this.addButton = new JButton();
		this.removeButton = new JButton();
		
		this.logic = Logic.getInstance();
		
		this.annotationsToAdd = new ArrayList<OWLAnnotation<?>>();
		this.annotationsToRemove = new ArrayList<OWLAnnotation<?>>();	
		
		initialize();
	}
	
	private void initialize() {
		this.annotationsLabel.setText(messages.getString("annotationsLabel.text"));
				
		
		this.addButton.setText(messages.getString("addButton.text"));
		this.addButton.addActionListener(this);
		
		this.removeButton.setText(messages.getString("removeButton.text"));
		this.removeButton.addActionListener(this);
		
		this.annotationsTable.setModel(annotationsTableModel);
		this.annotationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.annotationsTable.getColumnModel().getColumn(0).setMaxWidth(150);
		this.annotationsTable.getColumnModel().getColumn(2).setMaxWidth(100);
		
		JScrollPane annotationsTableScrollPane = new JScrollPane(annotationsTable);
		
		//utworzenie zarzadcy ukladu
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		
		
		//najpierw zdefiniowanie poziomej grupy - zupelnie tak samo jak robi to netbeans :)
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(annotationsLabel)
						.addGroup(layout.createSequentialGroup()
								.addComponent(annotationsTableScrollPane)
								.addGroup(layout.createParallelGroup()
										.addComponent(addButton)
										.addComponent(removeButton)))));
		
		//pozniej pionowej
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(annotationsLabel)
				.addGroup(layout.createParallelGroup()
						.addComponent(annotationsTableScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addComponent(addButton)
								.addComponent(removeButton))));
		
		layout.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addButton) {
			AnnotationsDialog dialog = new AnnotationsDialog();
			dialog.open();
			if(dialog.getAnnotation() != null) {
				Annotation addedAnnotation = dialog.getAnnotation();
				//dodanie do listy zmian
				if(addedAnnotation.getProperty().equals(Constants.RDFS_COMMENT)) {
					OWLCommentAnnotation annotation = logic.getDataFactory().getCommentAnnotation(addedAnnotation.getValue(), addedAnnotation.getLanguage());
					annotationsToAdd.add(annotation);
				} else if(addedAnnotation.getProperty().equals(Constants.RDFS_LABEL)) {
					OWLLabelAnnotation annotation = logic.getDataFactory().getOWLLabelAnnotation(addedAnnotation.getValue(), addedAnnotation.getLanguage());
					annotationsToAdd.add(annotation);
				}
				
				annotationsTableModel.getAnnotations().add(addedAnnotation);
				annotationsTableModel.fireTableDataChanged();
			}
		} else if(e.getSource() == removeButton) {
			int selectedRow = annotationsTable.getSelectedRow();
			if(selectedRow != -1) {
				Annotation deletedAnnotation = annotationsTableModel.getAnnotations().get(selectedRow);
				if(deletedAnnotation.getProperty().equals(Constants.RDFS_COMMENT)) {
					OWLCommentAnnotation annotation = logic.getDataFactory().getCommentAnnotation(deletedAnnotation.getValue(), deletedAnnotation.getLanguage());
					if(annotationsToAdd.contains(annotation)) {
						annotationsToAdd.remove(annotation);
					} else {
						annotationsToRemove.add(annotation);
					}
				} else if(deletedAnnotation.getProperty().equals(Constants.RDFS_LABEL)) {
					OWLLabelAnnotation annotation = logic.getDataFactory().getOWLLabelAnnotation(deletedAnnotation.getValue(), deletedAnnotation.getLanguage());
					if(annotationsToAdd.contains(annotation)) {
						annotationsToAdd.remove(annotation);
					} else {
						annotationsToRemove.add(annotation);
					}
				}
				
				annotationsTableModel.getAnnotations().remove(selectedRow);
				annotationsTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
			}
		}
	}

	public List<OWLAnnotation<?>> getAnnotationsToAdd() {
		return annotationsToAdd;
	}

	public List<OWLAnnotation<?>> getAnnotationsToRemove() {
		return annotationsToRemove;
	}
}
