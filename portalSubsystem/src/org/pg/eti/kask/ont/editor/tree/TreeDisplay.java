package org.pg.eti.kask.ont.editor.tree;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;

import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.panels.TreePanel;
import org.pg.eti.kask.ont.editor.tree.action.AutoPanAction;
import org.pg.eti.kask.ont.editor.tree.action.NodeColorAction;
import org.pg.eti.kask.ont.editor.tree.controls.ClassSelectionControl;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

import prefuse.Display;
import prefuse.Visualization;
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
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Tree;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Klasa przeprowadzajaca wizualizacje ontologii w postaci drzewa.
 *
 * @author Andrzej Jakowski
 */
public class TreeDisplay extends Display {

	private static final long serialVersionUID = -138596195380995596L;

	/** Akcja filtrujaca */
	private static final String FILTER_ACTION = "filter";
	/** Akcja wykonujaca szybkie odmalowanie */
	private static final String REPAINT_ACTION = "repaint";
	/** Akcja wykonujaca pelne odmalowanie */
	private static final String FULL_PAINT_ACTION = "fullPaint";
	/** Akcja wykonujaca animacje */
	private static final String ANIMATE_PAINT_ACTION = "animatePaint";
	/** Akcja wykonujaca animacje */
	private static final String ANIMATE_ACTION = "animate";
	/** Akcja przeprowadzajaca rysowanie elementow tekstowych */
	private static final String TEXTCOLOR_ACTION = "textColor";
	
	/** Akcja przeprowadzajaca rozlokowanie elementow w postaci drzewa */
	private static final String TREE_LAYOUT = "treeLayout";
	/** Akcja przeprowadzajaca rozwiniecie odpowiednich elementow poddrzewa */
	private static final String SUBTREE_LAYOUT = "subLayout";
	
	private TreePanel parentPanel;
	private Visualization visualization;
	private Tree tree;
	private LabelRenderer nodeRenderer;
	private EdgeRenderer edgeRenderer;
	private int orientation = prefuse.Constants.ORIENT_LEFT_RIGHT;

	/**
	 * Konstruktor tworzacy nowy display dla ontologii i
	 * inicjalizujacy odpowiednie komponenty.
	 * 
	 * @param tree drzewo do wyswietlenia
	 */
	public TreeDisplay(TreePanel parentPanel,Tree tree) {
		this.parentPanel = parentPanel;
		this.tree = tree;
		this.visualization = new Visualization();
		this.setVisualization(visualization);
		
		initVisualization();
		initDisplay();

		setOrientation(orientation);
		visualization.run(FILTER_ACTION);
	}

	/**
	 * Metoda inicjalizuje obiekt <code>Visualization</code>, odpowiedzialny
	 * za wizualizacje drzewa. Ustawia odpowiednie akcje, ktore sa wykonywane
	 * w momencie interakcji z uzytkownikiem.
	 */
	private void initVisualization() {
		// zarejestrowanie drzewa w obiekcie visualization
		visualization.add(Constants.TREE, tree);

		// renderer dla wezlow
		nodeRenderer = new LabelRenderer(Constants.GRAPH_NODE_NAME_COLUMN_NAME);
		nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		nodeRenderer.setHorizontalAlignment(prefuse.Constants.LEFT);
		nodeRenderer.setRoundedCorner(8, 8);
		
		// renderer dla krawedzi
		edgeRenderer = new EdgeRenderer(prefuse.Constants.EDGE_TYPE_CURVE);

		// zdefiniowanie zbiorczego renderera dla calego grafu
		DefaultRendererFactory rendererFactory = new DefaultRendererFactory(nodeRenderer);
		rendererFactory.add(new InGroupPredicate(Constants.TREE_EDGES), edgeRenderer);
		visualization.setRendererFactory(rendererFactory);
		
		// krawedzie nie sa interaktywne
		visualization.setValue(Constants.TREE_EDGES, null, VisualItem.INTERACTIVE, Boolean.FALSE);

		// zdefiniowanie akcji kolorujaych wezly i tekst
		ItemAction nodeColor = new NodeColorAction(Constants.TREE_NODES, visualization);
		ItemAction textColor = new ColorAction(Constants.TREE_NODES, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0));
		visualization.putAction(TEXTCOLOR_ACTION, textColor);

		
		ItemAction edgeColor = new ColorAction(Constants.TREE_EDGES, VisualItem.STROKECOLOR, ColorLib.rgb(200, 200, 200));

