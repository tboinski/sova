
package org.eti.kask.sova.graph;


import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.eti.kask.sova.nodes.SomeValuesFromPropertyNode;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import org.eti.kask.sova.utils.Debug;

import org.semanticweb.owl.model.*;



import prefuse.data.Table;


// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.7A3733DC-8F75-9D47-51C1-30AEC658BD96]
// </editor-fold> 
public class OWLtoGraphConverter
{

	private static final OWLtoGraphConverter INSTANCE = new OWLtoGraphConverter();
        Graph graph;

	private Table edges;
	private Table nodes;
       
    
        Hashtable<String, Integer> classes;
        Hashtable<String, Integer> properties;
        Hashtable<String, Integer> individuals;

         Hashtable<String, Integer> dataProperties;
	 Hashtable<String, Integer> anonyms;
         Node thing;
         int defaultNumber;

	// Private constructor prevents instantiation from other classes
	private OWLtoGraphConverter()
	{
            Debug.sendMessage("OWLtoGraphConverter constructor call");
            graph = new Graph();
            nodes = graph.getNodeTable();
            edges = graph.getEdgeTable();
            classes = new Hashtable<String, Integer>();
            properties = new Hashtable<String, Integer>();
            individuals = new Hashtable<String, Integer>();
            anonyms = new Hashtable<String, Integer>();
            anonyms.put("dummy", -100);
	}
	

	public static OWLtoGraphConverter getInstance()
	{
		return INSTANCE;
	}



