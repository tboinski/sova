/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.pg.eti.kask.sova.graph.Constants;
import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import org.pg.eti.kask.sova.graph.OWLtoHierarchyTreeConverter;
import org.pg.eti.kask.sova.visualization.annotation.AnnotationComponent;
import org.pg.eti.kask.sova.visualization.annotation.AnnotationListener;
import org.pg.eti.kask.sova.visualization.annotation.IRIInfoComponent;
import org.pg.eti.kask.sova.visualization.annotation.IRIInfoListener;
import org.semanticweb.owlapi.model.OWLOntology;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.SearchTupleSet;
import prefuse.util.FontLib;
import prefuse.util.ui.JSearchPanel;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Display wizualizowanej ontologii. Pozwala na generowanie graficznej
 * reprezentacji ontologii na podstawie podanego obiektu ontologii.
 * @author Piotr Kunowski
 */
public class OVDisplay extends Display {

    public static final int FORCE_DIRECTED_LAYOUT = 1;
    public static final int RADIAL_TREE_LAYOUT = 2;
    public static final int FRUCHTERMAN_REINGOLD_LAYOUT = 3;
    private int graphLayout = FORCE_DIRECTED_LAYOUT;
    private Graph graph = null;
    private OVVisualization visualizationForceDirected = null;
    private OVVisualization visualizationRadialGraph = null;
    private OVVisualization visualizationFruchtermanReingold = null;
    private OVVisualization visualizationTree = null;
    private boolean canPan = true;
    private OWLOntology ontology = null;

    private OVVisualization getGraphLayoutVis() {

        switch (graphLayout) {
            case 1:
                if (visualizationForceDirected == null) {
                    initForceDirectedVis();
                }
                return visualizationForceDirected;
            case 2:
                if (visualizationRadialGraph == null) {
                    initRadialGraphVis();
                }
                return visualizationRadialGraph;
            case 3:
                if (visualizationFruchtermanReingold == null) {
                    initFruchtermanReingoldVis();
                }
                return visualizationFruchtermanReingold;
        }
        if (visualizationForceDirected == null) {
            initForceDirectedVis();
        }
        return visualizationForceDirected;
    }

    private void initFruchtermanReingoldVis() {
        visualizationFruchtermanReingold = new FruchtermanReingoldVis();
        VisualGraph visualGraph = visualizationFruchtermanReingold.addGraph(Constants.GRAPH, this.getGraph());
        visualizationFruchtermanReingold.setVisualizationSettings();
        if (visualGraph.getNodeCount() > 0) {
            VisualItem currentClass = (VisualItem) visualGraph.getNode(0);
            visualizationFruchtermanReingold.getGroup(Visualization.FOCUS_ITEMS).setTuple(currentClass);
            currentClass.setFixed(true);
        }
    }

    private void initForceDirectedVis() {
        visualizationForceDirected = new ForceDirectedVis();
        VisualGraph visualGraph = visualizationForceDirected.addGraph(Constants.GRAPH, this.getGraph());
        visualizationForceDirected.setVisualizationSettings();
        // ustawienie podswietlonej klasy
        if (visualGraph.getNodeCount() > 0) {
            VisualItem currentClass = (VisualItem) visualGraph.getNode(0);
            visualizationForceDirected.getGroup(Visualization.FOCUS_ITEMS).setTuple(currentClass);
            currentClass.setFixed(true);
        }
        if (canPan) {
            pan(350, 350);
            canPan = false;
        }

    }

    private void initRadialGraphVis() {
        visualizationRadialGraph = new RadialGraphVis();
//		visualizationRadialGraph = getGraphLayoutVis();
        visualizationRadialGraph.addGraph(Constants.GRAPH, this.getGraph());
        visualizationRadialGraph.setVisualizationSettings();

    }

    /**
     * Ustawienie trybu wyświetlania
     * @param graphLayout
     */
    public void setGraphLayout(int graphLayout) {
        this.graphLayout = graphLayout;
    }

    public int getGraphLayout() {
        return graphLayout;
    }

