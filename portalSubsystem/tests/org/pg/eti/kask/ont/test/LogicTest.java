package org.pg.eti.kask.ont.test;


import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.DBOntology;
import org.pg.eti.kask.ont.common.Triple;
import org.pg.eti.kask.ont.common.VersionedURI;
import org.pg.eti.kask.ont.editor.Logic;
import org.semanticweb.owl.model.OWLOntology;

public class LogicTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void loadOntologyFromFile() throws Exception {
		Logic logic = Logic.getInstance();
		String fileURI = "file:///c:/pizza.owl";
		OWLOntology ontology =logic.loadOntologyFromFile(fileURI);
		Assert.assertNotNull(ontology);
		Assert.assertNotNull(logic.getLoadedOntologyURI());
		Assert.assertNotSame("", logic.getLoadedOntologyURI());
	}
	
	@Test
	public void loginUser() throws Exception {
		Logic logic = Logic.getInstance();
		boolean retValue = logic.loginUser("jakow", "asdfg");
		Assert.assertFalse(retValue);	
	}
	
	@Test
	public void getOntologiesInfo() throws Exception {
		Logic logic = Logic.getInstance();
		logic.loginUser("xh", "tajne");
		List<BaseURI> ontologies = logic.getOntologiesInfo();
		Assert.assertNotNull(ontologies);
		Assert.assertTrue(ontologies.size() > 0);
	}

	@Test
	public void getOntologyVersions() throws Exception {
		Logic logic = Logic.getInstance();
		logic.loginUser("xh", "tajne");
		List<BaseURI> ontologies = logic.getOntologiesInfo();
		Assert.assertNotNull(ontologies);		
		Assert.assertTrue(ontologies.size() > 0);
		BaseURI baseURI = ontologies.get(0);
		System.out.println(baseURI.getBaseURI());
		List<VersionedURI> versionedURIs = logic.getOntologyVersions(baseURI);
		Assert.assertNotNull(versionedURIs);		
		Assert.assertTrue(versionedURIs.size() > 0);
		System.out.println(versionedURIs.size());
	}
	
	@Test
	public void getOntology() throws Exception {
		Logic logic = Logic.getInstance();
		logic.loginUser("xh", "tajne");
		VersionedURI versionedURI = new VersionedURI();
		versionedURI.setBaseURI(new BaseURI("importowana"));
		versionedURI.setOntologyVersion(1);
		versionedURI.setOntologySubVersion(1);
		OWLOntology ontology = logic.getOntology(versionedURI);
		Assert.assertNotNull(ontology);
		System.out.println(ontology.getURI());
		DBOntology dbOntology = logic.getOntologyManager().convertToDBOntology(ontology);
		Assert.assertNotNull(dbOntology);
		System.out.println(dbOntology.getOntology().size());
	}
	
	@Test
	public void testTime() throws Exception {
		Logic logic = Logic.getInstance();
		String fileURI = "file:///c:/ontologie/cosmo/COSMO.owl";
		OWLOntology ontology =logic.loadOntologyFromFile(fileURI);
		long t1, t2;
		t1 = System.currentTimeMillis();
		DBOntology db =logic.getOntologyManager().convertToDBOntology(ontology);
		t2 = System.currentTimeMillis();
		System.out.println("T:"+(t2-t1));
		System.out.println("S="+db.getOntology().size());
	}
	
	@Test
	public void testMemory() throws Exception {
		Logic logic = Logic.getInstance();
		String fileURI = "file:///c:/ontologie/amino/amino-acid.owl";
		OWLOntology ontology =logic.loadOntologyFromFile(fileURI);
		DBOntology db =logic.getOntologyManager().convertToDBOntology(ontology);		
		Vector<Triple> triples = (Vector<Triple>)db.getOntology();
		db=null;
		ontology = null;
		fileURI = null;
		logic = null;
		Object[] tab = new Object[3000];
		tab[0] = triples;
		for(int i=1;i<3000;i++) {					
			tab[i] = triples.clone();
		}
		
		Runtime runtime=Runtime.getRuntime();
		runtime.gc();
		Thread.sleep(100);
		long memUsed=runtime.totalMemory()-runtime.freeMemory();
		System.out.println("MEMORY:"+memUsed);
	//	System.out.println("TROJKI:"+db.getOntology().size());

	}
	
	@Test
	public void testOntologies() throws Exception {
		Logic logic = Logic.getInstance();
		logic.loginUser("xh", "tajne");
		List<BaseURI> ontologie = logic.getOntologiesInfo();
		for(BaseURI u:ontologie) {
			List<VersionedURI> versions = logic.getOntologyVersions(u);
			for(VersionedURI v : versions) {
				System.out.println(v.getURIAsString());
				OWLOntology ont = logic.getOntology(v);
				
				System.out.println(ont);
				
				
			}
		}
	}
	
}
