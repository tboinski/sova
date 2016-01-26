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


import java.awt.geom.Point2D;
import javax.swing.JCheckBox;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * Klasa obsługi wizualizacji.
 *
 * @author Piotr Kunowski
 */
abstract public class OVVisualization extends Visualization {

    private OVPredicate filterOVPredicate;   
    protected static final String tree = "tree";
    protected static final String treeNodes = "tree.nodes";
    protected static final String treeEdges = "tree.edges";
    protected static final String linear = "linear";   
    protected static final String GRAPH = "graph";
    protected static final String graphNodes = "graph.nodes";   
    public static final String LAYOUT_ACTION = "layout";
    protected static final String FILTER_ACTION = "filter";
    protected static final String FILTER_ITEM = "item_filter";
    protected static final String FILTERS = "filters_items_dist";
    public static final String REPAINT_ACTION = "repaint";
    protected GraphDistanceFilter filterDist = null;
    protected OVItemFilter itemVisualizationFilter = null;
    protected boolean gravitation = true;
    private JCheckBox spanningTreeBox;
    protected Display display;
    protected SearchTupleSet search;
    protected Tuple[] selectedItem = null;
    protected int ItemIterator = 0;
    
        
    protected void animateToItem(VisualItem item){
        Double x = item.getX();
        Double y = item.getY();
        display.animatePanToAbs(new Point2D.Double(x,y), 400);
    }
    
    public void handleKeyPress(int keyCode) {  
        if(selectedItem != null)
            switch (keyCode) {
                case 38://UP KEY       
                    if(ItemIterator == 0)
                        ItemIterator = selectedItem.length - 1;                   
                    animateToItem((VisualItem)selectedItem[ItemIterator]);
                    --ItemIterator; 
                    break;
                case 40://DOWN KEY
                    animateToItem((VisualItem)selectedItem[ItemIterator]);
                    ++ItemIterator; 
                    if(ItemIterator == selectedItem.length)
                        ItemIterator = 0;
                    break;
            }
    }
    
    enum VisualizationType{
        GRAPH, TREE
    }
    
    /**
     *
     * @return filtr na wyświetlane elementy
     */
    public OVItemFilter getItemVisualizationFilter() {
        return itemVisualizationFilter;
    }

    /**
     * ustawienie podstawowego layoutu i domyślnych filtrów.
     */
    public void setVisualizationSettings(Display d) {
        
        setVisualizationRender();
        setVisualizationLayout(d);
    }

    /**
     * inicjalizacja filtru elementów - zmienna itemVisualizationFilter
     *
     */
    protected void initItemVisualizationFilter() {
        itemVisualizationFilter = new OVItemFilter();
    }

    protected void addRepaintAction() {
        ActionList repaint = new ActionList();
        repaint.add(new RepaintAction());
        this.putAction("repaint", repaint);
    }

