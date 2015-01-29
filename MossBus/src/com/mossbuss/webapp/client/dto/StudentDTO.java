package com.mossbuss.webapp.client.dto;

import java.io.Serializable;
/**	
 * 	Data Transfer Object for clients.
 * 		Clients are the parents of the children, this holds all there 
 * 		appropriate information.
 */
public class StudentDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int tripSheetID;		// The trip sheet the student is assigned to
	private int driverID;			// the driver the student is assigned to
	private int parentID;
	private String parentName;
	private String studentName;
	private String Address;
	
	
	public int getID() {
		return id;
	}
	public void setID(int ID) {
		this.id = ID;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String ParentName) {
		this.parentName = ParentName;
	}
	public int getParentID() {
		return parentID;
	}
	public void setParentID(int ParentID) {
		this.parentID = ParentID;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public void setDriverID(int DriverID) {
		this.driverID = DriverID;
	}
	public int getDriverID() {
		return driverID;
	}
	public void setTripSheetID(int TripSheetID) {
		this.tripSheetID = TripSheetID;
	}
	public int getTripSheetID() {
		return tripSheetID;
	}
	public void setAddress(String address) {
		this.Address = address;
	}
	public String getAddress() {
		return Address;
	}
}
