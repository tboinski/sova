package org.eti.kask.sova.visualization;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eti.kask.sova.graph.Constants;
import org.eti.kask.sova.graph.OWLtoGraphConverter;
import org.eti.kask.sova.graph.OWLtoHierarchyTreeConverter;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.util.display.DebugStatsPainter;
import prefuse.util.display.PaintListener;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
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
    private OVVisualization visualizationForceDirected = null;
    private OVVisualization visualizationRadialGraph = null;

    private OVVisualization getGraphLayoutVis() {

        switch (graphLayout) {
            case 1: 
            	if (visualizationForceDirected==null){
            		initForceDirectedVis();
            	}
                return visualizationForceDirected;
            case 2:
            	if (visualizationRadialGraph==null){
            		initRadialGraphVis();
            	}
                return visualizationRadialGraph;
        }
    	if (visualizationForceDirected==null){
    		initForceDirectedVis();
    	}
        return visualizationForceDirected;
    }
    private void initForceDirectedVis(){
    	visualizationForceDirected = new ForceDirectedVis();
    	visualizationForceDirected = getGraphLayoutVis();
        VisualGraph visualGraph =  visualizationForceDirected.addGraph(Constants.GRAPH, this.getGraph());
        visualizationForceDirected.setVisualizationSettings();
		// ustawienie podswietlonej klasy 
		if(visualGraph.getNodeCount() > 0) {
			VisualItem currentClass = (VisualItem) visualGraph.getNode(0);
			visualizationForceDirected.getGroup(Visualization.FOCUS_ITEMS).setTuple(currentClass);
			currentClass.setFixed(true);
		}
		
    }
    
	private void initRadialGraphVis() {
		visualizationRadialGraph = new RadialGraphVis();
		visualizationRadialGraph = getGraphLayoutVis();
		visualizationRadialGraph.addGraph( Constants.GRAPH, this.getGraph());
		visualizationRadialGraph.setVisualizationSettings();

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
        return getGraphLayoutVis();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public OVDisplay() {
        super();
        this.setSize(1500, 1500);
        this.setHighQuality(true);
        PaintListener m_debug  = new DebugStatsPainter();
        addPaintListener(m_debug);
        graph = new Graph();
//        this.setItemSorter(new TreeDepthItemSorter());
        this.addControlListener(new DragControl()); // drag items around
        this.addControlListener(new PanControl());  // pan with background left-drag
        this.addControlListener(new ZoomControl()); // zoom with vertical right-drag
        this.addControlListener(new WheelZoomControl());
        this.addControlListener(new NeighborHighlightControl());
        this.addControlListener(new ZoomToFitControl());
		addControlListener(new FocusControl(1, RadialGraphVis.FILTERS));
		addControlListener(new HoverActionControl(OVVisualization.REPAINT_ACTION));
       
    }

    /**
     * metoda wizualizuje zadaną ontologię
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateGraphFromOWl(OWLOntology ont) {
        try {       	
            this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));
            OVVisualization vis = getGraphLayoutVis();
            this.setVisualization(vis);
            vis.startLayout();           

        } catch (Exception ex) {
            Logger.getLogger(OVDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    /**
     * metoda generuje drzewo wnioskowania dla zadanej obiektu owl
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateTreeFromOWl(OWLOntology ont) {
        try {       	
           // this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));
            visualizationForceDirected = new OVNodeLinkTreeLayout();
             visualizationForceDirected.add(Constants.TREE, OWLtoHierarchyTreeConverter.getInstance().OWLtoTree(ont));
            
            visualizationForceDirected.setVisualizationSettings();
            this.setVisualization(visualizationForceDirected);
            setSize(700,600);
            setItemSorter(new TreeDepthItemSorter());
            addControlListener(new ZoomToFitControl());
            addControlListener(new ZoomControl());
            addControlListener(new WheelZoomControl());
            addControlListener(new PanControl());
        } catch (Exception ex) {
            Logger.getLogger(OVDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Zmiana algorytmu wizualizacji
     * @param visLayout
     */
    public void changeVisualizationLayout(int visLayout){
        this.setGraphLayout(visLayout);
    	this.removeAll();
        this.setVisualization(getGraphLayoutVis());
        this.repaint();
        this.getVisualization().refreshFilter();
    }

    
    
}
