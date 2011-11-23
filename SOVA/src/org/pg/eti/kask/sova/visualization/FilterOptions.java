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

/**
 * Klasa przechowująca informacje o włączonych filtrach.
 * @author Piotr Kunowski
 *
 */
public class FilterOptions {

    private static boolean classFilter = true;
    private static boolean subClassEdge = true;
    private static boolean equivalentClassEdge = true;
    private static boolean disjointClassEdge = true;
    private static boolean unionOf = true;
    private static boolean intersectionOf = true;
    private static boolean complementOf = true;
    private static boolean cardinality = true;
    private static boolean individual = true;
    private static boolean instanceOfEdge = true;
    private static boolean different = true;
    private static boolean sameAs = true;
    private static boolean oneOf = true;
    private static boolean dataType = true;
    private static boolean property = true;
    private static boolean subPropertyEdge = true;
    private static boolean equivalentPropertyEdge = true;
    private static boolean functionalProperty = true;
    private static boolean inverseFunctionalProperty = true;
    private static boolean symmetricProperty = true;
    private static boolean transitiveProperty = true;
    private static boolean instanceProperty = true;
    private static boolean inverseOfProperty = true;
    private static boolean domain = true;
    private static boolean range = true;
    private static boolean anonymouse = true;
    private static boolean distanceFilter = true;
    private static boolean showIRI = false;
    private static int distance = 10;

    /** filtr wizualizacji klas i związków pomiedzy nimi.
     * Odpowiada również za klasy anonimowe.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#Class">W3C - opis OWL</a>
     */
    public static synchronized boolean isClassFilter() {
        return classFilter;
    }

    /** filtr wizualizacji klas i związków pomiedzy nimi.
     * Odpowiada również za klasy anonimowe.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#Class">W3C - opis OWL</a>
     */
    public static synchronized void setClassFilter(boolean classFilter) {
        FilterOptions.classFilter = classFilter;
    }

    /** filtr wizualizacji związku podklasy pomiędzy klasami
     * false - filtr wyłączony - element nie jest wizulizowany
     * true filtr włączony - element jest wizualizowany
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#subClassOf">W3C - opis OWL</a>
     */
    public static synchronized boolean isSubClassEdge() {
        return subClassEdge;
    }

    /** filtr wizualizacji związku podklasy pomiędzy klasami
     * false - filtr wyłączony - element nie jest wizulizowany
     * true filtr włączony - element jest wizualizowany
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#subClassOf">W3C - opis OWL</a>
     */
    public static synchronized void setSubClassEdge(boolean subClassEdge) {
        FilterOptions.subClassEdge = subClassEdge;
    }

    /** filtr wizualizacji związku equivalentClass
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     */
    public static synchronized boolean isEquivalentClassEdge() {
        return equivalentClassEdge;
    }

    /** filtr wizualizacji związku equivalentClass
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     */
    public static synchronized void setEquivalentClassEdge(
            boolean equivalentClassEdge) {
        FilterOptions.equivalentClassEdge = equivalentClassEdge;
    }

    /** filtr wizualizacji związku disjointClass
     * false - filtr wyłączony - element nie jest wizulizowany
     * true filtr włączony - element jest wizualizowany
     */
    public static synchronized boolean isDisjointClassEdge() {
        return disjointClassEdge;
    }

    /** filtr wizualizacji związku disjointClass
     * false - filtr wyłączony - element nie jest wizulizowany
     * true filtr włączony - element jest wizualizowany
     */
    public static synchronized void setDisjointClassEdge(boolean disjointClassEdge) {
        FilterOptions.disjointClassEdge = disjointClassEdge;
    }

    /** filtr wizualizacji związku unionOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#booleanFull">W3C - opis OWL</a>
     */
    public static synchronized boolean isUnionOf() {
        return unionOf;
    }

    /** filtr wizualizacji związku unionOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#booleanFull">W3C - opis OWL</a>
     */
    public static synchronized void setUnionOf(boolean unionOf) {
        FilterOptions.unionOf = unionOf;
    }

    /** filtr wizualizacji związku intersectionOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#booleanFull">W3C - opis OWL</a>
     */
    public static synchronized boolean isIntersectionOf() {
        return intersectionOf;
    }

    /** filtr wizualizacji związku intersectionOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#booleanFull">W3C - opis OWL</a>
     */
    public static synchronized void setIntersectionOf(boolean intersectionOf) {
        FilterOptions.intersectionOf = intersectionOf;
    }

