package org.eti.kask.sova.edges;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import prefuse.util.GraphicsLib;
import prefuse.visual.VisualItem;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.8F1FE813-4E1D-D395-E5D8-7F8461CB3BD1]
// </editor-fold> 
public class SubEdge extends Edge {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.76EDB4C0-6484-66F9-90A7-09ED4FB6C778]
    // </editor-fold> 
    public SubEdge () {
    }

	@Override
	public void renderShape(Graphics2D g, Point2D start, Point2D end, VisualItem sourceNode, VisualItem targetNode)
	{
		//super.renderShape(g);
		//Debug.sendMessage("SubEdgeRender"); - działa

		//czesc kodu do przeniesienia do klasy Edge
		g.setPaint(strokeColor);

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
	}



}

