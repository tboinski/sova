<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>
<%@ taglib uri="/WEB-INF/tld/tomahawk.tld" prefix="t" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['addOntologyExperts.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body onload="loadFields();">
			<h:form id="form1">			
			<script type="text/javascript">
				function loadFields() {
					var baseURI = document.getElementById('form1:baseURI');
					var baseURIHidden = document.getElementById('form1:baseURIHidden');
					var title = document.getElementById('form1:title1');
					if(title == null) {
						title = document.getElementById('form1:title2');
					}					
					baseURI.value=baseURIHidden.value;
					title.innerHTML=baseURIHidden.value;
				}
			</script>
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body">
					
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'pl'}">
						<h2 style="text-align: center;">Dodawanie ekspertów do ontologii - <h:outputText id="title1" value="#{addOntologyExpertsBean.baseURI}"></h:outputText></h2>																		
					</h:panelGroup>
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'en'}">
						<h2 style="text-align: center;">Adding experts to ontology - <h:outputText id="title2" value="#{addOntologyExpertsBean.baseURI}"></h:outputText></h2>												
					</h:panelGroup>
				
					<t:saveState value="#{addOntologyExpertsBean.users}"></t:saveState>
					<t:saveState value="#{addOntologyExpertsBean.expertsNames}"></t:saveState>
					<t:saveState value="#{addOntologyExpertsBean.baseURI}"></t:saveState>
					<table style="width: 100%;">
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['ontologies.baseURI.fieldName']}"></h:outputText>
								</span>
							</td>
							<td>
								<h:inputText id="baseURI" disabled="true" style="width: 300px;">
								</h:inputText>
								<h:inputHidden id="baseURIHidden" value="#{addOntologyExpertsBean.baseURI}"></h:inputHidden>
								<br />
							</td>
						</tr>
						
						<tr>
							<td class="right_aligned" style="width: 50%;">
								<span class="bold">
								<h:outputText value="#{communicates['ontologies.users.fieldName']}" ></h:outputText>
								</span>
							</td>
							<td>
								<t:selectManyListbox id="users" value="#{addOntologyExpertsBean.expertsNames}"
                             		size="5" style="width: 200px;" >
                					<f:selectItems value="#{addOntologyExpertsBean.users}"/>                					
            					</t:selectManyListbox>            					
							</td>
						</tr>						
						<tr>	
							<td></td>						
							<td class="left_aligned">								
								<h:commandButton id="send" value="#{communicates['sendButton.text']}" action="#{addOntologyExpertsBean.submit}"></h:commandButton>								
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
