package org.coode.xml;

/**
 * Copyright (C) 2006, Matthew Horridge, University of Manchester
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

import org.coode.string.EscapeUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 30-May-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * Developed as part of the CO-ODE project
 * http://www.co-ode.org
 */
public class XMLWriterImpl implements XMLWriter {


    private Stack<XMLElement> elementStack;

    private Writer writer;

    private String encoding;

    private String xmlBase;

    private XMLWriterNamespaceManager xmlWriterNamespaceManager;

    private Map entities;

    private static final int TEXT_CONTENT_WRAP_LIMIT = 15;

    private boolean preambleWritten;


    public XMLWriterImpl(Writer writer, XMLWriterNamespaceManager xmlWriterNamespaceManager) {
        this(writer, xmlWriterNamespaceManager, "");
    }


    public XMLWriterImpl(Writer writer, XMLWriterNamespaceManager xmlWriterNamespaceManager, String xmlBase) {
        this.writer = writer;
        this.xmlWriterNamespaceManager = xmlWriterNamespaceManager;
        this.xmlBase = xmlBase;
        this.encoding = "";
        elementStack = new Stack();
        setupEntities();
    }


    private void setupEntities() {
        ArrayList namespaces = new ArrayList(xmlWriterNamespaceManager.getNamespaces());
        Collections.sort(namespaces, new Comparator() {
            public int compare(Object o1, Object o2) {
                // Shortest string first
                return ((String) o1).length() - ((String) o2).length();
            }
        });
        entities = new LinkedHashMap();
        for (Iterator it = namespaces.iterator(); it.hasNext();) {
            String curNamespace = (String) it.next();
            String curPrefix = xmlWriterNamespaceManager.getPrefixForNamespace(curNamespace);
            entities.put(curNamespace.replace("%", "&#37"), "&" + curPrefix + ";");
        }
    }


    private String swapForEntity(String value) {
        String repVal;
        for (Iterator it = entities.keySet().iterator(); it.hasNext();) {
            String curEntity = (String) it.next();
            String entityVal = (String) entities.get(curEntity);
            if (value.length() > curEntity.length()) {
                repVal = value.replace(curEntity, entityVal);
//                repVal = StringUtils.replaceOnce(value, curEntity, entityVal);
                if (repVal.length() < value.length()) {
                    return repVal;
                }
            }
        }
        return value;
    }


    public String getDefaultNamespace() {
        return xmlWriterNamespaceManager.getDefaultNamespace();
    }


    public String getXMLBase() {
        return xmlBase;
    }


