package org.pg.eti.kask.sova.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.pg.eti.kask.sova.visualization.OVDisplay;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

public class SovaVisualizationPanel extends JPanel {
	private static final long serialVersionUID = -4515710047558710080L;
	private OVDisplay display;
	private Options optionFrame = null;
	public boolean doLayout = true;
	private JButton options = null;
	private JPanel leftPanel = null, rightPanel = null;
	private AnnotationPanel annotation;
	private OWLOntology ontology = null;

	protected void disposeOWLView() {
		display.removeDisplayVis();
	}

	public SovaVisualizationPanel(OWLOntology onto) {
		this.ontology = onto;
		annotation = new AnnotationPanel();
		display = new OVDisplay(ontology);
		display.setSize(800, 600);
		display.addAnnotationComponent(annotation);
		display.generateGraphFromOWl();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		initLeftPanel();
		this.add(leftPanel);
		initRightPanel();
		this.add(rightPanel);
	}
	/**
	 * inicjalizacja panelu nawigacyjnego 
	 */
	private void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource(
				"SOVA.png")).getImage());
		rightPanel.add(icon);
		JPanel buttonPanel = new JPanel(new GridLayout(5, 1));
		JButton but = new JButton("Play/Stop");
		but.setToolTipText("Play or stop animation");
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
		but.setSize(100, 80);
		buttonPanel.add(but);
		JButton but2 = new JButton("Reset");
		but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				display.removeDisplayVis();
				display.generateGraphFromOWl(ontology);
				doLayout = true;
			}
		});
		but2.setSize(100, 80);
		but2.setToolTipText("Reload ontology");
		buttonPanel.add(but2);

		options = new JButton("Options");
		options.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (optionFrame == null) {
					initOptionFrame();
				}
				if (!optionFrame.isOptionFrameShow()) {
					optionFrame.setVisible(true);
					optionFrame.setOptionFrameShow(true);
				} else {
					optionFrame.setVisible(false);
					optionFrame.setOptionFrameShow(false);
				}
			}
		});
		options.setSize(100, 80);
		buttonPanel.add(options);
		rightPanel.add(buttonPanel);
		rightPanel.add(annotation);
		rightPanel.setPreferredSize(new Dimension(180, Integer.MAX_VALUE));
		rightPanel.setMaximumSize(new Dimension(190, Integer.MAX_VALUE));
		rightPanel.setMinimumSize(new Dimension(170, Integer.MAX_VALUE));
	}
	/**
	 * inicjalizacja panelu wizualizacji 
	 */
	private void initLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.add(display);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
	}

	private void initOptionFrame() {
		optionFrame = new Options(display);
		Point location = options.getLocationOnScreen();
		location.x -= optionFrame.getSize().width;
		location.y -= 200;
		optionFrame.setLocation(location);
	}

}
