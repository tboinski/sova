/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eti.kask.sova.visualization;

import prefuse.Visualization;
import prefuse.action.filter.GraphDistanceFilter;

/**
 *
 * @author piotr29
 */
public class OVGraphDistanceFilter extends GraphDistanceFilter {

    /**
     * Create a new GraphDistanceFilter that processes the given data group
     * and uses a graph distance of 1. By default, the
     * {@link prefuse.Visualization#FOCUS_ITEMS} group will be used as the
     * source nodes from which to measure the distance.
     * @param group the group to process. This group should resolve to a
     * Graph instance, otherwise exceptions will be thrown when this
     * Action is run.
     */
    public OVGraphDistanceFilter(String group) {
        this(group, 1);
    }

    /**
     * Create a new GraphDistanceFilter that processes the given data group
     * and uses the given graph distance. By default, the
     * {@link prefuse.Visualization#FOCUS_ITEMS} group will be used as the
     * source nodes from which to measure the distance.
     * @param group the group to process. This group should resolve to a
     * Graph instance, otherwise exceptions will be thrown when this
     * Action is run.
     * @param distance the graph distance within which items will be
     * visible.
     */
    public OVGraphDistanceFilter(String group, int distance) {
        this(group, Visualization.FOCUS_ITEMS, distance);
    }

    /**
     * Create a new GraphDistanceFilter that processes the given data group
     * and uses the given graph distance.
     * @param group the group to process. This group should resolve to a
     * Graph instance, otherwise exceptions will be thrown when this
     * Action is run.
     * @param sources the group to use as source nodes for measuring
     * graph distance.
     * @param distance the graph distance within which items will be
     * visible.
     */
    public OVGraphDistanceFilter(String group, String sources, int distance)
    {
        super(group,sources,distance);
    }


    @Override
     public void run(double frac) {
     System.out.println("filtr "+frac);
     super.run(frac);
         // mark the items

    }

}
