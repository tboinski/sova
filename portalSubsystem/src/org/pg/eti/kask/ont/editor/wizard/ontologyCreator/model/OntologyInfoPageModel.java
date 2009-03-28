package org.pg.eti.kask.ont.editor.wizard.ontologyCreator.model;

public class OntologyInfoPageModel {

	private String projectName;
	
	private String creatorUserName;
	
	private String baseUri;
	
	private String physicalURI;
	
	private String ontologyName;
	
	private String ontologyDescription;
	
	private String initialVersionDescription;
	
	public OntologyInfoPageModel() {
		this.projectName = new String();
		this.creatorUserName = new String();
		this.baseUri = new String();
		this.physicalURI = new String();
		this.ontologyName = new String();
		this.ontologyDescription = new String();
		this.initialVersionDescription = new String();
	}
	
	public OntologyInfoPageModel(String projectName, String creatorUserName, String baseUri, String physicalURI,
			String ontologyName, String ontologyDescription, String initialVersionDescription) {
		this.projectName = projectName;
		this.creatorUserName = creatorUserName;
		this.baseUri = baseUri;
		this.physicalURI = physicalURI;
		this.ontologyName = ontologyName;
		this.ontologyDescription = ontologyDescription;
		this.initialVersionDescription = initialVersionDescription;
	}


	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}
	
	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public String getPhysicalURI() {
		return physicalURI;
	}

	public void setPhysicalURI(String physicalURI) {
		this.physicalURI = physicalURI;
	}

	public String getOntologyName() {
		return ontologyName;
	}

	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName;
	}

	public String getOntologyDescription() {
		return ontologyDescription;
	}

	public void setOntologyDescription(String ontologyDescription) {
		this.ontologyDescription = ontologyDescription;
	}

	public String getInitialVersionDescription() {
		return initialVersionDescription;
	}

	public void setInitialVersionDescription(String initialVersionDescription) {
		this.initialVersionDescription = initialVersionDescription;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
