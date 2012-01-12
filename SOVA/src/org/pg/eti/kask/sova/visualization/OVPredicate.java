/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of SOVA.  SOVA is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.sova.visualization;

import org.pg.eti.kask.sova.edges.Edge;

import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.visual.EdgeItem;

/**
 * Klasa zawierająca predykaty filtrowania obiektów w wyswietlanych grafach
 * @author piotr29
 */
public class OVPredicate extends AbstractPredicate {

    public static final String NODE = "node";
    public static final  String EDGE = "edge";
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
            if (isDisjointEdgeFilter() && (o instanceof org.pg.eti.kask.sova.edges.DisjointEdge)) {
                return false;
            }
            if (isSubEdgeFilter() && (o instanceof org.pg.eti.kask.sova.edges.SubClassEdge)) {
                return false;
            }
            return true;
        }

        return true;
    }
}
