package com.mossbuss.webapp.client.ui.dash;

import java.io.IOException;

import org.marre.SmsSender;
import org.marre.sms.SmsException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.ui.driver.DriverView;
import com.mossbuss.webapp.client.ui.maintenanceRecords.BusView;
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
	@UiField Label greetingLabel;
	interface DashUiBinder extends UiBinder<Widget, Dash> {
	}
	public Dash() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	public void init() {
		
		navMenu.setStyleName("navButton");
		navMenu.maintenanceMenu.setStyleName("navButton");
		navMenu.studentsMenu.setStyleName("navButton");
		navMenu.tripSheetMenu.setStyleName("navButton");
//		initWidget(uiBinder.createAndBindUi(this));
//		navMenu.setStyleName("navMenu");
		
		//check database for people that owe money.. this 
		//must be moved server side with a timer.
		// its actually just pathetic..
		if (onlineUser.getAdmin()) {
			greetingService.checkClientsForSms(new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Void result) {
				System.out.println("Succesfully checked clients");
					
				}
				
				
			});
			
			
		}
	
		greetingLabel.setText("Welcome " + onlineUser.getName());
		navMenu.maintenanceMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				BusView busView = new BusView();
				busView.init();
				dataPanel.clear();
				dataPanel.add(busView);

			}
		});
		
		navMenu.studentsMenu.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				final studentSearch studentSearch = new studentSearch();
				studentSearch.getCancelButton().setVisible(false);

				dataPanel.clear();
				dataPanel.add(studentSearch);
			}
		});
		
		navMenu.tripSheetMenu.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				resetNavMenuStyle();
//				navMenu.tripSheetMenu.setStyleName("navMenuSelected");
				final TripSheetSearch tripSheetSearch = new TripSheetSearch();
				tripSheetSearch.getCancelButton().setVisible(false);
				tripSheetSearch.getSelectButton().setText("Print");
				tripSheetSearch.getTripView().initTripSheetButtons();
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
				DriverView driverView = new DriverView();
				driverView.init();
				dataPanel.clear();
				dataPanel.add(driverView);
//				resetNavMenuStyle();
//				navMenu.driversMenu.setStyleName("navMenuSelected");
			}
		});
		
	
		
	}
	public void resetNavMenuStyle() {
		navMenu.driversMenu.setStyleName("navMenu");
		navMenu.maintenanceMenu.setStyleName("navMenu");
		navMenu.studentsMenu.setStyleName("navMenu");
		navMenu.tripSheetMenu.setStyleName("navMenu");
		
	}
	public DriverDTO getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(DriverDTO onlineUser) {
		this.onlineUser = onlineUser;
		System.out.println("set online user, user name is: " + onlineUser.getName() + "user admin status: " + onlineUser.getAdmin());
//		navMenu.adminMenu.setVisible(onlineUser.getAdmin());
	}
}
