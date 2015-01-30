package com.mossbuss.webapp.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.eclipse.jetty.util.log.StdErrLog;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.shared.FieldVerifier;
import com.mossbuss.webapp.server.data.Driver;
import com.mossbuss.webapp.server.data.Client;
import com.mossbuss.webapp.server.data.Bus;
import com.mossbuss.webapp.server.data.MaintenanceRecord;
import com.mossbuss.webapp.server.data.Student;
import com.mossbuss.webapp.server.data.TripSheet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings({ "serial", "deprecation" })
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	protected DataSource serverDataSource = null;
	public void init() throws ServletException {// create database connection
		// pools
		try {
			serverDataSource = new ServerTestDataSource();
		}
		catch (ClassNotFoundException e1) {
			System.out.println("serverDataSource Error: "+e1.getMessage());
			e1.printStackTrace();
		}
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
	public StudentDTO saveStudent(StudentDTO studentDetails) throws Exception {
		Student student = new Student();
		student.setData(studentDetails);
		System.out.println("XXXXXXXXXXXDDDXXDDDX " + studentDetails.getStudentName());
		//Save
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Student.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if(student.getID() > 0){
				session.update(student);
			} else {
				session.save(student);
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
		studentDetails = student.getData();
		return studentDetails;
		
		//havent tested.
	}

	@Override
	public ArrayList<String> updateContactNameOracle(String sql)
			throws Exception {
		ArrayList<String> oracle = new ArrayList<String>();
		//Do Lookup
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				ResultSet srs = null;
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
				
					while(rs.next()) {			
						oracle.add(rs.getInt("id")+" :: "+rs.getString("Address")+ "; " +rs.getString("StudentName"));
					}
					
//					srs = stmt.executeQuery("select * from StudentName");
//					while(srs.next()) {
//						//System.out.println("" + srs.);
//					}
				
				} finally {
					if (rs != null) {
						try { rs.close(); } catch(SQLException ex) {}
						rs=null;
					}
					if (stmt != null) {
						try { stmt.close(); } catch(SQLException ex) {}
						stmt = null;
					}
					if (conn != null) {
						try { conn.close(); } catch(SQLException ex) {}
						conn = null;
					}
				}
		return oracle;
		//??????? does nothing...
				//ok it will work the same now..
		//the connection is not using Hibernate which is why it needs a datasource file. 
		//yeah what i mainly wanted to know is how to handle the DB calls "customer Like etc"
		//must i still do this method? ofcourse im sure..
		//yes.. without it you won't get suggestions
		//okay awesome, i wanna get this and printing of trip sheets done by the end of today.. 
	}

	@Override
	public StudentDTO getStudent(int selectedID) throws Exception {
		Student details = new Student();
		Session session= null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Student.class);
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
		StudentDTO customerDetails = details.getData();
		
		return customerDetails;
		
	}
	@Override
	public ArrayList<StudentDTO> getStudentsFromParent(int selectedParentID) throws Exception {
		ArrayList<StudentDTO> studentlist = new ArrayList<StudentDTO>();
		
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				ResultSet srs = null;
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select id from student where ParentID Like '" + selectedParentID + "'";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
				
					while(rs.next()) {			
						studentlist.add(getStudent(rs.getInt("id")));
					}
					
//					srs = stmt.executeQuery("select * from StudentName");
//					while(srs.next()) {
//						//System.out.println("" + srs.);
//					}
				
				} finally {
					if (rs != null) {
						try { rs.close(); } catch(SQLException ex) {}
						rs=null;
					}
					if (stmt != null) {
						try { stmt.close(); } catch(SQLException ex) {}
						stmt = null;
					}
					if (conn != null) {
						try { conn.close(); } catch(SQLException ex) {}
						conn = null;
					}
				}
				return studentlist;
	}
	@Override
	public ArrayList<DriverDTO> getAllDrivers() throws Exception {
		ArrayList<DriverDTO> driverlist = new ArrayList<DriverDTO>();
		
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				ResultSet srs = null;
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select * from driver";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
				
					while(rs.next()) {			
						driverlist.add(getDriver(rs.getInt("id")));
					}
					
//					srs = stmt.executeQuery("select * from StudentName");
//					while(srs.next()) {
//						//System.out.println("" + srs.);
//					}
				
				} finally {
					if (rs != null) {
						try { rs.close(); } catch(SQLException ex) {}
						rs=null;
					}
					if (stmt != null) {
						try { stmt.close(); } catch(SQLException ex) {}
						stmt = null;
					}
					if (conn != null) {
						try { conn.close(); } catch(SQLException ex) {}
						conn = null;
					}
				}
				return driverlist;
	}
	
	@Override
	public ArrayList<BusDTO> getAllBusses() throws Exception {
		ArrayList<BusDTO> buslist = new ArrayList<BusDTO>();
		
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				ResultSet srs = null;
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select * from driver";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
				
					while(rs.next()) {			
						buslist.add(getBus(rs.getInt("id")));
					}
					
//					srs = stmt.executeQuery("select * from StudentName");
//					while(srs.next()) {
//						//System.out.println("" + srs.);
//					}
				
				} finally {
					if (rs != null) {
						try { rs.close(); } catch(SQLException ex) {}
						rs=null;
					}
					if (stmt != null) {
						try { stmt.close(); } catch(SQLException ex) {}
						stmt = null;
					}
					if (conn != null) {
						try { conn.close(); } catch(SQLException ex) {}
						conn = null;
					}
				}
				return buslist;
	}
	
	private DriverDTO getDriver(int selectedID) throws Exception {
		Driver details = new Driver();
		Session session= null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Student.class);
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
		DriverDTO customerDetails = details.getData();
		
		return customerDetails;

	}



	@Override
	public ArrayList<StudentDTO> getStudentsFromTripSheet(int selectedTripSheetID) throws Exception {
		ArrayList<StudentDTO> studentlist = new ArrayList<StudentDTO>();
		
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				ResultSet srs = null;
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select id from student where TripSheetID Like '" + selectedTripSheetID + "'";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
				
					while(rs.next()) {			
						studentlist.add(getStudent(rs.getInt("id")));
					}
					
//					srs = stmt.executeQuery("select * from StudentName");
//					while(srs.next()) {
//						//System.out.println("" + srs.);
//					}
				
				} finally {
					if (rs != null) {
						try { rs.close(); } catch(SQLException ex) {}
						rs=null;
					}
					if (stmt != null) {
						try { stmt.close(); } catch(SQLException ex) {}
						stmt = null;
					}
					if (conn != null) {
						try { conn.close(); } catch(SQLException ex) {}
						conn = null;
					}
				}
				return studentlist;
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






	@Override
	public ClientDTO saveClient(ClientDTO clientDetails) throws Exception {
		Client client = new Client();
		client.setData(clientDetails);
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX CLIENT ADDRESS IS : " + clientDetails.getAddress());
		//Save
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Client.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if(client.getID() > 0){
				session.update(client);
			} else {
				session.save(client);
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
		clientDetails = client.getData();
		return clientDetails;
		
		//havent tested.
	}
	@Override
	public ClientDTO getClient(int selectedID) throws Exception {
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
	public TripSheetDTO getTripSheet(int selectedID) throws Exception {
		TripSheet details = new TripSheet();
		Session session = null;
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
		TripSheetDTO customerDetails = details.getData();
		
		return customerDetails;
		
	}
}
