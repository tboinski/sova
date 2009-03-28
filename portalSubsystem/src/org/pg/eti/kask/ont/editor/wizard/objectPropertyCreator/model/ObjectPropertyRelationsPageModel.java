package org.pg.eti.kask.ont.editor.wizard.objectPropertyCreator.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectPropertyRelationsPageModel {

	private boolean functionalProperty;
	
	private boolean inverseFunctionalProperty;
	
	private boolean symmetricProperty;
	
	private boolean transitiveProperty;
	
	private List<String> domainClassesUris;
	
	private List<String> rangeClassesUris;

	public ObjectPropertyRelationsPageModel() {
		this.functionalProperty = false;
		this.inverseFunctionalProperty = false;
		this.symmetricProperty = false;
		this.transitiveProperty = false;
		this.domainClassesUris = new ArrayList<String>();
		this.rangeClassesUris = new ArrayList<String>();
	}

	public boolean isFunctionalProperty() {
		return functionalProperty;
	}

	public void setFunctionalProperty(boolean functionalProperty) {
		this.functionalProperty = functionalProperty;
	}

	public boolean isInverseFunctionalProperty() {
		return inverseFunctionalProperty;
	}

	public void setInverseFunctionalProperty(boolean inverseFunctionalProperty) {
		this.inverseFunctionalProperty = inverseFunctionalProperty;
	}

	public boolean isSymmetricProperty() {
		return symmetricProperty;
	}

	public void setSymmetricProperty(boolean symmetricProperty) {
		this.symmetricProperty = symmetricProperty;
	}

	public boolean isTransitiveProperty() {
		return transitiveProperty;
	}

	public void setTransitiveProperty(boolean transitiveProperty) {
		this.transitiveProperty = transitiveProperty;
	}

	public List<String> getDomainClassesUris() {
		return domainClassesUris;
	}

	public void setDomainClassesUris(List<String> domainClassesUris) {
		this.domainClassesUris = domainClassesUris;
	}

	public List<String> getRangeClassesUris() {
		return rangeClassesUris;
	}

	public void setRangeClassesUris(List<String> rangeClassesUris) {
		this.rangeClassesUris = rangeClassesUris;
	}
	
	
}
