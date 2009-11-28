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

    protected Color cardinalityValueNodeColor;

    protected Color classNodeColor;

    protected Color dataTypeNodeColor;

    protected Color differentNodeColor;

    protected Color individualNodeColor;

    protected Color informationNodeColor;

    protected Color maxCardinalityValueNodeColor;

    protected Color minCardinalityValueNodeColor;

    protected Color nothingNodeColor;

    protected Color oneOfNodeColor;

    protected Color propertyNodeColor;

    protected Color sameAsNodeColor;

    protected Color someValuesFromNodeColor;

    protected Color thingNodeColor;

    public NodeColors () {
	    // kolory domyślne
	    classNodeColor = new Color(0, 182, 193);
	    thingNodeColor = Color.GREEN;
	    nothingNodeColor = Color.RED;
	    individualNodeColor = new Color(200, 194, 183);
	    differentNodeColor = new Color(209, 174, 114);
	    sameAsNodeColor = differentNodeColor;
	    propertyNodeColor = new Color(146, 61, 255);
	    someValuesFromNodeColor = new Color(203, 61, 255);
	    allValuesFromNodeColor = new Color(159, 152, 255);
	    dataTypeNodeColor = new Color(61, 255, 155);
	    anonymousClassNodeColor = Color.YELLOW;
	    cardinalityValueNodeColor = new Color(255, 171, 0);
	    minCardinalityValueNodeColor = new Color(162, 255, 0);
	    maxCardinalityValueNodeColor = new Color(255, 73, 0);
	    informationNodeColor = new Color(200, 113, 55);

	    //niewykorzystywane kolory - usunięte
	    /*transitivePropertyNodeColor = informationNodeColor;
	    functionalPropertyNodeColor = informationNodeColor;
	    inverseFunctionalPropertyColor = informationNodeColor;
	    symmetricPropertyNodeColor = informationNodeColor;

	    cardinalityNodeColor = anonymousClassNodeColor;
	    complementOfNodeColor = anonymousClassNodeColor;
	    unionOfNodeColor = anonymousClassNodeColor;
	    intersectionOfNodeColor = anonymousClassNodeColor;*/

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

    public Color getThingNodeColor () {
        return thingNodeColor;
    }

    public void setThingNodeColor (Color val) {
        this.thingNodeColor = val;
    }

}

