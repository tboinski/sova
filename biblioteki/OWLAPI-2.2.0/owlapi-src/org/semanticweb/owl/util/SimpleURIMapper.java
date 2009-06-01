package org.semanticweb.owl.util;

import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntologyURIMapper;

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
 * Date: 12-Dec-2006<br><br>
 */
public class SimpleURIMapper implements OWLOntologyURIMapper {

    private URI ontologyURI;

    private URI physicalURI;

    public SimpleURIMapper(URI ontologyURI, URI physicalURI) {
        this.ontologyURI = ontologyURI;
        this.physicalURI = physicalURI;
    }


    public URI getPhysicalURI(URI ontologyURI) {
        if(this.ontologyURI.equals(ontologyURI)) {
            return physicalURI;
        }
        else {
            return null;
        }
    }
}
