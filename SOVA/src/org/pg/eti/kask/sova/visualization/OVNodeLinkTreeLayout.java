/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.sova.visualization;

import java.awt.geom.Point2D;

import org.pg.eti.kask.sova.graph.Constants;


import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.FisheyeTreeFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * 
 * @author Piotr Kunowski
 *
 */
public class OVNodeLinkTreeLayout extends OVVisualization{
	private int distance =2;
	
    private static final String TREE = "tree";
    private static final String TREE_NODES = "tree.nodes";
    private static final String TREE_EDGES = "tree.edges";
    
    private LabelRenderer m_nodeRenderer;
    private EdgeRenderer m_edgeRenderer;
    private int m_orientation = prefuse.Constants.ORIENT_LEFT_RIGHT;
    private FisheyeTreeFilter fishTreeFilter = null;
    private FisheyeTreeFilter getFishTreeFilter(){
    	if (fishTreeFilter== null){
    		fishTreeFilter = new FisheyeTreeFilter(TREE, distance);
    	}
    	return fishTreeFilter;
    }
    /**
     * ustawia wilekość wyświetlanego drzewa. 
     */
    public void setDistance(int distance){
    	this.distance = distance;
    	getFishTreeFilter().setDistance(distance);
    }
	@Override
	public void setVisualizationLayout() {

        m_nodeRenderer =  (LabelRenderer) new NodeRenderer(Constants.TREE_NODES);
        m_nodeRenderer.setHorizontalAlignment(prefuse.Constants.LEFT);
        m_edgeRenderer = new EdgeRenderer(prefuse.Constants.EDGE_TYPE_CURVE);
        
        m_edgeRenderer.setHorizontalAlignment1(prefuse.Constants.RIGHT);
        m_edgeRenderer.setHorizontalAlignment2(prefuse.Constants.LEFT);
		m_edgeRenderer.setVerticalAlignment1(prefuse.Constants.CENTER);
		m_edgeRenderer.setVerticalAlignment2(prefuse.Constants.CENTER);
        
        
        DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
        rf.add(new InGroupPredicate(TREE_EDGES), m_edgeRenderer);
        this.setRendererFactory(rf);
        
        

        
		// krawedzie nie sa interaktywne
		this.setValue(Constants.TREE_EDGES, null, VisualItem.INTERACTIVE, Boolean.FALSE);
      
        // colors
        ItemAction nodeColor = new NodeColorAction(TREE_NODES);
        ItemAction textColor = new ColorAction(TREE_NODES,
                VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0));
        this.putAction("textColor", textColor);
        
        ItemAction edgeColor = new ColorAction(TREE_EDGES,
                VisualItem.STROKECOLOR, ColorLib.rgb(200,200,200));
        
        // quick repaint
        ActionList repaint = new ActionList();
        repaint.add(nodeColor);
        repaint.add(new RepaintAction());
        this.putAction("repaint", repaint);
        
        // full paint
        ActionList fullPaint = new ActionList();
        fullPaint.add(nodeColor);
        this.putAction("fullPaint", fullPaint);
        
