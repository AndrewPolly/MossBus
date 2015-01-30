package com.mossbuss.webapp.client.ui.tripSheet;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.ui.print.QuoteGrid;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class TripSheetView extends Composite {

	private static TripSheetViewUiBinder uiBinder = GWT.create(TripSheetViewUiBinder.class);
	private TripSheetDTO tripSheet = new TripSheetDTO();
	private ArrayList<StudentDTO> studentList = new ArrayList<StudentDTO>();
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	@UiField QuoteGrid studentsGrid;
	@UiField Label errorLabel;
	@UiField Button closeButton;
	interface TripSheetViewUiBinder extends UiBinder<Widget, TripSheetView> {
	}

	public TripSheetView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TripSheetDTO getTripSheet() {
		return tripSheet;
	}
	
	public void setTripSheet(TripSheetDTO trip) {
		this.tripSheet = trip;
		greetingService.getStudentsFromTripSheet(tripSheet.getID(),new AsyncCallback<ArrayList<StudentDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<StudentDTO> result) {
				studentList = result;
			}
		});
		
	}

	public void init() {
		studentsGrid.setGridItems(studentList);
		studentsGrid.drawGrid();
	}
	public Button getCloseButton() {
		return closeButton;
	}


}