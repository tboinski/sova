package org.eti.kask.sova.visualization;

import java.util.Iterator;
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
import prefuse.data.tuple.TupleSet;

/**
 * klasa wizualizująca graf w oparciu o algorytm RadialGraph
 * @author piotr29
 */
public class RadialGraphVis extends OVVisualization {

    @Override
    void setVisualizationLayout() {
        // repaint
        ActionList repaint = new ActionList();
        //   repaint.add(recolor);
        repaint.add(new RepaintAction());
        this.putAction("repaint", repaint);

        // animate paint change
        ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator("node"));
        animatePaint.add(new RepaintAction());
        this.putAction("animatePaint", animatePaint);

        // create the tree layout action
        RadialTreeLayout treeLayout = new RadialTreeLayout(GRAPH);
        this.putAction("treeLayout", treeLayout);

        CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(GRAPH);
        this.putAction("subLayout", subLayout);

        // create the filtering and layout
        ActionList filterLayout = new ActionList();
        filterLayout.add(new TreeRootAction(GRAPH, this));
        filterLayout.add(treeLayout);
        filterLayout.add(subLayout);
        this.putAction(LAYOUT_ACTION, filterLayout);

        // animated transition
        ActionList animate = new ActionList(1250);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator(GRAPH));
        animate.add(new PolarLocationAnimator("node", "linear"));
        animate.add(new ColorAnimator("node"));
        animate.add(new RepaintAction());
        this.putAction("animate", animate);
        this.alwaysRunAfter("layout", "animate");
    }

    /**
     * Switch the root of the tree by requesting a new spanning tree
     * at the desired root
     */
    public static class TreeRootAction extends GroupAction {

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
            g.getSpanningTree(f);
        }
    }
}
