package org.pg.eti.kask.ont.editor.graph;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.BorderFactory;

import org.pg.eti.kask.ont.editor.MainFrame;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.graph.action.EdgeDecoratorLayout;
import org.pg.eti.kask.ont.editor.graph.action.NodeColorAction;
import org.pg.eti.kask.ont.editor.graph.action.RelationsFilter;
import org.pg.eti.kask.ont.editor.graph.controls.ClassSelectionControl;
import org.pg.eti.kask.ont.editor.panels.GraphPanel;
import org.pg.eti.kask.ont.editor.util.EditorUtil;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/**
 * Klasa przeprowadzajaca wizualizacje ontologii w postaci grafu.
 *
 * @author Andrzej Jakowski
 */
public class GraphDisplay extends Display {

	private static final long serialVersionUID = -1293946400252455442L;
	
	/** Akcja przeprowadzajaca rysowanie drzewa */
	private static final String DRAW_ACTION = "draw";
	/** Akcja wykonujaca rozlokowanie elementow */
	private static final String LAYOUT_ACTION = "layout";
	
	private GraphPanel parentPanel;
	private Visualization visualization;
	private Graph graph;
	private GraphDistanceFilter hopsFilter;
	private RelationsFilter relationsFilter;
	private ForceDirectedLayout forceDirectedLayout;

	/**
	 * Konstruktor tworzacy nowy display dla ontologii i
	 * inicjalizujacy odpowiednie komponenty.
	 * 
	 * @param parentPanel panel nadrzedny
	 * @param graph graf do wyswietlenia
	 */
	public GraphDisplay(GraphPanel parentPanel, Graph graph)  {
		//inicjalizacja obiektow
		this.parentPanel = parentPanel;
		this.graph = graph;
		this.visualization = new Visualization();

		//inicjalizacja filtrow dla grafu
		this.hopsFilter = new GraphDistanceFilter(Constants.GRAPH, 1);
		this.relationsFilter = new RelationsFilter(visualization);

		//zaincicjalizowanie obiektu visualization
		initializeVisualization();
		initializeDisplay();		

		//ustawienie wizualizacji dla displaya
		this.setVisualization(visualization);
		this.visualization.run(DRAW_ACTION);
	}

