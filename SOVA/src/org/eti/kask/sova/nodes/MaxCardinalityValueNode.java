package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje wierzchołek ograniczenia kardynalności OWL MaxCardinality.
 */
public class MaxCardinalityValueNode extends CardinalityValueNode
{

	public MaxCardinalityValueNode()
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
		return colorScheme.getMaxCardinalityValueNodeColor();
	}
}

