package org.coode.owl.rdf.rdfxml;

import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.*;

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
 * Date: 03-Jan-2007<br><br>
 */
public class RDFXMLOntologyStorer implements OWLOntologyStorer {

    public boolean canStoreOntology(OWLOntologyFormat ontologyFormat) {
        if(ontologyFormat instanceof RDFXMLOntologyFormat) {
            return true;
        }
        return false;
    }


    public void storeOntology(OWLOntologyManager manager, OWLOntology ontology, URI physicalURI, OWLOntologyFormat ontologyFormat) throws
                                                                                                       OWLOntologyStorageException {
        try {
            File file = new File(physicalURI);
            // Ensure that the files exist
            file.getParentFile().mkdirs();
            OutputStream os = new FileOutputStream(file);
            RDFXMLRenderer renderer = new RDFXMLRenderer(manager, ontology, new BufferedWriter(new OutputStreamWriter(os)));
            renderer.render();
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
