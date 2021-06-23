package application;

/* Servlet ce permite obtinerea de informatii despre platformă, text/plain
 * Info livrat: versiune API
 * Autor: Emil Cazamir
 * Data: 2021.04.17
 */

import constants.Vars;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = Vars.servletApiUrlPath, name = Vars.servletApiLogicalName)
public class ServletApiVersion extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext context = request.getServletContext();
		String outputString = context.getEffectiveMajorVersion() + "." + context.getEffectiveMinorVersion();
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.print(outputString);
	}

}
