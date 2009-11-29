package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź oznaczającą rozłączność klas (OWL Disjoint).
 */
public class DisjointEdge extends Edge
{

	public DisjointEdge()
	{
		super();
		hasArrow = true;
		hasInvertedArrow = true;
	}

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getInversedDirectionArrow();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getInversedDirectionArrow();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getDisjointEdgeColor();
	}
}

