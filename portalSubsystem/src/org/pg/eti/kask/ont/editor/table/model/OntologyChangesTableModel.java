package org.pg.eti.kask.ont.editor.table.model;

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class OntologyChangesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -8565081226477867064L;
	
	private ResourceBundle messages;
	
	private List<OntologyProposition> propositions;
	
	private String[] columnNames;
	
	
	public OntologyChangesTableModel(List<OntologyProposition> propositions){
		this.messages = EditorUtil.getResourceBundle(OntologyChangesTableModel.class);
		this.propositions = propositions;
		
		this.columnNames = new String[]{messages.getString("changeId.text"), messages.getString("changeTitle.text"), 
				messages.getString("changeDescription.text"), messages.getString("toAdd.text"), 
				messages.getString("toRemove.text"), messages.getString("toPromote.text")};
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return propositions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		OntologyProposition prop = propositions.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return prop.getPropositionId();
			case 1:
				return prop.getTitle();
			case 2:
				return prop.getDescription();
			case 3:
				return prop.isPropositionToAdd();
			case 4:
				return prop.isPropositionToRemove(); 
			case 5:
				return prop.isPropositionToPromote();
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return Integer.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return Boolean.class;
			case 4:
				return Boolean.class;
			case 5:
				return Boolean.class;
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 3 || columnIndex == 4 || columnIndex == 5) {
			return true;
		} else {
			return false;
		}
	}
	
	

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		OntologyProposition prop = propositions.get(rowIndex);
		if(columnIndex == 3) {
			if(!prop.isPropositionToAdd()) {
				prop.setPropositionToAdd((Boolean)value);
			}
		} else if(columnIndex == 4) {
			if(!prop.isPropositionToRemove()) {
				prop.setPropositionToRemove((Boolean)value);
			}
		} else if(columnIndex == 5) {
			if(!prop.isPropositionToPromote()) {
				prop.setPropositionToPromote((Boolean)value);
			}
		}		
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public List<OntologyProposition> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<OntologyProposition> propositions) {
		this.propositions = propositions;
	}

}
