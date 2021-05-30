/* Numere de versiune așteptate/dorite pentru platformă
 * Autor: Emil Cazamir
 * Data: 2021.04.17
 */
package constants;

public class Vars {
	public final static String expectedServletApiString = "3.0";
	public final static int cargoListenerPort = 8080; 
	/* Containerul Crago este invocat de Maven. Numărul de port trebuie să corespundă
	 * cu numărul din ./pom.xml */
	public final static String servletApiUrlPath = "/servletapiversion";
	public final static String servletApiLogicalName = "servletapiversion";
	public final static String servletVerStrUrlPath = "/servletplatform";
	public final static String servletVerStrLogicalName = "servletplatform";
	
	public final static String cargoContainerEndpoint = "http://localhost" + ":" + cargoListenerPort;
	
}
