package org.pg.eti.kask.ont.editor.wizard.objectPropertyCreator;

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
import org.pg.eti.kask.ont.editor.wizard.objectPropertyCreator.model.BasicInformationsPageModel;
import org.pg.eti.kask.ont.editor.wizard.objectPropertyCreator.model.ObjectPropertyRelationsPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntologyChangeException;

public class CreateObjectPropertyWizard extends AbstractWizard {

	private static final long serialVersionUID = -1338341063702400506L;
	
	private ResourceBundle messages;
	
	private AbstractPage pages[];
	
	private Logic logic;
	
	private MainFrame parentFrame;
	
	public CreateObjectPropertyWizard(MainFrame parentFrame, String selectedPropertyURI) {
		this.messages = EditorUtil.getResourceBundle(CreateObjectPropertyWizard.class);
		
		this.pages = new AbstractPage[2];		
		this.pages[0] = new BasicInformationsPage(this, selectedPropertyURI);
		this.pages[1] = new ObjectPropertyRelationsPage(this);
		
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
		ObjectPropertyRelationsPageModel page2Result = ((ObjectPropertyRelationsPage)pages[1]).getResult();
		
		OWLAxiomChange declarationChange = logic.createObjectProperty(ontologyURI, page1Result.getPropertyURI());
		changes.add(declarationChange);
		
		String superPropertyUri = page1Result.getSuperPropertyURI();
		if(superPropertyUri != null && !superPropertyUri.trim().equals("")) {
			OWLAxiomChange subPropertyRelation = logic.createSubObjectPropertyRelation(ontologyURI, superPropertyUri, page1Result.getPropertyURI());
			changes.add(subPropertyRelation);
		}
		
		String inversePropertyUri = page1Result.getInversePropertyURI();
		if(inversePropertyUri != null && !inversePropertyUri.trim().equals("")) {
			OWLAxiomChange inversePropertyChange = logic.createInverseObjectPropertiesRelation(ontologyURI, page1Result.getPropertyURI(), inversePropertyUri);
			changes.add(inversePropertyChange);
		}
		
		if(page1Result.getAnnotations() != null && page1Result.getAnnotations().size() > 0 ) {
			OWLObjectProperty objectProperty = logic.getDataFactory().getOWLObjectProperty(URI.create(page1Result.getPropertyURI()));
			for(OWLAnnotation<?> annotation : page1Result.getAnnotations()) {
				OWLAxiomChange annotationChange = logic.createEntityAnnotationAxiom(ontologyURI, objectProperty, annotation);
				changes.add(annotationChange);
			}
		}
		
		if(page2Result.isFunctionalProperty()) {
			OWLAxiomChange functionalChange = logic.createFunctionalObjectProperty(ontologyURI, page1Result.getPropertyURI());
			changes.add(functionalChange);
		}
		
		if(page2Result.isInverseFunctionalProperty()) {
			OWLAxiomChange inverseFunctionalChange = logic.createInverseFunctionalObjectProperty(ontologyURI, page1Result.getPropertyURI());
			changes.add(inverseFunctionalChange);
		}
		
		if(page2Result.isSymmetricProperty()) {
			OWLAxiomChange symmetricChange = logic.createSymmetricObjectProperty(ontologyURI, page1Result.getPropertyURI());
			changes.add(symmetricChange);
		}
		
		if(page2Result.isTransitiveProperty()) {
			OWLAxiomChange transitiveChange = logic.createTransitiveObjectProperty(ontologyURI, page1Result.getPropertyURI());
			changes.add(transitiveChange);
		}
		
		List<String> domains = page2Result.getDomainClassesUris();
		
		for(String domain : domains) {
			OWLAxiom axiom = logic.getObjectPropertyDomainAxiom(page1Result.getPropertyURI(), domain);
			changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
		}
		
		List<String> ranges = page2Result.getRangeClassesUris();
		
		for(String range : ranges) {
			OWLAxiom axiom = logic.getObjectPropertyRangeAxiom(page1Result.getPropertyURI(), range);
			changes.add(new AddAxiom(logic.getOntology(ontologyURI), axiom));
		}
		
		try {
			logic.applyChanges(changes);
			
			PropertiesTree tree = parentFrame.getComponentsPanel().getPropertiesTree();
			
			String propertyName = EditorUtil.getOwlEntityName(page1Result.getPropertyURI());
			
			BasicTreeNode addedNode = new BasicTreeNode(page1Result.getPropertyURI(), propertyName, OntologyPropertiesTreeModel.OBJECT_PROPERTY);
			
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
