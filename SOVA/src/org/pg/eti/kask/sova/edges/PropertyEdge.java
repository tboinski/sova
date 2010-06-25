package org.pg.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;

import org.pg.eti.kask.sova.options.ArrowShapes;
import org.pg.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź oznaczającą relację między Property a klasą.
 */
public class PropertyEdge extends Edge
{

	public PropertyEdge()
	{
		super();
		hasArrow = true;
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getPropertyEdgeColor();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getDirectionArrow();
	}
}

