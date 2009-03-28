package org.pg.eti.kask.ont.editor.wizard.showTypes;

import java.util.List;
import java.util.ResourceBundle;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.pg.eti.kask.ont.editor.wizard.showTypes.model.IndividualTypePageModel;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * Klasa reprezentujaca wizard do tworzenia, wyswietlania i usuwania 
 * typow dla danego bytu ontologii.
 * 
 * @author Andrzej Jakowski
 */
public class ShowTypesWizard extends AbstractWizard {

	private static final long serialVersionUID = -3696045942304962507L;
	
	private ResourceBundle messages;
	
	private AbstractPage[] pages;
	
	private Logic logic;
	
	private String individualURI;
	
	public ShowTypesWizard(String individualURI) {
		this.messages = EditorUtil.getResourceBundle(ShowTypesWizard.class);
		
		this.individualURI = individualURI;
		
		this.logic = Logic.getInstance();
		this.pages = new AbstractPage[1];
		
		pages[0] = new IndividualTypePage(this, logic, individualURI);
	}

	@Override
	protected void doCancel() {

	}

	@Override
	protected void doFinish() {
		IndividualTypePageModel page1Result = ((IndividualTypePage)pages[0]).getResult();
		
		List<String> types = page1Result.getIndividualTypes();

		String ontologyURI = logic.getLoadedOntologyURI();
		
		//wygenerowanie zmian w ontologii
		List<OWLAxiomChange> changes =  logic.createIndividualTypesAxioms(ontologyURI, individualURI, types);
		
		
		try {
			//zatwierdzenie zmian
			logic.applyChanges(changes);			
			
		} catch (OWLOntologyChangeException e) {		
			e.printStackTrace();
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
