
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
import prefuse.controls.ZoomControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;



// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.8EA72EAB-93D1-E28C-37EF-04248F715E57]
// </editor-fold> 
public class OVDisplay extends Display {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.60BB3D24-55C8-3947-A223-ECCCD5B32904]
    // </editor-fold> 
    private Graph graph;
   // private Visualization visualization;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.DB4FD1B1-0319-3FFE-BFBB-7279878EB829]
    // </editor-fold> 
    public OVDisplay () {
	super();
	Visualization vis = new Visualization();
	this.visualizationSettings( vis);
	this.setVisualization(vis);
	graph= new Graph();

	//przykladowe defaultowe ustawienia
	this.addControlListener(new DragControl()); // drag items around
	this.addControlListener(new PanControl());  // pan with background left-drag
	this.addControlListener(new ZoomControl()); // zoom with vertical right-drag





    }

    public OVDisplay (OWLOntology ont) {
	    super();


    }



    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.EB7212E7-00EC-CB47-1E51-232066FB0843]
    // </editor-fold> 
    public Graph getGraph () {
        return graph;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.71E94C39-3C78-6321-602D-B20C8114ADD0]
    // </editor-fold> 
    public void setGraph (Graph val) {
        this.graph = val;
    }




    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.9EE67031-8AEC-4098-FA0E-A96B20AE4820]
    // </editor-fold> 
    public void generateGraphFromOWl (OWLOntology ont) {

	    this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));
	    Visualization vis =this.getVisualization();
	    vis.add("graph", this.getGraph());
	    this.visualizationSettings(vis);
    }



    public void visualizationSettings(Visualization vis){
	    ///////////
	//to trzeba umiescic w kodzie gdzie indziej

	//////////
	//Visualization vis = this.getVisualization();



// draw the "name" label for NodeItems
		org.eti.kask.sova.nodes.ThingNode t = new ThingNode();
		Debug.sendMessage(t.toString());
		LabelRenderer r = (LabelRenderer)new NodeRenderer("node");
		r.setRoundedCorner(8, 8); // round the corners

// create a new default renderer factory
// return our name label renderer as the default for all non-EdgeItems
// includes straight line edges for EdgeItems by default
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
		DataColorAction fill = new DataColorAction("graph.nodes", "gender",
			Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
// use black for node text
		ColorAction text = new ColorAction("graph.nodes",
			VisualItem.TEXTCOLOR, ColorLib.gray(0));
// use light grey for edges
		ColorAction edges = new ColorAction("graph.edges",
			VisualItem.STROKECOLOR, ColorLib.gray(1));

// create an action list containing all color assignments
		ActionList color = new ActionList();
		color.add(fill);
		color.add(text);
		color.add(edges);

// create an action list with an animated layout
// the INFINITY parameter tells the action list to run indefinitely
		ActionList layout = new ActionList(Activity.INFINITY);
		layout.add(new ForceDirectedLayout("graph"));
		layout.add(new RepaintAction());

// add the actions to the visualization
		vis.putAction("color", color);
		vis.putAction("layout", layout);

		vis.run("color");  // assign the colors
		vis.run("layout"); // start up the animated layout
		//this.setVisualization(vis);
    }

   

}