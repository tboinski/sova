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
