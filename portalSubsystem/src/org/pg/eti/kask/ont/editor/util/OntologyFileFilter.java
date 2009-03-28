package org.pg.eti.kask.ont.editor.util;

import java.io.File;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileFilter;

import org.pg.eti.kask.ont.editor.consts.Constants;

public class OntologyFileFilter extends FileFilter {

	private ResourceBundle messages;
	
	private boolean acceptProjectFiles;
	
	
	public OntologyFileFilter(boolean acceptProjectFiles) {
		this.messages = EditorUtil.getResourceBundle(OntologyFileFilter.class);
		this.acceptProjectFiles = acceptProjectFiles;
	}
	
	@Override
	public boolean accept(File f) {
		//sprawdzenie czy nie jest katalogiem
		if (f.isDirectory()) {
            return true;
        }
		
		//sprawdzenie czy nie jest to plik projektu
		if(acceptProjectFiles && f.getName().equals(Constants.PROJECT_FILE_NAME)) {
			return true;
		}
		

		//pobranie rozszerzenia pliku
        String extension = getExtension(f);
        if(extension != null) {
        	if(extension.equals(Constants.OWL_ONTOLOGY_FORMAT) || extension.equals(Constants.RDF_ONTOLOGY_FORMAT) ) {
        		return true;
            } else {
                return false;
            }
        }

        return false;

	}
	
	@Override
	public String getDescription() {
		return messages.getString("ontologyFiles.text");
	}
	
	public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }


}
