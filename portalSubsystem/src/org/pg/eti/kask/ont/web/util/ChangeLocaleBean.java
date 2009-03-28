package org.pg.eti.kask.ont.web.util;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class ChangeLocaleBean {
	
	private String currentLanguage;
	
	public ChangeLocaleBean() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		currentLanguage = context.getViewRoot().getLocale().getLanguage();
	}

	public void changeLocale(ActionEvent evt) {
		currentLanguage =  evt.getComponent().getId();
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getViewRoot().setLocale(new Locale(currentLanguage));
	}

	public String getCurrentLanguage() {
		return currentLanguage;
	}

	public void setCurrentLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}

}