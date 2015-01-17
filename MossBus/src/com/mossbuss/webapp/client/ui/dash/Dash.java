package com.mossbuss.webapp.client.ui.dash;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.DriverDTO;

import com.mossbuss.webapp.client.ui.admin.AdminSettings;


import com.google.gwt.user.client.ui.VerticalPanel;

public class Dash extends Composite {

	private static DashUiBinder uiBinder = GWT.create(DashUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private DriverDTO onlineUser;
	@UiField VerticalPanel dataPanel;
	@UiField NavMenu navMenu;

	interface DashUiBinder extends UiBinder<Widget, Dash> {
	}

	public Dash() {
//		initWidget(uiBinder.createAndBindUi(this));
//		
//		navMenu.salesMenu.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				OrderSearch orderSearch = new OrderSearch();
//				orderSearch.getNewSaleButton().addClickHandler(new ClickHandler() {
//					
//					@Override
//					public void onClick(ClickEvent event) {
//						QuoteEdit quoteEdit = new QuoteEdit();
//						quoteEdit.setOnlinUser(getOnlineUser());
//						dataPanel.clear();
//						dataPanel.add(quoteEdit);
//					}
//				});
//				dataPanel.clear();
//				dataPanel.add(orderSearch);
//			}
//		});
//		
//		navMenu.customersMenu.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				final CustomerSearch customerSearch = new CustomerSearch();
//				customerSearch.getCancelButton().setVisible(false);
//				customerSearch.getSelectButton().setText("Edit");
//				customerSearch.getSelectButton().addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						//customerSearch.setVisible(false);
//						final CustomerEdit customerEdit = new CustomerEdit();
//						customerEdit.setCustomerDetails(customerSearch.getCustomer());
//						final PopupPanel pPanel = new PopupPanel();
//						customerEdit.getCancelButton().addClickHandler(new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								pPanel.hide();
//								//customerSearch.setVisible(true);
//							}
//						});
//						customerEdit.getSaveButton().addClickHandler(new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								greetingService.saveCustomer(customerEdit.getCustomerDetails(), new AsyncCallback<CustomerDTO>() {
//
//									@Override
//									public void onFailure(Throwable caught) {
//										// TODO Fix This:
//										//errorLabel.setText(caught.getMessage());
//									}
//
//									@Override
//									public void onSuccess(CustomerDTO result) {
//										customerSearch.setCustomer(result);
//									}
//								});
//								pPanel.hide();
//								//customerSearch.setVisible(true);
//							}
//						});
//						pPanel.add(customerEdit);
//						pPanel.setModal(true);
//						pPanel.center();
//						customerSearch.init();
//					}
//				});
//				dataPanel.clear();
//				dataPanel.add(customerSearch);
//			}
//		});
//		
//		navMenu.stockCheckMenu.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				StockLookup stockView = new StockLookup();
//				dataPanel.clear();
//				dataPanel.add(stockView);
//				stockView.init();
//			}
//		});
		
//		navMenu.workshopMenu.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				StockRequest stockRequest = new StockRequest();
//				dataPanel.clear();
//				dataPanel.add(stockRequest);
//			}
//		});
		
//		navMenu.storesMenu.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				StockReceiving stockReceiving = new StockReceiving();
//				dataPanel.clear();
//				dataPanel.add(stockReceiving);
//			}
//		});
//		
		navMenu.adminMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AdminSettings adminSettingsPannel = new AdminSettings();
				dataPanel.clear();
				dataPanel.add(adminSettingsPannel);
			}
		});
		
	}

	public DriverDTO getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(DriverDTO onlineUser) {
		this.onlineUser = onlineUser;
		navMenu.adminMenu.setVisible(onlineUser.getAdmin());
	}
}