    /**
     * inicjalizacja zmiennej filtru filterDist
     */
    protected void initFilterDist() {

        TupleSet focusGroup = this.getGroup(Visualization.FOCUS_ITEMS);
        focusGroup.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
                for (int i = 0; i < rem.length; ++i) {
                    ((VisualItem) rem[i]).setFixed(false);
                }
                for (int i = 0; i < add.length; ++i) {
                    ((VisualItem) add[i]).setFixed(false);
                    ((VisualItem) add[i]).setFixed(true);
                }
                if (ts.getTupleCount() == 0) {
                    ts.addTuple(rem[0]);
                    ((VisualItem) rem[0]).setFixed(false);
                }

                run(FILTERS);
                if (!gravitation) {
                    refreshFilter();
                }

            }
        });

        filterDist = new GraphDistanceFilter(GRAPH, FilterOptions.getDistance());
    }

    /**
     * ustawia renderery dla krawędzi i wierzchołków dla wizualizacji
     */
    protected void setVisualizationRender() {
        LabelRenderer r = (LabelRenderer) new NodeRenderer("node");
        EdgeRenderer er = new EdgeRenderer();
        DefaultRendererFactory drf = new DefaultRendererFactory(r);
        drf.add(new InGroupPredicate("graph.edges"), er);
        this.setRendererFactory(drf);
        // ustawienie krawędzi jako nieaktywne 
        this.setValue("graph.edges", null, VisualItem.INTERACTIVE, Boolean.FALSE);

    }

    protected void addSearch(String type, Display d) {
        
        ItemAction nodeColor = new NodeColorAction(type);
        ItemAction textColor = new TextColorAction(type);
        this.display = d;
        // recolor
        ActionList recolor = new ActionList();
        recolor.add(nodeColor);
        recolor.add(textColor);
        putAction("recolor", recolor);

        // repaint
        ActionList repaint = new ActionList();
        repaint.add(recolor);
        repaint.add(new RepaintAction());
        putAction("repaint", repaint);
        
        // animate paint change
        ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator(type));
        animatePaint.add(new RepaintAction());
        putAction("animatePaint", animatePaint);
        
        FontAction fonts = new FontAction(type,
                FontLib.getFont("Tahoma", 10));
        fonts.add("ingroup('_search_')", FontLib.getFont("Tahoma", 20));
        
        search = new PrefixSearchTupleSet();
        addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                
                selectedItem = add;
                
                if(search.getQuery().length() == 0){
                    OVDisplay.class.cast(display).getSearchBox().repaint();
                    return;
                }
               
                cancel("animatePaint");
                run("recolor");
                run("animatePaint");
                
                if(selectedItem.length == 1){
                    animateToItem((VisualItem)selectedItem[0]);
                }
            }
        });
    }
    
    protected void addFilters() {
        ActionList filtersItmDist = new ActionList();
        if (!FilterOptions.isDistanceFilter()) {
            return;
        }

        if (itemVisualizationFilter == null) {
            initItemVisualizationFilter();
        }
        if (filterDist == null) {
            initFilterDist();
        }
        filtersItmDist.add(filterDist);
        filtersItmDist.add(itemVisualizationFilter);
        this.putAction(FILTERS, filtersItmDist);

    }

    public void removeFilters() {
        this.cancel(FILTERS);
        removeAction(FILTERS);
    }

    public void removeLayoutAction() {
        this.cancel(LAYOUT_ACTION);
        removeAction(LAYOUT_ACTION);
    }

    /**
     * ustawienie dystansu dla w filtrze odległościowym
     *
     * @param distance nowa odległość
     */
    public void setDistance(int distance) {
        if (filterDist != null) {
            filterDist.setDistance(distance);
        }
    }

    /**
     *
     * @return srednica grafu
     */
    public int getDistance() {
        int ret = -1;
        if (filterDist != null) {
            ret = filterDist.getDistance();
        }
        return ret;
    }

    /**
     *
     * @param d
     */
    public abstract void setVisualizationLayout(Display d);

    /**
     * odswieżenie filtrów, funkcja powinna byc wywołana po zmianach w
     * predykacie filtrów. Lub innych zmianach związanych z filtrami.
     *
     *
     */
    public void refreshFilter() {
        if (filterDist != null) {
            filterDist.run();
        }
        if (itemVisualizationFilter != null) {
            itemVisualizationFilter.run();
        }
        this.repaint();
    }

    public OVPredicate getFilterOVPredicate() {
        return filterOVPredicate;
    }

    public void setFilterOVPredicate(OVPredicate filterOVPredicate) {
        this.filterOVPredicate = filterOVPredicate;
    }

    /**
     * funkcja wyłączająca samorozmieszczanie - grawitację obiektów
     */
    public void stopLayout() {
        gravitation = false;
        this.cancel(LAYOUT_ACTION);

    }

    /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
    public void startLayout() {
        gravitation = true;
        this.run(LAYOUT_ACTION);
    }

    /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
    public void startFilters() {
        this.run(FILTERS);
    }

    /**
     * Set node fill colors
     */
    public static class NodeColorAction extends ColorAction {

        public NodeColorAction(String group) {
            super(group, VisualItem.FILLCOLOR, ColorLib.rgba(255, 255, 255, 0));
            add("_hover", ColorLib.gray(220, 230));
            add("ingroup('_search_')", ColorLib.rgb(255, 190, 190));
            add("ingroup('_focus_')", ColorLib.rgb(198, 229, 229));
        }

    } // end of inner class NodeColorAction

    /**
     * Set node text colors
     */
    public static class TextColorAction extends ColorAction {

        public TextColorAction(String group) {
            super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
            add("_hover", ColorLib.rgb(255, 0, 0));
        }
    } // end of inner class TextColorAction

    protected final void setSpanningTreeMode(JCheckBox box) {
        spanningTreeBox = box;
    }

    protected final JCheckBox getSpanningTreeMode() {
        return spanningTreeBox;
    }

}
