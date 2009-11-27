/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eti.kask.sova.nodes;

import java.awt.Color;
import org.eti.kask.sova.options.NodeColors;

/**
 * Klasa abstrakcyjna. Nadklasa wszystkich węzłów.
 */
public abstract class Node
{

	protected Color fillColor;
	protected float strokeWitdh;
	protected int height;
	protected int witdh;
	protected String label;
	protected boolean rounded = true;

	public Node()
	{
		label = "no label!";
		strokeWitdh = 1f; //domyślna grubość ramki węzła
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

	/**
	 * @return etykieta węzła
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label etykieta węzła
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return grubość obramowania węzła
	 */
	public float getStrokeWitdh()
	{
		return strokeWitdh;
	}

	/**
	 * @param strokeWidth grubość obramowania węzła
	 */
	public void setStrokeWitdh(float strokeWitdh)
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

	/**
	 * @return etykieta węzła
	 * @see Node#getLabel() 
	 */
	@Override
	public String toString()
	{
		return getLabel();
	}

	/**
	 * True, gdy węzeł ma mieć zaokrąglone narożniki.
	 * True, when the node should have rounded corners.
	 */
	public boolean isRounded()
	{
		return rounded;
	}

	public void setRounded(boolean rounded)
	{
		this.rounded = rounded;
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