    @Override
    public OVVisualization getVisualization() {
        return getGraphLayoutVis();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public synchronized OWLOntology getOntology() {
        return ontology;
    }

    public synchronized void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OVDisplay(OWLOntology ontology) {
        this();
        this.ontology = ontology;
    }

    public OVDisplay() {
        super();
        //włączenie pełnego odświeżania
        this.setDamageRedraw(false);
        this.setSize(1500, 1500);
        this.setHighQuality(true);
//        PaintListener m_debug  = new DebugStatsPainter();
//        addPaintListener(m_debug);
        graph = new Graph();
//        this.setItemSorter(new TreeDepthItemSorter());
        this.addControlListener(new DragControl()); // drag items around
        this.addControlListener(new PanControl());  // pan with background left-drag
        this.addControlListener(new ZoomControl()); // zoom with vertical right-drag
        this.addControlListener(new WheelZoomControl());
        this.addControlListener(new NeighborHighlightControl());
        this.addControlListener(new ZoomToFitControl());
        addControlListener(new FocusControl(1, RadialGraphVis.FILTERS));
        addControlListener(new HoverActionControl(OVVisualization.REPAINT_ACTION));
    }

    /**
     * Dodaje komponent nasłuchujący na zmiany zaznaczonego wierzchołka 
     * @param component
     * @param manager
     */
    public void addAnnotationComponent(AnnotationComponent component) {
        this.addControlListener(new AnnotationListener(component, ontology));
    }

    /**
     * Dodaje komponent nasłuchujący na zmiany wskazanego wierzchołka  
     * @param component
     */
    public void addIRIInfoComponent(IRIInfoComponent component) {
        this.addControlListener(new IRIInfoListener(component));
    }

    /**
     * metoda wizualizuje zadaną ontologię
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateGraphFromOWl(OWLOntology ont) {
        setOntology(ont);
        generateGraphFromOWl();
    }

    /**
     * metoda wizualizuje zadaną ontologię
     */
    public void generateGraphFromOWl() {
        try {
            OWLtoGraphConverter con = new OWLtoGraphConverter();
            this.setGraph(con.OWLtoGraph(getOntology()));
            OVVisualization vis = getGraphLayoutVis();
            this.setVisualization(vis);
            vis.startLayout();
            addJSearch();
        } catch (Exception ex) {
            Logger.getLogger(OVDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * metoda generuje drzewo wnioskowania dla zadanej obiektu owl
     */
    public void generateTreeFromOWl() {
        try {
            // this.setGraph(OWLtoGraphConverter.getInstance().OWLtoGraph(ont));
            visualizationTree = new OVNodeLinkTreeLayout();
            OWLtoHierarchyTreeConverter con = new OWLtoHierarchyTreeConverter();
            visualizationTree.add(Constants.TREE, con.OWLtoTree(getOntology()));
            visualizationTree.setVisualizationSettings();
            this.setVisualization(visualizationTree);
            setItemSorter(new TreeDepthItemSorter());
        } catch (Exception ex) {
            Logger.getLogger(OVDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * metoda generuje drzewo wnioskowania dla zadanej obiektu owl
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateTreeFromOWl(OWLOntology ont) {
        setOntology(ont);
        generateTreeFromOWl();
    }

    /**
     * Zmiana algorytmu wizualizacji
     * @param visLayout
     */
    public void changeVisualizationLayout(int visLayout) {
        this.setGraphLayout(visLayout);
        this.removeAll();
        this.setVisualization(getGraphLayoutVis());
        this.repaint();
        this.getVisualization().refreshFilter();
    }

    public void removeDisplayVis() {
        try {
            if (visualizationForceDirected != null) {
                visualizationForceDirected.reset();
            }
            visualizationForceDirected = null;
            if (visualizationRadialGraph != null) {
                visualizationRadialGraph.reset();
            }
            visualizationRadialGraph = null;
            if (visualizationTree != null) {
                visualizationTree.reset();
            }
            visualizationTree = null;
            graph.clear();
            graph.clearSpanningTree();
            graph.removeAllGraphModelListeners();
            graph.removeAllSets();
            graph = new Graph();
        } catch (Exception e) {
        }
    }

    /**
     * Wyświetla całe drzewo wywnioskowanej hierarchii 
     */
    public void showFullTree() {
        if (visualizationTree != null) {
            visualizationTree.setDistance(100);
            visualizationTree.refreshFilter();
        }
    }

    /**
     * Ukrywa drzewo wywnioskowanej hierarchii do podstawowej postaci 
     */
    public void hideFullTree() {
        if (visualizationTree != null) {
            visualizationTree.setDistance(2);
            visualizationTree.refreshFilter();
        }
    }
    private JPanel jSearch = null;

    public JPanel getSearchPanel() {
        if (jSearch == null) {
            jSearch = new JPanel();
        }
        return jSearch;
    }

    private void addJSearch() {
        SearchQueryBinding sq = new SearchQueryBinding(
                (Table) getVisualization().getGroup("graph.nodes"), "node.name",
                (SearchTupleSet) getVisualization().getGroup(Visualization.SEARCH_ITEMS));
        JSearchPanel search = sq.createSearchPanel();
        search.setShowResultCount(true);
        search.setBorder(BorderFactory.createEmptyBorder(5, 5, 4, 0));
        search.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));
        jSearch = (JPanel) search;
    }

    public void saveFullImage(FileOutputStream os, double scale) {
        try {
            Rectangle2D bounds = this.getVisualization().getBounds(Visualization.ALL_ITEMS);
//             Adding some margins here
            double width = bounds.getWidth() * scale + 100;
            double height = bounds.getHeight() * scale + 100;
            Display display = new Display(this.getVisualization());
//            display.pan(-bounds.getX() + 50, -bounds.getY() + 50);
//            display.panTo(new Point((int)(width/2), (int)(height/2)));
            display.pan((int) (width / 2), (int) (height / 2));
            display.zoom(new Point((int) (width / 2), (int) (height / 2)), 4);
            BufferedImage img = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) img.getGraphics();
            display.paintDisplay(g, new Dimension((int) width, (int) height));
            ImageIO.write(img, "png", os);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
