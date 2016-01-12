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

import main.java.org.pg.eti.kask.sova.options.ArrowShapes;
import main.java.org.pg.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź łączącą Property z klasą właściwości
 * (OWL DomainOf).
 */
public class DomainEdge extends Edge
{

	public DomainEdge()
	{
		super();
		hasArrow = true;
		hasInvertedArrow = true;
	}

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getCircle();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getCircle();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getDomainEdgeColor();
	}
}

