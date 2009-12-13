
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
        Hashtable< Integer, OWLAxiom> anonymousNodes = new Hashtable<Integer, OWLAxiom>();



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
                       //    Debug.sendMessage("Subclass entity axiomu " + arr[0].toString());
                         //  Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                        //   Debug.sendMessage("ma id w graph?: " + individuals.get(arr[1].toString()) );
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
                            Debug.sendMessage(axiom.toString() + " hash: " + axiom.hashCode() );

                            anonymousNodes.put(axiom.hashCode(), axiom);


                           
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
                   Debug.sendMessage("\n Axiomy klasy:" +cls.toString());
                     for(OWLAxiom axiom : ontology.getAxioms(cls)){ //przegladamy axiomy powiazane z dana klasa
                       Debug.sendMessage(axiom.toString());
                       if(axiom.getAxiomType() == AxiomType.SUBCLASS){

                           Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                    //       Debug.sendMessage("Subclass entity axiomu " + arr[0].toString());
                    //       Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                    //       Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(classes.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest subklasa anonimowej encji
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                   edges.set(row, "target", classes.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );
                           }

                       } else if(axiom.getAxiomType() == AxiomType.DISJOINT_CLASSES){

                           Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                      //     Debug.sendMessage("SDisjoint entity axiomu " + arr[0].toString());
                      ///     Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                      //     Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(classes.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest disjoint z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                   edges.set(row, "target", classes.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.DisjointEdge() );
                           }
                       } else if (axiom.getAxiomType() == AxiomType.EQUIVALENT_CLASSES){
                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                      //     Debug.sendMessage("SDisjoint entity axiomu " + arr[0].toString());
                      //     Debug.sendMessage("ma id w graph?: " + classes.get(arr[0].toString()) );
                      //     Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
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
                           
                       }else if(axiom.getAxiomType() == AxiomType.SUB_OBJECT_PROPERTY ){

                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("sub_property properties entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[1].toString()) );
                           if(properties.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest equivalent z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                    edges.set(row, "target", properties.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );
                           }

                       }else if(axiom.getAxiomType() == AxiomType.OBJECT_PROPERTY_RANGE ){
                           
                            Set<OWLEntity> entities =  axiom.getReferencedEntities();
                           Object [] arr = entities.toArray();
                           Debug.sendMessage("range properties entity axiomu " + arr[0].toString());
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
                           Debug.sendMessage("domain properties entity axiomu " + arr[0].toString());
                           Debug.sendMessage("ma id w graph?: " + properties.get(arr[0].toString()) );
                           Debug.sendMessage("ma id w graph?: " + classes.get(arr[1].toString()) );
                           if(properties.get(arr[0].toString()) != null  &&  classes.get(arr[1].toString()) != null){ //klasa nie jest equivalent z anonimowa encja
                                    int row = edges.addRow();
                                    edges.set(row, "source", classes.get(arr[1].toString()));
                                    edges.set(row, "target", properties.get(arr[0].toString()));
                                    edges.set(row, "edge", new org.eti.kask.sova.edges.DomainEdge() );
                           }
                       }else if(axiom.getAxiomType() == AxiomType.TRANSITIVE_OBJECT_PROPERTY){
                             Debug.sendMessage("transitive " + axiom.toString() + " hash: " + axiom.hashCode() );

                            anonymousNodes.put(axiom.hashCode(), axiom);
                       }else if(axiom.getAxiomType() == AxiomType.SYMMETRIC_OBJECT_PROPERTY){
                             Debug.sendMessage("symmetric " + axiom.toString() + " hash: " + axiom.hashCode() );

                            anonymousNodes.put(axiom.hashCode(), axiom);
                       }else if(axiom.getAxiomType() == AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY){
                             Debug.sendMessage("inverse functional "+ axiom.toString() + " hash: " + axiom.hashCode() );

                            anonymousNodes.put(axiom.hashCode(), axiom);
                            
                       }else if(axiom.getAxiomType() == AxiomType.FUNCTIONAL_OBJECT_PROPERTY){
                             Debug.sendMessage("functional " + axiom.toString() + " hash: " + axiom.hashCode() );

                            anonymousNodes.put(axiom.hashCode(), axiom);
                       }


             }
             }

        }

        private void insertAnonymousNodesAndEdges(OWLOntology ontology,  Graph graph,  Hashtable<String, Integer> properties, Hashtable<String, Integer> classes, Hashtable<String, Integer> individuals){


            for (OWLAxiom axiom : anonymousNodes.values()  ){
                Debug.sendMessage(axiom.toString());
                if(axiom.getAxiomType() == AxiomType.DIFFERENT_INDIVIDUALS ){
                    Node anonymous = graph.addNode();
                    org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.DifferentNode();
                    anonymous.set( "node", node );
                    Set<OWLEntity> entities =  axiom.getReferencedEntities();
                    Object [] arr = entities.toArray();
                    for(int i=0; i<arr.length; i++){
                        int row = edges.addRow();
                        edges.set(row, "source", anonymous.getRow());
                        edges.set(row, "target", individuals.get(arr[i].toString()));
                        edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );
                    }

                }else  if(axiom.getAxiomType() == AxiomType.TRANSITIVE_OBJECT_PROPERTY ){
                    Node anonymous = graph.addNode();
                    org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.TransitivePropertyNode();
                    anonymous.set( "node", node );
                    Set<OWLEntity> entities =  axiom.getReferencedEntities();
                    Object [] arr = entities.toArray();

                        int row = edges.addRow();
                        edges.set(row, "source", anonymous.getRow());
                        edges.set(row, "target", properties.get(arr[0].toString()));
                        edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );


                }else  if(axiom.getAxiomType() == AxiomType.SYMMETRIC_OBJECT_PROPERTY ){
                    Node anonymous = graph.addNode();
                    org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.SymmetricPropertyNode();
                    anonymous.set( "node", node );
                    Set<OWLEntity> entities =  axiom.getReferencedEntities();
                    Object [] arr = entities.toArray();

                        int row = edges.addRow();
                        edges.set(row, "source", anonymous.getRow());
                         edges.set(row, "target", properties.get(arr[0].toString()));
                        edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );


                }else  if(axiom.getAxiomType() == AxiomType.FUNCTIONAL_OBJECT_PROPERTY ){
                    Node anonymous = graph.addNode();
                    org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.FunctionalPropertyNode();
                    anonymous.set( "node", node );
                    Set<OWLEntity> entities =  axiom.getReferencedEntities();
                    Object [] arr = entities.toArray();

                        int row = edges.addRow();
                        edges.set(row, "source", anonymous.getRow());
                           edges.set(row, "target", properties.get(arr[0].toString()));
                        edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );


                }else  if(axiom.getAxiomType() == AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY ){
                    Node anonymous = graph.addNode();
                    org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.InverseFunctionalPropertyNode();
                    anonymous.set( "node", node );
                    Set<OWLEntity> entities =  axiom.getReferencedEntities();
                    Object [] arr = entities.toArray();

                        int row = edges.addRow();
                        edges.set(row, "source", anonymous.getRow());
                           edges.set(row, "target", properties.get(arr[0].toString()));
                        edges.set(row, "edge", new org.eti.kask.sova.edges.Edge() );


                }


            }


        }

        /**
         * Metoda zamiany obiektu OWLOntology na obiekt graph biblioteki prefuse.
         * Metoda wywoluje podrzedne metody wpisujace do grafu wezly i krawedzie
         * @param ontology
         * @return Graph - graf prefuse
         */
        public Graph OWLtoGraph(OWLOntology ontology){
            Graph graph = new Graph();
            nodes = graph.getNodeTable();
	    graph.addColumn( "node", org.eti.kask.sova.nodes.Node.class );

		// Dodajemy węzeł Thing
		Node thing = graph.addNode();
		org.eti.kask.sova.nodes.Node t = new org.eti.kask.sova.nodes.ThingNode();

		thing.set( "node", t );

		edges = graph.getEdgeTable();
		edges.addColumn("edge", org.eti.kask.sova.edges.Edge.class);

                for(OWLAxiom ax : ontology.getAxioms()){
                   Debug.sendMessage(ax.toString());
                }

                Hashtable<String, Integer> classes = new Hashtable<String, Integer>();
                Hashtable<String, Integer> properties = new Hashtable<String, Integer>();
                Hashtable<String, Integer> individuals = new Hashtable<String, Integer>();
                this.insertBaseClasses(ontology, graph, thing, classes);
                this.insertBasicEdgesForClasses(ontology, graph, classes);
                this.insertBaseProperties(ontology, graph, properties);
                this.insertBasicEdgesForProperties(ontology, graph, properties, classes);
                this.insertBaseIndividuals(ontology, graph, individuals);
                this.insertBasicEdgesForIndividuals(ontology, graph, individuals, classes);
                this.insertAnonymousNodesAndEdges(ontology, graph, properties, classes, individuals);



            return graph;
        }

	

	
}
