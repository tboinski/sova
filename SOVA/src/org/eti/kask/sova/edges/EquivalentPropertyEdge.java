package org.eti.kask.sova.edges;

import java.awt.Color;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź oznaczającą równoznaczność OWL Property
 * (OWL EquivalentProperty).
 */
public class EquivalentPropertyEdge extends EquivalentEdge
{

	public EquivalentPropertyEdge()
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
		return colorScheme.getEquivalentPropertyEdgeColor();
	}



}

