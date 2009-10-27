
package org.eti.kask.sova.graph;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import org.eti.kask.sova.utils.Debug;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.util.OWLAxiomVisitorAdapter;
import prefuse.data.Table;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.7A3733DC-8F75-9D47-51C1-30AEC658BD96]
// </editor-fold> 
public class OWLtoGraphConverter
{

	private static final OWLtoGraphConverter INSTANCE = new OWLtoGraphConverter();

	private Table edges;
	private Table nodes;

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

	
	/**
	 * Zamienia ontologię na graf z biblioteki prefuse.
	 * @param ontology Ontologia w formacie OWLOntology z OWL API.
	 * @return prefuse.data.Graph odzwierciedlający podaną ontologię.
	 */
	public Graph OWLtoGraph(OWLOntology ontology)
	{
		// Tworzymy nowy graf
		Graph graph = new Graph();
		nodes = graph.getNodeTable();
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

		// Wczytanie wszystkich klas i ich podklas
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

		disjointEdgeReader(ontology);

		graph.setEdgeTable(edges);

		return graph;
	}


	public void recursiveSubClassReader(Node parent, OWLClass cls,OWLOntology ontology )
	{

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

	/**
	 * Zaznacza związki OWL Disjoint pomiędzy klasami z ontologii na grafie
	 * w postaci krawędzi. <br/>
	 * Wymaga zainicjalizowanych zmiennych "edges" oraz "nodes"!
	 * @param ontology ontologia źródłowa
	 */
	protected void disjointEdgeReader(OWLOntology ontology)
	{
		
		for (OWLClass cls : ontology.getReferencedClasses()) {
			for (OWLDisjointClassesAxiom dca : ontology.getDisjointClassesAxioms(cls)) {
				// Debug.sendMessage(dca.toString());
				// format tekstu: "DisjointClasses( K1 K2 )"
				String[] disjointClasses = dca.toString().split(" ");

				// Wyszukanie wezlow w tablicy - nieoptymalne
				// można utworzyć dodatkowe indeksy w celu przyspieszenia				
				int c1RowNo = -1, c2RowNo = -1;
				for (int i = 0; i < nodes.getRowCount(); i++ ) {

					if (nodes.get(i, "node").toString().equals(disjointClasses[1])) {
						c1RowNo = i;
					}

					if (nodes.get(i, "node").toString().equals(disjointClasses[2])) {
						c2RowNo = i;
					}

					if (c1RowNo != -1 && c2RowNo != -1) break;
				}

				if (c1RowNo == -1 || c2RowNo == -1) {
					Debug.sendMessage("Nie odnaleziono węzła " + disjointClasses[1] +"lub"+ disjointClasses[2]);
					continue;
				}

				/* Ponieważ związki wczytane z ontologii są podwojone
				 * (każdy związek pomiędzy 2 klasami występuje przy obu klasach),
				 * potrzebny jest poniższy warunek, aby wyeliminować ten problem.
				 */
				if (c1RowNo < c2RowNo) {
					int row = edges.addRow();
					edges.set(row, "source", c1RowNo);
					edges.set(row, "target", c2RowNo);
					edges.set(row, "edge", new org.eti.kask.sova.edges.DisjointEdge());
				}

			}

		}
	}

}
