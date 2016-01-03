/*
 *
 * Copyright (c) 2010 Gda?sk University of Technology
 * Copyright (c) 2010 Kunowski Piotr
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

package org.pg.eti.kask.ont.pluginSova;

import org.protege.editor.core.ui.workspace.WorkspaceViewsTab;
import org.protege.editor.owl.model.OWLEntityDisplayProvider;
import org.protege.editor.core.ProtegeProperties;
import org.protege.editor.core.ui.view.View;
import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.view.ViewComponentPlugin;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent;
import org.semanticweb.owlapi.model.*;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;


public class pluginSovaTab extends WorkspaceViewsTab {

    private Set<ViewComponentPlugin> viewPlugins = new HashSet<ViewComponentPlugin>();


    private OWLEntityDisplayProvider provider = new OWLEntityDisplayProvider() {
        public boolean canDisplay(OWLEntity owlEntity) {
            return pluginSovaTab.this.canDisplay(owlEntity);
        }

        public JComponent getDisplayComponent() {
            return pluginSovaTab.this;
        }
    };


    private boolean canDisplay(OWLEntity owlEntity) {

        String entityCat = new NavFinder().getNav(owlEntity);

        // search the contained views to see if there is one that can show the entity
        for (View view : getViewsPane().getViews()){
            ViewComponent vc = view.getViewComponent();
            if (vc != null){ // if the view is on a tab that has been initialised ask it directly
                if (vc instanceof AbstractOWLSelectionViewComponent){
                    final AbstractOWLSelectionViewComponent owlEntityViewComponent = (AbstractOWLSelectionViewComponent)vc;
                    if (owlEntityViewComponent.canShowEntity(owlEntity)){
                        return true;
                    }
                }
            }
            else { // otherwise, ask its plugin
                ViewComponentPlugin plugin = getWorkspace().getViewManager().getViewComponentPlugin(view.getId());
                if (plugin != null) {
                    for (String nav : plugin.getNavigates()){
                        if (entityCat.equals(nav)){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void initialise() {
        super.initialise();
        getOWLEditorKit().getWorkspace().registerOWLEntityDisplayProvider(provider);
    }


    public void dispose() {
        getOWLEditorKit().getWorkspace().unregisterOWLEntityDisplayProvider(provider);
        super.dispose();
    }


    public OWLModelManager getOWLModelManager() {
        return (OWLModelManager) getWorkspace().getEditorKit().getModelManager();
    }


    public OWLEditorKit getOWLEditorKit() {
        return (OWLEditorKit) getWorkspace().getEditorKit();
    }


    class NavFinder implements OWLEntityVisitor{

        private String nav;


        public String getNav(OWLEntity owlEntity) {
            nav = null;
            owlEntity.accept(this);
            return nav;
        }


        public void visit(OWLClass owlClass) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.CLASS_VIEW_CATEGORY);
        }


        public void visit(OWLObjectProperty owlObjectProperty) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.OBJECT_PROPERTY_VIEW_CATEGORY);
        }


        public void visit(OWLDataProperty owlDataProperty) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.DATA_PROPERTY_VIEW_CATEGORY);
        }


        public void visit(OWLIndividual owlIndividual) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.INDIVIDUAL_VIEW_CATEGORY);
        }


        public void visit(OWLDatatype owlDataType) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.DATATYPE_VIEW_CATEGORY);
        }

        public void visit(OWLNamedIndividual owlni) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.INDIVIDUAL_VIEW_CATEGORY);
        }

        public void visit(OWLAnnotationProperty owlap) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.ANNOTATION_PROPERTY_VIEW_CATEGORY);
        }
    }
}
