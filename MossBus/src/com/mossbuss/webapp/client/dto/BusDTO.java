package com.mossbuss.webapp.client.dto;

import java.io.Serializable;

public class BusDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int driverID;
	private int tripSheetID;
	private String busName;
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getDriverID() {
		return driverID;
	}
	
	public void setDriverID(int driverID) {
		this.driverID = driverID;
	}
	
	public void setTripSheetID(int tripSheetID) {
		this.tripSheetID = tripSheetID;
	}
	
	public int getTripSheetID() {
		return tripSheetID;
	}
	
	public void setBusName(String busName) {
		this.busName = busName;
	}
	
	public String getBusName() {
		return busName;
	}
}
