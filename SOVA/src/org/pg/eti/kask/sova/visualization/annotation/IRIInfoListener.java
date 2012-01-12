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

package org.pg.eti.kask.sova.visualization.annotation;

import java.awt.event.MouseEvent;

import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import org.semanticweb.owlapi.model.IRI;
import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;
/**
 * Klasa listenera ustawia IRI wskazanego elementu w zadanym komponencie
 * @author Piotr Kunowski
 *
 */
public class IRIInfoListener extends ControlAdapter {
	private IRIInfoComponent iriInof;
	public IRIInfoListener(IRIInfoComponent component){
		this.iriInof = component;
	}
	
	/**
	 * Metoda po najechaniu na elemnet ustawia jego IRI w iriInfo
	 */
	@Override
	public void itemMoved(VisualItem arg0, MouseEvent arg1) {
		Object o = arg0.get(OWLtoGraphConverter.COLUMN_IRI);
		if (o==null){
			iriInof.setIRIInfo("");
		}else{
			iriInof.setIRIInfo(((IRI)o).toString());
		}
	}
	@Override
	public void itemExited(VisualItem arg0, MouseEvent arg1) {
		iriInof.setIRIInfo("");
	}

}
