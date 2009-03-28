package org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator.model;

import java.util.List;

public class DataPropertyRelationsPageModel {
	
	private boolean functionalProperty;
	
	private List<String> domainClassesUris;
	
	private List<String> rangeUris;

	public DataPropertyRelationsPageModel() {
	}

	public DataPropertyRelationsPageModel(boolean functionalProperty, List<String> domainClassesUris, List<String> rangeUris) {
		this.functionalProperty = functionalProperty;
		this.domainClassesUris = domainClassesUris;
		this.rangeUris = rangeUris;
	}

	public boolean isFunctionalProperty() {
		return functionalProperty;
	}

	public void setFunctionalProperty(boolean functionalProperty) {
		this.functionalProperty = functionalProperty;
	}

	public List<String> getDomainClassesUris() {
		return domainClassesUris;
	}

	public void setDomainClassesUris(List<String> domainClassesUris) {
		this.domainClassesUris = domainClassesUris;
	}

	public List<String> getRangeUris() {
		return rangeUris;
	}

	public void setRangeUris(List<String> rangeUris) {
		this.rangeUris = rangeUris;
	}
}
