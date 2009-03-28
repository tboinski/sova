package org.pg.eti.kask.ont.editor.wizard.page;

import java.awt.Component;

import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;

/**
 * 
 * 
 * @author Andrzej Jakowski
 */
public abstract class AbstractPage {
	
	//referencja do wizarda w obrebie ktorego dana strona bedzie dzialac
	protected AbstractWizard parentWizard;
	
	/**
	 * Konstruktor parametryzowany. Przesylana jest do niego referecja do wizarda
	 * w obrebie ktorego dana strona bedzie dzialac.
	 * 
	 * @param parentWizard referencja do wizarda w obrebie ktorego dana strona bedzie dzialac
	 */
	public AbstractPage(AbstractWizard parentWizard){
		this.parentWizard = parentWizard;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean validate();
	
	/**
	 * 
	 */
	public abstract Component getContainer();
	
	/**
	 * Zwraca wartosc pola parentWizard. 
	 * 
	 * @return instancja klasy AbstractWizard
	 */
	public AbstractWizard getParentWizard() {
		return parentWizard;
	}

	/**
	 * Ustawia pole parentWizard.
	 * 
	 * @param parentWizard - instacja klasy AbstractWizard
	 */
	public void setParentWizard(AbstractWizard parentWizard) {
		this.parentWizard = parentWizard;
	}
		
}
