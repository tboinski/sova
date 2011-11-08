/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.sova.options;

import java.awt.Color;

import org.pg.eti.kask.sova.utils.VisualizationProperties;

/**
 * Klasa przechowująca informacje o ustawieniach kolorów dla krawędzi.
 * @see org.pg.eti.kask.sova.edges.Edge
 */
public class EdgeColors
{

	protected Color rangeEdgeColor;
	protected Color domainEdgeColor;
	protected Color edgeColor;
	protected Color equivalentEdgeColor;
	protected Color disjointEdgeColor;
	protected Color equivalentPropertyEdgeColor;
	protected Color functionalEdgeColor;
	protected Color inverseOfEdgeColor;
	protected Color propertyEdgeColor;
	protected Color subClassEdgeColor;
	protected Color subPropertyEdgeColor;
	protected Color inverseOfMutualEdgeColor;
	protected Color operationEdgeColor;
	protected Color instanceOfColor;
	protected Color instancePropertyColor;
	public EdgeColors()
	{
		setDefaultColors();
	}

	public EdgeColors(VisualizationProperties colorProperties) {
		setDefaultColors();
		loadColors(colorProperties);
	}

	/**
	 * Zmienia ustawienia kolorów na załadowane w VisualizationProperies.
	 * Changes color setting to ones loaded in VisualizationProperies.
	 */
	public void loadColors(VisualizationProperties colorProperties) {
		String prefix = "edge.color.";

		edgeColor = colorProperties.getPropertyColor(prefix + "edgeColor", edgeColor);
		propertyEdgeColor = colorProperties.getPropertyColor(prefix + "propertyEdgeColor", propertyEdgeColor);
		domainEdgeColor = colorProperties.getPropertyColor(prefix + "domainEdgeColor", domainEdgeColor);
		rangeEdgeColor = colorProperties.getPropertyColor(prefix + "rangeEdgeColor", rangeEdgeColor);
		disjointEdgeColor = colorProperties.getPropertyColor(prefix + "disjointEdgeColor", disjointEdgeColor);
		equivalentEdgeColor = colorProperties.getPropertyColor(prefix + "equivalentEdgeColor", equivalentEdgeColor);
		equivalentPropertyEdgeColor = colorProperties.getPropertyColor(prefix + "equivalentPropertyEdgeColor", equivalentPropertyEdgeColor);
		functionalEdgeColor = colorProperties.getPropertyColor(prefix + "functionalEdgeColor", functionalEdgeColor);
		inverseOfEdgeColor = colorProperties.getPropertyColor(prefix + "inverseOfEdgeColor", inverseOfMutualEdgeColor);
		inverseOfMutualEdgeColor = colorProperties.getPropertyColor(prefix + "inverseOfMutualEdgeColor", inverseOfEdgeColor);
		operationEdgeColor = colorProperties.getPropertyColor(prefix + "operationEdgeColor", operationEdgeColor);
		subClassEdgeColor = colorProperties.getPropertyColor(prefix + "subClassEdgeColor", subClassEdgeColor);
		subPropertyEdgeColor = colorProperties.getPropertyColor(prefix + "subPropertyEdgeColor", subPropertyEdgeColor);
		instanceOfColor = colorProperties.getPropertyColor(prefix + "instanceOfEdgeColor", instanceOfColor);
		instancePropertyColor = colorProperties.getPropertyColor(prefix + "instancePropertyEdgeColor", instancePropertyColor);
	}

	protected void setDefaultColors() {
		// Ustawienia domyślne
		edgeColor = Color.BLACK;
		propertyEdgeColor = Color.BLACK;
		domainEdgeColor = Color.BLACK;
		rangeEdgeColor = Color.BLACK;
		disjointEdgeColor = Color.BLACK;
		equivalentEdgeColor = Color.BLACK;
		equivalentPropertyEdgeColor = Color.GREEN;
		inverseOfEdgeColor = Color.RED;
		functionalEdgeColor = new Color(200, 113, 55);
		inverseOfMutualEdgeColor = inverseOfEdgeColor;
		operationEdgeColor = Color.BLACK;
		subClassEdgeColor = Color.BLACK;
		subPropertyEdgeColor = Color.BLACK;
		instanceOfColor = Color.BLACK;
		instancePropertyColor = Color.BLACK;
	}

	public Color getInstanceOfColor() {
		return instanceOfColor;
	}

	public void setInstanceOfColor(Color val) {
		this.instanceOfColor = val;
	}

	public Color getDomainEdgeColor()
	{
		return domainEdgeColor;
	}

	public void setDomainEdgeColor(Color val)
	{
		this.domainEdgeColor = val;
	}

	public Color getInstancePropertyColor() {
		return instancePropertyColor;
	}

	public void setInstancePropertyColor(Color val) {
		this.instancePropertyColor = val;
	}

	public Color getEdgeColor()
	{
		return edgeColor;
	}

	public void setEdgeColor(Color val)
	{
		this.edgeColor = val;
	}

	public Color getEquivalentEdgeColor()
	{
		return equivalentEdgeColor;
	}

	public void setEquivalentEdgeColor(Color val)
	{
		this.equivalentEdgeColor = val;
	}

	public Color getDisjointEdgeColor()
	{
		return disjointEdgeColor;
	}

	public void setDisjointEdgeColor(Color disjointEdgeColor)
	{
		this.disjointEdgeColor = disjointEdgeColor;
	}

	public Color getEquivalentPropertyEdgeColor()
	{
		return equivalentPropertyEdgeColor;
	}

	public void setEquivalentPropertyEdgeColor(Color val)
	{
		this.equivalentPropertyEdgeColor = val;
	}

	public Color getFunctionalEdgeColor()
	{
		return functionalEdgeColor;
	}

	public void setFunctionalEdgeColor(Color val)
	{
		this.functionalEdgeColor = val;
	}

	public Color getInverseOfEdgeColor()
	{
		return inverseOfEdgeColor;
	}

	public void setInverseOfEdgeColor(Color val)
	{
		this.inverseOfEdgeColor = val;
	}

	public Color getPropertyEdgeColor()
	{
		return propertyEdgeColor;
	}

	public void setPropertyEdgeColor(Color val)
	{
		this.propertyEdgeColor = val;
	}

	public Color getRangeEdgeColor()
	{
		return rangeEdgeColor;
	}

	public void setRangeEdgeColor(Color val)
	{
		this.rangeEdgeColor = val;
	}

	

	public Color getSubClassEdgeColor() {
		return subClassEdgeColor;
	}

	public void setSubClassEdgeColor(Color val) {
		this.subClassEdgeColor = val;
	}

	public Color getSubPropertyEdgeColor() {
		return subPropertyEdgeColor;
	}

	public void setSubPropertyEdgeColor(Color val) {
		this.subPropertyEdgeColor = val;
	}

	public Color getInverseOfMutualEdgeColor()
	{
		return inverseOfMutualEdgeColor;
	}

	public void setInverseOfMutualEdgeColor(Color val)
	{
		this.inverseOfMutualEdgeColor = val;
	}

	public Color getOperationEdgeColor()
	{
		return operationEdgeColor;
	}

	public void setOperationEdgeColor(Color val)
	{
		this.operationEdgeColor = val;
	}


}

