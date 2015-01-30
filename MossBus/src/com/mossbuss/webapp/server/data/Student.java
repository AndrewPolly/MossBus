package com.mossbuss.webapp.server.data;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.StudentDTO;

@Entity
@Table(name="Student")
public class Student {

	
	private int id;
	private int driverID;
	private int tripSheetID;
	private int parentID;
	private String parentName;
	private String studentName;
	private String Address;
	
	public Student() {
		
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
		this.tripSheetID = TripSheetID;
	}
	@Column(name="ParentID")
	public int getParentID() {
		return parentID;
	}
	public void setParentID(int ParentID) {
		this.parentID = ParentID;
	}
	
	@Column(name="ParentName")
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String ParentName) {
		this.parentName = ParentName;
	}
	@Column(name="StudentName")
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String StudentName) {
		studentName = StudentName;
	}
	
	@Column(name="Address")
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		this.Address = address;
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
	public StudentDTO getData(){
		StudentDTO student = new StudentDTO();
		student.setID(this.getID());
		student.setStudentName(this.getStudentName());
		student.setDriverID(this.getDriverID());
		student.setParentID(this.getParentID());
		student.setParentName(this.getParentName());
		student.setTripSheetID(this.getTripSheetID());
		student.setAddress(this.getAddress());
		return student;
	}

	@Transient
	public void setData(StudentDTO student){
		this.setID(student.getID());
		this.setStudentName(student.getStudentName());
		this.setDriverID(student.getDriverID());
		this.setParentID(student.getParentID());
		this.setParentName(student.getParentName());
		this.setTripSheetID(student.getTripSheetID());
		this.setAddress(student.getAddress());
	}
}
