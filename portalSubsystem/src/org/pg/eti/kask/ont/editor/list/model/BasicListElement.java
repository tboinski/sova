package org.pg.eti.kask.ont.editor.list.model;

public class BasicListElement {
	
	private String elementLabel;
	
	private String elementURI;
	
	public BasicListElement() {
		this.elementLabel = new String();
		this.elementURI = new String();
	}

	public BasicListElement(String elementLabel, String elementURI) {
		this.elementLabel = elementLabel;
		this.elementURI = elementURI;
	}

	public String getElementLabel() {
		return elementLabel;
	}

	public void setElementLabel(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	public String getElementURI() {
		return elementURI;
	}

	public void setElementURI(String elementURI) {
		this.elementURI = elementURI;
	}

	@Override
	public String toString() {
		return this.elementLabel;
	}

	@Override
	public boolean equals(Object obj) {
		BasicListElement element = (BasicListElement)obj;
		if(this.elementURI.equals(element.getElementURI())) {
			return true;
		} else {
			return false;
		}
	}
}