    public XMLWriterNamespaceManager getNamespacePrefixes() {
        return xmlWriterNamespaceManager;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    public void setWrapAttributes(boolean b) {
        if (elementStack.size() > 0) {
            XMLElement element = (XMLElement) elementStack.peek();
            element.setWrapAttributes(b);
        }
    }


    public void writeStartElement(String name) throws IOException {
        String qName = xmlWriterNamespaceManager.getQName(name);
        if(qName == null) {
            // Could not generate a valid QName, therefore, we cannot
            // write valid XML - just throw an exception!
            throw new RuntimeException("Could not generate legal element name (qname) for " + name);
        }
        XMLElement element = new XMLElement(qName, elementStack.size());
        if (elementStack.size() > 0) {
            XMLElement topElement = (XMLElement) elementStack.peek();
            if (topElement != null) {
                topElement.writeElementStart(false);
            }
        }
        elementStack.push(element);
    }


    public void writeEndElement() throws IOException {
        // Pop the element off the stack and write it out
        if (elementStack.size() > 0) {
            XMLElement element = (XMLElement) elementStack.pop();
            element.writeElementEnd();
        }
    }


    public void writeAttribute(String attr, String val) {
        XMLElement element = (XMLElement) elementStack.peek();
        element.setAttribute(xmlWriterNamespaceManager.getQName(attr), val);
    }


    public void writeTextContent(String text) {
        XMLElement element = (XMLElement) elementStack.peek();
        element.setText(text);
    }


    public void writeComment(String commentText) throws IOException {
        XMLElement element = new XMLElement(null, elementStack.size());
        element.setText("<!-- " + commentText + " -->");
        if (!elementStack.isEmpty()) {
            XMLElement topElement = (XMLElement) elementStack.peek();
            if (topElement != null) {
                topElement.writeElementStart(false);
            }
        }
        if (preambleWritten) {
            element.writeElementStart(true);
        }
        else {
            elementStack.push(element);
        }
    }


    private void writeEntities(String rootName) throws IOException {
        writer.write("\n\n<!DOCTYPE " + xmlWriterNamespaceManager.getQName(rootName) + " [\n");
        for (Iterator it = entities.keySet().iterator(); it.hasNext();) {
            String entityVal = (String) it.next();
            String entity = (String) entities.get(entityVal);
            entity = entity.substring(1, entity.length() - 1);
            writer.write("    <!ENTITY ");
            writer.write(entity);
            writer.write(" \"");
            writer.write(EscapeUtils.escapeXML(entityVal));
            writer.write("\" >\n");
        }
        writer.write("]>\n\n\n");
    }


    public void startDocument(String rootElementName) throws IOException {
        String encodingString = "";
        if (encoding.length() > 0) {
            encodingString = " encoding=\"" + encoding + "\"";
        }
        writer.write("<?xml version=\"1.0\"" + encodingString + "?>\n");
        if (XMLWriterPreferences.getInstance().isUseNamespaceEntities()) {
            writeEntities(rootElementName);
        }
        preambleWritten = true;
        while (!elementStack.isEmpty()) {
            elementStack.pop().writeElementStart(true);
        }
        writeStartElement(rootElementName);
        setWrapAttributes(true);
        writeAttribute("xmlns", xmlWriterNamespaceManager.getDefaultNamespace());
        if (xmlBase.length() != 0) {
            writeAttribute("xml:base", xmlBase);
        }
        for (Iterator it = xmlWriterNamespaceManager.getPrefixes().iterator(); it.hasNext();) {
            String curPrefix = (String) it.next();
            writeAttribute("xmlns:" + curPrefix, xmlWriterNamespaceManager.getNamespaceForPrefix(curPrefix));
        }
    }


    public void endDocument() throws IOException {
        // Pop of each element
        while (!elementStack.isEmpty()) {
            writeEndElement();
        }
        writer.flush();
    }


    public class XMLElement {

        private String name;

        private Map attributes;

        String textContent;

        private boolean startWritten;

        private int indentation;

        private boolean wrapAttributes;


        public XMLElement(String name) {
            this(name, 0);
            wrapAttributes = false;
        }


        public XMLElement(String name, int indentation) {
            this.name = name;
            attributes = new LinkedHashMap();
            this.indentation = indentation;
            textContent = null;
            startWritten = false;
        }


        public void setWrapAttributes(boolean b) {
            wrapAttributes = true;
        }


        public void setAttribute(String attribute, String value) {
            attributes.put(attribute, value);
        }


        public void setText(String content) {
            textContent = content;
        }


        public void writeElementStart(boolean close) throws IOException {
            if (!startWritten) {
                startWritten = true;
                insertIndentation();
                if (name != null) {
                    writer.write('<');
                    writer.write(name);
                    writeAttributes();
                    if (textContent != null) {
                        boolean wrap = textContent.length() > TEXT_CONTENT_WRAP_LIMIT;
                        if (wrap) {
                            writeNewLine();
                            indentation++;
                            insertIndentation();
                        }
                        writer.write('>');
                        writeTextContent();
                        if (wrap) {
                            indentation--;
                        }
                    }
                    if (close) {
                        if (textContent != null) {
                            writeElementEnd();
                        }
                        else {
                            writer.write("/>");
                            writeNewLine();
                        }
                    }
                    else {
                        if (textContent == null) {
                            writer.write('>');
                            writeNewLine();
                        }
                    }
                }
                else {
                    // Name is null so by convension this is a comment
                    if (textContent != null) {
                        writer.write("\n\n\n");
                        StringTokenizer tokenizer = new StringTokenizer(textContent, "\n", true);
                        while (tokenizer.hasMoreTokens()) {
                            String token = tokenizer.nextToken();
                            if (token.equals("\n") == false) {
                                insertIndentation();
                            }
                            writer.write(token);
                        }
                        writer.write("\n\n");
                    }
                }
            }
        }


        public void writeElementEnd() throws IOException {
            if (name != null) {
                if (startWritten == false) {
                    writeElementStart(true);
                }
                else {
                    if (textContent == null) {
                        insertIndentation();
                    }
                    writer.write("</");
                    writer.write(name);
                    writer.write(">");
                    writeNewLine();
                }
            }
        }


        private void writeAttribute(String attr, String val) throws IOException {
            writer.write(attr);
            writer.write('=');
            writer.write('"');
            if (XMLWriterPreferences.getInstance().isUseNamespaceEntities()) {
                writer.write(swapForEntity(EscapeUtils.escapeXML(val)));
            }
            else {
                writer.write(EscapeUtils.escapeXML(val));
            }
            writer.write('"');
        }


        private void writeAttributes() throws IOException {
            for (Iterator it = attributes.keySet().iterator(); it.hasNext();) {
                String attr = (String) it.next();
                String val = (String) attributes.get(attr);
                writer.write(' ');
                writeAttribute(attr, val);
                if (it.hasNext() && wrapAttributes) {
                    writer.write("\n");
                    indentation++;
                    insertIndentation();
                    indentation--;
                }
            }
        }


        private void writeTextContent() throws IOException {
            if (textContent != null) {
                writer.write(EscapeUtils.escapeXML(textContent));
            }
        }


        private void insertIndentation() throws IOException {
            if (XMLWriterPreferences.getInstance().isIndenting()) {
                for (int i = 0; i < indentation * XMLWriterPreferences.getInstance().getIndentSize(); i++) {
                    writer.write(' ');
                }
            }
        }


        private void writeNewLine() throws IOException {
            writer.write('\n');
        }
    }
}
