/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 * Copyright (c) 2016 Wojciech Zielonka
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

import org.pg.eti.kask.sova.graph.Constants;
import prefuse.Display;

import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;

import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;

/**
 * klasa wizualizująca graf w oparciu o algorytm RadialGraph
 *
 * @author Piotr Kunowski
 */
public class RadialGraphVis extends OVVisualization {

    private static final String tree = Constants.GRAPH;
    private static final String treeNodes = Constants.GRAPH_NODES;
    private static final String linear = "linear";
    private RadialTreeLayout treeLayout;
    private double Size;
    
    public RadialTreeLayout getRadialTreeLayout(){
        return this.treeLayout;
    }
    
    public void setRadius(double radius){
        this.treeLayout.setAutoScale(false);
        this.treeLayout.setRadiusIncrement(radius);
        this.treeLayout.run();
    } 
    
    public RadialGraphVis(double actualSize){
        super();
        this.Size = actualSize;
    }
    
    @Override
    public void setVisualizationLayout(Display d) {
  
        addRepaintAction();
   
        // create the tree layout action
        treeLayout = new RadialTreeLayout(tree, (int) Size);
        this.putAction("treeLayout", treeLayout);
        treeLayout.setAutoScale(true);
        CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree);
        this.putAction("subLayout", subLayout);
        
        // create the filtering and layout
        ActionList filter = new ActionList();
        filter.add(new TreeRootAction(tree, this));
        filter.add(treeLayout);
        filter.add(subLayout);
        if (itemVisualizationFilter == null) {
            initItemVisualizationFilter();
        }
        filter.add(itemVisualizationFilter);

        this.putAction(FILTERS, filter);

        // animated transition
        ActionList animate = new ActionList(1250);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator(tree));
        animate.add(new PolarLocationAnimator(treeNodes, linear));
        animate.add(new ColorAnimator(treeNodes));
        animate.add(new RepaintAction());
        this.putAction("animate", animate);
        this.alwaysRunAfter(FILTERS, "animate");

        // filter graph and perform layout
        this.run(FILTERS);

        // maintain a set of items that should be interpolated linearly
        // this isn't absolutely necessary, but makes the animations nicer
        // the PolarLocationAnimator should read this set and act accordingly
        this.addFocusGroup(linear, new DefaultTupleSet());
        this.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(
                new TupleSetListener() {
                    public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                        TupleSet linearInterp = getGroup(linear);
                        if (add.length < 1) {
                            return;
                        }
                        linearInterp.clear();
                        if (add.length > 0) {
                            for (Node n = (Node) add[0]; n != null; n = n.getParent()) {
                                linearInterp.addTuple(n);
                            }
                        }
                    }
                }
        );   
        
        addSearch(Constants.treeNodes, d);   
    }

    /**
     * inicjalizacja filtru elementów - zmienna itemVisualizationFilter
     *
     */
    protected void initItemVisualizationFilter() {
        super.initItemVisualizationFilter();
        itemVisualizationFilter.setRememberOldState(false);
    }

    /**
     * funkcja wyłączająca samorozmieszczanie - grawitację obiektów
     */
    public void stopLayout() {
        gravitation = false;
        this.cancel("animate");
        this.cancel(LAYOUT_ACTION);

    }

    /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
    public void startLayout() {
        gravitation = true;
        this.run(FILTERS);
    }

    /**
     * Switch the root of the tree by requesting a new spanning tree at the
     * desired root
     */
    public class TreeRootAction extends GroupAction {

        public TreeRootAction(String graphGroup, Visualization vis) {
            super(graphGroup);
            m_vis = vis;

        }

        public void run(double frac) {
            TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
            if (focus == null || focus.getTupleCount() == 0) {
                return;
            }

            Graph g = (Graph) m_vis.getGroup(m_group);
            Node f = null;
            Iterator tuples = focus.tuples();
            while (tuples.hasNext() && !g.containsTuple(f = (Node) tuples.next())) {
                f = null;
            }
            if (f == null) {
                return;
            }

            if (RadialGraphVis.super.getSpanningTreeMode().isSelected()) {
                g.getSpanningTree(f);
            }
        }
    }
}
