package com.mossbuss.webapp.client.ui.dash;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;

public class NavMenu extends Composite {

	private static NavMenuUiBinder uiBinder = GWT.create(NavMenuUiBinder.class);
	@UiField Button maintenanceMenu;
	@UiField Button tripSheetMenu;
	@UiField Button studentsMenu;
	@UiField Button driversMenu;

	interface NavMenuUiBinder extends UiBinder<Widget, NavMenu> {
	}

	public NavMenu() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
