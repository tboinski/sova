package uk.ac.manchester.cs.bhig.util;

import java.io.PrintWriter;
import java.util.*;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 22-Jan-2008<br><br>
 */
public class MutableTree<N> implements Tree<N> {

    private N userObject;

    private MutableTree<N> parent;

    private List<MutableTree<N>> children;


    public MutableTree(N userObject) {
        this.userObject = userObject;
        children = new ArrayList<MutableTree<N>>();
    }

    public N getUserObject() {
        return userObject;
    }


    public void setParent(MutableTree<N> parent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        this.parent = parent;
        this.parent.children.add(this);
    }


    public void addChild(MutableTree<N> child) {
        children.add(child);
        child.parent = this;
    }


    public void removeChild(MutableTree<N> child) {
        children.remove(child);
        child.parent = null;
    }


    public void sortChildren(Comparator<Tree<N>> comparator) {
        Collections.sort(children, comparator);
    }


    public void clearChildren() {
        for(MutableTree<N> child : new ArrayList<MutableTree<N>>(children)) {
            removeChild(child);
        }
    }


    public Tree<N> getParent() {
        return parent;
    }


    public List<Tree<N>> getChildren() {
        return new ArrayList<Tree<N>>(children);
    }


    public int getChildCount() {
        return children.size();
    }


    public boolean isRoot() {
        return parent == null;
    }


    public boolean isLeaf() {
        return children.isEmpty();
    }


    public Tree<N> getRoot() {
        if(parent == null) {
            return this;
        }
        return parent.getRoot();
    }


    public List<Tree<N>> getPathToRoot() {
        List<Tree<N>> path = new ArrayList<Tree<N>>();
        path.add(0, this);
        Tree<N> par = parent;
        while(par != null) {
            path.add(0, par);
            par = par.getParent();
        }
        return path;
    }


    public List<N> getUserObjectPathToRoot() {
        List<N> path = new ArrayList<N>();
        path.add(0, this.getUserObject());
        Tree<N> par = parent;
        while(par != null) {
            path.add(0, par.getUserObject());
            par = par.getParent();
        }
        return path;
    }


    public Set<N> getUserObjectClosure() {
        Set<N> objects = new HashSet<N>();
        getUserObjectClosure(this, objects);
        return objects;
    }

    private void getUserObjectClosure(Tree<N> tree, Set<N> bin) {
        bin.add(tree.getUserObject());
        for(Tree<N> child : tree.getChildren()) {
            getUserObjectClosure(child, bin);
        }
    }


    public void dump(PrintWriter writer) {
        dump(writer, 0);
    }

    public void dump(PrintWriter writer, int indent) {
        int depth = getPathToRoot().size();
        for(int i = 0; i < depth + indent; i++) {
            writer.print("\t");
        }
        writer.println(toString());
        for(Tree<N> child : getChildren()) {
            child.dump(writer, indent);
        }
        writer.flush();
    }

    public void replace(MutableTree<N> tree) {
        parent.children.remove(this);
        parent.children.add(tree);
        parent = null;
        tree.children.clear();
        tree.children.addAll(children);
        children.clear();
    }


    public String toString() {
        return userObject.toString();
    }
}
