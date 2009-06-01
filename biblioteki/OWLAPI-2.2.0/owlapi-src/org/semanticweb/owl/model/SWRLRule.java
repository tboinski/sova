package org.semanticweb.owl.model;

import java.net.URI;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 15, 2007<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Represent a rule.  A rule consists of a head and a body.
 * Both the head and the body consist of a conjunction of
 * atoms.
 */
public interface SWRLRule extends OWLLogicalAxiom, SWRLObject {

    /**
     * Determines if this rule is anonymous.  Rules may be named
     * using URIs.
     * @return <code>true</code> if this rule is anonymous and therefore
     * doesn't have a URI.
     */
    public boolean isAnonymous();

    public URI getURI();

    /**
     * Gets the atoms in the body
     * @return A set of <code>SWRLAtom</code>s, which represent the atoms
     * in the body of the rule.
     */
    Set<SWRLAtom> getBody();


    /**
     * Gets the atoms in the head.
     * @return A set of <code>SWRLAtom</code>s, which represent the atoms
     * in the head of the rule
     */
    Set<SWRLAtom> getHead();
}
