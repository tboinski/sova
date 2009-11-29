package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentuj¹ca krawêdŸ oznaczaj¹c¹ równoznacznoœæ (OWL Equivalent).
 */
public class EquivalentEdge extends Edge
{

	public EquivalentEdge()
	{
		super();
		hasArrow = true;
		hasInvertedArrow = true;
	}

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getDirectionArrow();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getDirectionArrow();
	}

	/**
	 * @param colorScheme Ÿród³owy schemat kolorów
	 * @return kolor krawêdzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getEquivalentEdgeColor();
	}
 

}

