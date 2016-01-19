/*
 *
 * Copyright (c) 2010 Kunowski Piotr
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

package org.pg.eti.kask.ont.pluginSova;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;

import org.pg.eti.kask.sova.visualization.annotation.IRIInfoComponent;

public class IRITextField extends JTextField implements IRIInfoComponent {

	{	/* blok inicjujacy */
		this.setSize(new Dimension(500,20));
		this.setMaximumSize(new Dimension(500,20));
		this.setMinimumSize(new Dimension(500,20));
		this.setColumns(50);
		this.setEditable(false);
		this.setBackground(Color.WHITE);
	}

	public void setIRIInfo(String iri) {
		this.setText(iri);

	}

}
