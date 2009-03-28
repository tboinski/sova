package org.pg.eti.kask.ont.editor.consts;

import java.awt.Color;
import prefuse.util.ColorLib;

public final class Constants
{
	//stale zwiazane z lokalizacja
	public static final String LANGUAGE_ENGLISH = "en";
	public static final String LANGUAGE_POLISH = "pl";
	public static final String DEFAULT_LANGUAGE = LANGUAGE_POLISH;
	
	public static final String RDFS_COMMENT = "rdfs:comment";
	public static final String RDFS_LABEL = "rdfs:label";
	
	public static final String OWL_ONTOLOGY_FORMAT = "owl";
	public static final String RDF_ONTOLOGY_FORMAT = "rdf";
	
	public static final String XSD_INTEGER = "xsd:integer";
	public static final String XSD_BOOLEAN = "xsd:boolean";
	public static final String XSD_DATE = "xsd:date";
	public static final String XSD_DATETIME = "xsd:dateTime";
	public static final String XSD_TIME = "xsd:time";
	public static final String XSD_FLOAT = "xsd:float";
	public static final String XSD_DOUBLE = "xsd:double";
	public static final String XSD_STRING = "xsd:string";
	
	public static final String PROJECT_FILE_NAME="project.xml";
	
    public static final int NODE_COLOR_CLASS = ColorLib.rgb(253, 211, 100);
    public static final int NODE_COLOR_INDIVIDUAL = ColorLib.rgb(220, 70, 217);
    public static final int NODE_COLOR_SELECTED = ColorLib.rgb(255, 100, 100);
    public static final int NODE_COLOR_HIGHLIGHTED = ColorLib.rgb(144, 253, 126);
    public static final int NODE_COLOR_SEARCH = ColorLib.rgb(255,190,190);
    public static final int NODE_DEFAULT_COLOR = ColorLib.rgb(200, 200, 255);
    
    public static final String GRAPH = "graph";
    public static final String GRAPH_NODES = "graph.nodes";
    public static final String GRAPH_EDGES = "graph.edges";
    
    public static final String GRAPH_NODE_URI_COLUMN_NAME = "URI";
    public static final String GRAPH_NODE_NAME_COLUMN_NAME = "name";
    public static final String GRAPH_NODE_TYPE_COLUMN_NAME = "type";
    public static final String GRAPH_NODE_CLASS_TYPE = "class";
    
    public static final String GRAPH_EDGE_LABEL_COLUMN = "label";
    public static final String GRAPH_EDGE_TYPE_COLUMN = "type";
    public static final String GRAPH_EDGE_SUBCLASS_TYPE = "subClass";
    public static final String GRAPH_EDGE_DISJOINT_TYPE = "disJoint";
    public static final String GRAPH_EDGE_EQUIVALENCE_TYPE = "equivalence";
        
    public static final String TREE = "tree";
    public static final String TREE_NODES = "tree.nodes";
    public static final String TREE_EDGES = "tree.edges";
    
    public static final String TREE_NODE_CLASS_TYPE = "class";
    public static final String TREE_NODE_INDIVIDUAL_TYPE = "individual";
    
    public static final String TREE_NODE_URI_COLUMN = "URI";
    public static final String TREE_NODE_NAME_COLUMN = "name";
    public static final String TREE_NODE_TYPE_COLUMN = "type";
    
    public static final String EDGE_DECORATORS = "edgeDecorators";
    
    public static final Color VIS_COLOR_BACKGROUND = Color.WHITE;
    public static final Color VIS_COLOR_FOREGROUND = Color.BLACK;
    public static final Color VIS_COLOR_BORDER = Color.LIGHT_GRAY;
}