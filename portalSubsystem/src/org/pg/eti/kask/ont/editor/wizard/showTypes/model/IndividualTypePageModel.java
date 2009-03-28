package org.pg.eti.kask.ont.editor.wizard.showTypes.model;

import java.util.ArrayList;
import java.util.List;

public class IndividualTypePageModel {
	
	private List<String> individualTypes;
	
	public IndividualTypePageModel() {
		this.individualTypes = new ArrayList<String>();
	}

	public IndividualTypePageModel(List<String> individualTypes) {
		this.individualTypes = individualTypes;
	}

	public List<String> getIndividualTypes() {
		return individualTypes;
	}

	public void setIndividualTypes(List<String> individualTypes) {
		this.individualTypes = individualTypes;
	}

}
