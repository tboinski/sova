<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['login.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body>
			<h:form id="form1">
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body">
					<h2 style="text-align: center;"><h:outputText value="#{communicates['loggingIn.text']}"></h:outputText></h2>
					<br />
					<div style="text-align: center;">
						<h:outputText value="#{communicates['notSignedIn.text']}"></h:outputText>
						<h:outputLink value="#{request.contextPath}/pages/register.jsf">
							<h:outputText value="#{communicates['register.text']}"></h:outputText>
						</h:outputLink>.
					</div>
					<br /><br />
					
					<table style="width: 100%;">
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['username.text']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="login" value="#{userCredentialsBean.username}" required="true"
									requiredMessage="#{errors['fieldRequired.text']}" validator="#{userCredentialsBean.validateUserName}" >
								</h:inputText><br />
								<h:message for="login" styleClass="red_text"></h:message>
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['password.text']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputSecret id="password" value="#{userCredentialsBean.password}" required="true" 
									requiredMessage="#{errors['fieldRequired.text']}"  validator="#{userCredentialsBean.validatePassword}" >
								</h:inputSecret><br />
								<h:message for="password" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>	
							<td></td>						
							<td class="left_aligned">
								<h:commandButton id="send" value="#{communicates['loginButton.text']}" action="#{userCredentialsBean.userLogin}"></h:commandButton>
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
