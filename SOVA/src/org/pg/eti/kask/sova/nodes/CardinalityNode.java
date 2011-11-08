/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
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

package org.pg.eti.kask.sova.nodes;

/**
 * Klasa reprezentuje węzeł klasy anonimowej powstałej w wyniku nałożenia
 * ograniczenia Cardinality.
 */
public class CardinalityNode extends AnonymousClassNode
{

	/**
	 * Ustawia etykietę tego węzła jako "N".
	 */
	public CardinalityNode()
	{
		super();
		label = "N";
	}

	/**
	 * Klasy anonimowe nie posiadają konwencjonalnych etykiet.
	 * Poniższa funkcja nie robi nic.
	 */
	@Override
	public void setLabel(String label)
	{
	}

}

