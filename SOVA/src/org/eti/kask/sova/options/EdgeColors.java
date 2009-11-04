package org.eti.kask.sova.options;


import java.awt.Color;

/**
 * Klasa przechowująca informacje o ustawieniach kolorów dla krawędzi.
 * @see org.eti.kask.sova.edges.Edge
 */
public class EdgeColors {

    protected Color rangeEdgeColor;

    protected Color domainEdgeColor;
 
    protected Color edgeColor;
 
    protected Color equivalentEdgeColor;

    protected Color equivalentPropertyEdgeColor;

    protected Color functionalEdgeColor;

    protected Color inverseOfEdgeColor;
 
    protected Color propertyEdgeColor;

    protected Color subEdgeColor;
 
    public EdgeColors () {
	    // Ustawienia domyślne
	    edgeColor = Color.BLACK;
    }

    public Color getDomainEdgeColor () {
        return domainEdgeColor;
    }

    public void setDomainEdgeColor (Color val) {
        this.domainEdgeColor = val;
    }

    public Color getEdgeColor () {
        return edgeColor;
    }

    public void setEdgeColor (Color val) {
        this.edgeColor = val;
    }

    public Color getEquivalentEdgeColor () {
        return equivalentEdgeColor;
    }

    public void setEquivalentEdgeColor (Color val) {
        this.equivalentEdgeColor = val;
    }

    public Color getEquivalentPropertyEdgeColor () {
        return equivalentPropertyEdgeColor;
    }

    public void setEquivalentPropertyEdgeColor (Color val) {
        this.equivalentPropertyEdgeColor = val;
    }

    public Color getFunctionalEdgeColor () {
        return functionalEdgeColor;
    }

    public void setFunctionalEdgeColor (Color val) {
        this.functionalEdgeColor = val;
    }

    public Color getInverseOfEdgeColor () {
        return inverseOfEdgeColor;
    }

    public void setInverseOfEdgeColor (Color val) {
        this.inverseOfEdgeColor = val;
    }

    public Color getPropertyEdgeColor () {
        return propertyEdgeColor;
    }

    public void setPropertyEdgeColor (Color val) {
        this.propertyEdgeColor = val;
    }

    public Color getRangeEdgeColor () {
        return rangeEdgeColor;
    }

    public void setRangeEdgeColor (Color val) {
        this.rangeEdgeColor = val;
    }

    public Color getSubEdgeColor () {
        return subEdgeColor;
    }

    public void setSubEdgeColor (Color val) {
        this.subEdgeColor = val;
    }

}

