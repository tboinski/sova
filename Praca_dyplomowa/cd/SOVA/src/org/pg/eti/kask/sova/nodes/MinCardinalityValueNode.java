package org.pg.eti.kask.sova.nodes;

import java.awt.Color;

import org.pg.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje wierzchołek ograniczenia kardynalności OWL MinCardinality.
 */
public class MinCardinalityValueNode extends CardinalityValueNode
{

	public MinCardinalityValueNode()
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
		return colorScheme.getMinCardinalityValueNodeColor();
	}
}