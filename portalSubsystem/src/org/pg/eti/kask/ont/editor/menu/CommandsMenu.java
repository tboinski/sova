package org.pg.eti.kask.ont.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.dialogs.ChooseOntologyDialog;
import org.pg.eti.kask.ont.editor.table.model.OntologyInfoModel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.changesExporter.ExportChangesWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.DownloadOntologyWizard;
import org.pg.eti.kask.ont.editor.wizard.versionCreator.CreateNewVersionWizard;

/**
 * 
 *
 * @author Andrzej Jakowski
 */
public class CommandsMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 6737362288575598156L;
	
	private ResourceBundle messages;
	
	private Logic logic;
	private MainFrame parentFrame;
	
	private JMenuItem newVersionMenuItem;
	private JMenuItem importOntologyMenuItem;
	private JMenuItem exportChangesMenuItem;
	private JMenuItem lockOntologyMenuItem;
	private JMenuItem unLockOntologyMenuItem;
	private JMenuItem logoutMenuItem;

	public CommandsMenu(MainFrame parentFrame) {
		this.messages = EditorUtil.getResourceBundle(CommandsMenu.class);
		this.logic = Logic.getInstance();
		this.parentFrame = parentFrame;
		
		this.newVersionMenuItem = new JMenuItem();
		this.importOntologyMenuItem = new JMenuItem();
		this.exportChangesMenuItem = new JMenuItem();
		this.lockOntologyMenuItem = new JMenuItem();
		this.unLockOntologyMenuItem = new JMenuItem();
		this.logoutMenuItem = new JMenuItem();
		
		initialize();
		reinitializeMenu();
	}
	
	private void initialize() {
		newVersionMenuItem.setText(messages.getString("newVersionMenuItem.text"));
		newVersionMenuItem.addActionListener(this);
		
		importOntologyMenuItem.setText(messages.getString("importOntologyMenuItem.text"));
		importOntologyMenuItem.addActionListener(this);
		
		exportChangesMenuItem.setText(messages.getString("exportChangesMenuItem.text"));
		exportChangesMenuItem.addActionListener(this);
		
		lockOntologyMenuItem.setText(messages.getString("lockOntologyMenuItem.text"));
		lockOntologyMenuItem.addActionListener(this);
		
		unLockOntologyMenuItem.setText(messages.getString("unLockOntologyMenuItem.text"));
		unLockOntologyMenuItem.addActionListener(this);
		
		logoutMenuItem.setText(messages.getString("logoutMenuItem.text"));
		logoutMenuItem.addActionListener(this);
		
		this.add(newVersionMenuItem);
		this.add(importOntologyMenuItem);
		this.add(exportChangesMenuItem);
		this.addSeparator();
		this.add(lockOntologyMenuItem);
		this.add(unLockOntologyMenuItem);
		this.addSeparator();
		this.add(logoutMenuItem);
		
	}
	
	public void reinitializeMenu() {
		ProjectDescriptor desc = logic.getDescriptor();
		
		
		if(desc == null) {
			this.exportChangesMenuItem.setEnabled(false);	
		} else {
			this.exportChangesMenuItem.setEnabled(true);
		}		
		
		if(logic.isUserLoggedIn()) {
			this.logoutMenuItem.setEnabled(true);
		} else {
			this.logoutMenuItem.setEnabled(false);
		}
		
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newVersionMenuItem) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				CreateNewVersionWizard wizard = new CreateNewVersionWizard(parentFrame);
				wizard.open();
			}
		} else if(e.getSource() == importOntologyMenuItem) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				DownloadOntologyWizard wizard = new DownloadOntologyWizard(parentFrame);
				wizard.open();
			}
		} else if(e.getSource() == logoutMenuItem) {
			logic.logoutUser();
			JOptionPane.showMessageDialog(parentFrame, messages.getString("userLogout.body"), messages.getString("userLogout.title"), JOptionPane.ERROR_MESSAGE);
			reinitializeMenu();
		} else if(e.getSource() == exportChangesMenuItem) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				ExportChangesWizard wizard = new ExportChangesWizard(parentFrame);
				wizard.open();
			}		

		} else if(e.getSource() == lockOntologyMenuItem) {
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				List<BaseURI> allowedOntologies = logic.getAllowedOntologies(logic.getLoggedInUser().getUsername());
				List<BaseURI> myOntologies = new ArrayList<BaseURI>();
				for(BaseURI uri: allowedOntologies) {
					try {
						if(!logic.isLocked(uri)) {
							myOntologies.add(uri);
						}
					} catch (Exception ex) {						
						ex.printStackTrace();
					}
				}
				OntologyInfoModel model = new OntologyInfoModel(myOntologies);
				ChooseOntologyDialog dialog = new ChooseOntologyDialog(model);
				dialog.open();
				BaseURI ontologyURI = dialog.getResult();
				if(ontologyURI != null) {
					boolean result = logic.lockOntology(ontologyURI);
					if(result) {
						JOptionPane.showMessageDialog(parentFrame, messages.getString("lockOntologySuccess.title"), messages.getString("lockOntologySuccess.body"), JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(parentFrame, messages.getString("lockOntologyError.title"), messages.getString("lockOntologyError.body"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if(e.getSource() == unLockOntologyMenuItem) {		
			if(logic.checkIfUserLoggedIn(parentFrame)) {
				List<BaseURI> allowedOntologies = logic.getAllowedOntologies(logic.getLoggedInUser().getUsername());
				List<BaseURI> myOntologies = new ArrayList<BaseURI>();
				for(BaseURI uri: allowedOntologies) {
					try {
						if(logic.isLocked(uri)) {
							myOntologies.add(uri);
						}
					} catch (Exception ex) {						
						ex.printStackTrace();
					}
				}
				OntologyInfoModel model = new OntologyInfoModel(myOntologies);
				ChooseOntologyDialog dialog = new ChooseOntologyDialog(model);
				dialog.open();
				BaseURI ontologyURI = dialog.getResult();
				if(ontologyURI != null) {
					boolean result = logic.unlockOntology(ontologyURI);
					if(result) {
						JOptionPane.showMessageDialog(parentFrame, messages.getString("unLockOntologySuccess.title"), messages.getString("unLockOntologySuccess.body"), JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(parentFrame, messages.getString("unLockOntologyError.title"), messages.getString("unLockOntologyError.body"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
	
}
