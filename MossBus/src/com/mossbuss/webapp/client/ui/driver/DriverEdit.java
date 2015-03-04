package com.mossbuss.webapp.client.ui.driver;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;

public class DriverEdit extends Composite {

	private static DriverEditUiBinder uiBinder = GWT
			.create(DriverEditUiBinder.class);

	private DriverDTO driverDetails = new DriverDTO();
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	private ArrayList<String> Students = new ArrayList<String>();
	private StudentDTO studentDetails;
	private boolean admin = false;
	@UiField
	TabPanel tPanel;
	@UiField
	TextBox passwordField;

	@UiField
	TextBox nameField;
	@UiField
	TextBox emailAddress;

	@UiField
	ListBox adminBox;
	@UiField
	ListBox busBox;
	@UiField
	ListBox tripBox;
	@UiField
	Button assignBusButton;
	@UiField
	Button assignTripButton;
	@UiField
	Button assignButton;
	@UiField
	Button cancelButton;
	@UiField
	Label errorLabel;

	interface DriverEditUiBinder extends UiBinder<Widget, DriverEdit> {
	}

	public DriverEdit() {
		initWidget(uiBinder.createAndBindUi(this));
		adminBox.addItem("False");
		adminBox.addItem("True");
		adminBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				
				if (admin == false) {
					admin = true;
				} else {
					admin = false;
				}
			}
			
			
			
		});
		tPanel.selectTab(0);

	}

//	public Button getNextButton() {
//		return nextButton;
//	}

	public Button getCancelButton() {
		return cancelButton;
	}

//	public Button getSaveButton() {
//		return saveButton;
//	}

	public DriverDTO getDriverDetails() {
		driverDetails.setPassword(passwordField.getText());
		driverDetails.setName(nameField.getText());
		driverDetails.setEmailAddress(emailAddress.getText());
		
		
		driverDetails.setAdmin(admin);
		
		if (tripBox == null) {
			driverDetails.setTripSheetID(-1);
		} else { // obviously NOT FINISHED!!!! ADD LIST BOX IMPLEMENTATIONS!!!!
			driverDetails.setTripSheetID(-1);
		}
		if (busBox == null) {
			driverDetails.setBusID(-1);
		} else { // obviously NOT FINISHED!!!! ADD LIST BOX IMPLEMENTATIONS!!!!
			driverDetails.setBusID(-1);
		}
		return driverDetails;
	}

	public void setDriverDetails(DriverDTO driverDetails) {
		this.driverDetails = driverDetails;
		// OBVIOUSLY NOT IMPLEMENTASIE!!!!!!!!!!!!!! 
//		parentName.setText(clientDetails.getParentName());
//		clientDetails.setParentName(parentName.getText());
//
//		emailAddress.setText(clientDetails.getEmailAddress());
//		cellNumber.setText(clientDetails.getCellNumber());
//		addressField.setText(clientDetails.getAddress());
	}

//	public StudentDTO getStudentDetails() {
//		studentDetails.setParentName(clientDetails.getParentName());
//		studentDetails.setParentID(clientDetails.getID());
//		studentDetails.setStudentName(studentNames.getText());
//		studentDetails.setAddress(clientDetails.getAddress());
//		return studentDetails;
//	}
//
//	public void setStudentDetails(StudentDTO studentDetails) {
//		this.studentDetails = studentDetails;
//		parentName.setText(studentDetails.getParentName());
//		studentNames.setText(studentDetails.getStudentName());
//	}

	public void selectTpanel(int selector) {
		tPanel.selectTab(selector);
	}

	@UiHandler("backbutton1")
	void onBackbutton1Click(ClickEvent event) {
		tPanel.selectTab(0);
	}

//	@UiHandler("addStudentButton")
//	void onAddStudentButtonClick(ClickEvent event) {
//		studentDetails = new StudentDTO();
//		studentDetails.setAddress(clientDetails.getAddress());
//		studentDetails.setParentName(clientDetails.getParentName());
//		studentDetails.setStudentName(studentNames.getText());
//		studentDetails.setParentID(clientDetails.getID());
//		studentDetails.setDriverID(-1);
//		studentDetails.setTripSheetID(-1);
//		System.out.println("THIS IS ADD STUDENT BUTTON PLACE ADDRESS = "
//				+ clientDetails.getAddress());
//		greetingService.saveStudent(studentDetails,
//				new AsyncCallback<StudentDTO>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						// TODO Fix This:
//						// errorLabel.setText(caught.getMessage());
//					}
//
//					@Override
//					public void onSuccess(StudentDTO result) {
//						studentDetails = result;
//					}
//				});
//
//	}

//	@UiHandler("nextButton2")
//	void onNextButton2Click(ClickEvent event) {
//		tPanel.selectTab(2);
//	}
	@UiHandler("assignButton")
	void onNextButtonClick(ClickEvent event) {
		getDriverDetails();
		greetingService.saveDriver(driverDetails,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Fix This:
						// errorLabel.setText(caught.getMessage());
					}

					@Override
					public void onSuccess(Void x) {
						
					}
				});
		tPanel.selectTab(1);
	}

	@UiHandler("backButton3")
	void onBackButton3Click(ClickEvent event) {
		tPanel.selectTab(1);
	}
}
