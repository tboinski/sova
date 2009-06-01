package org.semanticweb.owl.util;

import org.semanticweb.owl.model.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
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
 * Date: 17-Jun-2007<br><br>
 *
 * A short form provider that generates short forms from the values of object
 * property assertions or data property assertions if the entity is an individual.
 * If the entity whose short form is not being generated is not an individual
 * (i.e. it is a class, property etc.) then an alternate short form provider is
 * used.  (As a side note, the use case for this particular short form provider
 * came from the SKOS community, which have individuals that have preferredLabel
 * property assertions).
 */
public class PropertyAssertionValueShortFormProvider implements ShortFormProvider {

    private List<OWLPropertyExpression> properties;

    private Map<OWLDataPropertyExpression, List<String>> preferredLanguageMap;

    private OWLOntologySetProvider ontologySetProvider;

    private ShortFormProvider alternateShortFormProvider;


    /**
     * Constructs a property value short form provider. Using <code>SimpleShortFormProvider</code> as the
     * alternate short form provider (see other constructor for details).
     */
    public PropertyAssertionValueShortFormProvider(List<OWLPropertyExpression> properties, Map<OWLDataPropertyExpression, List<String>> preferredLanguageMap,
                                                   OWLOntologySetProvider ontologySetProvider) {
        this(properties, preferredLanguageMap, ontologySetProvider, new SimpleShortFormProvider());
    }


    /**
     * Constructs a property value short form provider.
     * @param properties             A <code>List</code> of preferred properties.  The list is searched from
     *                                   start to end, so that property assertions whose property is at the start of the list have a higher
     *                                   priority and are selected over properties that appear towards or at the end of the list.
     * @param preferredLanguageMap       A map which maps data properties to preferred languages.  For any given
     *                                   data property there may be a list of preferred languages for the values of that property
     *                                   Languages at the start of the list
     *                                   have a higher priority over languages at the end of the list.  This parameter may be empty but it
     *                                   must not be <code>null</code>.
     * @param ontologySetProvider        An <code>OWLOntologySetProvider</code> which provides a set of ontology
     *                                   from which candidate annotation axioms should be taken.  For a given entity, all ontologies are
     *                                   examined.
     * @param alternateShortFormProvider A short form provider which will be used to generate the short form
     *                                   for an entity that does not have any property values (e.g. class, property).  This provider will also be used in the case where
     *                                   the value of an annotation is an <code>OWLIndividual</code> for providing the short form of the individual.
     */
    public PropertyAssertionValueShortFormProvider(List<OWLPropertyExpression> properties, Map<OWLDataPropertyExpression, List<String>> preferredLanguageMap,
                                                   OWLOntologySetProvider ontologySetProvider,
                                                   ShortFormProvider alternateShortFormProvider) {
        this.properties = properties;
        this.preferredLanguageMap = preferredLanguageMap;
        this.ontologySetProvider = ontologySetProvider;
        this.alternateShortFormProvider = alternateShortFormProvider;
    }


    public String getShortForm(OWLEntity entity) {
        int lastURIMatchIndex = Integer.MAX_VALUE;
        int lastLangMatchIndex = Integer.MAX_VALUE;
        if(!(entity instanceof OWLIndividual)) {
            return alternateShortFormProvider.getShortForm(entity);
        }
        OWLIndividual individual = (OWLIndividual) entity;
        // The candidate value to be rendered, we select this based on
        // ranking of annotation URI and ranking of lang (if present)
        OWLObject candidateValue = null;
        for (OWLOntology ontology : ontologySetProvider.getOntologies()) {

            for(OWLObjectPropertyAssertionAxiom ax : ontology.getObjectPropertyAssertionAxioms(individual)) {
                int index = properties.indexOf(ax.getProperty());
                if(index == -1) {
                    continue;
                }
                if(index < lastURIMatchIndex) {
                    candidateValue = ax.getObject();
                }
            }
            for(OWLDataPropertyAssertionAxiom ax : ontology.getDataPropertyAssertionAxioms(individual)) {
                int index = properties.indexOf(ax.getProperty());
                if(index == -1) {
                    continue;
                }
                if (index == lastURIMatchIndex) {
                    // Different property value but same prop, as previous candidate - look at lang tag for that URI
                    // and see if we take priority over the previous one
                    OWLObject obj = ax.getObject();
                    if (obj instanceof OWLUntypedConstant) {
                        OWLUntypedConstant untypedConstantVal = (OWLUntypedConstant) obj;
                        if (untypedConstantVal.hasLang()) {
                            List<String> langList = preferredLanguageMap.get(ax.getProperty());
                            if (langList != null) {
                                // There is no need to check if lang is null.  It may well be that no
                                // lang is preferred over any other lang.
                                int langIndex = langList.indexOf(untypedConstantVal.getLang());
                                if (langIndex != -1 && langIndex < lastLangMatchIndex) {
                                    lastLangMatchIndex = langIndex;
                                    candidateValue = ax.getObject();
                                }
                            }
                        }
                    }
                }
                else if (index < lastURIMatchIndex) {
                    // Better match than previous URI - wipe out previous match!
                    lastURIMatchIndex = index;
                    candidateValue = ax.getObject();
                }
            }


        }

        if (candidateValue != null) {
            return getRendering(candidateValue);
        }
        else {
            return alternateShortFormProvider.getShortForm(entity);
        }
    }


    /**
     * Obtains the rendering of the specified object.  If the object
     * is a constant then the rendering is equal to the literal value,
     * if the object is an individual then the rendering is equal to
     * the rendering of the individual as provided by the alternate
     * short form provider
     * @param object The object to the rendered
     * @return The rendering of the object.
     */
    private String getRendering(OWLObject object) {
        // We return the literal value of constants or use the alternate
        // short form provider to render individuals.
        if (object instanceof OWLConstant) {
            return ((OWLConstant) object).getLiteral();
        }
        else {
            return alternateShortFormProvider.getShortForm((OWLEntity) object);
        }
    }


    public List<OWLPropertyExpression> getProperties() {
        return properties;
    }


    public Map<OWLDataPropertyExpression, List<String>> getPreferredLanguageMap() {
        return preferredLanguageMap;
    }


    public void dispose() {
    }
}
