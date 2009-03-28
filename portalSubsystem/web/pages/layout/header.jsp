<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<div id="header">
	<div style="padding-top: 3px; float: right; font-size: 0.6em;">
		<h:outputText value="#{communicates['userLoggedIn.text']}" rendered="#{userCredentialsBean.loggedIn}"></h:outputText>
		<h:outputText value="#{userCredentialsBean.username}" rendered="#{userCredentialsBean.loggedIn}" styleClass="bold"></h:outputText>
		<h:outputText value=". " rendered="#{userCredentialsBean.loggedIn}"></h:outputText>
		<h:commandLink rendered="#{userCredentialsBean.loggedIn}" actionListener="#{userCredentialsBean.userLogout}" immediate="true">
			<h:outputText value="#{communicates['logout.text']}" styleClass="bold"></h:outputText>
		</h:commandLink>
				
		<h:outputText value="#{communicates['userNotLoggegIn.text']}" rendered="#{!userCredentialsBean.loggedIn}"></h:outputText>
		<h:outputLink value="#{request.contextPath}/pages/login.jsf" rendered="#{!userCredentialsBean.loggedIn}">
			<h:outputText value="#{communicates['login.text']}" styleClass="bold"></h:outputText>
		</h:outputLink>
	</div>
	<div class="logo">
		<span class="bold">OCS</span> - 
		<span class="bold">O</span>ntology <span class="bold">C</span>reation  
		<span class="bold">S</span>ystem		
	</div>
	<div style="font-size: 0.6em; text-align: right; float: right; padding-top: 7px">
		<h:outputText value="English version" rendered="#{facesContext.viewRoot.locale.language == 'en'}" styleClass="bold"></h:outputText> 
		<h:commandLink id="en" actionListener="#{changeLocaleBean.changeLocale}" 
			rendered="#{facesContext.viewRoot.locale.language == 'pl'}" immediate="true">
			<h:outputText value="English version"></h:outputText>
		</h:commandLink>
		<h:outputText> | </h:outputText>
		<h:outputText value="Wersja polska"	rendered="#{facesContext.viewRoot.locale.language == 'pl'}"	styleClass="bold"></h:outputText> 
		<h:commandLink id="pl" actionListener="#{changeLocaleBean.changeLocale}" 
			rendered="#{facesContext.viewRoot.locale.language == 'en'}" immediate="true">
			<h:outputText value="Wersja polska"></h:outputText>
		</h:commandLink>
	</div>
	<div id="navigation">
		<div class="padded_container" >			
			<ul>
				<li>
					<h:outputLink value="#{request.contextPath}/pages/home.jsf">
						<h:outputText value="#{communicates['home.text']}"></h:outputText>
					</h:outputLink>
				</li>				
				<li>
					<h:outputLink value="#{request.contextPath}/pages/about.jsf">
						<h:outputText value="#{communicates['about.text']}"></h:outputText>
					</h:outputLink>
				</li>
				<h:outputText rendered="#{userCredentialsBean.accepted}">
					<li>
						<h:outputLink value="#{request.contextPath}/pages/user/ocs-gui.jnlp">
							<h:outputText value="#{communicates['download.text']}"></h:outputText>
						</h:outputLink>
					</li>
				</h:outputText>
				<h:outputText rendered="#{userCredentialsBean.accepted}">
					<li>
						<h:outputLink value="#{request.contextPath}/pages/user/ontologiesList.jsf">
							<h:outputText value="#{communicates['ontologies.text']}"></h:outputText>
						</h:outputLink>
					</li>
				</h:outputText>
				<h:outputText rendered="#{userCredentialsBean.accepted && userCredentialsBean.admin}">
					<li>
						<h:outputLink value="#{request.contextPath}/pages/admin/usersList.jsf" >
							<h:outputText value="#{communicates['users.text']}"></h:outputText>
						</h:outputLink>
					</li>
				</h:outputText>
				<h:outputText rendered="#{userCredentialsBean.accepted}">
					<li>
						<h:outputLink value="#{request.contextPath}/jforum" >
							<h:outputText value="#{communicates['forum.text']}"></h:outputText>
						</h:outputLink>
					</li>
				</h:outputText>
			</ul>
		</div>
	</div>
</div>