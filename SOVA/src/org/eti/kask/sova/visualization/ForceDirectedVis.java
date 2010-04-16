package org.eti.kask.sova.visualization;

import javax.swing.JPanel;
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

    /**
     * ustawienie vizualizacji
     */
    @Override
    void setVisualizationLayout() {

        ForceDirectedLayout graphLayout =  new ForceDirectedLayout(GRAPH);
        ActionList layout = new ActionList(Activity.INFINITY);
        //ustawienie długości krawędzi 
        graphLayout.getForceSimulator().getForces()[2].setParameter(1, 100F);
        layout.add(graphLayout);
        layout.add(new RepaintAction());
        this.putAction(LAYOUT_ACTION, layout);


    }
    /**
     * Zwraca panel ustawień wizualizacji.
     * @return
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
        this.setDistanceFilter();

    }
}
