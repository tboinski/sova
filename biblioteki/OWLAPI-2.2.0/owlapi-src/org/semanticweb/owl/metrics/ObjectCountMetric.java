package org.semanticweb.owl.metrics;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
 * Date: 27-Jul-2007<br><br>
 */
public abstract class ObjectCountMetric<E extends Object> extends IntegerValuedMetric {


    public ObjectCountMetric(OWLOntologyManager owlOntologyManager) {
        super(owlOntologyManager);
    }

    protected abstract String getObjectTypeName();


    public String getName() {
        return getObjectTypeName() + " count";
    }

    protected abstract Set<? extends E> getObjects(OWLOntology ont);


    public Integer recomputeMetric() {
        return getObjects().size();
    }

    protected Set<? extends E> getObjects() {
        Set<E> objects = new HashSet<E>();
        for(OWLOntology ont : getOntologies()) {
            objects.addAll(getObjects(ont));
        }
        return objects;
    }


    protected boolean isMetricInvalidated(List<? extends OWLOntologyChange> changes) {
        return true;
    }

    protected void disposeMetric() {
    }
}
