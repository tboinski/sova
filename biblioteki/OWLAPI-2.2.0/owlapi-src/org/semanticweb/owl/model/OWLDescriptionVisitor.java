package org.semanticweb.owl.model;
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
 * Date: 13-Nov-2006<br><br>
 */
public interface OWLDescriptionVisitor {

    void visit(OWLClass desc);

    void visit(OWLObjectIntersectionOf desc);

    void visit(OWLObjectUnionOf desc);

    void visit(OWLObjectComplementOf desc);

    void visit(OWLObjectSomeRestriction desc);

    void visit(OWLObjectAllRestriction desc);

    void visit(OWLObjectValueRestriction desc);

    void visit(OWLObjectMinCardinalityRestriction desc);

    void visit(OWLObjectExactCardinalityRestriction desc);

    void visit(OWLObjectMaxCardinalityRestriction desc);

    void visit(OWLObjectSelfRestriction desc);

    void visit(OWLObjectOneOf desc);

    void visit(OWLDataSomeRestriction desc);

    void visit(OWLDataAllRestriction desc);

    void visit(OWLDataValueRestriction desc);

    void visit(OWLDataMinCardinalityRestriction desc);

    void visit(OWLDataExactCardinalityRestriction desc);

    void visit(OWLDataMaxCardinalityRestriction desc);


}
