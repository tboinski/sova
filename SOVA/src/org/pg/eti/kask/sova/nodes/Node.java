/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
