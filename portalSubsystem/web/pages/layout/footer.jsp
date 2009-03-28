<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/myfaces_core.tld" prefix="f" %>
<%@ taglib uri="/WEB-INF/tld/myfaces-html.tld" prefix="h" %>

<div id="footer">
	<div class="padded_container" >&copy; Copyright 2008
		<ul> 
			<li>
				<h:outputLink value="http://www.eti.pg.gda.pl/katedry/kask" target="_blank">
					<h:outputText value="KASK"></h:outputText>
				</h:outputLink>
			</li>
			<li>
				<h:outputLink value="http://www.eti.pg.gda.pl" target="_blank">
					<h:outputText value="WETI"></h:outputText>
				</h:outputLink>
			</li>
			<li class="last">
				<h:outputLink value="http://www.pg.gda.pl" target="_blank">
					<h:outputText value="#{communicates['pg.name']}"></h:outputText>
				</h:outputLink>
			</li>							
		</ul>
	</div>  
</div>