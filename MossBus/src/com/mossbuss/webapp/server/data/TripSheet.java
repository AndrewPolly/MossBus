package com.mossbuss.webapp.server.data;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.TripSheetDTO;

@Entity
@Table(name="TripSheet")
public class TripSheet {

	private int id;
	private int driverID;
	private int busID;
	private String tripName;
	private String driverName;
	
	
	public TripSheet() {
		
	}
	
	@Column(name="DriverID")
	public int getDriverID() {
		return driverID;
	}
	public void setDriverID(int DriverID) {
		driverID = DriverID;
	}
	
	@Column(name="BusID")
	public int getBusID() {
		return busID;
	}
	public void setBusID(int BusID) {
		busID = BusID;
	}
	
	@Column(name="TripName")
	public String getTripName() {
		return tripName;
	}
	public void setTripName(String TripName) {
		tripName = TripName;
	}
	
	@Column(name="DriverName")
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String DriverName) {
		driverName = DriverName;
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
	public TripSheetDTO getData(){
		TripSheetDTO TripSheet = new TripSheetDTO();
		TripSheet.setID(this.getID());
		TripSheet.setDriverID(this.getDriverID());
		TripSheet.setBusID(this.getBusID());
		TripSheet.setTripName(this.getTripName());
		TripSheet.setDriverName(this.getDriverName());
		return TripSheet;
	}
	
	@Transient
	public void setData(TripSheetDTO TripSheet){
		this.setID(TripSheet.getID());
		this.setDriverID(TripSheet.getDriverID());
		this.setBusID(TripSheet.getBusID());
		this.setTripName(TripSheet.getTripName());
		this.setDriverName(TripSheet.getDriverName());
	}
	
}
