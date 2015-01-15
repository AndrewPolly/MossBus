package com.mossbuss.webapp.server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.mossbuss.webapp.client.dto.ClientDTO;

@Entity
@Table(name="Client")
public class Client {

	
	private int id;
	private int payOption;			// The payment option of the client
	private int tripSheetID;		// The trip sheet the student is assigned to
	private int driverID;			// the driver the student is assigned to
	private float accBal;			// The account balance of the client
	private String emailAddress; 
	private String studentName;
	private String parentName;
	private String cellNumber;
	
	public Client() {
		
	}
	
	@Column(name="payOption")
	public int getPayOption() {
		return payOption;
	}
	public void setPayOption(int PayOption) {
		payOption = PayOption;
	}
	
	@Column(name="TripSheetID")
	public int getTripSheetID() {
		return tripSheetID;
	}
	public void setTripSheetID(int TripSheetID) {
		tripSheetID = TripSheetID;
	}
	
	@Column(name="DriverID")
	public int getDriverID() {
		return driverID;
	}
	public void setDriverID(int DriverID) {
		driverID = DriverID;
	}
	
	@Column(name="AccountBalance")
	public float getAccBal() {
		return accBal;
	}
	public void setAccBal(float AccBal) {
		accBal = AccBal;
	}
	
	@Column(name="EmailAddress")
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String EmailAddress) {
		emailAddress = EmailAddress;
	}
	
	@Column(name="StudentName")
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String StudentName) {
		studentName = StudentName;
	}
	
	@Column(name="ParentName")
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String ParentName) {
		parentName = ParentName;
	}
	
	@Column(name="CellNumber")
	public String getCellNumber() {
		return cellNumber;
	}
	public void setCellNumber(String CellNumber) {
		cellNumber = CellNumber;
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
	public ClientDTO getData(){
		ClientDTO client = new ClientDTO();
		client.setID(this.getID());
		client.setPayOption(this.getPayOption());
		client.setTripSheetID(this.getTripSheetID());
		client.setDriverID(this.getDriverID());
		client.setAccBal(this.getAccBal());
		client.setEmailAddress(this.getEmailAddress());
		client.setStudentName(this.getStudentName());
		client.setParentName(this.getParentName());
		client.setCellNumber(this.getCellNumber());
		return client;
	}

	@Transient
	public void setData(ClientDTO client){
		this.setID(client.getID());
		this.setPayOption(client.getPayOption());
		this.setTripSheetID(client.getTripSheetID());
		this.setDriverID(client.getDriverID());
		this.setAccBal(client.getAccBal());
		this.setEmailAddress(client.getEmailAddress());
		this.setStudentName(client.getStudentName());
		this.setParentName(client.getParentName());
		this.setCellNumber(client.getCellNumber());
	}
}
