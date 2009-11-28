package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;
import org.eti.kask.sova.options.NodeShapeType;
import org.eti.kask.sova.options.NodeShapes;

/**
 * Klasa reprezentuje węzeł OWL Nothing.
 */
public class NothingNode extends Node
{

	/**
	 * Ustawia etykietę tego węzła jako "NT".
	 */
	public NothingNode()
	{
		super();
		label = "NT";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getNothingNodeColor();
	}

	/**
	 * @param shapeScheme źródłowy schemat kształtów dla węzłów
	 * @return rodzaj kształtu węzła z zadanego schematu
	 */
	@Override
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getInformationNodeShapeType();
	}

}

