package org.coode.obo.parser;

import java.net.URI;
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
 * Date: 10-Jan-2007<br><br>
 */
public enum OBOVocabulary {

    FORMAT_VERSION("format-version"),
    TYPE_REF("typeref"),
    ID("id"),
    NAME("name"),
    ALT_ID("alt_name"),
    NAMESPACE("namespace"),
    DEFAULT_NAMESPACE("default_namespace"),
    DEF("def"),
    COMMENT("comment"),
    SUBSET("subset"),
    SYNONYM("subset"),
    RELATED_SYNONYM("related_synonym"),
    EXACT_SYNONYM("exact_synonym"),
    BROAD_SYNONYM("broad_synonym"),
    NARROW_SYNONYM("narrow_synonym"),
    XREF_ANALOGUE("xref_analogue"),
    XREF_UNKNOWN("xref_unknown"),
    IS_A("is_a"),
    PART_OF("part_of"),
    RELATIONSHIP("relationship"),
    IS_OBSOLETE("is_obsolete"),
    USE_TERM("use_term"),
    DOMAIN("domain"),
    RANGE("range"),
    IS_CYCLIC("is_cyclic"),
    IS_TRANSITIVE("is_transitive"),
    IS_SYMMETRIC("is_symmetric"),
    INTERSECTION_OF("intersection_of"),
    UNION_OF("union_of"),
    DISJOINT_FROM("disjoint_from"),
    TERM("Term")
    ;

    public static final String BASE = "http://www.geneontology.org/go#";



    OBOVocabulary(String name) {
        this.name = name;
        uri = URI.create(BASE + name);
    }

    private String name;

    private URI uri;


    public String getName() {
        return name;
    }


    public URI getURI() {
        return uri;
    }

    public String toString() {
        return name;
    }
}
