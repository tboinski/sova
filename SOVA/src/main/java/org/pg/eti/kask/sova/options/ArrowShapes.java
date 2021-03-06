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

package org.pg.eti.kask.sova.options;

import java.awt.Polygon;

/**
 * Singleton przechowujący kształty grotów dla strzałek.
 * @author infinity
 */
public class ArrowShapes {

	protected Polygon directionArrow;
	protected Polygon inversedDirectionArrow;
	protected Polygon circle;
	protected Polygon diamond;
	
	private static ArrowShapes INSTANCE = null;

	public static ArrowShapes getInstance() {
		if (INSTANCE == null) INSTANCE = new ArrowShapes();
		return INSTANCE;
	}

	/**
	 * Wypełnia kształty domyślnymi wartościami.
	 */
	protected ArrowShapes()
	{
		directionArrow = new Polygon(
			new int[]{0,  -4,   4, 0},
			new int[]{0, -12, -12, 0}, 4);

		inversedDirectionArrow = new Polygon(
			new int[]{-4,   0, 4, 0},
			new int[]{ 0, -12, 0, 0}, 4);
				
		circle = drawCircle(4, 12);
		circle.translate(0, -4);

		diamond = new Polygon(
			new int[]{  4,   0, -4, 0},
			new int[]{ -4, -8, -4, 0}, 4);

	}

	/**
	 * Rysuje wielokąt w kształcie okręgu o środku w punkcie (0,0).
	 * @param r promień okręgu
	 * @param n liczba wierzchołków
	 * @return okrąg
	 */
	protected Polygon drawCircle(int r, int n) {
		int[] xpoints = new int[n];
		int[] ypoints = new int[n];

		float angle;

		for(int i = 0; i < n; i++ ) {
			angle = (float) ((float) i * ((float) 2 * Math.PI / (float) n));
			xpoints[i] = Math.round((float)(Math.cos(angle)*(float)r));
			ypoints[i] = Math.round((float)(Math.sin(angle)*(float)r));
		}

		return new Polygon(xpoints, ypoints, n);
	}

	public Polygon getCircle()
	{
		return circle;
	}

	public void setCircle(Polygon circle)
	{
		this.circle = circle;
	}

	public Polygon getDiamond()
	{
		return diamond;
	}

	public void setDiamond(Polygon diamond)
	{
		this.diamond = diamond;
	}

	public Polygon getDirectionArrow()
	{
		return directionArrow;
	}

	public void setDirectionArrow(Polygon directionArrow)
	{
		this.directionArrow = directionArrow;
	}

	public Polygon getInversedDirectionArrow()
	{
		return inversedDirectionArrow;
	}

	public void setInversedDirectionArrow(Polygon inversedDirectionArrow)
	{
		this.inversedDirectionArrow = inversedDirectionArrow;
	}

	


}
