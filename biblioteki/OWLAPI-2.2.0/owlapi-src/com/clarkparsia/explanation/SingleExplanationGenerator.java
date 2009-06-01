package com.clarkparsia.explanation;

import org.semanticweb.owl.inference.OWLClassReasoner;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import java.util.Set;
/*
 * Copyright (C) 2007, Clark & Parsia
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * <p/>
 * Description: The explanation generator interface for returning a single
 * explanation for an unsatisfiable class. Use {@link SatisfiabilityConverter}
 * to convert an arbitrary axiom into a class description that can be used to
 * generate explanations for that axiom.
 * </p>
 * <p/>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p/>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 * @author Evren Sirin
 */
public interface SingleExplanationGenerator {

    /**
     * Get the ontology manager for this explanation generator.
     */
    public OWLOntologyManager getOntologyManager();


    /**
     * Sets the ontology according to which the explanations are generated.
     */
    public void setOntology(OWLOntology ont);


    /**
     * Returns the ontology according to which the explanations are generated.
     */
    public OWLOntology getOntology();


    /**
     * Sets the reasoner that will be used to generate explanations. This
     * function is provided in addition to
     * {@link #setReasonerFactory(ReasonerFactory)} because the reasoning
     * results already computed by the given reasoner can be reused. It is
     * guaranteed that the state of this reasoner will not be invalidated by
     * explanation generation, i.e. if the reasoner was in classified state it
     * will stay in classified state.
     */
    public void setReasoner(OWLClassReasoner reasoner);


    /**
     * Returns the reasoner associated with this generator.
     */
    public OWLClassReasoner getReasoner();


    /**
     * Sets the reasoner factory that will be used to generate fresh reasoners.
     * We create new reasoner instances to avoid invalidating the reasoning
     * state of existing reasoners. Explanation generation process will modify
     * the original ontology and/or reason over a subset of the original
     * ontology. Using an alternate fresh reasoner for these tasks ensures
     * efficient explanation generation without side effects to anything outside
     * the explanation generatyor.
     */
    public void setReasonerFactory(ReasonerFactory reasonerFactory);


    /**
     * Returns the reasoner factory used to generate reasoners.
     */
    public ReasonerFactory getReasonerFactory();


    /**
     * Get a single explanation for an arbitrary class expression, or empty set
     * if the given expression is satisfiable.
     * @param unsatClass arbitrary class expression whose unsatisfiability will be
     *                   explained
     * @return set of axioms explaining the unsatisfiability of given class
     *         expression, or empty set if the given expression is satisfiable.
     */
    public Set<OWLAxiom> getExplanation(OWLDescription unsatClass);
}
