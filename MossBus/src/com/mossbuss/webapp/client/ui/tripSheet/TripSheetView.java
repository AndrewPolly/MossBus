package com.mossbuss.webapp.client.ui.tripSheet;

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
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.ui.print.QuoteGrid;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class TripSheetView extends Composite {

	private static TripSheetViewUiBinder uiBinder = GWT.create(TripSheetViewUiBinder.class);
	private TripSheetDTO tripSheet = new TripSheetDTO();
	private ArrayList<StudentDTO> studentList = new ArrayList<StudentDTO>();
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private ArrayList<StudentDTO> nonSelectedStudents = new ArrayList<StudentDTO>();
	@UiField QuoteGrid studentsGrid;
	@UiField Label errorLabel;
	@UiField Button closeButton;
	@UiField TripSheetEdit tripEdit;
	@UiField Button addStudent;
	@UiField ListBox studentSelectBox;
	@UiField TabPanel tPanel;
	
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
		tripEdit.setTripSheet(trip);
		tripEdit.initTripSheet();
		greetingService.getStudentsFromTripSheet(tripSheet.getID(),new AsyncCallback<ArrayList<StudentDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<StudentDTO> result) {
				studentList = result;
				studentsGrid.setGridItems(studentList);
			}
		});
		tripEdit.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				greetingService.saveTripSheet(tripEdit.getTripSheet(), new AsyncCallback<TripSheetDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Fix This:
						//errorLabel.setText(caught.getMessage());
					}

					@Override
					public void onSuccess(TripSheetDTO result) {
						tripEdit.setTripSheet(result);
					}
				});
				
				//customerSearch.setVisible(true);
			}
		});
		// init ListBox.
		greetingService.getStudentsFromTripSheet(-1, new AsyncCallback<ArrayList<StudentDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<StudentDTO> result) {
				nonSelectedStudents = result;
				initListBox();
			}
		});
		addStudent.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nonSelectedStudents.get(studentSelectBox.getSelectedIndex()).setTripSheetID(tripSheet.getID());;
				
				greetingService.saveStudent(nonSelectedStudents.get(studentSelectBox.getSelectedIndex()), new AsyncCallback<StudentDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText(caught.getMessage());
					}
		
					@Override
					public void onSuccess(StudentDTO result) {
						studentList.add(result);
						studentsGrid.insertLineToGrid(result);
						
					}
				});
				nonSelectedStudents.remove(studentSelectBox.getSelectedIndex());
				
				initListBox();
				//customerSearch.setVisible(true);
			}
		});
		studentsGrid.setGridItems(studentList);
		
	}

	public void init() {
		studentsGrid.setGridItems(studentList);
		studentsGrid.drawGrid();
	}
	public Button getCloseButton() {
		System.out.println("XXX GET CLOSEBUTTON IN TRIPSHEET VIEW GOT CALLED");
		return closeButton;
	}
	public Button getAddStudentButton() {
		return addStudent;
	}
	public void initListBox() {
		
		
		studentSelectBox.clear();
		
		for (int i = 0; i < nonSelectedStudents.size(); i++) {
			studentSelectBox.addItem(nonSelectedStudents.get(i).getStudentName());
		}
		studentSelectBox.setSelectedIndex(0);
	}
	public Button getPrintButton() {
		return closeButton;
	}
	

}