    /** filtr wizualizacji związku cmplementOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C - opis OWL</a>
     */
    public static synchronized boolean isComplementOf() {
        return complementOf;
    }

    /** filtr wizualizacji związku cmplementOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C - opis OWL</a>
     */
    public static synchronized void setComplementOf(boolean complementOf) {
        FilterOptions.complementOf = complementOf;
    }

    /**
     * filtr wizualizacji związku cardinality. Odpowiada za wszystkie rodzaje
     * kardynalności (minCardinality, maxCardinality, cardybality)
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isCardinality() {
        return cardinality;
    }

    /**
     * filtr wizualizacji związku cardinality. Odpowiada za wszystkie rodzaje
     * kardynalności (minCardinality, maxCardinality, cardybality)
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setCardinality(boolean cardinality) {
        FilterOptions.cardinality = cardinality;
    }

    /**
     * filtr wizualizacji instancji klas i związków pomiędzy nimi
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isIndividual() {
        return individual;
    }

    /**
     * filtr wizualizacji instancji klas i związków pomiędzy nimi
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setIndividual(boolean individual) {
        FilterOptions.individual = individual;
    }

    /**
     * filtr wizualizacji związku instanceOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isInstanceOfEdge() {
        return instanceOfEdge;
    }

    /**
     * filtr wizualizacji związku instanceOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setInstanceOfEdge(boolean instanceOfEdge) {
        FilterOptions.instanceOfEdge = instanceOfEdge;
    }

    /**
     * filtr wizualizacji związku differentFrom / allDifferent
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isDifferent() {
        return different;
    }

    /**
     * filtr wizualizacji związku differentFrom / allDifferent
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setDifferent(boolean different) {
        FilterOptions.different = different;
    }

    /**
     * filtr wizualizacji związku sameAs
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isSameAs() {
        return sameAs;
    }

    /**
     * filtr wizualizacji związku sameAs
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setSameAs(boolean sameAs) {
        FilterOptions.sameAs = sameAs;
    }

    /**
     * filtr wizualizacji związku oneOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isOneOf() {
        return oneOf;
    }

    /**
     * filtr wizualizacji związku oneOf
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setOneOf(boolean oneOf) {
        FilterOptions.oneOf = oneOf;
    }

    /**
     * filtr wizualizacji dataType i ich związków z innymi elementami.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isDataType() {
        return dataType;
    }

    /**
     * filtr wizualizacji dataType i ich związków z innymi elementami.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setDataType(boolean dataType) {
        FilterOptions.dataType = dataType;
    }

    /**
     * filtr wizualizacji Property i ich związków z innymi elementami.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#property">W3C  - opis OWL</a>
     */
    public static synchronized boolean isProperty() {
        return property;
    }

    /**
     * filtr wizualizacji Property i ich związków z innymi elementami.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#property">W3C  - opis OWL</a>
     */
    public static synchronized void setProperty(boolean property) {
        FilterOptions.property = property;
    }

    /**
     * filtr wizualizacji związku (krawędzi) SubPropertyEdge.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isSubPropertyEdge() {
        return subPropertyEdge;
    }

    /**
     * filtr wizualizacji związku (krawędzi) SubPropertyEdge.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setSubPropertyEdge(boolean subPropertyEdge) {
        FilterOptions.subPropertyEdge = subPropertyEdge;
    }

    /**
     * filtr wizualizacji związku (krawędzi) EquivalentProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isEquivalentPropertyEdge() {
        return equivalentPropertyEdge;
    }

    /**
     * filtr wizualizacji związku (krawędzi) EquivalentProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setEquivalentPropertyEdge(
            boolean equivalentPropertyEdge) {
        FilterOptions.equivalentPropertyEdge = equivalentPropertyEdge;
    }

    /**
     * filtr wizualizacji FunctionalProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isFunctionalProperty() {
        return functionalProperty;
    }

    /**
     * filtr wizualizacji FunctionalProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setFunctionalProperty(boolean functionalProperty) {
        FilterOptions.functionalProperty = functionalProperty;
    }

    /**
     * filtr wizualizacji InverseFunctionalProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isInverseFunctionalProperty() {
        return inverseFunctionalProperty;
    }

    /**
     * filtr wizualizacji InverseFunctionalProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setInverseFunctionalProperty(
            boolean inverseFunctionalProperty) {
        FilterOptions.inverseFunctionalProperty = inverseFunctionalProperty;
    }

    /**
     * filtr wizualizacji SymmetricProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isSymmetricProperty() {
        return symmetricProperty;
    }

    /**
     * filtr wizualizacji SymmetricProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setSymmetricProperty(boolean symmetricProperty) {
        FilterOptions.symmetricProperty = symmetricProperty;
    }

    /**
     * filtr wizualizacji TransitiveProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isTransitiveProperty() {
        return transitiveProperty;
    }

    /**
     * filtr wizualizacji TransitiveProperty.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setTransitiveProperty(boolean transitiveProperty) {
        FilterOptions.transitiveProperty = transitiveProperty;
    }

    /**
     * filtr wizualizacji krawędzi łączącej wystąpienie prorerty (sameValueFrom,
     * allValueFrom) z wystąpieniem Property.
     * <ul>
     * <li><b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li><b>true</b> - filtr włączony - element jest wizualizowany
     * </ul>
     *
     */
    public static synchronized boolean isInstanceProperty() {
        return instanceProperty;
    }

