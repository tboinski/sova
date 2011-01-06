private void buildTree() {
    try {
        reasoner
                .loadOntologies(ontologyManager.getImportsClosure(ontology));
        OWLClass thingClass = dataFactory.getOWLThing();
        buildTree(null, thingClass);
        reasoner.clearOntologies();
    } catch (UnknownOWLOntologyException e) {
        e.printStackTrace();
    } catch (OWLReasonerException e) {
        e.printStackTrace();
    }
}

private void buildTree(Node parentNode, OWLEntity currentEntity)
        throws OWLReasonerException {
    Node currentNode = null;
    if (currentEntity instanceof OWLClass) {
        OWLClass currentClass = (OWLClass) currentEntity;
        if (reasoner.isSatisfiable(currentClass)) {
            if (parentNode == null) {
                currentNode = tree.addRoot();
            } else {
                currentNode = tree.addChild(parentNode);
            }
            usedClasses.add(currentClass.getURI().toString());
            org.pg.eti.kask.sova.nodes.Node node =null;
            
            if (currentClass.isOWLThing()){
                node = new org.pg.eti.kask.sova.nodes.ThingNode();
                node.setLabel("T");
            }else{
                node = new org.pg.eti.kask.sova.nodes.ClassNode();
                node.setLabel(currentClass.toString());
            }
            currentNode.set(Constants.TREE_NODES, node);
            Set<OWLClass> subClasses = OWLReasonerAdapter
                .flattenSetOfSets(reasoner.getSubClasses(currentClass));

            for (OWLClass child : subClasses) {
                buildTree(currentNode, child);
            }

            Set<OWLIndividual> individuals = reasoner.getIndividuals(
                    currentClass, true);

            for (OWLIndividual ind : individuals) {
                buildTree(currentNode, ind);
            }

        }
    } else if (currentEntity instanceof OWLIndividual) {
        OWLIndividual currentIndividual = (OWLIndividual) currentEntity;

        if (!currentIndividual.isAnonymous()) {
            currentNode = tree.addChild(parentNode);
            org.pg.eti.kask.sova.nodes.Node node = new IndividualNode();
            node.setLabel(currentIndividual.toString());
            currentNode.set(Constants.TREE_NODES, node);
        }
    }
}
