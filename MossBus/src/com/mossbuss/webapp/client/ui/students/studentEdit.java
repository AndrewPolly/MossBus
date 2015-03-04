package com.mossbuss.webapp.client.ui.students;



import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
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
import com.mossbuss.webapp.client.dto.StudentDTO;

public class studentEdit extends Composite {

	private static studentEditUiBinder uiBinder = GWT.create(studentEditUiBinder.class);
	
	private ClientDTO clientDetails = new ClientDTO();
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private ArrayList<String> Students = new ArrayList<String>();
	private StudentDTO studentDetails;
	@UiField TabPanel tPanel;
	@UiField TextBox addressField;

	@UiField TextBox parentName;
	@UiField TextBox emailAddress;
	@UiField ListBox payBox;

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
		payBox.addItem("Monthly");
		payBox.addItem("Semi-Monthly");
		payBox.addItem("Weekly");
		payBox.setSelectedIndex(0);
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
		Date today = new Date();
		System.out.println("Made new date for new client the date is " + today);
		clientDetails.setAccBal(0);
		//add option for this soon!!!!
		if (payBox.getSelectedIndex() > 1) {
			clientDetails.setPayOption(4);
		} else {
			clientDetails.setPayOption(payBox.getSelectedIndex() + 1);
		}
		
		clientDetails.setDateLastDebited(today);
		return clientDetails;
	}

	public void setClientDetails(ClientDTO clientDetails) {
		this.clientDetails = clientDetails;
		parentName.setText(clientDetails.getParentName());
		clientDetails.setParentName(parentName.getText());
		
		emailAddress.setText(clientDetails.getEmailAddress());
		cellNumber.setText(clientDetails.getCellNumber());
		if (clientDetails.getPayOption() > 2) {
			payBox.setSelectedIndex(2);
		} else {
			payBox.setSelectedIndex(clientDetails.getPayOption()-1);
		}
		
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
	
	
	@UiHandler("addStudentButton")
	void onAddStudentButtonClick(ClickEvent event) {
		studentDetails = new StudentDTO();
		studentDetails.setAddress(clientDetails.getAddress());
		studentDetails.setParentName(clientDetails.getParentName());
		studentDetails.setStudentName(studentNames.getText());
		studentDetails.setParentID(clientDetails.getID());
		studentDetails.setDriverID(-1);
		studentDetails.setTripSheetID(-1);
		System.out.println("THIS IS ADD STUDENT BUTTON PLACE ADDRESS = " + clientDetails.getAddress());
		greetingService.saveStudent(studentDetails, new AsyncCallback<StudentDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Fix This:
				//errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(StudentDTO result) {
				studentDetails = result;
			}
		});

	}
	@UiHandler("nextButton2")
	void onNextButton2Click(ClickEvent event) {
		tPanel.selectTab(2);
	}
	@UiHandler("nextButton")
	void onNextButtonClick(ClickEvent event) {
		getClientDetails();
		System.out.println("Next button was clicked saving client with last payed = " + this.clientDetails.getDateLastDebited());
		greetingService.saveClient(clientDetails, new AsyncCallback<ClientDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Fix This:
				//errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ClientDTO result) {
				clientDetails = result;
			}
		});
		tPanel.selectTab(1);
	}
	@UiHandler("backButton3")
	void onBackButton3Click(ClickEvent event) {
		tPanel.selectTab(1);
	}
}

