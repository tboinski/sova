package org.pg.eti.kask.ont.editor.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.consts.CommandsConstans;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.consts.IconsConstans;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.OntologyFileFilter;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.changesExporter.ExportChangesWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyCreator.CreateNewOntologyWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.DownloadOntologyWizard;
import org.semanticweb.owl.model.OWLOntology;

/**
 * 
 * @author Andrzej Jakowski
 */
public class ToolbarPanel extends JToolBar implements ActionListener, IconsConstans{

	private static final long serialVersionUID = -51428016824774477L;
	
	private ResourceBundle messages;
	
	private MainFrame parentFrame;
	
	private Logic logic;
	
	private JButton newOntologyButton;
	private JButton openProjectButton;
	private JButton saveProjectButton;
	private JButton importOntologyButton;
	private JButton exportChanges;
	private JButton updateOntology;

	public ToolbarPanel(MainFrame parentFrame){
		this.messages = EditorUtil.getResourceBundle(ToolbarPanel.class);
		this.parentFrame = parentFrame;
		this.logic = Logic.getInstance();
		
		this.newOntologyButton = new JButton(createImageIcon(ICON_GENERAL_PACKAGE_NAME.concat(NEW_ICON_NAME_24)));
		this.openProjectButton = new JButton(createImageIcon(ICON_GENERAL_PACKAGE_NAME.concat(OPEN_ICON_NAME_24)));
		this.saveProjectButton = new JButton(createImageIcon(ICON_GENERAL_PACKAGE_NAME.concat(SAVE_ICON_NAME_24)));
		this.importOntologyButton = new JButton(createImageIcon(ICON_GENERAL_PACKAGE_NAME.concat(IMPORT_ICON_NAME_24)));
		this.exportChanges = new JButton(createImageIcon(ICON_GENERAL_PACKAGE_NAME.concat(EXPORT_ICON_NAME_24)));
		this.updateOntology = new JButton(createImageIcon(ICON_GENERAL_PACKAGE_NAME.concat(REFRESH_ICON_NAME_24)));
		
		initialize();
		reinitializeButtons();
	}
	
	private void initialize() {
		this.newOntologyButton.setToolTipText(messages.getString("newOntologyButton.toolTipText"));
		this.newOntologyButton.addActionListener(this);
		this.newOntologyButton.setActionCommand(CommandsConstans.CREATE_ONTOLOGY_COMMAND);
		
		this.openProjectButton.setToolTipText(messages.getString("openProjectButton.toolTipText"));
		this.openProjectButton.addActionListener(this);
		this.openProjectButton.setActionCommand(CommandsConstans.OPEN_PROJECT_COMMAND);
		
		this.saveProjectButton.setToolTipText(messages.getString("saveProjectButton.toolTipText"));
		this.saveProjectButton.addActionListener(this);
		this.saveProjectButton.setActionCommand(CommandsConstans.SAVE_PROJECT_COMMAND);
		
		this.importOntologyButton.setToolTipText(messages.getString("importOntologyButton.toolTipText"));
		this.importOntologyButton.addActionListener(this);
		this.importOntologyButton.setActionCommand(CommandsConstans.IMPORT_ONTOLOGY_COMMAND);
		
		this.exportChanges.setToolTipText(messages.getString("exportChanges.toolTipText"));
		this.exportChanges.addActionListener(this);
		this.exportChanges.setActionCommand(CommandsConstans.EXPORT_ONTOLOGY_CHANGES_COMMAND);
		
		this.updateOntology.setToolTipText(messages.getString("updateOntology.toolTipText"));
		this.updateOntology.addActionListener(this);
		this.updateOntology.setActionCommand(CommandsConstans.UPDATE_ONTOLOGY_COMMAND);
		
		this.add(newOntologyButton);
		this.add(openProjectButton);
		this.add(saveProjectButton);
		this.addSeparator();
		this.add(importOntologyButton);
		this.add(exportChanges);
		this.addSeparator();
		//this.add(updateOntology);
		this.setFloatable(false);
	}
	
	public void reinitializeButtons() {
		ProjectDescriptor desc = logic.getDescriptor();
		
		
		if(desc == null) {
			this.exportChanges.setEnabled(false);			
		} else {
			this.exportChanges.setEnabled(true);
		}
		
		if(desc == null || logic.getLoadedOntologyURI() == null || logic.getLoadedOntologyURI().equals("")) {
			this.saveProjectButton.setEnabled(false);
		} else {
			this.saveProjectButton.setEnabled(true);
		}
				
	}
	
