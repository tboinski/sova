package org.pg.eti.kask.ont.editor.wizard.objectPropertyCreator.model;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owl.model.OWLAnnotation;

public class BasicInformationsPageModel {
	
	private String propertyURI;
	
	private String superPropertyURI;
	
	private String inversePropertyURI;
	
	private List<OWLAnnotation<?>> annotations;
	
	public BasicInformationsPageModel() {
		this.propertyURI = new String();
		this.superPropertyURI = new String();
		this.inversePropertyURI = new String();
		this.annotations = new ArrayList<OWLAnnotation<?>>();
	}

	public String getPropertyURI() {
		return propertyURI;
	}

	public void setPropertyURI(String propertyURI) {
		this.propertyURI = propertyURI;
	}

	public String getSuperPropertyURI() {
		return superPropertyURI;
	}

	public void setSuperPropertyURI(String superPropertyURI) {
		this.superPropertyURI = superPropertyURI;
	}

	public String getInversePropertyURI() {
		return inversePropertyURI;
	}

	public void setInversePropertyURI(String inversePropertyURI) {
		this.inversePropertyURI = inversePropertyURI;
	}

	public List<OWLAnnotation<?>> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<OWLAnnotation<?>> annotations) {
		this.annotations = annotations;
	}

}
