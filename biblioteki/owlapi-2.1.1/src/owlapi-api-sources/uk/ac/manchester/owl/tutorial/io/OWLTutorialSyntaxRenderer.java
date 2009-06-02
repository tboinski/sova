package uk.ac.manchester.owl.tutorial.io;

import org.semanticweb.owl.io.AbstractOWLRenderer;
import org.semanticweb.owl.io.OWLRendererIOException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import java.io.IOException;
import java.io.Writer;

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
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 24-April-2007<br>
 * <br>
 */
public class OWLTutorialSyntaxRenderer extends AbstractOWLRenderer {

    public OWLTutorialSyntaxRenderer(OWLOntologyManager manager) {
        super(manager);
    }

    @Override
    public void render(OWLOntology ontology, Writer writer)
            throws OWLRendererIOException {
        try {
            OWLTutorialSyntaxObjectRenderer ren = new OWLTutorialSyntaxObjectRenderer(
                    ontology, writer);
            ontology.accept(ren);
            writer.flush();
        } catch (IOException ex) {
            throw new OWLRendererIOException(ex);
        }
    }
}
