package org.pg.eti.kask.ont.web.ontologies;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.enterprise.constants.Errors;
import org.pg.eti.kask.ont.enterprise.frontend.SemWebPortal;
import org.pg.eti.kask.ont.enterprise.internal.SessionManager;
import org.pg.eti.kask.ont.web.ontologies.model.Ontology;
import org.pg.eti.kask.ont.web.ontologies.model.OntologyExpertsModel;
import org.pg.eti.kask.ont.web.users.UserCredentialsBean;

public class OntologiesListBean  {
		
	private List<Ontology> allOntologies;
	
	private List<BaseURI> subscribedOntologies;
	
	private List<OntologyExpertsModel> myOntologies;
	
	private SemWebPortal portal;
	
	private SessionManager manager;
	
	private UserCredentialsBean credentials;
	
	
	public OntologiesListBean() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		Properties prop=new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		prop.put("java.naming.provider.url", "ejbd://localhost:4201");

		Context context;
		try {
			context = new InitialContext(prop);
			portal = (SemWebPortal)context.lookup("SemWebPortalImplRemote");
			
			
			credentials = (UserCredentialsBean)facesCtx.getExternalContext().getSessionMap().get("userCredentialsBean");
			
			manager = portal.login(credentials.getUsername(), credentials.getPassword());
			
			initializeAllOntologies();
			
			initializeMyOntologies();
			
			initializeSubscribedOntologies();	
				
											
		} catch (Exception e) {
			//jesli bleden uprawnienia przekieruj na strone logowania
			if(e.getMessage().equals(Errors.INVALID_CREDENTIALS)) {
				try {
					facesCtx.getExternalContext().redirect(facesCtx.getExternalContext().getRequestContextPath()+"/pages/login.jsf");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} 
	}
	
	public void initializeAllOntologies() throws RemoteException {
		this.allOntologies = new ArrayList<Ontology>();
		List<BaseURI> ontologyBaseUri = manager.getOntologyManager().getBaseOntologyURIs();
		List<String> subscriptions = manager.getUserManager().getOntologySubscripions(credentials.getUsername());			
		
		for(BaseURI uri: ontologyBaseUri) {
			Ontology ont = new Ontology(uri, manager.getOntologyManager().getOntologyVersions(uri.getBaseURI().getURIAsString()));
							
			if(subscriptions.contains(uri.getURIAsString())) {
				ont.setSubscribed(true);
			} else {
				ont.setSubscribed(false);
			}									
			allOntologies.add(ont);
		}
	}
	
	public void initializeSubscribedOntologies() throws RemoteException {
		this.subscribedOntologies = new ArrayList<BaseURI>();		
		List<String> subs = manager.getUserManager().getOntologySubscripions(credentials.getUsername());
		
		List<BaseURI> allBaseUris = manager.getOntologyManager().getBaseOntologyURIs();
		
		for(BaseURI uri: allBaseUris) {
			if(subs.contains(uri.getURIAsString())) {
				subscribedOntologies.add(uri);
			}
		}		
	}
	
	public void initializeMyOntologies() throws RemoteException{
		this.myOntologies = new ArrayList<OntologyExpertsModel>();
		List<BaseURI> ontologies = manager.getOntologyManager().getOntologiesByOwner(credentials.getUsername());
		
		for(BaseURI uri : ontologies) {
			List<String> expertsNames = manager.getOntologyManager().getOntologyExperts(uri);
			myOntologies.add(new OntologyExpertsModel(uri, expertsNames));
		}
	}
	
	/**
	 * Akcja wykonywana w ramach subkskrypcji danej ontologii. 
	 * @param evt
	 * @throws RemoteException
	 */
	public void subscribeOntology(ActionEvent evt) throws RemoteException {
		//pobranie baseUri
		String baseUri = (String)evt.getComponent().getAttributes().get("baseURI");
		manager.getUserManager().addOntologySubscripion(baseUri, credentials.getUsername());
		initializeAllOntologies();
		initializeSubscribedOntologies();
	}
	
	/**
	 * Akcja wykonywana w ramach subkskrypcji danej ontologii. 
	 * @param evt
	 * @throws RemoteException
	 */
	public void unSubscribeOntology(ActionEvent evt) throws RemoteException {
		//pobranie baseUri
		String baseUri = (String)evt.getComponent().getAttributes().get("baseURI");
		manager.getUserManager().deleteOntologySubscription(baseUri, credentials.getUsername());
		initializeAllOntologies();
		initializeSubscribedOntologies();
	}
	
	/**
	 * 
	 * @param evt
	 * @throws RemoteException
	 */
	public void removeOntologyExpert(ActionEvent evt) throws RemoteException {
		String baseUri = (String)evt.getComponent().getAttributes().get("baseURI");
		String userName = (String)evt.getComponent().getAttributes().get("userName");
		manager.getOntologyManager().removeOntologyExpert(userName, new BaseURI(baseUri));		
		initializeMyOntologies();
	}
	
	public List<Ontology> getAllOntologies() {
		return allOntologies;
	}
	
	public void setAllOntologies(List<Ontology> ontologies) {				
		this.allOntologies = ontologies;
	}

	public List<BaseURI> getSubscribedOntologies() throws RemoteException {
		return subscribedOntologies;
	}

	public void setSubscribedOntologies(List<BaseURI> subscribedOntologies) {
		this.subscribedOntologies = subscribedOntologies;
	}

	public List<OntologyExpertsModel> getMyOntologies() {
		return myOntologies;
	}

	public void setMyOntologies(List<OntologyExpertsModel> myOntologies) {
		this.myOntologies = myOntologies;
	}

	public Integer getSelectedTab() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		Integer selectedTab = (Integer)facesCtx.getExternalContext().getSessionMap().get("ontologiesSelectedTab");
		facesCtx.getExternalContext().getSessionMap().remove("ontologiesSelectedTab");
		if(selectedTab != null)
			return selectedTab;
		return 0;
	}
	
}