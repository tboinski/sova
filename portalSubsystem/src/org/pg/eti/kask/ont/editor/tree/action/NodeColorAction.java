package org.pg.eti.kask.ont.editor.tree.action;

import org.pg.eti.kask.ont.editor.consts.Constants;

import prefuse.Visualization;
import prefuse.action.assignment.ColorAction;
import prefuse.visual.VisualItem;

/**
 * Klasa realizujaca kolorowanie wezlow grafu w ontologii. 
 *
 * @author Andrzej Jakowski
 */
public class NodeColorAction extends ColorAction {

	public NodeColorAction(String group, Visualization visualization) {
		super(group, VisualItem.FILLCOLOR);
		m_vis = visualization;
	}

	/**
	 * Zwraca kolor dla danego elementu.
	 */
	@Override
	public int getColor(VisualItem item) {
		int color = Constants.NODE_DEFAULT_COLOR;

		//ustaleneie koloru
		if (m_vis.isInGroup(item, Visualization.FOCUS_ITEMS)) {
			color = Constants.NODE_COLOR_SELECTED;
		} else if (item.getDOI() > -1) {
			color = Constants.NODE_COLOR_HIGHLIGHTED;
		} else if (item.canGetString(Constants.TREE_NODE_TYPE_COLUMN)) {
			String nodeType = item.getString(Constants.TREE_NODE_TYPE_COLUMN);
			if (nodeType != null && !nodeType.equals("")) {
				if (nodeType.equals(Constants.TREE_NODE_CLASS_TYPE)) {
					color = Constants.NODE_COLOR_CLASS;
				} else if (nodeType.equals(Constants.TREE_NODE_INDIVIDUAL_TYPE)) {
					color = Constants.NODE_COLOR_INDIVIDUAL;
				}
			}
		}

		return color;
	}
}