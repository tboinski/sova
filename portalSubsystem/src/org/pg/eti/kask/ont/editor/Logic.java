package org.pg.eti.kask.ont.editor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.DBOntology;
import org.pg.eti.kask.ont.common.DBProposition;
import org.pg.eti.kask.ont.common.OWLPropositionData;
import org.pg.eti.kask.ont.common.OntologyURI;
import org.pg.eti.kask.ont.common.PropositionDescription;
import org.pg.eti.kask.ont.common.User;
import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.consts.Constants;
import org.pg.eti.kask.ont.editor.dialogs.LoginDialog;
import org.pg.eti.kask.ont.editor.table.model.OntologyProposition;
import org.pg.eti.kask.ont.editor.util.ApplicationConfiguration;
import org.pg.eti.kask.ont.editor.util.ProjectDescriptor;
import org.pg.eti.kask.ont.lib.PGOWLOntologyManager;
import org.pg.eti.kask.ont.lib.PGOWLOntologyManagerImpl;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.OWLXMLOntologyFormat;
import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.AxiomType;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomChange;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLClassAxiom;
import org.semanticweb.owl.model.OWLCommentAnnotation;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLLabelAnnotation;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyStorageException;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.RemoveAxiom;
import org.semanticweb.owl.model.UnknownOWLOntologyException;
import org.semanticweb.owl.util.OWLEntityRemover;


/**
 * Klasa realizujaca wzorzec singletona. Jest to klasa narzedziowa
 * wykorzystana do przeprowadzania wszelkich operacji na ontologii. 
 * 
 * @author Andrzej Jakowski
 */
public class Logic {
	
	private static Logger logger = Logger.getLogger(Logic.class.getCanonicalName());
	
	//instancja tej klasy bedaca singletonem
	private static Logic instance = null;
	
	private OWLDataFactory dataFactory;
	private PGOWLOntologyManager ontologyManager;
	private Set<String> usedClasses;
	
	private User loggedInUser;
	private ApplicationConfiguration config;
	private ProjectDescriptor descriptor;
	private String loadedOntologyURI;
	private boolean changed;

	
	private Logic() {
		try {			
			URL url = new URL(ApplicationConfiguration.OCS_SERVER_URL);			
			this.ontologyManager = new PGOWLOntologyManagerImpl(OWLManager.createOWLOntologyManager().getOWLDataFactory(), url);
		} catch (MalformedURLException e) {		
			logger.severe("Error in OCS Server URL - please check ApplicationConfiguration.");
		}
		this.dataFactory = ontologyManager.getOWLDataFactory();
		this.loggedInUser = new User();		
		this.config = new ApplicationConfiguration();
		this.changed = false;
	}
	
		
	/**
	 * Metoda pobierajaca instacje tej klasy, ktora jest singletonem. Jesli juz istnieje
	 * obiekt tej klasy, wtedy jest on zwracany, w przeciwnym przypadku tworzona jest nowa
	 * instancja tej klasy. 
	 * 
	 * @return instancja tej klasy
	 */
	public static Logic getInstance() {
		if(instance != null) {
			return instance;
		} else {
			instance = new Logic();			
			return instance;
		}
	}
	