	/**
	 * Metoda tworzaca ikony dla toolbara na podstawie sciezki 
	 * (sciezka okresla polozenie danego zasobu w obrebie ClassLoader'a).
	 * 
	 * @param iconPath sciezka okreslajaca polozenie pliku ikony
	 * @return obiekt bedacy 
	 */
	private ImageIcon createImageIcon(String iconPath) {
		URL imageUrl =  getClass().getResource(iconPath);		
		return new ImageIcon(imageUrl);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(CommandsConstans.CREATE_ONTOLOGY_COMMAND)) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				CreateNewOntologyWizard wizard = new CreateNewOntologyWizard(parentFrame);
				wizard.open();
			}
		} else if(e.getActionCommand().equals(CommandsConstans.OPEN_PROJECT_COMMAND)) {
			openOntology();
		} else if(e.getActionCommand().equals(CommandsConstans.SAVE_PROJECT_COMMAND)) {
			saveOntology();
		} else if(e.getActionCommand().equals(CommandsConstans.IMPORT_ONTOLOGY_COMMAND)) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				DownloadOntologyWizard wizard = new DownloadOntologyWizard(parentFrame);
				wizard.open();
			}
			
		} else if(e.getActionCommand().equals(CommandsConstans.EXPORT_ONTOLOGY_CHANGES_COMMAND)) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				ExportChangesWizard wizard = new ExportChangesWizard(parentFrame);
				wizard.open();
			}
		} else if(e.getActionCommand().equals(CommandsConstans.UPDATE_ONTOLOGY_COMMAND)) {
			
		}		
			
	}
	
	public void openOntology() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(Main.APPLICATION_FOLDER_PATH));
		fc.addChoosableFileFilter(new OntologyFileFilter(true));
        fc.setAcceptAllFileFilterUsed(false);
        
		int retval = fc.showOpenDialog(parentFrame);
		if(retval == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			String fileName = file.getName();
			
			if(fileName.equals(Constants.PROJECT_FILE_NAME)) {
				
				ProjectDescriptor descriptor = new ProjectDescriptor();
				descriptor.processFile(file.toURI().toString());
									
				OWLOntology ontology =  logic.loadProject(descriptor);
				parentFrame.loadOntology(ontology);
				reinitializeButtons();
				parentFrame.getMenu().reintializeMenus();
				
				int retvalue = JOptionPane.showConfirmDialog(parentFrame, messages.getString("checkIfIsLocked.body"), messages.getString("checkIfIsLocked.title"), JOptionPane.YES_NO_OPTION);
				
				if(retvalue == JOptionPane.YES_OPTION) {
					try {
						if(logic.checkIfUserLoggedIn(parentFrame)) {
							boolean temp = logic.isLocked(new BaseURI(descriptor.getOntologyBaseURI()));
							
							if(!temp) {
								String message = messages.getString("projectLoaded.text") + "\n"
								+messages.getString("projectLoaded1.text");
						
								JOptionPane.showMessageDialog(parentFrame, message, messages.getString("projectLoaded.title"),JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(parentFrame, messages.getString("ontologyLocked.body"), messages.getString("projectLoaded.title"),JOptionPane.INFORMATION_MESSAGE);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			} else if(fileName.endsWith(".owl") || fileName.endsWith(".rdf")) {
				OWLOntology ontology =  logic.loadOntologyFromFile(file.toURI().toString());
				parentFrame.loadOntology(ontology);
				
				String message = messages.getString("ontologyLoaded.text") + "\n"
				+messages.getString("ontologyLoaded1.text");
			
				JOptionPane.showMessageDialog(parentFrame, message, messages.getString("ontologyLoaded.title"),JOptionPane.INFORMATION_MESSAGE);
				parentFrame.getMenu().reintializeMenus();
			}
				
		}
	}
	
	public void saveOntology() {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new OntologyFileFilter(false));
        fc.setAcceptAllFileFilterUsed(false);
		
		ProjectDescriptor desc = logic.getDescriptor();
		if(desc!=null) {
			fc.setCurrentDirectory(new File(Main.APPLICATION_FOLDER_PATH+"/"+desc.getProjectName()));
		}
		int retval = fc.showSaveDialog(parentFrame);
		if(retval == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String ontologyURI = logic.getDescriptor().getOntologyBaseURI();
			
			if(!file.getName().endsWith(".owl")) {
				file = new File(URI.create((file.toURI().toString() + ".owl")));							
			}

			OWLOntology ontology = logic.getOntology(ontologyURI);
			logic.saveOntologyToFile(ontology, file.toURI().toString(), null);
			logic.setChanged(false);
			
			if(desc != null) {
				desc.setLastModifiedOntologyFileUri(file.toURI().toString());
				desc.saveToFile(Main.APPLICATION_FOLDER_PATH+"/"+desc.getProjectName()+"/"+Constants.PROJECT_FILE_NAME);
			}
		}
	}
}
