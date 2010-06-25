package org.pg.eti.kask.sova.demo1;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pg.eti.kask.sova.utils.VisualizationProperties;
import org.pg.eti.kask.sova.visualization.FilterOptions;
import org.pg.eti.kask.sova.visualization.ForceDirectedVis;
import org.pg.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import prefuse.util.ui.JValueSlider;


/**
 *
 * @author infinity
 */
public class Demo {
	private static final String CHECKBOX_SUBCLASS_COMMAND = "checkbox_subclass";
	private static final String CHECKBOX_CLASS_COMMAND = "checkbox_class";
	private static final String CHECKBOX_DISJOINT_CLASS_COMMAND = "checkbox_disjoint_class";
	private static final String CHECKBOX_EQUIVALENT_CLASS_COMMAND = "checkbox_equvalent_class";
	private static final String CHECKBOX_CARDINALITY_COMMAND = "checkbox_cardinality_class";
	private static final String CHECKBOX_UNIONOF_COMMAND = "checkbox_unionof_class";
	private static final String CHECKBOX_COMPLEMENT_COMMAND = "checkbox_complement_class";
	private static final String CHECKBOX_INTERSECTION_COMMAND = "checkbox_intersection_class";
	
	private static final String CHECKBOX_INDYVIDUAL_COMMAND = "checkbox_indywidual_node";
	private static final String CHECKBOX_INSTANCEOF_COMMAND = "checkbox_instanceof";
	private static final String CHECKBOX_DIFFERENT_COMMAND = "checkbox_different";
	private static final String CHECKBOX_SAMEAS_COMMAND = "chechbox_sameas";
	private static final String CHECKBOX_ONEOF_COMMAND = "checkbox_oneof";
	private static final String CHECKBOX_PROPERTY_COMMAND = "checkbox_property";
	private static final String CHECKBOX_SUBPROPERTY_COMMAND = "checkbox_subproperty_edge";
	private static final String CHECKBOX_EQUIVALENT_COMMAND = "checkbox_equivalent_property";
	private static final String CHECKBOX_INVERSEOFPROPERTY_COMMAND = "checkbox_inverseof_property";
	private static final String CHECKBOX_FUNCTIONALPROPERTY_COMMAND = "checkbox_functionalproperty";
	private static final String CHECKBOX_INVERSFUNCTIONALPROPERTY_COMMAND = "checkbox_inversfunctionalproperty";
	private static final String CHECKBOX_SYMMETRICPROPERTY_COMMAND = "checkbox_symetricproperty";
	private static final String CHECKBOX_TRANSITIVEPROPERTY_COMMAND = "checkbox_transitiveproperty";	
	private static final String CHECKBOX_INSTANCEPROPERTY_COMMAND = "checkbox_instanceproperty";
	private static final String CHECKBOX_DOMAIN_COMMAND = "checkbox_domain";
	private static final String CHECKBOX_RANGE_COMMAND = "checkbox_drange";
	
