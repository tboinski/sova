
package org.eti.kask.sova.visualization;

import java.util.Iterator;
import org.eti.kask.sova.edges.Edge;
import prefuse.action.GroupAction;
import prefuse.data.Node;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;
import prefuse.visual.tuple.TableEdgeItem;

/**
 * Klasa filtrująca wyswietlane elementy ontologii. Wyswietla wszytskie elementy
 * poza tymi, które w klasie {@link FilterOptions} posiadają wartość false;
 * 
 * @author Piotr Kunowski
 * @see {@link FilterOptions}  
 */
public class OVItemFilter extends GroupAction {

    @Override
    public void run(double frac) {


        Iterator items = m_vis.items(m_group);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem)items.next();
            boolean isVisualItemVisible = item.isVisible();
            if(isVisualItemVisible) {
                if ((item instanceof Edge) || (item instanceof EdgeItem) || (item instanceof  TableEdgeItem)) {//krawędzie
                    Object o = ((VisualItem)item).get(OVPredicate.EDGE);
                    if ( (o instanceof org.eti.kask.sova.edges.DisjointEdge)&&(!FilterOptions.classFilter || !FilterOptions.disjointClassEdge )) {
                        ((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.eti.kask.sova.edges.SubClassEdge)&& (!FilterOptions. classFilter || !FilterOptions.subClassEdge)) {
                        ((VisualItem)item).setVisible(false);
                	}else
                    if ((o instanceof org.eti.kask.sova.edges.EquivalentEdge)&& (!FilterOptions.classFilter || !FilterOptions.equivalentClassEdge)) {
                        ((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.eti.kask.sova.edges.InstanceOfEdge)&& (!FilterOptions.individual || !FilterOptions.instanceOfEdge)) {
                    	((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.eti.kask.sova.edges.InstancePropertyEdge)&& (!FilterOptions.property || !FilterOptions.instanceProperty)) {
                       	((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.eti.kask.sova.edges.SubPropertyEdge)&& (!FilterOptions.property || !FilterOptions.subPropertyEdge)) {
                       	((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.eti.kask.sova.edges.EquivalentPropertyEdge)&& (!FilterOptions.property || !FilterOptions.equivalentPropertyEdge)) {
                       	((VisualItem)item).setVisible(false);
                    }else                    	
                    if ((o instanceof org.eti.kask.sova.edges.DomainEdge)&& (!FilterOptions.property || !FilterOptions.domain)) {
                       	((VisualItem)item).setVisible(false);
                    }else 
                    if ((o instanceof org.eti.kask.sova.edges.RangeEdge)&& (!FilterOptions.property || !FilterOptions.range)) {
                       	((VisualItem)item).setVisible(false);
                    }else 
                    if ((o instanceof org.eti.kask.sova.edges.InverseOfEdge || o instanceof org.eti.kask.sova.edges.InverseOfMutualEdge)
                    		&& (!FilterOptions.property || !FilterOptions.inverseOfProperty)) {
                       	((VisualItem)item).setVisible(false);
                    }else 
                    { // pozostałe krawdzie są widoczne
                        ((VisualItem)item).setVisible(true);
                    }

                }else{//wierzchołki
                    Object o = ((VisualItem)item).get(OVPredicate.NODE);
                    //Class node
                    if ( (o instanceof org.eti.kask.sova.nodes.ClassNode || o instanceof org.eti.kask.sova.nodes.ThingNode 
                    		|| o instanceof org.eti.kask.sova.nodes.NothingNode || o instanceof org.eti.kask.sova.nodes.AnonymousClassNode 
                    		|| o instanceof org.eti.kask.sova.nodes.CardinalityNode ) &&(!FilterOptions.classFilter)) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // class - uninOf
                    if ( (o instanceof org.eti.kask.sova.nodes.UnionOfNode)
                    		&&(!FilterOptions.classFilter || !FilterOptions.unionOf )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // Class -  intersectionOf
                    if ( (o instanceof org.eti.kask.sova.nodes.IntersectionOfNode)
                    		&&(!FilterOptions.classFilter || !FilterOptions.intersectionOf )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // Class - ComplementOf
                    if ( (o instanceof org.eti.kask.sova.nodes.ComplementOfNode)
                    		&&(!FilterOptions.classFilter || !FilterOptions.complementOf )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else    // Cardinality               	
                    if ( ( o instanceof org.eti.kask.sova.nodes.CardinalityValueNode)
                    		&&(!FilterOptions.classFilter || !FilterOptions.cardinality )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else    // Individual               	
                    if ( ( o instanceof org.eti.kask.sova.nodes.IndividualNode)
                      		&&(!FilterOptions.individual)) {
                        ((VisualItem)item).setVisible(false);
                            //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                          	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // Indywidual differentFrom/allDifferenr                 	
                    if ( ( o instanceof org.eti.kask.sova.nodes.DifferentNode)
                       		&&(!FilterOptions.individual || !FilterOptions.different)) {
                        ((VisualItem)item).setVisible(false);
                         //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                           	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        } 
                        
                    }else // Indywidual -  sameAs                 	
                    if ( ( o instanceof org.eti.kask.sova.nodes.SameAsNode)
                           &&(!FilterOptions.individual || !FilterOptions.sameAs)) {
                       ((VisualItem)item).setVisible(false);
                       //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                           	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else  //Indywidual - oneOf                	
                    if ( ( o instanceof org.eti.kask.sova.nodes.OneOfNode)
                       		&&(!FilterOptions.individual || !FilterOptions.oneOf)) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                           	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                   }else // Property node
                   if ( ( o instanceof org.eti.kask.sova.nodes.PropertyNode)
                      		&&(!FilterOptions.property )) {
                       ((VisualItem)item).setVisible(false);
                       //usunięcie kwawędzi powiązanej z wierzchołkiem
                       Node n = ((Node)item);
                       Iterator egdesToRemove = n.edges();
                       while (egdesToRemove.hasNext()){
                          	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                       }
                   }else //Property - FunctionalProperty node
                   if ( ( o instanceof org.eti.kask.sova.nodes.FunctionalPropertyNode)
                      		&&(!FilterOptions.property || !FilterOptions.functionalProperty)) {
                       ((VisualItem)item).setVisible(false);
                       //usunięcie kwawędzi powiązanej z wierzchołkiem
                       Node n = ((Node)item);
                       Iterator egdesToRemove = n.edges();
                       while (egdesToRemove.hasNext()){
                          	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                       }
                  }else //Property - InverseFunctionalProperty node
                  if ( ( o instanceof org.eti.kask.sova.nodes.InverseFunctionalPropertyNode)
                     		&&(!FilterOptions.property || !FilterOptions.inverseFunctionalProperty)) {
                      ((VisualItem)item).setVisible(false);
                      //usunięcie kwawędzi powiązanej z wierzchołkiem
                      Node n = ((Node)item);
                      Iterator egdesToRemove = n.edges();
                      while (egdesToRemove.hasNext()){
                          ((VisualItem)egdesToRemove.next()).setVisible(false); 	
                      }
                  }else //Property - SymmetricProperty node
                  if ( ( o instanceof org.eti.kask.sova.nodes.SymmetricPropertyNode)
                     		&&(!FilterOptions.property || !FilterOptions.symmetricProperty)) {
                      ((VisualItem)item).setVisible(false);
                      //usunięcie kwawędzi powiązanej z wierzchołkiem
                      Node n = ((Node)item);
                      Iterator egdesToRemove = n.edges();
                      while (egdesToRemove.hasNext()){
                          ((VisualItem)egdesToRemove.next()).setVisible(false); 	
                      }
                  }else //Property - TransitiveProperty node
                  if ( ( o instanceof org.eti.kask.sova.nodes.TransitivePropertyNode)
                     		&&(!FilterOptions.property || !FilterOptions.transitiveProperty)) {
                      ((VisualItem)item).setVisible(false);
                      //usunięcie kwawędzi powiązanej z wierzchołkiem
                      Node n = ((Node)item);
                      Iterator egdesToRemove = n.edges();
                      while (egdesToRemove.hasNext()){
                          ((VisualItem)egdesToRemove.next()).setVisible(false); 	
                      }
                  }else{  
                	  ((VisualItem)item).setVisible(true);
                    }
                    
                }
                
			} else {
				((VisualItem)item).setVisible(false);
			}
        }

    }



}
