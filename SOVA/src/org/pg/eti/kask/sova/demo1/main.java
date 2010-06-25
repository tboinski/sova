package org.pg.eti.kask.sova.demo1;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pg.eti.kask.sova.utils.Debug;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            PrintStream p;
            p = new PrintStream(System.out);
            Debug.setStream(p);
            Demo demo = new Demo();
            demo.startVisualization();
            
            

            p.close();
        } catch (Exception ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }

	}

}
