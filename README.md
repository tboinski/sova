# SOVA - Simple Ontology Visualization API

Simple Ontology Visualization API - an ontology visualization plugin for Protégé (https://protegewiki.stanford.edu/wiki/SOVA)

The plugin was prepared at Faculty of Electronics, Telecommunication and Informatics, Gdansk University of Technology. It is an extension of an OWLAPI-ontology visualization library prepared by me and my friends as a part of OCS system. It's Source Code is available under LGPLv3 licence.

SOVA has two types of visualization. First one presents full ontology, second presents hierarchy of classes and individuals after Pellet clasification. These types of visualization are described in details below.

## Full ontology visualization

It presents a full ontology as it was written in OWL file. All relations, classes, individuals, properties etc. are an image of OWL file data (without any ontology classification). There are often quite many elements to visualize so do not be afraid if you see a "big pizza". The visualization is interactive. Auto-arrange option can be switched off and on using play/stop button. More information can be found on Symbols page (https://kask.eti.pg.gda.pl/redmine/attachments/download/290/SOVA-Symbols.pdf).

## Visualization options

To access this menu press option button in SOVA side panel. You will find a set of filters to show or hide particular elements of the ontology here. This menu contains also a distance slider - you can use it to hide ontology elements which are more distant from currently chosen ontology element. The last option available in the options menu is an arrangement algorithm. In current version you can choose ForceDirectedLayout (default option) or RadialTreeLayout algorithm. ForceDirect uses a gravity algorithms to arrange elements. RadialTree puts ontology elements in circles around currently chosen element according to distance between these elements.

Classified hierarchy of classes and individuals

To use this option you need to install HermiT in your Protege.
To use this method of visualization:

    choose Hierarchy Tree Vis in PG ETI SOVA tab;
    choose HermiT in reasoner menu;
    press Start reasoner in reasoner menu;
    press restart in PG ETI SOVA tab.

As a default visualization will have 3 levels of depth. Child elements will be shown after pressing the father elements. Only classes and individuals will be shown by this visualization type.

## Running the plugin on Protege 4.3 with modern Java

    Download Protege from http://protege.stanford.edu/download/protege/4.3/zip/
    Download updated Felix package from http://felix.apache.org/downloads.cgi
    Extract both Protege and Felix
    Replace the felix.jar file in Prtege bin directory with the one from the bin directory of the felix archive
    Use 0.8.5 version (or later) of the Plugin
