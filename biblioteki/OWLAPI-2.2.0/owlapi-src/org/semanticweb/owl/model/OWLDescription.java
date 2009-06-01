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
 * Represents a class description in OWL.  This interface
 * covers named and anonymous classes.
 */
public interface OWLDescription extends OWLObject, OWLPropertyRange {

    /**
     * Determines whether or not this description represents
     * an anonymous class description.
     * @return <code>true</code> if this is an anonymous class description,
     * or <code>false</code> if this is a named class (<code>OWLClass</code>)
     */
    public boolean isAnonymous();


    /**
     * If this class description is in fact a named class then this
     * method may be used to obtain the description as an <code>OWLClass</code>
     * without the need for casting.  The general pattern of use is to use
     * the <code>isAnonymous</code> to first check 
     * @return This class description as an <code>OWLClass</code>.
     * @throws OWLRuntimeException if this class description is not an <code>OWLClass</code>.
     */
    public OWLClass asOWLClass();


    /**
     * Determines if this description is the built in class owl:Thing.
     * This method does not determine if the class is equivalent to owl:Thing.
     * @return <code>true</code> if this description is owl:Thing,
     * or <code>false</code> if this description is not owl:Thing
     */
    public boolean isOWLThing();


    /**
     * Determines if this description is the built in class owl:Nothing.
     * This method does not determine if the class is equivalent to owl:Nothing.
     * @return <code>true</code> if this description is owl:Nothing,
     * or <code>false</code> if this description is not owl:Nothing.
     */
    public boolean isOWLNothing();


    /**
     * Accepts a visit from an <code>OWLDescriptionVisitor</code>
     * @param visitor The visitor that wants to visit
     */
    public void accept(OWLDescriptionVisitor visitor);

    <O> O accept(OWLDescriptionVisitorEx<O> visitor);
}
