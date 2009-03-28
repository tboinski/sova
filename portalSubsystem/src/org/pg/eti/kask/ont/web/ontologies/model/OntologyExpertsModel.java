package org.pg.eti.kask.ont.web.ontologies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.pg.eti.kask.ont.common.BaseURI;

public class OntologyExpertsModel implements Serializable{

	private static final long serialVersionUID = -8906749213232731628L;
	
	private BaseURI ontologyBaseURI;
	
	private List<String> experts;
	
	public OntologyExpertsModel() {
		this.ontologyBaseURI = new BaseURI();
		this.experts = new ArrayList<String>();
	}
	
	public OntologyExpertsModel(BaseURI ontologyBaseURI, List<String> experts) {
		this.ontologyBaseURI = ontologyBaseURI;
		this.experts = experts;
	}

	public BaseURI getOntologyBaseURI() {
		return ontologyBaseURI;
	}

	public void setOntologyBaseURI(BaseURI ontologyBaseURI) {
		this.ontologyBaseURI = ontologyBaseURI;
	}

	public List<String> getExperts() {
		return experts;
	}

	public void setExperts(List<String> experts) {
		this.experts = experts;
	}

}
