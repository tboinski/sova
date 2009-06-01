package uk.ac.manchester.cs.owl.mansyntaxrenderer;

import org.coode.manchesterowlsyntax.ManchesterOWLSyntax;
import static org.coode.manchesterowlsyntax.ManchesterOWLSyntax.*;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.vocab.XSDVocabulary;

import java.io.Writer;
import java.util.*;
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
 * Date: 25-Apr-2007<br><br>
 */
public class ManchesterOWLSyntaxObjectRenderer extends AbstractRenderer implements OWLObjectVisitor {

    public static final int LINE_LENGTH = 70;

    private boolean wrap = true;

    private DescriptionComparator descriptionComparator;


    public ManchesterOWLSyntaxObjectRenderer(Writer writer) {
        super(writer);
        descriptionComparator = new DescriptionComparator();
    }


    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    protected List<? extends OWLObject> sort(Collection<? extends OWLObject> objects) {
        List<? extends OWLObject> sortedDescriptions = new ArrayList<OWLObject>(objects);
        Collections.sort(sortedDescriptions, descriptionComparator);
        return sortedDescriptions;
    }


    protected void write(Set<? extends OWLObject> objects, ManchesterOWLSyntax delimeter, boolean newline) {
        int tab = getIndent();
        pushTab(tab);
        for (Iterator<? extends OWLObject> it = sort(objects).iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                if (newline && wrap) {
                    writeNewLine();
                }
                write(delimeter);
            }
        }
        popTab();
    }


    protected void write(Set<? extends OWLDescription> objects, boolean newline) {
        boolean lastWasNamed = false;
        boolean first = true;

        for (Iterator<? extends OWLObject> it = sort(objects).iterator(); it.hasNext();) {
            OWLObject desc = it.next();
            if (!first) {
                if (newline) {
                    writeNewLine();
                }
                if (lastWasNamed && desc instanceof OWLRestriction) {
                    write("", THAT, " ");
                }
                else {
                    write("", AND, " ");
                }
            }

            first = false;
            desc.accept(this);

            lastWasNamed = desc instanceof OWLClass;
        }
    }


    private void writeRestriction(OWLQuantifiedRestriction restriction, ManchesterOWLSyntax keyword) {
        restriction.getProperty().accept(this);
        write(keyword);
        if (restriction.getFiller() instanceof OWLAnonymousDescription) {
            write("(");
        }
        restriction.getFiller().accept(this);
        if (restriction.getFiller() instanceof OWLAnonymousDescription) {
            write(")");
        }
    }


    private void writeRestriction(OWLValueRestriction restriction) {
        restriction.getProperty().accept(this);
        write(VALUE);
        restriction.getValue().accept(this);
    }


    private void writeRestriction(OWLCardinalityRestriction restriction, ManchesterOWLSyntax keyword) {
        restriction.getProperty().accept(this);
        write(keyword);
        write(Integer.toString(restriction.getCardinality()));
//        if(restriction.isQualified()) {
        writeSpace();
        restriction.getFiller().accept(this);
//        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Class descriptions
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLClass desc) {
        write(getShortFormProvider().getShortForm(desc));
    }


    public void visit(OWLObjectIntersectionOf desc) {
        write(desc.getOperands(), true);
    }


    public void visit(OWLObjectUnionOf desc) {
        write(desc.getOperands(), OR, true);
    }


    public void visit(OWLObjectComplementOf desc) {
        write("", NOT, desc.isAnonymous() ? " " : "");
        if (desc.isAnonymous()) {
            write("(");
        }
        desc.getOperand().accept(this);
        if (desc.isAnonymous()) {
            write(")");
        }
    }


    public void visit(OWLObjectSomeRestriction desc) {
        writeRestriction(desc, SOME);
    }


    public void visit(OWLObjectAllRestriction desc) {
        writeRestriction(desc, ONLY);
    }


    public void visit(OWLObjectValueRestriction desc) {
        writeRestriction(desc);
    }


    public void visit(OWLObjectMinCardinalityRestriction desc) {
        writeRestriction(desc, MIN);
    }


    public void visit(OWLObjectExactCardinalityRestriction desc) {
        writeRestriction(desc, EXACTLY);
    }


    public void visit(OWLObjectMaxCardinalityRestriction desc) {
        writeRestriction(desc, MAX);
    }


    public void visit(OWLObjectSelfRestriction desc) {
        desc.getProperty().accept(this);
        write(SOME);
        write(SELF);
    }


    public void visit(OWLObjectOneOf desc) {
        write("{");
        write(desc.getIndividuals(), ONE_OF_DELIMETER, false);
        write("}");
    }


    public void visit(OWLDataSomeRestriction desc) {
        writeRestriction(desc, SOME);
    }


    public void visit(OWLDataAllRestriction desc) {
        writeRestriction(desc, ONLY);
    }


    public void visit(OWLDataValueRestriction desc) {
        writeRestriction(desc);
    }


    public void visit(OWLDataMinCardinalityRestriction desc) {
        writeRestriction(desc, MIN);
    }


    public void visit(OWLDataExactCardinalityRestriction desc) {
        writeRestriction(desc, EXACTLY);
    }


    public void visit(OWLDataMaxCardinalityRestriction desc) {
        writeRestriction(desc, MAX);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Entities stuff
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLObjectProperty property) {
        write(getShortFormProvider().getShortForm(property));
    }


    public void visit(OWLDataProperty property) {
        write(getShortFormProvider().getShortForm(property));
    }


    public void visit(OWLIndividual individual) {
        write(getShortFormProvider().getShortForm(individual));
    }


    public void visit(OWLDataType dataType) {
        write(dataType.getURI().getFragment());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Data stuff
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLDataComplementOf node) {
        write(NOT);
        node.getDataRange().accept(this);
    }


    public void visit(OWLDataOneOf node) {
        write("{");
        write(node.getValues(), ONE_OF_DELIMETER, false);
        write("}");
    }


    public void visit(OWLDataRangeRestriction node) {
        node.getDataRange().accept(this);
        write("[");
        write(node.getFacetRestrictions(), FACET_RESTRICTION_SEPARATOR, false);
        write("]");
    }


    public void visit(OWLTypedConstant node) {
        if (node.getDataType().getURI().equals(XSDVocabulary.DOUBLE.getURI())) {
            write(node.getLiteral());
        }
        else if (node.getDataType().getURI().equals(XSDVocabulary.STRING.getURI())) {
            write("\"");
            write(node.getLiteral());
            write("\"");
        }
        else if (node.getDataType().getURI().equals(XSDVocabulary.FLOAT.getURI())) {
            write(node.getLiteral());
            write("f");
        }
        else if (node.getDataType().getURI().equals(XSDVocabulary.INT.getURI())) {
            write(node.getLiteral());
        }
        else if (node.getDataType().getURI().equals(XSDVocabulary.INTEGER.getURI())) {
            write(node.getLiteral());
        }
        else {
            write("\"");
            pushTab(getIndent());
            write(node.getLiteral(), wrap ? LINE_LENGTH : Integer.MAX_VALUE);
            popTab();
            write("\"^^");
            write(node.getDataType().getURI());
        }
    }


    public void visit(OWLUntypedConstant node) {
        write("\"");
        pushTab(getIndent());
        write(node.getLiteral(), wrap ? LINE_LENGTH : Integer.MAX_VALUE);
        popTab();
        write("\"");
        if (node.hasLang()) {
            write("@");
            write(node.getLang());
        }
    }


    public void visit(OWLDataRangeFacetRestriction node) {
        write(node.getFacet().getSymbolicForm());
        writeSpace();
        node.getFacetValue().accept(this);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Property expression stuff
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLObjectPropertyInverse property) {
        write(INVERSE);
        write("(");
        property.getInverse().accept(this);
        write(")");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Annotation stuff
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLObjectAnnotation annotation) {
        write(annotation.getAnnotationURI());
        writeSpace();
        annotation.getAnnotationValue().accept(this);
    }


    public void visit(OWLConstantAnnotation annotation) {
        write(annotation.getAnnotationURI());
        writeSpace();
        annotation.getAnnotationValue().accept(this);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Stand alone axiom representation
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLSubClassAxiom axiom) {
        axiom.getSubClass().accept(this);
        write(SUBCLASS_OF);
        axiom.getSuperClass().accept(this);
    }


    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        write(NOT);
        write("(");
        axiom.getSubject().accept(this);
        write(" ");
        axiom.getProperty().accept(this);
        write(" ");
        axiom.getObject().accept(this);
        write(")");
    }


    private void writePropertyCharacteristic(ManchesterOWLSyntax characteristic, OWLPropertyExpression prop) {
        write(characteristic);
        write("(");
        prop.accept(this);
        write(")");
    }


    public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(ANTI_SYMMETRIC, axiom.getProperty());
    }


    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(REFLEXIVE, axiom.getProperty());
    }


    public void visit(OWLDisjointClassesAxiom axiom) {
        write(axiom.getDescriptions(), DISJOINT_WITH, wrap);
    }


    public void visit(OWLDataPropertyDomainAxiom axiom) {
        axiom.getProperty().accept(this);
        write(DOMAIN);
        axiom.getDomain().accept(this);
    }


    public void visit(OWLImportsDeclaration axiom) {
    }


    public void visit(OWLAxiomAnnotationAxiom axiom) {
    }


    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        axiom.getProperty().accept(this);
        write(DOMAIN);
        axiom.getDomain().accept(this);
    }


    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        write(axiom.getProperties(), EQUIVALENT_TO, wrap);
    }


    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        write(NOT);
        write("(");
        axiom.getSubject().accept(this);
        write(" ");
        axiom.getProperty().accept(this);
        write(" ");
        axiom.getObject().accept(this);
        write(")");
    }


    public void visit(OWLDifferentIndividualsAxiom axiom) {
        write(axiom.getIndividuals(), DIFFERENT_FROM, wrap);
    }


    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        write(axiom.getProperties(), DISJOINT_WITH, wrap);
    }


    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        write(axiom.getProperties(), DISJOINT_WITH, wrap);
    }


    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        axiom.getProperty().accept(this);
        write(RANGE);
        axiom.getRange().accept(this);
    }


    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        write(" ");
        axiom.getProperty().accept(this);
        write(" ");
        axiom.getObject().accept(this);
    }


    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(FUNCTIONAL, axiom.getProperty());
    }


    public void visit(OWLObjectSubPropertyAxiom axiom) {
        axiom.getSubProperty().accept(this);
        write(SUB_PROPERTY_OF);
        axiom.getSuperProperty().accept(this);
    }


    public void visit(OWLDisjointUnionAxiom axiom) {
        axiom.getOWLClass().accept(this);
        write(DISJOINT_UNION_OF);
        for(Iterator<OWLDescription> it = axiom.getDescriptions().iterator(); it.hasNext(); ) {
            it.next().accept(this);
            if(it.hasNext()) {
                write(", ");
            }
        }
    }


    public void visit(OWLDeclarationAxiom axiom) {
        
    }


    public void visit(OWLEntityAnnotationAxiom axiom) {
    }


    public void visit(OWLOntologyAnnotationAxiom axiom) {
    }


    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(SYMMETRIC, axiom.getProperty());
    }


    public void visit(OWLDataPropertyRangeAxiom axiom) {
        axiom.getProperty().accept(this);
        write(RANGE);
        axiom.getRange().accept(this);
    }


    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        writePropertyCharacteristic(FUNCTIONAL, axiom.getProperty());
    }


    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        write(axiom.getProperties(), EQUIVALENT_TO, wrap);
    }


    public void visit(OWLClassAssertionAxiom axiom) {
        axiom.getIndividual().accept(this);
        write(TYPES);
        axiom.getDescription().accept(this);
    }


    public void visit(OWLEquivalentClassesAxiom axiom) {
        write(axiom.getDescriptions(), EQUIVALENT_TO, wrap);
    }


    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        write(" ");
        axiom.getProperty().accept(this);
        write(" ");
        axiom.getObject().accept(this);
    }


    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(TRANSITIVE, axiom.getProperty());
    }


    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(IRREFLEXIVE, axiom.getProperty());
    }


    public void visit(OWLDataSubPropertyAxiom axiom) {
        axiom.getSubProperty().accept(this);
        write(SUB_PROPERTY_OF);
        axiom.getSuperProperty().accept(this);
    }


    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        writePropertyCharacteristic(INVERSE_FUNCTIONAL, axiom.getProperty());
    }


    public void visit(OWLSameIndividualsAxiom axiom) {
        write(axiom.getIndividuals(), SAME_AS, wrap);
    }


    public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
        for (Iterator<OWLObjectPropertyExpression> it = axiom.getPropertyChain().iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                write(" o ");
            }
        }
        write(SUB_PROPERTY_OF);
        axiom.getSuperProperty().accept(this);
    }


    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        axiom.getFirstProperty().accept(this);
        write(INVERSE);
        axiom.getSecondProperty().accept(this);
    }


    public void visit(SWRLRule rule) {
        for (Iterator<SWRLAtom> it = rule.getBody().iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                write(", ");
            }
        }
        write(" -> ");
        for (Iterator<SWRLAtom> it = rule.getHead().iterator(); it.hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                write(", ");
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // SWRL
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


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
        for(Iterator<SWRLAtomDObject> it = node.getArguments().iterator(); it.hasNext(); ) {
            it.next().accept(this);
            if(it.hasNext()) {
                write(", ");
            }
        }
        write(")");
    }


    public void visit(SWRLAtomDVariable node) {
        write("?");
        write(node.getURI().getFragment());
    }


    public void visit(SWRLAtomIVariable node) {
        write("?");
        write(node.getURI().getFragment());
    }


    public void visit(SWRLAtomIndividualObject node) {
        node.getIndividual().accept(this);
    }


    public void visit(SWRLAtomConstantObject node) {
        node.getConstant().accept(this);
    }


    public void visit(SWRLSameAsAtom node) {
        write(SAME_AS);
        write("(");
        node.getFirstArgument().accept(this);
        write(", ");
        node.getSecondArgument().accept(this);
        write(")");
    }


    public void visit(SWRLDifferentFromAtom node) {
        write(DIFFERENT_FROM);
        write("(");
        node.getFirstArgument().accept(this);
        write(", ");
        node.getSecondArgument().accept(this);
        write(")");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Ontology
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public void visit(OWLOntology ontology) {

    }
}
