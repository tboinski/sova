package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Krawędź do oznaczania powiązań operacji, w wyniku których powstają
 * klasy anonimowe.
 * @author infinity
 */
public class OperationEdge extends Edge
{

	public OperationEdge()
	{
		super();
		hasArrow = true;
		arrowHeadColor = Color.WHITE;
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getCircle();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor dla tej krawędzi ustawiony w zadanym schemacie
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getOperationEdgeColor();
	}
}
