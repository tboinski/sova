package org.eti.kask.sova.visualization;

import org.eti.kask.sova.edges.Edge;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.visual.EdgeItem;

/**
 * Klasa zawierająca predykaty filtrowania obiektów w wyswietlanych grafach
 * @author piotr29
 */
public class OVPredicate extends AbstractPredicate {

    private String NODE = "node";
    private String EDGE = "edge";
    private boolean subEdgeFilter = false;
    private boolean disjointEdgeFilter = false;

    public OVPredicate() {
    }

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

    /**
     * metoda zwraca elamenty które będą wizualizowane
     * @param t element grafu
     * @return true jeśli element ma być widoczny, przeciwnie flase
     */
    @Override
    public boolean getBoolean(Tuple t) {
        if ((t instanceof Edge) || (t instanceof EdgeItem)) {
            Object o = t.get(EDGE);
            if (isDisjointEdgeFilter() && (o instanceof org.eti.kask.sova.edges.DisjointEdge)) {
                return false;
            }
            if (isSubEdgeFilter() && (o instanceof org.eti.kask.sova.edges.SubEdge)) {
                return false;
            }
            return true;
        }

        return true;
    }
}
