package org.pg.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;

import org.pg.eti.kask.sova.options.EdgeColors;

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
	protected Color arrowHeadColor;



    /**
     *
     */
    public Edge () {
	    hasArrow = false;
	    hasInvertedArrow = false;
    }

    /**
     * @return domyślny kolor dla tej krawędzi
     */
    public Color getStrokeColor () {
	    return strokeColor;
    }

    /**
     * @param colorScheme źródłowy schemat kolorów
     * @return kolor dla tej krawędzi ustawiony w zadanym schemacie
     */
    public Color getStrokeColorFromScheme (EdgeColors colorScheme) {
	    return colorScheme.getEdgeColor();
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
		return null;
	}

	public Polygon getInvArrowHead()
	{
		return null;
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




}

