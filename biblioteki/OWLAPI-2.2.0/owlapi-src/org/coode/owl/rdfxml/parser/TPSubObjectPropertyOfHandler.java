package org.coode.owl.rdfxml.parser;

import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;

import java.net.URI;
import java.util.List;
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
 * Date: 08-Dec-2006<br><br>
 */
public class TPSubObjectPropertyOfHandler extends TriplePredicateHandler {

    public TPSubObjectPropertyOfHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_SUB_OBJECT_PROPERTY_OF.getURI());
    }


    public boolean canHandleStreaming(URI subject, URI predicate, URI object) throws OWLException {
        // If the subject is anonymous, it *might* be a property chain - we
        // can't handle these in a streaming manner really
        return !isAnonymous(subject);
    }


    public void handleTriple(URI subject, URI predicate, URI object) throws OWLException {
        if (isAnonymous(subject) && getConsumer().hasPredicateObject(subject,
                                                                     OWLRDFVocabulary.RDF_TYPE.getURI(),
                                                                     OWLRDFVocabulary.RDF_LIST.getURI())) {
            // Property chain!
            OptimisedListTranslator<OWLObjectPropertyExpression> translator = new OptimisedListTranslator<OWLObjectPropertyExpression>(
                    getConsumer(),
                    new OWLObjectPropertyExpressionListItemTranslator(getConsumer()));
            List<OWLObjectPropertyExpression> props = translator.translateList(subject);
            addAxiom(getDataFactory().getOWLObjectPropertyChainSubPropertyAxiom(props,
                                                                                translateObjectProperty(object)));
        }
        else {
            addAxiom(getDataFactory().getOWLSubObjectPropertyAxiom(translateObjectProperty(subject),
                                                                   translateObjectProperty(object)));
        }
        consumeTriple(subject, predicate, object);
    }
}
