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
import main.java.org.pg.eti.kask.sova.options.NodeShapeType;
import main.java.org.pg.eti.kask.sova.options.NodeShapes;

/**
 * Klasa reprezentuje węzeł OWL Nothing.
 */
public class NothingNode extends Node
{

	/**
	 * Ustawia etykietę tego węzła jako "NT".
	 */
	public NothingNode()
	{
		super();
		label = "NT";
	}

	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getNothingNodeColor();
	}

	/**
	 * @param shapeScheme źródłowy schemat kształtów dla węzłów
	 * @return rodzaj kształtu węzła z zadanego schematu
	 */
	@Override
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getInformationNodeShapeType();
	}

}

