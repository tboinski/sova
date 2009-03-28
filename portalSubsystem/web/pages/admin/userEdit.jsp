<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['users.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body onload="loadFields();">
			<h:form id="form1">
			<script type="text/javascript">
				function loadFields() {
					var userNameHidden = document.getElementById('form1:userNameHidden');
					var userName = document.getElementById('form1:userName');
					var title = document.getElementById('form1:title1');
					if(title == null) {
						title = document.getElementById('form1:title2');
					}
					userName.value=userNameHidden.value;
					title.innerHTML=userNameHidden.value;
				}
			</script>
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body">
				
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'pl'}">
						<h2 style="text-align: center;">Edycja użytkownika <h:outputText id="title1" value="#{userEditBean.userName}"></h:outputText></h2>																		
					</h:panelGroup>
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'en'}">
						<h2 style="text-align: center;">User edition <h:outputText id="title2" value="#{userEditBean.userName}"></h:outputText></h2>												
					</h:panelGroup>
					
					<table style="width: 100%;">
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['username.text']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="userName" disabled="true">
								</h:inputText>
								<h:inputHidden id="userNameHidden" value="#{userEditBean.userName}"></h:inputHidden>
								<br />
							</td>
						</tr>
						
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['realName.text']}" ></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="realName" value="#{userEditBean.realName}" required="true" 
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
								<h:inputText id="email" value="#{userEditBean.email}" required="true" 
									requiredMessage="#{errors['fieldRequired.text']}" validator="#{userEditBean.validateEmail}" 
									size="40">
								</h:inputText><br />
								<h:message for="email" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['ontLeft.text']}" ></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="ontLeft" value="#{userEditBean.ontLeft}" required="true" 
									requiredMessage="#{errors['fieldRequired.text']}" validator="#{userEditBean.validateOntLeft}" 
									size="40" >
									
								</h:inputText><br />
								<h:message for="ontLeft" styleClass="red_text"></h:message>	
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['isAccepted.text']}" ></h:outputText>
								</span>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{userEditBean.isAccepted}"></h:selectBooleanCheckbox>
							</td>
						</tr>
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['isAdmin.text']}" ></h:outputText>
								</span>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{userEditBean.isAdmin}"></h:selectBooleanCheckbox>	
							</td>
						</tr>
						<tr>	
							<td></td>						
							<td class="left_aligned">
								<h:commandButton id="send" value="#{communicates['sendButton.text']}" action="#{userEditBean.submit}"></h:commandButton>
								&nbsp;&nbsp;<h:commandButton id="cancel" value="#{communicates['cancelButton.text']}" action="cancel" immediate="true"></h:commandButton>
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
