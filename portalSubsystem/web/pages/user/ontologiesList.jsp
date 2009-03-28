<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>
<%@ taglib uri="/WEB-INF/tld/tomahawk.tld" prefix="t" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<f:view locale="#{changeLocaleBean.currentLanguage}" >
		<head>
			<title>${communicates['ontologies.pageTitle']}</title>
	 		<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/main.css"/>
		</head>
		<body>
			<h:form id="form1">
			<div class="content">
			
				<!-- header -->
				<%@ include file="/pages/layout/header.jsp" %>
				
				<!-- main content -->
				
				<div class="body">
				
					<h2 style="text-align: center;"><h:outputText value="#{communicates['ontologies.ontologiesList.title']}"></h:outputText></h2>
					
					<span class="bold"><h:outputText value="#{communicates['ontologies.sampleOntologies']}"></h:outputText></span>
					<ul> 
						<li>
							<h:outputLink value="#{request.contextPath}/pages/sampleOntologies/travel.owl" target="_blank">
								<h:outputText value="#{communicates['travelOntology.text']}" styleClass="bold"></h:outputText>
							</h:outputLink> - <h:outputText value="#{communicates['travelOntology.text1']}"></h:outputText>
						</li>
						<li>
							<h:outputLink value="#{request.contextPath}/pages/sampleOntologies/camera2.owl" target="_blank">
								<h:outputText value="#{communicates['cameraOntology.text']}" styleClass="bold"></h:outputText>
							</h:outputLink> - <h:outputText value="#{communicates['cameraOntology.text1']}"></h:outputText>						
						</li>
					</ul>				
															
					<t:panelTabbedPane width="100%" id="ontologies" selectedIndex="#{ontologiesListBean.selectedTab}" >
						<t:panelTab id="allOntologiesTab" label="#{communicates['ontologies.allOntologiesTab.title']}" >
							
							<t:dataTable id="allOntologies" value="#{ontologiesListBean.allOntologies}" var="ontology"  
								rowClasses="ontsTable_oddrow, ontsTable_evenrow" 
								styleClass="ontsTable" columnClasses="ontsTable_baseUriCol,
								ontsTable_ontologyNameCol, ontsTable_versionsCol, ontsTable_subscribeCol"
								headerClass="ontsTable_header"
								preserveDataModel="true" varDetailToggler="detailTogglerBean">							
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.baseUri.columnName']}"></h:outputText>
									</f:facet>
									<h:outputText value="#{ontology.ontologyBaseURI.URIAsString}"></h:outputText>
								</h:column>		
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.ontologyName.columnName']}"></h:outputText>
									</f:facet>
									<h:outputText value="#{ontology.ontologyBaseURI.ontologyName}"></h:outputText>
								</h:column>										
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.subscription.columnName']}"></h:outputText>
									</f:facet>
									<t:popup styleClass="popup" id="z"
										closePopupOnExitingElement="true"
                						closePopupOnExitingPopup="true"                						
							            displayAtDistanceX="0"
							            displayAtDistanceY="0">
							            <h:commandLink rendered="#{!ontology.subscribed}"> 
							            	<h:outputText value="#{communicates['ontologies.subscribe.actionName']}"/>
							            </h:commandLink>							                          
						                <f:facet name="popup">
						                    <h:panelGroup>
						                        <h:panelGrid columns="1" >	
						                        	<h:outputText value="#{communicates['ontologies.subscriptionPopup.text']}"/>
						                        	<h:panelGroup>
						                        		<h:commandButton value="#{communicates['ontologies.yesOption.text']}" style="font-size: smaller;" actionListener="#{ontologiesListBean.subscribeOntology}">
						                        			<f:attribute name="baseURI" value="#{ontology.ontologyBaseURI.URIAsString}"/>
						                        		</h:commandButton>						                        			
						                        		&nbsp;<h:commandButton value="#{communicates['ontologies.noOption.text']}" style="font-size: smaller;"/>						                        			
						                        	</h:panelGroup>
						                        </h:panelGrid>
						                    </h:panelGroup>
						                </f:facet>
						            </t:popup>
								</h:column>		
								<h:column>
									<f:facet name="header">
										<h:outputText value="#{communicates['ontologies.versions.columnName']}"></h:outputText>
									</f:facet>
									<h:commandLink rendered="#{detailTogglerBean.currentDetailExpanded}" action="#{detailTogglerBean.toggleDetail}">
										<h:outputText value="#{communicates['ontologies.hideOption.text']}" />
									</h:commandLink>
									<h:commandLink rendered="#{!detailTogglerBean.currentDetailExpanded}" action="#{detailTogglerBean.toggleDetail}">
										<h:outputText value="#{communicates['ontologies.showOption.text']}" />
									</h:commandLink>																									
								</h:column>
								<f:facet name="detailStamp">									
									<t:dataTable id="versions" renderedIfEmpty="false" 
										var="version" value="#{ontology.versions}"
										rowClasses="ontVersionsTable_oddrow, ontVersionsTable_evenrow" 
										styleClass="ontVersionsTable" columnClasses="ontVersionsTable_versionCol,
										ontVersionsTable_subversionCol, ontVersionsTable_versionDescriptionCol"
										headerClass="ontVersionsTable_header">
										<h:column>
											<f:facet name="header">
												<h:outputText value="#{communicates['ontologies.version.columnName']}"></h:outputText>
											</f:facet>
											<h:outputText value="#{version.ontologyVersion}" />
										</h:column>
										<h:column>
											<f:facet name="header">
												<h:outputText value="#{communicates['ontologies.subVersion.columnName']}"></h:outputText>
											</f:facet>
											<h:outputText value="#{version.ontologySubversion}"></h:outputText>
										</h:column>
										<h:column>
											<f:facet name="header">
												<h:outputText value="#{communicates['ontologies.versionDescription.columnName']}"></h:outputText>
											</f:facet>
											<h:outputText value="#{version.versionDescription}"></h:outputText>
										</h:column>
									</t:dataTable>
								</f:facet>
								
							</t:dataTable>																	
												    
						</t:panelTab>
						<t:panelTab id="myOntologiesTab" label="#{communicates['ontologies.myOntologiesTab.title']}" >
					      <t:dataTable id="myOntologies" value="#{ontologiesListBean.myOntologies}" var="ontology"  
								rowClasses="ontsTable_oddrow, ontsTable_evenrow" 
								styleClass="ontsTable" columnClasses="ontsTable_baseUriCol,
								ontsTable_ontologyNameCol, ontsTable_versionsCol"
								headerClass="ontsTable_header"
								preserveDataModel="true" varDetailToggler="detailTogglerBean">
								
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.baseUri.columnName']}"></h:outputText>
									</f:facet>
									<h:outputText value="#{ontology.ontologyBaseURI.URIAsString}"></h:outputText>
								</h:column>		
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.ontologyName.columnName']}"></h:outputText>
									</f:facet>
									<h:outputText value="#{ontology.ontologyBaseURI.ontologyName}"></h:outputText>
								</h:column>										
								<h:column>
									<f:facet name="header">
										<h:outputText value="#{communicates['ontologies.experts.columnName']}"></h:outputText>
									</f:facet>
									<h:commandLink rendered="#{detailTogglerBean.currentDetailExpanded}" action="#{detailTogglerBean.toggleDetail}">
										<h:outputText value="#{communicates['ontologies.hideOption.text']}" />
									</h:commandLink>
									<h:commandLink rendered="#{!detailTogglerBean.currentDetailExpanded}" action="#{detailTogglerBean.toggleDetail}">
										<h:outputText value="#{communicates['ontologies.showOption.text']}" />
									</h:commandLink>	
								</h:column>		
								<f:facet name="detailStamp">		
									<h:panelGroup  style="width: 100%;">
										<h:outputLink value="addOntologyExperts.jsf">		
											<h:outputText value="#{communicates['ontologies.addExpert.actionName']}"></h:outputText>
											<f:param name="ontologyURI" value="#{ontology.ontologyBaseURI.URIAsString}"></f:param>
										</h:outputLink>												
										<t:dataTable id="experts" renderedIfEmpty="false" 
											var="expert" value="#{ontology.experts}"
											rowClasses="ontVersionsTable_oddrow, ontVersionsTable_evenrow" 
											styleClass="ontVersionsTable" columnClasses="ontVersionsTable_versionCol,
											ontVersionsTable_subversionCol, ontVersionsTable_versionDescriptionCol"
											headerClass="ontVersionsTable_header">
											<h:column>
												<f:facet name="header">
													<h:outputText value="#{communicates['ontologies.userName.columnName']}"></h:outputText>
												</f:facet>
												<h:outputText value="#{expert}" />											
											</h:column>		
											<h:column>
												<f:facet name="header">
													<h:outputText value="#{communicates['ontologies.deletion.columnName']}"></h:outputText>
												</f:facet>
												<t:commandButton value="#{communicates['ontologies.delete.actionName']}" 
													actionListener="#{ontologiesListBean.removeOntologyExpert}">
													<f:attribute name="baseURI" value="#{ontology.ontologyBaseURI.URIAsString}"/>
													<f:attribute name="userName" value="#{expert}"/>
												</t:commandButton>											
											</h:column>										
										</t:dataTable>
									</h:panelGroup>
								</f:facet>
								
							</t:dataTable>					
						</t:panelTab>
						
						<t:panelTab id="subscribedOntologiesTab" label="#{communicates['ontologies.subscribedOntologiesTab.title']}">						
					       <t:dataTable id="subscribedOntologies" value="#{ontologiesListBean.subscribedOntologies}" var="ontology"  
								rowClasses="ontsTable_oddrow, ontsTable_evenrow" 
								styleClass="ontsTable" columnClasses="ontsTable_baseUriCol,
								ontsTable_ontologyNameCol, ontsTable_versionsCol, ontsTable_subscribeCol"
								headerClass="ontsTable_header" preserveDataModel="true" >
								
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.baseUri.columnName']}"></h:outputText>
									</f:facet>
									<h:outputText value="#{ontology.URIAsString}"></h:outputText>
								</h:column>		
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.ontologyName.columnName']}"></h:outputText>
									</f:facet>
									<h:outputText value="#{ontology.ontologyName}"></h:outputText>
								</h:column>										
								<h:column>
									<f:facet name="header" >
										<h:outputText value="#{communicates['ontologies.subscription.columnName']}"></h:outputText>
									</f:facet>
									<t:popup styleClass="popup" id="z"
										closePopupOnExitingElement="true"
                						closePopupOnExitingPopup="true"                						
							            displayAtDistanceX="0"
							            displayAtDistanceY="0">
							            <h:commandLink > 
							            	<h:outputText value="#{communicates['ontologies.delete.actionName']}"/>
							            </h:commandLink>							                          
						                <f:facet name="popup">
						                    <h:panelGroup>
						                        <h:panelGrid columns="1" >	
						                        	<h:outputText value="#{communicates['ontologies.deleteSubscription.text']}"/>
						                        	<h:panelGroup>
						                        		<h:commandButton value="#{communicates['ontologies.yesOption.text']}" style="font-size: smaller;" actionListener="#{ontologiesListBean.unSubscribeOntology}" >
						                        			<f:attribute name="baseURI" value="#{ontology.URIAsString}"/>
						                        		</h:commandButton>						                        			
						                        		&nbsp;<h:commandButton value="#{communicates['ontologies.noOption.text']}" style="font-size: smaller;"/>						                        			
						                        	</h:panelGroup>
						                        </h:panelGrid>
						                    </h:panelGroup>
						                </f:facet>
						            </t:popup>
								</h:column>										
							</t:dataTable>					
						</t:panelTab>
					</t:panelTabbedPane>														
				</div>
				
				<!-- footer -->
				
				<%@ include file="/pages/layout/footer.jsp" %>				
			</div>
			</h:form>					
		</body>
	</f:view>
</html>
