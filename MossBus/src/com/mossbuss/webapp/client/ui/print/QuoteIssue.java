package com.skyrat.w8.client.ui.documents;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.skyrat.w8.client.GreetingService;
import com.skyrat.w8.client.GreetingServiceAsync;
import com.skyrat.w8.client.dto.W7QuoteDataDTO;
import com.skyrat.w8.client.dto.W8UserDTO;
import com.google.gwt.user.client.ui.ListBox;

public class QuoteIssue extends Composite {
	

	private static QuoteIssueUiBinder uiBinder = GWT.create(QuoteIssueUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private W7QuoteDataDTO details;
	private QuoteInfoPanel info;
	private ItemGridView gridView;
	private LetterHeadDoc letterHead;
	private W8UserDTO onlineUser;
	private W8UserDTO salesPerson;
	
	@UiField Button closeButton;
	@UiField Button printButton;
	@UiField FlowPanel dataFlow;
	@UiField ListBox branchSelect;
	
	
	
	HTML intro;
	HTML bee;
	HTML termsConditions;
	HTML paymentTerms;
	HTML installationTerms;
	HTML quoteHeading;
	HTML deliveryTime;
	HTML exchangeRate;
	HTML userSignature;
	
	String leterHeadJHB;
	

	interface QuoteIssueUiBinder extends
			UiBinder<Widget, QuoteIssue> {
	}

	public QuoteIssue() {
		initWidget(uiBinder.createAndBindUi(this));
		ArrayList<String> branchList = new ArrayList<String>();
		branchList.add("JHB");
		branchList.add("CT");
		branchList.add("DBN");
		branchList.add("PE");
		branchList.add("VT");
		for(int i = 0; i< branchList.size(); i++){
			branchSelect.addItem(branchList.get(i));
		}
		letterHead = new LetterHeadDoc();
		branchSelect.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				letterHead.setBranch(branchSelect.getItemText(branchSelect.getSelectedIndex()));
			}
		});
		info = new QuoteInfoPanel();
		gridView = new ItemGridView();
	}
	public W7QuoteDataDTO getDetails() {
		return details;
	}
	public void setDetails(W7QuoteDataDTO details, W8UserDTO onlineUser, W8UserDTO salesPerson) {
		this.details = details;
		setOnlineUser(onlineUser);
		userSignature = new HTML("<p align=\"LEFT\" style=\"page-break-after:always;\">"+salesPerson.getSignature()+"</p><HR>");
		
		gridView = new ItemGridView();
		gridView.setVatExempt(details.isVATexempt());
		gridView.setStockItems(details.getQuoteItems());
		gridView.setCurencySymbol(details.getCurrency());
		gridView.setQuoteHeading(details.getQuote_Summary());
		gridView.drawGrid();
		info.setData(details, getOnlineUser());
		intro = new HTML("<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><br>Dear " + details.getContact()+"</font></p>"
				+ "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">Thank you for your interest in Wirsam's products and services.</br>"
				+ "We take great pleasure in quoting as follows:</font></p>");
		
		bee = new HTML("<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\">Please note: Wirsam Scientific is a certified Level 5 BBBEE contributor. "
				+ "Certificate available on request </font></p>");
		
		
		termsConditions = new HTML("<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><strong>Validity of Quotation</strong><br>"
          + "These prices are valid for 30 days from the date of the quotation and thereafter subject to written confirmation. "
          + "Any alteration to this quotation shall not be binding unless agreed to in writing by the seller. "
          + "Prices quoted are subject to fluctuation in the foreign rate of exchange. "
          + "The following rates of exchange were applied: " +details.getRate_Notes()+" <br>"
          + "Any variation between the rate quoted and the rate paid to our suppliers will be for your account. "
          + "Changes in statutory duties and surcharges will also be for your account. "
          + "In this instance the difference in price will be reflected on your Invoice.</font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"> <strong>Delivery</strong><br>"
          + "Delivery is to be confirmed on order. Time is approximate and calculated from receipt of official order. "
          + "Ex-Stock is subject to prior sale aggreement:<br> Delivery Time: "+details.getDeliveryDetails()
          + "</font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"> <strong>Guarantee</strong><br> "
          + "Equipment offered in this quotation is guaranteed against defective material and workmanship from the date of installation for a period "
          + "of 12 months, except wear and tear parts and parts subject to consumption. "
          + "Any labour under guarantee will be carried out during normal working hours, at Wirsam's premises. "
          + "Equipment guarantees do not exceed original manufacturer guarantees.</font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><strong>Payment</strong><br>"
          + details.getPaymentTerms()+"</font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"> <strong>Cancellation of orders</strong><br>"
          + "Orders are firm and not subject to cancellation. Where cancellation of an order is accepted the purchaser "
          + "will become liable for a cancellation fee of not less than 30% (thirty percent) of the selling price, plus "
          + "the cost of shipping the goods back to the factory in the Country of Origin.</font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">"
          + "<strong>Installation and Training</strong><br>"+details.getInsallmentTerms()+"</font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"> <strong>Service</strong><br>"
          + "Complete service facilities are available from our Head Office in Johannesburg as well as our Branch offices. "
          + "Service contracts are also available.</font></p><br>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><strong>NOTE:</strong></font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><strong>Wirsam Scientifics' standard Terms and Conditions apply, "
          + "which are available on request.</strong></font></p>"
          + "<p align=\"LEFT\"><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">Should you require any further information, "
          + "please do not hesitate to contact us.</font>");
		
		
		DateTimeFormat fmt = DateTimeFormat.getFormat("MMyy");
		if(details.getBranch()!= null){
			letterHead.setBranch(details.getBranch());
		} else {
			letterHead.setBranch("JHB");
		}
		dataFlow.add(letterHead);
		dataFlow.add(new HTML("<HR>"));
		dataFlow.add(info);
		dataFlow.add(new HTML("<HR>"));
		dataFlow.add(intro);
		dataFlow.add(gridView);
		dataFlow.add(new HTML("</br>"));
		dataFlow.add(bee);
		dataFlow.add(userSignature);
		
		dataFlow.add(termsConditions);
	}
	
	@UiHandler("printButton")
	void onPrintButtonClick(ClickEvent event) {
		Window.setTitle(details.getCompany() + " " +  DateTimeFormat.getShortDateFormat().format(details.getQuote_Date()) + " WS" + details.getQuote_Number());
		branchSelect.setVisible(false);
		Window.print();
		Window.setTitle("W.O.L.8");
		branchSelect.setVisible(true);
	}
	public Button getCloseButton() {
		return closeButton;
	}
	public W8UserDTO getOnlineUser() {
		return onlineUser;
	}
	public void setOnlineUser(W8UserDTO onlineUser) {
		this.onlineUser = onlineUser;
	}
	public W8UserDTO getSalesPerson() {
		return salesPerson;
	}
	public void setSalesPerson(W8UserDTO salesPerson) {
		this.salesPerson = salesPerson;
	}
}
