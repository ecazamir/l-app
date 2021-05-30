package application;

import constants.Vars;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = Vars.servletVerStrUrlPath, name = Vars.servletVerStrLogicalName)
public class ServletRuntimeInformation extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext context = request.getServletContext();
		String outputString = context.getServerInfo();
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.print(outputString);
	}

}
