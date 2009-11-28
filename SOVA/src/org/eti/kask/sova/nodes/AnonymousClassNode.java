package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje węzeł klasy anonimowej.
 */
public class AnonymousClassNode extends Node
{

	public AnonymousClassNode()
	{
		super();
		label = "A";
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getAnonymousClassNodeColor();
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

