package org.pg.eti.kask.sova.nodes;

import java.awt.Color;

import org.pg.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje wierzchołek oznaczający relację OWL SameAs
 * pomiędzy wystąpieniami klas (OWL Individual).
 */
public class SameAsNode extends InformationNode
{

	/**
	 * Ustawia etykietę tego węzła na "=".
	 */
	public SameAsNode()
	{
		super();
		label = "=";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getSameAsNodeColor();
	}
}

