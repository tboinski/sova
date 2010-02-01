package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;
import org.eti.kask.sova.options.NodeShapeType;
import org.eti.kask.sova.options.NodeShapes;

/**
 * Klasa reprezentuje węzeł OWL Thing.
 */
public class ThingNode extends Node
{

	/**
	 * Ustawia etykietę tego węzła jako "T".
	 */
	public ThingNode()
	{
		super();
		label = "T";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getThingNodeColor();
	}

	/**
	 * @param shapeScheme źródłowy schemat kształtów dla węzłów
	 * @return rodzaj kształtu węzła z zadanego schematu
	 */
	@Override
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getThingNodeShapeType();
	}
}

