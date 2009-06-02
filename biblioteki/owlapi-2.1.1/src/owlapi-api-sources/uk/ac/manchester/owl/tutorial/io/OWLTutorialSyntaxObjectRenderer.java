package uk.ac.manchester.owl.tutorial.io;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.QNameShortFormProvider;
import org.semanticweb.owl.util.ShortFormProvider;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.*;

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
 * <p>A renderer that provides an HTML version of the ontology.</p>
 * 
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 24-April-2007<br>
 * <br>
 */
public class OWLTutorialSyntaxObjectRenderer implements OWLObjectVisitor {

    private OWLOntology ontology;

    private ShortFormProvider shortForms;

    private Writer writer;

    private int pos;

    int lastNewLinePos;

    private boolean tables = true;
    private static int TABLE_COLUMNS = 3;

    public String labelFor(OWLEntity entity) {
        return shortForms.getShortForm(entity);
    }

    public OWLTutorialSyntaxObjectRenderer(OWLOntology ontology, Writer writer) {
        this.ontology = ontology;
        this.writer = writer;
        this.shortForms = new QNameShortFormProvider();
    }

    public void visit(OWLOntologyAnnotationAxiom axiom) {
        /* Ignore annotations */
    }

    private void write(String s) {
        try {
            int newLineIndex = s.indexOf('\n');
            if (newLineIndex != -1) {
                lastNewLinePos = pos + newLineIndex;
            }
            pos += s.length();
            writer.write(s);
        } catch (IOException e) {
            throw new OWLRuntimeException(e);
        }
    }

    private void write(URI uri) {
        write("<");
        write(uri.toString());
        write(">");
    }

    public void header() {
        write("<html>\n");
        write("<head>\n");
        write("<style>\n");
        write("body { font-family: sans-serif; }\n");
        write(".key { color: grey; font-size: 75%; }\n");
        write(".op { color: grey; }\n");
        write(".cl { color: #800; }\n");
        write(".pr { color: #080; }\n");
        write(".in { color: #008; }\n");
        write(".box { border: solid 1px grey; padding: 10px; margin: 10px; }\n");
        int width = 100 / TABLE_COLUMNS;
        write("table { width: 100%; }\n");
        write("td { padding-left: 10px; padding-right: 10px; width: " + width
                + "%;}\n");
        write("</style>\n");
        write("<body>\n");
    }

    public void footer() {
        write("</body>\n");
        write("</html>\n");
    }

    private void writeCollection(Collection<? extends OWLObject> objects) {
        if (tables) {
            writeTable(objects);
        } else {
            writeList(objects);
        }
    }

    private void writeTable(Collection<? extends OWLObject> objects) {
        writeTableStart();
        int count = 0;
        for (Iterator<? extends OWLObject> it = objects.iterator(); it
                .hasNext();) {
            if (count % TABLE_COLUMNS == 0) {
                if (count > 0) {
                    writeTableRowEnd();
                }
                writeTableRowStart();
            }

            writeTableCellStart();
            it.next().accept(this);
            writeTableCellEnd();
            count++;
        }
        writeTableRowEnd();
        writeTableEnd();
    }

    private void writeList(Collection<? extends OWLObject> objects) {
        writeListStart();
        for (Iterator<? extends OWLObject> it = objects.iterator(); it
                .hasNext();) {
            writeListItemStart();
            it.next().accept(this);
            writeListItemEnd();
        }
        writeListEnd();
    }

    public void visit(OWLOntology ontology) {
        header();
        write("<h1>");
        write(ontology.getURI().toString());
        write("</h1>\n");
        write("<div>");
        write("<div class='box'>\n");
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>(ontology.getAxioms());
        axioms.removeAll(ontology.getImportsDeclarations());
        for (OWLImportsDeclaration decl : ontology.getImportsDeclarations()) {
            decl.accept(this);
            write("\n");
        }

        write("<h2>Classes</h2>\n");

        writeCollection(ontology.getReferencedClasses());

        write("</div>\n");

        write("<div class='box'>\n");
        write("<h2>Properties</h2>\n");

        writeCollection(ontology.getReferencedObjectProperties());
        writeCollection(ontology.getReferencedDataProperties());

        write("</div>\n");

        write("<div class='box'>\n");
        write("<h2>Individuals</h2>\n");

        writeCollection(ontology.getReferencedIndividuals());

        write("</div>");
        write("<div>");

        write("<div class='box'>");
        write("<h2>Axioms</h2>\n");
        writeListStart();

        for (OWLAxiom ax : ontology.getClassAxioms()) {
            writeListItemStart();

            ax.accept(this);
            writeListItemEnd();

        }

        for (OWLAxiom ax : ontology.getObjectPropertyAxioms()) {
            writeListItemStart();
            ax.accept(this);
            writeListItemEnd();

        }

        for (OWLAxiom ax : ontology.getDataPropertyAxioms()) {
            writeListItemStart();
            ax.accept(this);
            writeListItemEnd();

        }

        for (OWLAxiom ax : ontology.getIndividualAxioms()) {
            writeListItemStart();
            ax.accept(this);
            writeListItemEnd();

        }

        writeListEnd();
        write("</div>");

        footer();
    }

