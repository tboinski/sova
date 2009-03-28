package org.pg.eti.kask.ont.editor.graph.action;

import java.util.Iterator;

import org.pg.eti.kask.ont.editor.consts.Constants;

import prefuse.Visualization;
import prefuse.action.GroupAction;
import prefuse.visual.DecoratorItem;

/**
 * Klasa przeprowadzajaca filtrowanie wyswietlania releacji
 * na grafie ontologii. Definiuje takze domyslne wartosci
 * filtra.
 * 
 * @author Andrzej Jakowski
 */
public class RelationsFilter extends GroupAction {

	//aktualnie wybrany filtr 
	private int relationsFilter;
		
	/** Predefiniowany filtr nic nie wyswietlajacy */
	public static final int SHOW_NONE = 0;
	
	/** Predefiniowany filtr wyswietlajacy tylko podklasy */
	public static final int SHOW_SUBCLASSES = 1;
	
	/** Predefiniowany filtr wyswietlajacy klasy rozlaczne z dana klasa */
	public static final int SHOW_DISJOINTCLASSES = 2;
	
	/** Predefiniowany filtr wyswietlajacy klasy rownorzedne */
	public static final int SHOW_EQUIVALENTCLASSES = 4;
	
	/** Predefiniowany filtr wyswietlajacy wszystko */
	public static final int SHOW_ALL = 7;

	public RelationsFilter(Visualization visualization) {
		this.m_vis = visualization;
		this.relationsFilter = SHOW_SUBCLASSES;
	}

	/**
	 * Przeladowana metoda z klasy bazowej
	 */
	@Override
	public void run(double frac) {
		Iterator<?> iter = (Iterator<?>) m_vis.items(Constants.EDGE_DECORATORS);
		while (iter.hasNext()) {
			DecoratorItem di = (DecoratorItem)iter.next();
			boolean isDecoratedItemVisible = di.getDecoratedItem().isVisible();

			if(isDecoratedItemVisible) {
				String type =""+di.get("type");
				if(type.equals(Constants.GRAPH_EDGE_SUBCLASS_TYPE)) {
					if((relationsFilter & SHOW_SUBCLASSES) > 0) {
						di.setVisible(true);
					}
					else { 
						di.setVisible(false);
						di.getDecoratedItem().setVisible(false);
					}
				} else if(type.equals(Constants.GRAPH_EDGE_DISJOINT_TYPE)) {
					if((relationsFilter & SHOW_DISJOINTCLASSES) > 0) {
						di.setVisible(true);
					}
					else {
						di.setVisible(false);
						di.getDecoratedItem().setVisible(false);
					}
				}
				else if(type.equals(Constants.GRAPH_EDGE_EQUIVALENCE_TYPE)) {
					if((relationsFilter & SHOW_EQUIVALENTCLASSES) > 0) {
						di.setVisible(true); 
					}
					else {
						di.setVisible(false);
						di.getDecoratedItem().setVisible(false);
					}
				}
			} else {
				di.setVisible(false);
			}
		}
	}

	/**
	 * Zwraca aktualna wartosc filtra relacji na grafie ontologii. 
	 * 
	 * @return
	 */
	public int getRelationsFilter() {
		return relationsFilter;
	}

	/**
	 * Ustawia wartosc filtra na grafie ontologii. 
	 * 
	 * @param relationsFilter
	 */
	public void setRelationsFilter(int relationsFilter) {
		this.relationsFilter = relationsFilter;
	}

	

}
