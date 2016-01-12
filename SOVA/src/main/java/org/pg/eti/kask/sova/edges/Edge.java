/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of SOVA.  SOVA is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package main.java.org.pg.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;

import main.java.org.pg.eti.kask.sova.options.EdgeColors;

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

