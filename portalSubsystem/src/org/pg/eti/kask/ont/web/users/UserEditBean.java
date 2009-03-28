package org.pg.eti.kask.ont.web.users;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.pg.eti.kask.ont.common.User;
import org.pg.eti.kask.ont.enterprise.constants.Errors;
import org.pg.eti.kask.ont.enterprise.frontend.SemWebPortal;
import org.pg.eti.kask.ont.enterprise.internal.SessionManager;

public class UserEditBean {
	
	private String userName;
	
	private String realName;
	
	private String email;
	
	private boolean isAccepted;
	
	private boolean isAdmin;
	
	private int ontLeft;
	
	private SemWebPortal portal;
	
	private ResourceBundle errorMessages;
	
	public UserEditBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		//inicjalizacja resource bundle'a
		this.errorMessages = facesContext.getApplication().getResourceBundle(facesContext, "errors");
		
		String userToEdit = (String)facesContext.getExternalContext().getRequestParameterMap().get("name");
		
		Properties prop=new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		prop.put("java.naming.provider.url", "ejbd://localhost:4201");

		Context context;
		try {
			context = new InitialContext(prop);
			portal = (SemWebPortal)context.lookup("SemWebPortalImplRemote");
			
			UserCredentialsBean cred = (UserCredentialsBean)facesContext.getExternalContext().getSessionMap().get("userCredentialsBean");
			
			SessionManager manager = portal.login(cred.getUsername(), cred.getPassword());
			
			User user = manager.getUserManager().getUser(userToEdit);
			
			this.userName = user.getUsername();
			this.realName = user.getRealName();
			this.email = user.getEmail();
			this.isAccepted = user.getIsAccepted() == null ? false : user.getIsAccepted();				
			this.isAdmin = user.getIsAdmin() == null ? false : user.getIsAdmin();  
			this.ontLeft = user.getOntLeft() == null ? 0 : user.getOntLeft();
		} catch (Exception e) {
			//jesli bleden uprawnienia przekieruj na strone logowania
			if(e.getMessage().equals(Errors.INVALID_CREDENTIALS)) {
				try {
					facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath()+"/pages/login.jsf");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void validateEmail(FacesContext context, UIComponent component, Object value) {
		String email = (String)value;
		
		if(email.trim().equals("")) {
			((UIInput)component).setValid(false);
			((UIInput)component).setValue("");
			context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("fieldRequired.text")));
		} else {
			if(!email.contains("@")) {
				((UIInput)component).setValid(false);
				((UIInput)component).setValue("");
				context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("wrongEmailFormat.text")));
			}
		}
			
	}
	
	public void validateOntLeft(FacesContext context, UIComponent component, Object value) {
		String ontLeft= ""+value;
		
		if(ontLeft.trim().equals("")) {
			((UIInput)component).setValid(false);
			((UIInput)component).setValue("");
			context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("fieldRequired.text")));
		} else {
			try {
				int onts=Integer.parseInt(ontLeft);
				if(onts<0 ||onts>100) {
					((UIInput)component).setValid(false);
					((UIInput)component).setValue("");
					context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("rangeError.text")));
				}
			} catch (Exception e) {
				((UIInput)component).setValid(false);
				((UIInput)component).setValue("");
				context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("wrongFormatOfNumber.text")));
			}
		}
			
	}
	
	public String submit() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UserCredentialsBean cred = (UserCredentialsBean)facesContext.getExternalContext().getSessionMap().get("userCredentialsBean");
		SessionManager manager;
		try {
			manager = portal.login(cred.getUsername(), cred.getPassword());
			
			User userToModify = manager.getUserManager().getUser(userName);
			userToModify.setEmail(email);
			userToModify.setIsAccepted(isAccepted);
			userToModify.setIsAdmin(isAdmin);
			userToModify.setOntLeft(ontLeft);
			userToModify.setRealName(realName);
			
			manager.getUserManager().modifyUser(userToModify);
			
			return "success";
		} catch (Exception e) {
			//jesli bleden uprawnienia przekieruj na strone logowania
			if(e.getMessage().equals(Errors.INVALID_CREDENTIALS)) {
				try {
					facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath()+"/pages/login.jsf");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		return "";
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getOntLeft() {
		return ontLeft;
	}

	public void setOntLeft(int ontLeft) {
		this.ontLeft = ontLeft;
	}
}
