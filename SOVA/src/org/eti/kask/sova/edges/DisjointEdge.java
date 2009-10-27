package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;

/**
 * Krawędź, łącząca klasy rozłączne.
 */
public class DisjointEdge extends Edge {

   /** 
    *
    */ 
    public DisjointEdge () {

	    setHasArrow(true);
	    setHasInvertedArrow(true);
	    int[] xpoints = {-4, 0, 4, 0};
	    int[] ypoints = {0, -12, 0, -2};
	    arrowHead = new Polygon(xpoints, ypoints, 4);
	    setArrowHeadColor(Color.BLACK);
    }


}

