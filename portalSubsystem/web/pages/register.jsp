<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['register.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body>
			<h:form id="form1" >
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body" >
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'pl'}">
						<h2 style="text-align: center;">Rejestracja nowego użytkownika</h2>						
						<p style="text-align: center;">
						Proszę wypełnić <span class="bold">wszystkie pola</span>.
						</p> 
						<p style="text-align: center;">
						Po zatwierdzeniu przez <span class="bold">Administratora</span>
						możliwe będzie <span class="bold">zalogowanie</span>.
						</p>
					</h:panelGroup>
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'en'}">
						<h2 style="text-align: center;">Registering new user</h2>						
						<p style="text-align: center;">
						Please enter all <span class="bold">valid data</span>.
						</p>
						<p style="text-align: center;"> 
						After approving your registration form by
						<span class="bold">Administrator</span>, you will be able to <span class="bold">log in</span>.
						</p>
					</h:panelGroup>
					<table style="width: 100%;">
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['username.text']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="userName" value="#{registerUserBean.userName}" required="true"
									requiredMessage="#{errors['fieldRequired.text']}" validator="#{registerUserBean.validateUserName}" 
									size="40">
								</h:inputText><br />
								<h:message for="userName" styleClass="red_text"></h:message>
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['password.text']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputSecret id="password" value="#{registerUserBean.password}" required="true" 
									requiredMessage="#{errors['fieldRequired.text']}"  validator="#{registerUserBean.validatePassword}"
									size="40">
								</h:inputSecret><br />
								<h:message for="password" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['confirmPassword.text']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputSecret id="confirmedPassword" required="true" requiredMessage="#{errors['fieldRequired.text']}" 
									value="#{registerUserBean.confirmedPassword}" validator="#{registerUserBean.validatePassword}" 
									size="40">
								</h:inputSecret><br />
								<h:message for="confirmedPassword" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['realName.text']}" ></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="realName" value="#{registerUserBean.realName}" required="true" 
									requiredMessage="#{errors['fieldRequired.text']}" size="40">
								</h:inputText><br />
								<h:message for="realName" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['email.text']}" ></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="email" value="#{registerUserBean.email}" required="true" 
									requiredMessage="#{errors['fieldRequired.text']}" validator="#{registerUserBean.validateEmail}" 
									size="40">
								</h:inputText><br />
								<h:message for="email" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>	
							<td></td>						
							<td class="left_aligned">
								<h:commandButton id="send" value="#{communicates['sendButton.text']}" action="#{registerUserBean.submit}"></h:commandButton>
							</td>
						</tr>
					</table>
					
				</div>
				
				<!-- footer -->
				
				<%@ include file="/pages/layout/footer.jsp" %>
				
			</div>
			</h:form>			
		
		</body>
	</f:view>
</html>
