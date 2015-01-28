package com.mossbuss.webapp.server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.BusDTO;


@Entity
@Table(name="Bus")
public class Bus {
	
	private int id;
	private int driverID;
	private int tripSheetID;
	private String busName;
	
	public Bus() {
		
	}
	
	@Column(name="DriverID")
	public int getDriverID() {
		return driverID;
	}
	public void setDriverID(int DriverID) {
		driverID = DriverID;
	}
	
	@Column(name="TripSheetID")
	public int getTripSheetID() {
		return tripSheetID;
	}
	public void setTripSheetID(int TripSheetID) {
		tripSheetID = TripSheetID;
	}
	
	@Column(name="BusName")
	public String getBusName() {
		return busName;
	}
	public void setBusName(String BusName) {
		busName = BusName;
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
	public BusDTO getData(){
		BusDTO bus = new BusDTO();
		bus.setID(this.getID());
		bus.setDriverID(this.getDriverID());
		bus.setTripSheetID(this.getTripSheetID());
		bus.setBusName(this.getBusName());
		return bus;
	}
	
	@Transient
	public void setData(BusDTO bus){
		this.setID(bus.getID());
		this.setDriverID(bus.getDriverID());
		this.setTripSheetID(bus.getTripSheetID());
		this.setBusName(bus.getBusName());
	}
}
//verry good...

