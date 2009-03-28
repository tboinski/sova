package org.pg.eti.kask.ont.editor.wizard.individualCreator.model;

public class IndividualInformationPageModel {
	
	private String individualURI;
	
	private String label;
	
	private String comment;
	
	public IndividualInformationPageModel() {
		this.individualURI = new String();
		this.label = new String();
		this.comment = new String();
	}

	public IndividualInformationPageModel(String individualURI, String label, String comment) {
		this.individualURI = individualURI;
		this.label = label;
		this.comment = comment;
	}

	public String getIndividualURI() {
		return individualURI;
	}

	public void setIndividualURI(String individualURI) {
		this.individualURI = individualURI;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
