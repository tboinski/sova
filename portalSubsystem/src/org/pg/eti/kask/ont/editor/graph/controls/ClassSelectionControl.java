package org.pg.eti.kask.ont.editor.graph.controls;

import java.awt.event.MouseEvent;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.panels.GraphPanel;

import prefuse.controls.Control;
import prefuse.controls.ControlAdapter;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;

/**
 * Klasa przeprowadzajca aktualizacje wszystkich elementow
 * wyswietlajacych dane aktualnie zaznaczonej klasy na grafie.
 * 
 * @author Andrzej Jakowski
 */
public class ClassSelectionControl extends ControlAdapter {
	
	private GraphPanel graphPanel;
	
	public ClassSelectionControl(GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
	}

	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		//tutaj ma nastapic update panelu z wlasciwosciami
		//sprawdzenie czy kliknieto wezel lewym przyciskiem
		if(UILib.isButtonPressed(e, Control.LEFT_MOUSE_BUTTON)) {
			if(Constants.GRAPH_NODES.equals(item.getGroup())) {
				if(item.canGetString(Constants.GRAPH_NODE_URI_COLUMN_NAME)) {
					String uri = item.getString(Constants.GRAPH_NODE_URI_COLUMN_NAME);
					graphPanel.updatePropertiesPanel(uri);
				}
			}
		}
		
		if(UILib.isButtonPressed(e, Control.RIGHT_MOUSE_BUTTON)) {
			if(Constants.GRAPH_NODES.equals(item.getGroup())) {
				if(item.canGetString(Constants.GRAPH_NODE_URI_COLUMN_NAME)) {
				}
			}
		}
	}

	@Override
	public void itemEntered(VisualItem item, MouseEvent e) {
		//tutaj ma nastapic update pola z uri tuz pod grafem
		if(Constants.GRAPH_NODES.equals(item.getGroup())) {
			if(item.canGetString(Constants.GRAPH_NODE_URI_COLUMN_NAME)) {
				String uri = item.getString(Constants.GRAPH_NODE_URI_COLUMN_NAME);
				graphPanel.updateUriLabel(uri);
			}
		}
	}

	@Override
	public void itemExited(VisualItem item, MouseEvent e) {
		graphPanel.updateUriLabel("");
	}

}