        // animate paint change
        ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator(TREE_NODES));
        animatePaint.add(new RepaintAction());
        this.putAction("animatePaint", animatePaint);
        
        // create the tree layout action
        NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(TREE,
                m_orientation, 50, 3, 8);
        treeLayout.setLayoutAnchor(new Point2D.Double(25,300));
        this.putAction("treeLayout", treeLayout);
        
        CollapsedSubtreeLayout subLayout = 
            new CollapsedSubtreeLayout(TREE, m_orientation);
        this.putAction("subLayout", subLayout);
        
        AutoPanAction autoPan = new AutoPanAction();
        
        // create the filtering and layout
        ActionList filter = new ActionList();
        fishTreeFilter = new FisheyeTreeFilter(TREE, distance);
        filter.add(fishTreeFilter);
        filter.add(new FontAction(TREE_NODES, FontLib.getFont("Tahoma", 12)));
        filter.add(treeLayout);
        filter.add(subLayout);
        filter.add(textColor);
        filter.add(nodeColor);
        filter.add(edgeColor);
        this.putAction(FILTERS, filter);
        
        // animated transition
        ActionList animate = new ActionList(1000);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(autoPan);
        animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator(TREE));
        animate.add(new LocationAnimator(TREE_NODES));
        animate.add(new ColorAnimator(TREE_NODES));
        animate.add(new RepaintAction());
        this.putAction("animate", animate);
        this.alwaysRunAfter(FILTERS, "animate");
        
        // create animator for orientation changes
        ActionList orient = new ActionList(2000);
        orient.setPacingFunction(new SlowInSlowOutPacer());
        orient.add(autoPan);
        orient.add(new QualityControlAnimator());
        orient.add(new LocationAnimator(TREE_NODES));
        orient.add(new RepaintAction());
        this.putAction("orient", orient);
        this.run(FILTERS);
        
        TupleSet search = new PrefixSearchTupleSet(); 
        this.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                cancel("animatePaint");
                run("fullPaint");
                run("animatePaint");
            }
        });
    

	}
	@Override
	public void refreshFilter() {
		this.run(FILTERS);
		this.repaint();
	}
    public static class NodeColorAction extends ColorAction {
        
        public NodeColorAction(String group) {
            super(group, VisualItem.FILLCOLOR);
        }
        
        public int getColor(VisualItem item) {
            if ( m_vis.isInGroup(item, Visualization.SEARCH_ITEMS) )
                return ColorLib.rgb(255,190,190);
            else if ( m_vis.isInGroup(item, Visualization.FOCUS_ITEMS) )
                return ColorLib.rgb(198,229,229);
            else if ( item.getDOI() > -1 )
                return ColorLib.rgb(164,193,193);
            else
                return ColorLib.rgba(255,255,255,0);
        }
        
    }
    
    /**
     * ustawia render wierzchołków w wizualizacji
     */
    protected void setVisualizationRender() {
//        LabelRenderer r = (LabelRenderer) new NodeRenderer(Constants.TREE_NODES);
//        prefuse.render.EdgeRenderer er =  new prefuse.render.EdgeRenderer(prefuse.Constants.EDGE_TYPE_CURVE);
//        DefaultRendererFactory drf = new DefaultRendererFactory(r);
//        drf.add(new InGroupPredicate("tree.edges"), er);
//        this.setRendererFactory(drf);
//        // ustawienie krawędzi jako nieaktywne 
//    	this.setValue("tree.edges", null, VisualItem.INTERACTIVE, Boolean.FALSE);
    } 
    public void refreshVisualization() {}
    
    public class AutoPanAction extends Action {
        private Point2D m_start = new Point2D.Double();
        private Point2D m_end   = new Point2D.Double();
        private Point2D m_cur   = new Point2D.Double();
        private int     m_bias  = 150;
        
        public void run(double frac) {
            TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
            if ( ts.getTupleCount() == 0 )
                return;
            
            if ( frac == 0.0 ) {
                int xbias=0, ybias=0;
                switch ( m_orientation ) {
                case prefuse.Constants.ORIENT_LEFT_RIGHT:
                    xbias = m_bias;
                    break;
                case prefuse.Constants.ORIENT_RIGHT_LEFT:
                    xbias = -m_bias;
                    break;
                case prefuse.Constants.ORIENT_TOP_BOTTOM:
                    ybias = m_bias;
                    break;
                case prefuse.Constants.ORIENT_BOTTOM_TOP:
                    ybias = -m_bias;
                    break;
                }

                VisualItem vi = (VisualItem)ts.tuples().next();
//                m_cur.setLocation(getWidth()/2, getHeight()/2);
//                getAbsoluteCoordinate(m_cur, m_start);
                m_end.setLocation(vi.getX()+xbias, vi.getY()+ybias);
            } else {
                m_cur.setLocation(m_start.getX() + frac*(m_end.getX()-m_start.getX()),
                                  m_start.getY() + frac*(m_end.getY()-m_start.getY()));
//                panToAbs(m_cur);
            }
        }
    }
}
