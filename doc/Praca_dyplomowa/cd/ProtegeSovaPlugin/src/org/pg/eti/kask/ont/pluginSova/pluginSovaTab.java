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
import org.semanticweb.owl.model.*;

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


        public void visit(OWLDataType owlDataType) {
            nav = ProtegeProperties.getInstance().getProperty(ProtegeProperties.DATATYPE_VIEW_CATEGORY);
        }
    }
}
