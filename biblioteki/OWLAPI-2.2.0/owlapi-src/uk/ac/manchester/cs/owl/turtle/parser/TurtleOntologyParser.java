package uk.ac.manchester.cs.owl.turtle.parser;

import org.coode.owl.rdf.turtle.TurtleOntologyFormat;
import org.semanticweb.owl.io.AbstractOWLParser;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.OWLParserException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyFormat;

import java.io.BufferedInputStream;
import java.io.IOException;
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
 * Date: 23-Feb-2008<br><br>
 */
public class TurtleOntologyParser extends AbstractOWLParser {

    public OWLOntologyFormat parse(OWLOntologyInputSource inputSource, OWLOntology ontology) throws OWLParserException {
        try {
            TurtleParser parser;
            if(inputSource.isReaderAvailable()) {
                parser = new TurtleParser(inputSource.getReader(), new ConsoleTripleHandler(), inputSource.getPhysicalURI().toString());
            }
            else if(inputSource.isInputStreamAvailable()) {
                parser = new TurtleParser(inputSource.getInputStream(), new ConsoleTripleHandler(), inputSource.getPhysicalURI().toString());
            }
            else {
                parser = new TurtleParser(new BufferedInputStream(inputSource.getPhysicalURI().toURL().openStream()), new ConsoleTripleHandler(), inputSource.getPhysicalURI().toString());
            }

            OWLRDFConsumerAdapter consumer = new OWLRDFConsumerAdapter(getOWLOntologyManager(), ontology, parser);
            parser.setTripleHandler(consumer);
            parser.parseDocument();
            return new TurtleOntologyFormat();

        }
        catch(ParseException e) {
            throw new TurtleParserException(e);
        }
        catch (IOException e) {
            throw new TurtleParserException(e);
        }
    }
}
