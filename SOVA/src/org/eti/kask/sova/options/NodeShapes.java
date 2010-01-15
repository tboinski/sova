/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.eti.kask.sova.options;

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
