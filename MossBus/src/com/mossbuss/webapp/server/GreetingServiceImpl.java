package com.mossbuss.webapp.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.shared.FieldVerifier;
import com.mossbuss.webapp.server.data.Driver;
import com.mossbuss.webapp.server.data.Client;
import com.mossbuss.webapp.server.data.Bus;
import com.mossbuss.webapp.server.data.MaintenanceRecord;
import com.mossbuss.webapp.server.data.TripSheet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings({ "serial", "deprecation" })
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
		// Login
		Session session = null;
		Driver driver = new Driver();
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Driver.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			String hql = "from Driver where EmailAddress = '"+ driverDTO.getEmailAddress() +"' AND Password = '"+driverDTO.getPassword()+"'";
			
			Query query = session.createQuery(hql);
			List userList = query.list();
			if(userList == null || userList.size()<1){
				throw new Exception("Authentication Error");
			} else{
				driver = (Driver) userList.get(0);
			}	
		}
		catch (Exception e){
			throw new Exception("Authentication Error");
		}
		finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		DriverDTO test = new DriverDTO();
		test.setAdmin(true);
		test.setBusID(-1);
		test.setEmailAddress("2andrewp2@gmail.com");
		test.setName("Andrew");
		test.setPassword("Ghosty678");
		test.setTripSheetID(-1);
		return driver.getData();
		
	}

	@Override
	public ClientDTO saveStudent(ClientDTO studentDetails) throws Exception {
		Client customer = new Client();
		customer.setData(studentDetails);
		
		//Save
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Client.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if(customer.getID() > 0){
				session.update(customer);
			} else {
				session.save(customer);
			}
			session.getTransaction().commit();
			
		}
		catch(Exception e){
			throw new Exception(e.getMessage() + e.getCause());
		}
		finally{
			if(session != null && session.isOpen()){
				if(session != null && session.isOpen()){
					session.close();
				}
			}
		}
		studentDetails = customer.getData();
		return studentDetails;
		
		//havent tested.
	}

	@Override
	public ArrayList<String> updateContactNameOracle(String sql)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO getStudent(int selectedID) throws Exception {
		Client details = new Client();
		Session session= null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Client.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(details, selectedID);
			session.getTransaction().commit();	
		}
		catch (Exception e){
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());
			
		}
		finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		ClientDTO customerDetails = details.getData();
		
		return customerDetails;
		
	}

	@Override
	public void saveDriver(DriverDTO driverDetails) throws Exception {
		Driver driver = new Driver();
		driver.setData(driverDetails);
		
		//Save
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Client.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if(driver.getID() > 0){
				session.update(driver);
			} else {
				session.save(driver);
			}
			session.getTransaction().commit();
			
		}
		catch(Exception e){
			throw new Exception(e.getMessage() + e.getCause());
		}
		finally{
			if(session != null && session.isOpen()){
				if(session != null && session.isOpen()){
					session.close();
				}
			}
		}

		//havent tested.
		
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
