/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pg.eti.kask.sova.nodes;

import java.awt.Color;

import org.pg.eti.kask.sova.options.NodeColors;
import org.pg.eti.kask.sova.options.NodeShapeType;
import org.pg.eti.kask.sova.options.NodeShapes;

/**
 * Klasa abstrakcyjna. Nadklasa wszystkich węzłów.
 */
public abstract class Node
{
	protected float strokeWitdh;	
	protected String label;

	public Node()
	{
		label = "no label!";
		strokeWitdh = 1f; //domyślna grubość ramki węzła
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getClassNodeColor();
	}

	/**
	 * @param shapeScheme źródłowy schemat kształtów dla węzłów
	 * @return rodzaj kształtu węzła z zadanego schematu
	 */
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getDefaultNodeShapeType();
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