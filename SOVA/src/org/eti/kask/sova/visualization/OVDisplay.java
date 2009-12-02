package org.eti.kask.sova.visualization;

import org.eti.kask.sova.graph.OWLtoGraphConverter;
import org.eti.kask.sova.nodes.ThingNode;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.force.ForceSimulator;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * 
 */
public class OVDisplay extends Display
{
    public static final int FORCE_DIRECTED_LAYOUT =  1;
    private int  graphLayout = FORCE_DIRECTED_LAYOUT;
    private static final String  GRAPH = "graph";
    private ActionList layout = null;
    public  ForceSimulator getForceSimulator(){
       return  ((ForceDirectedLayout)layout.get(0)).getForceSimulator();

    }
    public Layout getGraphLayout() {

        switch (graphLayout) {
            case 1 : return new ForceDirectedLayout(GRAPH);
        }

        return new ForceDirectedLayout(GRAPH);
    }
    /**
     * 
     * @param graphLayout
     */
    public void setGraphLayout(int graphLayout) {
        this.graphLayout = graphLayout;
    }
    private static final String LAYOUT_ACTION = "layout";
    private Visualization vis;
    private  GraphDistanceFilter filter;
	/**
	 *
	 */
	private Graph graph;
	// private Visualization visualization;

	/**
	 *
	 */
	public OVDisplay()
	{
		super();
        graph = new Graph();
      
		vis = new Visualization();
		this.visualizationSettings(vis);
		this.setVisualization(vis);
		

		//przykladowe defaultowe ustawienia
		this.addControlListener(new DragControl()); // drag items around
		this.addControlListener(new PanControl());  // pan with background left-drag
		this.addControlListener(new ZoomControl()); // zoom with vertical right-drag
        this.addControlListener(new WheelZoomControl());
        this.addControlListener(new NeighborHighlightControl());

	}

    /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
    public void setDistance(int distance){
        filter.setDistance(distance);
    }
    public int getDistance(int distance){
        return filter.getDistance();
    }
    public void stopLayout(){
         vis.cancel(LAYOUT_ACTION);

    }
    /**
     * funkcja wyłączająca samorozmieszczanie - grawitację obiektów
     */
    public void startLayout(){
        vis.run(LAYOUT_ACTION);
    }

	/**
	 *
	 */
	public Graph getGraph()
	{
		return graph;
	}

	/**
	 *
	 */
	public void setGraph(Graph val)
	{
		this.graph = val;
	}

	/**
	 *
	 */
	public void generateGraphFromOWl(OWLOntology ont)
	{

		this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraphExperimental(ont));

		vis.add("graph", this.getGraph());
        this.startLayout();
		
	}
    /**
     * Ponowne wczytanie zmiennych wizualizacji
     */
    public void refreshVisualization(){
       vis.reset();
       this.visualizationSettings(vis);
       this.setVisualization(vis);
       this.repaint();

    }

	public void visualizationSettings(Visualization vis)
	{
        filter = new GraphDistanceFilter(GRAPH,12);
		org.eti.kask.sova.nodes.ThingNode t = new ThingNode();
		LabelRenderer r = (LabelRenderer) new NodeRenderer("node");
		EdgeRenderer er = new EdgeRenderer();
		DefaultRendererFactory drf = new DefaultRendererFactory(r);
		drf.add(new InGroupPredicate("graph.edges"), er);
		vis.setRendererFactory(drf);


        int hops = 30;
        final GraphDistanceFilter filter = new GraphDistanceFilter("graph", hops);

//        ColorAction fill = new ColorAction(nodes,
//                VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
//        fill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
//        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));

        ActionList draw = new ActionList();
        draw.add(filter);
//        draw.add(fill);
        draw.add(new ColorAction("node", VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction("node", VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
        draw.add(new ColorAction("graph.edges", VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction("graph.edges", VisualItem.STROKECOLOR, ColorLib.gray(200)));



// create an action list with an animated layout
// the INFINITY parameter tells the action list to run indefinitely
		layout = new ActionList(Activity.INFINITY);
        //layout.add(filter);
		layout.add(new ForceDirectedLayout("graph"));
		layout.add(new RepaintAction());

// add the actions to the visualization
        vis.putAction("draw", draw);
		vis.putAction(LAYOUT_ACTION, layout);
        vis.runAfter("draw", "layout");
        
	}
}