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

import org.pg.eti.kask.sova.options.EdgeColors;

/**
 * Klasa reprezentująca krawędź oznaczającą wzajemną odwrotność (OWL InverseOf)
 * property. Przy wzajemnej odwrotności krawędź nie posiada grotu.
 * @author infinity
 */
public class InverseOfMutualEdge extends InverseOfEdge {

	public InverseOfMutualEdge()
	{
		super();
		hasArrow = false;
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor dla tej krawędzi ustawiony w zadanym schemacie
	 */
	@Override
	public Color getStrokeColorFromScheme(EdgeColors colorScheme)
	{
		return colorScheme.getInverseOfMutualEdgeColor();
	}

}
