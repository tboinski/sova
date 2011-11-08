/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.sova.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import org.pg.eti.kask.sova.options.NodeColors;
import org.pg.eti.kask.sova.options.NodeShapes;
import org.pg.eti.kask.sova.utils.Debug;
import org.pg.eti.kask.sova.utils.VisualizationProperties;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphicsLib;
import prefuse.visual.VisualItem;

/**
 * Klasa odpowiadająca za poprawne rysowanie węzłów.
 */
public class NodeRenderer extends prefuse.render.LabelRenderer
{

	protected AffineTransform m_transform = new AffineTransform();
	protected NodeColors colorScheme = new NodeColors(VisualizationProperties.instanceOf());
	protected NodeShapes shapeScheme = new NodeShapes();
	protected ShapeRenderer shapeRenderer = new ShapeRenderer();

	/**
	 *
	 */
	public NodeRenderer()
	{
		super();
	}

	public NodeRenderer(String SOVAnodeField)
	{
		super(SOVAnodeField);
	}

	/**
	 * Metoda odpowiadająca za poprawne rysowanie węzłów.
	 */
	@Override
	public void render(Graphics2D g, VisualItem item)
	{
		org.pg.eti.kask.sova.nodes.Node SOVAnode = (org.pg.eti.kask.sova.nodes.Node)item.get(org.pg.eti.kask.sova.graph.Constants.GRAPH_NODES);
		// Ustawienie kolorów wypełnienia, obramowania oraz tekstu węzła
		item.setFillColor(ColorLib.color(SOVAnode.getFillColorFromScheme(colorScheme)));
		item.setStrokeColor(ColorLib.color(Color.BLACK));
		item.setStroke(new BasicStroke(SOVAnode.getStrokeWitdh()));
		item.setTextColor(ColorLib.color(Color.BLACK));
		if (item.isHighlighted()){
			item.setFillColor(ColorLib.color(Color.WHITE));
		}
		if (item.isInGroup(Visualization.SEARCH_ITEMS)){
			item.setFillColor(ColorLib.color(Color.RED));
			item.setTextColor(ColorLib.color(Color.WHITE));
		}
		//Dopasowuje kształt węzła do schematu
		Shape shape = getShape(item);
		if (shape == null) {
			Debug.sendMessage("Brak kształtu węzła {getShape(item)==NULL}!!");
			return;
		}
		switch(SOVAnode.getNodeShapeType(shapeScheme)) {
			case RECTANGLE: setRoundedCorner(0, 0);
					shape = getShape(item);
					break;
			case ROUNDED_RECTANGLE: setRoundedCorner(8, 8);
					shape = getShape(item);
					break;
			case CIRCLE: shape = changeShapeToCircle(shape);
					break;
			case ELLIPSE: shape = changeShapeToEllipse(shape);
					break;
		}	
		// Wypełnienie węzła
		int type = getRenderType(item);
		if (type == RENDER_TYPE_FILL || type == RENDER_TYPE_DRAW_AND_FILL) {
			GraphicsLib.paint(g, item, shape, getStroke(item), RENDER_TYPE_FILL);
		}

		// Wyrenderowanie tekstu i obrazka
		String text = m_text;
		Image img = getImage(item);

		if (text == null && img == null) {
			return;
		}

		double size = item.getSize();
		boolean useInt = 1.5 > Math.max(g.getTransform().getScaleX(),
			g.getTransform().getScaleY());
		double x = shape.getBounds().getMinX() + size * m_horizBorder;
		double y = shape.getBounds().getMinY() + size * m_vertBorder;
		
		// render image
		if (img != null) {
			double w = size * img.getWidth(null);
			double h = size * img.getHeight(null);
			double ix = x, iy = y;

			// determine one co-ordinate based on the image position
			switch (m_imagePos) {
				case Constants.LEFT:
					x += w + size * m_imageMargin;
					break;
				case Constants.RIGHT:
					ix = shape.getBounds().getMaxX() - size * m_horizBorder - w;
					break;
				case Constants.TOP:
					y += h + size * m_imageMargin;
					break;
				case Constants.BOTTOM:
					iy = shape.getBounds().getMaxY() - size * m_vertBorder - h;
					break;
				default:
					throw new IllegalStateException(
						"Unrecognized image alignment setting.");
			}

			// determine the other coordinate based on image alignment
			switch (m_imagePos) {
				case Constants.LEFT:
				case Constants.RIGHT:
					// need to set image y-coordinate
					switch (m_vImageAlign) {
						case Constants.TOP:
							break;
						case Constants.BOTTOM:
							iy = shape.getBounds().getMaxY() - size * m_vertBorder - h;
							break;
						case Constants.CENTER:
							iy = shape.getBounds().getCenterY() - h / 2;
							break;
					}
					break;
				case Constants.TOP:
				case Constants.BOTTOM:
					// need to set image x-coordinate
					switch (m_hImageAlign) {
						case Constants.LEFT:
							break;
						case Constants.RIGHT:
							ix = shape.getBounds().getMaxX() - size * m_horizBorder - w;
							break;
						case Constants.CENTER:
							ix = shape.getBounds().getCenterX() - w / 2;
							break;
					}
					break;
			}

			if (useInt && size == 1.0) {
				// if possible, use integer precision
				// results in faster, flicker-free image rendering
				g.drawImage(img, (int) ix, (int) iy, null);
			} else {
				m_transform.setTransform(size, 0, 0, size, ix, iy);
				g.drawImage(img, m_transform, null);
			}
		}

		// render text
		int textColor = item.getTextColor();

		/* Poniższe ustawienia czcionki są generowane przez bibliotekę
		 * Prefuse, a w przypadku jej powiększenia trzeba ręcznie
		 * modyfikować kształt/rozmiar węzła.
		 */
		//m_font = new Font("Arial", Font.BOLD, 20);
		

		if (text != null && ColorLib.alpha(textColor) > 0) {
			g.setPaint(ColorLib.getColor(textColor));
			g.setFont(m_font);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);

			// compute available width
			double tw;
			switch (m_imagePos) {
				case Constants.TOP:
				case Constants.BOTTOM:
					tw = shape.getBounds().getWidth() - 2 * size * m_horizBorder;
					break;
				default:
					tw = m_textDim.width;
			}

			// compute available height
			double th;
			switch (m_imagePos) {
				case Constants.LEFT:
				case Constants.RIGHT:
					th = shape.getBounds().getHeight() - 2 * size * m_vertBorder;
					break;
				default:
					th = m_textDim.height;
			}

			// compute starting y-coordinate
			y += fm.getAscent();
			switch (m_vTextAlign) {
				case Constants.TOP:
					break;
				case Constants.BOTTOM:
					y += th - m_textDim.height;
					break;
				case Constants.CENTER:
					y += (th - m_textDim.height) / 2;
			}

			// render each line of text
			int lh = fm.getHeight(); // the line height

			//wyśrodkowanie tekstu - można to zrobić lepiej... chyba
			int xAxisTranslation = (int) ((shape.getBounds().width - tw - 2) / 2);

			int start = 0, end = text.indexOf(m_delim);
			for (; end >= 0; y += lh) {
				drawString(g, fm, text.substring(start, end), useInt, x+xAxisTranslation, y, tw);
				start = end + 1;
				end = text.indexOf(m_delim, start);
			}
			drawString(g, fm, text.substring(start), useInt, x+xAxisTranslation, y, tw);
		}
		if (item.isFixed()){
			item.setStroke(new BasicStroke(3));
		}
		// draw border
		if (type == RENDER_TYPE_DRAW || type == RENDER_TYPE_DRAW_AND_FILL) {
			GraphicsLib.paint(g, item, shape, getStroke(item), RENDER_TYPE_DRAW);
		}

	}

	protected void drawString(Graphics2D g, FontMetrics fm, String text,
		boolean useInt, double x, double y, double w)
	{
		// compute the x-coordinate
		double tx;
		switch (m_hTextAlign) {
			case Constants.LEFT:
				tx = x;
				break;
			case Constants.RIGHT:
				tx = x + w - fm.stringWidth(text);
				break;
			case Constants.CENTER:
				tx = x + (w - fm.stringWidth(text)) / 2;
				break;
			default:
				throw new IllegalStateException(
					"Unrecognized text alignment setting.");
		}
		// use integer precision unless zoomed-in
		// results in more stable drawing
		if (useInt) {
			g.drawString(text, (int) tx, (int) y);
		} else {
			g.drawString(text, (float) tx, (float) y);
		}
	}

	/**
	 * <p>Zamienia domyślny, prostokątny kształt węzła na koło.
	 * Koło to ma średnicę równą szerokości lub wysokości węzła
	 * (większa z wartości) i środek w tym samym punkcie.
	 * </p><p>
	 * Zmiana kształtu na koło nie jest zalecana dla węzłów
	 * o długich etykietach.
	 * </p>
	 * @param rectangleShape prostokątny kształt
	 * @return okrągły kształt
	 */
	public Shape changeShapeToCircle(Shape rectangleShape) {

		int r = Math.max(rectangleShape.getBounds().width,
			rectangleShape.getBounds().height) / 2 + 1;

		return shapeRenderer.ellipse(
			rectangleShape.getBounds().getCenterX() - r,
			rectangleShape.getBounds().getCenterY() - r,
			2*r,
			2*r
			);
	}

	/**
	 * <p>Zamienia domyślny, prostokątny kształt węzła na elipsę.
	 * Elipsa ta ma średnicę pionową równą wysokości węzła oraz średnicę
	 * poziomą równą 1,5 jego szerokości i środek w tym samym punkcie.
	 * </p><p>
	 * Zmiana kształtu na elipsę nie jest zalecana dla węzłów
	 * o długich etykietach.
	 * </p>
	 * @param rectangleShape prostokątny kształt
	 * @return eliptyczny kształt
	 */
	public Shape changeShapeToEllipse(Shape rectangleShape) {

		/*int r = Math.max(rectangleShape.getBounds().width,
			rectangleShape.getBounds().height) / 2 + 1;*/

		int rHeight = rectangleShape.getBounds().height / 2 + 1;
		int rWidth = rectangleShape.getBounds().width / 2 + 1;

		return shapeRenderer.ellipse(
			rectangleShape.getBounds().getCenterX() - rWidth,
			rectangleShape.getBounds().getCenterY() - rHeight,
			3*rWidth,
			2*rHeight
			);
	}

	/**
	 * @return schemat kolorów dla węzłów
	 * @see NodeColors
	 */
	public NodeColors getColorScheme()
	{
		return colorScheme;
	}

	/**
	 * Ustawia schemat kolorów dla węzłów.
	 * @param colorScheme schemat kolorów dla węzłów
	 * @see NodeColors
	 */
	public void setColorScheme(NodeColors colorScheme)
	{
		this.colorScheme = colorScheme;
	}
}