    public void write(String str, OWLObject o) {
        write(str);
        write("(");
        o.accept(this);
        write(")");
    }

    private void write(Collection<? extends OWLObject> objects, String separator) {
        // int indent = getIndent();
        for (Iterator<? extends OWLObject> it = objects.iterator(); it
                .hasNext();) {
            it.next().accept(this);
            if (it.hasNext()) {
                writeSpace();
                write(separator);
                writeSpace();
                // writeIndent(indent);
            }
        }
    }

    private void write(Collection<? extends OWLObject> objects) {
        write(objects, "");
    }

    public void writeOpenBracket() {
        write("(");
    }

    public void writeCloseBracket() {
        write(")");
    }

    public void writeSpace() {
        write(" ");
    }

    public void writeAnnotations(OWLAxiom ax) {
        write(ax.getAnnotationAxioms(ontology));
        if (!ax.getAnnotationAxioms(ontology).isEmpty()) {
            writeSpace();
        }
    }

    public void visit(OWLConstantAnnotation annotation) {
        if (annotation.isLabel()) {
            write("label");
            writeOpenBracket();
            annotation.getAnnotationValue().accept(this);
            writeCloseBracket();
        } else if (annotation.isComment()) {
            write("comment");
            writeOpenBracket();
            annotation.getAnnotationValue().accept(this);
            writeCloseBracket();
        } else {
            write("annotation");
            writeOpenBracket();
            write(annotation.getAnnotationURI());
            writeSpace();
            annotation.getAnnotationValue().accept(this);
            writeCloseBracket();
        }
    }

    public void visit(OWLObjectAnnotation annotation) {
        write("annotation");
        writeOpenBracket();
        write(annotation.getAnnotationURI());
        writeSpace();
        annotation.getAnnotationValue().accept(this);
        writeCloseBracket();
    }

    public void writeListStart() {
        write("<ul>\n");
    }

    public void writeListEnd() {
        write("</ul>\n");
    }

    public void writeTableStart() {
        write("<table>\n");
    }

    public void writeTableEnd() {
        write("</table>\n");
    }

    public void writeTableRowStart() {
        write("<tr>\n");
    }

    public void writeTableRowEnd() {
        write("</tr>\n");
    }

    public void writeTableCellStart() {
        write("<td>\n");
    }

    public void writeTableCellEnd() {
        write("</td>\n");
    }

    public void writeListItemStart() {
        write("<li>\n");
    }

    public void writeListItemEnd() {
        write("</li>\n");
    }

    public void writePropertyCharacteristic(String str, OWLAxiom ax,
            OWLPropertyExpression prop) throws OWLRuntimeException {
        write(keyword(str));
        writeSpace();
        prop.accept(this);
    }

