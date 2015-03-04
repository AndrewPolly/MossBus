package com.mossbuss.webapp.client.ui.maintenanceRecords;

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
import com.mossbuss.webapp.client.dto.MaintenanceRecordDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.ui.print.MaintenanceGrid;
import com.mossbuss.webapp.client.ui.print.QuoteGrid;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class MaintenanceView extends Composite {

	private static MaintenanceViewUiBinder uiBinder = GWT
			.create(MaintenanceViewUiBinder.class);
	private BusDTO bus = new BusDTO();
	private ArrayList<MaintenanceRecordDTO> recordList = new ArrayList<MaintenanceRecordDTO>();
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	private ArrayList<StudentDTO> nonSelectedStudents = new ArrayList<StudentDTO>();
	@UiField
	MaintenanceGrid maintenanceGrid;
	@UiField
	Label errorLabel;
	@UiField
	Button printButton;
	@UiField
	Button cancelButton2;
	@UiField
	MaintenanceEdit maintenanceEdit;
	

	@UiField
	TabPanel tPanel;

	interface MaintenanceViewUiBinder extends UiBinder<Widget, MaintenanceView> {
	}

	public MaintenanceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public BusDTO getBus() {
		return bus;
	}

	public void setBus(BusDTO newBus) {
		this.bus = newBus;
		maintenanceEdit.setBus(newBus);
		
		greetingService.getRecordsFromBus(bus.getID(),
				new AsyncCallback<ArrayList<MaintenanceRecordDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText(caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<MaintenanceRecordDTO> result) {
						recordList = result;
						maintenanceGrid.setGridItems(recordList);
					}
				});
		maintenanceEdit.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				greetingService.saveMaintenanceRecord(maintenanceEdit.getRecord(),
						new AsyncCallback<MaintenanceRecordDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Fix This:
								// errorLabel.setText(caught.getMessage());
							}

							@Override
							public void onSuccess(MaintenanceRecordDTO result) {
								//set all text boxes to ""
								maintenanceEdit.getNotesField().setText("");;
							}
						});

				// customerSearch.setVisible(true);
			}
		});
		maintenanceEdit.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				maintenanceEdit.getNotesField().setText("");;
				tPanel.selectTab(0);
			}
			
		});
		// init ListBox.
		
		
		

	}

	public void init() {
		maintenanceGrid.setGridItems(recordList);
		maintenanceGrid.drawGrid();
	}


	public Button getCancelButton() {
		return cancelButton2;
	}

	public Button getPrintButton() {
		return printButton;
	}

}