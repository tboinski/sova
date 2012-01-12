/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of SOVA.  SOVA is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */


package org.pg.eti.kask.sova.visualization;

import java.util.Iterator;

import org.pg.eti.kask.sova.edges.Edge;
import org.pg.eti.kask.sova.graph.Constants;

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
	private boolean rememberOldState = true;
	

    public boolean isRememberOldState() {
		return rememberOldState;
	}

    /**
     * Wartość rememberOldState (domyślnie true) pozwala na zapamiętanie wcześniejszych 
     * filtrów. W tym przypadku odfiltrowanie elementy nie są uwzględiane przy sprawdzaniu.
     * Wartość wymagana przy filtrze na odległość. 
     * @param rememberOldState
     */
	public void setRememberOldState(boolean rememberOldState) {
		this.rememberOldState = rememberOldState;
	}


	@Override
    public void run(double frac) {


        Iterator items = m_vis.items(m_group);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem)items.next();
            boolean isVisualItemVisible = item.isVisible();
            if(isVisualItemVisible || !rememberOldState) {
                if ((item instanceof Edge) || (item instanceof EdgeItem) || (item instanceof  TableEdgeItem)) {//krawędzie
                    Object o = ((VisualItem)item).get(Constants.GRAPH_EDGES);
                    if ( (o instanceof org.pg.eti.kask.sova.edges.DisjointEdge)&&(!FilterOptions.isClassFilter() || !FilterOptions.isDisjointClassEdge() )) {
                        ((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.pg.eti.kask.sova.edges.SubClassEdge)&& (!FilterOptions. isClassFilter() || !FilterOptions.isSubClassEdge())) {
                        ((VisualItem)item).setVisible(false);
                	}else
                    if ((o instanceof org.pg.eti.kask.sova.edges.EquivalentEdge)&& (!FilterOptions.isClassFilter() || !FilterOptions.isEquivalentClassEdge())) {
                        ((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.pg.eti.kask.sova.edges.InstanceOfEdge)&& (!FilterOptions.isIndividual() || !FilterOptions.isInstanceOfEdge())) {
                    	((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.pg.eti.kask.sova.edges.InstancePropertyEdge)&& (!FilterOptions.isProperty() || !FilterOptions.isInstanceProperty())) {
                       	((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.pg.eti.kask.sova.edges.SubPropertyEdge)&& (!FilterOptions.isProperty() || !FilterOptions.isSubPropertyEdge())) {
                       	((VisualItem)item).setVisible(false);
                    }else
                    if ((o instanceof org.pg.eti.kask.sova.edges.EquivalentPropertyEdge)&& (!FilterOptions.isProperty() || !FilterOptions.isEquivalentPropertyEdge())) {
                       	((VisualItem)item).setVisible(false);
                    }else                    	
                    if ((o instanceof org.pg.eti.kask.sova.edges.DomainEdge)&& (!FilterOptions.isProperty() || !FilterOptions.isDomain())) {
                       	((VisualItem)item).setVisible(false);
                    }else 
                    if ((o instanceof org.pg.eti.kask.sova.edges.RangeEdge)&& (!FilterOptions.isProperty() || !FilterOptions.isRange())) {
                       	((VisualItem)item).setVisible(false);
                    }else 
                    if ((o instanceof org.pg.eti.kask.sova.edges.InverseOfEdge || o instanceof org.pg.eti.kask.sova.edges.InverseOfMutualEdge)
                    		&& (!FilterOptions.isProperty() || !FilterOptions.isInverseOfProperty())) {
                       	((VisualItem)item).setVisible(false);
                    }else 
                    { // pozostałe krawdzie są widoczne
                        ((VisualItem)item).setVisible(true);
                    }

                }else{//wierzchołki
                    Object o = ((VisualItem)item).get(Constants.GRAPH_NODES);
                    //Class node
                    if ( (o instanceof org.pg.eti.kask.sova.nodes.ClassNode || o instanceof org.pg.eti.kask.sova.nodes.ThingNode 
                    		|| o instanceof org.pg.eti.kask.sova.nodes.NothingNode
                    		) &&(!FilterOptions.isClassFilter())) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // class - uninOf
                    if ( (o instanceof org.pg.eti.kask.sova.nodes.UnionOfNode)
                    		&&(!FilterOptions.isClassFilter() || !FilterOptions.isUnionOf() )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                        
                    }else // class - anonymouse 
                        if ( 
                        		(o.getClass() == org.pg.eti.kask.sova.nodes.AnonymousClassNode.class)
                        		&&(!FilterOptions.isClassFilter() || !FilterOptions.isAnonymouse())) {
                            ((VisualItem)item).setVisible(false);
                            //usunięcie kwawędzi powiązanej z wierzchołkiem
                            Node n = ((Node)item);
                            Iterator egdesToRemove = n.edges();
                            while (egdesToRemove.hasNext()){
                            	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                            }    
                    }else // Class -  intersectionOf
                    if ( (o instanceof org.pg.eti.kask.sova.nodes.IntersectionOfNode)
                    		&&(!FilterOptions.isClassFilter() || !FilterOptions.isIntersectionOf() )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // Class - ComplementOf
                    if ( (o instanceof org.pg.eti.kask.sova.nodes.ComplementOfNode)
                    		&&(!FilterOptions.isClassFilter() || !FilterOptions.isComplementOf() )) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else    // Cardinality               	
                    if ( ( o instanceof org.pg.eti.kask.sova.nodes.CardinalityValueNode
                    		|| o instanceof org.pg.eti.kask.sova.nodes.CardinalityNode)
                    		&&(!FilterOptions.isClassFilter() || !FilterOptions.isCardinality())) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                        	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else    // Individual               	
                    if ( ( o instanceof org.pg.eti.kask.sova.nodes.IndividualNode)
                      		&&(!FilterOptions.isIndividual())) {
                        ((VisualItem)item).setVisible(false);
                            //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                          	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else // Indywidual differentFrom/allDifferenr                 	
                    if ( ( o instanceof org.pg.eti.kask.sova.nodes.DifferentNode)
                       		&&(!FilterOptions.isIndividual() || !FilterOptions.isDifferent())) {
                        ((VisualItem)item).setVisible(false);
                         //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                           	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        } 
                        
                    }else // Indywidual -  sameAs                 	
                    if ( ( o instanceof org.pg.eti.kask.sova.nodes.SameAsNode)
                           &&(!FilterOptions.isIndividual() || !FilterOptions.isSameAs())) {
                       ((VisualItem)item).setVisible(false);
                       //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                           	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                    }else  //Indywidual - oneOf                	
                    if ( ( o instanceof org.pg.eti.kask.sova.nodes.OneOfNode)
                       		&&(!FilterOptions.isIndividual() || !FilterOptions.isOneOf())) {
                        ((VisualItem)item).setVisible(false);
                        //usunięcie kwawędzi powiązanej z wierzchołkiem
                        Node n = ((Node)item);
                        Iterator egdesToRemove = n.edges();
                        while (egdesToRemove.hasNext()){
                           	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                        }
                   }else // Property node
                   if ( ( o instanceof org.pg.eti.kask.sova.nodes.PropertyNode)
                      		&&(!FilterOptions.isProperty() )) {
                       ((VisualItem)item).setVisible(false);
                       //usunięcie kwawędzi powiązanej z wierzchołkiem
                       Node n = ((Node)item);
                       Iterator egdesToRemove = n.edges();
                       while (egdesToRemove.hasNext()){
                          	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                       }
                   }else //Property - FunctionalProperty node
                   if ( ( o instanceof org.pg.eti.kask.sova.nodes.FunctionalPropertyNode)
                      		&&(!FilterOptions.isProperty() || !FilterOptions.isFunctionalProperty())) {
                       ((VisualItem)item).setVisible(false);
                       //usunięcie kwawędzi powiązanej z wierzchołkiem
                       Node n = ((Node)item);
                       Iterator egdesToRemove = n.edges();
                       while (egdesToRemove.hasNext()){
                          	((VisualItem)egdesToRemove.next()).setVisible(false); 	
                       }
                  }else //Property - InverseFunctionalProperty node
                  if ( ( o instanceof org.pg.eti.kask.sova.nodes.InverseFunctionalPropertyNode)
                     		&&(!FilterOptions.isProperty() || !FilterOptions.isInverseFunctionalProperty())) {
                      ((VisualItem)item).setVisible(false);
                      //usunięcie kwawędzi powiązanej z wierzchołkiem
                      Node n = ((Node)item);
                      Iterator egdesToRemove = n.edges();
                      while (egdesToRemove.hasNext()){
                          ((VisualItem)egdesToRemove.next()).setVisible(false); 	
                      }
                  }else //Property - SymmetricProperty node
                  if ( ( o instanceof org.pg.eti.kask.sova.nodes.SymmetricPropertyNode)
                     		&&(!FilterOptions.isProperty() || !FilterOptions.isSymmetricProperty())) {
                      ((VisualItem)item).setVisible(false);
                      //usunięcie kwawędzi powiązanej z wierzchołkiem
                      Node n = ((Node)item);
                      Iterator egdesToRemove = n.edges();
                      while (egdesToRemove.hasNext()){
                          ((VisualItem)egdesToRemove.next()).setVisible(false); 	
                      }
                  }else //Property - TransitiveProperty node
                  if ( ( o instanceof org.pg.eti.kask.sova.nodes.TransitivePropertyNode)
                     		&&(!FilterOptions.isProperty() || !FilterOptions.isTransitiveProperty())) {
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
