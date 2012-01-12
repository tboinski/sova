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

import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.layout.graph.FruchtermanReingoldLayout;
import prefuse.activity.Activity;

public class FruchtermanReingoldVis extends OVVisualization  {

    /**
     * ustawienie vizualizacji
     */
    @Override
    public void setVisualizationLayout() {

    	FruchtermanReingoldLayout graphLayout =  new FruchtermanReingoldLayout(GRAPH);
        ActionList layout = new ActionList(Activity.INFINITY);
        //ustawienie długości krawędzi 
        layout.add(graphLayout);
        layout.add(new RepaintAction());
        this.putAction(LAYOUT_ACTION, layout);
        addRepaintAction();

    }

    /**
     * ustawienie podstawowego layoutu i domyślnych filtrów.
     */
    @Override
    public void setVisualizationSettings() {
        super.setVisualizationSettings();
        this.addFilters();

    }

}
