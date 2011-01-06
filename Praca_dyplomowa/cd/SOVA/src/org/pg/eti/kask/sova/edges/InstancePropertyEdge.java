package org.pg.eti.kask.sova.edges;

import java.awt.Color;

import org.pg.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź łączącą wystąpienie property (sameValueFrom i
 * allValueFrom) z jego definicją.
 * 
 * @author Piotr Kunowski
 */
public class InstancePropertyEdge extends Edge{
	public InstancePropertyEdge() {
		super();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getInstancePropertyColor();
	}
}
