package com.skyrat.w8.client.ui.sales.quotes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.skyrat.w8.client.ui.general.Panels.UserSelectPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;

public class QuoteSearchPanel extends Composite {

	private static QuoteSearchUiBinder uiBinder = GWT.create(QuoteSearchUiBinder.class);
	
	@UiField SuggestBox txtSearch;
	@UiField Button bugsButton;
	@UiField FlowPanel resultsPanel;
	@UiField AbsolutePanel adminPanel;
	@UiField Button goAdminButton;
	@UiField ListBox selectStatus;
	@UiField IntegerBox intervalInteger;
	@UiField ListBox intervalType;
	@UiField UserSelectPanel spSelect;
	@UiField TextBox itemCodeText;
	@UiField TextBox companyText;
	@UiField Button itemSearch;
	@UiField RadioButton statusActive;
	@UiField RadioButton statusExpired;
	@UiField RadioButton statusBoth;
	@UiField RadioButton statusOrdered;
	@UiField Label loadingLabel;
	@UiField RadioButton searchWS;
	@UiField RadioButton searchHeading;
	@UiField RadioButton searchCompany;
	@UiField RadioButton searchCustomer;
	@UiField RadioButton searchOrderNumber;
	@UiField RadioButton searchInvoice;
	@UiField RadioButton selectMine;
	@UiField RadioButton selectOthers;
	@UiField TextBox salesPersonCode;
	@UiField ToggleButton searchMode;
	@UiField Button searchButton;
	@UiField ListBox searchLimit;
	@UiField Label limitLabel;
	
	private String onlineUserCode;


	MultiWordSuggestOracle quoteDetailsOracle;
	interface QuoteSearchUiBinder extends UiBinder<Widget, QuoteSearchPanel> {
	}

	public QuoteSearchPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		quoteDetailsOracle = (MultiWordSuggestOracle) txtSearch.getSuggestOracle();
		selectStatus.addItem("Pending Stock");
		selectStatus.addItem("Pending Invoice");
		selectStatus.addItem("Pending Picking");
		selectStatus.addItem("Pending Dispatch");
		intervalType.addItem("Day");
		intervalType.addItem("Month");
		intervalType.addItem("Year");
		searchLimit.addItem("25");
		searchLimit.addItem("50");
		searchLimit.addItem("100");
		searchLimit.addItem("300");
		searchLimit.addItem("500");
		searchLimit.addItem("No Limit");
		searchLimit.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(searchLimit.getSelectedIndex()>2){
					if(Window.confirm("Are you sure you want to do this? You may crash the Database!!")){
						
					}else{
						searchLimit.setSelectedIndex(0);;
					}
				}
				
			}
		});
		
		searchMode.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(searchMode.isDown()){
					searchButton.setVisible(true);
					limitLabel.setVisible(true);
					searchLimit.setVisible(true);
				} else {
					searchButton.setVisible(false);
					limitLabel.setVisible(false);
					searchLimit.setVisible(false);
				}
				
			}
		});
	}
	public SuggestBox getTxtSearch() {
		return txtSearch;
	}

	public void setTxtSearch(SuggestBox txtSearch) {
		this.txtSearch = txtSearch;
	}
	public FlowPanel getResultsPanel() {
		return resultsPanel;
	}

	public void setResultsPanel(FlowPanel resultsPanel) {
		this.resultsPanel = resultsPanel;
	}
	public String getSearchCriteria(){
		String criteria = "";
		if(searchWS.getValue()){
			criteria = "q.Quote_Number";
		} else if(searchHeading.getValue()){
			criteria = "q.Quote_Summary";
		} else if(searchCompany.getValue()){
			criteria = "q.Company";
		} else if(searchCustomer.getValue()){
			criteria = "q.Contact";
		} else if(searchWS.getValue()){
			criteria = "q.Quote_Number";
		} else if(searchOrderNumber.getValue()){
			criteria = "s.CustOrderNo";
		} else if(searchInvoice.getValue()){
			criteria = "s.AccpacInvoiceNumber";
		}
		return criteria;
	}
	public String getSearchStatus(){
		String searchStatus = "";
		if(statusActive.getValue()){
			searchStatus = " AND q.Quote_Status = 'Active'";
		} else if(statusExpired.getValue()){
			searchStatus = " AND q.Quote_Status = 'Expired'";
		} else if(statusBoth.getValue()){
			searchStatus = "";
		}
		return searchStatus;
	}
	public String getSalesPerson(){
		String searchStatus = "";
		if(selectMine.getValue()){
			searchStatus = " AND q.Sales_Person = '"+ getOnlineUserCode() +"'";
		} else if(selectOthers.getValue()){
			if(salesPersonCode.getText().length()>1){
				searchStatus = " AND q.Sales_Person = '"+ salesPersonCode.getText() +"'";
			} else { 
				searchStatus = "";
			}
		}
		return searchStatus;
	}
	public String getOnlineUserCode() {
		return onlineUserCode;
	}
	public void setOnlineUserCode(String onlineUserCode) {
		this.onlineUserCode = onlineUserCode;
	}
	public ToggleButton getSearchMode(){
		return searchMode;
	}
	public Button getSearchButton(){
		return searchButton;
	}
	public String getSearchLimit(){
		String limit = "";
		if(searchLimit.getSelectedIndex()==0){
			limit = "limit 25";
		} else if(searchLimit.getSelectedIndex()==1){
			limit = "limit 50";
		} else if(searchLimit.getSelectedIndex()==2){
			limit = "limit 100";
		} else if(searchLimit.getSelectedIndex()==3){
			limit = "limit 300";
		} else if(searchLimit.getSelectedIndex()==3){
			limit = "limit 500";
		} else if(searchLimit.getSelectedIndex()==3){
			limit = "";
		}
		return limit;
	}
}
