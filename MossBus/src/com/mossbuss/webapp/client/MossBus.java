package com.mossbuss.webapp.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mossbuss.webapp.client.ui.login.Login;



public class MossBus implements EntryPoint {

  private VerticalPanel tripSheetPanel = new VerticalPanel();
  private VerticalPanel passwordMainPanel = new VerticalPanel();
  private VerticalPanel menuVerticalPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private FlexTable LoginData = new FlexTable();
  private HorizontalPanel studentListPanel = new HorizontalPanel();
  private HorizontalPanel passwordInputPanel = new HorizontalPanel();
  private HorizontalPanel menuHorizontalPanel = new HorizontalPanel();

  private TextBox StudentName = new TextBox();
  private TextBox ParentName = new TextBox();
  private TextBox ParentCell = new TextBox();
  private TextBox ParentEmail = new TextBox();
  private TextBox UserName = new TextBox();
  private PasswordTextBox passWord = new PasswordTextBox();
  private Button addStockButton = new Button("Add");
  private Button gotoTripSheet = new Button("Trip Sheets");
  private Button gotoBusMaintenance = new Button("Bus Maintenance");
  private Label lastUpdatedLabel = new Label();

  
  private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
  //private ArrayList<ArrayList<String>> Clients = new ArrayList<ArrayList<String>>();  

  /**
   * Entry point method.
   */
  public void onModuleLoad() {
		if(com.google.gwt.user.client.Window.Location.getParameter("reset")!= null && 
				com.google.gwt.user.client.Window.Location.getParameter("reset") == "007"){
			RootPanel.get("appContainer").clear();
			RootPanel.get("appContainer").add(new Label("Resetting Database"));
			greetingService.resetDatabase("Reset", new AsyncCallback<Void>(){
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error! " + caught.getMessage());
				}

				@Override
				public void onSuccess(Void result) {
					Window.alert("Done!");
					
				}
			});
		}else {
			Login login = new Login();
			RootPanel.get("appContainer").setStyleName("appContainerStyle");
			RootPanel.get("appContainer").add(login);
			login.setFocus();
		}
	}