    public  boolean doLayout = true;
    public  OVDisplay display;
    private OWLOntology ontology = null;
    private JCheckBox chClass = null, chSubClass=null,
    chDisjointEdge=null,chCardinalityNode=null,chUnionOf=null,chIntersecionOf=null,chComplementOf=null,chEquivalent=null;
    private JCheckBox chIndywidual=null,chInstanceOf=null, chDifferent=null, chsameas=null, choneof=null; 
    private JCheckBox chproperty=null,chInstanceProperty=null,chInverseOfProperty=null, chDomain=null, chRange=null, chSubProperty=null,
    	chEquivalentProperty=null,chFunctionalProperty=null, chInversFunctionalProperty=null, chSymmetricProperty=null, chTransitiveProperty=null;
    public  void startVisualization() {

        
            display = new OVDisplay();
	    VisualizationProperties.instanceOf().loadProperties(Constants.PROPERTIES);
            // zoom with vertical right-drag
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                
            URI physicalURI = URI.create(Constants.ONTO_TEST_DIRECTORY);
            try {
                // Now ask the manager to load the ontology
            	ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
                display.generateGraphFromOWl(ontology);
//              display.generateTreeFromOWl(ontology);
            } catch (OWLOntologyCreationException ex) {
                Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
            }
            

            // create a new window to hold the visualization
            JFrame frame = new JFrame("SOVA prefuse usage demo");
            // ensure application exits when window is closed
            display.addKeyListener(new java.awt.event.KeyListener() {

                public void keyPressed(KeyEvent arg0) {
                    if (arg0.getKeyChar() == 'z') {
                        if (doLayout) {
                            display.getVisualization().stopLayout();
                            doLayout = false;
                        } else {
                            display.getVisualization().startLayout();
                            doLayout = true;
                        }
                    }
                }

                public void keyReleased(KeyEvent arg0) {
                }

                public void keyTyped(KeyEvent arg0) {
                }
            });
            JPanel visValues = new JPanel();
            visValues.setLayout(new BoxLayout(visValues, BoxLayout.Y_AXIS));
            if (display.getGraphLayout() == OVDisplay.FORCE_DIRECTED_LAYOUT) {
                  visValues.add(((ForceDirectedVis)display.getVisualization()).getControlPanel());
            }
            
            /*
             * Panel ustawianie dystansu w grafie
             * 
             */
        
			final JValueSlider slider = new JValueSlider("Distance", 1, 15,
					FilterOptions.distance);
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					display.getVisualization().setDistance( slider.getValue().intValue());
					display.getVisualization().refreshFilter();
				}
			});
			slider.setBackground(Color.WHITE);
			slider.setPreferredSize(new Dimension(200, 30));
			slider.setMaximumSize(new Dimension(220, 30));
	
			Box cf = new Box(BoxLayout.Y_AXIS);
			cf.add(slider);
			cf.setBorder(BorderFactory.createTitledBorder("Connectivity Filter"));
			JPanel panelDist = new JPanel();
			panelDist.add(cf);
			panelDist.setPreferredSize(new Dimension(220, 60));
			panelDist.setMaximumSize(new Dimension(250, 60));

            visValues.add(panelDist);
            JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
            buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rodzaje wizualizacji"));


            JRadioButton forceDirectedRadial = new JRadioButton("ForceDirectedLayout", true);
            forceDirectedRadial.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	display.changeVisualizationLayout(OVDisplay.FORCE_DIRECTED_LAYOUT);


                }
            });
            JRadioButton fruchtermanReingoldRadial = new JRadioButton("FruchtermanReingoldLayout", false);
            fruchtermanReingoldRadial.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	display.changeVisualizationLayout(OVDisplay.FRUCHTERMAN_REINGOLD_LAYOUT);


                }
            });
            JRadioButton radialTreeRadial = new JRadioButton("RadialTreeLayout", false);
            radialTreeRadial.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                	display.changeVisualizationLayout(OVDisplay.RADIAL_TREE_LAYOUT);
          
                }
            });
            ButtonGroup radialGroup = new ButtonGroup();
            radialGroup.add(forceDirectedRadial);
            radialGroup.add(fruchtermanReingoldRadial);
            radialGroup.add(radialTreeRadial);
            
            buttonPanel.add(forceDirectedRadial);
            buttonPanel.add(fruchtermanReingoldRadial);
            buttonPanel.add(radialTreeRadial);
            
            buttonPanel.setSize(200, 200);
            visValues.add(buttonPanel);
            CheckBoxListener checkboxListener = new CheckBoxListener();
            JPanel checkboxPanel = new JPanel(new GridLayout(10, 1));
            checkboxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filtry"));
            
            chClass = new JCheckBox("Klasy");
            chClass.setActionCommand(CHECKBOX_CLASS_COMMAND);
            chClass.setSelected(true);
            chClass.addActionListener(checkboxListener);	
            checkboxPanel.add(chClass);

            chSubClass = new JCheckBox("krawędzie SubEdge");
            chSubClass.setActionCommand(CHECKBOX_SUBCLASS_COMMAND);
            chSubClass.setSelected(true);
            chSubClass.addActionListener(checkboxListener);	
            checkboxPanel.add(chSubClass);


            chDisjointEdge = new JCheckBox("DisjointEdge");
            chDisjointEdge.setActionCommand(CHECKBOX_DISJOINT_CLASS_COMMAND);
            chDisjointEdge.addActionListener(checkboxListener);
            chDisjointEdge.setSelected(true);
            checkboxPanel.add(chDisjointEdge);
            
            chEquivalent = new JCheckBox("EquivalentEdge");
            chEquivalent.setActionCommand(CHECKBOX_EQUIVALENT_CLASS_COMMAND);
            chEquivalent.addActionListener(checkboxListener);
            chEquivalent.setSelected(true);
            checkboxPanel.add(chEquivalent);

            chCardinalityNode = new JCheckBox("Kardynalność");
            chCardinalityNode.setActionCommand(CHECKBOX_CARDINALITY_COMMAND);
            chCardinalityNode.addActionListener(checkboxListener);
            chCardinalityNode.setSelected(true);
            checkboxPanel.add(chCardinalityNode);
            
            chUnionOf = new JCheckBox("UnionOf");
            chUnionOf.setActionCommand(CHECKBOX_UNIONOF_COMMAND);
            chUnionOf.addActionListener(checkboxListener);
            chUnionOf.setSelected(true);
            checkboxPanel.add(chUnionOf);
            
            chIntersecionOf = new JCheckBox("IntersecionOf");
            chIntersecionOf.setActionCommand(CHECKBOX_INTERSECTION_COMMAND);
            chIntersecionOf.addActionListener(checkboxListener);
            chIntersecionOf.setSelected(true);
            checkboxPanel.add(chIntersecionOf);            
            
            chComplementOf = new JCheckBox("Complement");
            chComplementOf.setActionCommand(CHECKBOX_COMPLEMENT_COMMAND);
            chComplementOf.addActionListener(checkboxListener);
            chComplementOf.setSelected(true);
            checkboxPanel.add(chComplementOf);
            
            
            chIndywidual = new JCheckBox("Individual");
            chIndywidual.setActionCommand(CHECKBOX_INDYVIDUAL_COMMAND);
            chIndywidual.addActionListener(checkboxListener);
            chIndywidual.setSelected(true);
            checkboxPanel.add(chIndywidual);
            
            chInstanceOf = new JCheckBox("InstanceOf");
            chInstanceOf.setActionCommand(CHECKBOX_INSTANCEOF_COMMAND);
            chInstanceOf.addActionListener(checkboxListener);
            chInstanceOf.setSelected(true);
            checkboxPanel.add(chInstanceOf);
            
            chDifferent = new JCheckBox("different");
            chDifferent.setActionCommand(CHECKBOX_DIFFERENT_COMMAND);
            chDifferent.addActionListener(checkboxListener);
            chDifferent.setSelected(true);
            checkboxPanel.add(chDifferent);
            
            chsameas = new JCheckBox("sameAs");
            chsameas.setActionCommand(CHECKBOX_SAMEAS_COMMAND);
            chsameas.addActionListener(checkboxListener);
            chsameas.setSelected(true);
            checkboxPanel.add(chsameas);
            
            choneof = new JCheckBox("oneOf");
            choneof.setActionCommand(CHECKBOX_ONEOF_COMMAND);
            choneof.addActionListener(checkboxListener);
            choneof.setSelected(true);
            checkboxPanel.add(choneof);
            visValues.add(checkboxPanel);

            //property
            
            chproperty = new JCheckBox("Property");
            chproperty.setActionCommand(CHECKBOX_PROPERTY_COMMAND);
            chproperty.addActionListener(checkboxListener);
            chproperty.setSelected(true);
            checkboxPanel.add(chproperty);
            visValues.add(checkboxPanel);
            
            chSubProperty = new JCheckBox("subProperty");
            chSubProperty.setActionCommand(CHECKBOX_SUBPROPERTY_COMMAND);
            chSubProperty.addActionListener(checkboxListener);
            chSubProperty.setSelected(true);
            checkboxPanel.add(chSubProperty);
            visValues.add(checkboxPanel);
            
            chEquivalentProperty = new JCheckBox("equivalentProperty");
            chEquivalentProperty.setActionCommand(CHECKBOX_EQUIVALENT_COMMAND);
            chEquivalentProperty.addActionListener(checkboxListener);
            chEquivalentProperty.setSelected(true);
            checkboxPanel.add(chEquivalentProperty);
            visValues.add(checkboxPanel);
            
            chFunctionalProperty = new JCheckBox("functionalProperty");
            chFunctionalProperty.setActionCommand(CHECKBOX_FUNCTIONALPROPERTY_COMMAND);
            chFunctionalProperty.addActionListener(checkboxListener);
            chFunctionalProperty.setSelected(true);
            checkboxPanel.add(chFunctionalProperty);
            visValues.add(checkboxPanel);
            
            chInversFunctionalProperty = new JCheckBox("inversFunctionalProperty");
            chInversFunctionalProperty.setActionCommand(CHECKBOX_INVERSFUNCTIONALPROPERTY_COMMAND);
            chInversFunctionalProperty.addActionListener(checkboxListener);
            chInversFunctionalProperty.setSelected(true);
            checkboxPanel.add(chInversFunctionalProperty);
            visValues.add(checkboxPanel);
            
            chSymmetricProperty = new JCheckBox("symmetricProperty");
            chSymmetricProperty.setActionCommand(CHECKBOX_SYMMETRICPROPERTY_COMMAND);
            chSymmetricProperty.addActionListener(checkboxListener);
            chSymmetricProperty.setSelected(true);
            checkboxPanel.add(chSymmetricProperty);
            visValues.add(checkboxPanel);
            
            chInverseOfProperty = new JCheckBox("InversOfProperty");
            chInverseOfProperty.setActionCommand(CHECKBOX_INVERSEOFPROPERTY_COMMAND);
            chInverseOfProperty.addActionListener(checkboxListener);
            chInverseOfProperty.setSelected(true);
            checkboxPanel.add(chInverseOfProperty);
            visValues.add(checkboxPanel);
            
            chTransitiveProperty = new JCheckBox("transitiveProperty");
            chTransitiveProperty.setActionCommand(CHECKBOX_TRANSITIVEPROPERTY_COMMAND);
            chTransitiveProperty.addActionListener(checkboxListener);
            chTransitiveProperty.setSelected(true);
            checkboxPanel.add(chTransitiveProperty);
            visValues.add(checkboxPanel);
            
            chInstanceProperty = new JCheckBox("instancePropertyEdge");
            chInstanceProperty.setActionCommand(CHECKBOX_INSTANCEPROPERTY_COMMAND);
            chInstanceProperty.addActionListener(checkboxListener);
            chInstanceProperty.setSelected(true);
            checkboxPanel.add(chInstanceProperty);
            visValues.add(checkboxPanel);
            
            chDomain = new JCheckBox("domain");
            chDomain.setActionCommand(CHECKBOX_DOMAIN_COMMAND);
            chDomain.addActionListener(checkboxListener);
            chDomain.setSelected(true);
            checkboxPanel.add(chDomain);
            visValues.add(checkboxPanel);
            
            chRange = new JCheckBox("range");
            chRange.setActionCommand(CHECKBOX_RANGE_COMMAND);
            chRange.addActionListener(checkboxListener);
            chRange.setSelected(true);
            checkboxPanel.add(chRange);
            visValues.add(checkboxPanel);
            // create a search panel for the tree map
