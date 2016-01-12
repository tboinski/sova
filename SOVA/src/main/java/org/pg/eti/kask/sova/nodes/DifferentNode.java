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

package main.java.org.pg.eti.kask.sova.nodes;

import java.awt.Color;

import main.java.org.pg.eti.kask.sova.options.NodeColors;

/**
 * Klasa reprezentuje węzeł oznaczający relację DifferentFrom
 * lub AllDifferent pomiędzy wystąpieniami klas (OWL Individual).
 */
public class DifferentNode extends InformationNode
{

	/**
	 * Ustawia eykietę tego węzła na "≠".
	 */
	public DifferentNode()
	{
		super();
		label = "≠";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getDifferentNodeColor();
	}


}