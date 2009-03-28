package org.pg.eti.kask.ont.web.users;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.pg.eti.kask.ont.common.User;
import org.pg.eti.kask.ont.enterprise.constants.Errors;
import org.pg.eti.kask.ont.enterprise.frontend.SemWebPortal;
import org.pg.eti.kask.ont.enterprise.internal.SessionManager;

public class UsersListBean {
	
	private SemWebPortal portal;

	private List<User> users;
	
	public UsersListBean() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		Properties prop=new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		prop.put("java.naming.provider.url", "ejbd://localhost:4201");

		Context context;
		try {
			context = new InitialContext(prop);
			portal = (SemWebPortal)context.lookup("SemWebPortalImplRemote");
			
			
			UserCredentialsBean cred = (UserCredentialsBean)facesCtx.getExternalContext().getSessionMap().get("userCredentialsBean");
			
			SessionManager manager = portal.login(cred.getUsername(), cred.getPassword());
			
			users = manager.getUserManager().getUsers();
			
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
