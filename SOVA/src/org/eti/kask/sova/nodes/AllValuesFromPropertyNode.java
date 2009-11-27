package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje węzeł Property typu AllValuesFrom.
 */
public class AllValuesFromPropertyNode extends PropertyNode
{

	public AllValuesFromPropertyNode()
	{
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getAllValuesFromNodeColor();
	}
}

