/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of SOVA.  SOVA is free software: you can
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

import java.awt.Color;

import org.pg.eti.kask.sova.utils.VisualizationProperties;

/**
 * Klasa przechowująca informacje o ustawieniach kolorów dla węzłów.
 *
 * @see org.pg.eti.kask.sova.nodes.Node
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
    protected Color objectPropertyNodeColor;
    protected Color dataPropertyNodeColor;
    protected Color sameAsNodeColor;
    protected Color someValuesFromNodeColor;
    protected Color thingNodeColor;

    public NodeColors() {
        setDefaultColors();
    }

    protected void setDefaultColors() {
        // kolory domyślne
//		classNodeColor = new Color(0, 182, 193);
//		thingNodeColor = Color.GREEN;
//		nothingNodeColor = Color.RED;
//		individualNodeColor = new Color(200, 194, 183);
//		differentNodeColor = new Color(209, 174, 114);
//		sameAsNodeColor = differentNodeColor;
//                objectPropertyNodeColor = new Color(146, 61, 255);
//                dataPropertyNodeColor = new Color(146,193,49);//new Color(107, 142, 35);
//		someValuesFromNodeColor = new Color(203, 61, 255);
//		allValuesFromNodeColor = new Color(159, 152, 255);
//		dataTypeNodeColor = new Color(61, 255, 155);
//		anonymousClassNodeColor = Color.YELLOW;
//		cardinalityValueNodeColor = new Color(255, 171, 0);
//		minCardinalityValueNodeColor = new Color(162, 255, 0);
//		maxCardinalityValueNodeColor = new Color(255, 73, 0);
//		informationNodeColor = new Color(200, 113, 55);

        classNodeColor = new Color(234,238,49);//new Color(204, 159, 42);
        thingNodeColor = Color.GREEN;
        nothingNodeColor = Color.RED;
        individualNodeColor = new Color(203, 61, 255);//new Color(83, 24, 82);
        differentNodeColor = new Color(226,190,128);//new Color(209, 174, 114);
        sameAsNodeColor = differentNodeColor;
        objectPropertyNodeColor = new Color(55,108,255);//new Color(48, 111, 162);
        dataPropertyNodeColor = new Color(146,193,49);//new Color(107, 142, 35);
        someValuesFromNodeColor = new Color(177,0,99);//new Color(203, 61, 255);
        allValuesFromNodeColor = new Color(159, 152, 255);
        dataTypeNodeColor = new Color(41, 167, 121);
        anonymousClassNodeColor = Color.ORANGE;// YELLOW;
        cardinalityValueNodeColor = new Color(255, 171, 0);
        minCardinalityValueNodeColor = new Color(162, 255, 0);
        maxCardinalityValueNodeColor = new Color(255, 73, 0);
        informationNodeColor = new Color(200, 113, 55);//new Color(0, 182, 193);

        //unused
		    /*transitivePropertyNodeColor = informationNodeColor;
         functionalPropertyNodeColor = informationNodeColor;
         inverseFunctionalPropertyColor = informationNodeColor;
         symmetricPropertyNodeColor = informationNodeColor;

         cardinalityNodeColor = anonymousClassNodeColor;
         complementOfNodeColor = anonymousClassNodeColor;
         unionOfNodeColor = anonymousClassNodeColor;
         intersectionOfNodeColor = anonymousClassNodeColor;*/
    }

    public NodeColors(VisualizationProperties colorProperties) {
        setDefaultColors();
        loadColors(colorProperties);
    }

    /**
     * Zmienia ustawienia kolorów na załadowane w VisualizationProperies.
     * Changes color setting to ones loaded in VisualizationProperies.
     */
    public void loadColors(VisualizationProperties colorProperties) {
        String prefix = "node.color.";

        classNodeColor = colorProperties.getPropertyColor(prefix + "classNodeColor", classNodeColor);
        thingNodeColor = colorProperties.getPropertyColor(prefix + "thingNodeColor", thingNodeColor);
        nothingNodeColor = colorProperties.getPropertyColor(prefix + "nothingNodeColor", nothingNodeColor);
        individualNodeColor = colorProperties.getPropertyColor(prefix + "individualNodeColor", individualNodeColor);
        differentNodeColor = colorProperties.getPropertyColor(prefix + "differentNodeColor", differentNodeColor);
        sameAsNodeColor = colorProperties.getPropertyColor(prefix + "sameAsNodeColor", sameAsNodeColor);
        objectPropertyNodeColor = colorProperties.getPropertyColor(prefix + "objectPropertyNodeColor", objectPropertyNodeColor);
        dataPropertyNodeColor = colorProperties.getPropertyColor(prefix + "dataPropertyNodeColor", dataPropertyNodeColor);
        someValuesFromNodeColor = colorProperties.getPropertyColor(prefix + "someValuesFromNodeColor", someValuesFromNodeColor);
        allValuesFromNodeColor = colorProperties.getPropertyColor(prefix + "allValuesFromNodeColor", allValuesFromNodeColor);
        dataTypeNodeColor = colorProperties.getPropertyColor(prefix + "dataTypeNodeColor", dataTypeNodeColor);
        anonymousClassNodeColor = colorProperties.getPropertyColor(prefix + "anonymousClassNodeColor", anonymousClassNodeColor);
        cardinalityValueNodeColor = colorProperties.getPropertyColor(prefix + "cardinalityValueNodeColor", cardinalityValueNodeColor);
        minCardinalityValueNodeColor = colorProperties.getPropertyColor(prefix + "minCardinalityValueNodeColor", minCardinalityValueNodeColor);
        maxCardinalityValueNodeColor = colorProperties.getPropertyColor(prefix + "maxCardinalityValueNodeColor", maxCardinalityValueNodeColor);
        informationNodeColor = colorProperties.getPropertyColor(prefix + "informationNodeColor", informationNodeColor);
    }

    public Color getAllValuesFromNodeColor() {
        return allValuesFromNodeColor;
    }

    public void setAllValuesFromNodeColor(Color val) {
        this.allValuesFromNodeColor = val;
    }

    public Color getAnonymousClassNodeColor() {
        return anonymousClassNodeColor;
    }

    public void setAnonymousClassNodeColor(Color val) {
        this.anonymousClassNodeColor = val;
    }

    public Color getCardinalityValueNodeColor() {
        return cardinalityValueNodeColor;
    }

    public void setCardinalityValueNodeColor(Color val) {
        this.cardinalityValueNodeColor = val;
    }

    public Color getClassNodeColor() {
        return classNodeColor;
    }

    public void setClassNodeColor(Color val) {
        this.classNodeColor = val;
    }

    public Color getDataTypeNodeColor() {
        return dataTypeNodeColor;
    }

    public void setDataTypeNodeColor(Color val) {
        this.dataTypeNodeColor = val;
    }

    public Color getDifferentNodeColor() {
        return differentNodeColor;
    }

    public void setDifferentNodeColor(Color val) {
        this.differentNodeColor = val;
    }

    public Color getIndividualNodeColor() {
        return individualNodeColor;
    }

    public void setIndividualNodeColor(Color val) {
        this.individualNodeColor = val;
    }

    public Color getInformationNodeColor() {
        return informationNodeColor;
    }

    public void setInformationNodeColor(Color val) {
        this.informationNodeColor = val;
    }

    public Color getMaxCardinalityValueNodeColor() {
        return maxCardinalityValueNodeColor;
    }

    public void setMaxCardinalityValueNodeColor(Color val) {
        this.maxCardinalityValueNodeColor = val;
    }

    public Color getMinCardinalityValueNodeColor() {
        return minCardinalityValueNodeColor;
    }

    public void setMinCardinalityValueNodeColor(Color val) {
        this.minCardinalityValueNodeColor = val;
    }

    public Color getNothingNodeColor() {
        return nothingNodeColor;
    }

    public void setNothingNodeColor(Color val) {
        this.nothingNodeColor = val;
    }

    public Color getOneOfNodeColor() {
        return oneOfNodeColor;
    }

    public void setOneOfNodeColor(Color val) {
        this.oneOfNodeColor = val;
    }

    public Color getObjectPropertyNodeColor() {
        return objectPropertyNodeColor;
    }

    public void setObjectPropertyNodeColor(Color val) {
        this.objectPropertyNodeColor = val;
    }

    public Color getDataPropertyNodeColor() {
        return dataPropertyNodeColor;
    }

    public void setDataPropertyNodeColor(Color val) {
        this.dataPropertyNodeColor = val;
    }

    public Color getSameAsNodeColor() {
        return sameAsNodeColor;
    }

    public void setSameAsNodeColor(Color val) {
        this.sameAsNodeColor = val;
    }

    public Color getSomeValuesFromNodeColor() {
        return someValuesFromNodeColor;
    }

    public void setSomeValuesFromNodeColor(Color val) {
        this.someValuesFromNodeColor = val;
    }

    public Color getThingNodeColor() {
        return thingNodeColor;
    }

    public void setThingNodeColor(Color val) {
        this.thingNodeColor = val;
    }
}
