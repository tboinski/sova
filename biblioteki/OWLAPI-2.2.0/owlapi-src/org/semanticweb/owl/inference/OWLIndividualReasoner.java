package org.semanticweb.owl.inference;

import org.semanticweb.owl.model.*;

import java.util.Map;
import java.util.Set;
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
 * Date: 25-Oct-2006<br><br>
 */
public interface OWLIndividualReasoner extends OWLReasonerBase {


    Set<Set<OWLClass>> getTypes(OWLIndividual individual, boolean direct) throws OWLReasonerException;


    Set<OWLIndividual> getIndividuals(OWLDescription clsC, boolean direct) throws OWLReasonerException;


    Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual individual) throws OWLReasonerException;


    Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual individual) throws OWLReasonerException;


    /*
     * I have modified the signature of this method.  The OWLDescription parameter was
     * changed to an OWLClass parameter.
     *
     */
    boolean hasType(OWLIndividual individual, OWLDescription type, boolean direct) throws OWLReasonerException;


    boolean hasObjectPropertyRelationship(OWLIndividual subject, OWLObjectPropertyExpression property, OWLIndividual object) throws OWLReasonerException;


    boolean hasDataPropertyRelationship(OWLIndividual subject, OWLDataPropertyExpression property, OWLConstant object) throws OWLReasonerException;


    Set<OWLIndividual> getRelatedIndividuals(OWLIndividual subject, OWLObjectPropertyExpression property) throws OWLReasonerException;


    Set<OWLConstant> getRelatedValues(OWLIndividual subject, OWLDataPropertyExpression property) throws OWLReasonerException;


}
