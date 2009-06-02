package org.coode.xml;

/**
 * Copyright (C) 2006, Matthew Horridge,  University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import java.io.Writer;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 30-May-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Developed as part of the CO-ODE project
 * http://www.co-ode.org
 */
public class XMLWriterFactory {

    private static XMLWriterFactory instance;


    private XMLWriterFactory() {

    }


    /**
     * Gets the one and only instance of the <code>XMLWriterFactory</code>
     */
    public static synchronized XMLWriterFactory getInstance() {
        if (instance == null) {
            instance = new XMLWriterFactory();
        }
        return instance;
    }


    /**
     * Creates an XMLWriter.
     *
     * @param writer The <code>Writer</code> that the XMLWriter will actually write to
     */
    public XMLWriter createXMLWriter(Writer writer, XMLWriterNamespaceManager xmlWriterNamespaceManager, String xmlBase) {
        return new XMLWriterImpl(writer, xmlWriterNamespaceManager, xmlBase);
    }
}
