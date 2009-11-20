package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Krawędź łącząca klasy rozłączne.
 */
public class DisjointEdge extends Edge
{

	/**
	 *
	 */
	public DisjointEdge()
	{

		setHasArrow(true);
		setHasInvertedArrow(true);
		setArrowHeadColor(Color.BLACK);
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
}

