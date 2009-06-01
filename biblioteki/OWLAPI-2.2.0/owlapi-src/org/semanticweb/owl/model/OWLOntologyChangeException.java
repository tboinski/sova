package org.semanticweb.owl.model;
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
 * Date: 13-Apr-2007<br><br>
 *
 * A high level exception interface that describes errors that occurred when
 * applying changes to ontologies.
 */
public abstract class OWLOntologyChangeException extends OWLException {

    private OWLOntologyChange change;

    public OWLOntologyChangeException(OWLOntologyChange change, String message) {
        super(message);
        this.change = change;
    }


    public OWLOntologyChangeException(OWLOntologyChange change, String message, Throwable cause) {
        super(message, cause);
        this.change = change;
    }


    public OWLOntologyChangeException(OWLOntologyChange change, Throwable cause) {
        super(cause);
        this.change = change;
    }

    /**
     * Gets the ontology change that caused the exception.
     */
    public OWLOntologyChange getChange() {
        return change;
    }
}
