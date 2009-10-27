package org.eti.kask.sova.visualization;

import java.awt.Color;
import prefuse.util.GraphicsLib;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.eti.kask.sova.edges.Edge;
import prefuse.util.ColorLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

/**
 * Klasa odpowiadająca za właściwe wyświetlanie krawędzi.
 */
public class EdgeRenderer extends prefuse.render.EdgeRenderer
{

	public EdgeRenderer()
	{
	}


	@Override
	public void render(Graphics2D g, VisualItem item)
	{
		EdgeItem ei = (EdgeItem) item;
		int edgeRowNumber = ei.getRow();
		Edge ourEdge = (Edge) ei.getGraph().getEdgeTable().get(edgeRowNumber, "edge");

		// rysowanie krawędzi
		Shape shape = getShape(item);
		item.setStrokeColor(ColorLib.color(ourEdge.getStrokeColor()));
		
		if (shape != null) {
			drawShape(g, item, shape);
		}

		
		//ei.getVisualization().getAction("color");
		Point2D start = null, end = null;
		start = m_tmpPoints[0];
		end = m_tmpPoints[1];

		if (ourEdge.isHasArrow()) {
			VisualItem item2 = (VisualItem) ei.getTargetItem();
			Rectangle2D r2 = item2.getBounds();
			int i2 = GraphicsLib.intersectLineRectangle(start, end, r2,
					m_isctPoints);
			if (i2 > 0)
				end = m_isctPoints[0];
			
			AffineTransform at2 = getArrowTrans(start, end, ourEdge.getStrokeWidth());
			Shape arrowHead = at2.createTransformedShape(ourEdge.getArrowHead());

			g.setPaint(ourEdge.getArrowHeadColor());
			g.fill(arrowHead);
			g.setPaint(ourEdge.getStrokeColor());
			g.draw(arrowHead);
		}

		if (ourEdge.isHasInvertedArrow()) {
			VisualItem item2 = (VisualItem) ei.getSourceItem();
			Rectangle2D r2 = item2.getBounds();
			int i2 = GraphicsLib.intersectLineRectangle(start, end, r2,
					m_isctPoints);
			if (i2 > 0)
				end = m_isctPoints[0];

			AffineTransform at2 = getArrowTrans(end, start, ourEdge.getStrokeWidth());
			Shape arrowHead = at2.createTransformedShape(ourEdge.getInvArrowHead());

			g.setPaint(ourEdge.getArrowHeadColor());
			g.fill(arrowHead);
			g.setPaint(ourEdge.getStrokeColor());
			g.draw(arrowHead);
		}
	}
}

