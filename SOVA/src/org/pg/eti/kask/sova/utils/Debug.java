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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pg.eti.kask.sova.utils;

import java.io.PrintStream;

/**
 *
 * @author infinity
 */
public class Debug {
	
	private static final Debug INSTANCE = new Debug();
	private static PrintStream debugStream = null;
	
	private Debug() {
		
	}

	/**
	 * @return instancja klasy Debug.
	 */
	public Debug getInstance() {
		return INSTANCE;
	}

	/**
	 * @param ps strumień, na który będą wysyłane wiadomości pomocne przy debugowaniu.
	 */
	static public void setStream( PrintStream ps ) {
		debugStream = ps;
	}

	/**
	 * Wysyła wiadomość na strumień do debugowania, jeżeli został wcześniej podpięty za pomocą funkcji setStream.
	 * @param debugMessage
	 */
	static public void sendMessage( String debugMessage ) {
		if (debugStream != null)
			debugStream.println( debugMessage );
	}


}
