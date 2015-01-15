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
	private ArrayList<Integer> studentsID = new ArrayList<Integer>();
	
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
	
	@Column(name="StudentsID")
	public ArrayList<Integer> getStudentsID() {
		return studentsID;
	}
	public void setStudentsID(ArrayList<Integer> StudentsID) {
		studentsID = StudentsID;
	}
	public void addStudentID(int StudentID) {
		studentsID.add(StudentID);
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
		TripSheet.setStudentsID(this.getStudentsID());
		return TripSheet;
	}
	
	@Transient
	public void setData(TripSheetDTO TripSheet){
		this.setID(TripSheet.getID());
		this.setDriverID(TripSheet.getDriverID());
		this.setBusID(TripSheet.getBusID());
		this.setTripName(TripSheet.getTripName());
		this.setDriverName(TripSheet.getDriverName());
		this.setStudentsID(TripSheet.getStudentsID());
	}
	
}
