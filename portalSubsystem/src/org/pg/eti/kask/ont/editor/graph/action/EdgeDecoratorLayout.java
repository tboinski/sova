package org.pg.eti.kask.ont.editor.graph.action;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import org.pg.eti.kask.ont.editor.consts.Constants;

import prefuse.Visualization;
import prefuse.action.layout.Layout;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualItem;

/**
 * Klasa wyznaczajaca polozenie decoratora dla krawedzi
 * grafu. Dla kazdego kazdego decoratora wyznaczany jest 
 * element dekorowany. Polozenie dekoratora jest wyznaczone 
 * jest jako przeciecie przekatnych elementu dekorowanego.
 * 
 * @author Andrzej Jakowski
 */
public class EdgeDecoratorLayout extends Layout {

	/**
	 * Tworzy nowa instancje klasy przeprowadzajacej uklad
	 * dla dekoratora krawedzi. 
	 * 
	 * @param group grupa elementow grafu bedacych dekoratorami
	 * @param visualization referencja do obiektu visualization
	 */
	public EdgeDecoratorLayout(Visualization visualization) {		
		this.m_vis = visualization;
	}

	/**
	 * Przeladowana metoda z klasy bazowej.
	 */
	@Override
	public void run(double frac) {
		Iterator<?> iter = (Iterator<?>) m_vis.items(Constants.EDGE_DECORATORS);
		while (iter.hasNext()) {
			DecoratorItem item = (DecoratorItem) iter.next();
			VisualItem node = item.getDecoratedItem();
			Rectangle2D bounds = node.getBounds();
			setX(item, null, bounds.getCenterX());
			setY(item, null, bounds.getCenterY());
		}
	}
}