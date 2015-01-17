package com.mossbuss.webapp.client.ui.admin;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.DriverDTO;

public class UserMenu extends Composite {

	private static UserMenuUiBinder uiBinder = GWT.create(UserMenuUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	@UiField VerticalPanel userDataPanel;
	@UiField Button newButton;

	interface UserMenuUiBinder extends UiBinder<Widget, UserMenu> {
	}

	public UserMenu() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("newButton")
	void onNewButtonClick(ClickEvent event) {
		final UserEditor userEdit = new UserEditor();
		final PopupPanel pPanel = new PopupPanel();
		userEdit.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(userEdit.validateInput()){
					greetingService.saveDriver(userEdit.getDriverdetails(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
							
						}

						@Override
						public void onSuccess(Void result) {
							listUsers();
							pPanel.hide();
						}
					});
				}
			}
		});
		userEdit.cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				pPanel.hide();
			}
		});
		pPanel.add(userEdit);
		pPanel.center();
		pPanel.setModal(true);
	}
	public void listUsers() {
		userDataPanel.clear();
		greetingService.listDrivers("", new AsyncCallback<ArrayList<DriverDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<DriverDTO> result) {
				for(int i = 0; i< result.size(); i++){
					final UserDetailPanel details = new UserDetailPanel();
					details.setUser(result.get(i));
					details.deleteButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if(Window.confirm("Are you sure you want to delete this user?")){
								//TODO: delete User
							}
							
						}
					});
					details.editButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							final UserEditor userEdit = new UserEditor();
							userEdit.setUserdetails(details.getUser());
							final PopupPanel pPanel = new PopupPanel();
							userEdit.getSaveButton().addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									if(userEdit.validateInput()){
										greetingService.saveDriver(userEdit.getDriverdetails(), new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												Window.alert(caught.getMessage());
												
											}

											@Override
											public void onSuccess(Void result) {
												listUsers();
												pPanel.hide();
											}
										});
									}
								}
							});
							userEdit.cancelButton.addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									pPanel.hide();
								}
							});
							pPanel.add(userEdit);
							pPanel.center();
							pPanel.setModal(true);
							
						}
					});
					userDataPanel.add(details);
				}
				
			}
		});
		
	}
}
