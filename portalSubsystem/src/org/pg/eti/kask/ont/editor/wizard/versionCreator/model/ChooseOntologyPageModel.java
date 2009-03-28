package org.pg.eti.kask.ont.editor.wizard.versionCreator.model;

public class ChooseOntologyPageModel {
	
	private String projectName;
	

	public ChooseOntologyPageModel() {
		this.projectName = new String();
	}

	public ChooseOntologyPageModel(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
