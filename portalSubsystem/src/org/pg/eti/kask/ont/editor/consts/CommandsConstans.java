package org.pg.eti.kask.ont.editor.consts;

public interface CommandsConstans {

	//Action commands uzywane w menu 
	public static final String EXIT_COMMAND = "exit";
	public static final String CREATE_ONTOLOGY_COMMAND = "createOntology";
	public static final String CREATE_PROJECT_COMMAND = "createProject";
	public static final String OPEN_PROJECT_COMMAND = "openProject";
	public static final String SAVE_PROJECT_COMMAND = "saveProject";
	public static final String EXPORT_TO_OWL_COMMAND = "exportToOWL";
	public static final String EXPORT_TO_RDF_COMMAND = "exportToRDF";
	public static final String IMPORT_ONTOLOGY_COMMAND = "importOntology";
	public static final String EXPORT_ONTOLOGY_CHANGES_COMMAND = "exportOntologyChanges";
	public static final String UPDATE_ONTOLOGY_COMMAND = "updateOntology";	
	public static final String CREATE_NEW_VERSION_COMMAND = "createNewVersion";
	public static final String LOCK_ONTOLOGY_COMMAND = "lockOntology";
	public static final String SHOW_CHANGES_COMMAND = "showChanges";
	public static final String SET_POLISH_COMMAND = "setPolishLanguage";
	public static final String SET_ENGLISH_COMMAND = "setEnglishLanguage";
	public static final String SHOW_INFERRED_HIERARCHY = "showInferredHierarchy";
	public static final String INFO_COMMAND = "info";
	public static final String INDIVIDUALS_ENUM_COMMAND = "individualsEnumCommand";

	//Action commands uzywane w wizualizacji ontologii
	public static final String SHOW_SUBCLASSES_COMMAND ="showSubclasses";
	public static final String SHOW_DISJOINT_COMMAND ="showDisjointClasses";
	public static final String SHOW_EQUIVALENT_COMMAND ="showEquivalentClasses";

	//Action commands uzywane w individuals popup menu
	public static final String SHOW_TYPES ="showTypes";
	public static final String NEW_INDIVIDUAL ="newIndividual";
	public static final String DELETE_INDIVIDUAL ="deleteIndividual";
	public static final String SAME_INDIVIDUALS ="sameIndividuals";
	public static final String DIFFERENT_INDIVIDUALS ="differentIndividuals";

	//Action commands uzywane w classes popup menu
	public static final String OPEN_PROPERTY_EDITOR ="openPropertyEditor";
	public static final String CREATE_SUBCLASS="createSubClass";
	public static final String CREATE_CLASS="createClass";
	public static final String DELETE_CLASS="deleteClass";
	public static final String FOCUS_ON_GRAPH="focusOnGraph";
	public static final String SHOW_AXIOMS="showAxioms";
	
	//Action commands uzywane w properties popup menu
	public static final String DELETE_PROPERTY="deleteProperty";
	
	//common Action commands
	public static final String OK_BUTTON="okCommand";
	public static final String CANCEL_BUTTON="cancelCommand";
	public static final String BROWSE_CLASSES_BUTTON = "browseClasses";
	public static final String REMOVE_CLASS_BUTTON = "removeClass";
	
}
