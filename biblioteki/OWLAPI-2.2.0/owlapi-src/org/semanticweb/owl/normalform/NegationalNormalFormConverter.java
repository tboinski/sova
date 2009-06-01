package org.semanticweb.owl.normalform;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLDescriptionVisitorAdapter;
import uk.ac.manchester.cs.owl.OWLDataFactoryImpl;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
 * Date: 24-Sep-2007<br><br>
 */
public class NegationalNormalFormConverter implements NormalFormRewriter {

    private OWLDataFactory dataFactory;

    private OWLObjectComplementOfExtractor extractor;


    public NegationalNormalFormConverter(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
        extractor = new OWLObjectComplementOfExtractor();
    }


    public boolean isInNormalForm(OWLDescription description) {
        // The description is in negational normal form if negations
        // only appear in front of named concepts
        extractor.getComplementedDescriptions(description);
        for (OWLDescription desc : extractor.getComplementedDescriptions(description)) {
            if (desc.isAnonymous()) {
                return false;
            }
        }
        return true;
    }


    public OWLDescription convertToNormalForm(OWLDescription description) {
        // We just seek out negated descriptions and transform
        // them to NNF
        return seekNegatedDescriptions(description);
    }


    /**
     * Processes a description to ensure that the appropriate parts of the
     * description are in negational normal form.
     * @param description The description to be processed.
     * @return The description in negational normal form.
     */
    private OWLDescription seekNegatedDescriptions(OWLDescription description) {
        NegatedDescriptionSeeker seeker = new NegatedDescriptionSeeker();
        return seeker.process(description);
    }


    /**
     * A utility class that seeks out negated descriptions that may be buried
     * inside complex descriptions.  Whenever a negated description is found,
     * it is converted to negational normal form (NNF).
     */
    private class NegatedDescriptionSeeker extends OWLDescriptionVisitorAdapter {

        private OWLDescription result;


        /**
         * Examines the specified description and converts any nested
         * negated descriptions into negational normal form.
         * @param desc The description to be converted to negational
         *             normal form.
         * @return The description in negational normal form.
         */
        public OWLDescription process(OWLDescription desc) {
            result = desc;
            desc.accept(this);
            return result;
        }


        public void visit(OWLObjectAllRestriction desc) {
            OWLDescription filler = seekNegatedDescriptions(desc.getFiller());
            result = dataFactory.getOWLObjectAllRestriction(desc.getProperty(), filler);
        }


        public void visit(OWLObjectComplementOf desc) {
            result = convertNegatedDescription(desc.getOperand());
        }


        public void visit(OWLObjectExactCardinalityRestriction desc) {
            OWLDescription filler = seekNegatedDescriptions(desc.getFiller());
            result = dataFactory.getOWLObjectExactCardinalityRestriction(desc.getProperty(),
                                                                         desc.getCardinality(),
                                                                         filler);
        }


        public void visit(OWLObjectIntersectionOf desc) {
            Set<OWLDescription> opsnew = new HashSet<OWLDescription>();
            boolean changed = false;
            for (OWLDescription op : desc.getOperands()) {
                OWLDescription opnew = seekNegatedDescriptions(op);
                opsnew.add(opnew);
                if (!op.equals(opnew)) {
                    changed = true;
                }
            }
            if (changed) {
                result = dataFactory.getOWLObjectIntersectionOf(opsnew);
            }
            else {
                result = desc;
            }
        }


        public void visit(OWLObjectMaxCardinalityRestriction desc) {
            OWLDescription filler = seekNegatedDescriptions(desc.getFiller());
            if (!filler.equals(desc.getFiller())) {
                result = dataFactory.getOWLObjectMaxCardinalityRestriction(desc.getProperty(),
                                                                           desc.getCardinality(),
                                                                           filler);
            }
            else {
                result = desc;
            }
        }


        public void visit(OWLObjectMinCardinalityRestriction desc) {
            OWLDescription filler = seekNegatedDescriptions(desc.getFiller());
            if (!filler.equals(desc.getFiller())) {
                result = dataFactory.getOWLObjectMinCardinalityRestriction(desc.getProperty(),
                                                                           desc.getCardinality(),
                                                                           filler);
            }
            else {
                result = desc;
            }
        }