    /**
     * filtr wizualizacji krawędzi łączącej wystąpienie prorerty (sameValueFrom,
     * allValueFrom) z wystąpieniem Property.
     * <ul>
     * <li><b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li><b>true</b> - filtr włączony - element jest wizualizowany
     * </ul>
     *
     */
    public static synchronized void setInstanceProperty(boolean instanceProperty) {
        FilterOptions.instanceProperty = instanceProperty;
    }

    /**
     * filtr wizualizacji krawędzi inverseOf łączącej property
     * <ul>
     * <li><b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li><b>true</b> - filtr włączony - element jest wizualizowany
     * </ul>
     *
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C -  opis OWL</a>
     */
    public static synchronized boolean isInverseOfProperty() {
        return inverseOfProperty;
    }

    /**
     * filtr wizualizacji krawędzi inverseOf łączącej property
     * <ul>
     * <li><b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li><b>true</b> - filtr włączony - element jest wizualizowany
     * </ul>
     *
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C -  opis OWL</a>
     */
    public static synchronized void setInverseOfProperty(boolean inverseOfProperty) {
        FilterOptions.inverseOfProperty = inverseOfProperty;
    }

    /**
     * filtr wizualizacji związku Domain.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isDomain() {
        return domain;
    }

    /**
     * filtr wizualizacji związku Domain.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setDomain(boolean domain) {
        FilterOptions.domain = domain;
    }

    /**
     * filtr wizualizacji związku Range.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized boolean isRange() {
        return range;
    }

    /**
     * filtr wizualizacji związku Range.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
     */
    public static synchronized void setRange(boolean range) {
        FilterOptions.range = range;
    }

    /**
     * Filtr długości scieżki od zaznaczonego wierzchołka.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     */
    public static synchronized boolean isDistanceFilter() {
        return distanceFilter;
    }

    /**
     * Filtr długości scieżki od zaznaczonego wierzchołka.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     */
    public static synchronized void setDistanceFilter(boolean distanceFilter) {
        FilterOptions.distanceFilter = distanceFilter;
    }

    /**
     *
     * @return wartości filtra odległościowego
     */
    public static synchronized int getDistance() {
        return distance;
    }

    /**
     *
     * @param distance wartości filtra odległościowego
     */
    public static synchronized void setDistance(int distance) {
        FilterOptions.distance = distance;
    }

    /**
     * filtr wizualizacji klasy anonimowe oznaczone symbolem A.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     */
    public static synchronized boolean isAnonymouse() {
        return anonymouse;
    }

    /**
     * filtr wizualizacji klasy anonimowe oznaczone symbolem A.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany
     * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
     * </ul>
     */
    public static synchronized void setAnonymouse(boolean anonymouse) {
        FilterOptions.anonymouse = anonymouse;
    }

   /**
     * filtr wizualizacji pełnego IRI w etykiecie węzła.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - IRI nie jest wizualizowane
     * <li> <b>true</b> -  filtr włączony - IRI jest wizualizowane
     * </ul>
     */
    public static synchronized boolean isShowIRI() {
        return showIRI;
    }

   /**
     * filtr wizualizacji pełnego IRI w etykiecie węzła.
     * <ul>
     * <li> <b>false</b> - filtr wyłączony - IRI nie jest wizualizowane
     * <li> <b>true</b> -  filtr włączony - IRI jest wizualizowane
     * </ul>
     */
    public static synchronized void setShowIRI(boolean showURI) {
        FilterOptions.showIRI = showURI;
    }
}
