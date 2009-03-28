package org.pg.eti.kask.ont.editor.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class ProjectDescriptor extends DefaultHandler {
	
	public static final String ROOT_TAG_NAME="project";
	public static final String ONTOLOGY_TAG_NAME="ontology";
	public static final String BASE_ONTOLOGY_TAG_NAME="baseOntologyFile";
	public static final String LAST_MODIFIED_ONTOLOGY_TAG_NAME="lastModifiedOntologyFile";
	public static final String NAME_ATTRIBUTE_NAME="name";
	public static final String BASE_URI_ATTRIBUTE_NAME="baseURI";
	public static final String VERSION_ATTRIBUTE_NAME="version";
	public static final String SUBVERSION_ATTRIBUTE_NAME="subVersion";
	public static final String FILE_URI_ATTRIBUTE_NAME="fileURI";
	
	private String projectName;
	
	private String ontologyBaseURI;
	
	private int version;
	
	private int subVersion;
	
	private String baseOntologyFileUri;
	
	private String lastModifiedOntologyFileUri;
	
	public ProjectDescriptor() {
		this.projectName = "";
		this.ontologyBaseURI = "";
		this.version = 0;
		this.subVersion = 0;
		this.baseOntologyFileUri = "";
		this.lastModifiedOntologyFileUri = "";
	}	
	
	
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
	
	public void saveToFile(String filePath) {
		try {
			File file = new File(filePath);
	
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			
			fileWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n\n");
			
			fileWriter.write("<project name=\"" + this.projectName + "\" >\n");
			
			fileWriter.write("\t<ontology baseURI=\"" + this.ontologyBaseURI + "\" version=\""+this.version
					+ "\" subVersion=\"" + this.subVersion + "\" />\n");
			
			fileWriter.write("\t<baseOntologyFile fileURI=\"" + this.baseOntologyFileUri + "\" />\n");
			
			fileWriter.write("\t<lastModifiedOntologyFile fileURI=\"" + this.lastModifiedOntologyFileUri + "\" />\n");
			
			fileWriter.write("</project>");
			fileWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		if(name.equals(ROOT_TAG_NAME)) {
			this.projectName = attributes.getValue(NAME_ATTRIBUTE_NAME);	
		} else if(name.equals(ONTOLOGY_TAG_NAME)) {
			this.ontologyBaseURI = attributes.getValue(BASE_URI_ATTRIBUTE_NAME);	
			this.version = Integer.parseInt(attributes.getValue(VERSION_ATTRIBUTE_NAME));	
			this.subVersion = Integer.parseInt(attributes.getValue(SUBVERSION_ATTRIBUTE_NAME));	
		} else if(name.equals(BASE_ONTOLOGY_TAG_NAME)) {
			this.baseOntologyFileUri = attributes.getValue(FILE_URI_ATTRIBUTE_NAME);
		} else if(name.equals(LAST_MODIFIED_ONTOLOGY_TAG_NAME)) {
			this.lastModifiedOntologyFileUri = attributes.getValue(FILE_URI_ATTRIBUTE_NAME);
		}
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOntologyBaseURI() {
		return ontologyBaseURI;
	}

	public void setOntologyBaseURI(String ontologyBaseURI) {
		this.ontologyBaseURI = ontologyBaseURI;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getSubVersion() {
		return subVersion;
	}

	public void setSubVersion(int subVersion) {
		this.subVersion = subVersion;
	}

	public String getBaseOntologyFileUri() {
		return baseOntologyFileUri;
	}

	public void setBaseOntologyFileUri(String baseOntologyFileUri) {
		this.baseOntologyFileUri = baseOntologyFileUri;
	}

	public String getLastModifiedOntologyFileUri() {
		return lastModifiedOntologyFileUri;
	}

	public void setLastModifiedOntologyFileUri(String lastModifiedOntologyFileUri) {
		this.lastModifiedOntologyFileUri = lastModifiedOntologyFileUri;
	}

	@Override
	public String toString() {
		String returnValue = new String();
		
		returnValue = "PROJECT: " + this.projectName
			+ "\nONTOLOGY BASE URI: " + this.ontologyBaseURI
			+ "\nVERSION: " + this.version
			+ "\nSUBVERSION: " + this.subVersion
			+ "\nBASE FILE: " + this.baseOntologyFileUri
			+ "\nMODIFIED FILE: " + this.lastModifiedOntologyFileUri;
		return returnValue;
	}

}
