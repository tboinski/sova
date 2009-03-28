package org.pg.eti.kask.ont.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pg.eti.kask.ont.common.User;

/**
 * Serwlet zwracajacy plik jnlp - niezbedny do pobrania aplikacji z serwera.
 *
 * @author Andrzej Jakowski
 */
public class JnlpDownloadServlet extends HttpServlet {
	static final long serialVersionUID = 1L;
	
	/** Folder w archiwum war, gdzie znajduja sie jary aplikacji */
	private static final String GUI_FOLDER_PATH="application/";
	
	private static final String JNLP_MIME_TYPE="application/x-java-jnlp-file";
	

	

	public JnlpDownloadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//pobranie zalogowanego uzytkownika
		User user =(User)request.getSession().getAttribute("user");
		
		if(user != null ) {
			
			if(user.getIsAccepted()) {
				generateJnlpFile(request, response);
			}
		} else {				
			response.sendRedirect(request.getContextPath()+"/pages/login.jsf");
		}
	}
	
	private void generateJnlpFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String codebase = new String();
		PrintWriter pw = response.getWriter();
		response.setContentType(JNLP_MIME_TYPE);
		
		
		//ustalenie adresu aplikacji webowej - staje sie ona codebase dla jnlp
		URL requestedURL = new URL(request.getRequestURL().toString());
		codebase = requestedURL.getProtocol()+"://"+requestedURL.getHost();
		if(requestedURL.getPort()!=-1) {
			codebase = codebase+":"+ requestedURL.getPort();
		}
		codebase = codebase+request.getContextPath();
		
		//info o systemie
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");		
		pw.println("<jnlp spec=\"1.0+\" codebase=\""+codebase+"\">");
		
		pw.println("<information>");
		pw.println("<title>OCS - Ontology creation system</title>");
		pw.println("<vendor>KASK Department, ETI Faculty, Gdansk University of Technology</vendor>");
		pw.println("<homepage href=\"index.jsp\" />");
		pw.println("<offline-allowed />");
		pw.println("</information>");
		
		pw.println("<security>");
		pw.println("<all-permissions/>");
		pw.println("</security>");
		
		pw.println("<application-desc main-class=\"org.pg.eti.kask.ont.editor.Main\" />");
		
		//sekcja z pozostalymi jarami jakie maja byc dolaczone w trakcie deploy'u aplikacji
		pw.println("<resources>");
		pw.println("<j2se version=\"1.6\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"ocs-gui.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"jlfgr-1_0.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"Library.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"interfaces-1.1.8.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"common-3.2.0.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"antlr-runtime-3.0.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"commons-lang-2.2.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"prefuse.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-api.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-apibinding.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-change.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-debugging.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-dig1_1.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-functionalparser.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-functionalrenderer.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-impl.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-krssparser.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-mansyntaxparser.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-mansyntaxrenderer.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-metrics.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-oboparser.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-owlxmlparser.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-owlxmlrenderer.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-rdfapi.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-rdfxmlparser.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-rdfxmlrenderer.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"owlapi-util.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"pellet.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"commons-logging-1.1.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"aterm-java-1.6.jar\" />");
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"relaxngDatatype.jar\" />");  
		pw.println("<jar href=\""+GUI_FOLDER_PATH+"xsdlib.jar\" />");  

		pw.println("</resources>");
	
		pw.println("</jnlp>");
		
		pw.flush();
		pw.close();
	}
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}