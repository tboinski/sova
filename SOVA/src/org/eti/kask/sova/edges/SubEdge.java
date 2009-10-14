package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import prefuse.util.ColorLib;
import prefuse.util.GraphicsLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.8F1FE813-4E1D-D395-E5D8-7F8461CB3BD1]
// </editor-fold> 
public class SubEdge extends Edge {

   /** 
    *
    */ 
    public SubEdge () {
	    
	    setHasInvertedArrow(true);
	    int[] xpoints = {0, -4, 4, 0};
	    int[] ypoints = {0, -12, -12, 0};
	    arrowHead = new Polygon(xpoints, ypoints, 4);
	    setArrowHeadColor(Color.WHITE);

    }



	/*@Override
	public void renderShape(Graphics2D g,  VisualItem item, Point2D start, Point2D end)
	{

		item.setStrokeColor(ColorLib.color(Color.GREEN));

		EdgeItem ei = (EdgeItem) item;

		VisualItem targetItem = (VisualItem) ei.getTargetItem();
			Rectangle2D r2 = targetItem.getBounds();
			Point2D[] tmpPoints = new Point2D[2];

			int i2 = GraphicsLib.intersectLineRectangle(start, end, r2,
					tmpPoints);
			if (i2 > 0)
				end = tmpPoints[0];
			AffineTransform at2 = getArrowTrans(start, end, getStrokeWidth());
			Shape arrowHead2 = at2.createTransformedShape(m_arrowHead);

			g.fill(arrowHead2);


		//super.renderShape(g);
		//Debug.sendMessage("SubEdgeRender"); - działa

		//czesc kodu do przeniesienia do klasy Edge
		//g.setPaint(strokeColor);

		/*Rectangle2D sourceNodeBounds = sourceNode.getBounds();
		Point2D[] linePoints = new Point2D[2];
		Point2D[] linePoints2 = new Point2D[2];
		GraphicsLib.intersectLineRectangle(start, end, sourceNodeBounds, linePoints);

		Rectangle2D targetNodeBounds = targetNode.getBounds();
		int i2 = GraphicsLib.intersectLineRectangle(start, end, targetNodeBounds,
					linePoints2);
		if (i2 > 0)
			end = linePoints2[0];
*/
		/*AffineTransform at2 = EdgeRenderer getArrowTrans(start, end, width);
		Shape arrowHead2 = at2.createTransformedShape(m_arrowHead);

			g.fill(arrowHead2);*/
	//}



}

