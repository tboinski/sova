package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;
import org.eti.kask.sova.options.NodeShapeType;
import org.eti.kask.sova.options.NodeShapes;

/**
 * Klasa reprezentuje w�ze� OWL Thing.
 */
public class ThingNode extends Node
{

	/**
	 * Ustawia etykiet� tego w�z�a jako "T".
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
	 * @param shapeScheme �r�d�owy schemat kszta�t�w dla w�z��w
	 * @return rodzaj kszta�tu w�z�a z zadanego schematu
	 */
	@Override
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getInformationNodeShapeType();
	}
}

