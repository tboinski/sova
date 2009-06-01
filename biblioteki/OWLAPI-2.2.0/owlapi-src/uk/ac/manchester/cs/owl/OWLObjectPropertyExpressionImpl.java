package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.*;

import java.util.Set;
import java.util.TreeSet;
/*
 * Copyright (C) 2006, University of Manchester
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
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 26-Oct-2006<br><br>
 */
public abstract class OWLObjectPropertyExpressionImpl extends OWLPropertyExpressionImpl<OWLObjectPropertyExpression, OWLDescription> implements OWLObjectPropertyExpression {

    public OWLObjectPropertyExpressionImpl(OWLDataFactory dataFactory) {
        super(dataFactory);
    }

    protected Set<? extends OWLPropertyDomainAxiom> getDomainAxioms(OWLOntology ontology) {
        return ontology.getObjectPropertyDomainAxioms(this);
    }


    public boolean isFunctional(OWLOntology ontology) {
        return ontology.getFunctionalObjectPropertyAxiom(this) != null;
    }


    public boolean isFunctional(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isFunctional(ont)) {
                return true;
            }
        }
        return false;
    }


    public boolean isInverseFunctional(OWLOntology ontology) {
        return ontology.getInverseFunctionalObjectPropertyAxiom(this) != null;
    }


    public boolean isInverseFunctional(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isInverseFunctional(ont)) {
                return true;
            }
        }
        return false;
    }


    public boolean isSymmetric(OWLOntology ontology) {
        return ontology.getSymmetricObjectPropertyAxiom(this) != null;
    }


    public boolean isSymmetric(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isSymmetric(ont)) {
                return true;
            }
        }
        return false;
    }


    public boolean isAntiSymmetric(OWLOntology ontology) {
        return ontology.getAntiSymmetricObjectPropertyAxiom(this) != null;
    }


    public boolean isAntiSymmetric(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isAntiSymmetric(ont)) {
                return true;
            }
        }
        return false;
    }


    public boolean isReflexive(OWLOntology ontology) {
        return ontology.getReflexiveObjectPropertyAxiom(this) != null;
    }


    public boolean isReflexive(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isReflexive(ont)) {
                return true;
            }
        }
        return false;
    }


    public boolean isIrreflexive(OWLOntology ontology) {
        return ontology.getIrreflexiveObjectPropertyAxiom(this) != null;
    }


    public boolean isIrreflexive(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isIrreflexive(ont)) {
                return true;
            }
        }
        return false;
    }


    public boolean isTransitive(OWLOntology ontology) {
        return ontology.getTransitiveObjectPropertyAxiom(this) != null;
    }


    public boolean isTransitive(Set<OWLOntology> ontologies) {
        for(OWLOntology ont : ontologies) {
            if(isTransitive(ont)) {
                return true;
            }
        }
        return false;
    }


    protected Set<? extends OWLPropertyRangeAxiom<OWLObjectPropertyExpression, OWLDescription>> getRangeAxioms(
            OWLOntology ontology) {
        return ontology.getObjectPropertyRangeAxioms(this);
    }


    protected Set<? extends OWLSubPropertyAxiom<OWLObjectPropertyExpression>> getSubPropertyAxioms(
            OWLOntology ontology) {
        return ontology.getObjectSubPropertyAxiomsForLHS(this);
    }


    protected Set<? extends OWLNaryPropertyAxiom<OWLObjectPropertyExpression>> getEquivalentPropertiesAxioms(
            OWLOntology ontology) {
        return ontology.getEquivalentObjectPropertiesAxioms(this);
    }


    protected Set<? extends OWLNaryPropertyAxiom<OWLObjectPropertyExpression>> getDisjointPropertiesAxioms(
            OWLOntology ontology) {
        return ontology.getDisjointObjectPropertiesAxiom(this);
    }


    public Set<OWLObjectPropertyExpression> getInverses(OWLOntology ontology) {
        Set<OWLObjectPropertyExpression> result = new TreeSet<OWLObjectPropertyExpression>();
        for(OWLInverseObjectPropertiesAxiom ax : ontology.getInverseObjectPropertyAxioms(this)) {
            result.addAll(ax.getProperties());
        }
        result.remove(this);
        return result;
    }


    public Set<OWLObjectPropertyExpression> getInverses(Set<OWLOntology> ontologies) {
        Set<OWLObjectPropertyExpression> result = new TreeSet<OWLObjectPropertyExpression>();
        for(OWLOntology ont : ontologies) {
            result.addAll(getInverses(ont));
        }
        return result;
    }


    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof OWLObjectPropertyExpression;
    }

}
