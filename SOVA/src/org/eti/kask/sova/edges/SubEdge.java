package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;
import org.eti.kask.sova.options.ArrowShapes;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.8F1FE813-4E1D-D395-E5D8-7F8461CB3BD1]
// </editor-fold> 
public class SubEdge extends Edge {

   /** 
    *
    */ 
    public SubEdge () {
	    
	    setHasInvertedArrow(true);	
	    setArrowHeadColor(Color.WHITE);

    }

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getDirectionArrow();
	}




}

