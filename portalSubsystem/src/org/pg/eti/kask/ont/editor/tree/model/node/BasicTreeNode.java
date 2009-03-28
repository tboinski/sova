package org.pg.eti.kask.ont.editor.tree.model.node;

/**
 * Klasa reprezentujaca obiekt <code>UserData</code> we 
 * wszystkich drzewach JTree w aplikacji.
 * 
 * @author Andrzej Jakowski
 */
public class BasicTreeNode{

	private static final long serialVersionUID = 2569871831787816016L;
	
	private String elementURI;
	
	private String elementLabel;
	
	private String type;
	
	/**
	 * Konstuktor inicjalizujacy wszystkie pola. 
	 * 
	 * @param elementURI URI elementu
	 * @param elementLabel etykieta elementu
	 * @param type typ elementu
	 */
	public BasicTreeNode(String elementURI, String elementLabel, String type) {
		this.elementURI = elementURI;
		this.elementLabel = elementLabel;
		this.type = type;
	}

	@Override
	public String toString() {
		return elementLabel;
	}


	/**
	 * Zwraca wartosc pola <code>elementURI</code>.
	 * 
	 * @return elementURI
	 */
	public String getElementURI() {
		return elementURI;
	}

	/**
	 * Ustawia pole <code>elementURI</code>.
	 * 
	 * @param elementURI
	 */
	public void setElementURI(String elementURI) {
		this.elementURI = elementURI;
	}

	/**
	 * Zwraca wartosc pola <code>elementLabel</code>.
	 * 
	 * @return elementLabel
	 */
	public String getElementLabel() {
		return elementLabel;
	}

	/**
	 * Ustawia pole <code>elementLabel</code>.
	 * 
	 * @param elementLabel
	 */
	public void setElementLabel(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	/**
	 * Zwraca wartosc pola <code>type</code>.
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Ustawia pole <code>type</code>.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