	/**
	 * Metoda inicjalizuje obiekt <code>Visualization</code>, odpowiedzialny
	 * za wizualizacje w postaci grafu. Ustawia odpowiednie akcje, ktore sa wykonywane
	 * w momencie interakcji z uzytkownikiem.
	 */
	private void initializeVisualization() {
		// zarejestrowanie grafu w obiekvcie visualization
		VisualGraph visualGraph = visualization.addGraph(Constants.GRAPH, graph);

		// renderer dla wezlow
		LabelRenderer nodeRenderer = new LabelRenderer(Constants.GRAPH_NODE_NAME_COLUMN_NAME);
		nodeRenderer.setRoundedCorner(8, 8);

		// renderer dla krawedzi
		EdgeRenderer edgeRender = new EdgeRenderer(prefuse.Constants.EDGE_TYPE_LINE, prefuse.Constants.EDGE_ARROW_FORWARD);

		// renderer dla dekoratora dla krawedzi
		LabelRenderer edgeDecoratorRenderer = new LabelRenderer(Constants.GRAPH_EDGE_LABEL_COLUMN);

		// zdefiniowanie zbiorczego renderera dla calego grafu
		DefaultRendererFactory rendererFactory = new DefaultRendererFactory();
		rendererFactory.add(new InGroupPredicate(Constants.GRAPH_NODES), nodeRenderer);
		rendererFactory.add(new InGroupPredicate(Constants.EDGE_DECORATORS), edgeDecoratorRenderer);
		rendererFactory.add(new InGroupPredicate(Constants.GRAPH_EDGES), edgeRender);

		// zarejestrowanie renderera glownego w obiekcie visualization
		visualization.setRendererFactory(rendererFactory);

		// zarejestrowanie decoratora dla krawedzi
		Schema edgeDecoratorSchema = PrefuseLib.getVisualItemSchema();
		edgeDecoratorSchema.setDefault(VisualItem.INTERACTIVE, Boolean.FALSE);
		edgeDecoratorSchema.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(0));
		edgeDecoratorSchema.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 8));
		visualization.addDecorators(Constants.EDGE_DECORATORS, Constants.GRAPH_EDGES, edgeDecoratorSchema);

		// krawedzie nie sa interaktywne - nie dziala na nie zadna akcja
		// uzytkownika
		visualization.setValue(Constants.GRAPH_EDGES, null, VisualItem.INTERACTIVE, Boolean.FALSE);

		// ustawienie podswietlonej klasy 
		if(visualGraph.getNodeCount() > 0) {
			VisualItem currentClass = (VisualItem) visualGraph.getNode(0);
			visualization.getGroup(Visualization.FOCUS_ITEMS).setTuple(currentClass);
			currentClass.setFixed(true);
		}

		// naprawianie wizualizacji elementow zaznaczonych
		TupleSet focusGroup = visualization.getGroup(Visualization.FOCUS_ITEMS);
		focusGroup.addTupleSetListener(new TupleSetListener() {

			@Override
			public void tupleSetChanged(TupleSet tset, Tuple[] added, Tuple[] removed) {

				for (int i = 0; i < removed.length; ++i)
					((VisualItem) removed[i]).setFixed(false);

				for (int i = 0; i < added.length; ++i) {
					((VisualItem) added[i]).setFixed(false);
					((VisualItem) added[i]).setFixed(true);
				}

				if (tset.getTupleCount() == 0) {
					tset.addTuple(removed[0]);
					((VisualItem) removed[0]).setFixed(false);
				}

				visualization.run(DRAW_ACTION);
			}

		});

		
		// utworzenie listy akcji dla visualization
		ActionList drawActions = new ActionList();
		// filtr w zaleznosci od odleglosci + filtr relacji
		drawActions.add(hopsFilter);
		drawActions.add(relationsFilter);
		// wezly - kolor czarny
		drawActions.add(new ColorAction(Constants.GRAPH_NODES, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0)));
		drawActions.add(new ColorAction(Constants.GRAPH_NODES, VisualItem.STROKECOLOR, ColorLib.gray(0)));
		// krawedzie - kolor szary
		drawActions.add(new ColorAction(Constants.GRAPH_EDGES, VisualItem.STROKECOLOR, ColorLib.gray(200)));
		drawActions.add(new ColorAction(Constants.GRAPH_EDGES, VisualItem.FILLCOLOR, ColorLib.gray(200)));
		
		ActionList layoutActions = new ActionList(Activity.INFINITY);
		//manager layout'u - symulacja sil wystepujacych miedzy wezlami
		forceDirectedLayout = new ForceDirectedLayout(Constants.GRAPH);
		//grawitacja na minimum
		forceDirectedLayout.getForceSimulator().getForces()[0].setParameter(0, -10f);
		layoutActions.add(forceDirectedLayout);
		layoutActions.add(new RepaintAction());
		//akcja kolorujaca wezly w zaleznosci od aktywnosci mysza
		layoutActions.add(new NodeColorAction(Constants.GRAPH_NODES, visualization));
		//dodanie layout dla decoratora krawedzi
		layoutActions.add(new EdgeDecoratorLayout(visualization));
		

		// zarejestrowanie wszystkich akcji
		visualization.putAction(DRAW_ACTION, drawActions);
		visualization.putAction(LAYOUT_ACTION, layoutActions);
		visualization.runAfter(DRAW_ACTION, LAYOUT_ACTION);
	}

	/**
	 * Metoda inicjalizuje ten obiekt, ustawiajac odpowiednie kontrolki,
	 * a takze definiujac wlasciwosci wyswietlania.
	 */
	private void initializeDisplay() {		
		// dodanie zetawu kontrolek do zoomowania/itp
		this.addControlListener(new DragControl());
		this.addControlListener(new PanControl());
		this.addControlListener(new ZoomControl());
		this.addControlListener(new WheelZoomControl());
		this.addControlListener(new ZoomToFitControl());
		this.addControlListener(new NeighborHighlightControl());
		this.addControlListener(new FocusControl(1));
		this.addControlListener(new ClassSelectionControl(parentPanel));
		
		//ustawienie wlasciwosci display'a
		int maxWidth = (int)EditorUtil.getScreenSize().getWidth()-210;
		int maxHeight = (int)EditorUtil.getScreenSize().getHeight()-150;
		int minWidth = MainFrame.EDITOR_WIDTH -410;
		int minHeight = MainFrame.EDITOR_HEIGHT - 150;
		this.setMaximumSize(new Dimension(maxWidth, maxHeight));
		this.setPreferredSize(new Dimension(maxWidth, maxHeight));
		this.setMinimumSize(new Dimension(minWidth, minHeight));
		
		this.pan(200, 200);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Constants.VIS_COLOR_BORDER));
		this.setForeground(Constants.VIS_COLOR_FOREGROUND);
		this.setBackground(Constants.VIS_COLOR_BACKGROUND);
	}
	

	/**
	 * Metoda powodujaca przeniesienie focus'u na odpowiedni element ontologii.
	 * 
	 * @param nodeURI URI wezla do przeniesienia focus'u
	 */
	public void focusGraphNode(String nodeURI) {
		Table table = graph.getNodeTable();
		Iterator<?> tuples = table.tuples();
		Tuple result = null;
		while(tuples.hasNext()) {
			Tuple t = (Tuple)tuples.next();
			String currentURI = t.getString(Constants.GRAPH_NODE_URI_COLUMN_NAME);
			if(currentURI.equals(nodeURI)) {
				result = t;
				break;
			}			
		}
		if(result != null) {
			VisualItem vi = (VisualItem)visualization.getVisualItem(Constants.GRAPH_NODES, result);
			visualization.getGroup(Visualization.FOCUS_ITEMS).setTuple(vi);
		}
	}

	/**
	 * Zwraca filtr odpowiedzialny za wyswietlanie grafu do okreslonej glebokosci.
	 * 
	 * @return filtr wywsietlajacy elementyu tylko do okreslonej glebokosci
	 */
	public GraphDistanceFilter getDistanceFilter() {
		return hopsFilter;
	}

	/**
	 * Zwraca filtr odpowiedzialny za wyswietlanie tylko pewnych relacji (subclass, disjoint, itp).
	 * 
	 * @return filtr wyswietlajacy jedynie czesc relacji w ontologii
	 */
	public RelationsFilter getRelationsFilter() {
		return relationsFilter;
	}

	/**
	 * Metoda zwracajaca wyswietlany graf.
	 * 
	 * @return wyswietlany graf
	 */
	public Graph getGraph() {
		return graph;
	}

}