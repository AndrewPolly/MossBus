package com.mossbuss.webapp.client.ui.dash;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.ui.students.studentEdit;
import com.mossbuss.webapp.client.ui.students.studentSearch;
import com.mossbuss.webapp.client.ui.tripSheet.TripSheetEdit;
import com.mossbuss.webapp.client.ui.tripSheet.TripSheetSearch;
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
						customerEdit.setClientDetails(studentSearch.getClient());
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
								greetingService.saveClient(customerEdit.getClientDetails(), new AsyncCallback<ClientDTO>() {

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
						customerEdit.getAddStudentButton().addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								greetingService.saveStudent(customerEdit.getStudentDetails(), new AsyncCallback<StudentDTO>() {
									@Override
									public void onFailure(Throwable caught) {
										//TODO FIX THIS:
										//errorLabel.setText(caught.getMessage());
									}
									@Override
									public void onSuccess(StudentDTO result) {
										studentSearch.setStudent(result);
									}
								});
								pPanel.hide();
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
			
			@Override
			public void onClick(ClickEvent event) {
				final TripSheetSearch tripSheetSearch = new TripSheetSearch();
				tripSheetSearch.getCancelButton().setVisible(false);
				tripSheetSearch.getSelectButton().setText("Print");
				
//				tripSheetSearch.getSelectButton().addClickHandler(new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						//customerSearch.setVisible(false);
//						final TripSheetEdit tripSheetEdit = new TripSheetEdit();
//						tripSheetEdit.setTripSheet(tripSheetSearch.getTripSheet());
//						final PopupPanel pPanel = new PopupPanel();
//						tripSheetEdit.getCancelButton().addClickHandler(new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								pPanel.hide();
//								//customerSearch.setVisible(true);
//							}
//						});
//						
//						tripSheetEdit.getSaveButton().addClickHandler(new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								greetingService.saveTripSheet(tripSheetEdit.getTripSheet(), new AsyncCallback<TripSheetDTO>() {
//
//									@Override
//									public void onFailure(Throwable caught) {
//										// TODO Fix This:
//										//errorLabel.setText(caught.getMessage());
//									}
//
//									@Override
//									public void onSuccess(TripSheetDTO result) {
//										tripSheetSearch.setTripSheet(result);
//									}
//								});
//								pPanel.hide();
//								//customerSearch.setVisible(true);
//							}
//						});
//						
//						pPanel.add(tripSheetEdit);
//						pPanel.setModal(true);
//						pPanel.center();
//						
//					}
//				});
				
				tripSheetSearch.getTripView().getPrintButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.setTitle("Trip Sheet for: " + tripSheetSearch.getTripSheet().getDriverName());
						tripSheetSearch.getTripView().getPrintButton().setVisible(false);
						
						navMenu.setVisible(false);
						Window.print();
						tripSheetSearch.getTripView().getPrintButton().setVisible(true);
						Window.setTitle("Ticket App");
						navMenu.setVisible(true);
						
						
						//customerSearch.setVisible(true);
					}
				});
				
	
				dataPanel.clear();
				dataPanel.add(tripSheetSearch);
				
				
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
