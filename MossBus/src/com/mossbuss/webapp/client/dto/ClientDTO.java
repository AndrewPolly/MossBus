package com.mossbuss.webapp.client.dto;

import java.io.Serializable;
/**	
 * 	Data Transfer Object for clients.
 * 		Clients are the parents of the children, this holds all there 
 * 		appropriate information.
 */
public class ClientDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int payOption;			// The payment option of the client
	private float accBal;			// The account balance of the client
	private String emailAddress; 
	private String parentName;
	private String cellNumber;
	private String Address;
	
	
	public int getID() {
		return id;
	}
	public void setID(int ID) {
		this.id = ID;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String EmailAddress) {
		this.emailAddress = EmailAddress;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String ParentName) {
		this.parentName = ParentName;
	}
	public float getAccBal() {
		return accBal;
	}
	public void setAccBal(float AccBal) {
		this.accBal = AccBal;
	}
	public void setCellNumber(String CellNumber) {
		this.cellNumber = CellNumber;
	}
	public String getCellNumber() {
		return cellNumber;
	}
	public void setPayOption(int PayOption) {
		this.payOption = PayOption;
	}
	public int getPayOption() {
		return payOption;
	}
	public void setAddress(String address) {
		this.Address = address;
	}
	public String getAddress() {
		return Address;
	}
}
