package org.pg.eti.kask.sova.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.pg.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owlapi.model.OWLOntology;

public class HierarchyPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3124793583527560797L;
	private OWLOntology ontology = null;
	private JPanel leftPanel = null, rightPanel = null;
	private OVDisplay display;
	private boolean showFullTree = true;
	private JButton but3=null;
	public HierarchyPanel(OWLOntology ontology){
		this.ontology = ontology;
		initialiseOWLView();
	}
	private void initialiseOWLView(){
		display = new OVDisplay();
		display.setSize(1000, 900);
		display.generateTreeFromOWl(ontology);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		display.setSize(1000, 900);
		initLeftPanel();
		this.add(leftPanel);
		initRightPanel();
		this.add(rightPanel);

	}

	private void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource(
				"SOVA.png")).getImage());
		rightPanel.add(icon);
		JPanel buttonPanel = new JPanel(new GridLayout(8, 1));
		JButton but2 = new JButton("Reset");
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				display.removeDisplayVis();
				display.generateTreeFromOWl(ontology);

			}
		});
		but2.setSize(100, 80);
		but2.setToolTipText("Reload ontology");
		buttonPanel.add(but2);

		but3 = new JButton("Show Full Tree");
		but3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (showFullTree){
					display.showFullTree();
					but3.setText("Hide Full Tree");
					showFullTree = false;
				}else{
					display.hideFullTree();
					but3.setText("Show Full Tree");
					showFullTree = true;				
				}
			}
		});
		buttonPanel.add(but3);
		
		rightPanel.add(buttonPanel);
		rightPanel.setPreferredSize(new Dimension(120, Integer.MAX_VALUE));
		rightPanel.setMaximumSize(new Dimension(140, Integer.MAX_VALUE));
		rightPanel.setMinimumSize(new Dimension(100, Integer.MAX_VALUE));
	}

	private void initLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.add(display);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
	}
}
