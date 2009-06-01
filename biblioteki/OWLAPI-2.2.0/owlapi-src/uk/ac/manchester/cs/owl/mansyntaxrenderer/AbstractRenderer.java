package uk.ac.manchester.cs.owl.mansyntaxrenderer;

import org.coode.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owl.io.OWLRendererException;
import org.semanticweb.owl.io.OWLRendererIOException;
import org.semanticweb.owl.util.ShortFormProvider;
import org.semanticweb.owl.util.SimpleShortFormProvider;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * Copyright (C) 2007, University of Manchester
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


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 25-Apr-2007<br><br>
 */
public class AbstractRenderer {

    private ShortFormProvider shortFormProvider;

    private int lastNewLinePos = -1;

    private int currentPos;

    private Writer writer;

    private List<Integer> tabs;

    private boolean useTabbing = true;

    public AbstractRenderer(Writer writer) {
        this.writer = writer;
        shortFormProvider = new SimpleShortFormProvider();
        tabs = new ArrayList<Integer>();
        pushTab(0);
    }


    public void setUseTabbing(boolean useTabbing) {
        this.useTabbing = useTabbing;
    }


    public void setShortFormProvider(ShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
    }


    public void flush() throws OWLRendererException {
        try {
            writer.flush();
        }
        catch (IOException e) {
            throw new OWLRendererIOException(e);
        }
    }

    protected void pushTab(int size) {
        tabs.add(0, size);
    }

    protected void incrementTab(int increment) {
        int base = 0;
        if(!tabs.isEmpty()) {
            base = tabs.get(0);
        }
        tabs.add(0, base + increment);
    }

    protected void popTab() {
        tabs.remove(0);
    }

    protected void writeTab() {
        int tab = tabs.get(0);
        for(int i = 0; i < tab; i++) {
            write(" ");
        }
    }

    protected int getIndent() {
        return currentPos - lastNewLinePos - 2;
    }


    protected void write(String s) {
        if(s == null) {
            return;
        }
        int indexOfNewLine = s.indexOf('\n');
        if(indexOfNewLine != -1) {
            lastNewLinePos = currentPos + indexOfNewLine;
        }
        currentPos += s.length();
        try {
            writer.write(s);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(String s, int lineLen) {
        StringTokenizer tokenizer = new StringTokenizer(s, " \n\t-", true);
        int currentLineLength = 0;
        while(tokenizer.hasMoreTokens()) {
            String curToken = tokenizer.nextToken();
            write(curToken);
            if(curToken.equals("\n")) {
                writeTab();
            }
            currentLineLength += curToken.length();
            if(currentLineLength > lineLen && curToken.trim().length() != 0 && tokenizer.hasMoreTokens()) {
                writeNewLine();
                currentLineLength = 0;
            }
        }
    }

    protected void writeSpace() {
        write(" ");
    }

    protected void write(ManchesterOWLSyntax keyword) {
        write(" ", keyword, " ");
    }


    protected void writeNewLine() {
        write("\n");
        if (useTabbing) {
            writeTab();
        }
    }

    protected void write(String prefix, ManchesterOWLSyntax keyword, String suffix) {
        write(prefix);
        write(keyword.toString());
        write(suffix);
    }

    protected void write(URI uri) {
        if (uri.getFragment() != null) {
            write(uri.getFragment());
        }
        else {
            write(uri.toString());
        }
    }

    protected ShortFormProvider getShortFormProvider() {
        return shortFormProvider;
    }

}
