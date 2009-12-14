package org.eti.kask.sova.visualization;

import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.filter.VisibilityFilter;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.visual.expression.InGroupPredicate;

/**
 * Klasa obsługi wizualizacji.
 * @author piotr29
 */
abstract public class OVVisualization extends Visualization {

    private OVPredicate filterOVPredicate;
    protected static final String GRAPH = "graph";
    public static final String LAYOUT_ACTION = "layout";
    protected static final String FILTER_ACTION = "filter";

    public OVVisualization() {
        super();
    }

    /**
     * ustawienie podstawowego layoutu i domyślnych filtrów.
     */
    public void setVisualizationSettings() {
        setVisualizationRender();
        setVisualizationFilter();
        setDistanceFilter();
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
        filterOVPredicate = new OVPredicate();
        VisibilityFilter nodeDegreeFilter = new VisibilityFilter(filterOVPredicate);
        ActionList filter = new ActionList();
        filter.add(nodeDegreeFilter);

        this.putAction(FILTER_ACTION, filter);
    }

    protected void setDistanceFilter() {
        //TODO: dodanie filtra na odległości 
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
        this.cancel(FILTER_ACTION);
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
}
