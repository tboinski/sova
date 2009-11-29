package org.eti.kask.sova.visualization;

import org.eti.kask.sova.graph.OWLtoGraphConverter;
import org.eti.kask.sova.nodes.ThingNode;
import org.eti.kask.sova.utils.Debug;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * 
 */
public class OVDisplay extends Display
{
    private static final String LAYOUT_ACTION = "layout";
    private Visualization vis;
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
		vis = new Visualization();
		this.visualizationSettings(vis);
		this.setVisualization(vis);
		graph = new Graph();

		//przykladowe defaultowe ustawienia
		this.addControlListener(new DragControl()); // drag items around
		this.addControlListener(new PanControl());  // pan with background left-drag
		this.addControlListener(new ZoomControl()); // zoom with vertical right-drag
        this.addControlListener(new WheelZoomControl());





	}
    /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
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

		this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));

		vis.add("graph", this.getGraph());
        this.startLayout();
		
	}

	public void visualizationSettings(Visualization vis)
	{
		org.eti.kask.sova.nodes.ThingNode t = new ThingNode();
		Debug.sendMessage(t.toString());
		LabelRenderer r = (LabelRenderer) new NodeRenderer("node");


		EdgeRenderer er = new EdgeRenderer();
		DefaultRendererFactory drf = new DefaultRendererFactory(r);
		drf.add(new InGroupPredicate("graph.edges"), er);


		vis.setRendererFactory(drf);

// create our nominal color palette
// pink for females, baby blue for males
		int[] palette = new int[]{
			ColorLib.rgb(255, 180, 180), ColorLib.rgb(190, 190, 255)
		};
// map nominal data values to colors using our provided palette
		/*DataColorAction fill = new DataColorAction("graph.nodes", "gender",
			Constants.NOMINAL, VisualItem.FILLCOLOR, palette);*/
// use black for node text
		/*ColorAction text = new ColorAction("graph.nodes",
			VisualItem.TEXTCOLOR, ColorLib.gray(0));*/
// use light grey for edges
		/*ColorAction edges = new ColorAction("graph.edges",
			VisualItem.STROKECOLOR, ColorLib.gray(1));*/

// create an action list containing all color assignments
		/*ActionList color = new ActionList();
		color.add(fill);
		color.add(text);
		color.add(edges);*/

// create an action list with an animated layout
// the INFINITY parameter tells the action list to run indefinitely
		ActionList layout = new ActionList(Activity.INFINITY);
		layout.add(new ForceDirectedLayout("graph"));
		layout.add(new RepaintAction());

// add the actions to the visualization
		//vis.putAction("color", color);
		vis.putAction(LAYOUT_ACTION, layout);
		vis.run("color");  // assign the colors

	}
}