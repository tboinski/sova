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
					
					<br />
					<div style="text-align: center;">
						<h:outputText value="#{errors['loginFailure.text']}" style="color: #FF0000; font-size:12pt;"></h:outputText><br /><br />
						<h:outputText value="#{errors['loginFailureDescription.text']}"></h:outputText><br /><br />
						
						
						<h:outputText value="#{communicates['goToLoginPage.text1']}"></h:outputText>
						<h:outputLink value="#{request.contextPath}/pages/login.jsf" >
							<h:outputText value="#{communicates['loginButton.text']}" ></h:outputText>
						</h:outputLink>&nbsp;
						<h:outputText value="#{communicates['goToLoginPage.text2']}" ></h:outputText>
					</div>
					
					
										
				</div>
				
				<!-- footer -->
				
				<%@ include file="/pages/layout/footer.jsp" %>
				
			</div>
			</h:form>			
		
		</body>
	</f:view>
</html>
