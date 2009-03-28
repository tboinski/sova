package org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.model;

import org.pg.eti.kask.ont.common.VersionedURI;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class ChooseOntologyPageModel {

	//ostateczne uri ontologii
	private VersionedURI ontologyUri;

	public ChooseOntologyPageModel() {
		this.ontologyUri = new VersionedURI();
	}
	
	public ChooseOntologyPageModel(VersionedURI ontologyUri) {
		this.ontologyUri = ontologyUri;
	}

	public VersionedURI getOntologyUri() {
		return ontologyUri;
	}

	public void setOntologyUri(VersionedURI ontologyUri) {
		this.ontologyUri = ontologyUri;
	}

}
