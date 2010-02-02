package org.eti.kask.sova.visualization;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eti.kask.sova.graph.OWLtoGraphConverter;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.Display;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Display wizualizowanej ontologii. Pozwala na generowanie graficznej
 * reprezentacji ontologii na podstawie podanego obiektu ontologii.
 * @author Piotr Kunowski
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
        //this.addControlListener(new FocusControl(1, OVVisualization.LAYOUT_ACTION));
        this.addControlListener(new FocusControl(1));
        this.addControlListener(new ZoomToFitControl());



    }

    /**
     * metoda wizualizuje zadaną ontologię
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateGraphFromOWl(OWLOntology ont) {
        try {
            this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));
            visualization = getGraphLayoutVis();
            visualization.add("graph", this.getGraph());
            visualization.setVisualizationSettings();
            this.setVisualization(visualization);
            visualization.startLayout();
        } catch (Exception ex) {
            Logger.getLogger(OVDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Ponowne wczytanie zmiennych wizualizacji
     */
    public void refreshVisualization() {
        visualization.reset();
        visualization = getGraphLayoutVis();
        visualization.add("graph", this.getGraph());
        visualization.setVisualizationSettings();
        visualization.repaint();
        this.setVisualization(visualization);
        this.repaint();
        //this.getVisualization().startDistanceFilter();
//        this.getVisualization().startLayout();

    }
}
