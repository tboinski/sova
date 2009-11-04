package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 *
 */
public class DifferentNode extends InformationNode
{

	public DifferentNode()
	{
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getDifferentNodeColor();
	}
	
	
}

