package com.mossbuss.webapp.server;

import java.util.ArrayList;

import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public void resetDatabase(String string) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DriverDTO doLogin(DriverDTO driverDTO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO saveStudent(ClientDTO studentDetails) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> updateContactNameOracle(String sql)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO getStudent(int selectedID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDriver(DriverDTO userdetails) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<DriverDTO> listDrivers(String string) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveBus(BusDTO item) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BusDTO getBus(int selectedID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> updateBusCodeOracle(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TripSheetDTO saveTripSheet(TripSheetDTO orderDetails)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
