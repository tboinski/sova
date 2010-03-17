package org.eti.kask.sova.visualization;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ui.JValueSlider;
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
    protected static final String FILTER_DISTANCE ="distance_filter";
    protected GraphDistanceFilter filterDist = null;
    private OVItemFilter itemVisualizationFilter = null;



    public OVVisualization() {
        super();
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
    public void setVisualizationSettings() {
        setVisualizationRender();
        setVisualizationFilter();
//        setDistanceFilter();
        setVisualizationLayout();
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
    }

    /**
     * dodanie filtrów wizualizacji. 
     */
    protected void setVisualizationFilter() {
        ActionList filter = new ActionList();
//        filterOVPredicate = new OVPredicate();
//        VisibilityFilter nodeDegreeFilter = new VisibilityFilter(/*this,"graph.edges",*/ filterOVPredicate);
//        itemVisualizationFilter = new OVItemFilter();
//
//        filter.add(itemVisualizationFilter);

        this.putAction(FILTER_ACTION, filter);
    }
    protected void restartTupleSetListeners(String gruop){
  //  TupleSet focusGroup = this.getGroup(Visualization.FOCUS_ITEMS);
    }
    protected void setDistanceFilter() {
		// ustawienie podswietlonej klasy
//		VisualGraph visualGraph = (VisualGraph)this.getSourceData(GRAPH);
//		if(visualGraph.getNodeCount() > 0) {
//			VisualItem currentClass = (VisualItem) visualGraph.getNode(0);
//			this.getGroup(Visualization.FOCUS_ITEMS).setTuple(currentClass);
//			currentClass.setFixed(true);
//		}

        ActionList filterDistance = new ActionList();
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
             
                run(FILTER_DISTANCE);

            }
        });
        itemVisualizationFilter = new OVItemFilter();
        filterDist = new GraphDistanceFilter(GRAPH, FilterOptions.distance);
        filterDistance.add(filterDist);
        filterDistance.add(itemVisualizationFilter);
        this.putAction(FILTER_DISTANCE, filterDistance);

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
    abstract void setVisualizationLayout();

    /** odswieżenie filtrów, funkcja powinna byc wywołana po zmianach w predykacie
     * filtrów. Lub innych zmianach związanych z filtrami.
     *
     **/
    public void refreshFilter() {
        filterDist.run();
        itemVisualizationFilter.run();
        this.run(FILTER_ACTION);
       
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
        this.cancel(LAYOUT_ACTION);

    }

    /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
    public void startLayout() {
        this.run(LAYOUT_ACTION);
    }

     /**
     * funkcja włączająca samorozmieszczanie - grawitację obiektów
     */
    public void startDistanceFilter() {
        this.runAfter(FILTER_DISTANCE, FILTER_ACTION);
    }
    /**
     * Panel ustawianie dystansu w grafie
     * @return panel ustawuień
     */
    public JPanel getDistanceControlPanel(){
        final JValueSlider slider = new JValueSlider("Distance", 1, 15, 1);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
               setDistance(slider.getValue().intValue());
               refreshFilter();

            }
        });
        slider.setBackground(Color.WHITE);
        slider.setPreferredSize(new Dimension(290,30));
        slider.setMaximumSize(new Dimension(290,30));

        Box cf = new Box(BoxLayout.Y_AXIS);
        cf.add(slider);
        cf.setBorder(BorderFactory.createTitledBorder("Connectivity Filter"));
        JPanel panel = new JPanel();
        panel.add(cf);
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(310,60));
        panel.setMaximumSize(new Dimension(310,60));
        return panel;
    }
}
