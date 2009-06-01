package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLNaryBooleanDescription;
import org.semanticweb.owl.model.OWLObject;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
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
 * Date: 26-Oct-2006<br><br>
 */
public abstract class OWLNaryBooleanDescriptionImpl extends OWLAnonymousDescriptionImpl implements OWLNaryBooleanDescription {

    private Set<OWLDescription> operands;


    public OWLNaryBooleanDescriptionImpl(OWLDataFactory dataFactory, Set<? extends OWLDescription> operands) {
        super(dataFactory);
        this.operands = new TreeSet<OWLDescription>(operands);
    }


    public Set<OWLDescription> getOperands() {
        return Collections.unmodifiableSet(operands);
    }


    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof OWLNaryBooleanDescription)) {
                return false;
            }
            return ((OWLNaryBooleanDescription) obj).getOperands().equals(operands);
        }
        return false;
    }


    final protected int compareObjectOfSameType(OWLObject object) {
        return compareSets(operands, ((OWLNaryBooleanDescription) object).getOperands());
    }
}
