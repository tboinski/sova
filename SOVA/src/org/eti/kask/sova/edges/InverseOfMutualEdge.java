

package org.eti.kask.sova.edges;

import java.awt.Color;
import org.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź oznaczającą wzajemną odwrotność (OWL InverseOf)
 * property. Przy wzajemnej odwrotności krawędź nie posiada grotu.
 * @author infinity
 */
public class InverseOfMutualEdge extends InverseOfEdge {

	public InverseOfMutualEdge()
	{
		super();
		hasArrow = false;
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor dla tej krawędzi ustawiony w zadanym schemacie
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getInverseOfMutualEdgeColor();
	}

}
