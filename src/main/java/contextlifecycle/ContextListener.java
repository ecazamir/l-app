package contextlifecycle;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener("Context listener")
public class ContextListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(ContextListener.class.getName());

	private void initializeDatabase() throws SQLException {
		Connection connection = null;
		try {
			log.info("Pornire DB Derby in context servlet...");
			// API Reference: https://db.apache.org/derby/docs/10.14/devguide/derbydev.pdf
			connection = DriverManager.getConnection("jdbc:derby:memory:myDB;create=true");
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Nu se poate efectua conexiunea la DB Derby!", e);
			throw e;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private void shutdownDerby() throws SQLException {
		Connection connection = null;
		try {
			log.info("Oprire DB Derby in context servlet...");
			// API Reference: https://db.apache.org/derby/docs/10.14/devguide/derbydev.pdf
			connection = DriverManager.getConnection("jdbc:derby:memory:myDB;drop=true");

		} catch (SQLException e) {
			int errCode = e.getErrorCode();
			String sqlState = e.getSQLState();
			String sqlMessage = e.getMessage();
			if (sqlState.equals("08006")) {
				/*
				 * Comportament documentat, oprirea instantei genereaza exceptie cu SQL State
				 * 08006
				 */
				log.log(Level.INFO, "Derby oprit cu succes, cod: " + errCode + ", SQL State = " + sqlState
						+ ", SQL Message = " + sqlMessage);
			} else {
				log.log(Level.WARNING, "Derby oprit cu stare neasteptata, cod: " + errCode + ", SQL State = " + sqlState
						+ ", SQL Message = " + sqlMessage);
				throw e;
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		log.log(Level.INFO, "De-registrare JDBC driver ...");
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				System.out.println(driver.getClass().getName());
			} catch (SQLException e) {
				System.err.println("Eroare la de-registrare driver JDBC " + driver.getClass().getName());
			}
		}

	}

@Override
public void contextInitialized(ServletContextEvent event) {
	ServletContext context = event.getServletContext();
	System.out.println("Pornire aplicatie in context " + context.getContextPath());
	try {
		log.info("Incarcare driver DB Derby Embedded...");
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		initializeDatabase();
	} catch (ClassNotFoundException e) {
		log.log(Level.SEVERE, "Clasa driverului DB Derby nu este disponibila!", e);
	} catch (SQLException e) {
		log.log(Level.SEVERE, "Exceptie SQL DB Derby!", e);
	}
}

@Override
public void contextDestroyed(ServletContextEvent event) {
	ServletContext context = event.getServletContext();
	System.out.println("Oprire aplicatie in context " + context.getContextPath());
	try {
		shutdownDerby();
	} catch (SQLException e) {
		log.log(Level.SEVERE, "Nu se poate comunica cu instanta DB Derby!", e);
	}
}
}