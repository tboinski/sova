package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;
import org.eti.kask.sova.options.NodeShapeType;
import org.eti.kask.sova.options.NodeShapes;

/**
 * Klasa reprezentuje wêze³ OWL Thing.
 */
public class ThingNode extends Node
{

	/**
	 * Ustawia etykietê tego wêz³a jako "T".
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
	 * @param shapeScheme Ÿród³owy schemat kszta³tów dla wêz³ów
	 * @return rodzaj kszta³tu wêz³a z zadanego schematu
	 */
	@Override
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getInformationNodeShapeType();
	}
}

