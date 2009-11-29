package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentuj¹ca krawêdŸ oznaczaj¹c¹ odwrotnoœæ (OWL InverseOf).
 */
public class InverseOfEdge extends Edge
{

	public InverseOfEdge()
	{
		super();
		hasArrow = true;
	}

	/**
	 * @param colorScheme Ÿród³owy schemat kolorów
	 * @return kolor krawêdzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getInverseOfEdgeColor();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getDirectionArrow();
	}

}

