package org.pg.eti.kask.ont.editor.table.model;

public class Annotation {

	private String property;
	
	private String value;
	
	private String language;
	
	public Annotation() {
		this.property = new String();
		this.value = new String();
		this.language = new String();
	}

	public Annotation(String property, String value, String language) {
		this.property = property;
		this.value = value;
		this.language = language;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
