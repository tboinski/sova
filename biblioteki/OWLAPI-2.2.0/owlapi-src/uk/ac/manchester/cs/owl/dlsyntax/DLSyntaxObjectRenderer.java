package uk.ac.manchester.cs.owl.dlsyntax;

import org.semanticweb.owl.io.OWLObjectRenderer;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.ShortFormProvider;
import org.semanticweb.owl.util.SimpleShortFormProvider;
import org.semanticweb.owl.util.SimpleURIShortFormProvider;
import org.semanticweb.owl.util.URIShortFormProvider;
import static uk.ac.manchester.cs.owl.dlsyntax.DLSyntax.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
/*
 * Copyright (C) 2007, University of Manchester
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
 * Date: 10-Feb-2008<br><br>
 *
 * Renders objects in unicode DL syntax
 */
public class DLSyntaxObjectRenderer implements OWLObjectRenderer, OWLObjectVisitor {

    private ShortFormProvider shortFormProvider;

    private URIShortFormProvider uriShortFormProvider;

    private StringBuilder buffer;

    private OWLObject focusedObject;

    public DLSyntaxObjectRenderer() {
        this.shortFormProvider = new SimpleShortFormProvider();
        this.uriShortFormProvider = new SimpleURIShortFormProvider();
        this.buffer = new StringBuilder();
    }


    public void setFocusedObject(OWLObject focusedObject) {
        this.focusedObject = focusedObject;
    }

    public boolean isFocusedObject(OWLObject obj) {
        if(focusedObject == null) {
            return false;
        }
        return focusedObject.equals(obj);
    }


    public void setShortFormProvider(ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
    }

    public String render(OWLObject object) {
        buffer = new StringBuilder();
        object.accept(this);
        return buffer.toString();
    }


    public void visit(OWLOntology ontology) {
        for(OWLAxiom ax : new TreeSet<OWLAxiom>(ontology.getLogicalAxioms())) {
            ax.accept(this);
            write("\n");
        }
    }

    protected void write(String s) {
        buffer.append(s);
    }

    protected String renderEntity(OWLEntity entity) {
        return shortFormProvider.getShortForm(entity);
    }

    protected void writeEntity(OWLEntity entity) {
        write(renderEntity(entity));
    }

    protected void write(DLSyntax keyword) {
        write(keyword.toString());
    }

    protected void write(int i) {
        write(Integer.toString(i));
    }

    protected void writeNested(OWLObject object) {
        if (isBracketedIfNested(object)) {
            write("(");
        }
        object.accept(this);
        if (isBracketedIfNested(object)) {
            write(")");
        }
    }


    protected boolean isBracketedIfNested(OWLObject object) {
//        if(object instanceof OWLObjectComplementOf) {
//            if(!((OWLObjectComplementOf) object).getOperand().isAnonymous()) {
//                return false;
//            }
//        }
        return object instanceof OWLDescription && ((OWLDescription) object).isAnonymous();
    }

    private void writeObject(OWLObject object, boolean nest) {
        if(nest) {
            writeNested(object);
        }
        else {
            object.accept(this);
        }
    }

    protected void write(Collection<? extends OWLObject> objects, DLSyntax delim, boolean nest) {
        if(objects.size() == 2) {
            Iterator<? extends OWLObject> it = objects.iterator();
            OWLObject o1 = it.next();
            OWLObject o2 = it.next();
            if(isFocusedObject(o1) || !isFocusedObject(o2)) {
                writeObject(o1, nest);
                writeSpace();
                write(delim);
                writeSpace();
                writeObject(o2, nest);
            }
            else {
                writeObject(o2, nest);
                writeSpace();
                write(delim);
                writeSpace();
                writeObject(o1, nest);
            }
        }
        else {
            for(Iterator<? extends OWLObject> it = objects.iterator(); it.hasNext(); ) {
                OWLObject o = it.next();
                writeObject(o, nest);
                if(it.hasNext()) {
                    writeSpace();
                    write(delim);
                    writeSpace();
                }
            }
        }

    }

//    protected void write(Collection<? extends OWLObject> objects, DLSyntax keyword, boolean nest) {
//        write(objects, keyword, nest);
//    }



    public void visit(OWLSubClassAxiom axiom) {
        axiom.getSubClass().accept(this);
        writeSpace();
        write(SUBCLASS);
        writeSpace();
        axiom.getSuperClass().accept(this);
    }

