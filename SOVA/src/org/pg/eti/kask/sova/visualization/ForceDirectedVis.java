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

import javax.swing.JPanel;
import org.semanticweb.owlapi.model.OWLOntology;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.util.force.ForceSimulator;
import prefuse.util.ui.JForcePanel;

/**
 * Klasa wizualizujące grafy w oparciu o algorytm ForceDirected
 * @author Piotr Kunowski
 */
public class ForceDirectedVis extends OVVisualization {

    public ForceDirectedVis(OWLOntology o) {
        super(o);
    }

    /**
     * ustawienie vizualizacji
     */
    @Override
    public void setVisualizationLayout() {

        ForceDirectedLayout graphLayout =  new ForceDirectedLayout(GRAPH);
        ActionList layout = new ActionList(Activity.INFINITY);
        //ustawienie długości krawędzi 
        graphLayout.getForceSimulator().getForces()[2].setParameter(1, 150F);
        layout.add(graphLayout);
        layout.add(new RepaintAction());
        this.putAction(LAYOUT_ACTION, layout);
        addRepaintAction();
        addSearch();

    }
    /**
     * 
     * @return Panel ustawień wizualizacji
     */
    public JPanel getControlPanel(){
        prefuse.action.Action action = this.getAction(OVVisualization.LAYOUT_ACTION);
        ForceSimulator fsim = ((ForceDirectedLayout) ((ActionList) action).get(0)).getForceSimulator();
        return new JForcePanel(fsim);
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
