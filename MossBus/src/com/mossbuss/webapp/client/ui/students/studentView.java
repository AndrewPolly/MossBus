package com.mossbuss.webapp.client.ui.students;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class studentView extends Composite {

	private static studentViewUiBinder uiBinder = GWT.create(studentViewUiBinder.class);
	private ClientDTO customerDetails = new ClientDTO();
	private ArrayList<String> studentList = new ArrayList<String>();
	@UiField Label Address;
	@UiField Label studentName;
	@UiField Label parentName;
	@UiField Label cellNumber;
	@UiField Label emailAddress;
	@UiField Button nextButton;
	@UiField Button backButton;
	@UiField Button nextButton1;
	@UiField Button backButton1;
	@UiField Button closeButton;
	@UiField TabPanel tPanel;
	
	

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
		
	}
	
	
	

	@UiHandler("nextButton")
	void onNextButtonClick(ClickEvent event) {
		tPanel.selectTab(1);
	}
	
	@UiHandler("backButton")
	void onBackButtonClick(ClickEvent event) {
		tPanel.selectTab(0);
	
	}
	
	@UiHandler("nextButton1")
	void onNextButton1Click(ClickEvent event) {
		tPanel.selectTab(2);
	}
	
	@UiHandler("backButton1")
	void onBackButton1Click(ClickEvent event) {
		tPanel.selectTab(1);
	}
	
	public Button getCloseButton(){
		return closeButton;
	}
}