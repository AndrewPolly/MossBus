package com.mossbuss.webapp.client.ui.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.mossbuss.webapp.client.dto.DriverDTO;

public class UserDetailPanel extends Composite {

	private static UserDetailPanelUiBinder uiBinder = GWT.create(UserDetailPanelUiBinder.class);
	private DriverDTO driver;
	@UiField Button editButton;
	@UiField Button deleteButton;
	@UiField Label usernameLabel;

	interface UserDetailPanelUiBinder extends UiBinder<Widget, UserDetailPanel> {
	}

	public UserDetailPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DriverDTO getUser() {
		return driver;
	}

	public void setUser(DriverDTO driver) {
		this.driver = driver;
		usernameLabel.setText(driver.getEmailAddress());
		
	}

}
