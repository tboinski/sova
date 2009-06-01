package org.semanticweb.owl.io;

import java.io.*;
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
 * Date: 15-Nov-2007<br><br>
 *
 * An ontology input source which can read from a stream.
 */
public class StreamInputSource implements OWLOntologyInputSource {

    private URI physicalURI;

    private byte [] buffer;


    /**
     * Constructs an input source which will read an ontology from
     * a representation from the specified stream.
     * @param is The stream that the ontology representation will be
     * read from.
     */
    public StreamInputSource(InputStream is) {
        this(is, URI.create("http://org.semanticweb.ontologies/Ontology" + System.nanoTime()));
    }


    /**
     * Constructs an input source which will read an ontology from
     * a representation from the specified stream.
     * @param stream The stream that the ontology representation will be
     * read from.
     * @param physicalURI The physical URI which will be used as the base
     * of the document if needed.
     */
    public StreamInputSource(InputStream stream, URI physicalURI) {
        this.physicalURI = physicalURI;
        readIntoBuffer(stream);
    }


    /**
     * Reads all the bytes from the specified stream into a temporary buffer,
     * which is necessary because we may need to access the input stream more
     * than once.  In other words, this method caches the input stream.
     * @param stream The stream to be "cached"
     */
    private void readIntoBuffer(InputStream stream) {
        try {
            byte [] tempBuffer = new byte [4096];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read;
            while((read = stream.read(tempBuffer)) != -1) {
                bos.write(tempBuffer, 0, read);
            }
            buffer = bos.toByteArray();
        }
        catch (IOException e) {
            throw new OWLOntologyInputSourceException(e);
        }
    }

    public boolean isInputStreamAvailable() {
        return true;
    }


    public InputStream getInputStream() {
        return new ByteArrayInputStream(buffer);
    }


    public URI getPhysicalURI() {
        return physicalURI;
    }


    public Reader getReader() {
        return null;
    }


    public boolean isReaderAvailable() {
        return false;
    }
}
