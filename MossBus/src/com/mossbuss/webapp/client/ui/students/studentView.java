package com.mossbuss.webapp.client.ui.students;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class studentView extends Composite {

	private static studentViewUiBinder uiBinder = GWT
			.create(studentViewUiBinder.class);
	private ClientDTO customerDetails = new ClientDTO();
	private ArrayList<String> studentList = new ArrayList<String>();
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	@UiField
	Label Address;
	@UiField
	Label accBal;
	@UiField
	Label parentName;
	@UiField
	Label cellNumber;
	@UiField
	Label emailAddress;
	@UiField
	Label parentToSmsField;
	@UiField
	TextBox payField;
	@UiField
	Button payButton;
	
	// @UiField
	// Button nextButton;
	// @UiField
	// Button backButton;
	// @UiField
	// Button nextButton1;
	// @UiField
	// Button backButton1;
	@UiField
	TextArea messageField;
	@UiField
	Button sendSmsButton;
	@UiField
	TabPanel tPanel;
	@UiField
	Label notificationField;

	interface studentViewUiBinder extends UiBinder<Widget, studentView> {
	}

	public studentView() {
		initWidget(uiBinder.createAndBindUi(this));
		tPanel.selectTab(0);
	}

	public ClientDTO getCustomerDetails() {

		return customerDetails;
	}

	public void setCustomerDetails(ClientDTO customerDetails) {
		this.customerDetails = customerDetails;
		parentName.setText(customerDetails.getParentName());
		cellNumber.setText(customerDetails.getCellNumber());
		emailAddress.setText(customerDetails.getEmailAddress());
		Address.setText(customerDetails.getAddress());
		accBal.setText("R " + customerDetails.getAccBal());
		parentToSmsField.setText("Send an SMS to "
				+ customerDetails.getParentName());
	}

	// @UiHandler("nextButton")
	// void onNextButtonClick(ClickEvent event) {
	// tPanel.selectTab(1);
	// }
	//
	// @UiHandler("backButton")
	// void onBackButtonClick(ClickEvent event) {
	// tPanel.selectTab(0);
	//
	// }
	//
	// @UiHandler("nextButton1")
	// void onNextButton1Click(ClickEvent event) {
	// tPanel.selectTab(2);
	// }
	//
	// @UiHandler("backButton1")
	// void onBackButton1Click(ClickEvent event) {
	// tPanel.selectTab(1);
	// }
	//
	// a positive payment will be considered as the person paying for a trip, a
	// negative one would be
	// the cost for the month.
	@UiHandler("payButton")
	void onPayButtonClick(ClickEvent event) {
		System.out.println("HEJKGAKLSFJKLASDJF PAYMENT PAYTED");
		float account = customerDetails.getAccBal();
		double payment = Double.parseDouble(payField.getText());

		account = account + (float) payment;
		customerDetails.setAccBal(account);
		greetingService.saveClient(customerDetails,
				new AsyncCallback<ClientDTO>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(ClientDTO result) {
						setCustomerDetails(result);
						payField.setText("");
					}
				});

	}

	@UiHandler("sendSmsButton")
	void onSendSmsButtonClick(ClickEvent event) {
		System.out.println(" I GOT PRESSED BUDDY SEND THAT SMS");
		String message = messageField.getText();
		if (message.length() > 255) {
			notificationField
					.setText("Your message exceeds the 255 character limit, please remove some characters.");
		} else {
			messageField.setText("");
			greetingService.sendSMS(message, customerDetails.getCellNumber(),
					new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Message not sent. Please contact Mr. Polly at 2andrewp2@gmail.com or on 083 503 5513 for further assistance.");
						}

						@Override
						public void onSuccess(Void result) {
							messageField.setText("");
							notificationField
									.setText("Message successfully sent.");
						}
					});

		}

	}

}