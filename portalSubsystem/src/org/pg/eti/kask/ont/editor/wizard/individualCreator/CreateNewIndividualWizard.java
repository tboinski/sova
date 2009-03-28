package org.pg.eti.kask.ont.editor.wizard.individualCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.tree.IndividualsTree;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.individualCreator.model.IndividualInformationPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;

public class CreateNewIndividualWizard extends AbstractWizard {

	private static final long serialVersionUID = -400046343046414115L;
	
	private ResourceBundle messages;
	
	private AbstractPage[] pages;
	
	private Logic logic;
	
	private MainFrame parentFrame;
	
	public CreateNewIndividualWizard(MainFrame parentFrame) {
		this.messages = EditorUtil.getResourceBundle(CreateNewIndividualWizard.class);
		
		this.logic = Logic.getInstance();
		this.pages = new AbstractPage[1];
		this.parentFrame = parentFrame;
		
		this.pages[0] = new IndividualInformationPage(this);
	}

	@Override
	protected void doCancel() {

	}

	@Override
	protected void doFinish() {
		String ontologyURI = logic.getLoadedOntologyURI();
		List<OWLAxiomChange> listOfChanges = new ArrayList<OWLAxiomChange>();
		IndividualInformationPageModel page1result = ((IndividualInformationPage)this.pages[0]).getResult();
		
		listOfChanges.add(logic.createIndividual(ontologyURI, page1result.getIndividualURI()));
		
		if(!page1result.getLabel().equals("")) {
			OWLAxiomChange change = logic.createIndividualCommentAxiom(ontologyURI, page1result.getIndividualURI(), page1result.getComment());
			listOfChanges.add(change);
		}
		
		if(!page1result.getComment().equals("")) {
			OWLAxiomChange change = logic.createIndividualLabelAxiom(ontologyURI, page1result.getIndividualURI(), page1result.getLabel());
			listOfChanges.add(change);
		}
		
		try {
			logic.applyChanges(listOfChanges);
			
			IndividualsTree individualsTree = parentFrame.getComponentsPanel().getIndividualsTree();
			
			String indName =  EditorUtil.getOwlEntityName(page1result.getIndividualURI());
			BasicTreeNode addedNode = new BasicTreeNode(page1result.getIndividualURI(), indName, "individuals");
			
			individualsTree.addNode(addedNode, true);
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
