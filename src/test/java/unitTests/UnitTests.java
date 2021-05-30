package unitTests;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UnitTests {

	@Test
	public void testJDBCDriver() throws Exception {
		Logger logger = Logger.getLogger(UnitTests.class.getName());
		logger.info("Test unitate - JDBC Drivers");
		
		// Simulare DB in-memory
		try {
			logger.info("Pornire DB Derby test ...");
			@SuppressWarnings("unused")
			Connection connection = null;
			// API Reference: https://db.apache.org/derby/docs/10.14/devguide/derbydev.pdf
			connection = DriverManager.getConnection("jdbc:derby:memory:myDB;create=true");
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Nu se poate crea conexiune DB Derby!", e);
			throw e;
		} 
	    Enumeration<Driver> e = java.sql.DriverManager.getDrivers();
	    while (e.hasMoreElements()) {
	      Driver drv = e.nextElement();
	      String s = drv.getClass().getName();
	      assertTrue(s.matches("(.*)\\.apache\\.derby\\.(.*)"));
	    }
	}

}
