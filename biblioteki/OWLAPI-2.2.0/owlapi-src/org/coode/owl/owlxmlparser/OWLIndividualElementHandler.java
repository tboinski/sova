package org.coode.owl.owlxmlparser;

import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLIndividual;

import java.net.URI;
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
 * Date: 13-Dec-2006<br><br>
 */
public class OWLIndividualElementHandler extends AbstractOWLElementHandler<OWLIndividual> {

    private OWLIndividual individual;

    private URI name;

    public OWLIndividualElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }


    public OWLIndividual getOWLObject() {
        return individual;
    }


    public void attribute(String localName, String value) throws OWLXMLParserException {
        if(localName.equals("URI")) {
            name = getURI(value);
        }
    }


    final public void endElement() throws OWLXMLParserException {
//        URI uri = getNameAttribute();
        individual = getOWLDataFactory().getOWLIndividual(name);
        getParentHandler().handleChild(this);
    }
}
