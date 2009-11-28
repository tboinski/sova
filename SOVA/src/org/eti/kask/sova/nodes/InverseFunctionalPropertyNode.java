package org.eti.kask.sova.nodes;

/**
 * Klasa reprezentuje węzeł oznaczający, że dane (połączone z nim) OWL Property
 * to InverseFunctionalProperty.
 */
public class InverseFunctionalPropertyNode extends InformationNode
{
	/**
	 * Ustawia etykietę tego węzła jako "¬f".
	 */
	public InverseFunctionalPropertyNode()
	{
		super();
		label = "¬f";
	}
}