//  public void onModuleLoad() {
//	  
//	  
//	// Login start
//	LoginData.setText(0, 0, "User Name");
//	LoginData.setText(0, 1, "Password");
//	passwordInputPanel.add(UserName);
//	passwordInputPanel.add(passWord);
//	passwordMainPanel.add(LoginData);
//	passwordMainPanel.add(passwordInputPanel);
//	RootPanel.get("passwordPanel").add(passwordMainPanel);
//	UserName.setFocus(true);
//	waitForPass();
//	
//	// Set up menu at the top.
//	menuHorizontalPanel.add(gotoTripSheet);
//	menuHorizontalPanel.add(gotoBusMaintenance);
//	menuVerticalPanel.add(menuHorizontalPanel);
//    // Create table for Student data.
//	stocksFlexTable.setText(0, 0, "Account #");
//    stocksFlexTable.setText(0, 1, "Student");
//    stocksFlexTable.setText(0, 2, "Account Holder");
//    stocksFlexTable.setText(0, 3, "Cell Phone #");
//    stocksFlexTable.setText(0, 4, "Email Address");
//    stocksFlexTable.setText(0, 5, "Remove Account");
//    stocksFlexTable.setCellPadding(6);
//    // Add styles to elements in the table
//    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
//    stocksFlexTable.addStyleName("watchList");
//    // Assemble Add Stock panel.
//    studentListPanel.add(StudentName);
//    studentListPanel.add(ParentName);
//    studentListPanel.add(ParentCell);
//    studentListPanel.add(ParentEmail);
//    studentListPanel.add(addStockButton);
//
//    // Assemble Main panel.
//    tripSheetPanel.add(stocksFlexTable);
//    tripSheetPanel.add(studentListPanel);
//    tripSheetPanel.add(lastUpdatedLabel);
//    
//    // Associate the Main panel with the HTML host page.
//    RootPanel.get("studentList").add(tripSheetPanel);
//    RootPanel.get("studentList").clear();
//    // Move cursor focus to the StudentName text box.
//    StudentName.setFocus(true);
//    
//    // Listen for mouse events on the Add button.
//    addStockButton.addClickHandler(new ClickHandler() {
//      public void onClick(ClickEvent event) {
//        addStudent();
//      }
//    });
//    
//    
//    // Listen for keyboard events in the input box.
//    StudentName.addKeyDownHandler(new KeyDownHandler() {
//      public void onKeyDown(KeyDownEvent event) {
//        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//          ParentName.setFocus(true);
//        }
//      }
//    });
//    ParentName.addKeyDownHandler(new KeyDownHandler() {
//      public void onKeyDown(KeyDownEvent event) {
//        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//          ParentCell.setFocus(true);
//        }
//      }
//    });
//    ParentCell.addKeyDownHandler(new KeyDownHandler() {
//        public void onKeyDown(KeyDownEvent event) {
//          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//            ParentEmail.setFocus(true);
//          }
//        }
//      });
//    ParentEmail.addKeyDownHandler(new KeyDownHandler() {
//        public void onKeyDown(KeyDownEvent event) {
//          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//            addStudent();
//          }
//        }
//      });
//  }
//  private void waitForPass() {
//	  UserName.addKeyDownHandler(new KeyDownHandler() {
//	        public void onKeyDown(KeyDownEvent event) {
//	          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//	            passWord.setFocus(true);
//	          }
//	        }
//	      });
//	  passWord.addKeyDownHandler(new KeyDownHandler() {
//	        public void onKeyDown(KeyDownEvent event) {
//	          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//	            checkPass();
//	          }
//	        }
//	      });
//}
protected void checkPass() {
	final String User = UserName.getText().trim();
	final String Password = passWord.getText().trim();
	if (!User.matches("^[0-9A-Za-z\\.]{1,15}$")) {
	      Window.alert("'" + User + "' is not a valid user name.");
	      UserName.selectAll();
	      return;
	} else if (!Password.matches("^[0-9A-Za-z\\.]{1,50}$")) {
	      Window.alert("Your password makes no sense, please use letters and numbers.");
	      passWord.selectAll();
	      return;
	}
	RootPanel.get("passwordPanel").clear();
	RootPanel.get("studentList").add(tripSheetPanel);
	RootPanel.get("menuPanel").add(menuVerticalPanel);
	//TODO check with server if username and password are correct
	// if username and password work then, give a choice for bus management or trip sheets.
}
/**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
   */
  private void addStudent() {
	  final String Student = StudentName.getText().trim();
	  final String Parent = ParentName.getText().trim();
	  final String CellNumber = ParentCell.getText().trim().replaceAll("\\s", "");
	  final String EmailAddress = ParentEmail.getText().trim();
	  
	  // Check to see if input is valid.
	  if (Student.matches("") || Parent.matches("") || CellNumber.matches("")) {
		  Window.alert("Please fill in all the required fields.");
		  if (Student.matches("")){ StudentName.setFocus(true); return;}
		  if (Parent.matches("")){ ParentName.setFocus(true); return;}
		  if (CellNumber.matches("")){ ParentCell.setFocus(true); return;}
		  
	      return;
	  } else if (!Student.matches("[A-Za-z]+")) {
		  Window.alert("'" + Student + "' is not a valid student name.");
		  StudentName.selectAll();
	      return;
	  } else if (!Parent.matches("[A-Za-z]+")) {
		  Window.alert("'" + Parent + "' is not a valid parent name.");
		  ParentName.selectAll();
	      return;
	  } else if (!CellNumber.matches("[0-9]{10}") && !CellNumber.matches("\\+?[0-9]{11}")) {
		  Window.alert("'" + CellNumber + "' is not a valid cell phone number.");
		  ParentCell.selectAll();
	      return;
	  } else if (!EmailAddress.matches("") && !EmailAddress.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$") ) {
		  Window.alert("'" + EmailAddress + "' is not a valid EmailAddress.");
		 ParentEmail.selectAll();
	      return;
	  }
	  StudentName.setText("");
	  ParentName.setText("");
	  ParentCell.setText("");
	  ParentEmail.setText("");
	  // TODO Add the information to the database.
  }

}