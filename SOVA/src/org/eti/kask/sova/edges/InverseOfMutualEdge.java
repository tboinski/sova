

package org.eti.kask.sova.edges;

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

}
