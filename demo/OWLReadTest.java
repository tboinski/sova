/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prefusetest;

import java.net.URI;
import java.util.Set;
import javax.swing.JFrame;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.OWLXMLOntologyFormat;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyStorageException;
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
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 *
 * @author infinity
 */
public class OWLReadTest
{

	protected static Graph graph = null;
	protected static JFrame frame;
	protected static OWLOntology ontology;

	public static void main(String[] args)
	{
		graph = new Graph();
		//graph.
		OWLAPIFunction();
		prefuseFunction();

	}


	public static void rekReadSubclass(Node parent, OWLClass cls ){

		for (OWLDescription sub : cls.getSubClasses(ontology)) {
			Node n = graph.addNode();

			n.set("label", sub.toString());
			Edge e = graph.addEdge(parent, n);
			System.out.println("subclass "+ sub.toString() );
			rekReadSubclass(n, sub.asOWLClass() );

		}


	}

	public static void OWLAPIFunction()
	{

		try {
			// A simple example of how to load and save an ontology
			// We first need to obtain a copy of an OWLOntologyManager, which, as the
			// name suggests, manages a set of ontologies.  An ontology is unique within
			// an ontology manager.  To load multiple copies of an ontology, multiple managers
			// would have to be used.
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			// We load an ontology from a physical URI - in this case we'll load the pizza
			// ontology.
			URI physicalURI = URI.create("file:/home/infinity/Projekty/svn/ocs_visualisation/onot-test.owl");
			// Now ask the manager to load the ontology
		 ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
			// Print out all of the classes which are referenced in the ontology


			graph.addColumn("label",String.class );
			Node thing = graph.addNode();
			thing.set("label", "Thing");
			for (OWLClass cls : ontology.getReferencedClasses()){
				System.out.println(cls);
				if(cls.getSuperClasses(ontology).isEmpty()==true){ //Thing jest superklasa
					Node n = graph.addNode();
					n.set("label", cls.toString());
					Edge e = graph.addEdge(thing, n);
					System.out.println(cls.toString());
					rekReadSubclass(n, cls );

				}else{
					for (OWLDescription sup : cls.getSuperClasses(ontology)) {
						System.out.println("superclass "+sup);
					}
				}
				//for (OWLDescription sub : cls.getSubClasses(ontology)) {

				//}
				
			}
			// Now save a copy to another location in OWL/XML format (i.e. disregard the
			// format that the ontology was loaded in).
			// (To save the file on windows use a URL such as  "file:/C:\\windows\\temp\\MyOnt.owl")
			//URI physicalURI2 = URI.create("file:/tmp/MyOnt2.owl");
		//	manager.saveOntology(ontology, new OWLXMLOntologyFormat(), physicalURI2);
			// Remove the ontology from the manager
		//

			manager.removeOntology(ontology.getURI());
		} catch (OWLOntologyCreationException e) {
			System.out.println("The ontology could not be created: " + e.getMessage());
		//} catch (OWLOntologyStorageException e) {
		//	System.out.println("The ontology could not be saved: " + e.getMessage());
		}


	}

	public static void prefuseFunction()
	{
		//try {
		//	graph = new GraphMLReader().readGraph("socialnet.xml");
		//} catch (DataIOException e) {
	//		e.printStackTrace();
	//		System.err.println("Error loading graph. Exiting...");
	//		System.exit(1);
	//	}

		// add the graph to the visualization as the data group "graph"
// nodes and edges are accessible as "graph.nodes" and "graph.edges"
		System.out.println(graph.getNodeCount());
		Visualization vis = new Visualization();
		vis.add("graph", graph);

// draw the "name" label for NodeItems
		LabelRenderer r = new LabelRenderer("label");
		r.setRoundedCorner(8, 8); // round the corners

// create a new default renderer factory
// return our name label renderer as the default for all non-EdgeItems
// includes straight line edges for EdgeItems by default
		vis.setRendererFactory(new DefaultRendererFactory(r));

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
			VisualItem.STROKECOLOR, ColorLib.gray(200));

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

// create a new Display that pull from our Visualization
		Display display = new Display(vis);
		display.setSize(720, 500); // set display size
		display.addControlListener(new DragControl()); // drag items around
		display.addControlListener(new PanControl());  // pan with background left-drag
		display.addControlListener(new ZoomControl()); // zoom with vertical right-drag

// create a new window to hold the visualization
		frame = new JFrame("prefuse example");
// ensure application exits when window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(display);
		frame.pack();           // layout components in window
		frame.setVisible(true); // show the window

		vis.run("color");  // assign the colors
		vis.run("layout"); // start up the animated layout


	}
}
