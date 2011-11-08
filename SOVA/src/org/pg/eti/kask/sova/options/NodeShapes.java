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

package org.pg.eti.kask.sova.options;

/**
 * Klasa przechowująca informacje o kształtach poszczególnych węzłów.
 * @author infinity
 */
public class NodeShapes {

	protected NodeShapeType defaultNodeShapeType;
	protected NodeShapeType anonymousNodeShapeType;
	protected NodeShapeType individualNodeShapeType;
	protected NodeShapeType informationNodeShapeType;
	protected NodeShapeType cardinalityValueNodeShapeType;
	protected NodeShapeType thingNodeShapeType;
	protected NodeShapeType nothingNodeShapeType;

	public NodeShapes()
	{
		defaultNodeShapeType = NodeShapeType.ROUNDED_RECTANGLE;
		anonymousNodeShapeType = NodeShapeType.CIRCLE;
		individualNodeShapeType = NodeShapeType.RECTANGLE;
		informationNodeShapeType = NodeShapeType.CIRCLE;
		cardinalityValueNodeShapeType = NodeShapeType.ELLIPSE;
		thingNodeShapeType = NodeShapeType.CIRCLE;
		nothingNodeShapeType = NodeShapeType.CIRCLE;
	}

	public NodeShapeType getAnonymousNodeShapeType()
	{
		return anonymousNodeShapeType;
	}

	public void setAnonymousNodeShapeType(NodeShapeType anonymousNodeShapeType)
	{
		this.anonymousNodeShapeType = anonymousNodeShapeType;
	}

	public NodeShapeType getCardinalityValueNodeShapeType()
	{
		return cardinalityValueNodeShapeType;
	}

	public void setCardinalityValueNodeShapeType(NodeShapeType cardinalityValueNodeShapeType)
	{
		this.cardinalityValueNodeShapeType = cardinalityValueNodeShapeType;
	}

	public NodeShapeType getDefaultNodeShapeType()
	{
		return defaultNodeShapeType;
	}

	public void setDefaultNodeShapeType(NodeShapeType defaultNodeShapeType)
	{
		this.defaultNodeShapeType = defaultNodeShapeType;
	}

	public NodeShapeType getIndividualNodeShapeType()
	{
		return individualNodeShapeType;
	}

	public void setIndividualNodeShapeType(NodeShapeType individualNodeShapeType)
	{
		this.individualNodeShapeType = individualNodeShapeType;
	}

	public NodeShapeType getInformationNodeShapeType()
	{
		return informationNodeShapeType;
	}

	public void setInformationNodeShapeType(NodeShapeType informationNodeShapeType)
	{
		this.informationNodeShapeType = informationNodeShapeType;
	}

	public NodeShapeType getNothingNodeShapeType()
	{
		return nothingNodeShapeType;
	}

	public void setNothingNodeShapeType(NodeShapeType nothingNodeShapeType)
	{
		this.nothingNodeShapeType = nothingNodeShapeType;
	}

	public NodeShapeType getThingNodeShapeType()
	{
		return thingNodeShapeType;
	}

	public void setThingNodeShapeType(NodeShapeType thingNodeShapeType)
	{
		this.thingNodeShapeType = thingNodeShapeType;
	}

	

}
