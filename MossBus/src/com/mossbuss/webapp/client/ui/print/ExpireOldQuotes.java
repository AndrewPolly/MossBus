package com.mossbuss.webapp.client.ui.print;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.skyrat.w8.client.GreetingService;
import com.skyrat.w8.client.GreetingServiceAsync;
import com.skyrat.w8.client.ui.general.Panels.DatePickerWithYearSelector;
import com.google.gwt.user.client.ui.Label;

public class ExpireOldQuotes extends Composite implements HasText {

	private static ExpireOldQuotesUiBinder uiBinder = GWT.create(ExpireOldQuotesUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private String sp;
	private String dateString;
	@UiField Button button;
	@UiField DatePickerWithYearSelector expireDate;
	@UiField Label spLabel;

	interface ExpireOldQuotesUiBinder extends UiBinder<Widget, ExpireOldQuotes> {
	}

	public ExpireOldQuotes() {
		initWidget(uiBinder.createAndBindUi(this));
		sp = new String();
		dateString = new String();
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		Date today = new Date();
		if(expireDate.getValue() == null || expireDate.getValue() == today){
			Window.alert("Please Select a cut-off date. (Quotes older than the cut-off date will be expired).");
		} else {
			dateString = DateTimeFormat.getShortDateFormat().format(expireDate.getValue());
			String msg = "WARNING, this cannot be undone! Are you sure you want to expire all quotes older than " + dateString + " for sales person: " + sp + "?";
			
			if(Window.confirm(msg)){
				//expire Quotes!!!
				String sql = "Update quotedata set Quote_Status = 'Expired', Quote_Expiry_Date = now(),  Expiry_Details = 'Clean Up' where Sales_Person = '"+sp+"' AND Quote_Date <= '" + dateString +"' AND Quote_Status = 'Active'";
				greetingService.expireOldQuotes(sql, new AsyncCallback<String>() {
	
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("There was an error: "+ caught.getMessage());
					}
	
					@Override
					public void onSuccess(String result) {
						Window.alert(result + " quotes expired!");
					}
					
				});
			}
		}
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		spLabel.setText(sp);
		this.sp = sp;
		
	}
}