		// szybki repaint
		ActionList repaint = new ActionList();
		repaint.add(nodeColor);
		repaint.add(new RepaintAction());
		visualization.putAction(REPAINT_ACTION, repaint);

		// pelny repaint
		ActionList fullPaint = new ActionList();
		fullPaint.add(nodeColor);
		visualization.putAction(FULL_PAINT_ACTION, fullPaint);

		// akcja wykonujaca plynne przejscie miedzy kolorami dla wezlow
		ActionList animatePaint = new ActionList(400);
		animatePaint.add(new ColorAnimator(Constants.TREE_NODES));
		animatePaint.add(new RepaintAction());
		visualization.putAction(ANIMATE_PAINT_ACTION, animatePaint);

		// layout, ukladajacy wszsytko w drzewo
		NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(Constants.TREE, orientation, 50, 2, 8);
		treeLayout.setLayoutAnchor(new Point2D.Double(25, 300));
		visualization.putAction(TREE_LAYOUT, treeLayout);

		// layout ktory uklada wezly dla noworozwinietego poddrzewa
		CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(Constants.TREE, orientation);
		visualization.putAction(SUBTREE_LAYOUT, subLayout);

		// akcja ktora przesuwa drzewo automatycznie po zaznaczeniu konkretnego wezla
		AutoPanAction autoPan = new AutoPanAction(this);

		// akcja filtrujaca - nie wyswietla calosci drzewa
		ActionList filter = new ActionList();
		filter.add(new FisheyeTreeFilter(Constants.TREE, 2));
		filter.add(new FontAction(Constants.TREE_NODES, FontLib.getFont("Tahoma", 12)));
		filter.add(treeLayout);
		filter.add(subLayout);
		filter.add(textColor);
		filter.add(nodeColor);
		filter.add(edgeColor);
		visualization.putAction(FILTER_ACTION, filter);

