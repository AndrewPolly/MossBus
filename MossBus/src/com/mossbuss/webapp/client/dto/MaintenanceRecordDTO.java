package com.mossbuss.webapp.client.dto;

import java.io.Serializable;
import java.util.Date;

public class MaintenanceRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int busID;
	private Date maintenanceDate;
	private String notes;
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public void setBusID(int busID) {
		this.busID = busID;
	}
	
	public int getBusID() {
		return busID;
	}
	
	public void setDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}
	
	public Date getDate() {
		return maintenanceDate;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getNotes() {
		return notes;
	}
}