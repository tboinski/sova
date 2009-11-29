package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentuj�ca kraw�d� oznaczaj�c� r�wnoznaczno�� (OWL Equivalent).
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
	 * @param colorScheme �r�d�owy schemat kolor�w
	 * @return kolor kraw�dzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getEquivalentEdgeColor();
	}
 

}

