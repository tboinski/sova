package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;
import org.eti.kask.sova.options.NodeShapeType;
import org.eti.kask.sova.options.NodeShapes;

/**
 * Nadklasa dla klas węzłów reprezentujących informacje o różnych
 * właściwościach OWL Property.
 */
public class InformationNode extends Node
{
	/**
	 * Ustawia etykietę tego węzła jako "I".
	 */
	public InformationNode()
	{
		super();
		label = "I";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getInformationNodeColor();
	}

	/**
	 * Klasy informacji dotyczących Property nie posiadają konwencjonalnych
	 * etykiet. Poniższa funkcja nie robi nic.
	 */
	@Override
	public void setLabel(String label)
	{
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

