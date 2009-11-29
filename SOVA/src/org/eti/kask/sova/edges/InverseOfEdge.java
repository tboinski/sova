package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentuj�ca kraw�d� oznaczaj�c� odwrotno�� (OWL InverseOf).
 */
public class InverseOfEdge extends Edge
{

	public InverseOfEdge()
	{
		super();
		hasArrow = true;
	}

	/**
	 * @param colorScheme �r�d�owy schemat kolor�w
	 * @return kolor kraw�dzi wraz z grotami z zadanego schematu
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