       /**
        * Umieszcza w grafie wszystkie klasy nie-anonimowe zawarte w ontologii
        * oraz krawędzie między węzłem thing i klasami będącymi jego subclasses.
        * @param ontology
        * @param graph
        */
        private void insertBaseClasses(OWLOntology ontology,  Graph graph ){

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
        private void insertBaseProperties(OWLOntology ontology,  Graph graph){
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
            private void insertBaseIndividuals(OWLOntology ontology,  Graph graph){
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
             * Metoda umieszcza w grafie krawędzi i/lub wierzchołki
             * odpowiadające danemu obiektowi OWLDescription
             * @param description
             * @return
             * @throws Exception
             */
           private int DescriptionHandler( OWLDescription description  ) throws Exception{
		//ObjectSomeValueFrom
		if (description instanceof OWLObjectSomeRestriction){
			//dodaj anonimowe node  - uchwyt do niego powinien byc zwrocony
			
			if (anonyms.containsKey(description.toString())){
                                   // System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());
                                
			}else{
			//	anonyms.put(description.toString(), description.hashCode());
			//	System.out.println("ANONYM NODE DLA " + description.toString() + "dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.AnonymousClassNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());
				//Tworzenie somenode
                                Node someValNode = graph.addNode();
				if (((OWLObjectSomeRestriction) description).getProperty() instanceof OWLObjectProperty){
					//wstaw SomeNode i polacz

						//someVal.put(((OWLObjectSomeRestriction) description).getProperty().toString(), ((OWLObjectSomeRestriction) description).getProperty().hashCode());
				//		System.out.println("SomeVal NODE DLA " + ((OWLObjectSomeRestriction) description).getProperty());
                                                org.eti.kask.sova.nodes.Node sNode = new org.eti.kask.sova.nodes.SomeValuesFromPropertyNode();
                                                sNode.setLabel(((OWLObjectSomeRestriction) description).getProperty().toString() );
                                                someValNode.set("node", sNode);

                                                //dodanie krawedzi laczacej node SomeValuesFrom z definicja property
                                                int definitionID = properties.get(((OWLObjectSomeRestriction) description).getProperty().toString());
                                                 int someRow = edges.addRow();
                                                edges.set(someRow, "source", definitionID);
                                                edges.set(someRow, "target", someValNode.getRow());
                                                edges.set(someRow, "edge", new org.eti.kask.sova.edges.Edge());
				}else{
					//wywolaj rekurencyjnie i poznaj czym jest property
					//probably will never enter here;
					throw new Exception("Some values from somethign wired " + ((OWLObjectSomeRestriction) description).getProperty());

				}
                             //   System.out.println("EDGE MIEDZY ANONYM I SOMEVAL " + someValNode.getRow() +" " +anonymNode.getRow() );
                                int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", someValNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.PropertyEdge());

				//tworzenie wynikowego node
                                int targetId=0;
				if(((OWLObjectSomeRestriction) description).getFiller() instanceof OWLClass ){
					//pobierz  drugie node z z klas
				//	System.out.println("Some val from klass " +((OWLObjectSomeRestriction) description).getFiller() /* + " " + classes.get(((OWLObjectSomeRestriction) description).getFiller().toString() ) */ );
                                        targetId = classes.get( ( (OWLClass)(((OWLObjectSomeRestriction) description).getFiller())).toString());
				}else {
					//to cos bardziej skomplikowanego niz klasa lzu individual
				//	System.out.println("More complicated");
        				targetId =DescriptionHandler(((OWLObjectSomeRestriction) description).getFiller());
				}
				//polacz edgami i ogolnie upenij sie za bangla
                                int row2 = edges.addRow();
                                edges.set(row2, "source",  someValNode.getRow());
				edges.set(row2, "target", targetId);
				edges.set(row2, "edge", new org.eti.kask.sova.edges.PropertyEdge());

                                return anonymNode.getRow();
                            }

                }else if (description instanceof OWLObjectAllRestriction){

                        if (anonyms.containsKey(description.toString())){
                               //     System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());

			}else{
			//	anonyms.put(description.toString(), description.hashCode());
			//	System.out.println("ANONYM NODE DLA " + description.toString() + "dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.AnonymousClassNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());
				//Tworzenie somenode
                                Node allValNode = graph.addNode();
				if (((OWLObjectAllRestriction) description).getProperty() instanceof OWLObjectProperty){
					//wstaw SomeNode i polacz

						//someVal.put(((OWLObjectSomeRestriction) description).getProperty().toString(), ((OWLObjectSomeRestriction) description).getProperty().hashCode());
			//			System.out.println("SomeVal NODE DLA " + ((OWLObjectAllRestriction) description).getProperty());
                                                org.eti.kask.sova.nodes.Node sNode = new org.eti.kask.sova.nodes.AllValuesFromPropertyNode();
                                                sNode.setLabel(((OWLObjectAllRestriction) description).getProperty().toString() );
                                                allValNode.set("node", sNode);

                                                int definitionID = properties.get(((OWLObjectAllRestriction) description).getProperty().toString());
                                                 int someRow = edges.addRow();
                                                edges.set(someRow, "source", definitionID);
                                                edges.set(someRow, "target", allValNode.getRow());
                                                edges.set(someRow, "edge", new org.eti.kask.sova.edges.Edge());


				}else{
					//wywolaj rekurencyjnie i poznaj czym jest property
					//probably will never enter here;
					throw new Exception("All values from somethign wired " + ((OWLObjectAllRestriction) description).getProperty());

				}
                               // System.out.println("EDGE MIEDZY ANONYM I ALLVAL " + allValNode.getRow() +" " +anonymNode.getRow() );
                                int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", allValNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.PropertyEdge());

				//tworzenie wynikowego node
                                int targetId=0;
				if(((OWLObjectAllRestriction) description).getFiller() instanceof OWLClass ){
					//pobierz  drugie node z z klas
					//System.out.println("ALl val from klass " +((OWLObjectAllRestriction) description).getFiller() /* + " " + classes.get(((OWLObjectSomeRestriction) description).getFiller().toString() ) */ );
                                        targetId = classes.get( ( (OWLClass)(((OWLObjectAllRestriction) description).getFiller())).toString());
				}else {
					//to cos bardziej skomplikowanego niz klasa lzu individual
					//System.out.println("More complicated");
        				targetId =DescriptionHandler(((OWLObjectAllRestriction) description).getFiller());
				}
				//polacz edgami i ogolnie upenij sie za bangla
                                if(targetId!=-1){
                                    int row2 = edges.addRow();
                                    edges.set(row2, "source",  allValNode.getRow());
                                    edges.set(row2, "target", targetId);
                                    edges.set(row2, "edge", new org.eti.kask.sova.edges.PropertyEdge());

                                    return anonymNode.getRow();
                                }
                            }

		}else if (description instanceof OWLObjectValueRestriction){

			//System.out.println("HAS VALUE" +description );
                        if (anonyms.containsKey(description.toString())){
                                    //System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());

			}else{
                               // System.out.println("ANONYM NODE DLA " + description.toString() + "dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.AnonymousClassNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());
				//Tworzenie somenode
                                Node allValNode = graph.addNode();

                                if (((OWLObjectValueRestriction) description).getProperty() instanceof OWLObjectProperty){
				//wstaw has val Node i polacz

					//someVal.put(((OWLObjectSomeRestriction) description).getProperty().toString(), ((OWLObjectSomeRestriction) description).getProperty().hashCode());
					//System.out.println("has Val NODE DLA " + ((OWLObjectValueRestriction) description).getProperty());
                                        org.eti.kask.sova.nodes.Node sNode = new org.eti.kask.sova.nodes.AllValuesFromPropertyNode();
                                        sNode.setLabel(((OWLObjectValueRestriction) description).getProperty().toString() );
                                        allValNode.set("node", sNode);

                                         int definitionID = properties.get(((OWLObjectValueRestriction) description).getProperty().toString());
                                                 int someRow = edges.addRow();
                                                edges.set(someRow, "source", definitionID);
                                                edges.set(someRow, "target", allValNode.getRow());
                                                edges.set(someRow, "edge", new org.eti.kask.sova.edges.Edge());



                                }else{
				//wywolaj rekurencyjnie i poznaj czym jest property
				//probably will never enter here;
				throw new Exception("HAS values  somethign wired " + ((OWLObjectValueRestriction) description).getProperty());

                                }
                               // System.out.println("EDGE MIEDZY ANONYM I ALLVAL " + allValNode.getRow() +" " +anonymNode.getRow() );
                                int row = edges.addRow();
				edges.set(row, "source", anonymNode.getRow());
				edges.set(row, "target", allValNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.PropertyEdge());


                                  int row2 = edges.addRow();
                                  edges.set(row2, "source",  allValNode.getRow());
                                  edges.set(row2, "target", individuals.get(((OWLObjectValueRestriction) description).getValue().toString()));
                                  edges.set(row2, "edge", new org.eti.kask.sova.edges.PropertyEdge());
                                 // System.out.println("has Val INDIVIDUAL " + ((OWLObjectValueRestriction) description).getValue());
                                  return anonymNode.getRow();


                      }
		}else if (description instanceof OWLObjectIntersectionOf){

			//System.out.println("INTERSECTION " + ((OWLObjectIntersectionOf) description).toString());
			//wstaw anonym dla tej intersekcji
                           if (anonyms.containsKey(description.toString())){
                               //     System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());

			}else{
                              //  System.out.println("ANONYM NODE DLA " + description.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.IntersectionOfNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());
			//podczep do niego skladowe
                            for(OWLDescription d :((OWLObjectIntersectionOf) description).getOperands() ){
				if(d instanceof OWLClass){
				//	System.out.println("Skaldowa klasa intersekcji " + d.toString()   );
                                        int clsNodeID = classes.get(d.toString());
                                         int row = edges.addRow();
                                        edges.set(row, "source", clsNodeID);
                                        edges.set(row, "target",  anonymNode.getRow());
                                        edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());


				}else{
				//	System.out.println("Skompikowa skladow INtersekcji" + d.toString());
					int nodeID = DescriptionHandler(d);
                                        int row = edges.addRow();
                                        edges.set(row, "source", nodeID );
                                        edges.set(row, "target", anonymNode.getRow());
                                        edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());
				}

                            }

                            return anonymNode.getRow();
                        }
		}else if(description instanceof OWLObjectUnionOf){

			//System.out.println("UNIA " + ((OWLObjectUnionOf) description).toString());

                           if (anonyms.containsKey(description.toString())){
                               //     System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());

			}else{
                            	//wstaw anonym dla tej unii
                           //  System.out.println("ANONYM NODE DLA " + description.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.UnionOfNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());
			//podczep do niego skladowe
			for(OWLDescription d :((OWLObjectUnionOf) description).getOperands() ){
				if(d instanceof OWLClass){
                            //        System.out.println("Klasa w UNII" + d.toString());
					int clsNodeID = classes.get(d.toString());
                                         int row = edges.addRow();
                                        edges.set(row, "source", clsNodeID);
                                        edges.set(row, "target", anonymNode.getRow());
                                        edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());
				}else{
				//	System.out.println("Skompikowa skladow UNII" + d.toString());
					int nodeID = DescriptionHandler(d);
                                        int row = edges.addRow();
                                        edges.set(row, "source", nodeID);
                                        edges.set(row, "target", anonymNode.getRow());
                                        edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());
				}

			}
                        return anonymNode.getRow();
                        
                        }
		}else if(description instanceof OWLObjectComplementOf){
                      //  System.out.println("KOMPLEMENTARNE " + ((OWLObjectComplementOf) description).toString());

                           if (anonyms.containsKey(description.toString())){
                           //         System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());

			}else{

                          //    System.out.println("ANONYM NODE DLA " + description.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.ComplementOfNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());

                         //   System.out.println("OPERAND " + ((OWLObjectComplementOf)description ).getOperand());
                            int complID =0 ;

                              if( ((OWLObjectComplementOf)description).getOperand() instanceof OWLClass){
                                 complID = classes.get(((OWLObjectComplementOf)description).getOperand().toString());
                              }else{
                                complID = DescriptionHandler(((OWLObjectComplementOf)description).getOperand());
                              }

                             int row = edges.addRow();
                             edges.set(row, "source", complID);
                             edges.set(row, "target", anonymNode.getRow());
                             edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());

                             return anonymNode.getRow();

                        }

                }else if (description instanceof OWLObjectOneOf){
                        if (anonyms.containsKey(description.toString())){
                          //          System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                    return anonyms.get(description.toString());

			}else{

                          //    System.out.println("ANONYM NODE DLA " + description.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.OneOfNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());
                                for ( OWLIndividual ind: ((OWLObjectOneOf)description).getIndividuals()){
                                  int id = individuals.get(ind.toString());
                                   int row = edges.addRow();
				edges.set(row, "source", id);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());

                              }

                             return anonymNode.getRow();
                        }
                }else if(description instanceof OWLObjectMinCardinalityRestriction){
                              if (anonyms.containsKey(description.toString())){
                          //          System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                            return anonyms.get(description.toString());

                                }else{
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.CardinalityNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());


                                    int propertyLink =0;
                                    if (((OWLObjectMinCardinalityRestriction) description).getProperty() instanceof OWLProperty){
                                        propertyLink = properties.get(((OWLObjectMinCardinalityRestriction) description).getProperty().toString());
                                    }else{
                                       throw new Exception("Cardinality went wrong");
                                    }

                                    int classLink =0;
                                     if (((OWLObjectMinCardinalityRestriction) description).getFiller() instanceof OWLClass){
                                        classLink = classes.get(((OWLObjectMinCardinalityRestriction) description).getFiller().toString());
                                    }else{
                                        classLink = DescriptionHandler(((OWLObjectMinCardinalityRestriction) description).getFiller());
                                    }

                                Node numberNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node2 = new org.eti.kask.sova.nodes.MinCardinalityValueNode();
                                node2.setLabel( Integer.toString(((OWLObjectMinCardinalityRestriction) description).getCardinality()));

                                numberNode.set("node", node2);
                                anonyms.put(description.toString(), anonymNode.getRow());


                                      int row = edges.addRow();
				edges.set(row, "source", classLink);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.EquivalentEdge());

                                int row2 = edges.addRow();
				edges.set(row2, "source", propertyLink);
				edges.set(row2, "target", anonymNode.getRow());
				edges.set(row2, "edge", new org.eti.kask.sova.edges.Edge());

                                int row3 = edges.addRow();
				edges.set(row3, "source", numberNode.getRow());
				edges.set(row3, "target", anonymNode.getRow());
				edges.set(row3, "edge", new org.eti.kask.sova.edges.Edge());

                                return anonymNode.getRow();
                                }
                }else if(description instanceof OWLObjectMaxCardinalityRestriction){
                         if (anonyms.containsKey(description.toString())){
                          //          System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                            return anonyms.get(description.toString());

                                }else{
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.CardinalityNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());


                                    int propertyLink =0;
                                    if (((OWLObjectMaxCardinalityRestriction) description).getProperty() instanceof OWLProperty){
                                        propertyLink = properties.get(((OWLObjectMaxCardinalityRestriction) description).getProperty().toString());
                                    }else{
                                       throw new Exception("Cardinality went wrong");
                                    }

                                    int classLink =0;
                                     if (((OWLObjectMaxCardinalityRestriction) description).getFiller() instanceof OWLClass){
                                        classLink = classes.get(((OWLObjectMaxCardinalityRestriction) description).getFiller().toString());
                                    }else{
                                        classLink = DescriptionHandler(((OWLObjectMaxCardinalityRestriction) description).getFiller());
                                    }

                                Node numberNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node2 = new org.eti.kask.sova.nodes.MaxCardinalityValueNode();
                                node2.setLabel( Integer.toString(((OWLObjectMaxCardinalityRestriction) description).getCardinality()));

                                numberNode.set("node", node2);
                                anonyms.put(description.toString(), anonymNode.getRow());


                                      int row = edges.addRow();
				edges.set(row, "source", classLink);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.EquivalentEdge());

                                int row2 = edges.addRow();
				edges.set(row2, "source", propertyLink);
				edges.set(row2, "target", anonymNode.getRow());
				edges.set(row2, "edge", new org.eti.kask.sova.edges.Edge());

                                int row3 = edges.addRow();
				edges.set(row3, "source", numberNode.getRow());
				edges.set(row3, "target", anonymNode.getRow());
				edges.set(row3, "edge", new org.eti.kask.sova.edges.Edge());

                                return anonymNode.getRow();
                                }
                }else if(description instanceof OWLObjectExactCardinalityRestriction){
                         if (anonyms.containsKey(description.toString())){
                          //          System.out.println("ZWROCONO JUZ ISTNIEJACY  anonym" + description.toString() );
                                            return anonyms.get(description.toString());

                                }else{
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.CardinalityNode();
                                anonymNode.set("node", node);
                                anonyms.put(description.toString(), anonymNode.getRow());


                                    int propertyLink =0;
                                    if (((OWLObjectExactCardinalityRestriction) description).getProperty() instanceof OWLProperty){
                                        propertyLink = properties.get(((OWLObjectExactCardinalityRestriction) description).getProperty().toString());
                                    }else{
                                       throw new Exception("Cardinality went wrong");
                                    }

                                    int classLink =0;
                                     if (((OWLObjectExactCardinalityRestriction) description).getFiller() instanceof OWLClass){
                                        classLink = classes.get(((OWLObjectExactCardinalityRestriction) description).getFiller().toString());
                                    }else{
                                        classLink = DescriptionHandler(((OWLObjectExactCardinalityRestriction) description).getFiller());
                                    }

                                Node numberNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node2 = new org.eti.kask.sova.nodes.CardinalityValueNode();
                                node2.setLabel( Integer.toString(((OWLObjectExactCardinalityRestriction) description).getCardinality()));

                                numberNode.set("node", node2);
                                anonyms.put(description.toString(), anonymNode.getRow());


                                      int row = edges.addRow();
				edges.set(row, "source", classLink);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.EquivalentEdge());

                                int row2 = edges.addRow();
				edges.set(row2, "source", propertyLink);
				edges.set(row2, "target", anonymNode.getRow());
				edges.set(row2, "edge", new org.eti.kask.sova.edges.Edge());

                                int row3 = edges.addRow();
				edges.set(row3, "source", numberNode.getRow());
				edges.set(row3, "target", anonymNode.getRow());
				edges.set(row3, "edge", new org.eti.kask.sova.edges.Edge());

                                return anonymNode.getRow();
                                }
                }else {
			System.out.println("DESCUNHANLED: " + description);
		}


                return defaultNumber;
	}

        /**
         * Metoda zamiany obiektu OWLOntology na obiekt graph biblioteki prefuse.
         * Metoda wywoluje podrzedne metody wpisujace do grafu wezly i krawedzie
         * @param ontology
         * @return Graph - graf prefuse
         */
        public Graph OWLtoGraph(OWLOntology ontology) throws Exception{
           
	         graph.addColumn( "node", org.eti.kask.sova.nodes.Node.class );

		// Dodajemy węzeł Thing
		thing = graph.addNode();
		org.eti.kask.sova.nodes.Node t = new org.eti.kask.sova.nodes.ThingNode();

		thing.set( "node", t );
                defaultNumber = thing.getRow();
                
		edges.addColumn("edge", org.eti.kask.sova.edges.Edge.class);

                classes.put("Thing", defaultNumber);
              
                this.insertBaseClasses(ontology, graph);
                this.insertBaseProperties(ontology, graph);
                this.insertBaseIndividuals(ontology, graph);


               /*   Node n = graph.addNode();
                            org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.ClassNode();
                            node.setLabel(cls.toString());
                            n.set("node", node);

                            if( cls.getSuperClasses(ontology).isEmpty() == true ){ //thing jest superklasa
                                int row = edges.addRow();
				edges.set(row, "source", thing.getRow());
				edges.set(row, "target", n.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge() );
                            }*/
	         for(OWLAxiom axiom : ontology.getAxioms()){

             	  if (axiom instanceof OWLSubClassAxiom   ){
              		//System.out.println("OWLCLASSAXIOM : " + axiom.toString());
              		   int subClassID = 0;
                           int superClassID = 0;
                         //budowanie edge
              		  //uchwyty miedzy  edgem to ((OWLSubClassAxiom) axiom).getSubClass()   i(OWLSubClassAxiom) axiom).getSuperClass()
              		   if (((OWLSubClassAxiom) axiom).getSubClass()  instanceof OWLClass ){
              		//	   System.out.println("Subklasa:  " + ((OWLSubClassAxiom) axiom).getSubClass());
                                   subClassID = classes.get(((OWLSubClassAxiom) axiom).getSubClass().toString());

              			   //wez z hashtable uchwyt  subklasy
              		   } else {

              			   //pobierz z handlera uchwyt do wierzcholka
              			   subClassID = DescriptionHandler(((OWLSubClassAxiom) axiom).getSubClass());
              		   }

              		   if(((OWLSubClassAxiom) axiom).getSuperClass() instanceof OWLClass /*&& !((OWLSubClassAxiom) axiom).getSuperClass().isOWLThing()*/  ){
              		//	   System.out.println("Superklasa: " + ((OWLSubClassAxiom) axiom).getSuperClass());
              			   //wez z hashtable uchwyt taty
                                   superClassID = classes.get(((OWLSubClassAxiom) axiom).getSuperClass().toString());
              		   }else{

              			   superClassID = DescriptionHandler(((OWLSubClassAxiom) axiom).getSuperClass());
              		   }
              		    //dodaj edge
                           if(superClassID > 0 && subClassID > 0){
                                int row = edges.addRow();
				edges.set(row, "source", superClassID);
				edges.set(row, "target", subClassID);
				edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge());
                           }
                            


              	 }else if (axiom instanceof OWLDisjointClassesAxiom){
              		  //wstaw krawedz disjoint

              		//System.out.println("DISJOINT AXIOM: " +  ((OWLDisjointClassesAxiom) axiom).toString());
                        int id1=0,  id2=0, i=0;
              		for ( OWLDescription d: ((OWLDisjointClassesAxiom) axiom).getDescriptions() ){
              			if (d instanceof OWLClass){
              				//normalnie
              				if(i==0){
                                            id1= classes.get(d.toString());
                                        }else{
                                            id2=classes.get(d.toString());
                                        }
              			}else {
                                    if(i==0){
              				id1=DescriptionHandler(d);
                                    }else{
                                        id2=DescriptionHandler(d);
                                    }
              			}
                                i++;
              		}
                           if(id1 > 0 && id2 > 0){
                                int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.eti.kask.sova.edges.DisjointEdge());
                           }


              	    }else if(axiom instanceof OWLEquivalentClassesAxiom){

              		//System.out.println("EQUIV AXIOM: " +  ((OWLEquivalentClassesAxiom) axiom).toString());
                        int id1=0, id2=0, i=0;
              		for ( OWLDescription d: ((OWLEquivalentClassesAxiom) axiom).getDescriptions() ){
              			if (d instanceof OWLClass){
              				//normalnie
                                     if(i==0){
                                            id1= classes.get(d.toString());
                                        }else{
                                            id2=classes.get(d.toString());
                                        }
              			//	System.out.println("EQUIV skladowa klasa : " + d.toString() );
              			}else {
              			//	System.out.println("EQUIV skladowa skomplikowana : " + d.toString());
              				 if(i==0){
              				id1=DescriptionHandler(d);
                                    }else{
                                        id2=DescriptionHandler(d);
                                    }

              			}
                                i++;
              		}

                           if(id1 > 0 && id2 > 0){
                                int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.eti.kask.sova.edges.DisjointEdge());
                           }

              	  }else if (axiom instanceof OWLObjectPropertyRangeAxiom){

                      int id1 = properties.get(((OWLObjectPropertyRangeAxiom)axiom).getProperty().toString());

                      int id2=0;
                      if(((OWLObjectPropertyRangeAxiom)axiom).getRange() instanceof OWLClass){
                          id2 = classes.get(((OWLObjectPropertyRangeAxiom)axiom).getRange().toString());
                      }else{
                          id2 = DescriptionHandler(((OWLObjectPropertyRangeAxiom)axiom).getRange());
                      }


                       if(id1 > 0 && id2 > 0){
                        int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.eti.kask.sova.edges.RangeEdge());
                       }
                  }else if(axiom instanceof OWLObjectPropertyDomainAxiom ){
                     int id1 = properties.get(((OWLObjectPropertyDomainAxiom)axiom).getProperty().toString());

                      int id2=0;
                      if(((OWLObjectPropertyDomainAxiom)axiom).getDomain() instanceof OWLClass){
                          id2 = classes.get(((OWLObjectPropertyDomainAxiom)axiom).getDomain().toString());
                      }else{
                          id2 = DescriptionHandler(((OWLObjectPropertyDomainAxiom)axiom).getDomain());
                      }


                       if(id1 > 0 && id2 > 0){
                        int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.eti.kask.sova.edges.DomainEdge());
                       }

                  }else if (axiom instanceof OWLClassAssertionAxiom){
                      int id1 = individuals.get(((OWLClassAssertionAxiom)axiom).getIndividual().toString());

                      int id2 =0;
                      //if(!((OWLClassAssertionAxiom)axiom).getDescription().isOWLThing()){
                        if(((OWLClassAssertionAxiom)axiom).getDescription() instanceof OWLClass){
                              id2 = classes.get(((OWLClassAssertionAxiom)axiom).getDescription().toString());

                        }else{
                              id2 = DescriptionHandler(((OWLClassAssertionAxiom)axiom).getDescription());
                        }
                        if(id1 > 0 && id2 > 0){
                            int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());
                        }
                      //}

                  }else if(axiom instanceof OWLDifferentIndividualsAxiom){

                     //      System.out.println("DIFFRENT NODE " + axiom.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.DifferentNode();
                                anonymNode.set("node", node);

                              for ( OWLIndividual ind: ((OWLDifferentIndividualsAxiom)axiom).getIndividuals()){
                                  int id = individuals.get(ind.toString());
                                   int row = edges.addRow();
				edges.set(row, "source", id);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());

                              }


                  }else if( axiom  instanceof OWLSubPropertyAxiom){

                    //  System.out.println("OWLPROPERTYAXIOM : " + axiom.toString());
              		   int subClassID = 0;
                           int superClassID = 0;
                         //budowanie edge
              		  //uchwyty miedzy  edgem to ((OWLSubPropertyAxiom) axiom).getSubClass()   i(OWLSubPropertyAxiom) axiom).getSuperClass()
              		   if (((OWLSubPropertyAxiom) axiom).getSubProperty() instanceof OWLProperty ){
              		//	   System.out.println("Subprop:  " + ((OWLSubPropertyAxiom) axiom).getSubProperty());
                                   subClassID = properties.get(((OWLSubPropertyAxiom) axiom).getSubProperty().toString());

              			   //wez z hashtable uchwyt  subklasy
              		   }

              		   if(((OWLSubPropertyAxiom) axiom).getSuperProperty() instanceof OWLProperty   ){
              		//	   System.out.println("Superprop: " + ((OWLSubPropertyAxiom) axiom).getSuperProperty());
              			   //wez z hashtable uchwyt taty
                                   superClassID = properties.get(((OWLSubPropertyAxiom) axiom).getSuperProperty().toString());
              		   }
              		    //dodaj edge
                           if(superClassID > 0 && subClassID > 0){
                                int row = edges.addRow();
				edges.set(row, "source", superClassID);
				edges.set(row, "target", subClassID);
				edges.set(row, "edge", new org.eti.kask.sova.edges.SubEdge());
                           }



                  }else if (axiom instanceof OWLInverseObjectPropertiesAxiom){
                        int id1 = properties.get(((OWLInverseObjectPropertiesAxiom)axiom).getFirstProperty().toString() ) ;
                        int id2 = properties.get(((OWLInverseObjectPropertiesAxiom)axiom).getSecondProperty().toString() );
                         int row = edges.addRow();
		         edges.set(row, "source", id1);
		         edges.set(row, "target", id2);
			 edges.set(row, "edge", new org.eti.kask.sova.edges.InverseOfEdge());


                  
                 }else if(axiom instanceof OWLEquivalentObjectPropertiesAxiom){
                     int id1=0,id2=0, i=0;
                     for(OWLPropertyExpression d :((OWLEquivalentObjectPropertiesAxiom)axiom).getProperties()){
                         		//normalnie

                                     if(i==0){
                                            id1= properties.get(d.toString());
                                        }else{
                                            id2=properties.get(d.toString());
                                        }

              		       i++;
                     }
                     if(id1 > 0 && id2 > 0){
                            int row = edges.addRow();
				edges.set(row, "source", id1);
				edges.set(row, "target", id2);
				edges.set(row, "edge", new org.eti.kask.sova.edges.EquivalentPropertyEdge());
                        }



                 }else if ( axiom instanceof OWLFunctionalObjectPropertyAxiom ){
                       //     System.out.println("FUNC OBJ PRO " + axiom.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.FunctionalPropertyNode();
                                anonymNode.set("node", node);
                             int id = properties.get( ((OWLFunctionalObjectPropertyAxiom)axiom).getProperty().toString()   );
                                int row = edges.addRow();
		         edges.set(row, "source", anonymNode.getRow());
		         edges.set(row, "target", id);
			 edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());


                  }else if ( axiom instanceof OWLInverseFunctionalObjectPropertyAxiom){
                       //    System.out.println("FUNC INV OBJ PRO " + axiom.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.InverseFunctionalPropertyNode();
                                anonymNode.set("node", node);
                             int id = properties.get( ((OWLInverseFunctionalObjectPropertyAxiom)axiom).getProperty().toString()   );
                                int row = edges.addRow();
		         edges.set(row, "source", anonymNode.getRow());
		         edges.set(row, "target", id);
			 edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());
                  }else if (axiom instanceof OWLSymmetricObjectPropertyAxiom){
                    //  System.out.println("FUNC sym OBJ PRO " + axiom.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.SymmetricPropertyNode();
                                anonymNode.set("node", node);
                             int id = properties.get( ((OWLSymmetricObjectPropertyAxiom)axiom).getProperty().toString()   );
                                int row = edges.addRow();
		         edges.set(row, "source", anonymNode.getRow());
		         edges.set(row, "target", id);
			 edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());
                  }else if (axiom instanceof OWLTransitiveObjectPropertyAxiom){
                   //   System.out.println("FUNC sym OBJ PRO " + axiom.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.TransitivePropertyNode();
                                anonymNode.set("node", node);
                             int id = properties.get( ((OWLTransitiveObjectPropertyAxiom)axiom).getProperty().toString()   );
                                int row = edges.addRow();
		         edges.set(row, "source", anonymNode.getRow());
		         edges.set(row, "target", id);
			 edges.set(row, "edge", new org.eti.kask.sova.edges.Edge());
                  }else if (axiom instanceof OWLSameIndividualsAxiom){

                    //            System.out.println("SAME NODE " + axiom.toString() + " dodany" );
                                Node anonymNode = graph.addNode();
                                org.eti.kask.sova.nodes.Node node = new org.eti.kask.sova.nodes.SameAsNode();
                                anonymNode.set("node", node);

                              for ( OWLIndividual ind: ((OWLSameIndividualsAxiom)axiom).getIndividuals()){
                                  int id = individuals.get(ind.toString());
                                   int row = edges.addRow();
				edges.set(row, "source", id);
				edges.set(row, "target", anonymNode.getRow());
				edges.set(row, "edge", new org.eti.kask.sova.edges.OperationEdge());

                              }
                  }else{
                      
              		System.out.println("OMITTED: " + axiom  );
                     
              	  }
              	  

                 }
                 


            return graph;
        }

	

	
}
