package org.pg.eti.kask.ont.web.ontologies;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.pg.eti.kask.ont.common.BaseURI;
import org.pg.eti.kask.ont.common.User;
import org.pg.eti.kask.ont.enterprise.constants.Errors;
import org.pg.eti.kask.ont.enterprise.frontend.SemWebPortal;
import org.pg.eti.kask.ont.enterprise.internal.SessionManager;
import org.pg.eti.kask.ont.web.users.UserCredentialsBean;

public class AddOntologyExpertsBean implements Serializable {
	
	private static final long serialVersionUID = 6658521053742299138L;

	private String baseURI;
	
	private List<String> expertsNames;
	
	private List<SelectItem> users;
	
	private SemWebPortal portal;
	
	private SessionManager manager;
	
	private UserCredentialsBean credentials;
	
	public AddOntologyExpertsBean() {
		this.baseURI = new String();
		this.users = new ArrayList<SelectItem>();
		this.expertsNames = new ArrayList<String>();
		
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
			
			baseURI = facesCtx.getExternalContext().getRequestParameterMap().get("ontologyURI");
			
			List<String> experts = manager.getOntologyManager().getOntologyExperts(new BaseURI(baseURI));
			
			List<User> allUsers = manager.getUserManager().getUsers();
			
			for(User user: allUsers) {
				if(!experts.contains(user.getUsername())) {
					SelectItem si =new SelectItem(new String(user.getUsername()), user.getUsername());
					
					users.add(si);
				}
			}			
											
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
	
	public String submit() throws RemoteException{		
		for(String expertName: expertsNames) {
			manager.getOntologyManager().addOntologyExpert(expertName, new BaseURI( baseURI));
		}
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		facesCtx.getExternalContext().getSessionMap().put("ontologiesSelectedTab", new Integer(1));
		return "success";
	}

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	public List<String> getExpertsNames() {
		return expertsNames;
	}

	public void setExpertsNames(List<String> expertsNames) {
		this.expertsNames = expertsNames;
	}

	public List<SelectItem> getUsers() {
		return users;
	}

	public void setUsers(List<SelectItem> users) {
		this.users = users;
	}

}
