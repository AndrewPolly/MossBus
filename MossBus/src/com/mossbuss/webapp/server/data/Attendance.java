package com.mossbuss.webapp.server.data;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.AttendanceDTO;
import com.mossbuss.webapp.client.dto.BusDTO;


@Entity
@Table(name="Attendance")
public class Attendance {
	
	private int id;
	private Date dateAttended;
	private int studentID;

	
	public Attendance() {
		
	}
	
	@Column(name="StudentID")
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int newID) {
		this.studentID = newID;
	}
	
	@Column(name="DateAttended")
	public Date getDateAttended() {
		return dateAttended;
	}
	public void setDateAttended(Date newDate) {
		this.dateAttended = newDate;
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
	public AttendanceDTO getData(){
		AttendanceDTO attendance = new AttendanceDTO();
		attendance.setID(this.getID());
		attendance.setStudentID(this.getStudentID());
		attendance.setDateAttended(this.getDateAttended());
		
		return attendance;
	}
	
	@Transient
	public void setData(AttendanceDTO attendance){
		this.setID(attendance.getID());
		this.setStudentID(attendance.getStudentID());
		this.setDateAttended(attendance.getDateAttended());
		
	}
}
//verry good...

