<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>
<%@ taglib uri="/WEB-INF/tld/tomahawk.tld" prefix="t" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['users.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body>
			<h:form id="form1">
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body">
					
					<h2 style="text-align: center;"><h:outputText value="#{communicates['usersList.text']}"></h:outputText></h2>
					<br />
					
					<t:dataTable value="#{usersListBean.users}" var="user" rowClasses="usersTable_oddrow, usersTable_evenrow" 
						styleClass="usersTable" columnClasses="usersTable_usernameCol, usersTable_adminCol,
						usersTable_ontLeftCol, usersTable_fullNameCol, usersTable_emailCol, usersTable_lastLoginCol,
						usersTable_isAcceptedCol, usersTable_editCol" headerClass="usersTable_header"
						sortable="true" >
						<h:column >
							<f:facet name="header" >
								<h:outputText value="#{communicates['username1.text']}"></h:outputText>
							</f:facet>
							<h:outputText value="#{user.username}"></h:outputText>
						</h:column>		
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['admin.text']}"></h:outputText>
							</f:facet>
							<h:selectBooleanCheckbox value="#{user.isAdmin}" disabled="true"></h:selectBooleanCheckbox>							
						</h:column>
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['ontLeft1.text']}"></h:outputText>
							</f:facet>
							<h:outputText value="#{user.ontLeft}"></h:outputText>
						</h:column>
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['fullName.text']}"></h:outputText>
							</f:facet>
							<h:outputText value="#{user.realName}"></h:outputText>
						</h:column>
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['email1.text']}"></h:outputText>
							</f:facet>
							<h:outputText value="#{user.email}"></h:outputText>
						</h:column>
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['lastLogin.text']}"></h:outputText>
							</f:facet>
							<h:outputText value="#{user.lastLoginDate}">
								<f:convertDateTime pattern="dd.MM.yyyy HH:mm:ss" timeZone="GMT+01:00"/>
							</h:outputText>
						</h:column>
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['isAccepted1.text']}"></h:outputText>
							</f:facet>
							<h:selectBooleanCheckbox value="#{user.isAccepted}" disabled="true"></h:selectBooleanCheckbox>
						</h:column>
						<h:column >
							<f:facet name="header">
								<h:outputText value="#{communicates['edit.text']}"></h:outputText>
							</f:facet>
							<h:outputLink value="#{request.contextPath}/pages/admin/userEdit.jsf">
								<f:param name="name" value="#{user.username}"></f:param>
								<h:outputText value="#{communicates['edit.text']}"></h:outputText>
							</h:outputLink>
						</h:column>
					</t:dataTable>
					
					
				</div>
				
				<!-- footer -->
				
				<%@ include file="/pages/layout/footer.jsp" %>
				
			</div>
			</h:form>			
		
		</body>
	</f:view>
</html>
