<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['about.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body>
			<h:form id="form1">
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body">
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'pl'}">
					<p>						
						<h2 style="text-align:center;">Autorzy</h2>
						System powstał na <span class="bold"><a target="_blank" href="http://www.eti.pg.gda.pl/katedry/kask">Katedrze Architektury Systemów Komputerowych</a></span> na <span class="bold"><a target="_blank" href="http://www.eti.pg.gda.pl">Wydziale Elektroniki,
						Telekomunikacji i Informatyki</a></span>. W projekt głównie zaangażowani są:
						<ul>
							<li>dr inż. Paweł Czarnul</li>
							<li>mgr inż. Tomasz Boiński</li>
							<li>mgr inż. Łukasz Budnik</li>
							<li>Andrzej Jakowski</li>
							<li>Krzysztof Mazurkiewicz</li>
							<li>Jacek Mroziński</li>							
						</ul> 												
					</p>
					</h:panelGroup>
					
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'en'}">
					<p>						
						<h2 style="text-align:center;">The authors</h2>
						The OCS system is being developed in <span class="bold"><a target="_blank" href="http://www.eti.pg.gda.pl/katedry/kask">Computer Architecture Department</a></span> at <span class="bold"><a target="_blank" href="http://www.eti.pg.gda.pl">Faculty of Electronics, Telecommunications
						and Informatics</a></span>. People who attended in first release development:
						<ul>
							<li>Pawel Czarnul, Ph. D.</li>
							<li>Tomasz Boinski, M. Sc.</li>
							<li>Lukasz Budnik, M. Sc.</li>
							<li>Andrzej Jakowski</li>
							<li>Krzysztof Mazurkiewicz</li>
							<li>Jacek Mrozinski</li>							
						</ul> 												
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
