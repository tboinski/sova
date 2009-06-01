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
 * Date: 16-Apr-2008<br><br>
 */
public interface OWLDescriptionVisitorEx<O> {


    O visit(OWLClass desc);


    O visit(OWLObjectIntersectionOf desc);


    O visit(OWLObjectUnionOf desc);


    O visit(OWLObjectComplementOf desc);


    O visit(OWLObjectSomeRestriction desc);


    O visit(OWLObjectAllRestriction desc);


    O visit(OWLObjectValueRestriction desc);


    O visit(OWLObjectMinCardinalityRestriction desc);


    O visit(OWLObjectExactCardinalityRestriction desc);


    O visit(OWLObjectMaxCardinalityRestriction desc);


    O visit(OWLObjectSelfRestriction desc);


    O visit(OWLObjectOneOf desc);


    O visit(OWLDataSomeRestriction desc);


    O visit(OWLDataAllRestriction desc);


    O visit(OWLDataValueRestriction desc);


    O visit(OWLDataMinCardinalityRestriction desc);


    O visit(OWLDataExactCardinalityRestriction desc);


    O visit(OWLDataMaxCardinalityRestriction desc);
}
