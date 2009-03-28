package org.pg.eti.kask.ont.editor.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URI;
import java.util.Locale;
import java.util.ResourceBundle;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.vocab.XSDVocabulary;

/**
 * Klasa narzedziowa zawierajaca metody przydatne do wspolpracy
 * ze swing/awt, oraz wszelkie metody narzedziowe wykorzystywane w edytorze. 
 * 
 * @author Andrzej Jakowski
 */
public class EditorUtil {

	/**
	 * Metoda okreslajaca rozmiar ekranu.
	 * 
	 * @return instancja klasy Dimension z ustalonymi wymiarami ekranu 
	 */
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * Metoda wyznaczajaca wspolrzedne startowe dla komponentu
	 * o zadanych wymiarach, tak aby znalazl sie on dokladnie na
	 * srodku ekranu 
	 * 
	 * @param size rozmiar komponent
	 * @return instancja klasy Point okreslajaca wspolrzedne startowe komponentu o rozmiarach size
	 */
	public static Point getStartingPosition(Dimension size) {
		Dimension d = getScreenSize();
		Point centrePoint = new Point((int)d.getWidth()/2 - (int)size.getWidth()/2, (int)d.getHeight()/2 - (int)size.getHeight()/2);		
		return centrePoint;
	}
	
	/**
	 * Metoda wyznaczajaca srodek ekranu.
	 * 
	 * @return obiekt klast Point, okreslajacy wspolrzedne srodka ekranu
	 */
	public static Point getScreenCentre() {
		Dimension d = getScreenSize();
		Point centrePoint = new Point((int)d.getWidth()/2, (int)d.getHeight()/2);		
		return centrePoint;
	}
	
	/**
	 * Metoda pobierajaca nazwe klasy z obiektu reprezentujacego 
	 * klase w postaci OWL API.
	 * 
	 * @param owlClass klasa w postaci OWL API
	 * @return nazwa klasy
	 */
	public static String getOwlEntityName(OWLEntity owlEntity) {
		String result = null;
		result = owlEntity.getURI().getFragment();
		if (result != null) {
			return result;
		} else {
			return owlEntity.getURI().toString();
		}
	}
		
	/**
	 * Metoda pobierajaca nazwe klasy z informacji dostarczonych w URI klasy.
	 * 
	 * @param classURI URI klasy
	 * @return nazwa klasy
	 */
	public static String getOwlEntityName(String entityURI) {
		String result = null;
		URI uri = URI.create(entityURI);
		result = uri.getFragment();
		if (result != null) {
			return result;
		} else {
			return uri.toString();
		}
	}
	
	/**
	 * 
	 * @param currentClass 
	 * @return
	 */
	public static ResourceBundle getResourceBundle(Class<?> currentClass) {
		String messagesFileName = currentClass.getPackage().getName()+"."+"messages";
		return ResourceBundle.getBundle(messagesFileName, Locale.getDefault());
	}
	
	/**
	 * Metoda sprawdzajaca czy panel z wywnioskowana hierarchia ma byc widoczny. 
	 * Metoda korzysta z konfiguracji zapisanej w pliku konfiguracyjnym aplikacji.
	 *  
	 * @return true, jesli panel ma byc widoczny, false w przciwnym wypadku
	 */
	public static boolean isInferredHierarchyPanelVisisble() {
		
		ApplicationConfiguration config = Logic.getInstance().getConfig();
		
		
		return config.isShowInferredHierarchy();
	}
	
	/**
	 * Metoda konwerutujaca typ danych z reprezentacji OWL API na reprezentacje 
	 * wykorzystywana w systemie.
	 * @param dataType
	 * @return
	 */
	public static String convertFromOWLDataType(OWLDataType dataType) {
		if(dataType.getURI().equals(XSDVocabulary.INTEGER.getURI())) {
			return Constants.XSD_INTEGER;
		} else if(dataType.getURI().equals(XSDVocabulary.BOOLEAN.getURI())) {
			return Constants.XSD_BOOLEAN;
		} else if(dataType.getURI().equals(XSDVocabulary.DATE.getURI())) {
			return Constants.XSD_DATE;
		} else if(dataType.getURI().equals(XSDVocabulary.DOUBLE.getURI())) {
			return Constants.XSD_DOUBLE;
		} else if(dataType.getURI().equals(XSDVocabulary.FLOAT.getURI())) {
			return Constants.XSD_FLOAT;
		} else if(dataType.getURI().equals(XSDVocabulary.STRING.getURI())) {
			return Constants.XSD_STRING;
		} else if(dataType.getURI().equals(XSDVocabulary.TIME.getURI())) {
			return Constants.XSD_TIME;
		} else if(dataType.getURI().equals(XSDVocabulary.DATE_TIME.getURI())) {
			return Constants.XSD_DATETIME;
		}
		return null;
	}
	
	/**
	 * Metoda konwertujacatyp danych z reprezentacji wykorzystywanej w systemie 
	 * na akceptowana przez biblioteke OWL API.
	 * 
	 * @param text
	 * @return
	 */
	public static OWLDataType convertToOWLDataType(String text) {
		OWLDataType dataType = null;
		Logic logic = Logic.getInstance();
		
		if(text.equals(Constants.XSD_INTEGER)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.INTEGER.getURI());
		} else if(text.equals(Constants.XSD_BOOLEAN)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.BOOLEAN.getURI());
		} else if(text.equals(Constants.XSD_DATE)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE.getURI());
		} else if(text.equals(Constants.XSD_DATETIME)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DATE_TIME.getURI());
		} else if(text.equals(Constants.XSD_DOUBLE)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.DOUBLE.getURI());
		} else if(text.equals(Constants.XSD_FLOAT)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.FLOAT.getURI());
		} else if(text.equals(Constants.XSD_STRING)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.STRING.getURI());
		} else if(text.equals(Constants.XSD_TIME)) {
			dataType = logic.getDataFactory().getOWLDataType(XSDVocabulary.TIME.getURI());
		}		
		
		return dataType;
	}
}
