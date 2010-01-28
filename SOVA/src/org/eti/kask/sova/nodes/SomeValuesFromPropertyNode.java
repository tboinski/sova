package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje węzeł Property typu SomeValuesFrom.
 */
public class SomeValuesFromPropertyNode extends PropertyNode
{

	public SomeValuesFromPropertyNode()
	{
		super();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getSomeValuesFromNodeColor();
	}

	/**
	 * Przed właściwą etykietą tego węzła dołączany jest znak "∃:".
	 * @param label etykieta węzła
	 */
	@Override
	public void setLabel(String label)
	{
		this.label = "∃:" + label;
	}

}

