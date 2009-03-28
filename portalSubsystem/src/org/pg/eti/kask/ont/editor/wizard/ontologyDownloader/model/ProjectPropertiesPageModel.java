package org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.model;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class ProjectPropertiesPageModel {

	private String projectName;

	public ProjectPropertiesPageModel(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
