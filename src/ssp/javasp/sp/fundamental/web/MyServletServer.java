package ssp.javasp.sp.fundamental.web;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletHandler;

public class MyServletServer {

	public static void main(String[] args) throws Exception {
		Server server = MyServletServer.createHttpServletServer();
		server.join();
	}

	public static Server createHttpServletServer() throws Exception {
		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(8080);
		server.addConnector(http);

		ServletHandler servletHandler = new ServletHandler();
		// 1. get, post test
		servletHandler.addServletWithMapping(HelloWorldServlet.class, "/helloworld");
		//2. paramger test
		servletHandler.addServletWithMapping(CalculatorServlet.class, "/calculator");
		//3. paramger test
		servletHandler.addServletWithMapping(RestApiServiceServlet.class, "/rest/*");
		
		
		server.setHandler(servletHandler);

		server.start();
//		server.join();
		
		return server;
	}
}
