package org.pg.eti.kask.ont.editor.wizard.ontologyDownloader;

import java.io.File;
import java.net.URI;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.model.ChooseOntologyPageModel;
import org.pg.eti.kask.ont.editor.wizard.ontologyDownloader.model.ProjectPropertiesPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;

public class DownloadOntologyWizard extends AbstractWizard {

	private static final long serialVersionUID = 5202426276279273515L;
	
	private ResourceBundle messages;
	
	private AbstractPage[] pages;
	
	private MainFrame parentFrame;
	
	public DownloadOntologyWizard(MainFrame parentFrame) {
		
		this.messages = EditorUtil.getResourceBundle(DownloadOntologyWizard.class);
		this.pages = new AbstractPage[2];
		this.parentFrame = parentFrame;
		this.pages[0] = new ChooseOntologyPage(this);
		this.pages[1] = new ProjectPropertiesPage(this);		
	}	

	@Override
	protected void doCancel() {

	}

	@Override
	protected void doFinish() {
		//utworzenie katalogu projektu i zapis ontologii do pliku
		ChooseOntologyPageModel page1result = ((ChooseOntologyPage)this.pages[0]).getResult();
		ProjectPropertiesPageModel page2result = ((ProjectPropertiesPage)this.pages[1]).getResult();
		
		//utworzenie katalogu - projectName
		String projectName = page2result.getProjectName();
		
		File projectFolder = new File(Main.APPLICATION_FOLDER_PATH+projectName);
		if(!projectFolder.exists()) {
			projectFolder.mkdir();
		}
				
		
		ProjectDescriptor projectDesc = new ProjectDescriptor();
		projectDesc.setOntologyBaseURI(page1result.getOntologyUri().getBaseURI().getURIAsString());
		projectDesc.setProjectName(page2result.getProjectName());
		projectDesc.setBaseOntologyFileUri(projectFolder.toURI().toString()+"base.ont");
		projectDesc.setSubVersion(page1result.getOntologyUri().getOntologySubversion());
		projectDesc.setVersion(page1result.getOntologyUri().getOntologyVersion());
		projectDesc.saveToFile(projectFolder.getAbsolutePath()+"/project.xml");
		
		Logic logic = Logic.getInstance();			
		
		OWLOntology ontology = logic.getOntology(page1result.getOntologyUri());
		if(ontology == null) {
			try {
				ontology = logic.getOntologyManager().createOntology(URI.create(page1result.getOntologyUri().getBaseURI().getURIAsString()));
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
				
		logic.saveOntologyToFile(ontology, projectFolder.toURI().toString()+"base.ont", page1result.getOntologyUri());
		logic.setDescriptor(projectDesc);
		
		logic.loadProject(projectDesc);
		
		String message = messages.getString("information.body")+"\n"
			+ messages.getString("information1.body")+"\n"+ projectFolder.getAbsolutePath()+"\\base.ont";
		
		parentFrame.getToolbarPanel().reinitializeButtons();
		parentFrame.getMenu().reintializeMenus();
		parentFrame.loadOntology(ontology);
		
		JOptionPane.showMessageDialog(this, message, messages.getString("information.title"),JOptionPane.INFORMATION_MESSAGE);		
		
	}

	@Override
	public int getWizardWidth() {
		return 550;
	}

	@Override
	public int getWizardHeight() {
		return 600;
	}

	@Override
	protected void doStart() {

	}

	@Override
	protected AbstractPage[] getPages() {
		return pages;
	}

	@Override
	protected AbstractPage getStartPage() {
		return pages[0];
	}

	@Override
	protected String getWizardDescription() {
		return messages.getString("wizardDescription.text");
	}

	@Override
	protected String getWizardTitle() {
		return messages.getString("wizardTitle.text");
	}

}
