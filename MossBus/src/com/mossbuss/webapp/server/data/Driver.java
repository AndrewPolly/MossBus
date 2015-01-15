package com.mossbuss.webapp.server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.DriverDTO;

@Entity
@Table(name="Driver")
public class Driver {
	
	private int id;
	private int busID;				// the bus the driver is driving
	private int tripSheetID;		// the trip sheet the driver is assigned to
	private String emailAddress; 	// used as login name
	private String password;
	private String name;
	private Boolean admin;
	
	public Driver() {
		
	}
	
	@Column(name="BusID")
	public int getBusID() {
		return busID;
	}
	public void setBusID(int BusID) {
		busID = BusID;
	}
	
	@Column(name="TripSheetID")
	public int getTripSheetID() {
		return tripSheetID;
	}
	public void setTripSheetID(int TripSheetID) {
		tripSheetID = TripSheetID;
	}
	
	@Column(name="EmailAddress")
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String EmailAddress) {
		emailAddress = EmailAddress;
	}
	
	@Column(name="Password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String Password) {
		password = Password;
	}
	
	@Column(name="DriverName")
	public String getName() {
		return name;
	}
	public void setName(String Name) {
		name = Name;
	}
	
	@Column(name="Admin")
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean Admin) {
		admin = Admin;
	}
	
	@Id
	@GeneratedValue
	@Column(name="id")
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	
	@Transient
	public DriverDTO getData(){
		DriverDTO driver = new DriverDTO();
		driver.setID(this.getID());
		driver.setBusID(this.getBusID());
		driver.setTripSheetID(this.getTripSheetID());
		driver.setEmailAddress(this.getEmailAddress());
		driver.setPassword(this.getPassword());
		driver.setName(this.getName());
		driver.setAdmin(this.getAdmin());
		return driver;
	}
	
	@Transient
	public void setData(DriverDTO driver){
		this.setID(driver.getID());
		this.setBusID(driver.getBusID());
		this.setTripSheetID(driver.getTripSheetID());
		this.setEmailAddress(driver.getEmailAddress());
		this.setPassword(driver.getPassword());
		this.setName(driver.getName());
		this.setAdmin(driver.getAdmin());
	}
}
