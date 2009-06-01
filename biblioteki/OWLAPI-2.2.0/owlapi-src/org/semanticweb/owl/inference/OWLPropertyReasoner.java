package org.semanticweb.owl.inference;

import org.semanticweb.owl.model.*;

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
public interface OWLPropertyReasoner extends OWLReasonerBase {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Object properties
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty property) throws OWLReasonerException;


    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty property) throws OWLReasonerException;


    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty property) throws OWLReasonerException;


    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty property) throws OWLReasonerException;


    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty property) throws OWLReasonerException;


    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty property) throws OWLReasonerException;


    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty property) throws OWLReasonerException;


    public Set<OWLDescription> getRanges(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isFunctional(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isInverseFunctional(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isSymmetric(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isTransitive(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isReflexive(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isIrreflexive(OWLObjectProperty property) throws OWLReasonerException;


    public boolean isAntiSymmetric(OWLObjectProperty property) throws OWLReasonerException;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Data properties
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty property) throws OWLReasonerException;


    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty property) throws OWLReasonerException;


    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty property) throws OWLReasonerException;


    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty property) throws OWLReasonerException;


    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty property) throws OWLReasonerException;


    public Set<Set<OWLDescription>> getDomains(OWLDataProperty property) throws OWLReasonerException;


    public Set<OWLDataRange> getRanges(OWLDataProperty property) throws OWLReasonerException;


    public boolean isFunctional(OWLDataProperty property) throws OWLReasonerException;
}
