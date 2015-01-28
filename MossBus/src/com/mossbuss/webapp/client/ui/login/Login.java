package com.mossbuss.webapp.client.ui.login;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.ui.dash.Dash;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class Login extends Composite {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	DriverDTO driverDTO;
	@UiField Button loginButton;
	@UiField Label errorLabel;
	@UiField TextBox userNameText;
	@UiField PasswordTextBox userPasswordText;

	interface LoginUiBinder extends UiBinder<Widget, Login> {
	}

	public Login() {
		initWidget(uiBinder.createAndBindUi(this));
		userPasswordText.addKeyUpHandler(new KeyUpHandler() {	
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doLogin();
				}
			}
		});
	}

	@UiHandler("loginButton")
	void onLoginButtonClick(ClickEvent event) {
		doLogin();
	}
	
	private void doLogin() {
		//RootPanel.get("errorLabelContainer").clear();
		//errorLabel.setText("");
		driverDTO = new DriverDTO();
		///*
		String userName = userNameText.getText();
		String password = userPasswordText.getText();
		driverDTO.setEmailAddress(userName);
		driverDTO.setPassword(password);
		
		greetingService.doLogin(driverDTO, new AsyncCallback<DriverDTO>() {
			public void onFailure(Throwable caught) {
				errorLabel.setText(caught.getMessage());
			}

			public void onSuccess(DriverDTO result) {
				//save cookie
				Date now = new Date();
				now.setTime(now.getTime() + 365 * 24 * 60 * 60 * 1000);
				Cookies.setCookie("MossBuss_UserName", result.getEmailAddress(), now);
				
				//goto dash
				Dash dashPanel = new Dash();
				dashPanel.setOnlineUser(result);
				RootPanel.get("appContainer").clear();
				RootPanel.get("appContainer").add(dashPanel);
				//dashPanel.setOnlineUser(result);
				//dashPanel.setWSFocus();
			}
		});
	}
	
	public void setFocus() {
		userNameText.setText("Email Address");
		userNameText.setFocus(true);
		userNameText.selectAll();
		if(Cookies.getCookie("MossBuss_UserName")!= null){
			userNameText.setText(Cookies.getCookie("MossBuss_UserName"));
			userPasswordText.setFocus(true);

		}
	}
}
