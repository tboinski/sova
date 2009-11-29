
package org.eti.kask.sova.graph;


import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import org.eti.kask.sova.utils.Debug;
import org.semanticweb.owl.model.AxiomType;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLIndividualAxiom;
import org.semanticweb.owl.model.OWLLabelAnnotation;
import org.semanticweb.owl.model.OWLLogicalAxiom;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
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
        * Umieszcza w grafie wszystkie klasy nie-anonimowe zawarte w ontologii
        * oraz krawędzie między węzłem thing i klasami będącymi jego subclasses.
        * @param ontology
        * @param graph
        * @param thing
        * @param classes
        */
        private void insertBaseClasses(OWLOntology ontology,  Graph graph, Node thing, Hashtable<String, Integer> classes ){

                for(OWLClass cls : ontology.getReferencedClasses()){
                    //dodajemy na sucho wszystkie klasy bez krawedzi
                    //oprocz Thing
                     if(!cls.isOWLThing()){
                            Node n = graph.addNode();
                            org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.ClassNode();
                            node.setLabel(cls.toString());
                            n.set("node", node);

                            if( cls.getSuperClasses(ontology).isEmpty() == true ){ //thing jest superklasa
                                int row = edges.addRow();
				edges.set(row, "source", thing.getRow());
				edges.set(row, "target", n.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );

                            }


                            classes.put( cls.toString(),n.getRow());
                     }
                }

        }

        /**
         * Umieszcza w grafie wszystkie zdefiniowane w ontologii property
         * @param ontology
         * @param graph
         * @param properties
         */
        private void insertBaseProperties(OWLOntology ontology,  Graph graph,  Hashtable<String, Integer> properties){
             //dodajemy wszystkie definicje property
                 for( OWLObjectProperty property :  ontology.getReferencedObjectProperties()  ){

                            Node n = graph.addNode();
                            org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.PropertyNode();
                            node.setLabel(property.toString());
                            n.set("node", node);
                            properties.put( property.toString(),n.getRow());

                 }


        }

        /**
         * umieszcze w grafie węzły typu individual w grafie
         * @param ontology
         * @param graph
         * @param individuals
         */
            private void insertBaseIndividuals(OWLOntology ontology,  Graph graph,  Hashtable<String, Integer> individuals){
             //dodajemy wszystkie definicje individuals
                 for( OWLIndividual individual :  ontology.getReferencedIndividuals()  ){

                            Node n = graph.addNode();
                            org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.IndividualNode();
                            node.setLabel(individual.toString());
                            n.set("node", node);
                            individuals.put( individual.toString(),n.getRow());

                 }
        }


        /**
         * Umieszcza w grafie krawedzie zwiane z individualami
         * @param ontology
         * @param graph
         * @param individuals
         * @param classes
         */
         private void insertBasicEdgesForIndividuals(OWLOntology ontology,  Graph graph, Hashtable<String, Integer> individuals,Hashtable<String, Integer> classes ){

             for( OWLIndividual individual :  ontology.getReferencedIndividuals()  ){
                    Debug.sendMessage("Axiomy individuala : " +individual.toString());
                     for(OWLAxiom axiom : ontology.getAxioms(individual)){ //przegladamy axiomy powiazane z dana klasa
                       Debug.sendMessage(axiom.toString());
                        if(axiom.getAxiomType() == AxiomType.CLASS_ASSERTION){ //individual jest instacja klasy

                           Set<OWLEntity> entities =  axiom.getReferencedEntities();

                           //TODO: sprawdzenie zachowania gdy przegladamy pare w axiomie roznego typu


                           Object [] arr = entities.toArray();
                           Debug.sendMessage("Subclass entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + individuals.get(arr[1].toString()) );
                           if(classes.get(arr[0].toString()) != null  &&  individuals.get(arr[1].toString()) != null){ //klasa nie jest subklasa anonimowej encji
                                    int row = edges.addRow();
                                    edges.set(row, "source", individuals.get(arr[1].toString()));
                                   edges.set(row, "target", classes.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );
                           } else if(individuals.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){
                                int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                   edges.set(row, "target", individuals.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );

                        }

                       } else if(axiom.getAxiomType() == AxiomType.DIFFERENT_INDIVIDUALS){
                           //TODO: diffrent individuals

                           
                       }

                    }
             }


       }

        /**
         * Umieszcza w grafie krawędzie łączące klasy typów : Subclass, disjoint, equivalent
         * @param ontology
         * @param graph
         * @param classes
         */
        private void insertBasicEdgesForClasses(OWLOntology ontology,  Graph graph, Hashtable<String, Integer> classes){

               //majac wszystkie klasy reprezentowane na grafie dodajemy krawedzie miedzy nimi wedlug podanych axiomow
                 for(OWLClass cls : ontology.getReferencedClasses()){
                   Debug.sendMessage("Axiomy klasy:" +cls.toString());
                     for(OWLAxiom axiom : ontology.getAxioms(cls)){ //przegladamy axiomy powiazane z dana klasa
                       Debug.sendMessage(axiom.toString());
                       if(axiom.getAxiomType() == AxiomType.SUBCLASS){

                           Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("Subclass entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(classes.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest subklasa anonimowej encji
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                   edges.set(row, "target", classes.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );
                           }

                       } else if(axiom.getAxiomType() == AxiomType.DISJOINT_CLASSES){

                           Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("SDisjoint entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(classes.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest disjoint z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                   edges.set(row, "target", classes.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.DisjointEdge() );
                           }
                       } else if (axiom.getAxiomType() == AxiomType.EQUIVALENT_CLASSES){
                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("SDisjoint entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(classes.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest equivalent z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                    edges.set(row, "target", classes.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.EquivalentEdge() );
                           }


                       }

                     }

                }



        }

        /**
         * Umieszcza w grafie krawędzie łączące property i klasy typów : inverse property, range, domain
         * @param ontology
         * @param graph
         * @param properties
         * @param classes
         */
        private void insertBasicEdgesForProperties(OWLOntology ontology,  Graph graph,  Hashtable<String, Integer> properties, Hashtable<String, Integer> classes){

             for( OWLObjectProperty property :  ontology.getReferencedObjectProperties()  ){
                 Debug.sendMessage("Axiomy property:" +property.toString());
                     for(OWLAxiom axiom : ontology.getAxioms(property)){ //przegladamy axiomy powiazane z dana klasa
                       Debug.sendMessage(axiom.toString());
                       if(axiom.getAxiomType() == AxiomType.INVERSE_OBJECT_PROPERTIES ){
                           
                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("inverse properties entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[1].toString()) );
                           if(properties.get(arr[0].toString()) != null  &&  properties.get(arr[1].toString()) != null){ //klasa nie jest equivalent z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", properties.get(arr[1].toString()));
                                    edges.set(row, "target", properties.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.InverseOfEdge() );
                           }
                           
                       }else if(axiom.getAxiomType() == AxiomType.OBJECT_PROPERTY_RANGE ){
                           
                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("inverse properties entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(properties.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest equivalent z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                    edges.set(row, "target", properties.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.RangeEdge() );
                           }

                       }else if(axiom.getAxiomType() == AxiomType.OBJECT_PROPERTY_DOMAIN ){

                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("inverse properties entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(properties.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest equivalent z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                    edges.set(row, "target", properties.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.DomainEdge() );
                           }
                       }
             }
             }

        }

        /**
         * Experymentalna metoda zamiany obiektu OWLOntology na obiekt graph biblioteki prefuse.
         * Metoda wywoluje podrzedne metody wpisujace do grafu wezly i krawedzie
         * @param ontology
         * @return Graph - graf prefuse
         */
        public Graph OWLtoGraphExperimental(OWLOntology ontology){
            Graph graph = new Graph();
            nodes = graph.getNodeTable();
	    graph.addColumn( "node", org.eti.kask.sova.nodes.Node.class );

		// Dodajemy węzeł Thing
		Node thing = graph.addNode();
		org.eti.kask.sova.nodes.Node t = new org.eti.kask.sova.nodes.ThingNode();

		thing.set( "node", t );

		edges = graph.getEdgeTable();
		edges.addColumn("edge", org.eti.kask.sova.edges.Edge.class);

                Hashtable<String, Integer> classes = new Hashtable<String, Integer>();
                Hashtable<String, Integer> properties = new Hashtable<String, Integer>();
                Hashtable<String, Integer> individuals = new Hashtable<String, Integer>();
                this.insertBaseClasses(ontology, graph, thing, classes);
                this.insertBasicEdgesForClasses(ontology, graph, classes);
                this.insertBaseProperties(ontology, graph, properties);
              //  this.insertBasicEdgesForProperties(ontology, graph, properties, classes); //nie zaimplementowane graficznie, wiec nie uruchomione
                this.insertBaseIndividuals(ontology, graph, individuals);
                this.insertBasicEdgesForIndividuals(ontology, graph, individuals, classes);


            return graph;
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
			
                   // Debug.sendMessage( "browse:" + cls.toString() );
                      if(cls.isOWLThing()){   // napotkalismy na wezel Thing zdefiniowany bezposrednio
                            //TODO: nadpisz thing z graphu thingiem z owl
                            Debug.sendMessage("oto thing " + cls.toString());
                            




                        } else if (cls.isOWLClass() && cls.getSuperClasses(ontology).isEmpty() == true  ) { //Thing jest superklasa
				Node n = graph.addNode();
				org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.ClassNode();
				node.setLabel(cls.toString());
				n.set("node", node);
				//graph.addEdge(thing, n);
				int row = edges.addRow();
				edges.set(row, "source", thing.getRow());
				edges.set(row, "target", n.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );


				Debug.sendMessage("t to tata " + cls.toString() );
				recursiveSubClassReader(n, cls, ontology);

			}

		}

		disjointEdgeReader(ontology);
	           //individualReader(graph, ontology); - zastąpiony przez:
		nodeReader(graph, ontology.getLogicalAxioms()); //lekko nadmiarowe ;/
		individualAxiomReader(graph, ontology);
		
		//Tylko do testów
		/*for (OWLAxiom a : ontology.getAxioms()) {
			System.out.println(a);
		}*/


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

			// Wczytuje potomków klasy
			recursiveSubClassReader(n, sub.asOWLClass(), ontology );

		}


	}

	protected void individualReader(Graph graph, OWLOntology ontology) {

		for (OWLLogicalAxiom la : ontology.getIndividualAxioms()) {
			Debug.sendMessage("IA: " + la.toString());
			String[] axiomParts = identifyAxiomString(la.toString());

			if (axiomParts[0].equals("ClassAssertion")) {
			
				String[] individualAxioms = axiomParts[1].split(" ");

				//Wyszukuanie węzła klasy
				int classRowNo;
				for (classRowNo = 0; classRowNo < nodes.getRowCount(); classRowNo++ ) {

					if (nodes.get(classRowNo, "node").toString().equals(individualAxioms[0])) {
						break;
					}

				}

				if (classRowNo >= nodes.getRowCount()) {
					Debug.sendMessage("Nie odnaleziono węzła " + individualAxioms[0]);
					continue;
				}

				//Dodanie węzła instancji do grafu
				Node n = graph.addNode();
				org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.IndividualNode();
				node.setLabel(individualAxioms[1]);
				n.set("node", node);

				//Dodanie krawędzi łączącej instancję z klasą
				int row = edges.addRow();
				edges.set(row, "source", classRowNo);
				edges.set(row, "target", n.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());


			}
		}
	}

	protected void individualAxiomReader(Graph graph, OWLOntology ontology) {

		for (OWLIndividualAxiom ia : ontology.getIndividualAxioms()) {
			Debug.sendMessage("IA: " + ia.toString());
			String[] axiomParts = identifyAxiomString(ia.toString());

			if (axiomParts[0].equals("DifferentIndividuals")) {

				String[] individuals = axiomParts[1].trim().split(" ");
				int[] rowNo = new int[individuals.length];
				//Oznaczenie zabezpieczające
				for (int i = 0; i < rowNo.length; i++) rowNo[i] = -1;
				/**
				 * Wyszukanie węzłów instancji (OWL Individuals),
				 * które mają być połączone związkiem różności
				 * (OWL AllDifferent) w grafie.
				 */
				int found = 0;
				for (int i = 0; found < individuals.length && i < nodes.getRowCount(); i++ ) {

					for(int j = 0; j < individuals.length; j++) {

						if (nodes.get(i, "node").toString().equals(individuals[j])) {
							rowNo[j] = i;
							found++;
							break;
						}
					}
				}

				if (found < individuals.length) {
					Debug.sendMessage("Nie znaleziono któregoś z węzłów: ");
					for (int i = 0; i < individuals.length; i++)
						Debug.sendMessage(individuals[i]);
					break;
				}

				//Dodanie węzła różności do grafu
				Node n = graph.addNode();
				org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.DifferentNode();
				node.setLabel("≠");
				n.set("node", node);

				//Połączenie węzła nierówności z instancjami
				for (int i = 0; i < individuals.length; i++) {

					if (rowNo[i] != -1) {

						int row = edges.addRow();
						edges.set(row, "source", n.getRow());
						edges.set(row, "target", rowNo[i]);
						edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());
					}
				}


			}
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
		if (cls.isAnonymous() == false) //dodane jako workaround
                    for (OWLDisjointClassesAxiom dca : ontology.getDisjointClassesAxioms(cls)) {
				Debug.sendMessage(dca.toString());
				// format tekstu: "DisjointClasses( K1 K2 )"


				String[] disjointClasses = dca.toString().split(" ");

				// Wyszukanie wezlow w tablicy - nieoptymalne
				// można utworzyć dodatkowe indeksy w celu przyspieszenia				
				int c1RowNo = -1, c2RowNo = -1;
				/*for (int i = 0; i < nodes.getRowCount(); i++ ) {
					
					if (nodes.get(i, "node").toString() == null)  {
						Debug.sendMessage("Node label is NULL: " + nodes.get(i, "node").getClass().getCanonicalName());
						continue; //Pomija nienazwany węzeł
					}

					if (nodes.get(i, "node").toString().equals(disjointClasses[1])) {
						c1RowNo = i;
					}

					if (nodes.get(i, "node").toString().equals(disjointClasses[2])) {
						c2RowNo = i;
					}

					if (c1RowNo != -1 && c2RowNo != -1) break;
				}*/

				if (c1RowNo == -1 || c2RowNo == -1) {
//					Debug.sendMessage("Nie odnaleziono węzła " + disjointClasses[1] +"lub"+ disjointClasses[2]);
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

	/**
	 * Próba zrobienia 1 funkcji wczytującej wszystkie wężły, spowodowana
	 * sporą częścią powtarzającego się kodu...
	 * @param graph
	 * @param logicalAxiomSet
	 */
	protected void nodeReader(Graph graph, Set<OWLLogicalAxiom> logicalAxiomSet) {

		for (OWLLogicalAxiom la : logicalAxiomSet) {
			Debug.sendMessage("LA: " + la.toString());
			String[] axiomParts = identifyAxiomString(la.toString());

			if (axiomParts[0].equals("ClassAssertion")
				/*|| axiomParts[0].equals("ClassAssertion")
				 * itd.
				 */) {

				String[] individualAxioms = axiomParts[1].split(" ");

				//Wyszukuanie węzła klasy
				int classRowNo;
				for (classRowNo = 0; classRowNo < nodes.getRowCount(); classRowNo++ ) {

					if (nodes.get(classRowNo, "node").toString().equals(individualAxioms[0])) {
						break;
					}

				}

				if (classRowNo >= nodes.getRowCount()) {
					Debug.sendMessage("Nie odnaleziono węzła " + individualAxioms[0]);
					continue;
				}

				//Dodanie węzła instancji do grafu
				Node n = graph.addNode();
				org.eti.kask.sova.nodes.Node node = null;
				org.eti.kask.sova.edges.Edge edge = null;
				if (axiomParts[0].equals("ClassAssertion")) {
					node = new org.eti.kask.sova.nodes.IndividualNode();
					edge =  new org.eti.kask.sova.edges.Edge();
				} /*else if ...*/
				node.setLabel(individualAxioms[1]);
				n.set("node", node);

				//Dodanie krawędzi łączącej instancję z klasą
				int row = edges.addRow();
				edges.set(row, "source", classRowNo);
				edges.set(row, "target", n.getRow());
				edges.set(row, "edge", edge);


			}
		}
	}


	/**
	 * Rozdziela nazwę związku (OWL Axiom) od nazw połączonych nim elementów
	 * OWL. Zakłada String wejściowy w formacie "NazwaZwiązku(el1 el2 el3)"
	 * @param axiomString an OWL API SomeAxiom.toString() value
	 * @return Tablica <b>dwuelementowa</b>, gdzie <br/>
	 * tablica[0] to String z nazwą związku oraz <br/>
	 * tablica[1] to String zawierający elementy związku rozdzielone spacjami. 
	 */
	protected String[] identifyAxiomString(String axiomString) {
		String[] axiomParts = axiomString.split("[()]");
		if (axiomParts.length < 3) {
			/*Debug.sendMessage("Nieznany format Aksjomatu: " + axiomString);
			axiomParts = new String[3];
			axiomParts[0] = axiomParts[1] = axiomParts[2] = "";
			return axiomParts;*/
		}
		return axiomParts;
	}

}
