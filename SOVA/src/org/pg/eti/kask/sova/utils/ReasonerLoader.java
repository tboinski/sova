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

package org.pg.eti.kask.sova.utils;

import org.semanticweb.HermiT.Reasoner;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

/**
 * Klasa przechowująca i ładująca reasoner wykorzystywany w wywnioskowanej
 * hierarchii. Domyślnym reasonerem jest org.mindswap.pellet.owlapi.Reasoner,
 * jednak należy pamiętać o załączeniu bibliotek pellet.
 * 
 * Tymczasowy reasoner to HermiT, pozostałe są niezgodne z OWL API 3.2.3
 *
 * @author Piotr Kunowski
 * 
 */
public class ReasonerLoader {
    //nazwa klasy Reasonera.

//    private static final String REASONER_CLASS_NAME = "org.mindswap.pellet.owlapi.Reasoner";
    private static ReasonerLoader INSTANCE = null;
    private static OWLReasoner reasoner = null;

    private ReasonerLoader() {
    }

    /**
     * @return statyczna instancja klasy ReasonerLoader.
     */
    public static ReasonerLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReasonerLoader();
        }
        return INSTANCE;
    }

    /**
     * Metoda pobiera reasoner, jeśli nie został on załowdowany przez
     * użytkownika wcześnije to zwracany jest
     * org.mindswap.pellet.owlapi.Reasoner. Należy pamiętać o załączeniu
     * biblioteki pellet.
     *
     * @return
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws SecurityException
     */
    public OWLReasoner getReasoner(OWLOntology ont) throws SecurityException, ClassNotFoundException, NoSuchMethodException {
        if (reasoner == null) {
            initializeReasoner(ont);
        }

        return reasoner;
    }

    /**
     * metoda ustawia reasoner
     * @param reasoner
     */
    public void setReasoner(OWLReasoner reasoner) {
        ReasonerLoader.reasoner = reasoner;
    }

    /**
     * Metoda inicjalizuja mechanizm wnioskujacy.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private void initializeReasoner(OWLOntology ont) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
//
//        try {
//
//            // Create a console progress monitor. This will print the reasoner progress out to the console.
////            ReasonerProgressMonitor progressMonitor = new ConsoleProgressMonitor();
////            OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
//            // Create a reasoner that will reason over our ontology and its imports closure. Pass in the configuration.
//            Class<?> reasonerClass = Class.forName(REASONER_CLASS_NAME);
////            Constructor<?> con = reasonerClass.getConstructor(OWLOntologyManager.class);
////            reasoner = (OWLReasoner) con.newInstance(OWLManager.createOWLOntologyManager());
//            Constructor<?> con = reasonerClass.getConstructor(OWLOntology.class);
//            reasoner = (OWLReasoner) con.newInstance(ont);
//
//
//
//
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (UnknownOWLOntologyException e) {
//            e.printStackTrace();
//        }
        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        // Create a console progress monitor. This will print the reasoner progress out to the console.
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        // Specify the progress monitor via a configuration. We could also specify other setup parameters in


        // the configuration, and different reasoners may accept their own defined parameters this way.
        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
        // Create a reasoner that will reason over our ontology and its imports closure. Pass in the configuration.

        reasoner = reasonerFactory.createReasoner(ont, config);
    }
}
