package com.mossbuss.webapp.client.ui.maintenanceRecords;


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
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;

public class BusEdit extends Composite {

	private static BusEditUiBinder uiBinder = GWT
			.create(BusEditUiBinder.class);

	private BusDTO busDetails = new BusDTO();
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	private ArrayList<String> Students = new ArrayList<String>();
	private StudentDTO studentDetails;
	private boolean admin = false;
	@UiField
	TabPanel tPanel;

	@UiField
	TextBox nameField;
	
	@UiField
	Button cancelButton;
	@UiField
	Label errorLabel;

	interface BusEditUiBinder extends UiBinder<Widget, BusEdit> {
	}

	public BusEdit() {
		initWidget(uiBinder.createAndBindUi(this));
		
		tPanel.selectTab(0);

	}


	public Button getCancelButton() {
		return cancelButton;
	}


	public BusDTO getBusDetails() {
		
		busDetails.setBusName(nameField.getText());
		
		
		busDetails.setDriverID(-1);
		busDetails.setTripSheetID(-1);
		
		
		return busDetails;
	}

	public void setDriverDetails(BusDTO busDetails) {
		this.busDetails = busDetails;

	}



	public void selectTpanel(int selector) {
		tPanel.selectTab(selector);
	}

//	@UiHandler("backbutton1")
//	void onBackbutton1Click(ClickEvent event) {
//		tPanel.selectTab(0);
//	}


	@UiHandler("saveButton")
	void onNextButtonClick(ClickEvent event) {
		getBusDetails();
		greetingService.saveBus(busDetails,
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
		nameField.setText("");

	}

//	@UiHandler("backButton3")
//	void onBackButton3Click(ClickEvent event) {
//		tPanel.selectTab(1);
//	}
}
