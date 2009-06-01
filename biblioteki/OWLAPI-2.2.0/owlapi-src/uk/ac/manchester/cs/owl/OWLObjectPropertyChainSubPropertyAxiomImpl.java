package uk.ac.manchester.cs.owl;

import org.semanticweb.owl.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
 * Date: 22-Nov-2006<br><br>
 */
public class OWLObjectPropertyChainSubPropertyAxiomImpl extends OWLPropertyAxiomImpl implements OWLObjectPropertyChainSubPropertyAxiom {

    private List<OWLObjectPropertyExpression> propertyChain;

    private OWLObjectPropertyExpression superProperty;


    public OWLObjectPropertyChainSubPropertyAxiomImpl(OWLDataFactory dataFactory,
                                                      List<? extends OWLObjectPropertyExpression> propertyChain,
                                                      OWLObjectPropertyExpression superProperty) {
        super(dataFactory);
        this.propertyChain = new ArrayList<OWLObjectPropertyExpression>(propertyChain);
        this.superProperty = superProperty;
    }


    public List<OWLObjectPropertyExpression> getPropertyChain() {
        return Collections.unmodifiableList(propertyChain);
    }


    public OWLObjectPropertyExpression getSuperProperty() {
        return superProperty;
    }


    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(OWLAxiomVisitor visitor) {
        visitor.visit(this);
    }

    public <O> O accept(OWLAxiomVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        OWLObjectPropertyChainSubPropertyAxiom other = (OWLObjectPropertyChainSubPropertyAxiom) obj;
        return other.getPropertyChain().equals(getPropertyChain()) && other.getSuperProperty().equals(superProperty);
    }


    public AxiomType getAxiomType() {
        return AxiomType.PROPERTY_CHAIN_SUB_PROPERTY;
    }


    protected int compareObjectOfSameType(OWLObject object) {
        OWLObjectPropertyChainSubPropertyAxiom other = (OWLObjectPropertyChainSubPropertyAxiom) object;
        int i = 0;
        while(i < propertyChain.size() && i < other.getPropertyChain().size()) {
            int diff = propertyChain.get(i).compareTo(other.getPropertyChain().get(i));
            if(diff != 0) {
                return diff;
            }
        }
        int diff = propertyChain.size() - other.getPropertyChain().size();
        if(diff != 0) {
            return diff;
        }
        return superProperty.compareTo(other.getSuperProperty());
    }
}
