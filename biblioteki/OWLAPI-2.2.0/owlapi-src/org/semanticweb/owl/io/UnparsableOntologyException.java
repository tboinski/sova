package org.semanticweb.owl.io;

import org.semanticweb.owl.model.OWLOntologyCreationException;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
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
 * Date: 11-Apr-2008<br><br>
 */
public class UnparsableOntologyException extends OWLOntologyCreationException {

    private static boolean includeStackTraceInMessage = false;

    private URI ontologyURI;

    private Map<OWLParser, Exception> exceptions;

    public UnparsableOntologyException(URI ontologyURI, Map<OWLParser, Exception> exceptions) {
        super("Could not parse ontology: " + ontologyURI);
        this.ontologyURI = ontologyURI;
        this.exceptions = new LinkedHashMap<OWLParser, Exception>(exceptions);
    }


    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        msg.append("Problem parsing ");
        msg.append(ontologyURI);
        msg.append("\n");
        msg.append("Could not parse ontology.  Either a suitable parser could not be found, or " + "parsing failed.  See parser logs below for explanation.\n");
        msg.append("The following parsers were tried.\n");
        int counter = 1;
        for (OWLParser parser : exceptions.keySet()) {
            msg.append(counter);
            msg.append(") ");
            msg.append(parser.getClass().getName());
            msg.append("\n");
            counter++;
        }
        msg.append("\n\nDetailed logs:\n");
        for (OWLParser parser : exceptions.keySet()) {
            Exception exception = exceptions.get(parser);
            msg.append("--------------------------------------------------------------------------------\n");
            msg.append("Parser class: ");
            msg.append(parser.getClass().getName());
            msg.append("\n");
            msg.append(exception.getMessage());
            msg.append("\n\n");
            if (includeStackTraceInMessage) {
                msg.append("    Stack trace:\n");
                for (StackTraceElement element : exception.getStackTrace()) {
                    msg.append("        ");
                    msg.append(element.toString());
                    msg.append("\n");
                }
                msg.append("\n\n");
            }
        }
        return msg.toString();
    }


    public Map<OWLParser, Exception> getExceptions() {
        return Collections.unmodifiableMap(exceptions);
    }
}
