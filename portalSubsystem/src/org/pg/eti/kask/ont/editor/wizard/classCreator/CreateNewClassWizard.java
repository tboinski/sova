package org.pg.eti.kask.ont.editor.wizard.classCreator;

import java.net.URI;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.graph.model.ClassesGraphModel;
import org.pg.eti.kask.ont.editor.panels.MainPanel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.wizard.AbstractWizard;
import org.pg.eti.kask.ont.editor.wizard.classCreator.model.BasicInformationPageModel;
import org.pg.eti.kask.ont.editor.wizard.classCreator.model.ComplexPropertiesPageModel;
import org.pg.eti.kask.ont.editor.wizard.page.AbstractPage;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntology;

/**
 * 
 * @author Andrzej Jakowski
 */
public class CreateNewClassWizard extends AbstractWizard {
	
	private ResourceBundle messages;

	private static final long serialVersionUID = -2039143769007065832L;
	
	private AbstractPage[] pages;
	
	private Logic logic;
	
	private MainFrame parentFrame;
	
	public CreateNewClassWizard(MainFrame parentFrame, String superClassUri) {
		this.messages = EditorUtil.getResourceBundle(CreateNewClassWizard.class);
		this.logic = Logic.getInstance();
		this.pages = new AbstractPage[2];
		this.parentFrame = parentFrame;
		
		pages[0] = new BasicInformationPage(this, logic, superClassUri);
		pages[1] = new ComplexPropertiesPage(this, logic);
	}

	@Override
	protected void doCancel() {
	}

	@Override
	protected void doFinish() {
		String ontologyURI = logic.getLoadedOntologyURI();
		ArrayList<OWLAxiomChange> listOfChanges = new ArrayList<OWLAxiomChange>();
		BasicInformationPageModel page1Result = ((BasicInformationPage)pages[0]).getResult();
		ComplexPropertiesPageModel page2Result = ((ComplexPropertiesPage)pages[1]).getResult();
		
		if(page1Result.getSuperClassUri() != null && !page1Result.getSuperClassUri().trim().equals("")) {
			listOfChanges.add(logic.createSubClassRelation(ontologyURI, page1Result.getSuperClassUri(), page1Result.getClassUri()));
		} else {
			listOfChanges.add(logic.createSubClassRelation(ontologyURI, logic.getDataFactory().getOWLThing().getURI().toString(), page1Result.getClassUri()));
		}
	
		//sprawdzenie czy podano label
		if(page1Result.getLabel() != null && !page1Result.getLabel().trim().equals("") ) {
			listOfChanges.add(logic.createLabelAxiom(ontologyURI, page1Result.getClassUri(), page1Result.getLabel()));
		}
		
		//sprawdzenie czy podano comment
		if(page1Result.getLabel() != null && !page1Result.getLabel().trim().equals("") ) {
			OWLClass currentClass = logic.getDataFactory().getOWLClass(URI.create(page1Result.getClassUri()));
			listOfChanges.add(logic.createCommentAxiom(ontologyURI, currentClass, page1Result.getComment()));
		}
		
		//teraz pobranie danych z drugiej strony formularza
		if(page2Result.getDisJointClassesUris() != null && page2Result.getDisJointClassesUris().size() >0) {
			ArrayList<String> disjointClassesUris = new ArrayList<String>();
			disjointClassesUris.add(page1Result.getClassUri());
			
			for(String u : page2Result.getDisJointClassesUris()) {
				disjointClassesUris.add(u);
			}
			
			listOfChanges.add(logic.createDisjointClassesRelations(ontologyURI, disjointClassesUris));
		}
		
		try {
			
			//na koniec zatwierdzenie zmian
			logic.applyChanges(listOfChanges);
			
			OWLOntology ontology = logic.getOntology(ontologyURI);

			//aktualizacja grafu z wizualizacja
			ClassesGraphModel graph = new ClassesGraphModel(ontology);
			
			MainPanel visPanel = parentFrame.getMainPanel();
			visPanel.displayGraph(graph, ontology);
			visPanel.focusNodeOnGraph(page1Result.getClassUri());
			
			//aktualizacja drzewa z klasami
			//ClassesTree classesTree = parentFrame.getComponentsPanel().getClassesTree();
			parentFrame.loadOntology(ontology);
			
			/*String className =  EditorUtil.getOwlEntityName(page1Result.getClassUri());
			BasicTreeNode addedNode = new BasicTreeNode(page1Result.getClassUri(), className, "class");
			
			classesTree.addNode(addedNode, true);*/
			
		} catch(Exception e) {
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
