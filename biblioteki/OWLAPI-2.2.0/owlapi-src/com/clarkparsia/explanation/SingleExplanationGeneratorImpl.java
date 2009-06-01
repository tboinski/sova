package com.clarkparsia.explanation;

import com.clarkparsia.explanation.util.DefinitionTracker;
import org.semanticweb.owl.inference.OWLClassReasoner;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 * <p/>
 * Title: SingleExplanationGeneratorImpl
 * </p>
 * <p/>
 * Description: An abstract implementation of SingleExplanationGenerator that
 * can be used as the basis for different explanation generator techniques.
 * </p>
 * <p/>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p/>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 * @author Evren Sirin
 */
public abstract class SingleExplanationGeneratorImpl implements SingleExplanationGenerator {

    protected OWLOntologyManager owlOntologyManager;

    protected OWLOntology ontology;

    protected OWLClassReasoner reasoner;

    protected ReasonerFactory reasonerFactory;

    protected DefinitionTracker definitionTracker;

    protected OWLClassReasoner altReasoner;

    private long fullOntSize = -1;


    public SingleExplanationGeneratorImpl(OWLOntologyManager manager) {
        this.owlOntologyManager = manager;

        definitionTracker = new DefinitionTracker(manager);
    }


    public OWLOntologyManager getOntologyManager() {
        return owlOntologyManager;
    }


    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
        this.altReasoner = null;
        this.fullOntSize = 0;

        for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology))
            fullOntSize += ont.getLogicalAxioms().size();

        definitionTracker.setOntology(ontology);
    }


    public OWLClassReasoner getReasoner() {
        return reasoner;
    }


    public void setReasoner(OWLClassReasoner reasoner) {
        this.reasoner = reasoner;
        this.altReasoner = null;
    }


    public OWLOntology getOntology() {
        return ontology;
    }


    public ReasonerFactory getReasonerFactory() {
        return reasonerFactory;
    }


    public void setReasonerFactory(ReasonerFactory reasonerFactory) {
        this.reasonerFactory = reasonerFactory;
        this.altReasoner = null;
    }


    public OWLClassReasoner getAltReasoner() {
        if (altReasoner == null) {
            altReasoner = reasonerFactory.createReasoner(owlOntologyManager);
        }

        return altReasoner;
    }


    protected boolean isFirstExplanation() {
        if (fullOntSize == -1) {
            if (ontology == null)
                throw new RuntimeException("No ontology specified");
            else
                throw new RuntimeException("Ontology size unknown");
        }
        else {
            int currentOntSize = 0;
            for (OWLOntology ont : owlOntologyManager.getImportsClosure(ontology))
                currentOntSize += ont.getLogicalAxioms().size();

            return (currentOntSize == fullOntSize);
        }
    }
}
