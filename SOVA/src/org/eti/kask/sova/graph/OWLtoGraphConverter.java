
package org.eti.kask.sova.graph;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import org.eti.kask.sova.utils.Debug;
import prefuse.data.Table;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.7A3733DC-8F75-9D47-51C1-30AEC658BD96]
// </editor-fold> 
public class OWLtoGraphConverter
{

	private static final OWLtoGraphConverter INSTANCE = new OWLtoGraphConverter();

	private Table edges;

	// Private constructor prevents instantiation from other classes
	private OWLtoGraphConverter()
	{
	}
	// <editor-fold defaultstate="collapsed" desc=" UML Marker ">
	// #[regen=yes,id=DCE.12AC91E1-284B-9651-3544-3BC192F58A3B]
	// </editor-fold>

	public static OWLtoGraphConverter getInstance()
	{
		return INSTANCE;
	}

	// <editor-fold defaultstate="collapsed" desc=" UML Marker ">
	// #[regen=yes,id=DCE.4FCE5BE4-CCD1-8D03-913C-6F9B2FF8B14A]
	// </editor-fold>
	/**
	 * Zamienia ontologię na graf z biblioteki prefuse.
	 * @param ontology Ontologia w formacie OWLOntology z OWL API.
	 * @return prefuse.data.Graph odzwierciedlający podaną ontologię.
	 */
	public Graph OWLtoGraph(OWLOntology ontology)
	{
		// Tworzymy nowy graf
		Graph graph = new Graph();
		graph.addColumn( "node", org.eti.kask.sova.nodes.Node.class );

		// Dodajemy węzeł Thing
		Node thing = graph.addNode();
		org.eti.kask.sova.nodes.Node t = new org.eti.kask.sova.nodes.ThingNode();
		thing.set( "node", t );

		edges = graph.getEdgeTable();		
		//edges.addColumn("DEFAULT_SOURCE_KEY", Integer.class);
		//edges.addColumn("DEFAULT_TARGET_KEY", Integer.class);
		//edges.addColumn("DEFAULT_NODE_KEY", Integer.class);
		edges.addColumn("edge", org.eti.kask.sova.edges.Edge.class);

		for (OWLClass cls : ontology.getReferencedClasses()) {
			Debug.sendMessage( cls.toString() );

			if (cls.getSuperClasses(ontology).isEmpty() == true) { //Thing jest superklasa
				Node n = graph.addNode();
				org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.ClassNode();
				node.setLabel(cls.toString());
				n.set("node", node);

				//graph.addEdge(thing, n);
				int row = edges.addRow();
				edges.set(row, "source", thing.getRow());
				edges.set(row, "target", n.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );


				Debug.sendMessage( cls.toString() );
				recursiveSubClassReader(n, cls, ontology);

			}
		}

		graph.setEdgeTable(edges);

		return graph;
	}


	public void recursiveSubClassReader(Node parent, OWLClass cls,OWLOntology ontology ){

		for (OWLDescription sub : cls.getSubClasses( ontology)) {
			Node n = parent.getGraph().addNode();
			org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.ClassNode();
			node.setLabel(sub.toString());
			n.set("node", node);

			//Edge e = parent.getGraph().addEdge(parent, n);
			int row = edges.addRow();
			edges.set(row, "source", parent.getRow());
			edges.set(row, "target", n.getRow());
			edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );

			Debug.sendMessage("subclass "+ sub.toString() );
			recursiveSubClassReader(n, sub.asOWLClass(), ontology );

		}


	}

}
