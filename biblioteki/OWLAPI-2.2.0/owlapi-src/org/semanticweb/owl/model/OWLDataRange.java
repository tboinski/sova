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
 * Bio-Health Informatics Group
 * Date: 24-Oct-2006
 *
 * A high level interface which represents a data range.  Example of
 * data ranges are datatypes (e.g. int, float, double, string, ...),
 * complements of data ranges (e.g. not(int)), data enumerations (data oneOfs),
 * datatype restrictions (e.g. int > 3).
 */
public interface OWLDataRange extends OWLObject, OWLPropertyRange {

    /**
     * Determines if this data range is a datatype (int, float, ...)
     * @return <code>true</code> if this datarange is a datatype, or
     * <code>false</code> if it is not a datatype and is some other
     * data range such as a data range restriction, data oneOf or
     * data complementOf.
     */
    boolean isDataType();

    void accept(OWLDataVisitor visitor);

    <O> O accept(OWLDataVisitorEx<O> visitor);
}
