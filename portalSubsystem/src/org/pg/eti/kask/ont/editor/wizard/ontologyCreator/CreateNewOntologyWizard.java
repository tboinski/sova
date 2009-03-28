package org.pg.eti.kask.ont.editor.wizard.ontologyCreator;

import java.io.File;
import java.net.URI;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.Main;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.ontologyCreator.model.OntologyInfoPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;

public class CreateNewOntologyWizard extends AbstractWizard {

	private static final long serialVersionUID = 8418329783900991475L;
	
	private ResourceBundle messages;
	
	//tablica z kolejnymi stronami wizarda
	private AbstractPage[] pages;
	
	private MainFrame parentFrame;

	public CreateNewOntologyWizard(MainFrame parentFrame) {
		this.messages = EditorUtil.getResourceBundle(CreateNewOntologyWizard.class);
		this.parentFrame = parentFrame;
		this.pages = new AbstractPage[1];
		pages[0] = new OntologyInfoPage(this);

	}

	@Override
	protected void doFinish() {
		OntologyInfoPageModel result = ((OntologyInfoPage)pages[0]).getResult();
		
		Logic logic = Logic.getInstance();
		
		BaseURI baseURI = new BaseURI(result.getBaseUri(), result.getOntologyName(), result.getOntologyDescription());
		//stworzenie inicjalnej wersji ontologii
		VersionedURI versionedURI = new VersionedURI(baseURI, 0, 0);
		versionedURI.setVersionDescription(result.getInitialVersionDescription());
		
		if(!result.getPhysicalURI().trim().equals("")) {
			logic.createNewOntology(versionedURI, URI.create(result.getPhysicalURI()), result.getCreatorUserName());
		} else {
			logic.createEmptyOntology(versionedURI, result.getCreatorUserName());
		}
		
		String projectName = result.getProjectName();
		
		File projectFolder = new File(Main.APPLICATION_FOLDER_PATH+projectName);
		if(!projectFolder.exists()) {
			projectFolder.mkdir();
		}
				
		
		ProjectDescriptor projectDesc = new ProjectDescriptor();
		projectDesc.setOntologyBaseURI(versionedURI.getBaseURI().getURIAsString());
		projectDesc.setProjectName(result.getProjectName());
		projectDesc.setBaseOntologyFileUri(projectFolder.toURI().toString()+"base.ont");
		projectDesc.setSubVersion(versionedURI.getOntologySubversion());
		projectDesc.setVersion(versionedURI.getOntologyVersion());
		projectDesc.saveToFile(projectFolder.getAbsolutePath()+"/project.xml");

		
		OWLOntology ontology = logic.getOntology(versionedURI);
		if(ontology == null) {
			try {
				ontology = logic.getOntologyManager().createOntology(URI.create(versionedURI.getBaseURI().getURIAsString()));
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
				
		logic.saveOntologyToFile(ontology, projectFolder.toURI().toString()+"base.ont", versionedURI);
		logic.setDescriptor(projectDesc);
		
		logic.loadProject(projectDesc);
		
		parentFrame.getToolbarPanel().reinitializeButtons();
		parentFrame.getMenu().reintializeMenus();
		parentFrame.loadOntology(ontology);
		
		JOptionPane.showMessageDialog(this, messages.getString("messageOntologyCreated.body") , messages.getString("messageOntologyCreated.title"), JOptionPane.INFORMATION_MESSAGE);
	}
	

	@Override
	public int getWizardHeight() {
		return 600;
	}

	@Override
	public int getWizardWidth() {
		return 550;
	}

	@Override
	protected void doStart() {
	}

	@Override
	protected AbstractPage getStartPage() {
		return pages[0];
	}

	@Override
	protected void doCancel() {

	}

	@Override
	protected String getWizardDescription() {
		return messages.getString("wizardDescription.text");
	}

	@Override
	protected String getWizardTitle() {
		return messages.getString("wizardTitle.text");
	}

	@Override
	protected AbstractPage[] getPages() {
		return pages;
	}

}
