package org.pg.eti.kask.ont.editor.graph.action;

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
		if (item.isHighlighted())
			color = Constants.NODE_COLOR_HIGHLIGHTED;
		else if (item.isFixed())
			color = Constants.NODE_COLOR_SELECTED;
		else if (item.canGetString(Constants.GRAPH_EDGE_TYPE_COLUMN)) {
			if (item.getString(Constants.GRAPH_EDGE_TYPE_COLUMN) != null) {
				if (item.getString(Constants.GRAPH_EDGE_TYPE_COLUMN).equals(Constants.GRAPH_NODE_CLASS_TYPE))
					color = Constants.NODE_COLOR_CLASS;				
			}
		}

		return color;
	}
}