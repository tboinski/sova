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
						<h2 style="text-align: center;">Rejestracja powiodła się</h2>						
						<p style="text-align: center;">
						Rejestracja powiodła się.
						</p> 
						<p style="text-align: center;">
						Proszę oczekiwać na zatwierdzenie konta przez <span class="bold">Administratora</span>.
						</p>
					</h:panelGroup>
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'en'}">
						<h2 style="text-align: center;">Registration successful</h2>						
						<p style="text-align: center;">
						Registration succedded.
						</p>
						<p style="text-align: center;"> 
						Please wait until <span class="bold">Administrator</span> will activate your account.
						</p>
					</h:panelGroup>
					
					
				</div>
				
				<!-- footer -->
				
				<%@ include file="/pages/layout/footer.jsp" %>
				
			</div>
			</h:form>			
		
		</body>
	</f:view>
</html>
