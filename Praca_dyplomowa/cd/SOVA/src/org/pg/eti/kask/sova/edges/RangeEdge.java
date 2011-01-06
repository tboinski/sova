package org.pg.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;

import org.pg.eti.kask.sova.options.ArrowShapes;
import org.pg.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca na grafie krawędź łączącą Property z klasą właściwości
 * OWL Range.
 */
public class RangeEdge extends Edge
{

	public RangeEdge()
	{
		super();
		arrowHeadColor = Color.WHITE;
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
		return colorScheme.getRangeEdgeColor();
	}
}

