package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 *  Klasa reprezentująca krawędź związku OWL SubClass pomiędzy klasami.
 */
public class SubClassEdge extends Edge
{

	public SubClassEdge()
	{
		super();
		hasInvertedArrow = true;
		arrowHeadColor = Color.WHITE;

	}

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getDirectionArrow();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor dla tej krawędzi ustawiony w zadanym schemacie
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getSubClassEdgeColor();
	}
}

