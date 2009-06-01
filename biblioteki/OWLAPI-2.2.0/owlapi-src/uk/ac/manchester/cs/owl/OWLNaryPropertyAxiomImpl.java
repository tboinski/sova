package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLNaryPropertyAxiom;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLPropertyExpression;

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
public abstract class OWLNaryPropertyAxiomImpl<P extends OWLPropertyExpression> extends OWLPropertyAxiomImpl implements OWLNaryPropertyAxiom<P> {

    private Set<P> properties;


    public OWLNaryPropertyAxiomImpl(OWLDataFactory dataFactory, Set<? extends P> properties) {
        super(dataFactory);
        this.properties = new TreeSet<P>(properties);
    }


    public Set<P> getProperties() {
        return Collections.unmodifiableSet(properties);
    }


    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof OWLNaryPropertyAxiom)) {
                return false;
            }
            return ((OWLNaryPropertyAxiom) obj).getProperties().equals(properties);
        }
        return false;
    }


    final protected int compareObjectOfSameType(OWLObject object) {
        return compareSets(properties, ((OWLNaryPropertyAxiom) object).getProperties());
    }
}
