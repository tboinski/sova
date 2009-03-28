package org.pg.eti.kask.ont.editor.wizard.changesExporter;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.changesExporter.model.ChangeDescriptionPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLOntology;

public class ExportChangesWizard extends AbstractWizard {
	
	private ResourceBundle messages;
	
	private static final long serialVersionUID = -7154816700285248924L;
	
	private MainFrame parentFrame;
	
	private AbstractPage[] pages;
	
	private Logic logic;
	
	public ExportChangesWizard(MainFrame parentFrame) {		
		this.messages = EditorUtil.getResourceBundle(ExportChangesWizard.class);
		this.logic = Logic.getInstance();
		
		this.parentFrame = parentFrame;
		
		this.pages = new AbstractPage[1];
		this.pages[0] = new ChangeDescriptionPage(this);	
	}

	@Override
	protected void doCancel() {

	}

	@Override
	protected void doFinish() {
		ChangeDescriptionPageModel result = ((ChangeDescriptionPage)this.pages[0]).getResult();
		
		VersionedURI ontologyURI = new VersionedURI();
		ontologyURI.setBaseURI(new BaseURI(logic.getDescriptor().getOntologyBaseURI()));
		ontologyURI.setOntologyVersion(logic.getDescriptor().getVersion());
		ontologyURI.setOntologySubVersion(logic.getDescriptor().getSubVersion());
		
		ProjectDescriptor desc = logic.getDescriptor();
		
		OWLOntology baseOntology = logic.loadOntology(desc.getBaseOntologyFileUri());
		
		OWLOntology changedOntology = logic.getOntology(logic.getLoadedOntologyURI());

		try{
			if(logic.isLocked(new BaseURI(desc.getOntologyBaseURI()))) {
				JOptionPane.showMessageDialog(parentFrame, messages.getString("ontologyLocked.text"), messages.getString("ontologyLocked.title"), JOptionPane.ERROR_MESSAGE);
			} else {
				logic.exportChanges(baseOntology, changedOntology, result.getTitle(), result.getDescription(), ontologyURI);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
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
