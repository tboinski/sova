package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.D091903B-11F9-0355-2479-7CF2D14C4B66]
// </editor-fold> 
public class SameAsNode extends InformationNode
{

	/**
	 *
	 */
	public SameAsNode()
	{
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getSameAsNodeColor();
	}
}

