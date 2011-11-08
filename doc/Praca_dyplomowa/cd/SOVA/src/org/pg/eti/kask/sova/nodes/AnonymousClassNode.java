package org.pg.eti.kask.sova.nodes;

import java.awt.Color;

import org.pg.eti.kask.sova.options.NodeColors;
import org.pg.eti.kask.sova.options.NodeShapeType;
import org.pg.eti.kask.sova.options.NodeShapes;

/**
 * Klasa reprezentuje węzeł klasy anonimowej.
 */
public class AnonymousClassNode extends Node
{

        //identyfikator anonimowego komponentu
        private int anonumousId;


	public AnonymousClassNode()
	{
		super();
		label = "A";
        anonumousId=0;
	}

	/**
	 * @param colorScheme źródłowy schemat kolorów
	 * @return kolor wypełnienia węzła z zadanego schematu
	 */
	@Override
	public Color getFillColorFromScheme(NodeColors colorScheme)
	{
		return colorScheme.getAnonymousClassNodeColor();
	}

	/**
	 * Klasy anonimowe nie posiadają konwencjonalnych etykiet.
	 * Poniższa funkcja nie robi nic.
	 */
	@Override
	public void setLabel(String label)
	{
	}

	/**
	 * @param shapeScheme źródłowy schemat kształtów dla węzłów
	 * @return rodzaj kształtu węzła z zadanego schematu
	 */
	@Override
	public NodeShapeType getNodeShapeType(NodeShapes shapeScheme)
	{
		return shapeScheme.getAnonymousNodeShapeType();
	}


}

