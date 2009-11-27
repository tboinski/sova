package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 *
 */
public class DataTypeNode extends Node
{

	public DataTypeNode()
	{
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getDataTypeNodeColor();
	}
}

