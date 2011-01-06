package org.pg.eti.kask.sova.edges;

import java.awt.Color;

import org.pg.eti.kask.sova.options.EdgeColors;
/**
 *  Klasa reprezentująca krawędź InstanceOf, łączy individual z klasą, której jest instancją.
 * @author Piotr Kunowski
 */
public class InstanceOfEdge extends Edge {
	
	public InstanceOfEdge() {
		super();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getInstanceOfColor();
	}
}
