package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.B7C99D74-BF0F-4B05-D6B3-E7E54BDEE4F1]
// </editor-fold> 
public class Edge {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.4DE59F15-5FAB-F831-3F19-A2420FCFFF5C]
    // </editor-fold> 
    protected Color strokeColor;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.835E3069-DF1F-39A6-EACF-05513325683C]
    // </editor-fold> 
    protected int strokeWidth;


    protected boolean hasArrow;
    protected boolean hasInvertedArrow;
    protected Polygon arrowHead;
    protected Color arrowHeadColor;


    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.34E7DCE2-36DC-D3E7-7EC8-8CFDF8D4E076]
    // </editor-fold> 
    public Edge () {
	    setStrokeColor(Color.BLACK);
	    hasArrow = false;
	    hasInvertedArrow = false;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.D1A2D681-CDE3-BA7B-6064-1260D774B6B5]
    // </editor-fold> 
    public Color getStrokeColor () {
        return strokeColor;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.209A98D5-AB30-1096-6C17-837EAC30CF81]
    // </editor-fold> 
    public void setStrokeColor (Color val) {
        this.strokeColor = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.749333FD-92CF-870A-34E7-165DFA7F9C96]
    // </editor-fold> 
    public int getStrokeWidth () {
        return strokeWidth;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.D416D75E-5B30-8AB3-1861-FA0D95C6A647]
    // </editor-fold> 
    public void setStrokeWidth (int val) {
        this.strokeWidth = val;
    }


	public Polygon getArrowHead()
	{
		return arrowHead;
	}

	public void setArrowHead(Polygon arrowHead)
	{
		this.arrowHead = arrowHead;
	}

	public boolean isHasArrow()
	{
		return hasArrow;
	}

	public void setHasArrow(boolean hasArrow)
	{
		this.hasArrow = hasArrow;
	}

	public boolean isHasInvertedArrow()
	{
		return hasInvertedArrow;
	}

	public void setHasInvertedArrow(boolean hasInvertedArrow)
	{
		this.hasInvertedArrow = hasInvertedArrow;
	}

	public Color getArrowHeadColor()
	{
		return arrowHeadColor;
	}

	public void setArrowHeadColor(Color arrowHeadColor)
	{
		this.arrowHeadColor = arrowHeadColor;
	}

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.7F2F96F8-B319-8EE3-7EDB-3C2A09BB1022]
    // </editor-fold> 
    /*public void renderShape (Graphics2D g, VisualItem item, Point2D start, Point2D end) {



    }*/



}