    private void writePropertyAssertion(OWLPropertyAssertionAxiom ax) {
        write("(");
        ax.getSubject().accept(this);
        write(", ");
        ax.getObject().accept(this);
        write(")");
        write(" : ");
        if(ax instanceof OWLNegativeObjectPropertyAssertionAxiom || ax instanceof OWLNegativeDataPropertyAssertionAxiom) {
            write(NOT);
        }
        ax.getProperty().accept(this);
    }

    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        write(NOT);
        writePropertyAssertion(axiom);

    }


    public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
        throw new OWLRuntimeException("NOT IMPLEMENTED");
    }


    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        throw new OWLRuntimeException("NOT IMPLEMENTED");
    }


    public void visit(OWLDisjointClassesAxiom axiom) {
        write(axiom.getDescriptions(), DISJOINT_WITH, true);
    }

    private void writeDomainAxiom(OWLPropertyDomainAxiom axiom) {
        write(EXISTS);
        writeSpace();
        axiom.getProperty().accept(this);
        writeRestrictionSeparator();
        write(TOP);
        writeSpace();
        write(SUBCLASS);
        writeSpace();
        writeNested(axiom.getDomain());
    }


    private void writeRestrictionSeparator() {
        write(".");
    }


    public void visit(OWLDataPropertyDomainAxiom axiom) {
        writeDomainAxiom(axiom);
    }


    public void visit(OWLImportsDeclaration axiom) {

    }


    public void visit(OWLAxiomAnnotationAxiom axiom) {
    }


    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        writeDomainAxiom(axiom);
    }


    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        write(axiom.getProperties(), EQUIVALENT_TO, false);
    }


    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        write(NOT);
        writePropertyAssertion(axiom);
    }


    public void visit(OWLDifferentIndividualsAxiom axiom) {
        write(axiom.getIndividuals(), NOT_EQUAL, false);
    }


    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        write(axiom.getProperties(), DISJOINT_WITH, false);
    }


    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        write(axiom.getProperties(), DISJOINT_WITH, false);
    }

    private void writeRangeAxiom(OWLPropertyRangeAxiom axiom) {
        write(TOP);
        writeSpace();
        write(SUBCLASS);
        writeSpace();
        write(FORALL);
        writeSpace();
        axiom.getProperty().accept(this);
        writeRestrictionSeparator();
        writeNested(axiom.getRange());
    }

    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        writeRangeAxiom(axiom);
    }


    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        writePropertyAssertion(axiom);
    }

    private void writeFunctionalProperty(OWLPropertyExpression property) {
        write(TOP);
        writeSpace();
        write(SUBCLASS);
        writeSpace();
        write(MAX);
        writeSpace();
        write(1);
        writeSpace();
        property.accept(this);
    }

    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        writeFunctionalProperty(axiom.getProperty());
    }


    public void visit(OWLObjectSubPropertyAxiom axiom) {
        axiom.getSubProperty().accept(this);
        writeSpace();
        write(SUBCLASS);
        writeSpace();
        axiom.getSuperProperty().accept(this);
    }


    public void visit(OWLDisjointUnionAxiom axiom) {
        axiom.getOWLClass().accept(this);
        write(EQUAL);
        write(axiom.getDescriptions(), OR, false);
    }


    public void visit(OWLDeclarationAxiom axiom) {
    }


    public void visit(OWLEntityAnnotationAxiom axiom) {
    }


    public void visit(OWLOntologyAnnotationAxiom axiom) {
    }


    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(EQUIVALENT_TO);
        writeSpace();
        axiom.getProperty().accept(this);
        write(INVERSE);
        
    }


    private void writeSpace() {
        write(" ");
    }


    public void visit(OWLDataPropertyRangeAxiom axiom) {
        writeRangeAxiom(axiom);
    }



    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        writeFunctionalProperty(axiom.getProperty());
    }


    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        write(axiom.getProperties(), EQUIVALENT_TO, false);
    }


    public void visit(OWLClassAssertionAxiom axiom) {
        axiom.getIndividual().accept(this);
        write(" : ");
        axiom.getDescription().accept(this);
    }


    public void visit(OWLEquivalentClassesAxiom axiom) {
        write(axiom.getDescriptions(), EQUIVALENT_TO, false);
    }


    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        writePropertyAssertion(axiom);
    }


    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(IN);
        writeSpace();
        write("R");
        write("\u207A");
    }


    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
    }


    public void visit(OWLDataSubPropertyAxiom axiom) {
        axiom.getSubProperty().accept(this);
        write(SUBCLASS);
        axiom.getSuperProperty().accept(this);
    }


    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        write(TOP);
        writeSpace();
        write(SUBCLASS);
        writeSpace();
        write(MAX);
        writeSpace();
        write(1);
        writeSpace();
        axiom.getProperty().accept(this);
        write(INVERSE);
    }


    public void visit(OWLSameIndividualsAxiom axiom) {
        write(axiom.getIndividuals(), EQUAL, false);
    }


    public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
        write(axiom.getPropertyChain(), COMP, false);
        writeSpace();
        write(SUBCLASS);
        axiom.getSuperProperty().accept(this);
    }


    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        OWLObject o1 = axiom.getFirstProperty();
        OWLObject o2 = axiom.getSecondProperty();

        OWLObject first, second;
        if(isFocusedObject(o1) || !isFocusedObject(o2)) {
            first = o1;
            second = o2;
        }
        else {
            first = o2;
            second = o1;
        }

        first.accept(this);
        writeSpace();
        write(EQUIVALENT_TO);
        writeSpace();
        second.accept(this);
        write(INVERSE);
    }


    public void visit(SWRLRule rule) {
        write(rule.getHead(), WEDGE, false);
        writeSpace();
        write(IMPLIES);
        writeSpace();        
        write(rule.getBody(), WEDGE, false);

    }


    public void visit(OWLClass desc) {
        if(desc.isOWLThing()) {
            write(TOP);
        }
        else if(desc.isOWLNothing()) {
            write(BOTTOM);
        }
        else {
            writeEntity(desc);
        }
    }


    public void visit(OWLObjectIntersectionOf desc) {
        write(desc.getOperands(), AND, true);
    }


    public void visit(OWLObjectUnionOf desc) {
        write(desc.getOperands(), OR, true);
    }


    public void visit(OWLObjectComplementOf desc) {
        write(NOT);
        writeNested(desc.getOperand());
    }

    private void writeCardinalityRestriction(OWLCardinalityRestriction restriction, DLSyntax keyword) {
        write(keyword);
        writeSpace();
        write(restriction.getCardinality());
        writeSpace();
        restriction.getProperty().accept(this);
        if (restriction.isQualified()) {
            writeRestrictionSeparator();
            writeNested(restriction.getFiller());
        }
    }

    private void writeQuantifiedRestriction(OWLQuantifiedRestriction restriction, DLSyntax keyword) {
        write(keyword);
        writeSpace();
        restriction.getProperty().accept(this);
        writeRestrictionSeparator();
        writeNested(restriction.getFiller());
    }

    public void visit(OWLObjectSomeRestriction desc) {
        writeQuantifiedRestriction(desc, EXISTS);

    }


    public void visit(OWLObjectAllRestriction desc) {
        writeQuantifiedRestriction(desc, FORALL);
    }


    private void writeValueRestriction(OWLValueRestriction restriction) {
        write(EXISTS);
        writeSpace();
        restriction.getProperty().accept(this);
        writeRestrictionSeparator();
        write("{");
        restriction.getValue().accept(this);
        write("}");
    }

    public void visit(OWLObjectValueRestriction desc) {
        writeValueRestriction(desc);
    }


    public void visit(OWLObjectMinCardinalityRestriction desc) {
        writeCardinalityRestriction(desc, MIN);
    }


    public void visit(OWLObjectExactCardinalityRestriction desc) {
        writeCardinalityRestriction(desc, EQUAL);
    }


    public void visit(OWLObjectMaxCardinalityRestriction desc) {
        writeCardinalityRestriction(desc, MAX);
    }


    public void visit(OWLObjectSelfRestriction desc) {
        write(EXISTS);
        writeSpace();
        desc.getProperty().accept(this);
        write(" .");
        write(SELF);
    }


    public void visit(OWLObjectOneOf desc) {
        for(Iterator<OWLIndividual> it = desc.getIndividuals().iterator(); it.hasNext(); ) {
            write("{");
            it.next().accept(this);
            write("}");
            if(it.hasNext()) {
                write(" ");
                write(OR);
                write(" ");
            }
        }

    }


    public void visit(OWLDataSomeRestriction desc) {
        writeQuantifiedRestriction(desc, EXISTS);
    }


    public void visit(OWLDataAllRestriction desc) {
        writeQuantifiedRestriction(desc, FORALL);
    }


    public void visit(OWLDataValueRestriction desc) {
        writeValueRestriction(desc);
    }


    public void visit(OWLDataMinCardinalityRestriction desc) {
        writeCardinalityRestriction(desc, MIN);
    }


    public void visit(OWLDataExactCardinalityRestriction desc) {
        writeCardinalityRestriction(desc, EQUAL);
    }


    public void visit(OWLDataMaxCardinalityRestriction desc) {
        writeCardinalityRestriction(desc, MAX);
    }


    public void visit(OWLDataType node) {
        write(shortFormProvider.getShortForm(node));
    }


    public void visit(OWLDataComplementOf node) {
        write(NOT);
        node.getDataRange().accept(this);

    }


    public void visit(OWLDataOneOf node) {
        for(Iterator<OWLConstant> it = node.getValues().iterator(); it.hasNext(); ) {
            write("{");
            it.next().accept(this);
            write("}");
            if (it.hasNext()) {
                write(OR);
            }
        }
    }


    public void visit(OWLDataRangeRestriction node) {
        
    }


    public void visit(OWLTypedConstant node) {
        write(node.getLiteral());
    }


    public void visit(OWLUntypedConstant node) {
        write(node.getLiteral());
    }


    public void visit(OWLDataRangeFacetRestriction node) {
    }


    public void visit(OWLObjectProperty property) {
        writeEntity(property);
    }


    public void visit(OWLObjectPropertyInverse property) {
        property.getInverse().accept(this);
        write(INVERSE);
    }


    public void visit(OWLDataProperty property) {
        writeEntity(property);
    }


    public void visit(OWLIndividual individual) {
        writeEntity(individual);
    }


    public void visit(OWLObjectAnnotation annotation) {
        write(uriShortFormProvider.getShortForm(annotation.getAnnotationURI()));
        writeSpace();
        annotation.getAnnotationValue().accept(this);
    }


    public void visit(OWLConstantAnnotation annotation) {
        write(uriShortFormProvider.getShortForm(annotation.getAnnotationURI()));
        writeSpace();
        annotation.getAnnotationValue().accept(this);
    }


    public void visit(SWRLClassAtom node) {
        node.getPredicate().accept(this);
        write("(");
        node.getArgument().accept(this);
        write(")");
    }


    public void visit(SWRLDataRangeAtom node) {
        node.getPredicate().accept(this);
        write("(");
        node.getArgument().accept(this);
        write(")");
    }


    public void visit(SWRLObjectPropertyAtom node) {
        node.getPredicate().accept(this);
        write("(");
        node.getFirstArgument().accept(this);
        write(", ");
        node.getSecondArgument().accept(this);
        write(")");
    }


    public void visit(SWRLDataValuedPropertyAtom node) {
        node.getPredicate().accept(this);
        write("(");
        node.getFirstArgument().accept(this);
        write(", ");
        node.getSecondArgument().accept(this);
        write(")");
    }


    public void visit(SWRLBuiltInAtom node) {
        write(node.getPredicate().getShortName());
        write("(");
        write(node.getArguments(), COMMA, true);
        write(")");
    }


    public void visit(SWRLAtomDVariable node) {
        write("?");
        write(uriShortFormProvider.getShortForm(node.getURI()));
    }


    public void visit(SWRLAtomIVariable node) {
        write("?");
        write(uriShortFormProvider.getShortForm(node.getURI()));
    }


    public void visit(SWRLAtomIndividualObject node) {
        node.getIndividual().accept(this);
    }


    public void visit(SWRLAtomConstantObject node) {
        node.getConstant().accept(this);
    }


    public void visit(SWRLSameAsAtom node) {
        write("sameAs(");
        node.getFirstArgument().accept(this);
        write(", ");
        node.getSecondArgument().accept(this);
        write(")");

    }


    public void visit(SWRLDifferentFromAtom node) {
        write("differentFrom(");
        node.getFirstArgument().accept(this);
        write(", ");
        node.getSecondArgument().accept(this);
        write(")");
    }
}
