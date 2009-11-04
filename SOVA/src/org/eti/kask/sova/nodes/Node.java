/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eti.kask.sova.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import org.eti.kask.sova.options.NodeColors;

/**
 *
 * @author infinity
 */
public abstract class Node {

	protected Color fillColor;
	protected int strokeWitdh;
	protected int height;
	protected int witdh;
	protected String label;

	public Node()
	{
		label = "no label!";
	}

	/**
	 * @return domyślny kolor wypełnienia węzła
	 */
	public Color getFillColor()
	{
		return fillColor;
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getClassNodeColor();
	}

	public void setFillColor(Color fillColor)
	{
		this.fillColor = fillColor;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public int getStrokeWitdh()
	{
		return strokeWitdh;
	}

	public void setStrokeWitdh(int strokeWitdh)
	{
		this.strokeWitdh = strokeWitdh;
	}

	public int getWitdh()
	{
		return witdh;
	}

	public void setWitdh(int witdh)
	{
		this.witdh = witdh;
	}



	@Override
    public String toString() {
	    return getLabel();
    }

   /**
    *
    */
    /*public Annotation getAnnotation () {
        return null;
    }

   /**
    *
    */
    /*public void setAnnotation (Annotation val) {
    }*/

   /**
    *
    */
    /*public Comment getComment () {
        return null;
    }*/

   /**
    *
    */
    /*public void setComment (Comment val) {
    }*/

}
