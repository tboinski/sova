package org.pg.eti.kask.ont.editor.dialogs;

import java.util.ResourceBundle;

import org.pg.eti.kask.ont.editor.util.EditorUtil;

public class ClassComplementDialog extends GenericDialog {

	private static final long serialVersionUID = -6690429775531033012L;
	
	private ResourceBundle messages;
	
	public ClassComplementDialog() {
		this.messages = EditorUtil.getResourceBundle(ClassComplementDialog.class);
	}

	@Override
	public void doAdd() {
		
	}

	@Override
	public void doCancel() {
		
	}

	@Override
	public void doOk() {
		
	}

	@Override
	public void doRemove() {
		
	}

	@Override
	public String getListLabel() {
		return messages.getString("classComplementDialog.listLabel.text");
	}

	@Override
	public Object getResult() {
		return "";
	}

	@Override
	public String getTitle() {
		return messages.getString("classComplementDialog.title");
	}


}
