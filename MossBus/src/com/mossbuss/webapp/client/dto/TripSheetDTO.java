package com.mossbuss.webapp.client.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class TripSheetDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int driverID;
	private int busID;
	private String tripName;
	private String driverName;
	private ArrayList<Integer> studentsID = new ArrayList<Integer>();
	
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
	
	public int getBusID() {
		return busID;
	}
	
	public void setBusID(int busID) {
		this.busID = busID;
	}
	
	public String getTripName() {
		return tripName;
	}
	
	public void setTripName(String tripName) {
		this.tripName = tripName;
	}
	
	public String getDriverName() {
		return driverName;
	}
	
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
//	public ArrayList<String> getStudents() {
//		return students;
//	}
//	
//	public void addStudent(String studentName) {
//		this.students.add(studentName);
//	}
	
	public ArrayList<Integer> getStudentsID() {
		return studentsID;
	}
	
	public void setStudentsID(ArrayList<Integer> StudentsID) {
		studentsID = StudentsID;
	}
	
	public void addStudentID(int studentID) {
		this.studentsID.add(studentID);
	}
	
}
