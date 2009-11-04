package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 *
 */
public class InformationNode extends Node
{

	public InformationNode()
	{
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getInformationNodeColor();
	}
}

