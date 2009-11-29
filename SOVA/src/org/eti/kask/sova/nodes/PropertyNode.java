package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje węzeł Property, do którego są dołączane związki
 * określające jego zakres (Domain oraz Range) oraz cechy (np. transitive).
 */
public class PropertyNode extends Node
{

	public PropertyNode()
	{
		super();
		//setStrokeWitdh(2f);
		//setRounded(true);
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getPropertyNodeColor();
	}
}

