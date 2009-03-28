package org.pg.eti.kask.ont.editor.tree.controls;

import java.awt.event.MouseEvent;

import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.panels.TreePanel;

import prefuse.controls.Control;
import prefuse.controls.ControlAdapter;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;

/**
 * Klasa przeprowadzajca aktualizacje wszystkich elementow
 * wyswietlajacych dane aktualnie zaznaczonej klasy na drzewie.
 * 
 * @author Andrzej Jakowski
 */
public class ClassSelectionControl extends ControlAdapter {
	
	private TreePanel treePanel;
	
	public ClassSelectionControl(TreePanel treePanel) {
		this.treePanel = treePanel;
	}

	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		//tutaj ma nastapic update panelu z wlasciwosciami
		//sprawdzenie czy kliknieto wezel lewym przyciskiem
		if(UILib.isButtonPressed(e, Control.LEFT_MOUSE_BUTTON)) {
			if(Constants.TREE_NODES.equals(item.getGroup())) {
				if(item.canGetString(Constants.TREE_NODE_URI_COLUMN)) {
				}
			}
		}
		
		if(UILib.isButtonPressed(e, Control.RIGHT_MOUSE_BUTTON)) {
			if(Constants.TREE_NODES.equals(item.getGroup())) {
				if(item.canGetString(Constants.TREE_NODE_URI_COLUMN)) {
				}
			}
		}
	}

	@Override
	public void itemEntered(VisualItem item, MouseEvent e) {
		//tutaj ma nastapic update pola z uri tuz pod grafem
		if(Constants.TREE_NODES.equals(item.getGroup())) {
			if(item.canGetString(Constants.TREE_NODE_URI_COLUMN)) {
				String uri = item.getString(Constants.TREE_NODE_URI_COLUMN);
				treePanel.updateUriLabel(uri);
			}
		}
	}

	@Override
	public void itemExited(VisualItem item, MouseEvent e) {
		treePanel.updateUriLabel("");
	}

}
