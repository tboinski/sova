package org.pg.eti.kask.sova.nodes;

import java.awt.Color;

import org.pg.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje węzeł oznaczający relację DifferentFrom
 * lub AllDifferent pomiędzy wystąpieniami klas (OWL Individual).
 */
public class DifferentNode extends InformationNode
{

	/**
	 * Ustawia eykietę tego węzła na "≠".
	 */
	public DifferentNode()
	{
		super();
		label = "≠";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getDifferentNodeColor();
	}
	
	
}

