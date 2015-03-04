package com.mossbuss.webapp.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.eclipse.jetty.util.log.StdErrLog;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.marre.SmsSender;

import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.dto.AttendanceDTO;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.MaintenanceRecordDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.shared.FieldVerifier;
import com.mossbuss.webapp.server.data.Attendance;
import com.mossbuss.webapp.server.data.Driver;
import com.mossbuss.webapp.server.data.Client;
import com.mossbuss.webapp.server.data.Bus;
import com.mossbuss.webapp.server.data.MaintenanceRecord;
import com.mossbuss.webapp.server.data.Student;
import com.mossbuss.webapp.server.data.TripSheet;
import com.google.gwt.user.client.Window;
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
		} catch (ClassNotFoundException e1) {
			System.out.println("serverDataSource Error: " + e1.getMessage());
			e1.printStackTrace();
		}
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	
	public static void createAdmin(String EmailAddress, String UserName, String Password){
		Driver admin = new Driver();
		admin.setAdmin(true);
		admin.setEmailAddress(EmailAddress);
		admin.setName(UserName);
		admin.setPassword(Password);
		admin.setBusID(-1);
		admin.setTripSheetID(-1);
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Driver.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.save(admin);
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
	public void resetDatabase(String string) throws Exception {
		//Start Hibernate
				AnnotationConfiguration config = new AnnotationConfiguration();
				
				//Tables:
				config.addAnnotatedClass(Driver.class);
				config.addAnnotatedClass(Bus.class);
				config.addAnnotatedClass(Client.class);
				config.addAnnotatedClass(MaintenanceRecord.class);
				config.addAnnotatedClass(TripSheet.class);
				config.addAnnotatedClass(Student.class);
				config.addAnnotatedClass(Attendance.class);


					//Action
				config.configure();
				new SchemaExport(config).create(true, true);
				createAdmin("qatiqa@gmail.com", "Dieter", "dtb1007ivy");

	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	@Override
	public void checkClientsForSms() throws Exception {
		
		final ArrayList<ClientDTO> clientList;

		clientList = getAllClients();
		Date today = new Date();
		for (int i = 0; i < clientList.size(); i++) {
			// minutes changed to DAYS ... TimeUnit.DAYS
			System.out.println("in server checking client: " + clientList.get(i).getParentName());
			System.out.println("The date difference is: " + getDateDiff(clientList.get(i).getDateLastDebited(), today,
					TimeUnit.DAYS));
//			System.out.println("The date difference is: " + getDateDiff(today, clientList.get(i).getDateLastDebited(),
//					TimeUnit.MINUTES));
			System.out.println("checking if date dif > " + (float) 30 / clientList.get(i).getPayOption());
			if (getDateDiff(clientList.get(i).getDateLastDebited(), today,
					TimeUnit.DAYS) > (float) 30
					/ clientList.get(i).getPayOption()) {
				// update users accBal and send sms.
				float perMonthPayment = -350;
				float account = clientList.get(i).getAccBal();
				account = account + perMonthPayment
						/ (float)clientList.get(i).getPayOption();
				System.out.println("just deducted funds from account new balance is :" + account);
				clientList.get(i).setAccBal(account);
				clientList.get(i).setDateLastDebited(today);
				System.out.println("new date for client is : " + clientList.get(i).getDateLastDebited());
				System.out.println("new date today for client is : " + today);
				
				saveClient(clientList.get(i));
				if (clientList.get(i).getAccBal() < 0) {
					ArrayList<String> payOptions = new ArrayList<String>();
					payOptions.add("monthly");
					payOptions.add("semi-monthly");
					payOptions.add("unknown");
					payOptions.add("weekly");
					// sending sms is just for convenience at this point in
					// time.
					String message = "Hello "
							+ clientList.get(i).getParentName()
							+ ", this is a friendly reminder that your MossBus Charter Services has been debitted of: R "
							+ perMonthPayment
							/ clientList.get(i).getPayOption()
							+ ", for your "
							+ payOptions
									.get(clientList.get(i).getPayOption() - 1)
							+ " subscription. Your new balance is: R "
							+ clientList.get(i).getAccBal();
					System.out.println(message + " \n message length is: "
							+ message.length());
					sendSMS(message, clientList.get(i).getCellNumber());
				}
			}

		}

	}

	@Override
	public DriverDTO doLogin(DriverDTO driverDTO) throws Exception {
		// Login

		Session session = null;
		Driver driver = new Driver();

		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Driver.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			String hql = "from Driver where EmailAddress = '"
					+ driverDTO.getEmailAddress() + "' AND Password = '"
					+ driverDTO.getPassword() + "'";

			Query query = session.createQuery(hql);
			List userList = query.list();
			if (userList == null || userList.size() < 1) {
				throw new Exception("Authentication Error");
			} else {
				driver = (Driver) userList.get(0);
			}

		} catch (Exception e) {
			throw new Exception("Authentication Error");
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		// REMOVE THIS AND TEST IF IT WORKS>>>>>
		// DriverDTO test = new DriverDTO();
		// test.setAdmin(true);
		// test.setBusID(-1);
		// test.setEmailAddress("2andrewp2@gmail.com");
		// test.setName("Andrew");
		// test.setPassword("Ghosty678");
		// test.setTripSheetID(-1);
		return driver.getData();

	}

	// cell number must be of international format. i.e. 27 (number excluding
	// 0).
	public void sendSMS(String MSG, String cellNumber) throws Exception {
		SmsSender smsSender = SmsSender.getClickatellSender(
				"mossbus", "KORYRPdcNcDAJY", "3529505");

		String msg = MSG;
		String reciever = cellNumber;
		smsSender.connect();
		String msgids = smsSender.sendTextSms(msg, reciever);
		smsSender.disconnect();

	}

	@Override
	public StudentDTO saveStudent(StudentDTO studentDetails) throws Exception {
		Student student = new Student();
		student.setData(studentDetails);
		System.out.println("XXXXXXXXXXXDDDXXDDDX "
				+ studentDetails.getStudentName());
		// Save
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Student.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (student.getID() > 0) {
				session.update(student);
			} else {
				session.save(student);
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}
		studentDetails = student.getData();
		return studentDetails;

		// havent tested.
	}
	@Override
	public void saveAttendance(AttendanceDTO attendanceDetails) throws Exception {
		Attendance attendance = new Attendance();
		attendance.setData(attendanceDetails);
		
		// Save
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Attendance.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (attendance.getID() > 0) {
				session.update(attendance);
			} else {
				session.save(attendance);
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}
		

		// havent tested.
	}

	@Override
	public ArrayList<String> updateContactNameOracle(String sql)
			throws Exception {
		ArrayList<String> oracle = new ArrayList<String>();
		// Do Lookup
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

			while (rs.next()) {
				oracle.add(rs.getInt("id") + " :: " + rs.getString("Address")
						+ "; " + rs.getString("StudentName"));
			}

			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return oracle;
		// ??????? does nothing...
		// ok it will work the same now..
		// the connection is not using Hibernate which is why it needs a
		// datasource file.
		// yeah what i mainly wanted to know is how to handle the DB calls
		// "customer Like etc"
		// must i still do this method? ofcourse im sure..
		// yes.. without it you won't get suggestions
		// okay awesome, i wanna get this and printing of trip sheets done by
		// the end of today..
	}

	@Override
	public ArrayList<String> updateTripSheetNameOracle(String sql)
			throws Exception {
		ArrayList<String> oracle = new ArrayList<String>();
		// Do Lookup
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

			while (rs.next()) {
				oracle.add(rs.getInt("id") + " :: " + rs.getString("TripName")
						+ "; " + rs.getString("DriverName"));
			}

			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return oracle;
		// ??????? does nothing...
		// ok it will work the same now..
		// the connection is not using Hibernate which is why it needs a
		// datasource file.
		// yeah what i mainly wanted to know is how to handle the DB calls
		// "customer Like etc"
		// must i still do this method? ofcourse im sure..
		// yes.. without it you won't get suggestions
		// okay awesome, i wanna get this and printing of trip sheets done by
		// the end of today..
	}

	@Override
	public StudentDTO getStudent(int selectedID) throws Exception {
		Student details = new Student();
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Student.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(details, selectedID);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		StudentDTO customerDetails = details.getData();

		return customerDetails;

	}

	@Override
	public ArrayList<StudentDTO> getStudentsFromParent(int selectedParentID)
			throws Exception {
		ArrayList<StudentDTO> studentlist = new ArrayList<StudentDTO>();

		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ResultSet srs = null;
		try {
			synchronized (serverDataSource) {
				conn = serverDataSource.getConnection();
			}
			String sql = "select id from student where ParentID Like '"
					+ selectedParentID + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				studentlist.add(getStudent(rs.getInt("id")));
			}

			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return studentlist;
	}

	// if tripsheetID is -2, select ALL THE DRIVERS.
	@Override
	public ArrayList<DriverDTO> getAllDrivers(int TripSheetID) throws Exception {
		ArrayList<DriverDTO> driverlist = new ArrayList<DriverDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();

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
			if (TripSheetID == -2) {

				while (rs.next()) {

					int x = rs.getInt("id");
					System.out.println("IN GET ALL DRIVERS !!!!!!!! trip id -2"
							+ x);

					// DriverDTO realDriver = new DriverDTO();
					// realDriver.setAdmin(getDriver(x).getAdmin());
					// realDriver.setBusID(getDriver(x).getBusID());
					// realDriver.setEmailAddress(getDriver(x).getEmailAddress());
					// realDriver.setID(getDriver(x).getID());
					// realDriver.setName(getDriver(x).getName());
					// realDriver.setPassword(getDriver(x).getPassword());
					// realDriver.setTripSheetID(getDriver(x).getTripSheetID());
					driverlist.add(getDriver(x));

				}
			} else {

				while (rs.next()) {

					int x = rs.getInt("id");
					System.out.println("IN GET ALL DRIVERS !!!!!!!! " + x);
					if (getDriver(x).getTripSheetID() == TripSheetID) {
						DriverDTO realDriver = new DriverDTO();
						realDriver.setAdmin(getDriver(x).getAdmin());
						realDriver.setBusID(getDriver(x).getBusID());
						realDriver.setEmailAddress(getDriver(x)
								.getEmailAddress());
						realDriver.setID(getDriver(x).getID());
						realDriver.setName(getDriver(x).getName());
						realDriver.setPassword(getDriver(x).getPassword());
						realDriver
								.setTripSheetID(getDriver(x).getTripSheetID());

						driverlist.add(0, realDriver);
					} else if (getDriver(x).getTripSheetID() < 0) {
						DriverDTO realDriver = new DriverDTO();
						realDriver.setAdmin(getDriver(x).getAdmin());
						realDriver.setBusID(getDriver(x).getBusID());
						realDriver.setEmailAddress(getDriver(x)
								.getEmailAddress());
						realDriver.setID(getDriver(x).getID());
						realDriver.setName(getDriver(x).getName());
						realDriver.setPassword(getDriver(x).getPassword());
						realDriver
								.setTripSheetID(getDriver(x).getTripSheetID());
						driverlist.add(realDriver);
					}
				}
			}
			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return driverlist;
	}

	@Override
	public ArrayList<TripSheetDTO> getAllTripSheets() throws Exception {
		ArrayList<TripSheetDTO> tripList = new ArrayList<TripSheetDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();

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
			String sql = "select id from tripsheet";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			System.out.println("33333333");

			while (rs.next()) {

				int x = rs.getInt("id");
				System.out.println("IN GET ALL DRIVERS !!!!!!!! " + x);
				tripList.add(getTripSheet(x));
			}

			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return tripList;
	}

	// if tripsheetID is -2, select ALL THE DRIVERS.
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
			if (TripSheetID == -2) {
				while (rs.next()) {
					int x = rs.getInt("id");
					buslist.add(getBus(x));
				}
			} else {
				while (rs.next()) {
					int x = rs.getInt("id");
					if (getBus(x).getTripSheetID() == TripSheetID) {
						buslist.add(0, getBus(x));
					} else if (getBus(x).getTripSheetID() < 0) {
						buslist.add(getBus(x));
					}
				}
			}
			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return buslist;
	}

	public DriverDTO getDriver(int selectedID) throws Exception {
		Driver details = new Driver();
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Driver.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(details, selectedID);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		DriverDTO customerDetails = details.getData();

		return customerDetails;

	}

	@Override
	public ArrayList<StudentDTO> getStudentsFromTripSheet(
			int selectedTripSheetID) throws Exception {
		ArrayList<StudentDTO> studentlist = new ArrayList<StudentDTO>();

		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ResultSet srs = null;
		try {
			synchronized (serverDataSource) {
				conn = serverDataSource.getConnection();
			}
			String sql = "select id from student where TripSheetID Like '"
					+ selectedTripSheetID + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				studentlist.add(getStudent(rs.getInt("id")));
			}

			// srs = stmt.executeQuery("select * from StudentName");
			// while(srs.next()) {
			// //System.out.println("" + srs.);
			// }

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
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

		// Save
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Driver.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (driver.getID() > 0) {
				session.update(driver);
			} else {
				session.save(driver);
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}

		// havent tested.

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
			System.out.println("FFFFFFFFFFFFFFFF driver ID : "
					+ tripSheet.getDriverID());
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
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Bus.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (newBus.getID() > 0) {
				session.update(newBus);
			} else {
				session.save(newBus);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}

	}

	@Override
	public BusDTO getBus(int selectedID) throws Exception {
		Bus bus = new Bus();
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Bus.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(bus, selectedID);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());

		} finally {
			if (session != null && session.isOpen()) {
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

		// Save
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(TripSheet.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (tripSheet.getID() > 0) {
				session.update(tripSheet);
			} else {
				session.save(tripSheet);
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}
		if (tripSheet.getDriverID() > 0) {
			driver = getDriver(tripSheet.getDriverID());
			if (tripSheet.getBusID() > 0) {
				driver.setBusID(tripSheet.getBusID());
			}
			driver.setTripSheetID(tripSheet.getID());
			saveDriver(driver);
		}
		if (tripSheet.getBusID() > 0) {
			bus = getBus(tripSheet.getBusID());
			if (tripSheet.getDriverID() > 0) {
				bus.setDriverID(tripSheet.getDriverID());
			}
			bus.setTripSheetID(tripSheet.getID());
			saveBus(bus);
		}

		tripDetails = tripSheet.getData();
		return tripDetails;

		// havent tested.
	}

	@Override
	public ClientDTO saveClient(ClientDTO clientDetails) throws Exception {
		Client client = new Client();
		client.setData(clientDetails);
		
		// Save
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Client.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (client.getID() > 0) {
				session.update(client);
			} else {
				session.save(client);
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}
		clientDetails = client.getData();
		return clientDetails;

		// havent tested.
	}

	@Override
	public ClientDTO getClient(int selectedID) throws Exception {
		Client details = new Client();
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Client.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(details, selectedID);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		ClientDTO customerDetails = details.getData();

		return customerDetails;

	}

	public ArrayList<ClientDTO> getAllClients() throws Exception {
		ArrayList<ClientDTO> clientlist = new ArrayList<ClientDTO>();

		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			synchronized (serverDataSource) {
				conn = serverDataSource.getConnection();

			}
			String sql = "select id from client";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {

				int x = rs.getInt("id");

				clientlist.add(getClient(x));

			}

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return clientlist;
	}

	@Override
	public TripSheetDTO getTripSheet(int selectedID) throws Exception {

		TripSheet details = new TripSheet();
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(TripSheet.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(details, selectedID);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		TripSheetDTO tripDetails = details.getData();

		return tripDetails;

	}

	@Override
	public MaintenanceRecordDTO saveMaintenanceRecord(
			MaintenanceRecordDTO recordDetails) throws Exception {
		MaintenanceRecord record = new MaintenanceRecord();
		record.setData(recordDetails);

		// Save
		Session session = null;
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(MaintenanceRecord.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			if (record.getID() > 0) {
				session.update(record);
			} else {
				session.save(record);
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			throw new Exception(e.getMessage() + e.getCause());
		} finally {
			if (session != null && session.isOpen()) {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
		}
		recordDetails = record.getData();
		return recordDetails;

		// havent tested.
	}

	@Override
	public ArrayList<MaintenanceRecordDTO> getRecordsFromBus(int BusID)
			throws Exception {
		ArrayList<MaintenanceRecordDTO> recordlist = new ArrayList<MaintenanceRecordDTO>();
		ArrayList<Integer> checker = new ArrayList<Integer>();

		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;

		System.out.println("111111 getting records");
		try {
			synchronized (serverDataSource) {
				conn = serverDataSource.getConnection();

			}
			String sql = "select id from maintenancerecord order by id desc";
			System.out.println("111111 attempting communication");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			System.out.println("33333333");
			if (BusID == -2) {

				while (rs.next()) {

					int x = rs.getInt("id");
					System.out.println("IN GET ALL DRIVERS !!!!!!!! trip id -2"
							+ x);

					recordlist.add(getMaintenanceRecord(x));

				}
			} else {

				while (rs.next()) {
					System.out
							.println("11111 GOING THROUGH RESULTS in recorrds");
					int x = rs.getInt("id");
					System.out.println("IN GET ALL DRIVERS !!!!!!!! " + x);
					if (getMaintenanceRecord(x).getBusID() == BusID) {

						recordlist.add(getMaintenanceRecord(x));
					}
				}
			}

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ex) {
				}
				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
				conn = null;
			}
		}
		return recordlist;
	}

	private MaintenanceRecordDTO getMaintenanceRecord(int selectedID)
			throws Exception {
		System.out.println("Getting a main record");
		MaintenanceRecord record = new MaintenanceRecord();
		Session session = null;

		try {
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(MaintenanceRecord.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.load(record, selectedID);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			throw new Exception(e.getMessage());

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		MaintenanceRecordDTO recordDetails = record.getData();

		return recordDetails;
	}

}
