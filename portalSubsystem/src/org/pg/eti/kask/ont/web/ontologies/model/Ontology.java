package org.pg.eti.kask.ont.web.ontologies.model;

import java.io.Serializable;
import java.util.List;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.VersionedURI;

public class Ontology implements Serializable {
	
	private static final long serialVersionUID = 4203373708014718697L;

	private BaseURI ontologyBaseURI;
		
	private List<VersionedURI> versions;
	
	private boolean subscribed;
	
	public Ontology(BaseURI ontologyBaseURI, List<VersionedURI> versions) {
		this.ontologyBaseURI = ontologyBaseURI;
		this.versions = versions;
	}

	public BaseURI getOntologyBaseURI() {
		return ontologyBaseURI;
	}

	public void setOntologyBaseURI(BaseURI ontologyBaseURI) {
		this.ontologyBaseURI = ontologyBaseURI;
	}

	public List<VersionedURI> getVersions() {
		return versions;
	}

	public void setVersions(List<VersionedURI> versions) {
		this.versions = versions;
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	

}
