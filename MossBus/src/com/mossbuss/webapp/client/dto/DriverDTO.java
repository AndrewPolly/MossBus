package com.mossbuss.webapp.client.dto;

import java.io.Serializable;

public class DriverDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int busID;				// the bus the driver is driving
	private int tripSheetID;		// the trip sheet the driver is assigned to
	private String emailAddress; 	// used as login name
	private String password;
	private String name;
	private Boolean admin;
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	public void setBusID(int busID) {
		this.busID = busID;
	}
	public int getBusID() {
		return busID;
	}
	public void setTripSheetID(int tripSheetID) {
		this.tripSheetID = tripSheetID;
	}
	public int getTripSheetID(){
		return tripSheetID;
	}
}
