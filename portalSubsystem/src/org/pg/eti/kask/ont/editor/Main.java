package org.pg.eti.kask.ont.editor;

import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.util.ApplicationConfiguration;


/**
 * Klasa glowna pozwalajaca na uruchomienie aplikacji.
 * Uruchamia GUI poprzez stworzenie interfejsu swingowego
 * w watku event dispatchera.
 * 
 * @author Andrzej Jakowski
 */
public class Main {
	
	//referencja do loggera
	private static Logger logger = Logger.getLogger(Main.class.getCanonicalName());
	
	//nazwa aplikacji
	public static final String APPLICATION_NAME = "OCS - Ontology creation system";
	
	//sciezka do katalogu z konfiguracja i danymi aplikacji
	public static final String APPLICATION_FOLDER_PATH =System.getProperty("user.home").concat("/ocs/");
	
	//sciezka do pliku z konfiguracja aplikacji
	public static final String CONFIGURATION_FILE_PATH = APPLICATION_FOLDER_PATH+"config.xml";
	
	public static void main(String args[]) {
		startUp();
	}
	
	public static void startUp() {
		//poniewaz wiekszosc Swinga nie jest thread safe wiec trzeba 
		//odpalic gui w watku event dispatchera.
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	ApplicationConfiguration config = null;
				
				if (!checkConfigurationExistence()) {
					config = createConfiguration();
				} else {
					config = readConfiguration();
				}
				
				String language = (String)config.getLocaleLanguage();
				
				Locale.setDefault(new Locale(language));
		        createAndShowGUI(config);
		    }
		});		
	}
	
	/**
	 * Metoda skladajaca kolejne komponenty GUI oraz wyswietlajaca
	 * glowna ramke aplikacji.
	 * 
	 */
	public static void createAndShowGUI(ApplicationConfiguration config){
		//ustawienie look and feel na systemowe
		try {
			UIManager.setLookAndFeel(""+UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e ) {  }
		//pokazanie glownej ramki aplikacji
		MainFrame frame = new MainFrame(APPLICATION_NAME, config);		
		frame.initialize();			
	}

	/**
	 * Metoda sprawdzajaca czy dla danej instancji aplikacji
	 * istnieje jej konfiguracja w systemie.
	 * 
	 * @return true istnieje konfiguracja, false w przeciwnym wypadku
	 */
	public static boolean checkConfigurationExistence() {	
		boolean temp = true;
		File folder = new File(APPLICATION_FOLDER_PATH);
		File confFile = new File(CONFIGURATION_FILE_PATH);
		temp = folder.exists();
		temp = confFile.exists();
		
		return temp;
	}
	
	/**
	 * Tworzy nowa konfiguracje aplikacji na podstawie predefiniowanych danych.
	 * 
	 * @return konfiguracja aplikacji zapisanych w postaci par (klucz-wartosc)
	 */
	public static ApplicationConfiguration createConfiguration() {
		boolean created = true;
		logger.info("Creating application configuration");		
		File folder = new File(APPLICATION_FOLDER_PATH);
		if(!folder.exists())
			created = folder.mkdir();

		ApplicationConfiguration config = new ApplicationConfiguration();
		config.setLocaleLanguage(Constants.DEFAULT_LANGUAGE);
		config.setUserName("");
		config.setPassword("");
		config.setShowInferredHierarchy(false);
		
		config.saveToFile(CONFIGURATION_FILE_PATH);
		 
		logger.info("Application configuration created ? " + created);
		return config;
	}
	
	/**
	 * Metoda odczytujaca konfiguracje aplikacji.
	 * 
	 * @return konfiguracja aplikacji zapisanych w postaci par (klucz-wartosc) 
	 */
	public static ApplicationConfiguration readConfiguration() {
		logger.info("Reading application configuration");
		
		ApplicationConfiguration conf = new ApplicationConfiguration();
		conf.processFile(new File(CONFIGURATION_FILE_PATH).toURI().toString());
		return conf;
	}
	
}
