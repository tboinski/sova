package org.eti.kask.sova.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import org.eti.kask.sova.options.NodeColors;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.DE075DE3-6E98-CA10-DC35-D36371893BF6]
// </editor-fold> 
public class FunctionalPropertyNode extends InformationNode {

   /** 
    *
    */ 
    public FunctionalPropertyNode () {
    }

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getFunctionalPropertyNodeColor();
	}




}

