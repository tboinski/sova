package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.62915D8C-1304-C852-C116-E41695A34198]
// </editor-fold> 
public class ThingNode extends Node {

   /** 
    *
    */ 
    public ThingNode () {
	    super();
	    setLabel("TestowanieBardzoDlugiejEtykietyWezla");
	    setFillColor(Color.GREEN);
    }

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getThingNodeColor();
	}




}

