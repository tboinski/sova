package org.pg.eti.kask.sova.nodes;

/**
 * Klasa reprezentuje węzeł klasy anonimowej powstałej w wyniku nałożenia
 * ograniczenia Cardinality.
 */
public class CardinalityNode extends AnonymousClassNode
{

	/**
	 * Ustawia etykietę tego węzła jako "N".
	 */
	public CardinalityNode()
	{
		super();
		label = "N";
	}

	/**
	 * Klasy anonimowe nie posiadają konwencjonalnych etykiet.
	 * Poniższa funkcja nie robi nic.
	 */
	@Override
	public void setLabel(String label)
	{
	}

}

