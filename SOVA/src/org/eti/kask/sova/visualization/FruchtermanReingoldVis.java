package org.eti.kask.sova.visualization;

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
