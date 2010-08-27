package org.pg.eti.kask.ont.pluginSova;

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
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;



public class SovaVisualization extends AbstractOWLViewComponent {

	private static final long serialVersionUID = -4515710047558710080L;
    private  OVDisplay display;

    public  boolean doLayout = true;
    private JButton options = null;
    private Options optionFrame=null;
    private boolean isOptionFrameShow = false;
    private  JPanel leftPanel = null, rightPanel = null;
    private AnnotationPanel annotation;
    @Override
    protected void disposeOWLView() {
    	display.removeDisplayVis();
    }

    @Override
    protected void initialiseOWLView() throws Exception {
    	if (display == null){
    		display = new OVDisplay(getOWLModelManager().getActiveOntology());
    		annotation = new AnnotationPanel();
    		display.addAnnotationComponent(annotation);
    		display.setSize(800, 600);
        }
		display.setOntology(getOWLModelManager().getActiveOntology());
    	display.generateGraphFromOWl();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        if (leftPanel == null){
        	initLeftPanel();
        	this.add(leftPanel);
        }
        if (rightPanel==null){
        	initRightPanel();
        	this.add(rightPanel);
        }
    }

	private void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		ImagePanel icon = new ImagePanel(new ImageIcon(getClass().getResource(
				"/img/SOVA.png")).getImage());
		rightPanel.add(icon);
		JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
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
				display.generateGraphFromOWl(getOWLModelManager()
						.getActiveOntology());
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
				if (!isOptionFrameShow){
					optionFrame.setVisible(true);
					isOptionFrameShow = true;
				}else{
					optionFrame.setVisible(false);
					isOptionFrameShow = false;
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
    private void initLeftPanel(){
        leftPanel = new JPanel();
		leftPanel.add(display);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    }
    private void initOptionFrame(){
		optionFrame = new Options(display);
		Point location = options.getLocationOnScreen();
		location.x -= optionFrame.getSize().width;
		location.y -= 250;
		optionFrame.setLocation(location);
    }
  
}
