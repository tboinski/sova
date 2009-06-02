package uk.ac.manchester.owl.tutorial.io;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyFormat;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyStorageException;
import org.semanticweb.owl.model.OWLOntologyStorer;
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
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 24-April-2007<br>
 * <br>
 */
public class OWLTutorialSyntaxOntologyStorer implements OWLOntologyStorer {

    /**
     * Determines if this storer can store an ontology in the specified ontology
     * format.
     * 
     * @param ontologyFormat
     *            The desired ontology format.
     * @return <code>true</code> if this storer can store an ontology in the
     *         desired format.
     */
    public boolean canStoreOntology(OWLOntologyFormat ontologyFormat) {
        return ontologyFormat.equals(new OWLTutorialSyntaxOntologyFormat());
    }

    /**
     * Stores an ontology at the specified physical URI in the specified format.
     * 
     * @param ontology
     *            The ontology to be stored
     * @param physicalURI
     *            The physical URI that specifies the location
     * @param ontologyFormat
     *            The format that the ontology should be stored in
     */
    public void storeOntology(OWLOntologyManager manager, OWLOntology ontology,
            URI physicalURI, OWLOntologyFormat ontologyFormat)
            throws OWLOntologyStorageException {
        try {
            OWLTutorialSyntaxRenderer renderer = new OWLTutorialSyntaxRenderer(
                    manager);

            File file = new File(physicalURI);
            OutputStream os = new FileOutputStream(file);
            renderer.render(ontology, new BufferedWriter(
                    new OutputStreamWriter(os)));
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new OWLOntologyStorageException(e);
        }
    }
}
