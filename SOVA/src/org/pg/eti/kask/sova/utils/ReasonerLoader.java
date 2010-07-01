package org.pg.eti.kask.sova.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.UnknownOWLOntologyException;

/**
 * Klasa przechowująca i ładująca reasoner wykorzystywany w wywnioskowanej
 * hierarchii. Domyślnym reasonerem jest org.mindswap.pellet.owlapi.Reasoner,
 * jednak należy pamiętać o załączeniu bibliotek pellet.
 * 
 * @author Piotr Kunowski
 * 
 */
public class ReasonerLoader {
	//nazwa klasy Reasonera.
	private static final String REASONER_CLASS_NAME = "org.mindswap.pellet.owlapi.Reasoner";
	
	private static ReasonerLoader INSTANCE = null;
	private static OWLReasoner reasoner=null;
	private ReasonerLoader(){}
	/**
	 * @return statyczna instancja klasy ReasonerLoader.
	 */
	public static ReasonerLoader getInstance() {
		if (INSTANCE==null) INSTANCE = new ReasonerLoader();
		return INSTANCE;
	}

	/**
	 * Metoda pobiera reasoner, jeśli nie został on załowdowany przez
	 * użytkownika wcześnije to zwracany jest
	 * org.mindswap.pellet.owlapi.Reasoner. Należy pamiętać o załączeniu
	 * biblioteki pellet.
	 * 
	 * @return
	 */
	public OWLReasoner getReasoner() {
		if (reasoner==null) initializeReasoner();
		
		return reasoner;
	}
	/**
	 * metoda ustawia reasoner
	 * @param reasoner
	 */
	public void setReasoner(OWLReasoner reasoner) {
		ReasonerLoader.reasoner = reasoner;
	}
	/**
	 * Metoda inicjalizuja mechanizm wnioskujacy.
	 */
	private void initializeReasoner() {
		
		try {
			Class<?> reasonerClass = Class.forName(REASONER_CLASS_NAME);
			Constructor<?> con = reasonerClass.getConstructor(OWLOntologyManager.class);
			reasoner = (OWLReasoner)con.newInstance(OWLManager.createOWLOntologyManager());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (UnknownOWLOntologyException e) {
			e.printStackTrace();
		} 
	}
	
}
