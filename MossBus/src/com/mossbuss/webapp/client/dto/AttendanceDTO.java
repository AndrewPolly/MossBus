package com.mossbuss.webapp.client.dto;

import java.io.Serializable;
import java.util.Date;

public class AttendanceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Date dateAttended;
	private int studentID;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public Date getDateAttended() {
		return dateAttended;
	}

	public void setDateAttended(Date newDate) {
		this.dateAttended = newDate;
	}

	public void setStudentID(int newID) {
		this.studentID = newID;
	}

	public int getStudentID() {
		return studentID;
	}


}
