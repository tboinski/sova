/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eti.kask.sova.nodes;

import java.awt.Color;
import java.awt.Graphics2D;

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

	public Color getFillColor()
	{
		return fillColor;
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

    // <editor-fold defaultstate="collapsed" desc=" UML Marker ">
    // #[regen=yes,id=DCE.8BEC2D01-6712-8B52-F218-54CF9071E33B]
    // </editor-fold>
    /*public Annotation getAnnotation () {
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker ">
    // #[regen=yes,id=DCE.F6219503-B98B-D945-EC48-0BF02D9365EB]
    // </editor-fold>
    public void setAnnotation (Annotation val) {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker ">
    // #[regen=yes,id=DCE.1D8B5B6E-ABF1-D4A3-7B30-C7FE1EC723F5]
    // </editor-fold>
    public Comment getComment () {
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker ">
    // #[regen=yes,id=DCE.63D8B7B7-6963-2261-0666-A8C8F62FAB0A]
    // </editor-fold>
    public void setComment (Comment val) {
    }*/

}