//            SearchQueryBinding sq = new SearchQueryBinding(
//                 (Table)display.getVisualization().getGroup(org.eti.kask.sova.graph.Constants.GRAPH_NODES), "node",
//                 (SearchTupleSet)display.getVisualization().getGroup(Visualization.SEARCH_ITEMS));
//            
//            JSearchPanel search = sq.createSearchPanel();
//            search.setShowResultCount(true);
//            search.setBorder(BorderFactory.createEmptyBorder(5,5,4,0));
//            search.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));
            
            Box v1 = new Box(BoxLayout.X_AXIS);
//            v1.add(search);
            JButton but = new JButton("Wł/Wy Animację");
            but.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    if (doLayout) {
                        display.getVisualization().stopLayout();
                        doLayout = false;
                    } else {
                        display.getVisualization().startLayout();
                        doLayout = true;
                    }
                }
            });
            v1.add(but);
            JButton but2 = new JButton("Reset");
            but2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                   display.removeDisplayVis();
                   display.generateGraphFromOWl(ontology); 
                }
            });
            v1.add(but2);

            v1.setBorder(BorderFactory.createTitledBorder("Opcje Animacji"));
            visValues.add(v1);
            // create a new JSplitPane to present the interface
            JSplitPane split = new JSplitPane();
            split.setLeftComponent(display);
            split.setRightComponent(visValues);
            split.setOneTouchExpandable(true);
            split.setContinuousLayout(false);
            split.setDividerLocation(700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(split);
            frame.pack(); // layout components in window
            frame.setVisible(true); // show the window



    }
    class CheckBoxListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getActionCommand().equals(CHECKBOX_CLASS_COMMAND)){
				if (chClass.isSelected()){
					FilterOptions.classFilter = true;
					chSubClass.setEnabled(true);
					chDisjointEdge.setEnabled(true);
					chUnionOf.setEnabled(true);
					chIntersecionOf.setEnabled(true);
					chCardinalityNode.setEnabled(true);
					chComplementOf.setEnabled(true);
					chEquivalent.setEnabled(true);
					
				}else{
					FilterOptions.classFilter = false;
					chSubClass.setEnabled(false);
					chDisjointEdge.setEnabled(false);
					chUnionOf.setEnabled(false);
					chIntersecionOf.setEnabled(false);
					chCardinalityNode.setEnabled(false);
					chComplementOf.setEnabled(false);
					chEquivalent.setEnabled(false);
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_SUBCLASS_COMMAND)){
				if (chSubClass.isSelected()){
					FilterOptions.subClassEdge = true;
				}else{
					FilterOptions.subClassEdge = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_DISJOINT_CLASS_COMMAND)){
				if (chDisjointEdge.isSelected()){
					FilterOptions.disjointClassEdge = true;
				}else{
					FilterOptions.disjointClassEdge = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_UNIONOF_COMMAND)){
				if (chUnionOf.isSelected()){
						FilterOptions.unionOf = true;
				}else{
						FilterOptions.unionOf = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_INTERSECTION_COMMAND)){
				if (chIntersecionOf.isSelected()){
						FilterOptions.intersectionOf = true;
				}else{
						FilterOptions.intersectionOf = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_EQUIVALENT_CLASS_COMMAND)){
			if (chEquivalent.isSelected()){
					FilterOptions.equivalentClassEdge = true;
			}else{
				FilterOptions.equivalentClassEdge = false;
			}
			}else
				if (e.getActionCommand().equals(CHECKBOX_COMPLEMENT_COMMAND)){
					if (chComplementOf.isSelected()){
							FilterOptions.complementOf = true;
					}else{
						FilterOptions.complementOf = false;
					}
				}else
			if (e.getActionCommand().equals(CHECKBOX_CARDINALITY_COMMAND)){
				if (chCardinalityNode.isSelected()){
					FilterOptions.cardinality = true;
				}else{
					FilterOptions.cardinality = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_INDYVIDUAL_COMMAND)){
				if (chIndywidual.isSelected()){
					FilterOptions.individual = true;
					chInstanceOf.setEnabled(true);
					chDifferent.setEnabled(true);
					chsameas.setEnabled(true);
					choneof.setEnabled(true);
				}else{
					FilterOptions.individual = false;
					chInstanceOf.setEnabled(false);
					chDifferent.setEnabled(false);
					chsameas.setEnabled(false);
					choneof.setEnabled(false);
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_INSTANCEOF_COMMAND)){
				if (chInstanceOf.isSelected()){
					FilterOptions.instanceOfEdge = true;
				}else{
					FilterOptions.instanceOfEdge = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_DIFFERENT_COMMAND)){
				if (chDifferent.isSelected()){
					FilterOptions.different = true;
				}else{
					FilterOptions.different = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_SAMEAS_COMMAND)){
				if (chsameas.isSelected()){
					FilterOptions.sameAs = true;
				}else{
					FilterOptions.sameAs = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_ONEOF_COMMAND)){
				if (choneof.isSelected()){
					FilterOptions.oneOf = true;
				}else{
					FilterOptions.oneOf = false;

				}
			}else//property
			if (e.getActionCommand().equals(CHECKBOX_PROPERTY_COMMAND)){
				if (chproperty.isSelected()){
					FilterOptions.property = true;
					chSubProperty.setEnabled(true);
					chEquivalentProperty.setEnabled(true);
					chFunctionalProperty.setEnabled(true);
					chInversFunctionalProperty.setEnabled(true);
					chSymmetricProperty.setEnabled(true);
					chTransitiveProperty.setEnabled(true);
					chInstanceProperty.setEnabled(true);
					chDomain.setEnabled(true);
					chRange.setEnabled(true);
					chInverseOfProperty.setEnabled(true);
					
				}else{
					FilterOptions.property = false;
					chSubProperty.setEnabled(false);
					chEquivalentProperty.setEnabled(false);
					chFunctionalProperty.setEnabled(false);
					chInversFunctionalProperty.setEnabled(false);
					chSymmetricProperty.setEnabled(false);
					chTransitiveProperty.setEnabled(false);
					chInstanceProperty.setEnabled(false);
					chDomain.setEnabled(false);
					chRange.setEnabled(false);
					chInverseOfProperty.setEnabled(false);
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_SUBPROPERTY_COMMAND)){
				if (chSubProperty.isSelected()){
					FilterOptions.subPropertyEdge = true;
				}else{
					FilterOptions.subPropertyEdge = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_EQUIVALENT_COMMAND)){
				if (chEquivalentProperty.isSelected()){
					FilterOptions.equivalentPropertyEdge = true;
				}else{
					FilterOptions.equivalentPropertyEdge = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_INVERSEOFPROPERTY_COMMAND)){
				if (chInverseOfProperty.isSelected()){
					FilterOptions.inverseOfProperty = true;
				}else{
					FilterOptions.inverseOfProperty = false;
				}
			}else	
			if (e.getActionCommand().equals(CHECKBOX_FUNCTIONALPROPERTY_COMMAND)){
				if (chFunctionalProperty.isSelected()){
					FilterOptions.functionalProperty = true;
				}else{
					FilterOptions.functionalProperty = false;
				}

			}else
			if (e.getActionCommand().equals(CHECKBOX_INVERSFUNCTIONALPROPERTY_COMMAND)){
				if (chInversFunctionalProperty.isSelected()){
					FilterOptions.inverseFunctionalProperty = true;
				}else{
					FilterOptions.inverseFunctionalProperty = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_SYMMETRICPROPERTY_COMMAND)){
				if (chSymmetricProperty.isSelected()){
					FilterOptions.symmetricProperty = true;
				}else{
					FilterOptions.symmetricProperty = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_TRANSITIVEPROPERTY_COMMAND)){
				if (chTransitiveProperty.isSelected()){
					FilterOptions.transitiveProperty = true;
				}else{
					FilterOptions.transitiveProperty = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_INSTANCEPROPERTY_COMMAND)){
				if (chInstanceProperty.isSelected()){
					FilterOptions.instanceProperty = true;
				}else{
					FilterOptions.instanceProperty = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_DOMAIN_COMMAND)){
				if (chDomain.isSelected()){
					FilterOptions.domain = true;
				}else{
					FilterOptions.domain = false;
				}
			}else
			if (e.getActionCommand().equals(CHECKBOX_RANGE_COMMAND)){
				if (chRange.isSelected()){
					FilterOptions.range = true;
				}else{
					FilterOptions.range = false;
				}
			}
			
	
			display.getVisualization().refreshFilter();			
		}
    	
    }
}