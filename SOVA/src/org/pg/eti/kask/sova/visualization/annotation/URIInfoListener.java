/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.sova.visualization.annotation;

import java.awt.event.MouseEvent;
import java.net.URI;

import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;
/**
 * Klasa listenera ustawia URI wskazanego elementu w zadanym komponencie
 * @author Piotr Kunowski
 *
 */
public class URIInfoListener extends ControlAdapter {
	private URIInfoComponent uriInof;
	public URIInfoListener(URIInfoComponent component){
		this.uriInof = component;
	}
	
	/**
	 * Metoda po najechaniu na elemnet ustawia jego uri w uriInfo
	 */
	@Override
	public void itemMoved(VisualItem arg0, MouseEvent arg1) {
		Object o = arg0.get(OWLtoGraphConverter.COLUMN_URI);
		if (o==null){
			uriInof.setURIInfo("");
		}else{
			uriInof.setURIInfo(((URI)o).toString());
		}
	}
	@Override
	public void itemExited(VisualItem arg0, MouseEvent arg1) {
		uriInof.setURIInfo("");
	}

}
