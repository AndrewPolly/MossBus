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
	public ListBox getBusSelectBox() {
		return busSelectBox;
	}
	public ListBox getDriverSelectBox() {
		return driverSelectBox;
	}
	public void driverInit() {
		System.out.println("AT INIT DOING SOMETHING HERE XXXXXXXXXXXX");
		greetingService.getAllDrivers(new AsyncCallback<ArrayList<DriverDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Fix This:
				//errorLabel.setText(caught.getMessage());
				System.out.println("Got to failure.!!!!");
			}

			@Override
			public void onSuccess(ArrayList<DriverDTO> result) {
				Drivers = result;
				System.out.println("DriverArray LIST SIZE IS : " + Drivers.size());
				System.out.println("DriverArray LIS XXXX T SIZE IS : " + Drivers.size());
				//check for drivers not yet allocated to trip sheet.
				for (int i = 0;  i < Drivers.size(); i++) {
					if (Drivers.get(i).getTripSheetID() < 0) {
						driverSelectBox.addItem(Drivers.get(i).getName());
						System.out.println("Driver AT ID: " + i + " is: " + Drivers.get(i).getName());
					}
				}
			}
		});
		
		
		
	}
	public void busInit() {
		System.out.println("GOT INTO BUSINIT");
		greetingService.getAllBusses(new AsyncCallback<ArrayList<BusDTO>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Fix This:
				//errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<BusDTO> busresult) {
				Busses = busresult;
				System.out.println("ONSUCCESS" + Busses.size());
				//check for busses that havent yet been allocated.
				for (int i = 0; i < Busses.size(); i++) {
					System.out.println("IN FOR LOOP" + i);
					if (Busses.get(i).getTripSheetID() < 0) {
						busSelectBox.addItem(Busses.get(i).getBusName());
						System.out.println("BUS AT ID: " + i + " is: " + Busses.get(i).getBusName());
					} else {
						System.out.println("Else statement:" + Busses.get(0).getTripSheetID());
					}
				}
			}
		});
		
	}
	public TripSheetDTO getTripSheet() {
		TripSheetDTO trip = new TripSheetDTO();
		trip.setTripName(tripNameField.getText());
		trip.setDriverID(getDriverSelection().getID());
		System.out.println("Driver id is : " + getDriverSelection().getID());
		trip.setBusID(getBusSelection().getID());
		System.out.println("Bus id is : " + getBusSelection().getID());
		trip.setDriverName(getDriverSelection().getName());
		System.out.println("Driver name is : " + getDriverSelection().getName());
		return trip;
	}
	public void setTripSheet(TripSheetDTO tripSheet) {
		this.tripDetails = tripSheet;
		tripNameField.setText(tripDetails.getTripName());
		driverInit();
		busInit();
		driverSelectBox.setItemSelected(getDriverIntSelection(), true);
		busSelectBox.setItemSelected(getBusIntSelection(), true);
	}

	public void selectTpanel(int selector) {
		tPanel.selectTab(selector);
	}
	
	public int getDriverIntSelection() {
		int Item = driverSelectBox.getSelectedIndex();
		int counter = 0;
		
		for (int i = 0;  i < Drivers.size(); i++) {
			if (Drivers.get(i).getTripSheetID() < 0) {
				counter++;
				if (counter == Item) {
					return counter;
				}
			}
		}
		//this should never happen;
		return 0;
	}
	public int getBusIntSelection() {
		int Item = busSelectBox.getSelectedIndex();
		int counter = 0;
		
		for (int i = 0;  i < Busses.size(); i++) {
			if (Busses.get(i).getTripSheetID() < 0) {
				
				if (counter == Item) {
					return counter;
				}
				counter++;
			}
		}
		//this should never happen;
		return 0;
	}
	public DriverDTO getDriverSelection() {
		int Item = driverSelectBox.getSelectedIndex();
		int counter = 0;
		DriverDTO selectedDriver = new DriverDTO();
		
		for (int i = 0;  i < Drivers.size(); i++) {
			
			if (Drivers.get(i).getTripSheetID() < 0) {
				
				if (counter == Item) {
					selectedDriver = Drivers.get(i);
					i = Drivers.size();
				}
				counter++;
			}
		}
		return selectedDriver;
	}
	public BusDTO getBusSelection() {
		int Item = busSelectBox.getSelectedIndex();
		int counter = 0;
		BusDTO selectedBus = new BusDTO();
		for (int i = 0;  i < Busses.size(); i++) {
			if (Busses.get(i).getTripSheetID() < 0) {
				if (counter == Item) {
					selectedBus = Busses.get(i);
					i = Busses.size();
				}
				counter++;
			}
		}
		return selectedBus;
	}
	
}

