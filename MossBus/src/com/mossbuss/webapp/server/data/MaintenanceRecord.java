package com.mossbuss.webapp.server.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.MaintenanceRecordDTO;

@Entity
@Table(name="MaintenanceRecord")
public class MaintenanceRecord {

	private int id;
	private int busID;
	private Date maintenanceDate;
	private String notes;
	
	
	public MaintenanceRecord() {
		
	}
	
	@Column(name="BusID")
	public int getBusID() {
		return busID;
	}
	public void setBusID(int BusID) {
		busID = BusID;
	}
	
	@Column(name="MaintenanceDate")
	public Date getDate() {
		return maintenanceDate;
	}
	public void setDate(Date MaintenanceDate) {
		maintenanceDate = MaintenanceDate;
	}
	
	@Column(name="Notes")
	public String getNotes() {
		return notes;
	}
	public void setNotes(String Notes) {
		notes = Notes;
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
	public MaintenanceRecordDTO getData(){
		MaintenanceRecordDTO Maintenance = new MaintenanceRecordDTO();
		Maintenance.setID(this.getID());
		Maintenance.setBusID(this.getBusID());
		Maintenance.setDate(this.getDate());
		Maintenance.setNotes(this.getNotes());
		return Maintenance;
	}
	
	@Transient
	public void setData(MaintenanceRecordDTO Maintenance){
		this.setID(Maintenance.getID());
		this.setBusID(Maintenance.getBusID());
		this.setDate(Maintenance.getDate());
		this.setNotes(Maintenance.getNotes());
	}
}
