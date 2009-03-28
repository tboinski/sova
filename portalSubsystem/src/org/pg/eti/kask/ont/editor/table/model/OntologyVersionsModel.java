package org.pg.eti.kask.ont.editor.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class OntologyVersionsModel extends AbstractTableModel {

	private static final long serialVersionUID = -4115893311802553247L;
	
	private ResourceBundle messages;

	//nazwy kolumn
	private String[] columnNames;
	private List<VersionedURI> data;
	
	public OntologyVersionsModel(){
		this.messages = EditorUtil.getResourceBundle(OntologyVersionsModel.class);
		this.data = new ArrayList<VersionedURI>();
		this.columnNames = new String[]{messages.getString("ontologyVersion.text"), messages.getString("ontologySubversion.text"), messages.getString("ontologyVersionDescription.text")};
	}
	
	public OntologyVersionsModel(List<VersionedURI> data){
		this.messages = EditorUtil.getResourceBundle(OntologyVersionsModel.class);
		this.data = data;
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
				return data.get(rowIndex).getOntologyVersion();
			case 1:
				return data.get(rowIndex).getOntologySubversion();
			case 2:
				return data.get(rowIndex).getVersionDescription();
			default:
				return null;
		}
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public List<VersionedURI> getData() {
		return data;
	}

	public void setData(List<VersionedURI> data) {
		this.data = data;
	}

}
