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

package main.java.org.pg.eti.kask.sova.visualization;

import main.java.org.pg.eti.kask.sova.graph.Constants;
import org.semanticweb.owlapi.model.OWLOntology;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
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
 * @author Piotr Kunowski
 */
abstract public class OVVisualization extends Visualization {

    private OVPredicate filterOVPredicate;
    protected static final String GRAPH = "graph";
    public static final String LAYOUT_ACTION = "layout";
    protected static final String FILTER_ACTION = "filter";
    protected static final String FILTER_ITEM = "item_filter";
    protected static final String FILTERS ="filters_items_dist";
    public static final String REPAINT_ACTION = "repaint";
    protected GraphDistanceFilter filterDist = null;
    protected OVItemFilter itemVisualizationFilter = null;
    protected boolean gravitation = true;

        
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
    public void setVisualizationSettings() {
        setVisualizationRender();
        
        setVisualizationLayout();
    }
    /** inicjalizacja filtru elementów  - zmienna itemVisualizationFilter
     * 
     */
    protected void initItemVisualizationFilter(){
    	itemVisualizationFilter = new OVItemFilter();
    }
    
    protected void addRepaintAction(){
        ActionList repaint = new ActionList();
         repaint.add(new RepaintAction());
         this.putAction("repaint", repaint);
    }
    /**
     * inicjalizacja zmiennej filtru filterDist
     */
    protected void initFilterDist(){
    
          TupleSet focusGroup = this.getGroup(Visualization.FOCUS_ITEMS);
          focusGroup.addTupleSetListener(new TupleSetListener() {
              public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem)
              {
                  for ( int i=0; i<rem.length; ++i )
                      ((VisualItem)rem[i]).setFixed(false);
                  for ( int i=0; i<add.length; ++i ) {
                      ((VisualItem)add[i]).setFixed(false);
                      ((VisualItem)add[i]).setFixed(true);
                  }
                  if ( ts.getTupleCount() == 0 ) {
                      ts.addTuple(rem[0]);
                      ((VisualItem)rem[0]).setFixed(false);
                  }
               
                  run(FILTERS);
                  if (!gravitation){
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


    protected void addSearch(){
        ItemAction nodeColor = new NodeColorAction("graph.nodes");
        ItemAction textColor = new TextColorAction("graph.nodes");
        this.putAction("textColor", textColor);
        
        
        FontAction fonts = new FontAction("graph.nodes", 
                FontLib.getFont("Tahoma", 10));
        fonts.add("ingroup('_search_')", FontLib.getFont("Tahoma", 20));
        
        // recolor
        ActionList recolor = new ActionList();
        recolor.add(nodeColor);
        recolor.add(textColor);
        this.putAction("recolor", recolor);
        SearchTupleSet search = new PrefixSearchTupleSet();
        this.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                
                run("recolor");
                
            }
        });
    }
    
    protected void addFilters() {
		ActionList filtersItmDist = new ActionList();
    	if (!FilterOptions.isDistanceFilter()) return;
    	
    	if (itemVisualizationFilter == null) initItemVisualizationFilter();
        if (filterDist==null) initFilterDist();
    	filtersItmDist.add(filterDist);
        filtersItmDist.add(itemVisualizationFilter);
        this.putAction(FILTERS, filtersItmDist);

    }
   

    public void removeFilters(){
    	this.cancel(FILTERS);
    	removeAction(FILTERS);
    }
    
    public void removeLayoutAction(){
    	this.cancel(LAYOUT_ACTION);
    	removeAction(LAYOUT_ACTION);
    }
    /**
     * ustawienie dystansu dla w filtrze odległościowym
     *
     * @param distance nowa odległość
     */
    public void setDistance(int distance){
        if (filterDist!=null){
            filterDist.setDistance(distance);
        }
    }
    /**
     *
     * @return srednica grafu
     */
    public int getDistance(){
        int ret=-1;
        if (filterDist!=null){
            ret = filterDist.getDistance();
        }
        return ret;
    }
    /**
     * 
     */
    public abstract void setVisualizationLayout();

    /** odswieżenie filtrów, funkcja powinna byc wywołana po zmianach w predykacie
     * filtrów. Lub innych zmianach związanych z filtrami.
     *
     **/
    public void refreshFilter() {
        if (filterDist!=null) filterDist.run();
        if (itemVisualizationFilter!=null)itemVisualizationFilter.run();
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
            super(group, VisualItem.FILLCOLOR, ColorLib.rgba(255,255,255,0));
            add("_hover", ColorLib.gray(220,230));
            add("ingroup('_search_')", ColorLib.rgb(255,190,190));
            add("ingroup('_focus_')", ColorLib.rgb(198,229,229));
        }
                
    } // end of inner class NodeColorAction
    
    /**
     * Set node text colors
     */
    public static class TextColorAction extends ColorAction {
        public TextColorAction(String group) {
            super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
            add("_hover", ColorLib.rgb(255,0,0));
        }
    } // end of inner class TextColorAction
    
}
