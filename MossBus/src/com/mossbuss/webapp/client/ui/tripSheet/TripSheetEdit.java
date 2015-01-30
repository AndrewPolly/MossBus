package com.mossbuss.webapp.client.ui.tripSheet;

import java.util.ArrayList;

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
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;

public class TripSheetEdit extends Composite {

	private static studentEditUiBinder uiBinder = GWT.create(studentEditUiBinder.class);
	
	private TripSheetDTO tripDetails = new TripSheetDTO();
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private ArrayList<String> Students = new ArrayList<String>();
	private ArrayList<BusDTO> Busses = new ArrayList<BusDTO>();
	private ArrayList<DriverDTO> Drivers = new ArrayList<DriverDTO>();
	private StudentDTO studentDetails;
	@UiField TabPanel tPanel;
	@UiField TextBox tripNameField;

	@UiField ListBox driverSelectBox;
	@UiField ListBox busSelectBox;

	@UiField Button saveButton;
	@UiField Button cancelButton;
	@UiField Label errorLabel;


	interface studentEditUiBinder extends UiBinder<Widget, TripSheetEdit> {
	}

	public TripSheetEdit() {
		initWidget(uiBinder.createAndBindUi(this));
		//Create Customer Edit Interface based on CustomerDTO
		tPanel.selectTab(0);
	
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}
	public Button getSaveButton() {
		return saveButton;
	}
	public void init() {
		greetingService.getAllDrivers(new AsyncCallback<ArrayList<DriverDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Fix This:
				//errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<DriverDTO> result) {
				Drivers = result;
			}
		});
		for (int i = 0;  i < Drivers.size(); i++) {
			if (Drivers.get(i).getTripSheetID() < 0) {
				driverSelectBox.addItem(Drivers.get(i).getName());
			}
		}
		greetingService.getAllBusses(new AsyncCallback<ArrayList<BusDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Fix This:
				//errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<BusDTO> busresult) {
				Busses = busresult;
			}
		});
		for (int i = 0; i < Busses.size(); i++) {
			
			if (Busses.get(i).getTripSheetID() < 0) {
				busSelectBox.addItem(Busses.get(i).getBusName());
			}
		}
		
	}
	public TripSheetDTO getTripSheet() {
		tripDetails.setTripName(tripNameField.getText());
		busSelectBox.get
		tripDetails.setParentName(parentName.getText());
		tripDetails.setEmailAddress(emailAddress.getText());
		tripDetails.setCellNumber(cellNumber.getText());
		
		
		
		
		return tripDetails;
	}

	public void setTripSheet(TripSheetDTO tripSheet) {
		this.tripDetails = tripSheet;
		tripNameField.setText(tripDetails.getTripName());
		
		driverSelectBox.
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
	
}

