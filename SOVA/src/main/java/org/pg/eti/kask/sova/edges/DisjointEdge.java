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

package org.pg.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Polygon;

import org.pg.eti.kask.sova.options.ArrowShapes;
import org.pg.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź oznaczającą rozłączność klas (OWL Disjoint).
 */
public class DisjointEdge extends Edge
{

	public DisjointEdge()
	{
		super();
		hasArrow = true;
		hasInvertedArrow = true;
	}

	@Override
	public Polygon getInvArrowHead()
	{
		return ArrowShapes.getInstance().getInversedDirectionArrow();
	}

	@Override
	public Polygon getArrowHead()
	{
		return ArrowShapes.getInstance().getInversedDirectionArrow();
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor krawędzi wraz z grotami z zadanego schematu
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getDisjointEdgeColor();
	}
}

