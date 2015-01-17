package com.mossbuss.webapp.client.ui.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class AdminSettings extends Composite {

	private static AdminSettingsUiBinder uiBinder = GWT
			.create(AdminSettingsUiBinder.class);
	@UiField Button newItem;
	@UiField Button userButton;
	@UiField Button workButton;
	@UiField AbsolutePanel dataPanel;

	interface AdminSettingsUiBinder extends UiBinder<Widget, AdminSettings> {
	}

	public AdminSettings() {
		initWidget(uiBinder.createAndBindUi(this));
		//Leave this for now
		
	}

	@UiHandler("userButton")
	void onUserButtonClick(ClickEvent event) {
		dataPanel.clear();
		UserMenu userSetting = new UserMenu();
		userSetting.listUsers();
		dataPanel.add(userSetting);
		
		
	}
//	TODO TODO
//	@UiHandler("workButton")
//	void onWorkButtonClick(ClickEvent event) {
//		StockSearch stockSearch = new StockSearch();
//		final PopupPanel pPanel = new PopupPanel();
//		
//		
//		
//		pPanel.add(stockSearch);
//		pPanel.center();
//		pPanel.setModal(true);
//	}
//	@UiHandler("newItem")
//	void onNewItemClick(ClickEvent event) {
//		dataPanel.clear();
//		StockItemEntry stockItemEntry = new StockItemEntry();
//		dataPanel.add(stockItemEntry);
//	}
}

