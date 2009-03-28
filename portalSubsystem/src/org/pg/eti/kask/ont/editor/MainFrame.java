package org.pg.eti.kask.ont.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.pg.eti.kask.ont.editor.dialogs.ProgressDialog;
import org.pg.eti.kask.ont.editor.graph.model.ClassesGraphModel;
import org.pg.eti.kask.ont.editor.menu.MainMenuBar;
import org.pg.eti.kask.ont.editor.panels.ComponentRepositoryPanel;
import org.pg.eti.kask.ont.editor.panels.MainPanel;
import org.pg.eti.kask.ont.editor.panels.ToolbarPanel;
import org.pg.eti.kask.ont.editor.tree.model.ClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyClassesTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyIndividualsTreeModel;
import org.pg.eti.kask.ont.editor.tree.model.OntologyPropertiesTreeModel;
import org.pg.eti.kask.ont.editor.util.ApplicationConfiguration;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.semanticweb.owl.model.OWLOntology;

/**
 * 
 * @author Andrzej Jakowski
 */
public class MainFrame extends JFrame implements WindowListener {

	private static final long serialVersionUID = 9164756211764025631L;
	
	private ResourceBundle messages;
	
	//pierwotne wymiary edytora
	public static final int EDITOR_WIDTH = (int)EditorUtil.getScreenSize().getWidth()-300;
	public static final int EDITOR_HEIGHT = (int)EditorUtil.getScreenSize().getHeight()-200;
	
	private Logic logic;
	//poszczegolne komponenty ramki
	private ComponentRepositoryPanel componentsPanel;
	private MainPanel mainPanel;	
	private ToolbarPanel toolbarPanel;
	private JPanel frameBodyPanel; 
	private JPanel bodyPanel;
	private Dimension editorSize;
	private MainMenuBar menu;
	
	/**
	 * Konstruktor domyslny.
	 */
	public MainFrame(ApplicationConfiguration config) {
		super();
		//stworzenie wszystkich komponentow
		this.logic = Logic.getInstance();
		this.logic.setConfig(config);
		this.bodyPanel = new JPanel();
		this.frameBodyPanel = new JPanel();
		this.componentsPanel = new ComponentRepositoryPanel(this);
		this.mainPanel = new MainPanel();
		this.toolbarPanel = new ToolbarPanel(this);
		this.editorSize = new Dimension(EDITOR_WIDTH, EDITOR_HEIGHT);
		this.menu = new MainMenuBar(this);
		this.messages = EditorUtil.getResourceBundle(MainFrame.class);
	}
	
	/**
	 * Konstruktor parametryzowany ustawiajacy tytul ramki.
	 * 
	 * @param title
	 */
	public MainFrame(String title, ApplicationConfiguration config) {
		super(title);
		//stworzenie wszystkich komponentow
		
		this.logic = Logic.getInstance();
		this.logic.setConfig(config);
		this.bodyPanel = new JPanel();
		this.frameBodyPanel = new JPanel();
		this.componentsPanel = new ComponentRepositoryPanel(this);
		this.mainPanel = new MainPanel();
		this.toolbarPanel = new ToolbarPanel(this);
		this.editorSize = new Dimension(EDITOR_WIDTH, EDITOR_HEIGHT);
		this.menu = new MainMenuBar(this);
		this.messages = EditorUtil.getResourceBundle(MainFrame.class);
	}

