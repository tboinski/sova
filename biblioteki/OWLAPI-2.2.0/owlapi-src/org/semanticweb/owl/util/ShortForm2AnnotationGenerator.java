package org.semanticweb.owl.util;

import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
 * Date: 15-Feb-2008<br><br>
 *
 */
public class ShortForm2AnnotationGenerator implements OWLCompositeOntologyChange {

    // The annotation URI to be used.
    private URI annotationURI;

    // An optional language tag to be used - could be null;
    private String languageTag;

    private OWLOntologyManager ontologyManager;

    private ShortFormProvider shortFormProvider;

    private OWLEntitySetProvider<? extends OWLEntity> entitySetProvider;

    private OWLOntology ontology;


    public ShortForm2AnnotationGenerator(OWLOntologyManager ontologyManager, OWLOntology ontology, OWLEntitySetProvider<? extends OWLEntity> entitySetProvider,
                                         ShortFormProvider shortFormProvider, URI annotationURI, String languageTag) {
        this.ontologyManager = ontologyManager;
        this.shortFormProvider = shortFormProvider;
        this.entitySetProvider = entitySetProvider;
        this.ontology = ontology;
        this.annotationURI = annotationURI;
        this.languageTag = languageTag;
    }


    public ShortForm2AnnotationGenerator(OWLOntologyManager ontologyManager, OWLOntology ontology, OWLEntitySetProvider<? extends OWLEntity> entitySetProvider,
                                         ShortFormProvider shortFormProvider, URI annotationURI) {
        this(ontologyManager, ontology, entitySetProvider, shortFormProvider, annotationURI, null);
    }


    public List<OWLOntologyChange> getChanges() {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLEntity ent : entitySetProvider.getEntities()) {
            String shortForm = shortFormProvider.getShortForm(ent);
            OWLConstant con;
            if (languageTag != null) {
                con = ontologyManager.getOWLDataFactory().getOWLUntypedConstant(shortForm, languageTag);
            }
            else {
                con = ontologyManager.getOWLDataFactory().getOWLUntypedConstant(shortForm);
            }
            if (ontology.containsEntityReference(ent)) {
                OWLOntologyChange chg = new AddAxiom(ontology,
                                                     ontologyManager.getOWLDataFactory().getOWLEntityAnnotationAxiom(ent,
                                                                                                                     annotationURI,
                                                                                                                     con));
                changes.add(chg);
            }
        }
        return changes;
    }
}
