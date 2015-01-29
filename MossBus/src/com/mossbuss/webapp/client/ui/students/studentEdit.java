package com.mossbuss.webapp.client.ui.students;


import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;

public class studentEdit extends Composite {

	private static studentEditUiBinder uiBinder = GWT.create(studentEditUiBinder.class);
	
	private ClientDTO clientDetails = new ClientDTO();
	
	private ArrayList<String> Students = new ArrayList<String>();
	private StudentDTO studentDetails;
	@UiField TabPanel tPanel;
	@UiField TextBox addressField;

	@UiField TextBox parentName;
	@UiField TextBox emailAddress;


	@UiField TextBox cellNumber;
	@UiField TextBox studentNames;
	@UiField Button addStudentButton;
	@UiField Button saveButton;
	@UiField Button nextButton;
	@UiField Button cancelButton;
	@UiField Label errorLabel;


	interface studentEditUiBinder extends UiBinder<Widget, studentEdit> {
	}

	public studentEdit() {
		initWidget(uiBinder.createAndBindUi(this));
		//Create Customer Edit Interface based on CustomerDTO
		tPanel.selectTab(0);
	
	}
	
	public Button getNextButton() {
		return nextButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
	public Button getAddStudentButton() {
		return addStudentButton;
	}
	public Button getSaveButton() {
		return saveButton;
	}

	public ClientDTO getClientDetails() {
		clientDetails.setAddress(addressField.getText());
		clientDetails.setParentName(parentName.getText());
		clientDetails.setEmailAddress(emailAddress.getText());
		clientDetails.setCellNumber(cellNumber.getText());
		
		
		clientDetails.setAccBal(0);
		
		return clientDetails;
	}

	public void setClientDetails(ClientDTO clientDetails) {
		this.clientDetails = clientDetails;
		parentName.setText(clientDetails.getParentName());
		clientDetails.setParentName(parentName.getText());
		
		emailAddress.setText(clientDetails.getEmailAddress());
		cellNumber.setText(clientDetails.getCellNumber());
		addressField.setText(clientDetails.getAddress());
	}
	public StudentDTO getStudentDetails() {
		studentDetails.setParentName(clientDetails.getParentName());
		studentDetails.setParentID(clientDetails.getID());
		studentDetails.setStudentName(studentNames.getText());
		studentDetails.setAddress(clientDetails.getAddress());
		return studentDetails;
	}

	public void setStudentDetails(StudentDTO studentDetails) {
		this.studentDetails = studentDetails;
		parentName.setText(studentDetails.getParentName());
		studentNames.setText(studentDetails.getStudentName());
	}
	public void selectTpanel(int selector) {
		tPanel.selectTab(selector);
	}
	@UiHandler("backbutton1")
	void onBackbutton1Click(ClickEvent event) {
		tPanel.selectTab(0);
	}
	@UiHandler("nextButton2")
	void onNextButton2Click(ClickEvent event) {
		tPanel.selectTab(2);
	}
	@UiHandler("backButton3")
	void onBackButton3Click(ClickEvent event) {
		tPanel.selectTab(1);
	}
}

