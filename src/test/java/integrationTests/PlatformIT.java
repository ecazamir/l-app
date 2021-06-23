/* Test integrare platformă
 * Se verifică dacă versiunea Servlet API corespunde cerințelor
 * și dacă cererile primesc răspunsurile solicitate.
 * Autor: Emil Cazamir
 * Data: 2021.04.17
 * Referințe: 
 *   https://github.com/mike10004/cargo-tomcat8-example/
 *   https://www.twilio.com/blog/5-ways-to-make-http-requests-in-java
 */

package integrationTests;

import static org.junit.Assert.assertFalse;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import constants.Vars;
import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.junit.Test;

public class PlatformIT {
	
	@Test
	public void testServletApiVersion() throws Exception {

		Logger logger = Logger.getLogger(PlatformIT.class.getName());

		String cargoUrl = Vars.cargoContainerEndpoint + Vars.servletApiUrlPath;
		logger.info("Test acces URL: " + cargoUrl);
		// Deschidere conexiune
		URL url = new URL(cargoUrl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// Setare parametri conexiune
		connection.setRequestProperty("accept", "text/plain");
		// Execută cererea
		InputStream responseStream = connection.getInputStream();
		String responseString = IOUtils.toString(responseStream, StandardCharsets.US_ASCII);
		logger.info(responseString);
		ComparableVersion expectedApiVersion = new ComparableVersion(Vars.expectedServletApiString);
		ComparableVersion actualApiVersion = new ComparableVersion(responseString);
		assertFalse(actualApiVersion.compareTo(expectedApiVersion) < 0);
		logger.info("Test versiune API încheiat, API raportat " + actualApiVersion + ", API asteptat "
				+ expectedApiVersion);
	}
}
