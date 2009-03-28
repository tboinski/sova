package org.pg.eti.kask.ont.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.OntologyFileFilter;
import org.pg.eti.kask.ont.editor.wizard.ontologyCreator.CreateNewOntologyWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.DownloadOntologyWizard;
import org.semanticweb.owl.model.OWLOntology;

/**
 * Klasa reprezentujaca menu plik. Zostaly w nim umieszczone elementy
 * pozwalajace na tworzenie nowych projektow, ich zapis na dysk i odczyt.
 * 
 * @author Andrzej Jakowski
 */
public class FileMenu extends JMenu implements ActionListener{

	private static final long serialVersionUID = -2675613129765969273L;
	
	private ResourceBundle messages;
	
	private MainFrame parentFrame;
	
	private Logic logic;
	
	private JMenu newMenu;
	private JMenu exportMenu;
	private JMenuItem newOntologyMenuItem;
	private JMenuItem newProjectMenuItem;
	private JMenuItem openProjectMenuItem;
	private JMenuItem saveProjectMenuItem;
	private JMenuItem exportRDFMenuItem;
	private JMenuItem exportOWLMenuItem;
	private JMenuItem exitMenuItem;
	
	
	/**
	 * Bezparametrowy konstruktor tworzacy menu plik i inicjalizujacy wszystkie jego
	 * komponenty.
	 */
	public FileMenu(MainFrame parentFrame){
		this.messages = EditorUtil.getResourceBundle(FileMenu.class);
		this.parentFrame = parentFrame;
		
		this.logic = Logic.getInstance();
		
		this.newMenu= new JMenu();
		this.newOntologyMenuItem = new JMenuItem();
		this.newProjectMenuItem = new JMenuItem();
		this.openProjectMenuItem = new JMenuItem();
		this.saveProjectMenuItem = new JMenuItem();
		this.exportMenu= new JMenu();
		this.exportRDFMenuItem = new JMenuItem();
		this.exportOWLMenuItem = new JMenuItem();
		this.exitMenuItem = new JMenuItem();
		
		initialize();
		reinitializeMenu();
	}
		
	private void initialize() {
		
		//new submenu
		newMenu.setText(messages.getString("newMenu.text"));
		
		newOntologyMenuItem.setText(messages.getString("newOntologyMenuItem.text"));
		newOntologyMenuItem.setActionCommand(CommandsConstans.CREATE_ONTOLOGY_COMMAND);
		newOntologyMenuItem.addActionListener(this);
		
		newProjectMenuItem.setText(messages.getString("newProjectMenuItem.text"));
		newProjectMenuItem.setActionCommand(CommandsConstans.CREATE_PROJECT_COMMAND);
		newProjectMenuItem.addActionListener(this);
		
		newMenu.add(newOntologyMenuItem);
		newMenu.add(newProjectMenuItem);
		
		//open menu item
		openProjectMenuItem.setText(messages.getString("openProjectMenuItem.text"));
		openProjectMenuItem.setActionCommand(CommandsConstans.OPEN_PROJECT_COMMAND);
		openProjectMenuItem.addActionListener(this);
		
		//save menu item
		saveProjectMenuItem.setText(messages.getString("saveProjectMenuItem.text"));
		saveProjectMenuItem.setActionCommand(CommandsConstans.SAVE_PROJECT_COMMAND);
		saveProjectMenuItem.addActionListener(this);
		
		//export menu
		exportMenu.setText(messages.getString("exportMenu.text"));
		
		//export menu items
		exportRDFMenuItem.setText(messages.getString("exportRDFMenuItem.text"));
		exportRDFMenuItem.setActionCommand(CommandsConstans.EXPORT_TO_RDF_COMMAND);
		exportRDFMenuItem.addActionListener(this);
		
		exportOWLMenuItem.setText(messages.getString("exportOWLMenuItem.text"));
		exportOWLMenuItem.setActionCommand(CommandsConstans.EXPORT_TO_OWL_COMMAND);
		exportOWLMenuItem.addActionListener(this);
		
		exportMenu.add(exportRDFMenuItem);
		exportMenu.add(exportOWLMenuItem);
		
		//exit menu item		
		exitMenuItem.setText(messages.getString("exitMenuItem.text"));
		exitMenuItem.setActionCommand(CommandsConstans.EXIT_COMMAND);
		exitMenuItem.addActionListener(this);
		
		this.add(newMenu);
		this.add(openProjectMenuItem);
		this.add(saveProjectMenuItem);
		this.add(exportMenu);
		this.addSeparator();
		this.add(exitMenuItem);
	}
	
	public void reinitializeMenu() {
		String loadedOntologyURI = logic.getLoadedOntologyURI();
		
		if(loadedOntologyURI == null || loadedOntologyURI.equals("")) {
			this.exportMenu.setEnabled(false);		
			this.saveProjectMenuItem.setEnabled(false);
		} else {
			this.exportMenu.setEnabled(true);
			this.saveProjectMenuItem.setEnabled(true);
		}		
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.CREATE_ONTOLOGY_COMMAND)) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				CreateNewOntologyWizard wizard = new CreateNewOntologyWizard(parentFrame);
				wizard.open();
			}
		} else if(e.getActionCommand().equals(CommandsConstans.CREATE_PROJECT_COMMAND)) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				DownloadOntologyWizard wizard = new DownloadOntologyWizard(parentFrame);
				wizard.open();
			}
		} else if(e.getActionCommand().equals(CommandsConstans.OPEN_PROJECT_COMMAND)) {
			parentFrame.getToolbarPanel().openOntology();
			reinitializeMenu();
		} else if(e.getActionCommand().equals(CommandsConstans.EXPORT_TO_OWL_COMMAND)) {
			exportOntology(Constants.OWL_ONTOLOGY_FORMAT);
		} else if(e.getActionCommand().equals(CommandsConstans.EXPORT_TO_RDF_COMMAND)) {
			exportOntology(Constants.RDF_ONTOLOGY_FORMAT);
		} else if(e.getActionCommand().equals(CommandsConstans.SAVE_PROJECT_COMMAND)) {
			parentFrame.getToolbarPanel().saveOntology();
		} else if(e.getActionCommand().equals(CommandsConstans.EXIT_COMMAND)) {
			parentFrame.closingApplication(parentFrame);
		}
						
	}
	
	public void exportOntology(String ontologyFormat) {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new OntologyFileFilter(false));
        fc.setAcceptAllFileFilterUsed(false);
		
		fc.setCurrentDirectory(new File(Main.APPLICATION_FOLDER_PATH));

		int retval = fc.showSaveDialog(parentFrame);
		if(retval == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			if(ontologyFormat.equals(Constants.OWL_ONTOLOGY_FORMAT)) {
				if(!file.getName().endsWith(".owl")) {
					file = new File(URI.create((file.toURI().toString() + ".owl")));							
				}
			}
			
			if(ontologyFormat.equals(Constants.RDF_ONTOLOGY_FORMAT)) {
				if(!file.getName().endsWith(".rdf")) {
					file = new File(URI.create((file.toURI().toString() + ".rdf")));
				}
			}
			String ontologyURI = logic.getLoadedOntologyURI();
			
			OWLOntology ontology = logic.getOntology(ontologyURI);
			logic.exportOntology(ontology, file.toURI().toString(), ontologyFormat);
			String message = messages.getString("exportOntologyDialogBody.text")+" "+file.getAbsolutePath();
			JOptionPane.showMessageDialog(parentFrame, message, messages.getString("exportOntologyDialogTitle.text"), JOptionPane.INFORMATION_MESSAGE);
		}
	}
		
}
