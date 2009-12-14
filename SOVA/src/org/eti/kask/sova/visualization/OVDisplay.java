package org.eti.kask.sova.visualization;

import org.eti.kask.sova.graph.OWLtoGraphConverter;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.Display;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * 
 * @author piotr29
 */
public class OVDisplay extends Display {

    public static final int FORCE_DIRECTED_LAYOUT = 1;
    public static final int RADIAL_TREE_LAYOUT = 2;
    private int graphLayout = FORCE_DIRECTED_LAYOUT;
    private Graph graph = null;
    private OVVisualization visualization;

    private OVVisualization getGraphLayoutVis() {

        switch (graphLayout) {
            case 1:
                return new ForceDirectedVis();
            case 2:
                return new RadialGraphVis();
        }
        return new ForceDirectedVis();
    }

    /**
     * Ustawienie trybu wyświetlania
     * @param graphLayout
     */
    public void setGraphLayout(int graphLayout) {
        this.graphLayout = graphLayout;
    }

    public int getGraphLayout() {
        return graphLayout;
    }

    @Override
    public OVVisualization getVisualization() {
        return visualization;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public OVDisplay() {
        super();
        graph = new Graph();
        visualization = new ForceDirectedVis();
        this.setItemSorter(new TreeDepthItemSorter());
        this.addControlListener(new DragControl()); // drag items around
        this.addControlListener(new PanControl());  // pan with background left-drag
        this.addControlListener(new ZoomControl()); // zoom with vertical right-drag
        this.addControlListener(new WheelZoomControl());
        this.addControlListener(new NeighborHighlightControl());
        this.addControlListener(new FocusControl(1, OVVisualization.LAYOUT_ACTION));

    }

    /**
     * metoda wizualizuje zadaną ontologię
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateGraphFromOWl(OWLOntology ont) {

        this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));
        visualization = getGraphLayoutVis();
        visualization.add("graph", this.getGraph());
        visualization.setVisualizationSettings();
        this.setVisualization(visualization);
        visualization.startLayout();

    }

    /**
     * Ponowne wczytanie zmiennych wizualizacji
     */
    public void refreshVisualization() {
        visualization.reset();
        visualization = getGraphLayoutVis();
        visualization.add("graph", this.getGraph());
        visualization.setVisualizationSettings();
        this.setVisualization(visualization);
        this.repaint();
        this.getVisualization().startLayout();

    }
}
