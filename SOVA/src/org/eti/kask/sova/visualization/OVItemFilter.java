
package org.eti.kask.sova.visualization;

import java.util.Iterator;
import org.eti.kask.sova.edges.Edge;
import prefuse.action.GroupAction;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;
import prefuse.visual.tuple.TableEdgeItem;

/**
 * Klasa filtrująca wyswietlane elementy ontologii. Pozwala na włączania
 * i wyłączanie wizualizacji elementu.
 * @author Piotr Kunowski
 */
public class OVItemFilter extends GroupAction {
    private boolean subEdgeFilter = false;
    private boolean disjointEdgeFilter = false;


    /**
     * sprawdzenie czy jest włączone filtrowanie krawędzi typu DisjointEdge
     * @return true jeśli włączone, przeciwnie false
     */
    public boolean isDisjointEdgeFilter() {
        return disjointEdgeFilter;
    }

    /**
     * Ustawienie opcji filtrowania
     * @param subEdgeFilter true włączenie filtru
     */
    public void setDisjointEdgeFilter(boolean disjointEdgeFilter) {
        this.disjointEdgeFilter = disjointEdgeFilter;
    }

    /**
     * sprawdzenie czy jest włączone filtrowanie krawędzi typu SubEdge
     * @return true jeśli włączone, przeciwnie false
     */
    public boolean isSubEdgeFilter() {
        return subEdgeFilter;
    }

    /**
     * Ustawienie opcji filtrowania
     * @param subEdgeFilter true włączenie filtru
     */
    public void setSubEdgeFilter(boolean subEdgeFilter) {
        this.subEdgeFilter = subEdgeFilter;
    }

    @Override
    public void run(double frac) {


        Iterator items = m_vis.items(m_group);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem)items.next();
            boolean isVisualItemVisible = item.isVisible();
            if(isVisualItemVisible) {
                if ((item instanceof Edge) || (item instanceof EdgeItem) || (item instanceof  TableEdgeItem)) {
                    Object o = ((VisualItem)item).get(OVPredicate.EDGE);
                    if (isDisjointEdgeFilter() && (o instanceof org.eti.kask.sova.edges.DisjointEdge)) {
                       // return false;
//                       ((prefuse.data.Edge)item).get
                        ((VisualItem)item).setVisible(false);
                    }else
                    if (isSubEdgeFilter() && (o instanceof org.eti.kask.sova.edges.SubEdge)) {
                       // return false;
                        ((VisualItem)item).setVisible(false);
                    }else{
                        ((VisualItem)item).setVisible(true);
                    }

                }
			} else {
				((VisualItem)item).setVisible(false);
			}
        }

    }



}
