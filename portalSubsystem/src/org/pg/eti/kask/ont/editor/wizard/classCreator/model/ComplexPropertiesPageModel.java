package org.pg.eti.kask.ont.editor.wizard.classCreator.model;

import java.util.ArrayList;

import org.pg.eti.kask.ont.editor.wizard.classCreator.ComplexPropertiesPage;

/**
 * Klasa bedaca reprezentacja obiektowa drugiej strony wizarda
 * sluzacego do tworzenia nowej klasy ontologii. Zawiera model
 * obiektowy danych wprowadzonych przez uzytkownika.
 * 
 * @see ComplexPropertiesPage
 * @author Andrzej Jakowski
 */
public class ComplexPropertiesPageModel {
	
	private ArrayList<String> disJointClassesUris; 
	
	private String expression;
	
	public ComplexPropertiesPageModel()	{
		this.disJointClassesUris = new ArrayList<String>();
		this.expression = new String();
	}
	public ComplexPropertiesPageModel(ArrayList<String> disJointClassesUris, String expression) {
		this.disJointClassesUris = disJointClassesUris;
		this.expression = expression;
	}

	public ArrayList<String> getDisJointClassesUris() {
		return disJointClassesUris;
	}

	public void setDisJointClassesUris(ArrayList<String> disJointClassesUris) {
		this.disJointClassesUris = disJointClassesUris;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
