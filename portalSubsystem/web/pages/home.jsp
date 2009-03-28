<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}">
		<head>
			<title>${communicates['home.pageTitle']}</title>
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
					<div style="clear:both;">
						<a>
							<h:graphicImage styleClass="leftAlignedImage" url="/images/print-screen1.jpg"></h:graphicImage>
						</a>							
						<h2>OCS - pierwsza wersja jest już dostępna!</h2>
						<p>
							<span class="bold">OCS - Ontology Creation System</span> to system powstający na Katedrze Architektury 
							Systemów Komputerowych na Wydziale Elektroniki, Telekomunikacji i Informatyki.
							System służy do składowania i pozyskiwania ontologii. Dostęp do ontologii
							może się odbywać poprzez dedykowane narzędzie - edytor ontologiczny, bądź 
							przez załączenie odpowiedniej biblioteki do Twojego programu.						
						</p>						
						<p>
							OCS powstał w oparciu o platformę <span class="bold">JEE</span>. Natomiast ontologie zapisywane są 
							w postaci trójek RDF. Całość dostępna jest poprzez wygodne API, opierające się na 
							modelu obiektowym ontologii, przy wykorzystaniu biblioteki 
							<a target="_blank" href="http://owlapi.sourceforge.net/">OWL API</a>.
						</p>
					</div>
					<div style="clear:both;"></div>
					<div style="clear:both;">
						<a>
							<h:graphicImage styleClass="leftAlignedImage" url="/images/print-screen2.jpg"></h:graphicImage>
						</a>
						<h2>Zapraszamy</h2>
						<p>									
							System pozwala na <span class="bold">zespołową pracę</span> nad rozwojem ontologii. Nad całym procesem wprowadzania zmian
							czuwa właściciel ontologii wraz z ekspertem dziedzinowym. Obaj mogą tworzyć nowe wersje, jak również 
							blokować dostęp do ontologii. Wszystkie z powyżej opisanych funkcji dostępne są już w pierwszej 
							wersji systemu.
						</p>
						<p>	
							Zapraszamy wszystkich do korzystania z systemu.
							Jeśli chcesz pobrać aktualną wersję systemu, zarejestruj się! Wkrótce Administrator aktywuje 
							Twoje konto.
						</p>
						<p>	 
							Jeśli nie jesteś zainteresowany edytorem, bądź chcesz stworzyć swój własny,
							współpracujący z systemem OCS napisz do nas. Administrator udostępni Ci wszystkie wymagane 
							biblioteki.	Jeśli jeszcze nie masz konta to <h:outputLink value="#{request.contextPath}/pages/register.jsf">zarejestruj sie!</h:outputLink>
						</p>
					</div>		
					
					</h:panelGroup>
					
					
					<h:panelGroup rendered="#{facesContext.viewRoot.locale.language == 'en'}">					
					<div style="clear:both;">
						<a>
							<h:graphicImage styleClass="leftAlignedImage" url="/images/print-screen1.jpg"></h:graphicImage>
						</a>							
						<h2>OCS - first version is now available!</h2>
						<p>
							<span class="bold">OCS - Ontology Creation System</span> is being developed 
							in Computer Architecture Department at Faculty of Electronics, Telecommunications
							and Informatics. It is knowledge acquisition system, which was designed to provide
							common platform for accessing ontologies. You can access ontologies by using 
							dedicated software - ontology editor, or by including special library in
							your application. 
						</p>						
						<p>
							OCS is working as <span class="bold">JEE</span> application. Ontologies are stored 
							in database using RDF triples. Everything is accessible through special designed and
							comfortable API, which keeps ontologies in object model. It is based on 
							<a target="_blank" href="http://owlapi.sourceforge.net/">OWL API</a> project. 							
						</p>
					</div>
					<div style="clear:both;"></div>
					<div style="clear:both;">
						<a>
							<h:graphicImage styleClass="leftAlignedImage" url="/images/print-screen2.jpg"></h:graphicImage>
						</a>
						<h2>Check the OCS out!</h2>
						<p>						
							OCS is software, which enables to develop ontologies in 
							<span class="bold">cooperative</span> way. Introducing new changes is controlled 
							by priviledged users - ontology owner and ontology expert. They can introduce new versions
							and also lock access to ontologies. Every functions described above are already available 
							in first release.
						</p>
						<p>	
							Get the OCS now. If you want to check the OCS, please sign in. Soon Administrator will 
							activate your account and you will be able to download it. 							
						</p>
						<p>	 
							If you are not interested in our ontology editor, or you want to create your own, 
							please send us email. Administrator will provide you every information and 
							software. If you are not signed in, please <h:outputLink value="#{request.contextPath}/pages/register.jsf">register!</h:outputLink>							
						</p>
					</div>		
					
					</h:panelGroup>										
				</div>
				
				<!-- footer -->
				
				<%@ include file="/pages/layout/footer.jsp" %>
				
			</div>
			</h:form>			
		
		</body>
	</f:view>
</html>