    public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("antisymmetric", axiom, axiom.getProperty());
    }

    public void visit(OWLAxiomAnnotationAxiom axiom) {
        // Ignored
    }

    public void visit(OWLClassAssertionAxiom axiom) {
        axiom.getIndividual().accept(this);
        write(keyword(":"));
        writeSpace();
        axiom.getDescription().accept(this);
    }

    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        writeSpace();
        axiom.getProperty().accept(this);
        writeSpace();
        axiom.getObject().accept(this);
    }

    public void visit(OWLDataPropertyDomainAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(keyword("domain"));
        writeSpace();
        axiom.getDomain().accept(this);
    }

    public void visit(OWLDataPropertyRangeAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(keyword("range"));
        writeSpace();
        axiom.getRange().accept(this);
    }

    public void visit(OWLDataSubPropertyAxiom axiom) {
        axiom.getSubProperty().accept(this);
        writeSpace();
        write(keyword("subProperty"));
        writeSpace();
        axiom.getSuperProperty().accept(this);
    }

    public void visit(OWLDeclarationAxiom axiom) {
    }

    public void visit(OWLDifferentIndividualsAxiom axiom) {
        write(axiom.getIndividuals(), keyword("!="));
    }

    public void visit(OWLDisjointClassesAxiom axiom) {
        write(axiom.getDescriptions(), keyword("|"));
    }

    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        write(axiom.getProperties(), keyword("|"));
    }

    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        write(keyword("disjoint"));
        write(axiom.getProperties(), keyword("|"));
    }

    public void visit(OWLDisjointUnionAxiom axiom) {
        axiom.getOWLClass().accept(this);
        writeSpace();
        write(keyword("=="));
        writeSpace();
        write(axiom.getDescriptions(), keyword("|"));
    }

    public void visit(OWLEntityAnnotationAxiom axiom) {
        // Ignored
    }

    public void visit(OWLEquivalentClassesAxiom axiom) {
        write(axiom.getDescriptions(), keyword("=="));
    }

    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        write(axiom.getProperties(), keyword("=="));
    }

    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        write(axiom.getProperties(), keyword("=="));
    }

    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        writePropertyCharacteristic("functional", axiom, axiom.getProperty());
    }

    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("functional", axiom, axiom.getProperty());
    }

    public void visit(OWLImportsDeclaration axiom) {
                   write(keyword("imports"));
        write(axiom.getImportedOntologyURI());
    }

    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("inversefunctional", axiom, axiom
                .getProperty());
    }

    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        axiom.getFirstProperty().accept(this);
        writeSpace();
        write(keyword("inverse"));
        writeSpace();
        axiom.getSecondProperty().accept(this);
    }

    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("Irreflexive", axiom, axiom.getProperty());
    }

    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
                axiom.getSubject().accept(this);
        writeSpace();
        write(keyword("notvalue"));
        axiom.getProperty().accept(this);
        writeSpace();
        axiom.getObject().accept(this);
    }

    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        writeSpace();
        write(keyword("notvalue"));
        axiom.getProperty().accept(this);
        writeSpace();
        axiom.getObject().accept(this);
    }

    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        axiom.getSubject().accept(this);
        writeSpace();
        axiom.getProperty().accept(this);
        writeSpace();
        axiom.getObject().accept(this);
    }

    public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
        write("chain");
        writeOpenBracket();
        write(axiom.getPropertyChain());
        writeCloseBracket();
        writeSpace();
        write(keyword("subProperty"));
        writeSpace();
        axiom.getSuperProperty().accept(this);
    }

    public void visit(OWLObjectPropertyDomainAxiom axiom) {
                axiom.getProperty().accept(this);
        writeSpace();
        write(keyword("domain"));
        writeSpace();
        axiom.getDomain().accept(this);
            }

    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        axiom.getProperty().accept(this);
        writeSpace();
        write(keyword("range"));
        writeSpace();
        axiom.getRange().accept(this);
    }

    public void visit(OWLObjectSubPropertyAxiom axiom) {
        axiom.getSubProperty().accept(this);
        writeSpace();
        write(keyword("subProperty"));
        writeSpace();
        axiom.getSuperProperty().accept(this);
    }

    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("reflexive", axiom, axiom.getProperty());
    }

    public void visit(OWLSameIndividualsAxiom axiom) {
        write(axiom.getIndividuals(), keyword("="));
    }

    public void visit(OWLSubClassAxiom axiom) {
        axiom.getSubClass().accept(this);
        writeSpace();
        write(keyword("subClass"));
        writeSpace();
        axiom.getSuperClass().accept(this);
    }

    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("symmetric", axiom, axiom.getProperty());
    }

    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        writePropertyCharacteristic("transitive", axiom, axiom.getProperty());
    }

    public void visit(OWLClass desc) {
        write("<span class='cl'>" + labelFor(desc) + "</span>");
    }

    private void writeRestriction(String str,
            OWLCardinalityRestriction restriction) {
        write(str);
        writeOpenBracket();
        write(Integer.toString(restriction.getCardinality()));
        writeSpace();
        restriction.getProperty().accept(this);
        if (restriction.isQualified()) {
            writeSpace();
            restriction.getFiller().accept(this);
        }
        writeCloseBracket();
    }

    private void writeRestriction(String str,
            OWLQuantifiedRestriction restriction) {
        writeRestriction(str, restriction.getProperty(), restriction
                .getFiller());
    }

    private void writeRestriction(String str, OWLPropertyExpression prop,
            OWLObject filler) throws OWLRuntimeException {
        write(str);
        writeOpenBracket();
        prop.accept(this);
        writeSpace();
        filler.accept(this);
        writeCloseBracket();
    }

    public void visit(OWLDataAllRestriction desc) {
        writeRestriction(operator("only"), desc);
    }

    public void visit(OWLDataExactCardinalityRestriction desc) {
        writeRestriction("exact", desc);
    }

    public void visit(OWLDataMaxCardinalityRestriction desc) {
        writeRestriction("atmost", desc);
    }

    public void visit(OWLDataMinCardinalityRestriction desc) {
        writeRestriction("atleast", desc);
    }

    public void visit(OWLDataSomeRestriction desc) {
        writeRestriction(operator("some"), desc);
    }

    public void visit(OWLDataValueRestriction desc) {
        writeRestriction("has-value", desc.getProperty(), desc.getValue());
    }

    public void visit(OWLObjectAllRestriction desc) {
        writeRestriction(operator("only"), desc);
    }

    public void visit(OWLObjectComplementOf desc) {
        write(operator("not"), desc.getOperand());
    }

    public void visit(OWLObjectExactCardinalityRestriction desc) {
        writeRestriction("exact", desc);
    }

    public void visit(OWLObjectIntersectionOf desc) {
        writeOpenBracket();
        write(desc.getOperands(), keyword("and"));
        writeCloseBracket();
    }

    public void visit(OWLObjectMaxCardinalityRestriction desc) {
        writeRestriction("atmost", desc);
    }

    public void visit(OWLObjectMinCardinalityRestriction desc) {
        writeRestriction("atleast", desc);
    }

    public void visit(OWLObjectOneOf desc) {
        write(operator("one-of"));
        writeOpenBracket();
        write(desc.getIndividuals());
        writeCloseBracket();
    }

    public void visit(OWLObjectSelfRestriction desc) {
        write("self", desc.getProperty());
    }

    public void visit(OWLObjectSomeRestriction desc) {
        writeRestriction(operator("some"), desc);
    }

    public void visit(OWLObjectUnionOf desc) {
        writeOpenBracket();
        write(desc.getOperands(), " or ");
        writeCloseBracket();
    }

    public void visit(OWLObjectValueRestriction desc) {
        writeRestriction("hasValue", desc.getProperty(), desc.getValue());
    }

    public void visit(OWLDataComplementOf node) {
        write(operator("not"), node.getDataRange());
    }

    public void visit(OWLDataOneOf node) {
        write(operator("one-of"));
        write("(");
        write(node.getValues());
        write(")");
    }

    public void visit(OWLDataType node) {
        write("Datatype");
        writeOpenBracket();
        write(node.getURI());
        writeCloseBracket();
    }

    public void visit(OWLDataRangeRestriction node) {
        write("DatatypeRestriction");
        writeOpenBracket();
        node.getDataRange().accept(this);
        for (OWLDataRangeFacetRestriction restriction : node
                .getFacetRestrictions()) {
            writeSpace();
            restriction.accept(this);
        }
        writeCloseBracket();
    }

    public void visit(OWLDataRangeFacetRestriction node) {
        write(node.getFacet().getURI());
        writeSpace();
        node.getFacetValue().accept(this);
    }

    public void visit(OWLTypedConstant node) {
        write("\"");
        write(node.getLiteral());
        write("\"");
        write("^^");
        write(node.getDataType().getURI());
    }

    public void visit(OWLUntypedConstant node) {
        write("\"");
        write(node.getLiteral());
        write("\"");
        if (node.hasLang()) {
            write("@");
            write(node.getLang());
        }
    }

    public void visit(OWLDataProperty property) {
        write("<span class='pr'>" + labelFor(property) + "</span>");
    }

    public void visit(OWLObjectProperty property) {
        write("<span class='pr'>" + labelFor(property) + "</span>");
    }

    public void visit(OWLObjectPropertyInverse property) {
        write("inv");
        writeOpenBracket();
        property.getInverse().accept(this);
        writeCloseBracket();
    }

    public void visit(OWLIndividual individual) {
        write("<span class='in'>" + labelFor(individual) + "</span>");
    }

    public void visit(SWRLRule rule) {
    }

    public void visit(SWRLAtomIndividualObject node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLClassAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLDataRangeAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLObjectPropertyAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLDataValuedPropertyAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLBuiltInAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLAtomDVariable node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLAtomIVariable node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLAtomConstantObject node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLDifferentFromAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public void visit(SWRLSameAsAtom node) {
        throw new OWLRuntimeException("NOT IMPLEMENTED!");
    }

    public String keyword(String str) {
        return ("<span class='key'>" + str + "</span>");
    }

    public String operator(String str) {
        return ("<span class='op'>" + str + "</span>");
    }
}
