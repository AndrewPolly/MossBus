package com.mossbuss.webapp.client.ui.tripSheet;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Label;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;


@SuppressWarnings("deprecation")
public class TripSheetSearch extends Composite {

	private static studentSearchUiBinder uiBinder = GWT.create(studentSearchUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	MultiWordSuggestOracle contactNameOracle;
	
	private DriverDTO driver;
	private BusDTO bus;
	private TripSheetDTO TripSheet;
	private ArrayList<StudentDTO> student = new ArrayList<StudentDTO>();
	@UiField Button newTripSheetButton;
	@UiField TripSheetView tripSheetViewPanel;
	@UiField Button cancelButton;
	@UiField SuggestBox tripSheetText;
	@UiField Label errorLabel;

	interface studentSearchUiBinder extends UiBinder<Widget, TripSheetSearch> {
	}

	public TripSheetSearch() {
		initWidget(uiBinder.createAndBindUi(this));
		tripSheetViewPanel.setVisible(false);
		contactNameOracle = (MultiWordSuggestOracle) tripSheetText.getSuggestOracle();
		ContactChangeHandler contactChangeHandler = new ContactChangeHandler();
		ContactSuggestionSelectionHandler contactSuggestionHandler = new ContactSuggestionSelectionHandler();
		tripSheetText.addKeyDownHandler(contactChangeHandler);
		tripSheetText.addEventHandler(contactSuggestionHandler);
	}
	
	public void init(){
		tripSheetText.setFocus(true);
	}

	public TripSheetDTO getTripSheet() {
		return TripSheet;
	}
	public Button getSelectButton() {
		return tripSheetViewPanel.getCloseButton();
	}
	public void setTripSheet(TripSheetDTO tripSheet) {
		this.TripSheet = tripSheet;
		tripSheetViewPanel.setTripSheet(tripSheet);
		tripSheetViewPanel.init();
		tripSheetViewPanel.setVisible(true);
	}
	@UiHandler("newTripSheetButton")
	void onNewTripSheetButtonClick(ClickEvent event) {
		final TripSheetEdit tripSheetEdit = new TripSheetEdit();
		final PopupPanel pPanel = new PopupPanel();
		tripSheetEdit.init();
		tripSheetEdit.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pPanel.hide();
			}
		});
		tripSheetEdit.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				greetingService.saveTripSheet(TripSheet, new AsyncCallback<TripSheetDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText(caught.getMessage());
					}

					@Override
					public void onSuccess(TripSheetDTO result) {
						setTripSheet(result);
					}
				});
				pPanel.hide();
			}
		});
		tripSheetEdit.getDriverSelectBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				driver = tripSheetEdit.getDriverSelection();
				updateDriver(driver);
			}
		});
		tripSheetEdit.getBusSelectBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				bus = tripSheetEdit.getBusSelection();
				updateBus(bus);
			}
		});
		pPanel.add(tripSheetEdit);
		pPanel.setModal(true);
		pPanel.center();
	}
	public Button getCancelButton(){
		return cancelButton;
	}
	public void updateDriver(DriverDTO driver) {
		this.TripSheet.setDriverID(driver.getID());
	}
	public void updateBus(BusDTO bus) {
		this.TripSheet.setBusID(bus.getID());
	}
//	public Button getSelectButton(){
//		return tripSheetViewPanel.getCloseButton();
//	}
	class ContactChangeHandler implements KeyDownHandler {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			errorLabel.setText("");
			if(tripSheetText.getText().length() >1){
				ArrayList<String> wordList = new ArrayList<String>();
				String[] words = tripSheetText.getText().split(" ");
				for (String word : words){
				   System.out.println(word);
				   wordList.add(word);
				}
				String sql = "select id, DriverName, TripName from tripsheet where TripName Like '%" + wordList.get(0) + "%'";
				
				System.out.println(sql);
				if(event.isAltKeyDown() || event.isControlKeyDown() || event.isDownArrow() || event.isLeftArrow() || event.isRightArrow() | event.isUpArrow()){
					//do nothing
				} else{
					try {
						updateContactNameOracle(sql);
					}
					catch (Exception e) {
						errorLabel.setText(e.getMessage());
						System.out.println(sql);
						System.out.println("Error: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void updateContactNameOracle(ArrayList<String> newList){
		contactNameOracle.clear();
		for(int i=0; i<newList.size(); i++){
			contactNameOracle.add(newList.get(i));
		}
	}
	public void updateContactNameOracle(String sql) throws Exception{
		greetingService.updateContactNameOracle(sql, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			public void onSuccess(ArrayList<String> result) {
				updateContactNameOracle(result);
				tripSheetText.showSuggestionList();
			}
		});
	}
	class ContactSuggestionSelectionHandler implements SuggestionHandler{
		@Override
		public void onSuggestionSelected(SuggestionEvent event) {
			errorLabel.setText("");
			//Get all Customer details for that company
			String suggestion = event.getSelectedSuggestion().getDisplayString();
			suggestion = suggestion.replaceAll("\\<.*?>","");
			suggestion = suggestion.trim();
			int endIndex = suggestion.indexOf(" :: ");
			suggestion = suggestion.substring(0, endIndex);
			int selectedID = Integer.parseInt(suggestion);
			greetingService.getTripSheet(selectedID, new AsyncCallback<TripSheetDTO>() {
				
				@Override
				public void onSuccess(TripSheetDTO result) {
					setTripSheet(result);
					errorLabel.setText("");
					tripSheetViewPanel.setVisible(true);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(caught.getMessage());
				}
			});
		}
	}
}
