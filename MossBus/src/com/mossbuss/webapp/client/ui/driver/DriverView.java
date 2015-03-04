package com.mossbuss.webapp.client.ui.driver;



	import java.util.ArrayList;

	import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.ui.print.DriverGrid;
import com.mossbuss.webapp.client.ui.print.QuoteGrid;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

	public class DriverView extends Composite {

		private static DriverViewUiBinder uiBinder = GWT.create(DriverViewUiBinder.class);
		private TripSheetDTO tripSheet = new TripSheetDTO();
		private ArrayList<DriverDTO> driverList = new ArrayList<DriverDTO>();
		private ArrayList<BusDTO> busList = new ArrayList<BusDTO>();
		private ArrayList<TripSheetDTO> tripList = new ArrayList<TripSheetDTO>();
		private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
		private ArrayList<StudentDTO> nonSelectedStudents = new ArrayList<StudentDTO>();
		@UiField DriverGrid driversGrid;
		@UiField Label errorLabel;
		@UiField Button printButton;
		@UiField DriverEdit driverEdit;
		@UiField TabPanel tPanel;
		
		interface DriverViewUiBinder extends UiBinder<Widget, DriverView> {
		}

		public DriverView() {
			initWidget(uiBinder.createAndBindUi(this));
		}

		
		
		

		public void init() {
			greetingService.getAllDrivers(-2, new AsyncCallback<ArrayList<DriverDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(caught.getMessage());
				}
	
				@Override
				public void onSuccess(ArrayList<DriverDTO> result) {
					driverList = result;
					System.out.println("get all drivers success result.size = " + result.size() + " driverList.size() = " + driverList.size());
					driversGrid.setGridItems(driverList);
					driversGrid.drawGrid();
				}
			});
			greetingService.getAllBusses(-2, new AsyncCallback<ArrayList<BusDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(caught.getMessage());
				}
	
				@Override
				public void onSuccess(ArrayList<BusDTO> result) {
					busList = result;
					
					driversGrid.setBusItems(busList);
					driversGrid.drawGrid();
				}
			});
			greetingService.getAllTripSheets(new AsyncCallback<ArrayList<TripSheetDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(caught.getMessage());
				}
	
				@Override
				public void onSuccess(ArrayList<TripSheetDTO> result) {
					tripList = result;
					
					driversGrid.setTripItems(tripList);
					driversGrid.drawGrid();
				}
			});
			
		}
		public Button getPrintButton() {
			System.out.println("XXX GET CLOSEBUTTON IN TRIPSHEET VIEW GOT CALLED");
			return printButton;
		}
		
		
		

	}	

