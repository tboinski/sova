package org.semanticweb.owl.vocab;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
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
 * Bio-Health Informatics Group
 * Date: 24-Oct-2006
 */
public enum OWLRestrictedDataRangeFacetVocabulary {

    LENGTH("length", "length"),

    MIN_LENGTH("minLength", "minLength"),

    MAX_LENGTH("maxLength", "maxLength"),

    PATTERN("pattern", "pattern"),

    MIN_INCLUSIVE("minInclusive", ">="),

    MIN_EXCLUSIVE("minExclusive", ">"),

    MAX_INCLUSIVE("maxInclusive", "<="),

    MAX_EXCLUSIVE("maxExclusive", "<"),

    TOTAL_DIGITS("totalDigits", "totalDigits"),

    FRACTION_DIGITS("fractionDigits", "fractionDigits");


    public final static Set<URI> FACET_URIS;


    static {
        Set<URI> uris = new HashSet<URI>();
        for (OWLRestrictedDataRangeFacetVocabulary v : values()) {
            uris.add(v.getURI());
        }
        FACET_URIS = Collections.unmodifiableSet(uris);
    }


    private URI uri;

    private String shortName;

    private String symbolicForm;


    OWLRestrictedDataRangeFacetVocabulary(String shortName, String symbolicForm) {
        this.uri = URI.create(Namespaces.OWL2 + shortName);
        this.shortName = shortName;
        this.symbolicForm = symbolicForm;
    }


    public URI getURI() {
        return uri;
    }


    public String getShortName() {
        return shortName;
    }


    public String getSymbolicForm() {
        return symbolicForm;
    }


    public String toString() {
        return shortName;
    }


    public static Set<URI> getFacetURIs() {
        return FACET_URIS;
    }


    public static OWLRestrictedDataRangeFacetVocabulary getFacet(URI uri) {
        for (OWLRestrictedDataRangeFacetVocabulary vocabulary : OWLRestrictedDataRangeFacetVocabulary.values()) {
            if (vocabulary.getURI().equals(uri)) {
                return vocabulary;
            }
        }
        return null;
    }


    /**
     * Gets a facet by its short name
     * @param shortName The short name of the facet.
     * @return The facet or <code>null</code> if not facet by the specified name exists.
     */
    public static OWLRestrictedDataRangeFacetVocabulary getFacetByShortName(String shortName) {
        for (OWLRestrictedDataRangeFacetVocabulary vocabulary : OWLRestrictedDataRangeFacetVocabulary.values()) {
            if (vocabulary.getShortName().equals(shortName)) {
                return vocabulary;
            }
        }
        return null;
    }

    public static OWLRestrictedDataRangeFacetVocabulary getFacetBySymbolicName(String symbolicName) {
        for (OWLRestrictedDataRangeFacetVocabulary vocabulary : OWLRestrictedDataRangeFacetVocabulary.values()) {
            if (vocabulary.getSymbolicForm().equals(symbolicName)) {
                return vocabulary;
            }
        }
        return null;
    }

    public static Set<String> getFacets() {
        Set<String> result = new HashSet<String>();
        for(OWLRestrictedDataRangeFacetVocabulary v : values()) {
            result.add(v.getSymbolicForm());
        }
        return result;
    }
}
