/*
 *
 * Copyright (c) 2010 Gdańsk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
 * Copyright (c) 2010 Jaworska Anna
 * Copyright (c) 2010 Kleczkowski Radosław
 * Copyright (c) 2010 Orłowski Piotr
 *
 * This file is part of OCS.  OCS is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.pg.eti.kask.sova.graph;

import java.util.HashSet;
import java.util.Set;
import org.pg.eti.kask.sova.utils.ReasonerLoader;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import prefuse.data.Node;
import prefuse.data.Tree;

/**
 * Konwerter obiektu OWLOntology na obiekt Tree wykorzystywany przez Prefuse.
 * Klasa jest singletonem.
 * 
 * @author Piotr Kunowski
 * 
 */
public class OWLtoHierarchyTreeConverter {
    //nazwa klasy Reasonera.

    private static final String REASONER_CLASS_NAME = "org.mindswap.pellet.owlapi.Reasoner";
    //rerefencje do obiektow OWL API
    private OWLOntology ontology;
    private OWLOntologyManager ontologyManager;
    private OWLDataFactory dataFactory;
    private OWLReasoner reasoner;
    private Tree tree;
    //wykorzystane klasy podczas budowania drzewa
    private Set<String> usedClasses;

    public OWLtoHierarchyTreeConverter() {
    }

    public Tree OWLtoTree(OWLOntology ontology) {

        this.ontology = ontology;
        this.ontologyManager = OWLManager.createOWLOntologyManager();
        this.dataFactory = ontologyManager.getOWLDataFactory();

        this.usedClasses = new HashSet<String>();
        tree = new Tree();
        tree.addColumn(Constants.TREE_NODES, org.pg.eti.kask.sova.nodes.Node.class);

        //inicjalizacja reasonera
        try {
            initializeReasoner();
            buildTree();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        return tree;
    }

    /**
     * Zbudowanie drzewa poczynajac od elementu najwyzej stojacego w hierarchi -
     * klasy OWLThing.
     */
    private void buildTree() {
        try {
//            reasoner.loadOntologies(ontologyManager.getImportsClosure(ontology));
            OWLClass thingClass = dataFactory.getOWLThing();
            buildTree(null, thingClass);
//            reasoner.clearOntologies();
        } catch (UnknownOWLOntologyException e) {
            e.printStackTrace();
        } catch (OWLException/*OWLReasonerException*/ e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda rekurencyjnie rozbudowujaca drzewo.
     *
     * @param parentNode aktualnie przetwarzany wezel
     * @param currentEntity  aktualnie przetwarzany obiekt OWL API (albo OWLClass, albo  OWLIndividual)
     * @throws OWLReasonerException wyjatek z reasonera
     */
    private void buildTree(Node parentNode, OWLEntity currentEntity)
            throws OWLException/*OWLReasonerException*/ {
        Node currentNode = null;

        if (currentEntity instanceof OWLClass) {
            OWLClass currentClass = (OWLClass) currentEntity;

            if (reasoner.isSatisfiable(currentClass)) {
                if (parentNode == null) {
                    currentNode = tree.addRoot();
                } else {
                    currentNode = tree.addChild(parentNode);
                }
                usedClasses.add(currentClass.getIRI().toString());
                //tu zmienilem
                org.pg.eti.kask.sova.nodes.Node node = null;

                if (currentClass.isOWLThing()) {
                    node = new org.pg.eti.kask.sova.nodes.ThingNode();
                    node.setLabel("T");

                } else {
                    node = new org.pg.eti.kask.sova.nodes.ClassNode();
                    node.setLabel(currentClass.getIRI().toString());
                }
                currentNode.set(Constants.TREE_NODES, node);

                Set<OWLClass> subClasses = (reasoner.getSubClasses(currentClass, true)).getFlattened();

                for (OWLClass child : subClasses) {
                    buildTree(currentNode, child);
                }

                Set<OWLNamedIndividual> individuals = reasoner.getInstances(currentClass, true).getFlattened();

                for (OWLNamedIndividual ind : individuals) {
                    buildTree(currentNode, ind);
                }

            }
        } else if (currentEntity instanceof OWLIndividual) {
            OWLIndividual currentIndividual = (OWLIndividual) currentEntity;

            if (!currentIndividual.isAnonymous()) {
                currentNode = tree.addChild(parentNode);
                org.pg.eti.kask.sova.nodes.Node node = new org.pg.eti.kask.sova.nodes.IndividualNode();
                node.setLabel(currentIndividual.asOWLNamedIndividual().getIRI().toString());
                //tu zmieniłem
                currentNode.set(Constants.TREE_NODES, node);
            }
        }
    }

    /**
     * Metoda inicjalizuja mechanizm wnioskujacy.
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws SecurityException
     */
    private void initializeReasoner() throws SecurityException, ClassNotFoundException, NoSuchMethodException {
        this.reasoner = ReasonerLoader.getInstance().getReasoner(ontology);
    }
}
