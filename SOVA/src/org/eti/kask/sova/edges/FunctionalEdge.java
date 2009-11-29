package org.eti.kask.sova.edges;

import java.awt.Color;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź łączącą wierzchołki InformationNode
 * z OWL Property, którego dotyczy.
 */
public class FunctionalEdge extends Edge
{

	public FunctionalEdge()
	{
		super();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getFunctionalEdgeColor();
	}
}