        public void visit(OWLObjectSomeRestriction desc) {
            OWLDescription filler = seekNegatedDescriptions(desc.getFiller());
            if (!filler.equals(desc.getFiller())) {
                result = dataFactory.getOWLObjectSomeRestriction(desc.getProperty(), filler);
            }
            else {
                result = desc;
            }
        }


        public void visit(OWLObjectUnionOf desc) {
            Set<OWLDescription> opsnew = new HashSet<OWLDescription>();
            boolean changed = false;
            for (OWLDescription op : desc.getOperands()) {
                OWLDescription opnew = seekNegatedDescriptions(op);
                opsnew.add(opnew);
                if (!op.equals(opnew)) {
                    changed = true;
                }
            }
            if (changed) {
                result = dataFactory.getOWLObjectUnionOf(opsnew);
            }
            else {
                result = desc;
            }
        }
    }


    /**
     * Assumes that the description was negated.  The deacription is processed
     * by pushing the negation inwards.  For example, if the input was
     * p some C, then the return value would be p only (not C)
     * @param description The description to be converted
     * @return The converted description
     */
    private OWLDescription convertNegatedDescription(OWLDescription description) {
        OWLDescriptionConverter converter = new OWLDescriptionConverter();
        return converter.convert(description);
    }


    private class OWLDescriptionConverter extends OWLDescriptionVisitorAdapter {

        private OWLDescription result;

        /**
         * Converts the specified description into NNF.  It is
         * assumed that the description is negated.  Hence,  if the
         * description is  p some C then the converted description will
         * be p only D, where D is the converted form of C.
         * @param desc The (assumed to be negated) description.
         * @return The description in NNF.
         */
        public OWLDescription convert(OWLDescription desc) {
            result = null;
            desc.accept(this);
            if(result == null) {
                return dataFactory.getOWLObjectComplementOf(desc);
            }
            return result;
        }


        public void visit(OWLDataExactCardinalityRestriction desc) {
            //  == n R C  <=>  >= n R C and <= n R C
            // Decompose
            OWLDataPropertyExpression prop = desc.getProperty();
            int cardinality = desc.getCardinality();
            Set<OWLDescription> ops = new HashSet<OWLDescription>();
            OWLDescription minCard = dataFactory.getOWLDataMinCardinalityRestriction(prop,
                                                                                     cardinality,
                                                                                     desc.getFiller());
            OWLDescription maxCard = dataFactory.getOWLDataMaxCardinalityRestriction(prop,
                                                                                     cardinality,
                                                                                     desc.getFiller());
            ops.add(convertNegatedDescription(minCard));
            ops.add(convertNegatedDescription(maxCard));
            result = dataFactory.getOWLObjectUnionOf(ops);
        }


        public void visit(OWLDataMaxCardinalityRestriction desc) {
            // not(at-most n R C)
            // at-least n+1 R C
            result = dataFactory.getOWLDataMinCardinalityRestriction(desc.getProperty(),
                                                                     desc.getCardinality() + 1,
                                                                     desc.getFiller());
        }


        public void visit(OWLDataMinCardinalityRestriction desc) {
            int card = desc.getCardinality();
            if (card != 0) {
                card = card - 1;
                result = dataFactory.getOWLDataMaxCardinalityRestriction(desc.getProperty(), card, desc.getFiller());
            }
            else {
                result = dataFactory.getOWLNothing();
            }
        }


        public void visit(OWLObjectAllRestriction desc) {
            result = dataFactory.getOWLObjectSomeRestriction(desc.getProperty(),
                                                             convertNegatedDescription(desc.getFiller()));
        }


        public void visit(OWLObjectComplementOf desc) {
            // Double negation, which cancels out (polarity is
            // positive) so we just process the operand in case
            // there is negation buried in the operand
            result = seekNegatedDescriptions(desc.getOperand());
        }


