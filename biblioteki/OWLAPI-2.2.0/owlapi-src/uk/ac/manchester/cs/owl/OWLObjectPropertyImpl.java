package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
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
public class OWLObjectPropertyImpl extends OWLObjectPropertyExpressionImpl implements OWLObjectProperty {

    private URI uri;


    public OWLObjectPropertyImpl(OWLDataFactory dataFactory, URI uri) {
        super(dataFactory);
        this.uri = uri;
    }


    public URI getURI() {
        return uri;
    }


    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof OWLObjectProperty)) {
                return false;
            }
            return ((OWLObjectProperty) obj).getURI().equals(uri);
        }
        return false;
    }


    public void accept(OWLEntityVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(OWLPropertyExpressionVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(OWLNamedObjectVisitor visitor) {
        visitor.visit(this);
    }


    public <O> O accept(OWLEntityVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public <O> O accept(OWLPropertyExpressionVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public boolean isAnonymous() {
        return false;
    }


    public OWLObjectProperty asOWLObjectProperty() {
        return this;
    }


    protected Set<? extends OWLSubPropertyAxiom<OWLObjectPropertyExpression>> getSubPropertyAxiomsForRHS(
            OWLOntology ont) {
        return ont.getObjectSubPropertyAxiomsForRHS(this);
    }


    public Set<OWLAnnotation> getAnnotations(OWLOntology ontology) {
        return ImplUtils.getAnnotations(this, Collections.singleton(ontology));
    }


    public Set<OWLAnnotationAxiom> getAnnotationAxioms(OWLOntology ontology) {
        return ImplUtils.getAnnotationAxioms(this, Collections.singleton(ontology));
    }


    public Set<OWLAnnotation> getAnnotations(OWLOntology ontology, URI annotationURI) {
        return ImplUtils.getAnnotations(this, annotationURI, Collections.singleton(ontology));
    }

    public OWLDataProperty asOWLDataProperty() {
        throw new OWLRuntimeException("Not a data property!");
    }


    public OWLDataType asOWLDataType() {
        throw new OWLRuntimeException("Not a data type!");
    }


    public OWLIndividual asOWLIndividual() {
        throw new OWLRuntimeException("Not an individual!");
    }


    public OWLClass asOWLClass() {
        throw new OWLRuntimeException("Not an OWLClass!");
    }


    public boolean isOWLClass() {
        return false;
    }


    public boolean isOWLDataProperty() {
        return false;
    }


    public boolean isOWLDataType() {
        return false;
    }


    public boolean isOWLIndividual() {
        return false;
    }


    public boolean isOWLObjectProperty() {
        return true;
    }


    protected int compareObjectOfSameType(OWLObject object) {
        return uri.compareTo(((OWLObjectProperty) object).getURI());
    }
}
