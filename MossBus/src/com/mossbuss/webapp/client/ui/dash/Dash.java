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
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.ui.students.studentEdit;
import com.mossbuss.webapp.client.ui.students.studentSearch;
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
		initWidget(uiBinder.createAndBindUi(this));
		
		navMenu.maintenanceMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
			}
		});
		
		navMenu.studentsMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final studentSearch studentSearch = new studentSearch();
				studentSearch.getCancelButton().setVisible(false);
				studentSearch.getSelectButton().setText("Edit");
				studentSearch.getSelectButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//customerSearch.setVisible(false);
						final studentEdit customerEdit = new studentEdit();
						customerEdit.setCustomerDetails(studentSearch.getCustomer());
						final PopupPanel pPanel = new PopupPanel();
						customerEdit.getCancelButton().addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								pPanel.hide();
								//customerSearch.setVisible(true);
							}
						});
						customerEdit.getSaveButton().addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								greetingService.saveStudent(customerEdit.getStudentDetails(), new AsyncCallback<ClientDTO>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Fix This:
										//errorLabel.setText(caught.getMessage());
									}

									@Override
									public void onSuccess(ClientDTO result) {
										studentSearch.setCustomer(result);
									}
								});
								pPanel.hide();
								//customerSearch.setVisible(true);
							}
						});
						pPanel.add(customerEdit);
						pPanel.setModal(true);
						pPanel.center();
						studentSearch.init();
					}
				});
				dataPanel.clear();
				dataPanel.add(studentSearch);
			}
		});
		
		navMenu.tripSheetMenu.addClickHandler(new ClickHandler() {
			//the view for the students... StudentEdit is where students
			// will be added.
			@Override
			public void onClick(ClickEvent event) {
//				studentEdit studentView = new studentEdit();
//				dataPanel.clear();
//				dataPanel.add(studentView);
//				studentView.init();
			}
		});
		
		navMenu.driversMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				StockRequest stockRequest = new StockRequest();
//				dataPanel.clear();
//				dataPanel.add(stockRequest);
			}
		});
		
	
		
	}

	public DriverDTO getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(DriverDTO onlineUser) {
		this.onlineUser = onlineUser;
//		navMenu.adminMenu.setVisible(onlineUser.getAdmin());
	}
}
