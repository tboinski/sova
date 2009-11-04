package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Reprezentuje węzły instancji klas (OWL Individual).
 */
public class IndividualNode extends Node
{

	public IndividualNode()
	{
		setFillColor(Color.GRAY);
		setRounded(false);
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getIndividualNodeColor();
	}
}

