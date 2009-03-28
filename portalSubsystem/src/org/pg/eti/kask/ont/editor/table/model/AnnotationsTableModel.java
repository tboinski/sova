package org.pg.eti.kask.ont.editor.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class AnnotationsTableModel extends AbstractTableModel{

	private static final long serialVersionUID = -8873766956923269824L;
	
	private ResourceBundle messages;
	
	private List<Annotation> annotations;
	
	private String[] columnNames;
	
	public AnnotationsTableModel() {
		this.messages = EditorUtil.getResourceBundle(AnnotationsTableModel.class);
		
		this.annotations = new ArrayList<Annotation>();
		
		this.columnNames = new String[]{messages.getString("property.text"), messages.getString("value.text"), messages.getString("language.text")};
	}
	
	public AnnotationsTableModel(List<Annotation> annotations) {
		this.messages = EditorUtil.getResourceBundle(AnnotationsTableModel.class);
		
		this.annotations = annotations;
		
		this.columnNames = new String[]{messages.getString("property.text"), messages.getString("value.text"), messages.getString("language.text")};
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return annotations.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Annotation annotation =  annotations.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return annotation.getProperty();
			case 1:
				return annotation.getValue();
			case 2:
				return annotation.getLanguage();
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

}