    /**
     * 
     * @param descriptor
     * @return
     */
	public OWLOntology loadProject(ProjectDescriptor descriptor) {
		OWLOntology ontology = null;
		try {
			this.descriptor = descriptor;			
			String ontologyPhysicalURI = new String();
			if(!descriptor.getLastModifiedOntologyFileUri().equals("")) {
				ontologyPhysicalURI= descriptor.getLastModifiedOntologyFileUri();
			} else {
				ontologyPhysicalURI = descriptor.getBaseOntologyFileUri();
			}
			
			ontology = ontologyManager.loadOntologyFromPhysicalURI(URI.create(ontologyPhysicalURI));
			this.loadedOntologyURI = ontology.getURI().toString();
			this.changed = false;
		} catch(OWLOntologyCreationException ex) {
			logger.severe("Ontology loading error: " + ex.getMessage());		
		}
		return ontology; 
	}
	
	
	public void printSomething(String classURI) {
		System.out.println(classURI);
		
		OWLOntology ontology = ontologyManager.getOntology(URI.create(loadedOntologyURI));
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classURI));
		Set<OWLClassAxiom> axioms = ontology.getAxioms(currentClass);
		System.out.println("SIZE:" + axioms.size());
		for(OWLClassAxiom a :axioms) {
			System.out.println(a);
		}
		
		
	}
	
	
	public boolean lockOntology(BaseURI ontologyURI) {
		try {
			ontologyManager.lockOntology(ontologyURI);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    public boolean unlockOntology(BaseURI ontologyURI) {
    	try {
			ontologyManager.unlockOntology(ontologyURI);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Metoda pobierajaca ontologie z pliku, przy wykorzystaniu kopii <b>OntologyManagera</b>
	 * znajdujacej sie w instancji tej klasy. Metoda moze posluzyc do ladowania dowolnej liczby 
	 * ontologii o roznym URI!!!. W przypadku potrzeby ladowania wielu kopii ontologii o tym samym
	 * URI nalezy wykorzystac metoda {@link Logic#loadOntology(String)}.
	 * 
	 * @param fileURI URI pliku z ontologia
	 * @return null jesli ontologia nie moze byc pobrana, w przeciwnym wypadku obiekt
	 *  OWL API reprezentujacy ontologie
	 */
	public OWLOntology loadOntologyFromFile(String fileURI) {
		OWLOntology ontology = null;
		try {
			this.descriptor = null;
			ontology = ontologyManager.loadOntologyFromPhysicalURI(URI.create(fileURI));
			this.loadedOntologyURI = ""+ontology.getURI();
			this.changed = false;
		} catch(OWLOntologyCreationException ex) {
			ex.printStackTrace();
		}
		return ontology; 
	}
	
	/**
	 * Metoda pobierajaca ontologie z pliku. Uwaga: ontologia ladowana jest przy uzyciu
	 * nowego <b>OntologyManagera</b>!!! Metoda moze byc wykorzystana gdy chcemy miec dostep do
	 * 2 kopii ontologii o tym samym URI. 
	 * 
	 * @param fileURI URI pliku z ontologia
	 * @return null jesli ontologia nie moze byc pobrana, w przeciwnym wypadku obiekt
	 *  OWL API reprezentujacy ontologie
	 */
	public OWLOntology loadOntology(String fileURI) {
		OWLOntology baseOntology = null;
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		try {
			baseOntology = manager.loadOntologyFromPhysicalURI(URI.create(fileURI));
			manager.removeOntology(baseOntology.getURI());
			this.changed = false;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return baseOntology;
	}

	/**
	 * Metoda dodajaca relacje dziedzicznia dla 2 klas.
	 * 
	 * @param superClassUri uri klasy nadrzednej 
	 * @param subClassUri uri klasy podrzednej
	 * @return obiekt reprezentujacy zmiany w ontologii
	 */
	public OWLAxiomChange createSubClassRelation(String ontologyURI, String superClassUri, String subClassUri) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		OWLClass superClass = dataFactory.getOWLClass(URI.create(superClassUri));
		OWLClass newClass = dataFactory.getOWLClass(URI.create(subClassUri));
		
		OWLAxiom axiom = dataFactory.getOWLSubClassAxiom(newClass, superClass);
		return new AddAxiom(ontology, axiom);
	}
	
	/**
	 * 
	 * @param disjointClassesUris
	 * @return
	 */
	public OWLAxiomChange createDisjointClassesRelations(String ontologyURI, ArrayList<String> disjointClassesUris) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		HashSet<OWLClass> disjontClasess = new HashSet<OWLClass>();
		
		//dodanie klas do zbioru klas rozlacznych
		for(String uri: disjointClassesUris) {
			OWLClass currentClass = dataFactory.getOWLClass(URI.create(uri));
			disjontClasess.add(currentClass);
		}
		
		//stworzneie axiomu
		OWLDisjointClassesAxiom axiom = dataFactory.getOWLDisjointClassesAxiom(disjontClasess);
		return new AddAxiom(ontology, axiom);
	}
	
	public OWLAxiomChange createEquivalentClassesAxiom(OWLDescription firstClass, OWLDescription secondClass) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(loadedOntologyURI));
		
		Set<OWLDescription> classes = new HashSet<OWLDescription>();
		classes.add(firstClass);
		classes.add(secondClass);
		
		OWLEquivalentClassesAxiom axiom = dataFactory.getOWLEquivalentClassesAxiom(classes);
		return new AddAxiom(ontology, axiom);
	}
	
	/**
	 * 
	 * @param ontologyURI
	 * @param individualURI
	 * @return
	 */
	public OWLAxiomChange createIndividual(String ontologyURI, String individualURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLIndividual ind = dataFactory.getOWLIndividual(URI.create(individualURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLDeclarationAxiom(ind));
	}
	
	public OWLAxiomChange createClass(String ontologyURI, String classURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLDeclarationAxiom(currentClass));
	}
	
	public OWLAxiomChange createObjectProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty currentProperty = dataFactory.getOWLObjectProperty(URI.create(propertyURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLDeclarationAxiom(currentProperty));
	}
	
	public OWLAxiomChange createDataProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLDataProperty currentProperty = dataFactory.getOWLDataProperty(URI.create(propertyURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLDeclarationAxiom(currentProperty));
	}
	
	public OWLAxiomChange createSubObjectPropertyRelation(String ontologyURI, String superPropertyURI, String subPropertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty superProperty = dataFactory.getOWLObjectProperty(URI.create(superPropertyURI));
		
		OWLObjectProperty subProperty = dataFactory.getOWLObjectProperty(URI.create(subPropertyURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLSubObjectPropertyAxiom(subProperty, superProperty));
	}
	
	public OWLAxiomChange createSubDataPropertyRelation(String ontologyURI, String superPropertyURI, String subPropertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLDataProperty superProperty = dataFactory.getOWLDataProperty(URI.create(superPropertyURI));
		
		OWLDataProperty subProperty = dataFactory.getOWLDataProperty(URI.create(subPropertyURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLSubDataPropertyAxiom(subProperty, superProperty));
	}
	
	public OWLAxiomChange createInverseObjectPropertiesRelation(String ontologyURI, String firstPropertyURI, String secondPropertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty firstProperty = dataFactory.getOWLObjectProperty(URI.create(firstPropertyURI));
		
		OWLObjectProperty secondProperty = dataFactory.getOWLObjectProperty(URI.create(secondPropertyURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLInverseObjectPropertiesAxiom(firstProperty, secondProperty));
	}
	
	public OWLAxiomChange createFunctionalObjectProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty property = dataFactory.getOWLObjectProperty(URI.create(propertyURI));
		return new AddAxiom(ontology, dataFactory.getOWLFunctionalObjectPropertyAxiom(property));
	}
	
	public OWLAxiomChange createFunctionalDataProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLDataProperty property = dataFactory.getOWLDataProperty(URI.create(propertyURI));
		return new AddAxiom(ontology, dataFactory.getOWLFunctionalDataPropertyAxiom(property));
	}
	
	public OWLAxiomChange createInverseFunctionalObjectProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty property = dataFactory.getOWLObjectProperty(URI.create(propertyURI));
		return new AddAxiom(ontology, dataFactory.getOWLInverseFunctionalObjectPropertyAxiom(property));
	}
	
	public OWLAxiomChange createSymmetricObjectProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty property = dataFactory.getOWLObjectProperty(URI.create(propertyURI));
		return new AddAxiom(ontology, dataFactory.getOWLSymmetricObjectPropertyAxiom(property));
	}
	
	public OWLAxiomChange createTransitiveObjectProperty(String ontologyURI, String propertyURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLObjectProperty property = dataFactory.getOWLObjectProperty(URI.create(propertyURI));
		return new AddAxiom(ontology, dataFactory.getOWLTransitiveObjectPropertyAxiom(property));
	}
	
	public OWLAxiomChange createEntityAnnotationAxiom(String ontologyURI, OWLEntity entity, OWLAnnotation<?> annotation) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		return new AddAxiom(ontology, dataFactory.getOWLEntityAnnotationAxiom(entity, annotation));
	}

	/**
	 * @param ontologyURI
	 * @param individualUri
	 * @param comment
	 * @return
	 */
	public OWLAxiomChange createIndividualCommentAxiom(String ontologyURI, String individualUri, String comment) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLIndividual currentInd = dataFactory.getOWLIndividual(URI.create(individualUri)); 
		
		//stworzenie aksjomu okreslajacego dany komentarz
		OWLCommentAnnotation commentAnnotation = dataFactory.getCommentAnnotation(comment);
		OWLAxiom axiom = dataFactory.getOWLEntityAnnotationAxiom(currentInd, commentAnnotation);
		
		return new AddAxiom(ontology, axiom);
	}
	
	/**
	 * 
	 * @param ontologyURI
	 * @param individualUri
	 * @param label
	 * @return
	 */
	public OWLAxiomChange createIndividualLabelAxiom(String ontologyURI, String individualUri, String label) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		OWLIndividual currentInd = dataFactory.getOWLIndividual(URI.create(individualUri));
		
		OWLLabelAnnotation labelAnnotation = dataFactory.getOWLLabelAnnotation(label);
		OWLAxiom axiom = dataFactory.getOWLEntityAnnotationAxiom(currentInd, labelAnnotation);
		
		return new AddAxiom(ontology, axiom);
	}
	
	/**
	 * 
	 * @param ontologyURI
	 * @param individualsUris
	 * @return
	 */
	public OWLAxiomChange createSameIndividualsAxiom(String ontologyURI, List<String> individualsUris) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		
		for(String indURI : individualsUris) {
			OWLIndividual ind = dataFactory.getOWLIndividual(URI.create(indURI)); 
			individuals.add(ind);
		}
		
		OWLSameIndividualsAxiom axiom = dataFactory.getOWLSameIndividualsAxiom(individuals);
		return new AddAxiom(ontology, axiom);
	}
	
	public OWLAxiomChange createDifferentIndividualsAxiom(String ontologyURI, List<String> individualsUris) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		
		for(String indURI : individualsUris) {
			OWLIndividual ind = dataFactory.getOWLIndividual(URI.create(indURI)); 
			individuals.add(ind);
		}
		
		OWLDifferentIndividualsAxiom axiom = dataFactory.getOWLDifferentIndividualsAxiom(individuals);
		return new AddAxiom(ontology, axiom);
	}
	
	public OWLAxiom getObjectPropertyRangeAxiom(String objectPropertyURI, String classURI) {
		OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(URI.create(objectPropertyURI));
		
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classURI));
		OWLObjectPropertyRangeAxiom axiom = dataFactory.getOWLObjectPropertyRangeAxiom(objectProperty, currentClass);
			
		return axiom;
	}
	
	public OWLAxiom getDataPropertyRangeAxiom(String dataPropertyURI, String rangeURI) {
		OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(URI.create(dataPropertyURI));
		
		OWLDataType rangeDataType = dataFactory.getOWLDataType(URI.create(rangeURI));
		OWLDataPropertyRangeAxiom axiom = dataFactory.getOWLDataPropertyRangeAxiom(dataProperty, rangeDataType);
			
		return axiom;
	}
	
	public OWLAxiom getDataPropertyRangeAxiom(String dataPropertyURI, OWLDataRange range) {
		
		OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(URI.create(dataPropertyURI));
		
		OWLDataPropertyRangeAxiom axiom = dataFactory.getOWLDataPropertyRangeAxiom(dataProperty, range);
		
		return axiom;
	}
	
	public OWLAxiom getObjectPropertyDomainAxiom(String objectPropertyURI, String classURI) {
		OWLObjectProperty objectProperty = dataFactory.getOWLObjectProperty(URI.create(objectPropertyURI));
		
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classURI));
		OWLObjectPropertyDomainAxiom axiom = dataFactory.getOWLObjectPropertyDomainAxiom(objectProperty, currentClass);
			
		return axiom;
	}
	
	public OWLAxiom getDataPropertyDomainAxiom(String dataPropertyURI, String classURI) {
		
		OWLDataProperty dataProperty = dataFactory.getOWLDataProperty(URI.create(dataPropertyURI));
		
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classURI));
		OWLDataPropertyDomainAxiom axiom = dataFactory.getOWLDataPropertyDomainAxiom(dataProperty, currentClass);
		
		return axiom;
	}
	
	
	/**
	 * 
	 * @param individualURI
	 * @param individualTypes
	 * @return
	 */
	public List<OWLAxiomChange> createIndividualTypesAxioms(String ontologyURI, String individualURI, List<String> individualTypes) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		List<OWLAxiomChange> result = new ArrayList<OWLAxiomChange>();
		OWLIndividual currentInd = dataFactory.getOWLIndividual(URI.create(individualURI));
		
		//usuniecie starych typow
		Set<OWLDescription> types = currentInd.getTypes(ontology);
		for(OWLDescription desc : types) {
			if(!desc.isAnonymous()) {
				OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(currentInd, desc.asOWLClass());
				result.add(new RemoveAxiom(ontology, axiom));
			}
		}
		
		//dodanie aksjomatow z nastepujaca lista typow
		for(String indType : individualTypes) {
			OWLClass currentClass = dataFactory.getOWLClass(URI.create(indType));
			OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(currentInd, currentClass);
			result.add(new AddAxiom(ontology, axiom));
		}
		
		return result;
	}
	
	public void createNewOntology(VersionedURI versionedURI, URI physicalURI, String userName) {
		try {
			OWLOntology ont = ontologyManager.loadOntologyFromPhysicalURI(physicalURI);			
			DBOntology dbOntology = ontologyManager.convertToDBOntology(ont);			
			dbOntology.setOwner(userName);			
			dbOntology.setVersionedURI(versionedURI);
			ontologyManager.importOntology(dbOntology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createEmptyOntology(VersionedURI versionedURI, String userName) {		
		try {
			DBOntology dbOntology = new DBOntology();
			dbOntology.setOwner(userName);
			dbOntology.setVersionedURI(versionedURI);
			ontologyManager.importOntology(dbOntology);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
		
	/**
	 * 
	 * @param classUri
	 * @param label
	 * @return
	 */
	public OWLAxiomChange createLabelAxiom(String ontologyURI, String classUri, String label) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		//pobranie klasy 
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classUri));
		
		OWLLabelAnnotation labelAnnotation = dataFactory.getOWLLabelAnnotation(label);
		OWLAxiom axiom = dataFactory.getOWLEntityAnnotationAxiom(currentClass, labelAnnotation);
		
		return new AddAxiom(ontology, axiom);
	}
	
	/**
	 * Metoda dodaja aksjom komentarza do danej klasy.
	 * 
	 * @param classUri uri klasy do ktorej bedzie doddany komentarz
	 * @param comment komentarz
	 * @return obiekt reprezentujacy zmiane w ontologii
	 */
	public OWLAxiomChange createCommentAxiom(String ontologyURI, OWLEntity entity, String comment) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		//stworzenie aksjomu okreslajacego dany komentarz
		OWLCommentAnnotation commentAnnotation = dataFactory.getCommentAnnotation(comment);
		OWLAxiom axiom = dataFactory.getOWLEntityAnnotationAxiom(entity, commentAnnotation);
		
		return new AddAxiom(ontology, axiom);
	}
	
	
	/**
	 * Meotda tworzy liste aksjomow pozwalajacych na usuwanie
	 * danej klasy i jej wszystkich podklas (rekurencyjnie). 
	 * 
	 * @param classUri URI klasy do usuniecia
	 * @return lista aksjomow
	 */
	public List<OWLOntologyChange> createDeleteClassAxiom(String ontologyURI, String classUri) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		//pobranie klasy
		OWLClass currentClass = dataFactory.getOWLClass(URI.create(classUri));
		
		List<OWLClass> listOfClasses = new ArrayList<OWLClass>();

		//wygenerowanie listy klas do usuniecia - cale podrzewo od danego drzewa
		listOfClasses = getClassesToRemove(ontologyURI, currentClass, listOfClasses);
		//dodanie na koncu biezacej klasy
		listOfClasses.add(currentClass);
		

		OWLEntityRemover remover = new OWLEntityRemover(ontologyManager, Collections.singleton(ontology));
		
		for(OWLClass c: listOfClasses) {
			c.accept(remover);
		}
		
		return remover.getChanges();
	}
	
	public List<OWLOntologyChange> createDeleteObjectPropertyAxiom(String ontologyURI, OWLObjectProperty property) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		List<OWLObjectProperty> listOfProperties = new ArrayList<OWLObjectProperty>();

		//wygenerowanie calej galezi drzewa do usuniecia
		listOfProperties = getObjectPropertiesToRemove(ontologyURI, property, listOfProperties);
		
		listOfProperties.add(property);
		
		OWLEntityRemover remover = new OWLEntityRemover(ontologyManager, Collections.singleton(ontology));
		
		for(OWLObjectProperty prop: listOfProperties) {
			prop.accept(remover);
		}
		
		return remover.getChanges();
	}
	
	public List<OWLOntologyChange> createDeleteDataPropertyAxiom(String ontologyURI, OWLDataProperty property) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		List<OWLDataProperty> listOfProperties = new ArrayList<OWLDataProperty>();

		//wygenerowanie calej galezi drzewa do usuniecia
		listOfProperties = getDataPropertiesToRemove(ontologyURI, property, listOfProperties);
		
		listOfProperties.add(property);
		
		OWLEntityRemover remover = new OWLEntityRemover(ontologyManager, Collections.singleton(ontology));
		
		for(OWLDataProperty prop: listOfProperties) {
			prop.accept(remover);
		}
		
		return remover.getChanges();
	}
	
	
	
	/**
	 * 
	 * @param baseOntology
	 * @param changedOntology
	 * @param title
	 * @param description
	 * @param ontologyURI
	 */
	public void exportChanges(OWLOntology baseOntology, OWLOntology changedOntology, String title, String description, VersionedURI ontologyURI) {
		DBOntology baseDBOntology;
		DBOntology changedDBOntology;
		try {
			baseDBOntology = ontologyManager.convertToDBOntology(baseOntology);
			changedDBOntology = ontologyManager.convertToDBOntology(changedOntology);
			
			DBProposition proposition = new DBProposition();
			proposition.setDescription(description);
			proposition.setTitle(title);
			proposition.setOwner(loggedInUser.getUsername());
			
			ontologyManager.newProposition(baseDBOntology, changedDBOntology, proposition, ontologyURI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * 
	 * @param individualUri
	 * @return
	 */
	public List<OWLOntologyChange> createDeleteIndividualAxiom(String ontologyURI, String individualUri) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		OWLIndividual currentIndividual = dataFactory.getOWLIndividual(URI.create(individualUri));
		
		OWLEntityRemover remover = new OWLEntityRemover(ontologyManager, Collections.singleton(ontology));
		currentIndividual.accept(remover);
		
		List<OWLOntologyChange> result = remover.getChanges();
		
		return result;
	}
	
	
	/**
	 * Generuje liste wszystkich podklas danej klasy - rekurencyjnie.
	 * @param rootClass klasa bazowa bedaca korzeniem danego poddrzewa
	 * @param listOfClasses aktualna lista klas
	 * @return lista wszystkich podklas
	 */
	private List<OWLClass> getClassesToRemove(String ontologyURI, OWLClass rootClass, List<OWLClass> listOfClasses) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		Set<OWLDescription> subClasses = rootClass.getSubClasses(ontology);
		
		for(OWLDescription desc : subClasses) {
			if(!desc.isAnonymous()) {
				OWLClass c = desc.asOWLClass();
				getClassesToRemove(ontologyURI, c, listOfClasses);
				listOfClasses.add(c);
			}
		}
		return listOfClasses;
	}
	
	private List<OWLObjectProperty> getObjectPropertiesToRemove(String ontologyURI, OWLObjectProperty rootProperty, List<OWLObjectProperty> listOfProperties) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		Set<OWLObjectPropertyExpression> subProperties = rootProperty.getSubProperties(ontology);
		
		for(OWLObjectPropertyExpression desc : subProperties) {
			if(!desc.isAnonymous()) {
				OWLObjectProperty prop = desc.asOWLObjectProperty();
				getObjectPropertiesToRemove(ontologyURI, prop, listOfProperties);
				listOfProperties.add(prop);
			}
		}
		return listOfProperties;
	}
	
	private List<OWLDataProperty> getDataPropertiesToRemove(String ontologyURI, OWLDataProperty rootProperty, List<OWLDataProperty> listOfProperties) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		Set<OWLDataPropertyExpression> subProperties = rootProperty.getSubProperties(ontology);
		
		for(OWLDataPropertyExpression desc : subProperties) {
			if(!desc.isAnonymous()) {
				OWLDataProperty prop = desc.asOWLDataProperty();
				getDataPropertiesToRemove(ontologyURI, prop, listOfProperties);
				listOfProperties.add(prop);
			}
		}
		return listOfProperties;
	}
	
	public void displayReferencedClasses(String ontologyURI){
		this.usedClasses = new HashSet<String>();
		OWLOntology ontology = ontologyManager.getOntology(URI.create(ontologyURI));
		
		Set<OWLClass> referencedClasses = ontology.getReferencedClasses();
		for(OWLClass currentClass: referencedClasses) {
			if(currentClass.getSuperClasses(ontology) == null || currentClass.getSuperClasses(ontology).size() == 0) { 
				if(!usedClasses.contains(currentClass.getURI().toString())) {
					System.out.println(currentClass.getURI().toString());
					//dodanie kolejnego wezla 
					usedClasses.add(currentClass.getURI().toString());
					//rozbudowanie drzewa
					displaySubclasses(ontology, currentClass, 1);
				}
			}
		}
	}
	
	private void displaySubclasses(OWLOntology ontology, OWLClass currentClass, int index) {
		Set<OWLDescription> subClasses = currentClass.getSubClasses(ontology);
		
		for(OWLDescription desc: subClasses) {
			if(!desc.isAnonymous()) {
				OWLClass c= desc.asOWLClass();
				for(int i=0;i<index;i++)
					System.out.print("\t");
				System.out.println(c.getURI().toString());
				usedClasses.add(c.getURI().toString());
				int temp = index;
				displaySubclasses(ontology, c, temp+1);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean checkIfUserLoggedIn(MainFrame parentFrame) {
		if(loggedInUser == null || loggedInUser.getUsername() == null || loggedInUser.getUsername().equals("")) {
			LoginDialog dialog = new LoginDialog();
			dialog.open();			
		}
		
		if(loggedInUser.getUsername() != null && !loggedInUser.getUsername().equals("")) {
			parentFrame.getMenu().reintializeMenus();
			return true;
		}
		else { 
			return false;
		}
	}
	
	public boolean isLocked(OntologyURI ontologyURI) throws Exception{
		return ontologyManager.isLocked(ontologyURI);
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<BaseURI> getAllowedOntologies(String userName) {
		List<BaseURI> ownedOntologies = new ArrayList<BaseURI>();
		List<BaseURI> expertisedOntologies = new ArrayList<BaseURI>();
		List<BaseURI> returnValue = new ArrayList<BaseURI>();
		
		try {
			ownedOntologies = ontologyManager.getOntologiesByUser(userName);
			expertisedOntologies = ontologyManager.getOntologiesByExpert(userName);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		returnValue = ownedOntologies;

		//sprawdzamy czy juz mamy te uri
		//jesli nie to dodajemy
		for(BaseURI uri : expertisedOntologies) {
			if(!returnValue.contains(uri)) {
				returnValue.add(uri);
			}
		}
		
		Collections.reverse(returnValue);
		
		return returnValue;
		
	}
	
	/**
	 * Metoda pobierajaca informacje o wszystkich ontologiach 
	 * w systemie (bez wersji i podwersji).
	 * 
	 * @return lista wszystkich ontologii
	 */
	public List<BaseURI> getOntologiesInfo() {
		List<BaseURI> listOfBaseUris = new ArrayList<BaseURI>();
		
		try {
			listOfBaseUris = ontologyManager.getBaseOntologyURIs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.reverse(listOfBaseUris);
		
		return listOfBaseUris;		
	}
	

	/**
	 * Metoda zwracajaca dla danej ontologii liste jego wersji i podwersji
	 * 
	 * @param baseUri bazowe uri ontologi
	 * @return list wersji i podwersji danej ontologii
	 */
	public List<VersionedURI> getOntologyVersions(OntologyURI baseUri) {
		List<VersionedURI> listOfVersionedUris = new ArrayList<VersionedURI>();
		
		try {
			listOfVersionedUris = ontologyManager.getOntologyVersions(baseUri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.reverse(listOfVersionedUris);
		
		return listOfVersionedUris;
	}
	
	/**
	 * Metoda przeprowadzajaca proces uwierzytelnia uzytkownika w systemie.
	 *  
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean loginUser(String userName, String password) throws Exception{
		ontologyManager.logIn(userName, password);
		loggedInUser = new User();
		loggedInUser.setUsername(userName);
		return true;
	}
	
	public boolean isUserLoggedIn() {
		boolean returnValue = false;
		if(loggedInUser != null && loggedInUser.getUsername() != null &&!loggedInUser.getUsername().trim().equals("")) {
			returnValue = true;
		}
		return returnValue;
	}
	
	public void logoutUser() {
		try {
			ontologyManager.logOut();
			this.loggedInUser = null;
		} catch(Exception ex) {
		}		
	}
	
	/**
	 * 
	 * @param filePath
	 */
	public void saveOntologyToFile(OWLOntology ontologyToSave, String fileURI, VersionedURI ontologyURI) {
		URI physicalURI = URI.create(fileURI);
		
		try {
			ontologyManager.saveOntology(ontologyToSave, new OWLXMLOntologyFormat(), physicalURI);
		} catch (UnknownOWLOntologyException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param ontology
	 * @param fileURI
	 * @param ontologyFormat
	 */
	public void exportOntology(OWLOntology ontology, String fileURI, String ontologyFormat) {
		URI physicalURI = URI.create(fileURI);
		
		try {
			if(ontologyFormat.equals(Constants.OWL_ONTOLOGY_FORMAT)){
				ontologyManager.saveOntology(ontology, new OWLXMLOntologyFormat(), physicalURI);
			} else if(ontologyFormat.equals(Constants.RDF_ONTOLOGY_FORMAT)){
				ontologyManager.saveOntology(ontology, new RDFXMLOntologyFormat(), physicalURI);
			}
		} catch (UnknownOWLOntologyException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}
	
	public OWLOntology getOntology(VersionedURI ontologyURI) {
		OWLOntology ontology = null;
		try {
			ontology = ontologyManager.getOntology(ontologyURI);
			this.loadedOntologyURI = ""+ontology.getURI();
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ontology;
	}
	
	/**
	 * 
	 * @param ontologyURI
	 * @return
	 */
	public List<OntologyProposition> getPropositions(VersionedURI ontologyURI) {
		List<OntologyProposition> returnValue = new ArrayList<OntologyProposition>();
		
		try {
			List<PropositionDescription> props = ontologyManager.getPropositions(ontologyURI);
			
			for(PropositionDescription prop : props) {
				OntologyProposition temp = new OntologyProposition();
				temp.setPropositionId(prop.getPropositionID());
				temp.setDescription(prop.getDescription());
				temp.setTitle(prop.getTitle());
				temp.setPropositionToAdd(false);
				temp.setPropositionToPromote(true);
				temp.setPropositionToRemove(false);
				
				returnValue.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}
	
	public Set<OWLDifferentIndividualsAxiom> getDifferentIndividuals() {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(this.loadedOntologyURI));
		Set<OWLDifferentIndividualsAxiom> axioms= ontology.getAxioms(AxiomType.DIFFERENT_INDIVIDUALS);
		return axioms; 
	}
	
	public Set<OWLAxiom> getReferencedAxioms(OWLEntity entity) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(loadedOntologyURI));
		
		Set<OWLAxiom> axioms = ontology.getReferencingAxioms(entity);
		return axioms;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<OWLDifferentIndividualsAxiom> getDifferentIndividualsAxioms(String individualURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(this.loadedOntologyURI));
		OWLIndividual individual = dataFactory.getOWLIndividual(URI.create(individualURI));
		
		Set<OWLDifferentIndividualsAxiom> axioms = ontology.getDifferentIndividualAxioms(individual);
		
		return axioms;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<OWLSameIndividualsAxiom> getSameIndividualsAxioms(String individualURI) {
		OWLOntology ontology = ontologyManager.getOntology(URI.create(this.loadedOntologyURI));
		OWLIndividual individual = dataFactory.getOWLIndividual(URI.create(individualURI));
		
		Set<OWLSameIndividualsAxiom> axioms = ontology.getSameIndividualAxioms(individual);
		
		return axioms;
	}
	/**
	 * 
	 * @param propositionId
	 * @return
	 */
	public OWLPropositionData getPropositionData(Integer propositionId, URI ontologyURI) {
		OWLPropositionData data = new OWLPropositionData();
		try {
			data = ontologyManager.getProposition(propositionId, ontologyURI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public void createNewVersion(VersionedURI ontologyURI, List<Integer> accepted, List<Integer> rejected, boolean newVersionIndicator) {
		try {
			ontologyManager.createNewVersion(ontologyURI, accepted, rejected, new Boolean(newVersionIndicator));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public OWLOntology getOntology(String ontologyURI) {
		return ontologyManager.getOntology(URI.create(ontologyURI));			
	}
	
	
	/**
	 * Metoda zapisujaca zmiany w ontologii. 
	 * 
	 * @param changes lista zmian w ontologii
	 * @throws OWLOntologyChangeException 
	 */
	public void applyChanges(List<? extends OWLOntologyChange> changes) throws OWLOntologyChangeException {
		ontologyManager.applyChanges(changes);
		this.changed = true;
	}
	
	public void applyChange(OWLOntologyChange change) throws OWLOntologyChangeException {
		ontologyManager.applyChange(change);
		this.changed = true;
	}

	/**
	 * 
	 * @return
	 */
	public PGOWLOntologyManager getOntologyManager() {
		return ontologyManager;
	}

	/**
	 * 
	 * @param ontologyManager
	 */
	public void setOntologyManager(PGOWLOntologyManager ontologyManager) {
		this.ontologyManager = ontologyManager;
	}

	/**
	 * 
	 * @return
	 */
	public OWLDataFactory getDataFactory() {
		return dataFactory;
	}

	/**
	 * 
	 * @param dataFactory
	 */
	public void setDataFactory(OWLDataFactory dataFactory) {
		this.dataFactory = dataFactory;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public ApplicationConfiguration getConfig() {
		return config;
	}

	public void setConfig(ApplicationConfiguration config) {
		this.config = config;
	}

	public ProjectDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(ProjectDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	public String getLoadedOntologyURI() {
		return loadedOntologyURI;
	}

	public void setLoadedOntologyURI(String loadedOntologyURI) {
		this.loadedOntologyURI = loadedOntologyURI;
	}

	/**
	 * Okresla czy zaladowana ontologia lub projekt ulegly zmianie.
	 * @return
	 */
	public boolean isChanged() {
		return changed;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
