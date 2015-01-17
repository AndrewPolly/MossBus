package com.mossbuss.webapp.client.ui.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.google.gwt.user.client.ui.CheckBox;

public class UserEditor extends Composite {

	private static UserEditorUiBinder uiBinder = GWT.create(UserEditorUiBinder.class);
	
	private DriverDTO driverDetails;
	private int driverID;
	@UiField TextBox email;
	@UiField TextBox password;
	@UiField TextBox name;
	@UiField Label idLabel;
	@UiField CheckBox adminCheck;
	@UiField Button saveButton;
	@UiField Button cancelButton;

	interface UserEditorUiBinder extends UiBinder<Widget, UserEditor> {
	}

	public UserEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		driverID = -1;
	}

	public DriverDTO getDriverdetails() {
		driverDetails = new DriverDTO();
		if(driverID > 0){
			driverDetails.setID(driverID);
		}
		driverDetails.setEmailAddress(email.getText());
		driverDetails.setPassword(password.getText());
		driverDetails.setName(name.getText());
		driverDetails.setAdmin(adminCheck.getValue());
		return driverDetails;
	}

	public void setUserdetails(DriverDTO driverDetails) {
		this.driverDetails = driverDetails;
		setUserID(driverDetails.getID());
		email.setText(driverDetails.getEmailAddress());
		password.setText(driverDetails.getPassword());
		name.setText(driverDetails.getName());
		adminCheck.setValue(driverDetails.getAdmin());
	}

	public int getUserID() {
		return driverID;
	}

	public void setUserID(int userID) {
		this.driverID = userID;
		idLabel.setText(String.valueOf(userID));
	}

	public Button getSaveButton(){
		return saveButton;
	}

	public boolean validateInput() {
		boolean valid = true;
		if(email.getText() == null || email.getText().length() < 2){
			Window.alert("Please enter a valid email address or User Name.");
			valid = false;
			email.setFocus(true);
			email.selectAll();
		} else if(password.getText() == null || password.getText().length() < 2){
			Window.alert("Please enter a valid Password.");
			valid = false;
			password.setFocus(true);
			password.selectAll();
		} else if(name.getText() == null || name.getText().length() < 2){
			Window.alert("Please enter a valid Name.");
			valid = false;
			name.setFocus(true);
			name.selectAll();
		}
		return valid;
	}
}
