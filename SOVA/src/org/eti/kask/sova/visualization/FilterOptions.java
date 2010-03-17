package org.eti.kask.sova.visualization;
/**
 * Klasa przechowująca informacje o włączonych filtrach.
 * @author Piotr Kunowski
 *
 */
public class FilterOptions {
	
	/** filtr wizualizacji klas i związków pomiedzy nimi. 
	 * Odpowiada również za klasy anonimowe.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#Class">W3C - opis OWL</a>
	 */
	public static boolean  classFilter = true;
	
	/** filtr wizualizacji związku podklasy pomiędzy klasami
	 * false - filtr wyłączony - element nie jest wizulizowany
	 * true filtr włączony - element jest wizualizowany
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#subClassOf">W3C - opis OWL</a>
	 */	
	public static boolean subClassEdge = true;
	
	/** filtr wizualizacji związku equivalentClass
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 */	
	public static boolean equivalentClassEdge = true;
	
	/** filtr wizualizacji związku disjointClass
	 * false - filtr wyłączony - element nie jest wizulizowany
	 * true filtr włączony - element jest wizualizowany
	 */	
	public static boolean disjointClassEdge = true;
	
	/** filtr wizualizacji związku unionOf
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#booleanFull">W3C - opis OWL</a>
	 */	
	public static boolean unionOf = true;
	
	/** filtr wizualizacji związku intersectionOf
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#booleanFull">W3C - opis OWL</a>
	 */	
	public static boolean intersectionOf = true;
	
	/** filtr wizualizacji związku cmplementOf
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C - opis OWL</a>
	 */	
	public static boolean complementOf = true;
	
	/**
	 * filtr wizualizacji związku cardinality. Odpowiada za wszystkie rodzaje
	 * kardynalności (minCardinality, maxCardinality, cardybality) 
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean cardinality = true;
	
	/**
	 * filtr wizualizacji instancji klas i związków pomiędzy nimi 
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean individual = true;
	
	/**
	 * filtr wizualizacji związku instanceOf
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean instanceOfEdge = true;
	
	/**
	 * filtr wizualizacji związku differentFrom / allDifferent
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean different = true;
	
	/**
	 * filtr wizualizacji związku sameAs
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean sameAs = true;	
	
	
	/**
	 * filtr wizualizacji związku oneOf
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean oneOf = true;	
	
	/**
	 * filtr wizualizacji dataType i ich związków z innymi elementami.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean dataType = true;		
	
	/**
	 * filtr wizualizacji Property i ich związków z innymi elementami.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#property">W3C  - opis OWL</a>
	 */	
	public static boolean property = true;	
	
	/**
	 * filtr wizualizacji związku (krawędzi) SubPropertyEdge.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean subPropertyEdge = true;	
	
	/**
	 * filtr wizualizacji związku (krawędzi) EquivalentProperty.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean equivalentPropertyEdge = true;
	
	/**
	 * filtr wizualizacji FunctionalProperty.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean functionalProperty = true;
	/**
	 * filtr wizualizacji InverseFunctionalProperty.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */
	public static boolean inverseFunctionalProperty = true;
	
	/**
	 * filtr wizualizacji SymmetricProperty.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean symmetricProperty = true;
	
	/**
	 * filtr wizualizacji TransitiveProperty.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean transitiveProperty = true;

	/**
	 * filtr wizualizacji krawędzi łączącej wystąpienie prorerty (sameValueFrom,
	 * allValueFrom) z wystąpieniem Property.
	 * <ul>
	 * <li><b>false</b> - filtr wyłączony - element nie jest wizualizowany
	 * <li><b>true</b> - filtr włączony - element jest wizualizowany
	 * </ul>
	 * 
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C - opis OWL</a>
	 */	
	public static boolean instanceProperty = true;
	
	/**
	 * filtr wizualizacji krawędzi inverseOf łączącej property
	 * <ul>
	 * <li><b>false</b> - filtr wyłączony - element nie jest wizualizowany
	 * <li><b>true</b> - filtr włączony - element jest wizualizowany
	 * </ul>
	 * 
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C -  opis OWL</a>
	 */	
	public static boolean inverseOfProperty = true;
	
	/**
	 * filtr wizualizacji związku Domain.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean domain = true;
	
	/**
	 * filtr wizualizacji związku Range.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 * @see <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210">W3C  - opis OWL</a>
	 */	
	public static boolean range = true;	
	
	/**
	 * Filtr długości scieżki od zaznaczonego wierzchołka.
	 * <ul>
	 * <li> <b>false</b> - filtr wyłączony - element nie jest wizualizowany 
	 * <li> <b>true</b> -  filtr włączony - element jest wizualizowany
	 * </ul>
	 */
	public static boolean distanceFilter = true;
	
	/**
	 * wartości filtra odległościowego. 
	 */
	public static int distance = 10;
	
	
	
}
