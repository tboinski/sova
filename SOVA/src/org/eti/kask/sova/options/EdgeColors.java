package org.eti.kask.sova.options;

import java.awt.Color;
import org.eti.kask.sova.utils.VisualizationProperties;

/**
 * Klasa przechowująca informacje o ustawieniach kolorów dla krawędzi.
 * @see org.eti.kask.sova.edges.Edge
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
	protected Color subEdgeColor;
	protected Color inverseOfMutualEdgeColor;
	protected Color operationEdgeColor;

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
		disjointEdgeColor = colorProperties.getPropertyColor(prefix + ".disjointEdgeColor", disjointEdgeColor);
		equivalentEdgeColor = colorProperties.getPropertyColor(prefix + "equivalentEdgeColor", equivalentEdgeColor);
		equivalentPropertyEdgeColor = colorProperties.getPropertyColor(prefix + "equivalentPropertyEdgeColor", equivalentPropertyEdgeColor);
		functionalEdgeColor = colorProperties.getPropertyColor(prefix + "functionalEdgeColor", functionalEdgeColor);
		inverseOfEdgeColor = colorProperties.getPropertyColor(prefix + "inverseOfEdgeColor", inverseOfEdgeColor);
		inverseOfMutualEdgeColor = colorProperties.getPropertyColor(prefix + "inverseOfMutualEdgeColor", inverseOfEdgeColor);
		operationEdgeColor = colorProperties.getPropertyColor(prefix + "operationEdgeColor", operationEdgeColor);
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
	}

	public Color getDomainEdgeColor()
	{
		return domainEdgeColor;
	}

	public void setDomainEdgeColor(Color val)
	{
		this.domainEdgeColor = val;
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

	public Color getSubEdgeColor()
	{
		return subEdgeColor;
	}

	public void setSubEdgeColor(Color val)
	{
		this.subEdgeColor = val;
	}

	public Color getInverseOfMutualEdgeColor()
	{
		return inverseOfMutualEdgeColor;
	}

	public void setInverseOfMutualEdgeColor(Color inverseOfMutualEdgeColor)
	{
		this.inverseOfMutualEdgeColor = inverseOfMutualEdgeColor;
	}

	public Color getOperationEdgeColor()
	{
		return operationEdgeColor;
	}

	public void setOperationEdgeColor(Color operationEdgeColor)
	{
		this.operationEdgeColor = operationEdgeColor;
	}


}

