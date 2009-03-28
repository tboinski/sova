package org.pg.eti.kask.ont.editor.tree.action;

import java.awt.geom.Point2D;

import org.pg.eti.kask.ont.editor.tree.TreeDisplay;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.VisualItem;

/**
 * Klasa realizujaca automatyczne przesuniecie drzewa, 
 * tak ze aktualny wezel bedzie widoczny w ustalonym punkcie.
 *
 * @author Andrzej Jakowski
 */
public class AutoPanAction extends Action {

	private TreeDisplay display;

	private Point2D startPoint = new Point2D.Double();
	private Point2D endPoint = new Point2D.Double();
	private Point2D currentPoint = new Point2D.Double();
	private int bias = 150;

	public AutoPanAction(TreeDisplay display) {
		this.display = display;
		this.m_vis = display.getVisualization();
	}

	@Override
	public void run(double frac) {
		TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
		if (ts.getTupleCount() == 0)
			return;
		if (frac < 0.003) {
			int xbias = 0, ybias = 0;
			switch (display.getOrientation()) {
			case Constants.ORIENT_LEFT_RIGHT:
				xbias = bias;
				break;
			case Constants.ORIENT_RIGHT_LEFT:
				xbias = -bias;
				break;
			case Constants.ORIENT_TOP_BOTTOM:
				ybias = bias;
				break;
			case Constants.ORIENT_BOTTOM_TOP:
				ybias = -bias;
				break;
			}
			VisualItem vi = (VisualItem) ts.tuples().next();
			currentPoint.setLocation(display.getWidth() / 2, display.getHeight() / 2);
			display.getAbsoluteCoordinate(currentPoint, startPoint);
			endPoint.setLocation(vi.getX() + xbias, vi.getY() + ybias);

		} else {
			currentPoint.setLocation(startPoint.getX()+frac*(endPoint.getX()-startPoint.getX()), 
					startPoint.getY()+frac*(endPoint.getY()-startPoint.getY()));

			display.panToAbs(currentPoint);
		}
	}

}
