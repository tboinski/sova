package org.eti.kask.sova.visualization;

import java.awt.Color;
import prefuse.util.GraphicsLib;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.eti.kask.sova.edges.Edge;
import prefuse.util.ColorLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;



// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.769FB976-1116-80D3-DB2B-962A92ACAA70]
// </editor-fold> 
public class EdgeRenderer extends prefuse.render.EdgeRenderer {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.8C9D0761-8895-EEEC-F2E0-DD75418D827D]
    // </editor-fold> 
    public EdgeRenderer () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.7A3AF910-9382-3BA5-8EBB-D4FDA1C41D2E]
    // </editor-fold> 
	@Override
    public void render (Graphics2D g, VisualItem item) {

		Shape shape = getShape(item);
		item.setStrokeColor(ColorLib.color(Color.ORANGE));
		//g.setPaint();


		if (shape != null)
			drawShape(g, item, shape);

		EdgeItem ei = (EdgeItem)item;
		int edgeRowNumber = ei.getRow();
		Edge ourEdge = (Edge) ei.getGraph().getEdgeTable().get(edgeRowNumber, "edge");
		//ei.getVisualization().getAction("color");
		Point2D start = null, end = null;
		/*String stype = (String) item.get(EDGE_TYPE);
		int type = m_edgeType;
		if (stype != null) {
			try {
				type = Integer.parseInt(stype);
			} catch (Exception ex) {
			}
		}
		switch (type) {
		case EDGE_TYPE_LINE:
			start = m_tmpPoints[0];
			end = m_tmpPoints[1];
			width = m_width;
			break;
		case EDGE_TYPE_CURVE:
			start = m_ctrlPoints[1];
			end = m_tmpPoints[1];
			width = 1;
			break;
		default:
			throw new IllegalStateException("Unknown edge type.");
		}*/
		
		start = m_tmpPoints[0];
		end = m_tmpPoints[1];
		VisualItem sourceNode = (VisualItem) ei.getSourceNode();		
		VisualItem targetNode = (VisualItem) ei.getTargetNode();
		
		ourEdge.renderShape(g, start, end, sourceNode, targetNode);
		


    }

}

