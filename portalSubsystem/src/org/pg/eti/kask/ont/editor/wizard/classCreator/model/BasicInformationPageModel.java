package org.pg.eti.kask.ont.editor.wizard.classCreator.model;

/**
 * Klasa bedaca reprezentacja obiektowa modelu danych zastosowanych
 * na pierwszej stronie wizarda tworzacego nowa klase ontologii.  
 * 
 * @author Andrzej Jakowski
 */
public class BasicInformationPageModel {
	
	private String classUri;
	
	private String superClassUri;
	
	private String label;
	
	private String comment;
	
	/**
	 * 
	 */
	public BasicInformationPageModel(){
		this.classUri = new String();
		this.superClassUri = new String();
		this.label = new String();
		this.comment = new String();
	}

	/**
	 * 
	 * @param classUri
	 * @param superClassUri
	 * @param label
	 * @param comment
	 */
	public BasicInformationPageModel(String classUri, String superClassUri, String label, String comment) {
		this.classUri = classUri;
		this.superClassUri = superClassUri;
		this.label = label;
		this.comment = comment;
	}

	/**
	 * 
	 * @return
	 */
	public String getClassUri() {
		return classUri;
	}

	/**
	 * 
	 * @param classUri
	 */
	public void setClassUri(String classUri) {
		this.classUri = classUri;
	}

	/**
	 * 
	 * @return
	 */
	public String getSuperClassUri() {
		return superClassUri;
	}

	/**
	 * 
	 * @param superClassUri
	 */
	public void setSuperClassUri(String superClassUri) {
		this.superClassUri = superClassUri;
	}

	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