        public void visit(OWLObjectExactCardinalityRestriction desc) {
            //  == n R C  <=>  >= n R C and <= n R C
            // Decompose
            OWLObjectPropertyExpression prop = desc.getProperty();
            int cardinality = desc.getCardinality();
            Set<OWLDescription> ops = new HashSet<OWLDescription>();
            OWLDescription minCard = dataFactory.getOWLObjectMinCardinalityRestriction(prop,
                                                                                       cardinality,
                                                                                       desc.getFiller());
            OWLDescription maxCard = dataFactory.getOWLObjectMaxCardinalityRestriction(prop,
                                                                                       cardinality,
                                                                                       desc.getFiller());
            ops.add(convertNegatedDescription(minCard));
            ops.add(convertNegatedDescription(maxCard));
            result = dataFactory.getOWLObjectUnionOf(ops);
        }


        public void visit(OWLObjectIntersectionOf desc) {
            Set<OWLDescription> opsnew = new HashSet<OWLDescription>();
            for (OWLDescription op : desc.getOperands()) {
                opsnew.add(convertNegatedDescription(op));
            }
            result = dataFactory.getOWLObjectUnionOf(opsnew);
        }


        public void visit(OWLObjectMaxCardinalityRestriction desc) {
            // not(at-most n R C)
            // at-least n+1 R C
            result = dataFactory.getOWLObjectMinCardinalityRestriction(desc.getProperty(),
                                                                       desc.getCardinality() + 1,
                                                                       seekNegatedDescriptions(desc.getFiller()));
        }


        public void visit(OWLObjectMinCardinalityRestriction desc) {
            int card = desc.getCardinality();
            if (card != 0) {
                card = card - 1;
                result = dataFactory.getOWLObjectMaxCardinalityRestriction(desc.getProperty(),
                                                                           card,
                                                                           seekNegatedDescriptions(desc.getFiller()));
            }
            else {
                result = dataFactory.getOWLNothing();
            }
        }


        public void visit(OWLObjectSomeRestriction desc) {
            result = dataFactory.getOWLObjectAllRestriction(desc.getProperty(),
                                                            convertNegatedDescription(desc.getFiller()));
        }


        public void visit(OWLObjectUnionOf desc) {
            Set<OWLDescription> opsnew = new HashSet<OWLDescription>();
            boolean changed = false;
            for (OWLDescription op : desc.getOperands()) {
                OWLDescription opnew = convertNegatedDescription(op);
                if(!opnew.equals(op)) {
                    changed = true;
                }
                opsnew.add(op);
            }
            if (changed) {
                result = dataFactory.getOWLObjectIntersectionOf(opsnew);
            }
            else {
                result = desc;
            }
        }


        public void visit(OWLObjectValueRestriction desc) {
            // Rewrite as  \exists R {val} and negate this
            OWLDescription filler = dataFactory.getOWLObjectComplementOf(dataFactory.getOWLObjectOneOf(Collections.singleton(
                    desc.getValue())));
            result = dataFactory.getOWLObjectAllRestriction(desc.getProperty(), filler);
        }


        public void visit(OWLObjectOneOf desc) {
            // {a, b, c} -->  {a} or {b} or {c}
            // NNF:  not{a} and not{b} and not{c}
            Set<OWLDescription> ops = new HashSet<OWLDescription>();
            for (OWLIndividual ind : desc.getIndividuals()) {
                OWLDescription singleton = dataFactory.getOWLObjectOneOf(Collections.singleton(ind));
                ops.add(dataFactory.getOWLObjectComplementOf(singleton));
            }
            result = dataFactory.getOWLObjectIntersectionOf(ops);
        }
    }


    public static void main(String[] args) {
        OWLDataFactory df = new OWLDataFactoryImpl();
        Set<OWLDescription> ops = new HashSet<OWLDescription>();
        ops.add(df.getOWLClass(URI.create("A")));
                ops.add(df.getOWLObjectSomeRestriction(df.getOWLObjectProperty(URI.create("p")),
                                                   df.getOWLClass(URI.create("B"))));
                OWLDescription d = df.getOWLObjectIntersectionOf(ops);
                OWLDescription negD = df.getOWLObjectComplementOf(d);


        NegationalNormalFormConverter converter = new NegationalNormalFormConverter(df);

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            converter.convertToNormalForm(negD);
        }
        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0));





    }
}
