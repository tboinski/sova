
package org.eti.kask.sova.graph;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import org.eti.kask.sova.utils.Debug;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.7A3733DC-8F75-9D47-51C1-30AEC658BD96]
// </editor-fold> 
public class OWLtoGraphConverter
{

	private static final OWLtoGraphConverter INSTANCE = new OWLtoGraphConverter();

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
		graph.addColumn( "label", String.class );

		// Dodajemy węzeł Thing
		Node thing = graph.addNode();
		thing.set( "label", "Thing" );


		for (OWLClass cls : ontology.getReferencedClasses()) {
			Debug.sendMessage( cls.toString() );

			if (cls.getSuperClasses(ontology).isEmpty() == true) { //Thing jest superklasa
				Node n = graph.addNode();
				n.set("label", cls.toString());
				graph.addEdge(thing, n);
				Debug.sendMessage( cls.toString() );
				recursiveSubClassReader(n, cls, ontology);

			}
		}

		return graph;
	}


	public static void recursiveSubClassReader(Node parent, OWLClass cls,OWLOntology ontology ){

		for (OWLDescription sub : cls.getSubClasses( ontology)) {
			Node n = parent.getGraph().addNode();

			n.set("label", sub.toString());
			Edge e = parent.getGraph().addEdge(parent, n);
			System.out.println("subclass "+ sub.toString() );
			recursiveSubClassReader(n, sub.asOWLClass(), ontology );

		}


	}

}


package org.eti.kask.sova.graph;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.7A3733DC-8F75-9D47-51C1-30AEC658BD96]
// </editor-fold> 
public class OWLtoGraphConverter
{

	private static final OWLtoGraphConverter INSTANCE = new OWLtoGraphConverter();

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
	public Graph OWLtoGraph(OWLOntology ontology)
	{
		Graph graph = new Graph();
		graph.addColumn("label", String.class);
		Node thing = graph.addNode();
		thing.set("label", "Thing");
		for (OWLClass cls : ontology.getReferencedClasses()) {
			System.out.println(cls);
			if (cls.getSuperClasses(ontology).isEmpty() == true) { //Thing jest superklasa
				Node n = graph.addNode();
				n.set("label", cls.toString());
				Edge e = graph.addEdge(thing, n);
				System.out.println(cls.toString());
				recursiveSubClassReader(n, cls, ontology);

			}
		}

		return null;
	}


	public static void recursiveSubClassReader(Node parent, OWLClass cls,OWLOntology ontology ){

		for (OWLDescription sub : cls.getSubClasses( ontology)) {
			Node n = parent.getGraph().addNode();

			n.set("label", sub.toString());
			Edge e = parent.getGraph().addEdge(parent, n);
			System.out.println("subclass "+ sub.toString() );
			recursiveSubClassReader(n, sub.asOWLClass(), ontology );

		}


	}

}


