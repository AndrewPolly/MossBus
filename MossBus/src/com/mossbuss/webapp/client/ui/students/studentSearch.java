package com.mossbuss.webapp.client.ui.students;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Label;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;

@SuppressWarnings("deprecation")
public class studentSearch extends Composite {

	private static studentSearchUiBinder uiBinder = GWT
			.create(studentSearchUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	MultiWordSuggestOracle contactNameOracle;

	private ClientDTO client;
	private StudentDTO student;
	@UiField
	Button newCustomerButton;
	@UiField
	studentView customerViewPanel;
	@UiField
	Button cancelButton;
	@UiField
	SuggestBox contactNameText;
	@UiField
	Label errorLabel;

	interface studentSearchUiBinder extends UiBinder<Widget, studentSearch> {
	}

	public studentSearch() {
		initWidget(uiBinder.createAndBindUi(this));
		customerViewPanel.setVisible(false);
		// customerViewPanel.getCloseButton().setText("Select");
		contactNameOracle = (MultiWordSuggestOracle) contactNameText
				.getSuggestOracle();
		ContactChangeHandler contactChangeHandler = new ContactChangeHandler();
		ContactSuggestionSelectionHandler contactSuggestionHandler = new ContactSuggestionSelectionHandler();
		contactNameText.addKeyDownHandler(contactChangeHandler);
		contactNameText.addEventHandler(contactSuggestionHandler);
	}

	public void init() {
		contactNameText.setFocus(true);
	}

	public ClientDTO getClient() {
		return client;
	}

	public void setCustomer(ClientDTO client) {
		this.client = client;
		customerViewPanel.setCustomerDetails(client);
		customerViewPanel.setVisible(true);
	}

	public StudentDTO getStudent() {
		return student;
	}

	public void setStudent(StudentDTO Student) {
		this.student = Student;
		greetingService.getClient(student.getParentID(),
				new AsyncCallback<ClientDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText(caught.getMessage());
					}

					@Override
					public void onSuccess(ClientDTO result) {
						customerViewPanel.setCustomerDetails(result);
					}
				});

	}

	@UiHandler("newCustomerButton")
	void onNewCustomerButtonClick(ClickEvent event) {
		final studentEdit studentEdit = new studentEdit();
		final PopupPanel pPanel = new PopupPanel();
		studentEdit.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pPanel.hide();
			}
		});
		studentEdit.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorLabel.setText("");
				greetingService.saveClient(studentEdit.getClientDetails(),
						new AsyncCallback<ClientDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								errorLabel.setText(caught.getMessage());
							}

							@Override
							public void onSuccess(ClientDTO result) {
								setCustomer(result);
							}
						});
				pPanel.hide();
			}
		});
		pPanel.add(studentEdit);
		pPanel.setModal(true);
		pPanel.center();
	}

	public Button getCancelButton() {
		return cancelButton;
	}

//	public Button getSelectButton() {
//		return customerViewPanel.getCloseButton();
//	}

	class ContactChangeHandler implements KeyDownHandler {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			errorLabel.setText("");
			if (contactNameText.getText().length() > 1) {
				ArrayList<String> wordList = new ArrayList<String>();
				String[] words = contactNameText.getText().split(" ");
				for (String word : words) {
					System.out.println(word);
					wordList.add(word);
				}
				String sql = "select id, Address, StudentName from student where StudentName Like '%"
						+ wordList.get(0) + "%'";

				System.out.println(sql);
				if (event.isAltKeyDown() || event.isControlKeyDown()
						|| event.isDownArrow() || event.isLeftArrow()
						|| event.isRightArrow() | event.isUpArrow()) {
					// do nothing
				} else {
					try {
						updateContactNameOracle(sql);
					} catch (Exception e) {
						errorLabel.setText(e.getMessage());
						System.out.println(sql);
						System.out.println("Error: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void updateContactNameOracle(ArrayList<String> newList) {
		contactNameOracle.clear();
		for (int i = 0; i < newList.size(); i++) {
			contactNameOracle.add(newList.get(i));
		}
	}

	public void updateContactNameOracle(String sql) throws Exception {
		greetingService.updateContactNameOracle(sql,
				new AsyncCallback<ArrayList<String>>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(ArrayList<String> result) {
						updateContactNameOracle(result);
						contactNameText.showSuggestionList();
					}
				});
	}

	class ContactSuggestionSelectionHandler implements SuggestionHandler {
		@Override
		public void onSuggestionSelected(SuggestionEvent event) {
			errorLabel.setText("");
			// Get all Customer details for that company
			String suggestion = event.getSelectedSuggestion()
					.getDisplayString();
			suggestion = suggestion.replaceAll("\\<.*?>", "");
			suggestion = suggestion.trim();
			int endIndex = suggestion.indexOf(" :: ");
			suggestion = suggestion.substring(0, endIndex);
			int selectedID = Integer.parseInt(suggestion);
			greetingService.getStudent(selectedID,
					new AsyncCallback<StudentDTO>() {

						@Override
						public void onSuccess(StudentDTO result) {
							// setCustomer(result);
							// errorLabel.setText("");
							// customerViewPanel.setVisible(true);

							greetingService.getClient(result.getParentID(),
									new AsyncCallback<ClientDTO>() {

										@Override
										public void onSuccess(ClientDTO resultClient) {
											setCustomer(resultClient);
											errorLabel.setText("");
											customerViewPanel.setVisible(true);
										}

										@Override
										public void onFailure(Throwable caught) {
											errorLabel.setText(caught
													.getMessage());
										}
									});
						}

						@Override
						public void onFailure(Throwable caught) {
							errorLabel.setText(caught.getMessage());
						}
					});
		}
	}
}
