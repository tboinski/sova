/*
 *
 * Copyright (c) 2010 GdaÅ„sk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski RadosÅ‚aw
 * Copyright (c) 2010 OrÅ‚owski Piotr
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.Iterator;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.pg.eti.kask.sova.graph.Constants;
import org.pg.eti.kask.sova.graph.OWLtoGraphConverter;
import org.pg.eti.kask.sova.graph.OWLtoHierarchyTreeConverter;
import org.pg.eti.kask.sova.visualization.annotation.AnnotationComponent;
import org.pg.eti.kask.sova.visualization.annotation.AnnotationListener;
import org.pg.eti.kask.sova.visualization.annotation.IRIInfoComponent;
import org.pg.eti.kask.sova.visualization.annotation.IRIInfoListener;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
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
import org.pg.eti.kask.sova.nodes.ClassNode;
import org.pg.eti.kask.sova.nodes.Node;
import org.pg.eti.kask.sova.nodes.ObjectPropertyNode;
import org.pg.eti.kask.sova.nodes.DataPropertyNode;
import prefuse.data.tuple.TupleSet;

/**
 * Display wizualizowanej ontologii. Pozwala na generowanie graficznej
 * reprezentacji ontologii na podstawie podanego obiektu ontologii.
 *
 * @author Piotr Kunowski
 */
public class OVDisplay extends Display {

    public static final int FORCE_DIRECTED_LAYOUT = 1;
    public static final int RADIAL_TREE_LAYOUT = 2;
    public static final int FRUCHTERMAN_REINGOLD_LAYOUT = 3;
    public static final int NOTE_LINK_TREE_LAYOUT = 4;

    public enum VisualizationEnums {

        LABELS, ID
    };

    private int graphLayout = RADIAL_TREE_LAYOUT;
    private Graph graph = null;
    private OVVisualization visualizationForceDirected = null;
    private OVVisualization visualizationRadialGraph = null;
    private OVVisualization visualizationFruchtermanReingold = null;
    private OVVisualization visualizationNodeLinkTree = null;
    private OVVisualization visualizationTree = null;
    private JCheckBox spanningTreeBox;
    private boolean canPan = true;
    private OWLOntology ontology = null;

    // WyÅ›wietl w wÄ™zÅ‚ach wybrane przez uÅ¼ytkownika atrybuty
    // Label lub ID klasy
    public void changeGraphVisualization(VisualizationEnums e, JComboBox comboBox) {
        // Iteracja po wszytkich elementach ontologii
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        Iterator items = m_vis.items();
        while (items.hasNext()) {
            VisualItem item = (VisualItem) items.next();
            Object o = ((VisualItem) item).get(Constants.GRAPH_NODES);
            if (o instanceof ClassNode || o instanceof ObjectPropertyNode || o instanceof DataPropertyNode) {

                Object element = ((VisualItem) item).get(OWLtoGraphConverter.COLUMN_IRI);
                OWLClass currentClass = manager.getOWLDataFactory().getOWLClass(IRI.create(((IRI) element).toURI()));
                Set<OWLAnnotation> set = currentClass.getAnnotations(this.ontology);

                String stringPropoperty;
                String labelValue = "";
                String langValue = "";
                String labelValueWithoutLang = "";

                for (OWLAnnotation elem : set) {
                    OWLAnnotationProperty prop = elem.getProperty();
                    stringPropoperty = prop.getIRI().getFragment();
                    if (stringPropoperty.equals("label")) {
                        stringPropoperty = elem.getValue().toString();
                        if (stringPropoperty.contains("@")) {
                            labelValue = stringPropoperty.substring(0, stringPropoperty.length() - 3);
                            langValue = stringPropoperty.substring(stringPropoperty.length() - 2, stringPropoperty.length());
                        } else {
                            labelValueWithoutLang = stringPropoperty;
                        }

                        if (comboBox != null && langValue.equals(comboBox.getSelectedItem())) {
                            break;
                        }
                    }
                    langValue = "";
                    labelValue = "";
                }

                Node castedObject = Node.class.cast(o);

                switch (e) {
                    case LABELS:
                        if (!labelValue.isEmpty()) {
                            castedObject.setLabel(labelValue);
                        } else if (!labelValueWithoutLang.isEmpty()) {
                            castedObject.setLabel(labelValueWithoutLang);
                        }
                        break;

                    case ID:
                        castedObject.setLabel(currentClass.getIRI().getFragment());
                        break;

                    default:
                        break;
                }
                this.repaint();
            }
        }
    }

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
            case 4:
                if (visualizationNodeLinkTree == null) {
                    initNodeLinkTreeVis();
                }
                return visualizationNodeLinkTree;

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

    private void initNodeLinkTreeVis() {
        visualizationNodeLinkTree = new NodeLinkTreeVis();
        visualizationRadialGraph.setSpanningTreeMode(spanningTreeBox);
        visualizationNodeLinkTree.addGraph(Constants.GRAPH, this.getGraph());
        visualizationNodeLinkTree.setVisualizationSettings();
    }

    private void initRadialGraphVis() {
        visualizationRadialGraph = new RadialGraphVis();
        visualizationRadialGraph.setSpanningTreeMode(spanningTreeBox);
        visualizationRadialGraph.add(Constants.GRAPH, this.getGraph());
        visualizationRadialGraph.setVisualizationSettings();
    }

    /**
     * Ustawienie trybu wyÅ›wietlania
     *
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

    public void setSpanningTreeCheckBox(JCheckBox box) {
        this.spanningTreeBox = box;
    }

    public OVDisplay() {
        super();
        //wÅ‚Ä…czenie peÅ‚nego odÅ›wieÅ¼ania
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
     * Dodaje komponent nasÅ‚uchujÄ…cy na zmiany zaznaczonego wierzchoÅ‚ka
     *
     * @param component
     * @param manager
     */
    public void addAnnotationComponent(AnnotationComponent component) {
        this.addControlListener(new AnnotationListener(component, ontology));
    }

    /**
     * Dodaje komponent nasÅ‚uchujÄ…cy na zmiany wskazanego wierzchoÅ‚ka
     *
     * @param component
     */
    public void addIRIInfoComponent(IRIInfoComponent component) {
        this.addControlListener(new IRIInfoListener(component));
    }

    /**
     * metoda wizualizuje zadanÄ… ontologiÄ™
     *
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateGraphFromOWl(OWLOntology ont) {
        setOntology(ont);
        generateGraphFromOWl();
    }

    /**
     * metoda wizualizuje zadanÄ… ontologiÄ™
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
     *
     * @param ont ontologia zapisana w OWLAPI
     */
    public void generateTreeFromOWl(OWLOntology ont) {
        setOntology(ont);
        generateTreeFromOWl();
    }

    /**
     * Zmiana algorytmu wizualizacji
     *
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

            if (visualizationNodeLinkTree != null) {
                visualizationNodeLinkTree.reset();
            }
            visualizationNodeLinkTree = null;

            graph.clear();
            graph.clearSpanningTree();
            graph.removeAllGraphModelListeners();
            graph.removeAllSets();
            graph = new Graph();
        } catch (Exception e) {
        }
    }

    /**
     * WyÅ›wietla caÅ‚e drzewo wywnioskowanej hierarchii
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
