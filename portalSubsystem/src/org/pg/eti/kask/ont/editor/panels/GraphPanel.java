package org.pg.eti.kask.ont.editor.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pg.eti.kask.ont.editor.Logic;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.graph.GraphDisplay;
import org.pg.eti.kask.ont.editor.graph.action.RelationsFilter;
import org.pg.eti.kask.ont.editor.util.EditorUtil;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLCommentAnnotation;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLLabelAnnotation;
import org.semanticweb.owl.model.OWLOntology;

import prefuse.action.filter.GraphDistanceFilter;
import prefuse.data.Graph;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;

/**
 * 
 * @author Andrzej Jakowski
 */
public class GraphPanel extends JPanel implements ChangeListener, ActionListener{

	private static final long serialVersionUID = -6129490626051427230L;
	
	private ResourceBundle messages;
	
	private Logic logic;
	private OWLOntology ontology;

	private GraphDisplay display;
	private JSpinner graphHops;
	private JCheckBox showSubClasses;
	private JCheckBox showDisjointClasses;
	private JCheckBox showEquivalentClasses;
	private JTextArea nameTextArea;
	private JTextArea labelTextArea;
	private JTextArea commentTextArea;
	private JFastLabel uriLabel;
	private JEditorPane legendPane;
	private JPanel controlPanel;
	private JPanel propertiesPanel;
		
	/**
	 * 
	 * @param display
	 */
	public GraphPanel(Graph graph, OWLOntology ontology){
		this.messages = EditorUtil.getResourceBundle(GraphPanel.class);
		
		this.logic = Logic.getInstance();
		
		this.ontology = ontology;
		this.display = new GraphDisplay(this, graph);
		
		//stworzenie wszystkich komponentow
		initializeControlPanel();
		initializeLegendPanel();
		initializePropertiesPanel();
		initialize();
	}

