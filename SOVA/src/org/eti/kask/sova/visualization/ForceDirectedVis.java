package org.eti.kask.sova.visualization;

import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;

/**
 * Klasa wizualizujące grafy w oparciu o algorytm ForceDirected
 * @author piotr29
 */
public class ForceDirectedVis extends OVVisualization {

    /**
     * ustawienie vizualizacji
     */
    @Override
    void setVisualizationLayout() {
        ForceDirectedLayout graphLayout =  new ForceDirectedLayout(GRAPH);
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(graphLayout);
        layout.add(new RepaintAction());
        this.putAction(LAYOUT_ACTION, layout);

    }

}
