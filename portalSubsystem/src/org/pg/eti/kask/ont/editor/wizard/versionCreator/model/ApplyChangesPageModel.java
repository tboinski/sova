package org.pg.eti.kask.ont.editor.wizard.versionCreator.model;

import java.util.ArrayList;
import java.util.List;

import org.pg.eti.kask.ont.common.VersionedURI;

public class ApplyChangesPageModel {
	
	private VersionedURI ontologyURI;
	
	private List<Integer> propsToRemove;
	
	private List<Integer> propsToAdd;
	
	private boolean newVersionIndicator;
	
	public ApplyChangesPageModel() {
		this.ontologyURI = new VersionedURI();
		this.propsToRemove = new ArrayList<Integer>();
		this.propsToAdd = new ArrayList<Integer>();
		this.newVersionIndicator = false;
	}

	public ApplyChangesPageModel(VersionedURI ontologyURI, List<Integer> propsToRemove, List<Integer> propsToAdd, boolean newVersionIndicator) {
		this.ontologyURI = ontologyURI;
		this.propsToRemove = propsToRemove;
		this.propsToAdd = propsToAdd;
		this.newVersionIndicator = newVersionIndicator;
	}

	public VersionedURI getOntologyURI() {
		return ontologyURI;
	}

	public void setOntologyURI(VersionedURI ontologyURI) {
		this.ontologyURI = ontologyURI;
	}

	public List<Integer> getPropsToRemove() {
		return propsToRemove;
	}

	public void setPropsToRemove(List<Integer> propsToRemove) {
		this.propsToRemove = propsToRemove;
	}

	public List<Integer> getPropsToAdd() {
		return propsToAdd;
	}

	public void setPropsToAdd(List<Integer> propsToAdd) {
		this.propsToAdd = propsToAdd;
	}
	
	public boolean isNewVersionIndicator() {
		return newVersionIndicator;
	}

	public void setNewVersionIndicator(boolean newVersionIndicator) {
		this.newVersionIndicator = newVersionIndicator;
	}
	

	@Override
	public String toString() {
		String returnValue = new String();
		returnValue = "URI: "+ontologyURI.getBaseURI()+";"+ontologyURI.getOntologyVersion()+";"+ontologyURI.getOntologySubversion();
		
		returnValue += "\nPROPS TO ADD:";
		for(Integer i: propsToAdd) {
			returnValue += "\n\t"+i;
		}
		
		returnValue += "\nPROPS TO REMOVE:";
		for(Integer i: propsToRemove) {
			returnValue += "\n\t"+i;
		}
		
		return returnValue;
	}

	
	
	
}
