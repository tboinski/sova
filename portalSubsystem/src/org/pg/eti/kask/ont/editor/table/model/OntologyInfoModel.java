package org.pg.eti.kask.ont.editor.table.model;

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

/**
 * 
 * @author Andrzej Jakowski
 */
public class OntologyInfoModel extends AbstractTableModel {

	private static final long serialVersionUID = -628499806966072223L;
	
	private ResourceBundle messages;

	//nazwy kolumn
	private String[] columnNames;
	private List<BaseURI> data;
	
	public OntologyInfoModel(List<BaseURI> data){
		this.messages = EditorUtil.getResourceBundle(OntologyInfoModel.class);
		this.data = data;
		this.columnNames = new String[]{messages.getString("ontologyName.text"), messages.getString("ontologyBaseURI.text"), messages.getString("ontologyDescription.text")};
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch (columnIndex) {
			case 0:
				return data.get(rowIndex).getOntologyName();
			case 1:
				return data.get(rowIndex).getBaseURI().getURIAsString();
			case 2:
				return data.get(rowIndex).getOntologyDescription();
			default:
				return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

}
