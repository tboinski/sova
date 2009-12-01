package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;

/**
 *  Klasa reprezentująca krawędź związku OWL SubClass pomiędzy klasami.
 */
public class SubEdge extends Edge
{

	public SubEdge()
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
}

