package org.pg.eti.kask.ont.editor.util;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Klasa przechowujaca konfiguracje aplikacji,ktora przechowywana jest w pliku. 
 * Instancja tej klasy jest singeltonem i pobiera sie ja za pomoca metody getInstance.
 *
 * @author Andrzej Jakowski
 */
public class ApplicationConfiguration extends DefaultHandler{
	
	//adres serwera ocs - wykorzystywane przez biblioteke komunikacyjna
	public static final String OCS_SERVER_URL = "http://ocs.kask.eti.pg.gda.pl/ServCom/Servlet";
	
	//super tajne haslo do klucza (musi byc 128 bitowe)
	//tym kluczem szyfrowane jest haslo uzytkownika
	private static final String pass="superTajneHaslo1";
	
	//nazwy tagow w pliku xml
	public static final String ROOT_TAG_NAME = "config";
	public static final String LOCALE_LANGUAGE_TAG_NAME = "localeLanguage";
	public static final String USER_TAG_NAME = "user";
	public static final String PASSWORD_ATTRIBUTE_NAME = "password";
	public static final String NAME_ATTRIBUTE_NAME = "name";
	public static final String SHOW_ATTRIBUTE_NAME = "show";
	public static final String LANGUAGE_ATTRIBUTE_NAME = "language";
	public static final String SHOW_INFERRED_HIERARCHY_TAG_NAME = "showInferredHierarchy";
	
	private String userName;
	
	private String password;
	
	private String localeLanguage;
	
	private boolean showInferredHierarchy;
	
	
	/**
	 * Konstruktor do ktorego dostep jest tylko z tej klasy, tak aby
	 * miec pewnosc ze z innej klasy nie zostanie stworzona
	 * instancja tej klasy.
	 */
	public ApplicationConfiguration() {
		this.userName = new String();
		this.password = new String();
		this.localeLanguage = new String();
		this.showInferredHierarchy = false;
	}
	
	/**
	 * 
	 * @param fileURI
	 */
	public void processFile(String fileURI) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();			
			XMLReader xmlReader = parser.getXMLReader();			
			xmlReader.setContentHandler(this);			
			xmlReader.parse(new InputSource(fileURI));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes){
		if(name.equals(USER_TAG_NAME)) {
			this.userName = attributes.getValue(NAME_ATTRIBUTE_NAME);	
			
			String encryptedPassword = attributes.getValue(PASSWORD_ATTRIBUTE_NAME);
			try {
				this.password = decrypt(encryptedPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(name.equals(LOCALE_LANGUAGE_TAG_NAME)) {
			this.localeLanguage = attributes.getValue(LANGUAGE_ATTRIBUTE_NAME);	
		} else if(name.equals(SHOW_INFERRED_HIERARCHY_TAG_NAME)) {
			this.showInferredHierarchy = Boolean.parseBoolean(attributes.getValue(SHOW_ATTRIBUTE_NAME));	
		}
	}	

	
	/**
	 * Metoda zapisujaca konfiguracje aplikacji do pliku.
	 * 
	 * @param properties konfiguracja aplikacji w postaci (klucz-wartosc)
	 * @param filePath sciezka do pliku pliku do ktorego ma nastapic zapis
	 * @return true operacja sie powiodla, false w przeciwnym wypadku
	 */
	public void saveToFile(String filePath) {
		try {
			File file = new File(filePath);
			
			file.createNewFile();
			
			FileWriter fileWriter = new FileWriter(file);			
			
			fileWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n\n");
			
			fileWriter.write("<config>\n");
			
			String encryptedPassword = "";
			
			if(!this.password.equals("")) {
				encryptedPassword = encrypt(this.password);
			}
				
			fileWriter.write("\t<user name=\""+this.userName+"\" password=\""+encryptedPassword+"\" />\n");						
			fileWriter.write("\t<showInferredHierarchy show=\""+this.showInferredHierarchy+"\" />\n");
			fileWriter.write("\t<localeLanguage language=\""+this.localeLanguage+"\" />\n");
			
			fileWriter.write("</config>");
			fileWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	/**
	 * Metoda szyfrujaca dana wiadomosc, wykorzytywana przy zapisie hasla 
	 * uzytkownika do pliku w celu utajnienia.
	 * 
	 * @param message wiadomosc w otwartym tekscie
	 * @return wiadomosc zaszyfrowana
	 * @throws Exception 
	 */
	public static String encrypt(String message) throws Exception {
				
		SecretKeySpec spec = new SecretKeySpec(pass.getBytes(), "AES");
				
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, spec);	
		
		byte[] pom = message.getBytes();
		
		byte[] enc = cipher.doFinal(pom);
		
		String returnValue = new String();
		
		for(int i=0;i<enc.length;i++) {
			String temp =Integer.toHexString(0xff &enc[i]);
			if(temp.length()==1) {
				temp = "0".concat(temp);
			}
			returnValue = returnValue.concat(temp);
		}
		
		return returnValue;
	}
	
	/**
	 * Metoda deszyfrujaca dana wiadomosc, wykorzystywana przy odczycie 
	 * hasla uzytkownika w celu uwierzytelnia w portalu.
	 * 
	 * @param message wiadomosc zaszyfrowana
	 * @return wiadomosc odszyfrowana
	 * @throws Exception
	 */
	public static String decrypt(String message) throws Exception {
		
		byte[] pom = new byte[message.length()/2];
		int beginIndex=0, endIndex=2;
		for(int i=0;i<message.length()/2;i++) {						
			pom[i] = (byte)Integer.parseInt(message.substring(beginIndex, endIndex), 16);			
			beginIndex+=2;
			endIndex+=2;
		}
		
		SecretKeySpec spec = new SecretKeySpec(pass.getBytes(), "AES");
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, spec);
		
		byte[] enc = cipher.doFinal(pom);
		return new String(enc);
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocaleLanguage() {
		return localeLanguage;
	}

	public void setLocaleLanguage(String localeLanguage) {
		this.localeLanguage = localeLanguage;
	}

	public boolean isShowInferredHierarchy() {
		return showInferredHierarchy;
	}

	public void setShowInferredHierarchy(boolean showInferredHierarchy) {
		this.showInferredHierarchy = showInferredHierarchy;
	}

}