		// akcje realizujaca animacje dla wszystkich wezlow 
		ActionList animate = new ActionList(1000);
		animate.setPacingFunction(new SlowInSlowOutPacer());
		animate.add(autoPan);
		animate.add(new QualityControlAnimator());
		animate.add(new VisibilityAnimator(Constants.TREE));
		animate.add(new LocationAnimator(Constants.TREE_NODES));
		animate.add(new ColorAnimator(Constants.TREE_NODES));
		animate.add(new RepaintAction());
		visualization.putAction(ANIMATE_ACTION, animate);
		visualization.alwaysRunAfter(FILTER_ACTION, ANIMATE_ACTION);
	}

	/**
	 * Metoda inicjalizuje ten obiekt, ustawiajac odpowiednie kontrolki,
	 * a takze definiujac wlasciwosci wyswietlania.
	 */
	private void initDisplay() {
		this.setItemSorter(new TreeDepthItemSorter());
		
		this.addControlListener(new ZoomToFitControl());
		this.addControlListener(new WheelZoomControl());
		this.addControlListener(new PanControl());
		this.addControlListener(new ClassSelectionControl(parentPanel));
		this.addControlListener(new FocusControl(1, FILTER_ACTION));
		
		//ustawienie wlasciwosci display'a
		int maxWidth = (int)EditorUtil.getScreenSize().getWidth()-210;
		int maxHeight = (int)EditorUtil.getScreenSize().getHeight()-150;
		int minWidth = MainFrame.EDITOR_WIDTH -410;
		int minHeight = MainFrame.EDITOR_HEIGHT - 150;
		this.setMaximumSize(new Dimension(maxWidth, maxHeight));
		this.setPreferredSize(new Dimension(maxWidth, maxHeight));
		this.setMinimumSize(new Dimension(minWidth, minHeight));
		
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Constants.VIS_COLOR_BORDER));
		this.setForeground(Constants.VIS_COLOR_FOREGROUND);
		this.setBackground(Constants.VIS_COLOR_BACKGROUND);
	}

	/**
	 * Ustawia ulozenie drzewa, ktore moze byc polozene na ekranie w dowolnym kierunku
	 * - z dolu, na gore, z lewej do prawej, etc.
	 * 
	 * @param orientation stala z klasy <code>prefuse.Constants</code>
	 */
	public void setOrientation(int orientation) {
		NodeLinkTreeLayout nodeTreeLayout = (NodeLinkTreeLayout) visualization.getAction(TREE_LAYOUT);
		CollapsedSubtreeLayout collapsedSubtreeLayout = (CollapsedSubtreeLayout) visualization.getAction(SUBTREE_LAYOUT);
		switch (orientation) {
			case prefuse.Constants.ORIENT_LEFT_RIGHT:
				nodeRenderer.setHorizontalAlignment(prefuse.Constants.LEFT);
				edgeRenderer.setHorizontalAlignment1(prefuse.Constants.RIGHT);
				edgeRenderer.setHorizontalAlignment2(prefuse.Constants.LEFT);
				edgeRenderer.setVerticalAlignment1(prefuse.Constants.CENTER);
				edgeRenderer.setVerticalAlignment2(prefuse.Constants.CENTER);
				break;
			case prefuse.Constants.ORIENT_RIGHT_LEFT:
				nodeRenderer.setHorizontalAlignment(prefuse.Constants.RIGHT);
				edgeRenderer.setHorizontalAlignment1(prefuse.Constants.LEFT);
				edgeRenderer.setHorizontalAlignment2(prefuse.Constants.RIGHT);
				edgeRenderer.setVerticalAlignment1(prefuse.Constants.CENTER);
				edgeRenderer.setVerticalAlignment2(prefuse.Constants.CENTER);
				break;
			case prefuse.Constants.ORIENT_TOP_BOTTOM:
				nodeRenderer.setHorizontalAlignment(prefuse.Constants.CENTER);
				edgeRenderer.setHorizontalAlignment1(prefuse.Constants.CENTER);
				edgeRenderer.setHorizontalAlignment2(prefuse.Constants.CENTER);
				edgeRenderer.setVerticalAlignment1(prefuse.Constants.BOTTOM);
				edgeRenderer.setVerticalAlignment2(prefuse.Constants.TOP);
				break;
			case prefuse.Constants.ORIENT_BOTTOM_TOP:
				nodeRenderer.setHorizontalAlignment(prefuse.Constants.CENTER);
				edgeRenderer.setHorizontalAlignment1(prefuse.Constants.CENTER);
				edgeRenderer.setHorizontalAlignment2(prefuse.Constants.CENTER);
				edgeRenderer.setVerticalAlignment1(prefuse.Constants.TOP);
				edgeRenderer.setVerticalAlignment2(prefuse.Constants.BOTTOM);
				break;
			default:
				nodeRenderer.setHorizontalAlignment(prefuse.Constants.LEFT);
				edgeRenderer.setHorizontalAlignment1(prefuse.Constants.RIGHT);
				edgeRenderer.setHorizontalAlignment2(prefuse.Constants.LEFT);
				edgeRenderer.setVerticalAlignment1(prefuse.Constants.CENTER);
				edgeRenderer.setVerticalAlignment2(prefuse.Constants.CENTER);
		}
		this.orientation = orientation;
		nodeTreeLayout.setOrientation(orientation);
		collapsedSubtreeLayout.setOrientation(orientation);
	}

	/**
	 * Pobiera aktualna orientacje drzewa na ekranie.
	 * 
	 * @return wartosc okreslajaca orinetacje drzewa, jest to stala z klasy <code>prefuse.Constants</code>
	 */
	public int getOrientation() {
		return orientation;
	}

}
