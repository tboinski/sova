package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź łączącą Property z klasą właściwości
 * (OWL DomainOf).
 */
public class DomainEdge extends Edge
{

	public DomainEdge()
	{
		super();
		hasArrow = true;
		hasInvertedArrow = true;
	}

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getDiamond();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getDiamond();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getDomainEdgeColor();
	}
}

