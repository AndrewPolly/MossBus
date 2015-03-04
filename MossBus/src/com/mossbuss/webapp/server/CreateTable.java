package com.mossbuss.webapp.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.mossbuss.webapp.server.data.Attendance;
import com.mossbuss.webapp.server.data.Driver;
import com.mossbuss.webapp.server.data.Bus;
import com.mossbuss.webapp.server.data.Client;
import com.mossbuss.webapp.server.data.MaintenanceRecord;
import com.mossbuss.webapp.server.data.Student;
import com.mossbuss.webapp.server.data.TripSheet;


@SuppressWarnings("deprecation")
public class CreateTable {

	public static void main(String[] args) {
		doReset();
		createAdmin("2andrewp2@gmail.com", "Andrew", "Ghosty678");
		createAdmin("diteremail@email.com", "Dieter", "Dieter4");
		createAdmin("AjLolHemAdemail@email.com", "Aj", "Dieter4");
		createAdmin("DebbieGGemail@email.com", "Debbie", "Dieter4");
		createAdmin("someoneemail@email.com", "Another", "Dieter4");
		createBus("Bus1");
		createBus("AJS POES BUS");
		createBus("Bus 3");
		createBus("Stevie wonder");
		System.out.println("Completed");
	}
	public static void doReset(){
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
//		createAdmin("testAdmin@test.com", "Andrew", "test");
	}
	public static void createBus(String BusName) {
		Bus newBus = new Bus();
		newBus.setBusName(BusName);
		newBus.setDriverID(-1);
		newBus.setTripSheetID(-1);
		
		Session session = null;
		try{
			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(Bus.class);
			config.configure();
			SessionFactory factory = config.buildSessionFactory();
			session = factory.getCurrentSession();
			session.beginTransaction();
			session.save(newBus);
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
}