	/**
	 * Metoda inicjalizujaca kolejne komponenty w obrebie ramki.
	 */
	public void initialize() {		
		
		//toolbar panel
		toolbarPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 36));
		toolbarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		toolbarPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 36));
		toolbarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
				
		//body panel
		JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentsPanel, mainPanel);
		splitPaneLeft.setOneTouchExpandable(true);
		splitPaneLeft.setDividerLocation(200);			
		splitPaneLeft.setDividerSize(6);
				
		bodyPanel.add(splitPaneLeft);
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.X_AXIS));
		bodyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//main panel
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		//frame body panel
		frameBodyPanel.add(toolbarPanel);
		frameBodyPanel.add(bodyPanel);
		frameBodyPanel.setLayout(new BoxLayout(frameBodyPanel, BoxLayout.Y_AXIS));
		
		this.setJMenuBar(menu);
		this.getContentPane().add(frameBodyPanel);
		//this.setExtendedState(MAXIMIZED_BOTH);
		this.setSize(editorSize);
		this.setLocation(EditorUtil.getStartingPosition(this.getSize()));		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.addWindowListener(this);
		
	}
	
	/**
	 * 
	 * @param absoluthePath
	 */
	public void loadOntologyFromFile(String absoluthePath){
		
		//odpalenie nowego watku w ktorym wczytana bedzie ontologia
		SwingWorker<OWLOntology, Integer> task = new SwingWorker<OWLOntology, Integer>(){

			@Override
			protected OWLOntology doInBackground() {
				OWLOntology ont = null;
				setProgress(50);
				try {
					//ont = Logic.loadOntologyFromFile("file:///c:/pizza.owl");
				} catch (Exception e) {
					e.printStackTrace();
				}
				setProgress(100);
				return ont;
			}
		
		};			
		
		final ProgressDialog dialog = new ProgressDialog("Wczytywanie ontologii", 0, 100);
		
		task.addPropertyChangeListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					dialog.setProgress(((Integer)evt.getNewValue()).intValue());
	            }
			}
			
		});
		
		task.execute();
		dialog.open();
		
	
		
		//loadOntology(logic.getOntology());
	}
	
	public void loadOntology(OWLOntology ontology) {	
		ClassesGraphModel graph = new ClassesGraphModel(ontology);
		mainPanel.displayGraph(graph, ontology);
		
		if(EditorUtil.isInferredHierarchyPanelVisisble()) {
			ClassesTreeModel tree = new ClassesTreeModel(ontology);
			mainPanel.displayTree(tree);
		}
		componentsPanel.displayOntologyClasses(new OntologyClassesTreeModel(ontology));
		componentsPanel.displayOntologyIndividuals(new OntologyIndividualsTreeModel(ontology));
		componentsPanel.displayOntologyProperties(new OntologyPropertiesTreeModel(ontology, OntologyPropertiesTreeModel.ALL_PROPERTY_TREE));
	}
	
	/**
	 * 
	 * @return
	 */
	public ComponentRepositoryPanel getComponentsPanel() {
		return componentsPanel;
	}
	
	public void closingApplication(Component component) {
		ProjectDescriptor descriptor = logic.getDescriptor();
		String ontologyURI = logic.getLoadedOntologyURI();
		logic.logoutUser();
		if(descriptor == null && (ontologyURI == null || ontologyURI.equals(""))) {
			System.exit(0);
		} else if(!logic.isChanged()) {
			System.exit(0);
		} else {
			if(descriptor != null ) {
				String message = messages.getString("saveProjectBody1.text")
					+ " " + descriptor.getProjectName() + " " + messages.getString("saveProjectBody2.text");
				int result = JOptionPane.showConfirmDialog(component, message, messages.getString("saveProjectTitle.text"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					getToolbarPanel().saveOntology();
					System.exit(0);
				} else if(result == JOptionPane.NO_OPTION) {
					System.exit(0);
				} else if(result == JOptionPane.CANCEL_OPTION) {
					
				}
			} else if (ontologyURI != null && !ontologyURI.equals("")) {
				String message = messages.getString("saveOntologyBody1.text")
				+ " " + ontologyURI + " " + messages.getString("saveOntologyBody2.text");
				int result = JOptionPane.showConfirmDialog(component, message, messages.getString("saveOntologyTitle.text"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					getToolbarPanel().saveOntology();
					System.exit(0);
				} else if(result == JOptionPane.NO_OPTION) {
					System.exit(0);
				} else if(result == JOptionPane.CANCEL_OPTION) {
					
				}
			}
			
		}
	}

	/**
	 * 
	 * @return
	 */
	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public ToolbarPanel getToolbarPanel() {
		return toolbarPanel;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		closingApplication(this);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	public MainMenuBar getMenu() {
		return menu;
	}

}
