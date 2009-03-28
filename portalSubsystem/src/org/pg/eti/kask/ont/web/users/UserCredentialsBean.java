package org.pg.eti.kask.ont.web.users;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.pg.eti.kask.ont.common.User;
import org.pg.eti.kask.ont.enterprise.frontend.SemWebPortal;
import org.pg.eti.kask.ont.enterprise.internal.SessionManager;

public class UserCredentialsBean implements Serializable{
	
	private static final long serialVersionUID = 867172806390714431L;

	private String username;
	
	private String password;
	
	private boolean admin;
	
	private boolean accepted;
	
	private boolean loggedIn;
	
	private SemWebPortal portal;
	
	
	
	public UserCredentialsBean() {
		
		this.username =new String();
		this.password = new String();
		this.accepted = false;
		this.admin = false;
		this.loggedIn = false;
	}
	
	public String userLogin() {
		//dostanie sie do bean'a
		Properties prop=new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		prop.put("java.naming.provider.url", "ejbd://localhost:4201");

		
		Context context;
		try {
			context = new InitialContext(prop);
			portal = (SemWebPortal)context.lookup("SemWebPortalImplRemote");
			
			SessionManager session = portal.login(username, password);
			User loggedUser = session.getUserManager().getUser(username);
			
			if(loggedUser.getIsAccepted() != null) {
				this.accepted = loggedUser.getIsAccepted().booleanValue();
			}
			if(loggedUser.getIsAdmin()!=null) {
				this.admin = loggedUser.getIsAdmin().booleanValue();
			}
			this.loggedIn = true;
			
			//tylko na potrzby serwletu generujacego plik jnlp
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.getExternalContext().getSessionMap().put("user", loggedUser);			
			
			return "loginSucceded";
		} catch (Exception e) {
			
		}
		
		this.username = new String();
		this.password = new String();
		return "loginFailure";
	}
	
	public void userLogout(ActionEvent event) {
		//usuniecie obiketu zalogowanego uzytkownika z sesji
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getExternalContext().getSessionMap().put("user", null);
		
		this.username = "";
		this.password = "";
		this.admin = false;
		this.accepted = false;
		this.loggedIn = false;
		
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			String contextPath = context.getExternalContext().getRequestContextPath();
			context.getExternalContext().redirect(contextPath+"/pages/home.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void validateUserName(FacesContext context, UIComponent component, Object value) {
		//inicjalizacja resource bundle'a
		FacesContext ctx = FacesContext.getCurrentInstance();
		ResourceBundle errorMessages = ctx.getApplication().getResourceBundle(ctx, "errors");
		String username = (String)value;
		if(username.trim().equals("")) {
			((UIInput)component).setValid(false);
			((UIInput)component).setValue("");
			context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("fieldRequired.text")));
			return;
		} 
	}
	
	public void validatePassword(FacesContext context, UIComponent component, Object value) {
		//inicjalizacja resource bundle'a
		FacesContext ctx = FacesContext.getCurrentInstance();
		ResourceBundle errorMessages = ctx.getApplication().getResourceBundle(ctx, "errors");
		String password = (String)value;
		if(password.trim().equals("")) {
			((UIInput)component).setValid(false);
			((UIInput)component).setValue("");
			context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("fieldRequired.text")));
			return;
		} 
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
}
