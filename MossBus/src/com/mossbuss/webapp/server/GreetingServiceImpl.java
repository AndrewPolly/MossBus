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
			String hql = "from Driver where EmailAddress = '" + driverDTO.getEmailAddress() +"' AND Password = '"+driverDTO.getPassword()+"'";
			
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
	public ArrayList<String> updateTripSheetNameOracle(String sql)
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
						oracle.add(rs.getInt("id")+" :: "+rs.getString("TripName")+ "; " +rs.getString("DriverName"));
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
	public ArrayList<DriverDTO> getAllDrivers(int TripSheetID) throws Exception {
		ArrayList<DriverDTO> driverlist = new ArrayList<DriverDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();
		DriverDTO realDriver = new DriverDTO();
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				Statement stmt2 = null;
				
				ResultSet rs2 = null;
				System.out.println("111111");
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
						System.out.println("22222222");
					}
					String sql = "select id from driver";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					
					System.out.println("33333333");
					
					
					while(rs.next()) {			
						int x = rs.getInt("id");
						if (getDriver(x).getTripSheetID() == TripSheetID) {
							driverlist.add(0, getDriver(x));
						} else if (getDriver(x).getTripSheetID() < 0) {
							driverlist.add(getDriver(x));
						}
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
	public ArrayList<BusDTO> getAllBusses(int TripSheetID) throws Exception {
		ArrayList<BusDTO> buslist = new ArrayList<BusDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				
				
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select id from bus";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {			
						int x = rs.getInt("id");
						if (getBus(x).getTripSheetID() == TripSheetID) {
							buslist.add(0, getBus(x));
						} else if (getBus(x).getTripSheetID() < 0) {
							buslist.add(getBus(x));
						}
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
			config.addAnnotatedClass(Driver.class);
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
		System.out.println("Also Gets here!!!!");
		Driver driver = new Driver();
		driver.setData(driverDetails);

		//Save
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Driver.class);
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
	public void updateDBtripSheetSelected(int tripSheetID) throws Exception {
		TripSheetDTO tripSheet = new TripSheetDTO();
		BusDTO bus = new BusDTO();
		DriverDTO driver = new DriverDTO();
		
		tripSheet = getTripSheet(tripSheetID);
		System.out.println("SDSSDSDSDSDSDSDS");
		if (tripSheet.getBusID() > 0) { 
			System.out.println("DDDDDDDDDDDDDDDDDD");
			bus = getBus(tripSheet.getBusID());
			bus.setTripSheetID(tripSheetID);
			saveBus(bus);
			System.out.println("EEEEEEEEEEEEEEEEE");
		}
		if (tripSheet.getDriverID() > 0) {
			System.out.println("FFFFFFFFFFFFFFFF driver ID : " + tripSheet.getDriverID());
			driver = getDriver(tripSheet.getDriverID());
			System.out.println("okay well driverName is : " + driver.getName());
			driver.setTripSheetID(tripSheetID);
			saveDriver(driver);
			System.out.println("GGGGGGGGGGGGGGGGG");
		}
	}
	@Override
	public void saveBus(BusDTO item) throws Exception {
		Bus newBus = new Bus();
		newBus.setBusName(item.getBusName());
		newBus.setDriverID(item.getDriverID());
		newBus.setTripSheetID(item.getTripSheetID());
		newBus.setID(item.getID());
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Bus.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if(newBus.getID() > 0){
				session.update(newBus);
			} else {
				session.save(newBus);
			}
			session.getTransaction().commit();
		}
		catch(Exception e){
			System.out.println(e.getMessage() + e.getCause());
		}
		finally{
			if(session != null && session.isOpen()){
				if(session != null && session.isOpen()){
					session.close();
				}
			}
		}
		
	}

	@Override
	public BusDTO getBus(int selectedID) throws Exception {
		Bus bus = new Bus();
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Bus.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(bus, selectedID);
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
		BusDTO busDetails = bus.getData();
		
		return busDetails;
	}

	@Override
	public ArrayList<String> updateBusCodeOracle(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TripSheetDTO saveTripSheet(TripSheetDTO tripDetails)
			throws Exception {
		TripSheet tripSheet = new TripSheet();
		DriverDTO driver = new DriverDTO();
		BusDTO bus = new BusDTO();
		tripSheet.setData(tripDetails);
		
		//Save
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(TripSheet.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if(tripSheet.getID() > 0){
				session.update(tripSheet);
			} else {
				session.save(tripSheet);
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
		if (tripSheet.getDriverID() > 0) {
			driver = getDriver(tripSheet.getDriverID());
			driver.setTripSheetID(tripSheet.getID());
			saveDriver(driver);
		} 
		if (tripSheet.getBusID() > 0) {
			bus = getBus(tripSheet.getDriverID());
			bus.setTripSheetID(tripSheet.getID());
			saveBus(bus);
		}
		updateTripFalicies();
		tripDetails = tripSheet.getData();
		return tripDetails;
		
		//havent tested.
	}

	private void updateTripFaliciesBus(ArrayList<Integer> validBusses) throws Exception {
		ArrayList<BusDTO> buslist = new ArrayList<BusDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();
		BusDTO busChanger = new BusDTO();
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				
				
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select id from bus";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {			
						int x = rs.getInt("id");
						if (validBusses.contains(x)) {
							
						} else {
							busChanger = getBus(x);
							busChanger.setTripSheetID(-1);
							saveBus(busChanger);
						}
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
				
	}
	private void updateTripFaliciesDriver(ArrayList<Integer> validDrivers) throws Exception {
		ArrayList<BusDTO> buslist = new ArrayList<BusDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();
		DriverDTO driverChanger = new DriverDTO();
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				
				
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select id from bus";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {			
						int x = rs.getInt("id");
						if (validDrivers.contains(x)) {
							
						} else {
							driverChanger = getDriver(x);
							driverChanger.setTripSheetID(-1);
							saveDriver(driverChanger);
						}
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
	}
	public void updateTripFalicies() throws Exception {
		ArrayList<Integer> validBusses = new ArrayList<Integer>();
		ArrayList<Integer> validDrivers = new ArrayList<Integer>();
		
		//Fill list of validBusses and validDrivers by scanning through the trip sheets.
		//
		ArrayList<BusDTO> buslist = new ArrayList<BusDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();
				Statement stmt = null;
				Connection conn = null;
				ResultSet rs = null;
				
				
				try {
					synchronized (serverDataSource) {
						conn = serverDataSource.getConnection();
					}
					String sql = "select id from tripsheet";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {			
						int x = rs.getInt("id");
						if (getTripSheet(x).getBusID() > 0) {
							validBusses.add(getTripSheet(x).getBusID());
						} 
						if (getTripSheet(x).getDriverID() > 0) {
							validDrivers.add(getTripSheet(x).getBusID());
						}
					}
					
					updateTripFaliciesBus(validBusses);
					updateTripFaliciesDriver(validDrivers);
					
//					srs = stmt.executeQuery("select * from StudentName");
//					while(srs.next()) {
//						//System.out.println("" + srs.);
//					}
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			config.addAnnotatedClass(TripSheet.class);
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
		TripSheetDTO tripDetails = details.getData();
		
		return tripDetails;
		
	}
}
