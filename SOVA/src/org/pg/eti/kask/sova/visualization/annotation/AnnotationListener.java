package org.pg.eti.kask.sova.visualization.annotation;

import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Set;
import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLCommentAnnotation;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLLabelAnnotation;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;
/**
 * Klasa listenera ustawiająca opisy zaznaczonego elementu w zadanym komponencie
 * @author Piotr Kunowski
 *
 */
public class AnnotationListener extends ControlAdapter {
	private AnnotationComponent descriptComponent=null;
	private OWLOntologyManager manager=null;
	private OWLOntology ontology = null;
	public AnnotationListener(AnnotationComponent component, OWLOntology ontology){
		this.descriptComponent = component;
		this.ontology = ontology;
		this.manager = OWLManager.createOWLOntologyManager();
		
	}
	
	
	 /**
	 * Obsługa akcji kliknięcia na obiekt
     * @see prefuse.controls.Control#itemClicked(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
	@Override
    public void itemClicked(VisualItem item, MouseEvent e) {
    	Object o = item.get(OWLtoGraphConverter.URI_COLUMN);
    	descriptComponent.setNameText("");
    	descriptComponent.setLabelText("");
    	descriptComponent.setCommentText("");
    	if (o!=null){
    		OWLClass currentClass = manager.getOWLDataFactory().getOWLClass((URI)o);
			descriptComponent.setNameText(currentClass.getURI().getFragment());
			Set<OWLAxiom> refAxioms = ontology.getReferencingAxioms(currentClass);
			for(OWLAxiom axiom : refAxioms) {
				if(axiom instanceof OWLEntityAnnotationAxiom ) {
					if(((OWLEntityAnnotationAxiom)axiom).getAnnotation() instanceof OWLLabelAnnotation) {
						descriptComponent.setLabelText(((OWLEntityAnnotationAxiom)axiom).getAnnotation().getAnnotationValueAsConstant().getLiteral());
					} else if(((OWLEntityAnnotationAxiom)axiom).getAnnotation() instanceof OWLCommentAnnotation) {
						descriptComponent.setCommentText(((OWLEntityAnnotationAxiom)axiom).getAnnotation().getAnnotationValueAsConstant().getLiteral());
					}
				}
			}
    	}
    }
}
