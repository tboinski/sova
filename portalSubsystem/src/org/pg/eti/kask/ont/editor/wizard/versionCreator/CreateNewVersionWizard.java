package org.pg.eti.kask.ont.editor.wizard.versionCreator;

import java.io.File;
import java.net.URI;
import java.util.ResourceBundle;

import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.pg.eti.kask.ont.editor.wizard.versionCreator.model.ApplyChangesPageModel;
import org.pg.eti.kask.ont.editor.wizard.versionCreator.model.ChooseOntologyPageModel;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;

public class CreateNewVersionWizard extends AbstractWizard {

	private static final long serialVersionUID = -5130660618080708702L;
	
	private ResourceBundle messages;
	
	private MainFrame parentFrame;
	
	private AbstractPage[] pages;
	
	public CreateNewVersionWizard(MainFrame parentFrame) {
		this.messages = EditorUtil.getResourceBundle(CreateNewVersionWizard.class);
		
		this.pages = new AbstractPage[2];
		this.pages[0] = new ChooseOntologyPage(this);
		this.pages[1] = new ApplyChangesPage(this);
		this.parentFrame = parentFrame;
	}
		
	public void setOntologyURI(VersionedURI ontologyURI) {
		((ApplyChangesPage)this.pages[1]).setOntologyURI(ontologyURI);
	}
	
	@Override
	protected void doCancel() {
		
	}

	@Override
	protected void doFinish() {
		ChooseOntologyPageModel page1result = ((ChooseOntologyPage)this.pages[0]).getResult();
		ApplyChangesPageModel page2result = ((ApplyChangesPage)this.pages[1]).getResult();
		
		Logic logic = Logic.getInstance();
				
		logic.createNewVersion(page2result.getOntologyURI(), page2result.getPropsToAdd(), page2result.getPropsToRemove(), page2result.isNewVersionIndicator());
				
		if(!page1result.getProjectName().trim().equals("")) {
			String projectName = page1result.getProjectName();
			
			File projectFolder = new File(Main.APPLICATION_FOLDER_PATH+projectName);
			if(!projectFolder.exists()) {
				projectFolder.mkdir();
			}
			
			if(page2result.isNewVersionIndicator()) {
				page2result.getOntologyURI().setOntologyVersion(page2result.getOntologyURI().getOntologyVersion()+1);
			} else {
				page2result.getOntologyURI().setOntologySubVersion(page2result.getOntologyURI().getOntologySubversion()+1);
			}
			
			ProjectDescriptor projectDesc = new ProjectDescriptor();
			projectDesc.setOntologyBaseURI(page2result.getOntologyURI().getBaseURI().getURIAsString());
			projectDesc.setProjectName(page1result.getProjectName());
			projectDesc.setBaseOntologyFileUri(projectFolder.toURI().toString()+"base.ont");
			projectDesc.setSubVersion(page2result.getOntologyURI().getOntologySubversion());
			projectDesc.setVersion(page2result.getOntologyURI().getOntologyVersion());
			projectDesc.saveToFile(projectFolder.getAbsolutePath()+"/project.xml");
	
			
			OWLOntology ontology = logic.getOntology(page2result.getOntologyURI());
			if(ontology == null) {
				try {
					ontology = logic.getOntologyManager().createOntology(URI.create(page2result.getOntologyURI().getBaseURI().getURIAsString()));
				} catch (OWLOntologyCreationException e) {
					e.printStackTrace();
				}
			}
					
			logic.saveOntologyToFile(ontology, projectFolder.toURI().toString()+"base.ont", page2result.getOntologyURI());
			logic.setDescriptor(projectDesc);
			
			logic.loadProject(projectDesc);
			
			parentFrame.getToolbarPanel().reinitializeButtons();
			parentFrame.getMenu().reintializeMenus();
			parentFrame.loadOntology(ontology);
		}
		
		
	}
	
	@Override
	public int getWizardWidth() {
		return 650;
	}

	@Override
	public int getWizardHeight() {
		return 700;
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
