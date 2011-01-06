package org.pg.eti.kask.sova.nodes;

/**
 * Klasa reprezentuje węzeł klasy anonimowej OWL będącej wynikiem
 * dopełnienia (OWL ComplementOf).
 */
public class ComplementOfNode extends AnonymousClassNode
{
	/**
	 * Ustawia etykietę tego węzła jako "¬".
	 */
	public ComplementOfNode()
	{
		super();
		label = "¬";
	}
}

