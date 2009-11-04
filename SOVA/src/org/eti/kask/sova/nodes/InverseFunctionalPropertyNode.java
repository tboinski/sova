package org.eti.kask.sova.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import org.eti.kask.sova.options.NodeColors;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.6149BB74-6C71-3E73-7017-7590D5F56588]
// </editor-fold> 
public class InverseFunctionalPropertyNode extends InformationNode
{

	/**
	 *
	 */
	public InverseFunctionalPropertyNode()
	{
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getInverseFunctionalPropertyColor();
	}
}

