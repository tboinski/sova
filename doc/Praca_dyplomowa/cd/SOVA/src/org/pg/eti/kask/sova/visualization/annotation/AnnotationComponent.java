package org.pg.eti.kask.sova.visualization.annotation;
/**
 * Interfejs zawiera metody, które musi posiadać formatka wyświetlająca 
 * informacje o zaznaczonym wierzchołku 
 * @author Piotr Kunowski
 *
 */
public interface AnnotationComponent {
	/**
	 * ustawienie nazwy elementu
	 * @param name nazwa elementu
	 */
	public void setNameText(String name);
	/**
	 * ustawienie etykiety
	 * @param label
	 */
	public void setLabelText(String label);
	/**
	 * ustawienie opisu elementu
	 * @param coment 
	 */
	public void setCommentText(String coment);
	
}
