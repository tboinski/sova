package org.pg.eti.kask.ont.editor.table.model;

public class OntologyProposition {
	
	private int propositionId;
	
	private String title;
	
	private String description;
	
	private boolean propositionToAdd;
	
	private boolean propositionToRemove;
	
	private boolean propositionToPromote;
	
	

	public OntologyProposition() {
		this.propositionId = 0;
		this.title = new String();
		this.description = new String();
		this.propositionToAdd = false;
		this.propositionToRemove = false;
		this.propositionToPromote = false;
	}

	public OntologyProposition(int propositionId, String title,
			String description, boolean propositionToAdd,
			boolean propositionToRemove, boolean propositionToPromote) {
		this.propositionId = propositionId;
		this.title = title;
		this.description = description;
		this.propositionToAdd = propositionToAdd;
		this.propositionToRemove = propositionToRemove;
		this.propositionToPromote = propositionToPromote;
	}

	public int getPropositionId() {
		return propositionId;
	}

	public void setPropositionId(int propositionId) {
		this.propositionId = propositionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPropositionToAdd() {
		return propositionToAdd;
	}

	public void setPropositionToAdd(boolean propositionToAdd) {
		this.propositionToAdd = propositionToAdd;
		if(propositionToAdd) {
			propositionToPromote = false;
			propositionToRemove = false;
		}
	}

	public boolean isPropositionToRemove() {
		return propositionToRemove;
	}

	public void setPropositionToRemove(boolean propositionToRemove) {		
		this.propositionToRemove = propositionToRemove;
		if(propositionToRemove) {
			propositionToAdd = false;
			propositionToPromote = false;
		}
	}

	public boolean isPropositionToPromote() {
		return propositionToPromote;
	}

	public void setPropositionToPromote(boolean propositionToPromote) {
		this.propositionToPromote = propositionToPromote;
		if(propositionToPromote) {
			propositionToAdd = false;
			propositionToRemove = false;
		}
	}
	
}
