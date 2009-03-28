package org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.tree.PropertiesTree;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.node.BasicTreeNode;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator.model.BasicInformationsPageModel;
import org.pg.eti.kask.ont.editor.wizard.dataPropertyCreator.model.DataPropertyRelationsPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLOntologyChangeException;

public class CreateDataPropertyWizard extends AbstractWizard {

	private static final long serialVersionUID = -7818940179761156705L;
	
	private ResourceBundle messages;
	
	private AbstractPage pages[];
	
	private Logic logic;
	
	private MainFrame parentFrame;	
	
	public CreateDataPropertyWizard(MainFrame parentFrame, String selectedPropertyURI) {
		this.messages = EditorUtil.getResourceBundle(CreateDataPropertyWizard.class);
		
		this.pages = new AbstractPage[2];		
		this.pages[0] = new BasicInformationsPage(this, selectedPropertyURI);
		this.pages[1] = new DataPropertyRelationsPage(this);
		
		this.parentFrame = parentFrame;
		
		this.logic = Logic.getInstance();
	}

	@Override
	protected void doCancel() {
	}

	@Override
	protected void doFinish() {
		String ontologyURI = logic.getLoadedOntologyURI();
		List<OWLAxiomChange> changes = new ArrayList<OWLAxiomChange>();
		
		BasicInformationsPageModel page1Result = ((BasicInformationsPage)pages[0]).getResult();
		DataPropertyRelationsPageModel page2Result = ((DataPropertyRelationsPage)pages[1]).getResult();
		
		OWLAxiomChange declarationAxiom = logic.createDataProperty(ontologyURI, page1Result.getPropertyURI());
		changes.add(declarationAxiom);
		
		String superPropertyUri = page1Result.getSuperPropertyURI();
		if(superPropertyUri != null && !superPropertyUri.trim().equals("")) {
			OWLAxiomChange subPropertyRelation = logic.createSubDataPropertyRelation(ontologyURI, superPropertyUri, page1Result.getPropertyURI());
			changes.add(subPropertyRelation);
		}
						
		if(page1Result.getAnnotations() != null && page1Result.getAnnotations().size() > 0 ) {
			OWLDataProperty dataProperty = logic.getDataFactory().getOWLDataProperty(URI.create(page1Result.getPropertyURI()));
			for(OWLAnnotation<?> annotation : page1Result.getAnnotations()) {
				OWLAxiomChange annotationChange = logic.createEntityAnnotationAxiom(ontologyURI, dataProperty, annotation);
				changes.add(annotationChange);
			}
		}
		
		if(page2Result.isFunctionalProperty()) {
			OWLAxiomChange functionalChange = logic.createFunctionalDataProperty(ontologyURI, page1Result.getPropertyURI());
			changes.add(functionalChange);
		}
		
		
		List<String> domains = page2Result.getDomainClassesUris();
		
		for(String domain : domains) {
			OWLAxiom axiom = logic.getDataPropertyDomainAxiom(page1Result.getPropertyURI(), domain);
			changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
		}
		
		List<String> ranges = page2Result.getRangeUris();
		
		for(String range : ranges) {
			OWLAxiom axiom = logic.getDataPropertyRangeAxiom(page1Result.getPropertyURI(), range);
			changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
		}
		
		try {
			logic.applyChanges(changes);
			
			PropertiesTree tree = parentFrame.getComponentsPanel().getPropertiesTree();
			
			String propertyName = EditorUtil.getOwlEntityName(page1Result.getPropertyURI());
			
			BasicTreeNode addedNode = new BasicTreeNode(page1Result.getPropertyURI(), propertyName, OntologyPropertiesTreeModel.DATA_PROPERTY);
			
			tree.addNode(addedNode, true);
		} catch (OWLOntologyChangeException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doStart() {
	}

	@Override
	protected AbstractPage[] getPages() {
		return this.pages;
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
	
	@Override
	public int getWizardWidth() {
		return 550;
	}
	
	@Override
	public int getWizardHeight() {
		return 600;
	}

		
}
