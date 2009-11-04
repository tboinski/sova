package org.eti.kask.sova.options;

import java.awt.Color;
import prefuse.util.ColorLib;

/**
 * Klasa przechowująca informacje o ustawieniach kolorów dla węzłów.
 * @see org.eti.kask.sova.nodes.Node
 */
public class NodeColors {

    protected Color allValuesFromNodeColor;

    protected Color anonymousClassNodeColor;

    protected Color cardinalityNodeColor;

    protected Color cardinalityValueNodeColor;

    protected Color classNodeColor;

    protected Color complementOfNodeColor;

    protected Color dataTypeNodeColor;

    protected Color differentNodeColor;

    protected Color functionalPropertyNodeColor;

    protected Color individualNodeColor;

    protected Color informationNodeColor;

    protected Color intersectionOfNodeColor;

    protected Color inverseFunctionalPropertyColor;

    protected Color maxCardinalityValueNodeColor;

    protected Color minCardinalityValueNodeColor;

    protected Color nothingNodeColor;

    protected Color oneOfNodeColor;

    protected Color propertyNodeColor;

    protected Color sameAsNodeColor;

    protected Color someValuesFromNodeColor;

    protected Color symmetricPropertyNodeColor;

    protected Color thingNodeColor;

    protected Color transitivePropertyNodeColor;

    protected Color unionOfNodeColor;

    public NodeColors () {
	    // kolory domyślne
	    classNodeColor = new Color(0, 182, 193);
	    thingNodeColor = Color.GREEN;
	    individualNodeColor = new Color(200, 194, 183);
	    differentNodeColor = new Color(209, 174, 114);
    }

    public Color getAllValuesFromNodeColor () {
        return allValuesFromNodeColor;
    }

    public void setAllValuesFromNodeColor (Color val) {
        this.allValuesFromNodeColor = val;
    }

    public Color getAnonymousClassNodeColor () {
        return anonymousClassNodeColor;
    }

    public void setAnonymousClassNodeColor (Color val) {
        this.anonymousClassNodeColor = val;
    }

    public Color getCardinalityNodeColor () {
        return cardinalityNodeColor;
    }

    public void setCardinalityNodeColor (Color val) {
        this.cardinalityNodeColor = val;
    }

    public Color getCardinalityValueNodeColor () {
        return cardinalityValueNodeColor;
    }

    public void setCardinalityValueNodeColor (Color val) {
        this.cardinalityValueNodeColor = val;
    }

    public Color getClassNodeColor () {
        return classNodeColor;
    }

    public void setClassNodeColor (Color val) {
        this.classNodeColor = val;
    }

    public Color getComplementOfNodeColor () {
        return complementOfNodeColor;
    }

    public void setComplementOfNodeColor (Color val) {
        this.complementOfNodeColor = val;
    }

    public Color getDataTypeNodeColor () {
        return dataTypeNodeColor;
    }

    public void setDataTypeNodeColor (Color val) {
        this.dataTypeNodeColor = val;
    }

    public Color getDifferentNodeColor () {
        return differentNodeColor;
    }

    public void setDifferentNodeColor (Color val) {
        this.differentNodeColor = val;
    }

    public Color getFunctionalPropertyNodeColor () {
        return functionalPropertyNodeColor;
    }

    public void setFunctionalPropertyNodeColor (Color val) {
        this.functionalPropertyNodeColor = val;
    }

    public Color getIndividualNodeColor () {
        return individualNodeColor;
    }

    public void setIndividualNodeColor (Color val) {
        this.individualNodeColor = val;
    }

    public Color getInformationNodeColor () {
        return informationNodeColor;
    }

    public void setInformationNodeColor (Color val) {
        this.informationNodeColor = val;
    }

    public Color getIntersectionOfNodeColor () {
        return intersectionOfNodeColor;
    }

    public void setIntersectionOfNodeColor (Color val) {
        this.intersectionOfNodeColor = val;
    }

    public Color getInverseFunctionalPropertyColor () {
        return inverseFunctionalPropertyColor;
    }

    public void setInverseFunctionalPropertyColor (Color val) {
        this.inverseFunctionalPropertyColor = val;
    }

    public Color getMaxCardinalityValueNodeColor () {
        return maxCardinalityValueNodeColor;
    }

    public void setMaxCardinalityValueNodeColor (Color val) {
        this.maxCardinalityValueNodeColor = val;
    }

    public Color getMinCardinalityValueNodeColor () {
        return minCardinalityValueNodeColor;
    }

    public void setMinCardinalityValueNodeColor (Color val) {
        this.minCardinalityValueNodeColor = val;
    }

    public Color getNothingNodeColor () {
        return nothingNodeColor;
    }

    public void setNothingNodeColor (Color val) {
        this.nothingNodeColor = val;
    }

    public Color getOneOfNodeColor () {
        return oneOfNodeColor;
    }

    public void setOneOfNodeColor (Color val) {
        this.oneOfNodeColor = val;
    }

    public Color getPropertyNodeColor () {
        return propertyNodeColor;
    }

    public void setPropertyNodeColor (Color val) {
        this.propertyNodeColor = val;
    }

    public Color getSameAsNodeColor () {
        return sameAsNodeColor;
    }

    public void setSameAsNodeColor (Color val) {
        this.sameAsNodeColor = val;
    }

    public Color getSomeValuesFromNodeColor () {
        return someValuesFromNodeColor;
    }

    public void setSomeValuesFromNodeColor (Color val) {
        this.someValuesFromNodeColor = val;
    }

    public Color getSymmetricPropertyNodeColor () {
        return symmetricPropertyNodeColor;
    }

    public void setSymmetricPropertyNodeColor (Color val) {
        this.symmetricPropertyNodeColor = val;
    }

    public Color getThingNodeColor () {
        return thingNodeColor;
    }

    public void setThingNodeColor (Color val) {
        this.thingNodeColor = val;
    }

    public Color getTransitivePropertyNodeColor () {
        return transitivePropertyNodeColor;
    }

    public void setTransitivePropertyNodeColor (Color val) {
        this.transitivePropertyNodeColor = val;
    }

    public Color getUnionOfNodeColor () {
        return unionOfNodeColor;
    }

    public void setUnionOfNodeColor (Color val) {
        this.unionOfNodeColor = val;
    }

}

