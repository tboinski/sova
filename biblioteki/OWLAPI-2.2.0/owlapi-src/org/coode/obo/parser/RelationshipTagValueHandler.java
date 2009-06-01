package org.coode.obo.parser;

import org.semanticweb.owl.model.*;

import java.net.URI;
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
 * Date: 06-Mar-2007<br><br>
 */
public class RelationshipTagValueHandler extends AbstractTagValueHandler {

    public RelationshipTagValueHandler(OBOConsumer consumer) {
        super(OBOVocabulary.RELATIONSHIP.getName(), consumer);
    }


    public void handle(String id, String value) {
        URI propURI = getURIFromValue(value.substring(0, value.indexOf(' ')).trim());
        URI fillerURI = getURIFromValue(value.substring(value.indexOf(' '), value.length()).trim());
        OWLObjectProperty prop = getDataFactory().getOWLObjectProperty(propURI);
        OWLClass filler = getDataFactory().getOWLClass(fillerURI);
        OWLDescription restriction = getDataFactory().getOWLObjectSomeRestriction(prop, filler);
        OWLClass subCls = getDataFactory().getOWLClass(getURIFromValue(id));
        applyChange(new AddAxiom(getOntology(), getDataFactory().getOWLSubClassAxiom(subCls, restriction)));
    }
}
