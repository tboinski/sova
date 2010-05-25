package org.pg.eti.kask.ont.pluginSova;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.eti.kask.sova.visualization.OVDisplay;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

public class HierarchyTreeVis extends AbstractOWLViewComponent {

	private  OVDisplay display;
	private JPanel panel = null;
	@Override
	protected void disposeOWLView() {
		display.removeDisplayVis();	
	}

	@Override
	protected void initialiseOWLView() throws Exception {
		if (display == null){
    		display = new OVDisplay();
    		display.setSize(800, 600);
    	
    		
        }
    	display.generateTreeFromOWl(getOWLModelManager().getActiveOntology());
    	this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	if (panel == null){
	    	panel = new JPanel();
			panel.add(display);
			panel.setSize(800, 600);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			this.add(panel);
			this.repaint();
        }
	}

}
