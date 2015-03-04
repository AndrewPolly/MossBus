package com.mossbuss.webapp.client.ui.maintenanceRecords;



	import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import com.mossbuss.webapp.client.dto.MaintenanceRecordDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;

	public class MaintenanceEdit extends Composite {

		private static MaintenanceEditUiBinder uiBinder = GWT
				.create(MaintenanceEditUiBinder.class);

		private BusDTO busDetails = new BusDTO();
		private final GreetingServiceAsync greetingService = GWT
				.create(GreetingService.class);


		@UiField
		TabPanel tPanel;
		@UiField
		TextBox notesField;

		@UiField
		Button saveButton;
		@UiField
		Button cancelButton;
		@UiField
		Label errorLabel;

		interface MaintenanceEditUiBinder extends UiBinder<Widget, MaintenanceEdit> {
		}

		public MaintenanceEdit() {
			initWidget(uiBinder.createAndBindUi(this));
			// Create Customer Edit Interface based on CustomerDTO
			tPanel.selectTab(0);

		}

		public Button getCancelButton() {
			return this.cancelButton;
		}

		public Button getSaveButton() {
			return this.saveButton;
		}


		public TextBox getNotesField() {
			return this.notesField;
		}



		public MaintenanceRecordDTO getRecord() {
			MaintenanceRecordDTO record = new MaintenanceRecordDTO();
			Date date = new Date();
			record.setDate(date);
			record.setNotes(notesField.getText());
			record.setBusID(busDetails.getID());

			return record;
		}


		public void setBus(BusDTO newBus) {
			this.busDetails = newBus;
		}

		public void selectTpanel(int selector) {
			tPanel.selectTab(selector);
		}











	

	}
