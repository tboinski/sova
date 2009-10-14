package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * Nadklasa dla wszystkich krawędzi, zwykła czarna krawędź.
 */
public class Edge
{

	/**
	 *
	 */
	protected Color strokeColor;
	/**
	 *
	 */
	protected int strokeWidth;
	protected boolean hasArrow;
	protected boolean hasInvertedArrow;
	protected Polygon arrowHead;
	protected Color arrowHeadColor;



    /**
     *
     */
    public Edge () {
	    setStrokeColor(Color.BLACK);
	    hasArrow = false;
	    hasInvertedArrow = false;
    }

    public Color getStrokeColor () {
	    return strokeColor;
    }

	/**
	 *
	 */
	public void setStrokeColor(Color val)
	{
		this.strokeColor = val;
	}

	/**
	 *
	 */
	public int getStrokeWidth()
	{
		return strokeWidth;
	}

	/**
	 *
	 */
	public void setStrokeWidth(int val)
	{
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

