package org.pg.eti.kask.ont.web.users;

import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.pg.eti.kask.ont.common.User;
import org.pg.eti.kask.ont.enterprise.constants.Errors;
import org.pg.eti.kask.ont.enterprise.frontend.SemWebPortal;


public class RegisterUserBean {

	private String userName;
	
	private String password;
	
	private String confirmedPassword;
	
	private String realName;
	
	private String email;
	
	private SemWebPortal portal;
	
	private ResourceBundle errorMessages;
	
	public RegisterUserBean() {
		//inicjalizacja resource bundle'a
		FacesContext ctx = FacesContext.getCurrentInstance();
		this.errorMessages = ctx.getApplication().getResourceBundle(ctx, "errors");
		
		//dostanie sie do bean'a
		Properties prop=new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
		prop.put("java.naming.provider.url", "ejbd://localhost:4201");

		Context context;
		try {
			context = new InitialContext(prop);
			portal = (SemWebPortal)context.lookup("SemWebPortalImplRemote");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void validateUserName(FacesContext context, UIComponent component, Object value) {
		String username = (String)value;
		if(username.trim().equals("")) {
			((UIInput)component).setValid(false);
			((UIInput)component).setValue("");
			context.addMessage(component.getClientId(context), new FacesMessage(errorMessages.getString("fieldRequired.text")));
			return;
		} else {
			//sprawdzenie czy uzytkownik o takim username juz istnieje
		}
	}
	
	public void validatePassword(FacesContext context, UIComponent component, Object value) {
		
		if(component.getId().equals("password")) {
			//jesli to komponent z haslem to zapisz haslo do sesji
			context.getExternalContext().getSessionMap().put("password", value);
		} else if(component.getId().equals("confirmedPassword")) {
			//pobranie hasla poprzednio wrzuconego do sesji
			//i sprawdzenie z wartoscia w kontrolce confirmedPassword
			String enteredPass = (String)context.getExternalContext().getSessionMap().get("password");
			
			context.getExternalContext().getSessionMap().put("password", null);
			if(!enteredPass.equals(value)) {
				UIInput componentPassword = (UIInput)context.getViewRoot().findComponent("form1").findComponent("password"); 
				componentPassword.setValid(false);
				
				context.addMessage(componentPassword.getClientId(context), new FacesMessage(errorMessages.getString("passwordsDontMatch.text")));
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
	
	public String submit() {
			
		User newUser = new User();

		newUser.setUsername(this.userName);
		newUser.setEmail(this.email);
		newUser.setIsAccepted(new Boolean(false));
		newUser.setIsAdmin(new Boolean(false));
		newUser.setRealName(this.realName);
		newUser.setPasswd(this.password);
		newUser.setOntLeft(new Integer(10));
		newUser.setLastLoginDate(Calendar.getInstance().getTime());

		try {
			portal.createUser(newUser);
		} catch (Exception e) {
			if(e.getMessage().equals(Errors.USER_ALREADY_EXISTS)) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				UIInput usernameInput = (UIInput )ctx.getViewRoot().findComponent("form1").findComponent("userName");
				ctx.addMessage(usernameInput.getClientId(ctx), new FacesMessage(errorMessages.getString("userAlereadyExists.text")));
			}
			return "";
		}

		return "success";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}
}