	private void initialize() {		
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		
		//najpierw prawy panel
		rightPanel.add(controlPanel);
		rightPanel.add(legendPane);
		rightPanel.add(propertiesPanel);
		rightPanel.setPreferredSize(new Dimension(220, Integer.MAX_VALUE));
		rightPanel.setMaximumSize(new Dimension(220, Integer.MAX_VALUE));
		rightPanel.setMinimumSize(new Dimension(220, Integer.MAX_VALUE));
		rightPanel.setBackground(Constants.VIS_COLOR_BACKGROUND);
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(ColorLib.gray(200),true)));
		rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		
		//uri label
		//dodanie uriLabel - na dole na panelu
		uriLabel = new JFastLabel("");
		uriLabel.setPreferredSize(new Dimension(1000, 20));
		uriLabel.setMaximumSize(new Dimension(1000, 20));	
		uriLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Constants.VIS_COLOR_BORDER));
		uriLabel.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 12));
		uriLabel.setBackground(Constants.VIS_COLOR_BACKGROUND);
		uriLabel.setForeground(Constants.VIS_COLOR_FOREGROUND);
		
		//pozniej lewy panel
		leftPanel.add(display);
		leftPanel.add(uriLabel);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);		

		this.setBackground(Constants.VIS_COLOR_BACKGROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(leftPanel);
		this.add(rightPanel);
	}
	
	public void updateUriLabel(String text) {
		uriLabel.setText(text);
	}
	
	public void updatePropertiesPanel(String uri) {
		
		try {
			//najpierw deinicjalizacja
			nameTextArea.setText("");
			labelTextArea.setText("");
			commentTextArea.setText("");

			OWLClass currentClass = logic.getDataFactory().getOWLClass(new URI(uri));
			
			nameTextArea.setText(currentClass.getURI().getFragment());
			
			Set<OWLAxiom> refAxioms = ontology.getReferencingAxioms(currentClass);
			
			for(OWLAxiom axiom : refAxioms) {
				if(axiom instanceof OWLEntityAnnotationAxiom ) {
					if(((OWLEntityAnnotationAxiom)axiom).getAnnotation() instanceof OWLLabelAnnotation) {
						labelTextArea.setText(((OWLEntityAnnotationAxiom)axiom).getAnnotation().getAnnotationValueAsConstant().getLiteral());
					} else if(((OWLEntityAnnotationAxiom)axiom).getAnnotation() instanceof OWLCommentAnnotation) {
						commentTextArea.setText(((OWLEntityAnnotationAxiom)axiom).getAnnotation().getAnnotationValueAsConstant().getLiteral());
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int value = ((Integer) graphHops.getValue()).intValue();
	
		GraphDistanceFilter filter = display.getDistanceFilter();
		filter.setDistance(value);
		filter.run();
	
		display.getVisualization().run("draw");
	}
	
	public void focusNodeOnGraph(String nodeURI) {
		display.focusGraphNode(nodeURI);
		updatePropertiesPanel(nodeURI);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		int relationsFilter = 0;
		
		if(showSubClasses.isSelected())
			relationsFilter |= RelationsFilter.SHOW_SUBCLASSES;
		if(showDisjointClasses.isSelected())
			relationsFilter |= RelationsFilter.SHOW_DISJOINTCLASSES;			
		if(showEquivalentClasses.isSelected())
			relationsFilter |= RelationsFilter.SHOW_EQUIVALENTCLASSES;			
		
		display.getRelationsFilter().setRelationsFilter(relationsFilter);
		display.getRelationsFilter().run();
		display.getVisualization().run("draw");
	}
	
	private void initializeControlPanel() {
		controlPanel = new JPanel();
		
		GraphDistanceFilter filter = display.getDistanceFilter();

		//stworzenie labeli
		JLabel hopsLabel = new JLabel();
		hopsLabel.setText(messages.getString("hopsLabel.text"));
		
		JLabel subClassesLabel = new JLabel();
		subClassesLabel.setText(messages.getString("subClassesLabel.text"));
		
		JLabel disjointClassesLabel = new JLabel();
		disjointClassesLabel.setText(messages.getString("disjointClassesLabel.text"));
		
		JLabel equivalentClassesLabel = new JLabel();
		equivalentClassesLabel.setText(messages.getString("equivalentClassesLabel.text"));

		//swtorzenie spinnera i ustawienie jego dzialania
		SpinnerNumberModel model = new SpinnerNumberModel(filter.getDistance(), 0, null, 1);
		graphHops = new JSpinner();
		graphHops.addChangeListener(this);
		graphHops.setModel(model);
		
		//stworzenie checkboxow
		showSubClasses = new JCheckBox();
		showSubClasses.setBackground(Constants.VIS_COLOR_BACKGROUND);
		showSubClasses.addActionListener(this);	
		showSubClasses.setSelected(true);
		
		showDisjointClasses = new JCheckBox();		
		showDisjointClasses.setBackground(Constants.VIS_COLOR_BACKGROUND);
		showDisjointClasses.addActionListener(this);
		
		showEquivalentClasses= new JCheckBox();
		showEquivalentClasses.setBackground(Constants.VIS_COLOR_BACKGROUND);
		showEquivalentClasses.addActionListener(this);
		
		GroupLayout layout = new GroupLayout(controlPanel);
		controlPanel.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		
		//najpierw zdefiniowanie poziomej grupy 
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(hopsLabel)
						.addComponent(subClassesLabel)
						.addComponent(disjointClassesLabel)
						.addComponent(equivalentClassesLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(graphHops)
						.addComponent(showSubClasses)
						.addComponent(showDisjointClasses)
						.addComponent(showEquivalentClasses)));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(hopsLabel)
						.addComponent(graphHops))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(subClassesLabel)
						.addComponent(showSubClasses))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(disjointClassesLabel)
						.addComponent(showDisjointClasses))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(equivalentClassesLabel)
						.addComponent(showEquivalentClasses)));
		
		//inicjalizacja glownego panelu
		controlPanel.setMinimumSize(new Dimension(215, 110));
		controlPanel.setPreferredSize(new Dimension(215, 110));
		controlPanel.setMaximumSize(new Dimension(215, 110));
		controlPanel.setBackground(Constants.VIS_COLOR_BACKGROUND);
		controlPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("controlPanel.border.text")));
		
	}
	
	private void initializeLegendPanel() {
		String content = "<html><body>" +
		"<table style=\"font-size: 8px; font-family: Tahoma \">" +
		"<tr><td bgcolor=\"" + Integer.toHexString(Constants.NODE_COLOR_CLASS & 0x00ffffff) + "\" width=\"20px\"></td><td> - "+messages.getString("legendPanel.owlClassItem.text")+"</td></tr>" +
		"<tr><td bgcolor=\"" + Integer.toHexString(Constants.NODE_COLOR_SELECTED & 0x00ffffff) + "\"></td><td> - "+messages.getString("legendPanel.selectedNodeItem.text")+"</td></tr>" +
		"<tr><td bgcolor=\"" + Integer.toHexString(Constants.NODE_COLOR_HIGHLIGHTED & 0x00ffffff) + "\"></td><td> - "+messages.getString("legendPanel.neighbourNodeItem.text")+"</td></tr>" +
		"</table>"+
		"</body></html>";

		legendPane = new JEditorPane("text/html", content);
		legendPane.setEditable(false);
		legendPane.setBorder(BorderFactory.createTitledBorder(messages.getString("legend.border.text")));		
		legendPane.setMinimumSize(new Dimension(215, 90));
		legendPane.setPreferredSize(new Dimension(215, 90));
		legendPane.setMaximumSize(new Dimension(215, 90));
		
	}

	private void initializePropertiesPanel() { 
		propertiesPanel = new JPanel();
		
		//najpierw okreslenie zaznaczonego wezla
		JLabel nameText = new JLabel();
		nameText.setText(messages.getString("nameText.text"));
		nameText.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//name JTextArea
		nameTextArea = new JTextArea();
		nameTextArea.setEditable(false);
		nameTextArea.setText("");
		nameTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameTextArea.setPreferredSize(new Dimension(205, 20));
		nameTextArea.setMaximumSize(new Dimension(205, 20));		
		nameTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		nameTextArea.setFont(FontLib.getFont("Tahoma", 10));
		
		//label JLabel
		JLabel labelText = new JLabel();
		labelText.setText(messages.getString("labelText.text"));
		labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//label JTextArea
		labelTextArea = new JTextArea();
		labelTextArea.setEditable(false);		
		labelTextArea.setText("");
		labelTextArea.setLineWrap(true);
		labelTextArea.setWrapStyleWord(true);
		labelTextArea.setPreferredSize(new Dimension(205, 20));
		labelTextArea.setMaximumSize(new Dimension(205, 20));		
		labelTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));	
		labelTextArea.setFont(FontLib.getFont("Tahoma", 10));
		
		//comment JLabel
		JLabel commentText = new JLabel();
		commentText.setText(messages.getString("commentText.text"));
		commentText.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//comment JTextArea
		commentTextArea = new JTextArea();		
		commentTextArea.setEditable(false);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setText("");
		commentTextArea.setFont(FontLib.getFont("Tahoma", 10));
		JScrollPane pane = new JScrollPane(commentTextArea);
		pane.setAlignmentX(Component.LEFT_ALIGNMENT);
		pane.setPreferredSize(new Dimension(205, EditorUtil.getScreenSize().height-460));
		pane.setMaximumSize(new Dimension(205, EditorUtil.getScreenSize().height-460));
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		propertiesPanel.add(nameText);
		propertiesPanel.add(nameTextArea);
		propertiesPanel.add(labelText);
		propertiesPanel.add(labelTextArea);
		propertiesPanel.add(commentText);
		propertiesPanel.add(pane);
		
		//ustawienie glownego panelu
		propertiesPanel.setPreferredSize(new Dimension(215, EditorUtil.getScreenSize().height-350));
		propertiesPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("propertiesPanel.border.text")));
		propertiesPanel.setBackground(Constants.VIS_COLOR_BACKGROUND);
		propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
		
	}
	
	/**
	 * 
	 * @return
	 */
	public GraphDisplay getDisplay() {
		return display;
	}

	/**
	 * 
	 * @param display
	 */
	public void setDisplay(GraphDisplay display) {
		this.display = display;
	}
}
